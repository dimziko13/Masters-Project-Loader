package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the data_receiver_log database table.
 * 
 */
@Entity
@Table(name="data_receiver_log")
@NamedQuery(name="DataReceiverLog.findAll", query="SELECT d FROM DataReceiverLog d")
public class DataReceiverLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="dr_log_id")
	private Long drLogId;

	@Column(name="size_bytes")
	private Long sizeBytes;

	private Timestamp ts;

	//bi-directional many-to-one association to Block
	@ManyToOne
	@JoinColumn(name="block_id")
	private Block block;

	//bi-directional many-to-one association to DataType
	@ManyToOne
	@JoinColumn(name="type_id")
	private DataType dataType;

	//bi-directional many-to-one association to IpAddress
	@ManyToOne
	@JoinColumn(name="dst_ip_id")
	private IpAddress ipAddress1;

	//bi-directional many-to-one association to IpAddress
	@ManyToOne
	@JoinColumn(name="src_ip_id")
	private IpAddress ipAddress2;

	public DataReceiverLog() {
	}

	public Long getDrLogId() {
		return this.drLogId;
	}

	public void setDrLogId(Long drLogId) {
		this.drLogId = drLogId;
	}

	public Long getSizeBytes() {
		return this.sizeBytes;
	}

	public void setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}

	public Timestamp getTs() {
		return this.ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public Block getBlock() {
		return this.block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public IpAddress getIpAddress1() {
		return this.ipAddress1;
	}

	public void setIpAddress1(IpAddress ipAddress1) {
		this.ipAddress1 = ipAddress1;
	}

	public IpAddress getIpAddress2() {
		return this.ipAddress2;
	}

	public void setIpAddress2(IpAddress ipAddress2) {
		this.ipAddress2 = ipAddress2;
	}

}