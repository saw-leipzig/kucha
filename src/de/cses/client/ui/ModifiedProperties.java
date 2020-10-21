package de.cses.client.ui;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.shared.ModifiedEntry;

public interface ModifiedProperties extends PropertyAccess<ModifiedEntry> {
	@Path("modifiedEntryID")
	ModelKeyProvider<ModifiedEntry> key();

	ValueProvider<ModifiedEntry, String> entryID();
	ValueProvider<ModifiedEntry, String> modifiedBy();
	ValueProvider<ModifiedEntry, String> modifiedOn();
	ValueProvider<ModifiedEntry, String> annoID();
	ValueProvider<ModifiedEntry, String> tags();

}

