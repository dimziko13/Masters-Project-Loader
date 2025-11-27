package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the user_agent database table.
 * 
 */
@Entity
@Table(name="user_agent")
@NamedQuery(name="UserAgent.findAll", query="SELECT u FROM UserAgent u")
public class UserAgent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="agent_id")
	private Long agentId;

	@Column(name="agent_string")
	private String agentString;

	private String family;

	private String os;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="userAgent")
	private List<AccessLog> accessLogs;

	public UserAgent() {
	}

	public Long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentString() {
		return this.agentString;
	}

	public void setAgentString(String agentString) {
		this.agentString = agentString;
	}

	public String getFamily() {
		return this.family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setUserAgent(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setUserAgent(null);

		return accessLog;
	}

}