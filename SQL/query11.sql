-- 11. IPs that issued a particular HTTP method in a time range
SELECT DISTINCT ip.ip
FROM access_log a
JOIN http_method m ON m.method_id = a.method_id
JOIN ip_address ip ON ip.ip_id = a.ip_id
WHERE m.method = :method
  AND a.ts BETWEEN :start_time AND :end_time
ORDER BY ip.ip;
