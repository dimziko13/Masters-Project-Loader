-- 10. Access logs with specific Firefox version
SELECT a.*
FROM access_log a
JOIN user_agent ua ON ua.agent_id = a.agent_id
WHERE ua.agent_string LIKE '%' || :version || '%';
