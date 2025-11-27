-- 2. Total logs per day for a specific action type
SELECT DATE(ts) AS day, COUNT(*) AS total_logs
FROM access_log
WHERE method_id = :method_id
  AND ts BETWEEN :start_time AND :end_time
GROUP BY day
ORDER BY day;
