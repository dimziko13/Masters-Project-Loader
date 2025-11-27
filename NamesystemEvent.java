package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the namesystem_event database table.
 * 
 */
@Entity
@Table(name="namesystem_event")
@NamedQuery(name="NamesystemEvent.findAll", query="SELECT n FROM NamesystemEvent n")
public class NamesystemEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ns_event_id")
	private Long nsEventId;

	@Column(name="size_bytes")
	private Long sizeBytes;

	private Timestamp ts;

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

	//bi-directional many-to-many association to Block
	@ManyToMany
	@JoinTable(
		name="ns_event_block"
		, joinColumns={
			@JoinColumn(name="ns_event_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="block_id")
			}
		)
	private List<Block> blocks;

	public NamesystemEvent() {
	}

	public Long getNsEventId() {
		return this.nsEventId;
	}

	public void setNsEventId(Long nsEventId) {
		this.nsEventId = nsEventId;
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

	public List<Block> getBlocks() {
		return this.blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

}