package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the ip_address database table.
 * 
 */
@Entity
@Table(name="ip_address")
@NamedQuery(name="IpAddress.findAll", query="SELECT i FROM IpAddress i")
public class IpAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Id
	//@Column(name="ip_id")
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ip_id")

	private Long ipId;

	private String ip;

	//bi-directional many-to-one association to AccessLog
	@OneToMany(mappedBy="ipAddress")
	private List<AccessLog> accessLogs;

	//bi-directional many-to-one association to DataReceiverLog
	@OneToMany(mappedBy="ipAddress1")
	private List<DataReceiverLog> dataReceiverLogs1;

	//bi-directional many-to-one association to DataReceiverLog
	@OneToMany(mappedBy="ipAddress2")
	private List<DataReceiverLog> dataReceiverLogs2;

	//bi-directional many-to-one association to NamesystemEvent
	@OneToMany(mappedBy="ipAddress1")
	private List<NamesystemEvent> namesystemEvents1;

	//bi-directional many-to-one association to NamesystemEvent
	@OneToMany(mappedBy="ipAddress2")
	private List<NamesystemEvent> namesystemEvents2;

	public IpAddress() {
	}

	public Long getIpId() {
		return this.ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<AccessLog> getAccessLogs() {
		return this.accessLogs;
	}

	public void setAccessLogs(List<AccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public AccessLog addAccessLog(AccessLog accessLog) {
		getAccessLogs().add(accessLog);
		accessLog.setIpAddress(this);

		return accessLog;
	}

	public AccessLog removeAccessLog(AccessLog accessLog) {
		getAccessLogs().remove(accessLog);
		accessLog.setIpAddress(null);

		return accessLog;
	}

	public List<DataReceiverLog> getDataReceiverLogs1() {
		return this.dataReceiverLogs1;
	}

	public void setDataReceiverLogs1(List<DataReceiverLog> dataReceiverLogs1) {
		this.dataReceiverLogs1 = dataReceiverLogs1;
	}

	public DataReceiverLog addDataReceiverLogs1(DataReceiverLog dataReceiverLogs1) {
		getDataReceiverLogs1().add(dataReceiverLogs1);
		dataReceiverLogs1.setIpAddress1(this);

		return dataReceiverLogs1;
	}

	public DataReceiverLog removeDataReceiverLogs1(DataReceiverLog dataReceiverLogs1) {
		getDataReceiverLogs1().remove(dataReceiverLogs1);
		dataReceiverLogs1.setIpAddress1(null);

		return dataReceiverLogs1;
	}

	public List<DataReceiverLog> getDataReceiverLogs2() {
		return this.dataReceiverLogs2;
	}

	public void setDataReceiverLogs2(List<DataReceiverLog> dataReceiverLogs2) {
		this.dataReceiverLogs2 = dataReceiverLogs2;
	}

	public DataReceiverLog addDataReceiverLogs2(DataReceiverLog dataReceiverLogs2) {
		getDataReceiverLogs2().add(dataReceiverLogs2);
		dataReceiverLogs2.setIpAddress2(this);

		return dataReceiverLogs2;
	}

	public DataReceiverLog removeDataReceiverLogs2(DataReceiverLog dataReceiverLogs2) {
		getDataReceiverLogs2().remove(dataReceiverLogs2);
		dataReceiverLogs2.setIpAddress2(null);

		return dataReceiverLogs2;
	}

	public List<NamesystemEvent> getNamesystemEvents1() {
		return this.namesystemEvents1;
	}

	public void setNamesystemEvents1(List<NamesystemEvent> namesystemEvents1) {
		this.namesystemEvents1 = namesystemEvents1;
	}

	public NamesystemEvent addNamesystemEvents1(NamesystemEvent namesystemEvents1) {
		getNamesystemEvents1().add(namesystemEvents1);
		namesystemEvents1.setIpAddress1(this);

		return namesystemEvents1;
	}

	public NamesystemEvent removeNamesystemEvents1(NamesystemEvent namesystemEvents1) {
		getNamesystemEvents1().remove(namesystemEvents1);
		namesystemEvents1.setIpAddress1(null);

		return namesystemEvents1;
	}

	public List<NamesystemEvent> getNamesystemEvents2() {
		return this.namesystemEvents2;
	}

	public void setNamesystemEvents2(List<NamesystemEvent> namesystemEvents2) {
		this.namesystemEvents2 = namesystemEvents2;
	}

	public NamesystemEvent addNamesystemEvents2(NamesystemEvent namesystemEvents2) {
		getNamesystemEvents2().add(namesystemEvents2);
		namesystemEvents2.setIpAddress2(this);

		return namesystemEvents2;
	}

	public NamesystemEvent removeNamesystemEvents2(NamesystemEvent namesystemEvents2) {
		getNamesystemEvents2().remove(namesystemEvents2);
		namesystemEvents2.setIpAddress2(null);

		return namesystemEvents2;
	}

}
