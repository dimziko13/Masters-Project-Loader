-- 13. IPs that issued any four distinct HTTP methods in a time range
SELECT ip.ip
FROM access_log a
JOIN http_method m ON m.method_id = a.method_id
JOIN ip_address ip ON ip.ip_id = a.ip_id
WHERE a.ts BETWEEN :start_time AND :end_time
GROUP BY ip.ip
HAVING COUNT(DISTINCT m.method) >= 4
ORDER BY ip.ip;
