package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the http_method database table.
 * 
 */
@Entity
@Table(name="http_method")
@NamedQuery(name="HttpMethod.findAll", query="SELECT h FROM HttpMethod h")
public class HttpMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="method_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methodId;

	private String method;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="httpMethod")
	private List<AccessLog> accessLogs;

	public HttpMethod() {
	}

	public Integer getMethodId() {
		return this.methodId;
	}

	public void setMethodId(Integer methodId) {
		this.methodId = methodId;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setHttpMethod(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setHttpMethod(null);

		return accessLog;
	}

}