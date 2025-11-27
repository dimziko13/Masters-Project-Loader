-- 5. Referrers that lead to more than one resource
SELECT r.referrer_url, COUNT(DISTINCT a.resource_id) AS resource_count
FROM referrer r
JOIN access_log a ON a.referrer_id = r.referrer_id
GROUP BY r.referrer_url
HAVING COUNT(DISTINCT a.resource_id) > 1
ORDER BY resource_count DESC;
