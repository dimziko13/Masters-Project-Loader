package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the referrer database table.
 * 
 */
@Entity
@NamedQuery(name="Referrer.findAll", query="SELECT r FROM Referrer r")
public class Referrer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="referrer_id")
	private Long referrerId;

	@Column(name="referrer_url")
	private String referrerUrl;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="referrer")
	private List<AccessLog> accessLogs;

	public Referrer() {
	}

	public Long getReferrerId() {
		return this.referrerId;
	}

	public void setReferrerId(Long referrerId) {
		this.referrerId = referrerId;
	}

	public String getReferrerUrl() {
		return this.referrerUrl;
	}

	public void setReferrerUrl(String referrerUrl) {
		this.referrerUrl = referrerUrl;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setReferrer(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setReferrer(null);

		return accessLog;
	}

}