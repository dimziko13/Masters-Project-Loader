package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the access_log database table.
 * 
 */
@Entity
@Table(name="access_log")
@NamedQuery(name="AccessLog.findAll", query="SELECT a FROM AccessLog a")
public class AccessLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="access_id")
	private Long accessId;

	@Column(name="response_size")
	private Long responseSize;

	private Timestamp ts;

	//bi-directional many-to-one association to HttpMethod
	@ManyToOne
	@JoinColumn(name="method_id")
	private HttpMethod httpMethod;

	//bi-directional many-to-one association to HttpStatus
	@ManyToOne
	@JoinColumn(name="status_id")
	private HttpStatus httpStatus;

	//bi-directional many-to-one association to IpAddress
	@ManyToOne
	@JoinColumn(name="ip_id")
	private IpAddress ipAddress;

	//bi-directional many-to-one association to Referrer
	@ManyToOne
	@JoinColumn(name="referrer_id")
	private Referrer referrer;

	//bi-directional many-to-one association to Resource
	@ManyToOne
	@JoinColumn(name="resource_id")
	private Resource resource;

	//bi-directional many-to-one association to UserAgent
	@ManyToOne
	@JoinColumn(name="agent_id")
	private UserAgent userAgent;

	//bi-directional many-to-one association to UserIdentity
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserIdentity userIdentity;

	public AccessLog() {
	}

	public Long getAccessId() {
		return this.accessId;
	}

	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}

	public Long getResponseSize() {
		return this.responseSize;
	}

	public void setResponseSize(Long responseSize) {
		this.responseSize = responseSize;
	}

	public Timestamp getTs() {
		return this.ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public HttpMethod getHttpMethod() {
		return this.httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public IpAddress getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Referrer getReferrer() {
		return this.referrer;
	}

	public void setReferrer(Referrer referrer) {
		this.referrer = referrer;
	}

	public Resource getResource() {
		return this.resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public UserAgent getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}

	public UserIdentity getUserIdentity() {
		return this.userIdentity;
	}

	public void setUserIdentity(UserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}

}