package de.cses;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Test;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.ImageEntry;

public class MysqlConnectorTest {
	
	MysqlConnector mc = MysqlConnector.getInstance();

	@Test
	public void testCreateNewImageEntry() {
		System.out.println("new entry added to 'images': " + mc.createNewImageEntry());
	}

//	@Test
//	public void testUpdateImageEntry() {
//		mc.updateImageEntry(new ImageEntry(4, 2, 3, "Die FotoGräfinnen", 7, "test entry", Date.valueOf("1964-03-17")));
//		System.out.println("Entry changed " + mc.getImageEntry(4).getSqlUpdate());
//	}

//	@Test
//	public void testGetImageEntries() {
//		ArrayList<ImageEntry> entries = mc.getImageEntries();
//		for (ImageEntry ie : entries) {
//			System.out.println(ie.getTitle());
//		}
//	}
	
//	@Test
//	public void testGetImageEntry() {
//		ImageEntry ie;
//		for (int i = 2; i<4; ++i) {
//			ie = mc.getImageEntry(i);
//			if (ie != null) {
//				System.out.println("Entry no. " + i + " = " + ie.getTitle());
//			}
//		}
//	}
}
