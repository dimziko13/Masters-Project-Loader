-- 9. Blocks replicated same day & hour they were served
WITH served AS (
    SELECT DATE(d.ts) AS day, EXTRACT(HOUR FROM d.ts) AS hour, d.block_id
    FROM data_receiver_log d
),
replicated AS (
    SELECT DATE(n.ts) AS day, EXTRACT(HOUR FROM n.ts) AS hour, neb.block_id
    FROM namesystem_event n
    JOIN ns_event_block neb ON neb.ns_event_id = n.ns_event_id
)
SELECT s.day, s.hour, s.block_id
FROM served s
JOIN replicated r ON r.day = s.day AND r.hour = s.hour AND r.block_id = s.block_id
ORDER BY s.day, s.hour, s.block_id;
