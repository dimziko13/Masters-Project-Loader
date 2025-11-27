package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the user_identity database table.
 * 
 */
@Entity
@Table(name="user_identity")
@NamedQuery(name="UserIdentity.findAll", query="SELECT u FROM UserIdentity u")
public class UserIdentity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;

	private String pass;

	@Column(name="user_name")
	private String userName;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="userIdentity")
	private List<AccessLog> accessLogs;

	public UserIdentity() {
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setUserIdentity(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setUserIdentity(null);

		return accessLog;
	}

}