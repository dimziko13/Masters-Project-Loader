-- 3. Most common log per source IP for a specific day
SELECT ip.ip, COUNT(*) AS total
FROM access_log a
JOIN ip_address ip ON ip.ip_id = a.ip_id
WHERE DATE(a.ts) = :specific_day
GROUP BY ip.ip
ORDER BY total DESC
LIMIT 1;
