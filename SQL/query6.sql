-- 6. 2nd-most-common resource requested
SELECT path, request_count FROM (
    SELECT r.path, COUNT(*) AS request_count,
           ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) AS rn
    FROM access_log a
    JOIN resource r ON r.resource_id = a.resource_id
    GROUP BY r.path
) q
WHERE rn = 2;
