package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the block database table.
 * 
 */
@Entity
@NamedQuery(name="Block.findAll", query="SELECT b FROM Block b")
public class Block implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="block_id")
	private String blockId;

	@Column(name="size_bytes")
	private Long sizeBytes;

	//bi-directional many-to-one association to DataReceiverLog
	@OneToMany(mappedBy="block")
	private List<DataReceiverLog> dataReceiverLogs;

	//bi-directional many-to-many association to NamesystemEvent
	@ManyToMany(mappedBy="blocks")
	private List<NamesystemEvent> namesystemEvents;

	public Block() {
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public Long getSizeBytes() {
		return this.sizeBytes;
	}

	public void setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}

	public List<DataReceiverLog> getDataReceiverLogs() {
		return this.dataReceiverLogs;
	}

	public void setDataReceiverLogs(List<DataReceiverLog> dataReceiverLogs) {
		this.dataReceiverLogs = dataReceiverLogs;
	}

	public DataReceiverLog addDataReceiverLog(DataReceiverLog dataReceiverLog) {
		getDataReceiverLogs().add(dataReceiverLog);
		dataReceiverLog.setBlock(this);

		return dataReceiverLog;
	}

	public DataReceiverLog removeDataReceiverLog(DataReceiverLog dataReceiverLog) {
		getDataReceiverLogs().remove(dataReceiverLog);
		dataReceiverLog.setBlock(null);

		return dataReceiverLog;
	}

	public List<NamesystemEvent> getNamesystemEvents() {
		return this.namesystemEvents;
	}

	public void setNamesystemEvents(List<NamesystemEvent> namesystemEvents) {
		this.namesystemEvents = namesystemEvents;
	}

}