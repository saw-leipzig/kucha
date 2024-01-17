package de.cses.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExportEntry extends AbstractEntry {
    private int id;
    private String description;
    private ArrayList<Integer> iconography;

    public ExportEntry()
    {
    	
    }
    public ExportEntry(int id, String description)
    {
        this.id = id;
        this.description = description;
        this.iconography = iconography;
    }

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }
    public ArrayList<Integer> getIconography()
    {
        return iconography;
    }

    public void setIconography(ArrayList<Integer> iconography)
    {
        this.iconography = iconography;
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

