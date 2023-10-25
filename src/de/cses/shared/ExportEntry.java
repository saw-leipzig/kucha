package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExportEntry extends AbstractEntry {
    private int id;
    private String description;

    public ExportEntry()
    {
    	
    }
    public ExportEntry(int id, String description)
    {
        this.id = id;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return description + " " + Integer.toString(id);
    }
    public String getUniqueId()
    {
        return description + " " + Integer.toString(id);
    }
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return description + " " + Integer.toString(id);
	}
}

