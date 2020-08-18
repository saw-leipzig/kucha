package de.cses.shared;

import java.sql.Date;
import java.util.Calendar;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExpeditionEntry implements IsSerializable {
	
	private int expeditionID;
	private String name;
	private String leader;
	private Date startDate;
	private Date endDate;

	public ExpeditionEntry() { }

	public ExpeditionEntry(int expeditionID, String name, String leader, Date startDate, Date endDate) {
		super();
		this.setExpeditionID(expeditionID);
		this.setName(name);
		this.setLeader(leader);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}

	public int getExpeditionID() {
		return expeditionID;
	}

	public void setExpeditionID(int expeditionID) {
		this.expeditionID = expeditionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
