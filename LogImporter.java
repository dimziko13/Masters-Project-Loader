package com.example.mastersapp;

import com.example.mastersapp.entities.*;
import com.example.mastersapp.repository.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogImporter {

    private static final int BATCH_SIZE = 500;

    @Autowired private IpAddressRepository ipAddressRepository;
    @Autowired private HttpMethodRepository httpMethodRepository;
    @Autowired private HttpStatusRepository httpStatusRepository;
    @Autowired private ReferrerRepository referrerRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private UserAgentRepository userAgentRepository;
    @Autowired private AccessLogRepository accessLogRepository;

    @Autowired private BlockRepository blockRepository;
    @Autowired private DataReceiverLogRepository dataReceiverLogRepository;
    @Autowired private DataTypeRepository dataTypeRepository;
    @Autowired private NamesystemEventRepository namesystemEventRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // simple caches to avoid repeated SELECTs
    private final Map<String, IpAddress> ipCache = new HashMap<>();
    private final Map<String, Block> blockCache = new HashMap<>();
    private final Map<String, DataType> dataTypeCache = new HashMap<>();

    // ------------------------------------------------------------
    // PUBLIC ENTRY POINT (orchestrator)
    // ------------------------------------------------------------

    /**
     * Called from StartupRunner. Paths are the three log files.
     */
    public void importAll(String accessPath, String drPath, String nsPath) {
        System.out.println("=== START IMPORT ===");

        try {
            importNamesystemLogsTx(nsPath);
        } catch (Exception e) {
            System.err.println("NAMESYSTEM LOG import failed");
            e.printStackTrace();
        }
        
        
        
        try {
            importAccessLogsTx(accessPath);
        } catch (Exception e) {
            System.err.println("ACCESS LOG import failed — continuing with DR and NS");
            e.printStackTrace();
        }

        try {
            importDataReceiverLogsTx(drPath);
        } catch (Exception e) {
            System.err.println("DATA RECEIVER LOG import failed — continuing with NS");
            e.printStackTrace();
        }

        

        System.out.println("=== END IMPORT ===");
    }

    // ===========================================================================================
    // ACCESS LOG IMPORT
    // ===========================================================================================

    private static final Pattern ACCESS_PATTERN = Pattern.compile(
        "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+\\[(.+?)\\]\\s+\"(.+?)\"\\s+(\\d{3})\\s+(\\S+)\\s+\"(.*?)\"\\s+\"(.*?)\"$"
    );

    @Transactional(rollbackFor = Exception.class)
    public void importAccessLogsTx(String path) throws IOException {
        System.out.println("Importing ACCESS logs (transactional) from: " + path);

        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    processAccessLine(line);
                    count++;
                    batchFlush(count);

                    if (count % 1000 == 0) {
                        System.out.println("[ACCESS] Imported " + count + " lines...");
                    }
                } catch (Exception e) {
                    System.err.println("[ACCESS] Failed line: " + line);
                    e.printStackTrace();
                    // if you want to skip bad lines instead of aborting the whole file, replace:
                    //throw e; // abort ACCESS import only
                    // with:
                    // continue;
                }
            }
        }

        System.out.println("[ACCESS] Transaction complete, imported: " + count);
    }

    private void processAccessLine(String line) throws ParseException {

        Matcher m = ACCESS_PATTERN.matcher(line);
        if (!m.matches()) throw new RuntimeException("Invalid access log: " + line);

        String ipStr       = m.group(1);
        String remoteIdent = m.group(2);
        String remoteUser  = m.group(3);
        String tsStr       = m.group(4);
        String requestLine = m.group(5);
        int statusCode     = Integer.parseInt(m.group(6));
        String sizeStr     = m.group(7);
        String refStr      = m.group(8);
        String uaStr       = m.group(9);

        Timestamp ts = parseApacheTimestamp(tsStr);

        // Parse request line → METHOD and PATH
        String methodStr; //= "GET";
        String pathStr; //= "/";
        String[] parts = requestLine.split(" ");
        if (parts.length >= 2) {
            methodStr = parts[0];
            pathStr = parts[1];
        }
        else {
        	methodStr = "GET";
        	pathStr="/";
        	}

        Long size = sizeStr.equals("-") ? null : Long.parseLong(sizeStr);

        IpAddress ip = resolveIp(ipStr);
        HttpMethod method = httpMethodRepository.findByMethod(methodStr)
                .orElseGet(() -> httpMethodRepository.save(newMethod(methodStr)));
        HttpStatus status = httpStatusRepository.findByStatusCode(statusCode)
                .orElseGet(() -> httpStatusRepository.save(newStatus(statusCode)));

        Referrer ref = null;
        if (!"-".equals(refStr)) {
            ref = referrerRepository.findByReferrerUrl(refStr)
                    .orElseGet(() -> referrerRepository.save(newReferrer(refStr)));
        }

        Resource res = resourceRepository.findByPath(pathStr)
                .orElseGet(() -> resourceRepository.save(newResource(pathStr)));

        UserAgent ua = userAgentRepository.findByAgentString(uaStr)
                .orElseGet(() -> userAgentRepository.save(newUserAgent(uaStr)));

        AccessLog log = new AccessLog();
        log.setTs(ts);
        log.setResponseSize(size);
        log.setIpAddress(ip);
        log.setHttpMethod(method);
        log.setHttpStatus(status);
        log.setReferrer(ref);
        log.setResource(res);
        log.setUserAgent(ua);

        accessLogRepository.save(log);
    }

    // ===========================================================================================
    // DATA RECEIVER LOG IMPORT
    // ===========================================================================================

    private static final Pattern DR_PATTERN = Pattern.compile(
        "^(\\d{6})\\s+(\\d{6})\\s+(\\d+)\\s+INFO\\s+dfs\\.DataNode\\$DataXceiver:\\s+Receiving block\\s+(blk_-?\\d+)\\s+src:\\s+/(\\d+\\.\\d+\\.\\d+\\.\\d+):\\d+\\s+dest:\\s+/(\\d+\\.\\d+\\.\\d+\\.\\d+):\\d+$"
    );

    @Transactional(rollbackFor = Exception.class)
    public void importDataReceiverLogsTx(String path) throws IOException {
        System.out.println("Importing DR logs (transactional) from: " + path);

        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    processDRLine(line);
                    count++;
                    batchFlush(count);

                    if (count % 1000 == 0)
                        System.out.println("[DR] Imported " + count + " lines...");

                } catch (Exception e) {
                    System.err.println("[DR] Failed line: " + line);
                    e.printStackTrace();
                    //throw e; // or continue; to skip bad DR lines
                }
            }
        }

        System.out.println("[DR] Transaction complete, imported: " + count);
    }

    private void processDRLine(String line) throws ParseException {
        Matcher m = DR_PATTERN.matcher(line);
        if (!m.matches()) throw new RuntimeException("Invalid DR log: " + line);

        String date = m.group(1);
        String time = m.group(2);
        Long size = Long.parseLong(m.group(3));
        String blockId = m.group(4);
        String srcIpStr = m.group(5);
        String dstIpStr = m.group(6);

        Timestamp ts = parseShortTimestamp(date, time);

        Block block = resolveBlock(blockId);
        IpAddress src = resolveIp(srcIpStr);
        IpAddress dst = resolveIp(dstIpStr);
        DataType type = resolveDataType("DATA_RECEIVER");

        DataReceiverLog dr = new DataReceiverLog();
        dr.setTs(ts);
        dr.setSizeBytes(size);
        dr.setBlock(block);
        dr.setIpAddress2(src); // src_ip_id
        dr.setIpAddress1(dst); // dst_ip_id
        dr.setDataType(type);

        dataReceiverLogRepository.save(dr);
    }

    // ===========================================================================================
    // NAME SYSTEM EVENT IMPORT (full NS handling)
    // ===========================================================================================

    // Pattern A: replicate – src + multiple dst IPs + block
    private static final Pattern NS_REPLICATE_PATTERN = Pattern.compile(
        "^(\\d{6})\\s+(\\d{6})\\s+(\\d+)\\s+INFO\\s+dfs\\.FSNamesystem:.*?ask\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+):\\d+.*?\\b(blk_-?\\d+)\\b.*?datanode\\(s\\)\\s+((?:\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+(?:\\s+)?)+)$"
    );

    // Pattern B: addStoredBlock – single IP + optional size + block
    private static final Pattern NS_ADDSTOR_PATTERN = Pattern.compile(
        "^(\\d{6})\\s+(\\d{6})\\s+(\\d+)\\s+INFO\\s+dfs\\.FSNamesystem:.*?(\\d+\\.\\d+\\.\\d+\\.\\d+):\\d+.*?\\b(blk_-?\\d+)\\b(?:\\s+size\\s+(\\d+))?.*$"
    );

    // Pattern C: allocateBlock – no IP, just block
    private static final Pattern NS_ALLOC_PATTERN = Pattern.compile(
        "^(\\d{6})\\s+(\\d{6})\\s+(\\d+)\\s+INFO\\s+dfs\\.FSNamesystem:.*?\\b(blk_-?\\d+)\\b.*$"
    );

    @Transactional(rollbackFor = Exception.class)
    public void importNamesystemLogsTx(String path) throws IOException {
        System.out.println("Importing NS logs (transactional) from: " + path);

        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    processNSLine(line);
                    count++;
                    batchFlush(count);

                    if (count % 1000 == 0)
                        System.out.println("[NS] Imported " + count + " lines...");

                } catch (Exception e) {
                    System.err.println("[NS] Failed line: " + line);
                    e.printStackTrace();
                    //throw e; // or continue; to skip bad NS lines
                }
            }
        }

        System.out.println("[NS] Transaction complete, imported: " + count);
    }

    private void processNSLine(String line) throws ParseException {

        Matcher m;

        // PATTERN A: replicate (src + multiple dst IPs)
        m = NS_REPLICATE_PATTERN.matcher(line);
        if (m.matches()) {
            handleNSReplicate(line, m);
            return;
        }

        // PATTERN B: addStoredBlock (single IP, optional size)
        m = NS_ADDSTOR_PATTERN.matcher(line);
        if (m.matches()) {
            handleNSAddStored(line, m);
            return;
        }

        // PATTERN C: allocateBlock (no IP)
        m = NS_ALLOC_PATTERN.matcher(line);
        if (m.matches()) {
            handleNSAllocate(line, m);
            return;
        }

        System.err.println("[NS] Unrecognized NS line: " + line);
    }

    private void handleNSReplicate(String rawLine, Matcher m) throws ParseException {

        String date = m.group(1);
        String time = m.group(2);
        Long metric = Long.parseLong(m.group(3));
        String srcIpStr = m.group(4);
        String blockId = m.group(5);
        String dstListStr = m.group(6);

        Timestamp ts = parseShortTimestamp(date, time);

        Block block = resolveBlock(blockId);
        DataType type = resolveDataType("NAMESYSTEM");
        IpAddress src = resolveIp(srcIpStr);

        String[] dstTokens = dstListStr.trim().split("\\s+");
        for (String tok : dstTokens) {
            String dstIpOnly = tok.split(":")[0];
            IpAddress dst = resolveIp(dstIpOnly);

            NamesystemEvent ns = new NamesystemEvent();
            ns.setTs(ts);
            ns.setSizeBytes(metric);
            ns.setDataType(type);
            ns.setIpAddress2(src); // src_ip_id
            ns.setIpAddress1(dst); // dst_ip_id

            ns.setBlocks(new ArrayList<>(Collections.singletonList(block)));

            namesystemEventRepository.save(ns);
        }
    }

    private void handleNSAddStored(String rawLine, Matcher m) throws ParseException {

        String date = m.group(1);
        String time = m.group(2);
        Long metric = Long.parseLong(m.group(3));
        String ipStr = m.group(4);
        String blockId = m.group(5);
        String sizeStr = m.group(6);

        Long size = (sizeStr != null) ? Long.parseLong(sizeStr) : metric;

        Timestamp ts = parseShortTimestamp(date, time);

        Block block = resolveBlock(blockId);
        IpAddress ip = resolveIp(ipStr);
        DataType type = resolveDataType("NAMESYSTEM");

        NamesystemEvent ns = new NamesystemEvent();
        ns.setTs(ts);
        ns.setSizeBytes(size);
        ns.setDataType(type);
        ns.setIpAddress1(ip);   // dst node
        ns.setIpAddress2(null); // no src known

        ns.setBlocks(new ArrayList<>(Collections.singletonList(block)));

        namesystemEventRepository.save(ns);
    }

    private void handleNSAllocate(String rawLine, Matcher m) throws ParseException {

        String date = m.group(1);
        String time = m.group(2);
        Long metric = Long.parseLong(m.group(3));
        String blockId = m.group(4);

        Timestamp ts = parseShortTimestamp(date, time);

        Block block = resolveBlock(blockId);
        DataType type = resolveDataType("NAMESYSTEM");

        NamesystemEvent ns = new NamesystemEvent();
        ns.setTs(ts);
        ns.setSizeBytes(metric);
        ns.setDataType(type);
        ns.setIpAddress1(null);
        ns.setIpAddress2(null);

        ns.setBlocks(new ArrayList<>(Collections.singletonList(block)));

        namesystemEventRepository.save(ns);
    }

    // ===========================================================================================
    // HELPER / RESOLVER METHODS
    // ===========================================================================================

    private IpAddress resolveIp(String ipStr) {
        return ipCache.computeIfAbsent(ipStr, ip -> {
            return ipAddressRepository.findByIp(ip).orElseGet(() -> {
                IpAddress x = new IpAddress();
                x.setIp(ip);
                return ipAddressRepository.save(x);
            });
        });
    }

    private Block resolveBlock(String blockId) {
        return blockCache.computeIfAbsent(blockId, id -> {
            return blockRepository.findById(id).orElseGet(() -> {
                Block b = new Block();
                b.setBlockId(id);
                return blockRepository.save(b);
            });
        });
    }

    private DataType resolveDataType(String name) {
        return dataTypeCache.computeIfAbsent(name, n -> {
            return dataTypeRepository.findByTypeName(n).orElseGet(() -> {
                DataType dt = new DataType();
                dt.setTypeName(n);
                return dataTypeRepository.save(dt);
            });
        });
    }

    private HttpMethod newMethod(String m) {
        HttpMethod x = new HttpMethod();
        x.setMethod(m);
        return x;
    }

    private HttpStatus newStatus(int code) {
        HttpStatus x = new HttpStatus();
        x.setStatusCode(code);
        return x;
    }

    private Referrer newReferrer(String url) {
        Referrer r = new Referrer();
        r.setReferrerUrl(url);
        return r;
    }

    private Resource newResource(String p) {
        Resource r = new Resource();
        r.setPath(p);
        return r;
    }

    private UserAgent newUserAgent(String s) {
        UserAgent ua = new UserAgent();
        ua.setAgentString(s);
        return ua;
    }

    private Timestamp parseApacheTimestamp(String s) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        return new Timestamp(df.parse(s).getTime());
    }

    private Timestamp parseShortTimestamp(String date, String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd HHmmss");
        return new Timestamp(df.parse(date + " " + time).getTime());
    }

    private void batchFlush(int count) {
        if (count % BATCH_SIZE == 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}
