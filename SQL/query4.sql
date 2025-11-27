-- 4. Top-5 block IDs by total actions per day
SELECT DATE(d.ts) AS day, d.block_id, COUNT(*) AS actions
FROM data_receiver_log d
WHERE d.ts BETWEEN :start_date AND :end_date
  AND d.block_id IS NOT NULL
GROUP BY day, d.block_id
ORDER BY actions DESC
LIMIT 5;
