-- 7. Access logs where size < specified number
SELECT *
FROM access_log
WHERE response_size < :max_size;
