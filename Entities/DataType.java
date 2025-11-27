package com.example.mastersapp.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the data_type database table.
 * 
 */
@Entity
@Table(name="data_type")
@NamedQuery(name="DataType.findAll", query="SELECT d FROM DataType d")
public class DataType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="type_id")
	private Integer typeId;

	@Column(name="type_name")
	private String typeName;

	//bi-directional many-to-one association to DataReceiverLog
	@OneToMany(mappedBy="dataType")
	private List<DataReceiverLog> dataReceiverLogs;

	//bi-directional many-to-one association to NamesystemEvent
	@OneToMany(mappedBy="dataType")
	private List<NamesystemEvent> namesystemEvents;

	public DataType() {
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<DataReceiverLog> getDataReceiverLogs() {
		return this.dataReceiverLogs;
	}

	public void setDataReceiverLogs(List<DataReceiverLog> dataReceiverLogs) {
		this.dataReceiverLogs = dataReceiverLogs;
	}

	public DataReceiverLog addDataReceiverLog(DataReceiverLog dataReceiverLog) {
		getDataReceiverLogs().add(dataReceiverLog);
		dataReceiverLog.setDataType(this);

		return dataReceiverLog;
	}

	public DataReceiverLog removeDataReceiverLog(DataReceiverLog dataReceiverLog) {
		getDataReceiverLogs().remove(dataReceiverLog);
		dataReceiverLog.setDataType(null);

		return dataReceiverLog;
	}

	public List<NamesystemEvent> getNamesystemEvents() {
		return this.namesystemEvents;
	}

	public void setNamesystemEvents(List<NamesystemEvent> namesystemEvents) {
		this.namesystemEvents = namesystemEvents;
	}

	public NamesystemEvent addNamesystemEvent(NamesystemEvent namesystemEvent) {
		getNamesystemEvents().add(namesystemEvent);
		namesystemEvent.setDataType(this);

		return namesystemEvent;
	}

	public NamesystemEvent removeNamesystemEvent(NamesystemEvent namesystemEvent) {
		getNamesystemEvents().remove(namesystemEvent);
		namesystemEvent.setDataType(null);

		return namesystemEvent;
	}

}
