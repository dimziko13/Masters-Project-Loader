package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the http_status database table.
 * 
 */
@Entity
@Table(name="http_status")
@NamedQuery(name="HttpStatus.findAll", query="SELECT h FROM HttpStatus h")
public class HttpStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="status_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer statusId;

	@Column(name="status_code")
	private Integer statusCode;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="httpStatus")
	private List<AccessLog> accessLogs;

	public HttpStatus() {
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setHttpStatus(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setHttpStatus(null);

		return accessLog;
	}

}
