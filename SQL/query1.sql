-- 1. Total logs per type in time range (descending)
SELECT l.type, COUNT(*) AS total_logs
FROM (
    SELECT 'access' AS type, ts FROM access_log
    UNION ALL
    SELECT 'dr' AS type, ts FROM data_receiver_log
    UNION ALL
    SELECT 'ns' AS type, ts FROM namesystem_event
) l
WHERE l.ts BETWEEN :start_time AND :end_time
GROUP BY l.type
ORDER BY total_logs DESC;
