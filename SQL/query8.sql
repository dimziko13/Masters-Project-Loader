-- 8. Blocks replicated same day they were served
WITH served AS (
    SELECT DATE(d.ts) AS day, d.block_id
    FROM data_receiver_log d
    WHERE d.block_id IS NOT NULL
),
replicated AS (
    SELECT DATE(n.ts) AS day, neb.block_id
    FROM namesystem_event n
    JOIN ns_event_block neb ON neb.ns_event_id = n.ns_event_id
)
SELECT s.day, s.block_id
FROM served s
JOIN replicated r ON r.day = s.day AND r.block_id = s.block_id
ORDER BY s.day, s.block_id;
