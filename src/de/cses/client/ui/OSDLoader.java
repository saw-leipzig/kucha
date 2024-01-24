package de.cses.client.ui;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.CoordinateEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.ExportEntry;


interface ExportProperties extends PropertyAccess<ExportEntry> {

	ModelKeyProvider<ExportEntry> uniqueID();

	LabelProvider<ExportEntry> name();

}
interface ExportViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml exportLabel(String name);
}

public class OSDLoader {
	private JavaScriptObject osdDic;
	private JavaScriptObject jso;
	ArrayList<ImageEntry> images;
	private boolean annoation;
	private TreeStore<IconographyEntry> icoTree =null;
	private Collection<IconographyEntry> icoEntries = null;
	private boolean isWall=false;
	private HashMap<String, JavaScriptObject> peAnnos = new HashMap<String, JavaScriptObject>();
	//private Object osdLoader;
	private OSDListener osdListener;
	JavaScriptObject annos = null;
	DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public OSDLoader(ArrayList<ImageEntry> images, boolean annotation, TreeStore<IconographyEntry> icoTree, OSDListener osdListener) {
		// Constructor for DepictionEntry
		// We need to initiate a Javascript Object in Javascript Code
		this.osdDic = createDic();
		this.images=images;
		this.annoation =annotation;
		this.icoTree=icoTree;
		if (icoTree!=null) {
			icoTree.setEnableFilters(false);
			icoEntries = icoTree.getRootItems();
			icoTree.setEnableFilters(true);
		}

		//this.osdLoader=this;
		this.osdListener=osdListener;
	}
	public OSDLoader(ImageEntry image) {
		// Constructor for ImageEntry
		// We need to initiate a Javascript Object in Javascript Code
		this.osdDic = createDic();
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		this.images=images;
		this.annoation =false;
		this.icoTree=null;
		//this.osdLoader=this;
	}
	public OSDLoader(ArrayList<WallDimensionEntry> wdes) {
		// Constructor for PositionEntry
		// We need to initiate a Javascript Object in Javascript Code
		this.osdDic = createDic();
		this.isWall=true;
		this.annos = null;
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		for (WallDimensionEntry wde : wdes) {
			ImageEntry image = new ImageEntry();
			image.setFilename(wde.getWallSketch().getFilename());
			images.add(image);
			boolean isRect = (wde.getType() == 0? false: true );
			annos = ConcatList(annos, createMap(wde.getRegisters(),wde.getColumns(), 1000 , 1000, isRect, wde.getName()));
		}
		this.images=images;
		this.annoation =true;
		this.icoTree=null;
		//this.osdLoader=this;
	}
	public void export(String annotoriousID, String imageID) {
		PopupPanel modifiedPopUp = new PopupPanel();
		FramedPanel modifiedFP = new FramedPanel();
		modifiedFP.setHeading("Link Annotation " + annotoriousID);

	    ExportProperties exportProps = GWT.create(ExportProperties.class);
	    ListStore<ExportEntry> exportEntryLS = new ListStore<ExportEntry>(exportProps.uniqueID());
	    ComboBox<ExportEntry> exportSelectionCB = new ComboBox<ExportEntry>(exportEntryLS, exportProps.name(),
				new AbstractSafeHtmlRenderer<ExportEntry>() {

					@Override
					public SafeHtml render(ExportEntry item) {
						final ExportViewTemplates exortTemplates = GWT.create(ExportViewTemplates.class);
						return exortTemplates.exportLabel(item.getName());
					}
				});
		exportSelectionCB.setEmptyText("nothing selected");
		exportSelectionCB.setWidth(200);
		modifiedFP.add(exportSelectionCB);
		TextButton exportButton = new TextButton("Link");
		exportButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				dbService.linkAnnoToEntry(annotoriousID, exportSelectionCB.getValue(), new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Util.doLogging(caught.getLocalizedMessage());
						caught.printStackTrace();
						Info.display("Error!", "Linking Annotation failed!!!");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						modifiedPopUp.hide();
					}
				});			

			}
		});
		modifiedFP.addButton(exportButton);
		dbService.getEntriesByImageID(imageID, annotoriousID,  new AsyncCallback<ArrayList<ExportEntry>>() {
			@Override
			public void onFailure(Throwable caught) {
				Util.doLogging(caught.getLocalizedMessage());
				caught.printStackTrace();
				Util.doLogging(caught.getMessage());
				Info.display("Error!", "Saving Annotation failed!!!");
				
			}

			@Override
			public void onSuccess(ArrayList<ExportEntry> result) {
				ArrayList<Integer> icos = new ArrayList<Integer>();
				for (AnnotationEntry ae: osdListener.getAnnotations()){
					if (ae.getAnnotoriousID().equals(annotoriousID)) {
						for (IconographyEntry ico: ae.getTags()) {
							icos.add(ico.getIconographyID());
						}
						break;
					}
				}
				for (ExportEntry entry : result) {
					entry.setIconography(icos);
					exportEntryLS.add(entry);
				}
			}
		});	
		modifiedPopUp.add(modifiedFP);
		ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeToolButton.setToolTip(Util.createToolTip("close"));
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				modifiedPopUp.hide();
			}
		});
		modifiedFP.addTool(closeToolButton);
		modifiedPopUp.setModal(true);
		modifiedPopUp.center();

	}
	public void getResults(String id ,String tags, String polygon, String image, Boolean delete, Boolean update, Double creationTime, Double modificationTime, Boolean isProposed) {
		// Function for sending the Annotations to MYSQL-Backend
		Util.doLogging("Annotation recieved: Tag: "+tags+", Polygone: "+polygon+", Image: "+image+", delete: "+Boolean.toString(delete)+", update: "+Boolean.toString(update)+", DepictionID: "+osdListener.getDepictionID());
		// Even JS-Arrays are not usable in Java-Code, so we have to use a String to get all the tags, which are than associated with a Iconography Entry
		ArrayList<IconographyEntry> newTags = new ArrayList<IconographyEntry>();
		String dummy[] = tags.split(";", -1);
		for (String tag : dummy) {
			IconographyEntry icoEntry= icoTree.findModelWithKey(tag);
			//Util.doLogging("found tag value: "+icoEntry.getText());
			newTags.add(icoEntry);
		}
		JavaScriptObject bodies = createList();
		for (IconographyEntry ie : newTags) {
			bodies = addToBody(bodies,ie.getIconographyID(), ie.getText(), image);
		}

		// As we want our MySQL-DB to understand the Polygons, we need to convert them into WKT and then into a GeoJSON
		String svgRaw = polygon.replace("<svg><path d=\"","").replace("\"></path></svg>","");
		String newPoly = toWKT(svgRaw);
		if (newPoly != "") {
			String newPolyGeoJson=toGeopJson(newPoly);
			//Util.doLogging("Writing Annotation");
			AnnotationEntry annoEntry = new AnnotationEntry(osdListener.getDepictionID(), id, newTags, newPolyGeoJson, image, delete, update, creationTime, modificationTime, isProposed);
			String w3cAnno = generateW3CAnnotation(annoEntry.getAnnotoriousID() , bodies, newPolyGeoJson, annoEntry.getImage(), annoEntry.getIsProposed()).toString();
			annoEntry.setW3c(w3cAnno);
			annoEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());		
			// Create a new annotation and decide, what to do with it.
			AnnotationEntry annoEntryDB = new AnnotationEntry(annoEntry.getDepictionID(), annoEntry.getAnnotoriousID(), annoEntry.getTags(), newPoly, annoEntry.getImage(), annoEntry.getDelete(), annoEntry.getUpdate(), creationTime, modificationTime, isProposed);
			annoEntryDB.setW3c(w3cAnno);
			
			if (!update && !delete) {
				osdListener.addAnnotation(annoEntry);
			}
			else {
				ArrayList<AnnotationEntry> newAnnotations = new ArrayList<AnnotationEntry>();
				for (AnnotationEntry ae : osdListener.getAnnotations()) {
					if (ae.getAnnotoriousID()!=id) {
						newAnnotations.add(ae);
					}
				}
				if (update) {
					newAnnotations.add(annoEntry);
				}
				osdListener.setAnnotationsInParent(newAnnotations);
				
			}
			osdListener.setAnnotationsInParent(osdListener.getAnnotations());
			annoEntryDB.setLastChangedByUser(UserLogin.getInstance().getUsername());		
			Util.doLogging("Start Saving Annotation");
			if (osdListener.getDepictionID()>0) {			
				dbService.setAnnotationResults(annoEntryDB, osdListener.isOrnament(), new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Util.doLogging(caught.getLocalizedMessage());
						caught.printStackTrace();
						Info.display("Error!", "Saving Annotation failed!!!");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						Util.doLogging("Annotation Saved: "+Boolean.toString(result));
					}
				});			
			}
						
		}

	}
	public void setosd() {
		 dbService.getOSDContext(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {
				startLoadingTiles(result);
			}
		});
	}

	public void startLoadingTiles(String context) {
		Collection<IconographyEntry> icos = StaticTables.getInstance().getIconographyEntries().values();
		if (icos.size()>0) {
			loadTiles(images, context, annoation, icos);
		}
		//It might happen, that Static tables is not fast enough fetching tables and loading getIconographyEntries seems to corrupt the fetching process if  
		else {
			StaticTables.getInstance().reloadIconography();
			dbService.getIconography(new AsyncCallback<ArrayList<IconographyEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(ArrayList<IconographyEntry> result) {
					HashMap<Integer, IconographyEntry> iconographyEntryMap = new HashMap<Integer, IconographyEntry>();
					for (IconographyEntry ie : result) {
						iconographyEntryMap.put(ie.getIconographyID(), ie);
					}
					loadTiles(images, context, annoation, iconographyEntryMap.values());
				}
			});			
		}
					
	}
	public JavaScriptObject listConverter(Collection<IconographyEntry> icoTree) {
		JavaScriptObject list = null;
		if (icoTree!=null) {
			for (IconographyEntry ie : icoTree) {
				JavaScriptObject icoResult = null;
				if (ie.getChildren()==null) {
					icoResult = addIcoEntry(ie.getIconographyID(), ie.getParentID(), ie.getText(), ie.getSearch(), null);
				}
				else {
					JavaScriptObject children = listConverter(ie.getChildren());
					icoResult = addIcoEntry(ie.getIconographyID(), ie.getParentID(), ie.getText(), ie.getSearch(), children);
				}
				list = addToList(list, icoResult);
			}			
		}
		return list;
	}
	public void loadTiles(ImageEntry image, String context, boolean annotation, List<IconographyEntry> icoTree) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation, false);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic, UserLogin.getInstance().getSessionID(), annotation, icos, this,null, isWall);
	}
	public void loadTiles(ArrayList<ImageEntry> images, String context, boolean annotation, Collection<IconographyEntry> icoTree) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation, isWall);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		if (osdListener!=null) {
			annos= generateW3CAnnotations(osdListener.getAnnotations());
		}
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, icos, this, annos, isWall);	
	}
	public void loadTilesForWallPositions(ArrayList<CoordinateEntry> pes, String context) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		for (CoordinateEntry pe : pes) {
			ImageEntry image = new ImageEntry();
			image.setFilename(pe.getName());
		}
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, true, true);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = null;
		JavaScriptObject annos = null;
		if (osdListener!=null) {
			annos= generateW3CAnnotations(osdListener.getAnnotations());			
		}
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), true, icos, this, annos, isWall);	
	}
	
	public void destroyAllViewers() {
		destroyAllViewersJava(jso);
	}
	private JavaScriptObject generateW3CAnnotations(ArrayList<AnnotationEntry> annoEntries) {
		JavaScriptObject w3cAnnotation = createList();
		if (annoEntries!=null) {
			for (AnnotationEntry annoEntry : annoEntries) {
//				//Util.doLogging(annoEntry.getPolygone());
//				JavaScriptObject bodies = createList();
//				for (IconographyEntry ie : annoEntry.getTags()) {
//					bodies = addToBody(bodies,ie.getIconographyID(), ie.getText(), annoEntry.getImage());
//				}
//				
				JavaScriptObject w3cAnnotationObject = parseW3CAnnotation(annoEntry.getW3c());
				w3cAnnotation = addAnnotation(w3cAnnotation, w3cAnnotationObject);
			}			
		}
		return w3cAnnotation;
	}

	public static native JavaScriptObject addToBody(JavaScriptObject bodies,int id, String tag, String image)
	/*-{
	 	body={};
	 	body["type"]="TextualBody";
	 	body["value"]=tag;
	 	body["id"]=id;
	 	body["image"]=image;
	 	bodies.push(body);
 		return bodies;
	}-*/;
	public static native JavaScriptObject generateW3CAnnotation(String annotoriousID, JavaScriptObject tags, String polygone, String image, Boolean isProposed)
	/*-{
		var anno={};
		anno["@context"]="http://www.w3.org/ns/anno.jsonld";
		anno["id"]=annotoriousID;
		anno["type"]="Annotation";
		anno["body"]=tags;
		anno["creator"]={
		"type":isProposed?"computer":"human"
		}
		var target={};
		var selector={};
		selector["type"]="SvgSelector";
		selector["conformsTo"]="http://www.w3.org/TR/media-frags/";
		var root = $wnd.JSON.parse(polygone);
		//$wnd.console.log(root);
		var geoGenerator=$wnd.d3.geoPath()
		selector["value"]="<svg><path d=\""+geoGenerator(root)+"\"></path></svg>";
		target["selector"]=selector;
		target["source"]= image,
		anno["target"]=target;
	    return anno;
	}-*/;
	public static native JavaScriptObject parseW3CAnnotation(String w3cAnno)
	/*-{
		var anno = JSON.parse(w3cAnno)
	    return anno;
	}-*/;
	public static native JavaScriptObject addAnnotation(JavaScriptObject annos,JavaScriptObject anno)
	/*-{
		annos.push(anno);
	    return annos;
	}-*/;
	public void removeOrAddAnnotations(ArrayList<AnnotationEntry> annos, boolean add) {
		JavaScriptObject jsAnnos= generateW3CAnnotations(annos);
		removeOrAddAnnotationsJS(jso, jsAnnos, add);

	}
	public static native void removeOrAddAnnotationsJS(JavaScriptObject viewers, JavaScriptObject annos, Boolean add) 
	/*-{
	    if (viewers["annotorious"]!=null){
	    	for (var v in viewers["annotorious"]) {
				var annosForViewer = [];
				for (var k = 0, length2 = annos.length; k < length2; k++){
					if (annos[k].target.source===v){
						if (add){
							annosForViewer.push(annos[k]);
							//viewers["annotorious"][v].addAnnotation(annos[k]);
							//$wnd.console.log("Adding ",annos[k]," to ",v)
						}
						else{
							//$wnd.console.log("removing ",annos[k]," from ",v)
							viewers["annotorious"][v].removeAnnotation(annos[k]);
						}
					}	
				}
				// $wnd.console.log("Adding ",annosForViewer)
//				if (annosForViewer.length > 0){
//				    var a = $doc.createElement("a");
//				    a.href = $wnd.URL.createObjectURL(new Blob([JSON.stringify(annosForViewer)], {type: "text/plain"}));
//				    a.download = "demo.txt";
//				    a.click(); 
//				}
				viewers["annotorious"][v].setAnnotations(annosForViewer);	
			}	
	    }
	}-*/;
	public void removeAllAnnotations() {
		removeAllAnnotationsJS(jso);
	}
	public static native void removeAllAnnotationsJS(JavaScriptObject viewers) 
	/*-{
		//$wnd.console.log("removeAllAnnotationsJS started.")
		for (var v in viewers["annotorious"]) {
				var emptyAnnos=[];
				viewers["annotorious"][v].setAnnotations(emptyAnnos);
		}
	}-*/;

//	public static native void addAnnotations(JavaScriptObject viewers, JavaScriptObject annos) 
//	/*-{
//	}-*/;
	public static native JavaScriptObject destroyAllViewersJava(JavaScriptObject viewers)
	/*-{
		// $wnd.console.log("destroy all viewers Started");
		if (viewers!=null){
			if (viewers["dic"]!=null){					
				for (var k in viewers["dic"]) {
		    		viewers["dic"][k].destroy();
		    		viewers["dic"][k]=null;
		    		delete viewers["dic"][k];
				};
			}
			if (viewers["annotorious"]!=null){				
				for (var k in viewers["annotorious"]) {
		    		viewers["annotorious"][k].destroy();
		    		viewers["annotorious"][k]=null;
		    		delete viewers["annotorious"][k];
				};
			}
		}

	}-*/;
	public void highlightAnnotation(String annoID) {
		highlightAnnotationJS(jso, annoID);
	}
	public static native void highlightAnnotationJS(JavaScriptObject viewers, String annoID)
	/*-{          
//		var annoStyle = {
//            style:{
//              outer: {
//                "vector-effect": "none",
//                "stroke": "#f00",
//                "fill": "rgba(0, 128, 0,0.55)",
//                "stroke-width":"3px",
//                "transition": "fill 1s, stroke-width 0.7s"
//              },
//              inner: {
//                "vector-effect": "none",
//                "stroke": "#f00",
//                "stroke-width": "3px",
//                "transition": "stroke-width 0.7s"
//              }
//            }
//          }
//		for (var k in viewers["annotorious"]) {
//    		viewers["annotorious"][k].setAnnotationStyle(annoID, annoStyle);
//		};

	}-*/;
	public void deHighlightAnnotation(String annoID) {
		deHighlightAnnotationJS(jso, annoID);
	}
	public static native void deHighlightAnnotationJS(JavaScriptObject viewers, String annoID)
	/*-{
	 	//$wnd.console.log("deHighlightAnnotationsJS started");
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].removeAnnotation(annoID);
		};

	}-*/;
	public static native String toWKT(String polygon)
	/*-{
	    $wnd.console.log("polygon", polygon);		
		try {
	        var union =null;
	        var results =polygon.split('M');
	        results.forEach(function (result, index) {
	          if (result.length>0){
	            result=result.replace(/ Z/g,"Z")
	            result=result.replace(/Z /g,"Z")
	            result=result.replace(/Z/g,"")
	            result=result.replace(/L /g,"L")
	            result=result.replace(/ L/g,"L")
	            var coords = result.split("L")
	          	$wnd.console.log("coords", coords);
	            poly="POLYGON(("
	            var first=true;
	            var firstCoord = ""
	            var lastCoord=""
	            coords.forEach(function(coord, index){
	          	  var newCoord = parseFloat(coord.split(",")[0]).toFixed(2).toString() + " " + parseFloat(coord.split(",")[1]).toFixed(2).toString();
	              if (!first){
	                poly+=", ";
	                lastCoord=coord.replace(","," ")
	              }
	              else{
	              	firstCoord=coord.replace(","," ")
	              	} 
	              first=false;
	              poly+=coord.replace(","," ");
	            });
	            if (firstCoord!=lastCoord){
	            	poly+=", "+firstCoord
	            }
	            poly+="))";
	        	$wnd.console.log("poly: ",poly);
	        	var precisionModel = new $wnd.jsts.geom.PrecisionModel($wnd.jsts.geom.PrecisionModel.FLOATING)
    			var geometryPrecisionReducer = new $wnd.jsts.precision.GeometryPrecisionReducer(precisionModel)
    			var geomFactoryJSTS = new $wnd.jsts.geom.GeometryFactory(precisionModel)
	            var leser = new $wnd.jsts.io.WKTReader(geomFactoryJSTS);
	            if (!union){
	              union=leser.read(poly);
	            }
	            else {
	              union =union.symDifference(leser.read(poly))
	            }
	          }
	        });
	        var schreiber = new $wnd.jsts.io.WKTWriter();
	        var polygonRes = schreiber.write(union);
			return polygonRes;
		} catch (e) {
		   $wnd.console.log("Failed to convert polygon.",e, polygon);
		   $wnd.alert("Failed to convert polygon.");
		   return "";
		}			
	}-*/;
	public static native String toGeopJson(String polygon)
	/*-{
        // $wnd.console.log("WKT: ",polygon);
        var leser = new $wnd.wellknown(polygon);
        var res = JSON.stringify(leser)
		// $wnd.console.log("result: ",res);
		return res
	}-*/;
	public static native JavaScriptObject createMap(int rows, int cols, int height, int width, boolean isRect, String filename) 
	/*-{	
        function isOdd(num) { return num % 2;}
        function addRhombus(row,col, rows, cols, height, width){
          var heightRhomb = 0; 
          heightRhomb = (rows>1 ? (height*2)/(rows-1) : (height*2)/(1))*(((row)/2));
          var heightrow = rows>1 ? (height*2)/(rows-1) : (height*2)/(1);
          var newPath = "";
          if (isOdd(row) === 0){
            newPath = "M"+((width*((2*col)-1)) / ((2*cols) - 2)) +"," + ((row === 1) ? "0" : (heightRhomb-heightrow))+"L" + ((col === cols) ? width : ((((col * width) / ((cols) - 1))))) + "," + ((row === 1) ? "0" : (heightRhomb-heightrow+(heightrow/2)))+"L"+ (((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2)) +"," + ((row === rows) ? height : (heightRhomb))+"L" + ((((col+1) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width))*2)) + "," + ((heightrow/2)+heightRhomb-heightrow)+"L"+(((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2)) +"," + (heightRhomb-heightrow)+"z"; 
          } else {
            newPath = "M"+((col === 1) ? "0" : (((col-1) / ((cols) - 1) * width))) +"," + ((row === 1) ? "0" : (heightRhomb-heightrow))+"L" + ((col === cols) ? width : ((((width * (2*col-1)) / ((2 * cols) - 2))))) + "," + ((row === 1) ? "0" : (heightRhomb-heightrow+(heightrow/2)))+"L"+((col === 1) ? "0" : (((col-1) / ((cols) - 1) * width))) +"," + ((row === rows) ? height : (heightRhomb))+"L" + ((col === 1) ? "0" : ((((col) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width)))-((((1) / ((cols) - 1) * width)/2)))) + "," + ((heightrow/2)+heightRhomb-heightrow)+"L"+((col === 1) ? "0" : (((col-1) / ((cols) - 1) * width))) +"," + ((row === 1) ? "0" : (heightRhomb-heightrow))+"z";
          }
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "Reg. "+row+", No. "+col,
            "type": "Annotation",
            "body": [
              {
                  "type": "TextualBody",
                  "value": "nimbus around the head",
                  "id": 2255,
                  "image": filename
              }
            ],
            "target": {
                "selector": {
                    "type": "SvgSelector",
                    "conformsTo": "http://www.w3.org/TR/media-frags/",
                    "value": "<svg><path d=\""+newPath+"\"></path></svg>"
                },
                "source": filename
            }
          }
          return annotation;
        }
        function addRect(row,col, rows, cols, height, width){
          var heightRhomb = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var heightrow = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var newPath = "";
          newPath = "xywh=pixel:"+(width-((width/cols)*col))+","+(height-((height/rows)*row))+","+(width/cols)+","+(height/rows);
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "Reg. "+row+", No. "+col,
            "type": "Annotation",
            "body": [
              {
                  "type": "TextualBody",
                  "value": "nimbus around the head",
                  "id": 2255,
                  "image": filename
              }
            ],
            "target": {
                "selector": {
                    "type": "FragmentSelector", 
                    "conformsTo": "http://www.w3.org/TR/media-frags/",
                    "value": newPath
                },
                "source": filename
            }
          }
          return annotation;
        }
        var annotations = [];
        for (var j = 1; j< rows+1; j++){
          for(var i = 1; i < (cols+1); i+=1){
  			$wnd.console.log("started loop: ",i,j, rows, cols);	      	
          	var annotationObject = {};
            if (isRect){
                annotationObject = addRect(j,i,rows,cols,height, width);
            } else {
              if (!((i === cols) && (isOdd(j) === 0))){
                annotationObject = addRhombus(j,i,rows,cols,height, width);
              }
            }
            if (Object.keys(annotationObject).length > 0){
            	annotations.push(annotationObject);
            }
          }
        }
        $wnd.console.log("annotations:",annotations);
        return annotations;
	}-*/;
	public void setHasContourAllign(boolean hasContourAllign) {
		setHasContourAllignJS(jso, hasContourAllign);
	}
	public static native JavaScriptObject setHasContourAllignJS(JavaScriptObject viewers, boolean hasContourAllign)
	/*-{
		for (var v in viewers["annotorious"]) {
			viewers["annotorious"][v].setHasContourAllign(hasContourAllign);
		}
	}-*/;
	public static native JavaScriptObject createZoomeImage(JavaScriptObject tiles,JavaScriptObject wheres, JavaScriptObject source, JavaScriptObject dic, String sessionID, boolean anno, JavaScriptObject icoTree, OSDLoader osdLoader, JavaScriptObject annos, boolean isWall)
	/*-{
	 annotorious={};
	 
	 var ExportWidget = function(args) {
		  var annoExport = function(evt) {
			osdLoader.@de.cses.client.ui.OSDLoader::export(Ljava/lang/String;Ljava/lang/String;)(args.annotation.id, args.annotation.targets[0].source);		  
			}
	 		
		  var createButton = function(value) {
		    var button = document.createElement('button');
			button.innerHTML = 'Link to other Entry'
		    button.addEventListener('click', annoExport); 
		    return button;
		  }
		
		  var container = document.createElement('div');
		  container.className = 'export-widget';
		  
		  var button1 = createButton();
		
		  container.appendChild(button1);
	      $wnd.console.log("exportbutton is loading" + container);
		  
		  return container;
		}
	 $doc.cookie = "sessionID="+sessionID+";SameSite=Lax;"; 
	    $wnd.OpenSeadragon.setString('Tooltips.SelectionToggle','Selection Demo');
	    $wnd.OpenSeadragon.setString('Tooltips.SelectionConfirm','Ok');
	    $wnd.OpenSeadragon.setString('Tooltips.ImageTools','Image tools');
	    $wnd.OpenSeadragon.setString('Tool.brightness','Brightness');
	    $wnd.OpenSeadragon.setString('Tool.contrast','Contrast');
	    $wnd.OpenSeadragon.setString('Tool.thresholding','Thresholding');
	    $wnd.OpenSeadragon.setString('Tool.invert','Invert');
	    $wnd.OpenSeadragon.setString('Tool.gamma','Gamma');
	    $wnd.OpenSeadragon.setString('Tool.greyscale','Greyscale');
	    $wnd.OpenSeadragon.setString('Tool.reset','Reset');
	    $wnd.OpenSeadragon.setString('Tooltips.HorizontalGuide', 'Add Horizontal Guide');
	    $wnd.OpenSeadragon.setString('Tooltips.VerticalGuide', 'Add Vertical Guide');
	    $wnd.OpenSeadragon.setString('Tool.rotate', 'Rotate');
	    $wnd.OpenSeadragon.setString('Tool.close', 'Close');
	 if (wheres){
	 	  	MyHighlightFormatter = function(annotation) {
            	return 'highlight';
        	}
	 	// $wnd.console.log("wheres:",wheres);
	 	for (var i = 0, length = wheres.length; i < length; i++){
			// $wnd.console.log("Processing image, request header: ",wheres[i]);
		 	if (!(wheres[i] in dic)){  			
			 	dic[wheres[i]] =  $wnd.OpenSeadragon({
			        id: wheres[i],
			        showRotationControl: true,
			        showFlipControl: true,
			        maxZoomLevel: 100,
			        minZoomLevel: 0.001,
			        ajaxWithCredentials: true,
			        loadTilesWithAjax: true,
			        tileRequestHeaders:{"SessionID": sessionID},
			        ajaxHeaders: {"SessionID": sessionID},
			        loadTilesWithAjax:true,
					prefixUrl: "scripts/imageviewer/images/",
	//				tileSources: tiles[wheres[i]]
					
				}); 
	        dic[wheres[i]].open({
	            // The headers specified here will be combined with those in the Viewer object (if any)
	            ajaxHeaders:  {"SessionID": sessionID},
	            tileSource: tiles[wheres[i]]
	        });			
				if (anno){
					
					var savedAnnos=[];
					var foundAnno=false;
					for (var k = 0, length2 = annos.length; k < length2; k++){
						//$wnd.console.log("annotation", annos[k]);	
						if (annos[k].target.source===wheres[i]){
							//$wnd.console.log("foundAnnotation");	
							savedAnnos.push(annos[k]);
							foundAnno=true;
						}
					}
					//$wnd.console.log("config");
					var config = {};
					config["locale"] = 'auto',
					config["readOnly"]=false;
					config["allowEmpty"] = true;
					config["inverted"] = false;
					config["drawOnSingleClick"] = false
					var tw = $wnd.TreeWidget;
					if (isWall){
						config["widgets"]=[{ widget: 'TAG', vocabulary: [ 'lost'] }];
						config["readOnly"]=true;
					} else {
						config["widgets"]=[{ widget: tw, tree: icoTree }, ExportWidget];
					}
					config["image"]=wheres[i];
					config["formatter"] = [MyHighlightFormatter];
					// $wnd.console.log("Adding Annotorious",config);
					annotorious[wheres[i]] = $wnd.OpenSeadragon.Annotorious(dic[wheres[i]],config);
			        $wnd.Annotorious.SelectorPack(annotorious[wheres[i]]);
			        // annotorious[wheres[i]].setDrawingTool('bettermultipolygon');
			        annotorious[wheres[i]].setDrawingTool('bettermultipolygon');
	
					//annotorious[wheres[i]].setDrawingTool("polygon");
					//annotorious[wheres[i]].setDrawingEnabled(true);
					//$wnd.console.log("Adding Handler");
					annotorious[wheres[i]].on('createAnnotation',function(annotation) {
						  // $wnd.console.log("annotation",annotation);
							
				          results="";
				          polygonRes=annotation.target.selector.value
				          var image = "";
				          for (var key in annotation.body){
							if (results===""){
								results=annotation.body[key].id;
							}
							else{
								results=results+";"+annotation.body[key].id;
							}
							if (annotation.target.source.includes("%2Fimages%2F")){
						 		image=annotation.target.source.split("%2Fimages%2F")[1]
							} else {
								image=annotation.target.source
							}
								
						}		
						//$wnd.console.log("results: ",results);
						annotation["creator"] = annotation.creator?annotation.creator:{"type":"human"}
						osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,polygonRes,image, false, false, annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
					});
					annotorious[wheres[i]].on('deleteAnnotation', function(annotation) {
						"deleteAnnotation Triggered"
						results=""
						var image = "";
						for (var key in annotation.body){
							if (results===""){
								results=annotation.body[key].id;
							}
							else{
								results=results+";"+annotation.body[key].id;
							}
							if (annotation.target.source.includes("%2Fimages%2F")){
						 		image=annotation.target.source.split("%2Fimages%2F")[1]
							} else {
								image=annotation.target.source
							}
						}
						
						//$wnd.console.log("results: ",results);
						osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, true, false,annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
					});
					annotorious[wheres[i]].on('selectAnnotation', function(annotation) {
					});
	//				annotorious[wheres[i]].on('mouseEnterAnnotation', function(annotation, event) {
	//				});
	//				annotorious[wheres[i]].on('mouseLeaveAnnotation', function(annotation, event) {
	//				});
					annotorious[wheres[i]].on('startSelection', function(point) {
					});
					annotorious[wheres[i]].on('updateAnnotation', function(annotation, previous) {
						results=""
						var image = "";
						for (var key in annotation.body){
							if (results===""){
								results=annotation.body[key].id;
							}
							else{
								results=results+";"+annotation.body[key].id;
							}
							if (annotation.target.source.includes("%2Fimages%2F")){
						 		image=annotation.target.source.split("%2Fimages%2F")[1]
							} else {
								image=annotation.target.source
							}
						}
						$wnd.console.log("Updating Annotation ", annotation);
						osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, false, true, annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
					});
					//$wnd.console.log("Adding Annotations");
					if (isWall){
						if (foundAnno){
							// $wnd.console.log("Found Annotation for picture ",wheres[i],": ",savedAnnos);
							annotorious[wheres[i]].setAnnotations(savedAnnos);
							var annoStyle = {
					            style:{
					              outer: {
					                "vector-effect": "none",
					                "stroke": "#f00",
					                "fill": "rgba(0, 128, 0,0.55)",
					                "stroke-width":"5px",
					                "transition": "fill 1s, stroke-width 0.7s"
					              },
					              inner: {
					                "vector-effect": "none",
					                "stroke": "#fff",
					                "stroke-width": "1px",
					                "transition": "stroke-width 0.7s"
					              }
					            }
					          }
							for (var k in savedAnnos) {
								annotorious[wheres[i]].setAnnotationStyle(savedAnnos[k].id, annoStyle);
							};
	
						}					
					}
				}		
				// $wnd.console.log("Adding Imagefilters");
				
				dic[wheres[i]].imagefilters({menuId:"menu"+wheres[i],
			    							 toolsLeft: 270
			    							});
				dic[wheres[i]].addHandler("pre-full-page", function (data) {
						data.preventDefaultAction=true;
	  					if (data.eventSource.element.requestFullscreen) {
	    					data.eventSource.element.requestFullscreen();
	  					} else if (data.eventSource.element.mozRequestFullScreen) { 
	    					data.eventSource.element.mozRequestFullScreen();
	  					} else if (data.eventSource.element.webkitRequestFullscreen) { 
	    					data.eventSource.element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
	  					} else if (data.eventSource.element.msRequestFullscreen) {
	    					data.eventSource.element.msRequestFullscreen();
	  					} else if (data.eventSource.element.webkitEnterFullScreen) {
	  						data.eventSource.element.webkitEnterFullScreen(Element.ALLOW_KEYBOARD_INPUT);
						}
	  					else {
	      					var el = $doc.documentElement;
	      					el.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
	    				}
				});
				
	
				// $wnd.console.log("Processing ended, i=",i, wheres.length);
				//$wnd.console.log(dic[wheres[i]].viewport.getContainerSize().x+" / "+dic[wheres[i]].viewport.getContainerSize().y);

		}
	 }
	 }

	    
		
		return {"dic":dic,"annotorious":annotorious}
	}-*/;
	public static native JavaScriptObject addToList(JavaScriptObject list, JavaScriptObject ie)
	/*-{
	if (list==null){
	 var list = [];
	}
	list.push(ie);	
		return list;
	}-*/;	
	public static native JavaScriptObject ConcatList(JavaScriptObject list, JavaScriptObject ie)
	/*-{
	if (list==null){
	 var list = [];
	}
	list = list.concat(ie);
		return list;
	}-*/;	
	public static native JavaScriptObject createDic()
	/*-{	
	 var dic = {};
		return dic
	}-*/;	
	public static native JavaScriptObject createList()
	/*-{	
	 var list = [];
		return list
	}-*/;	
	public static native JavaScriptObject addIcoEntry(int Id, int parentId, String text, String search, JavaScriptObject children)
	/*-{
		var icoEntry={};
		icoEntry['id']=Id;
		icoEntry['parentId']=parentId;
		if (text!=null){
			icoEntry['label']=text;
		}
		else{
			icoEntry['label']="";
		}	
		if (search!=null){
			icoEntry['search']=search+", "+text;
		}
		else{
			icoEntry['search']="";
		}	
		if (children!=null){
			icoEntry['children']=children;
		}
		icoEntry["checked"]=false;
		return icoEntry
	}-*/;	

	public static native JavaScriptObject addZoomeImage(JavaScriptObject tiles, String source, String fileName)
	/*-{
		if (tiles==null){
			
			tiles={};
		}
		tiles[fileName]=source;
		
		return tiles
	}-*/;	
	public static native JavaScriptObject addImageFileNames(JavaScriptObject ifn,  String source)
	/*-{
		if (ifn==null){
			
			ifn=[source];
			        
		}
		else{
			ifn.push(source);
		}
		return ifn
	}-*/;
	public static native JavaScriptObject addImageDic(JavaScriptObject imgElDic, String source, Element imgEl)
	/*-{
		if (imgElDic==null){
			
			imgElDic={};
			        
		}
		
		imgElDic[source]=imgEl;
		//$wnd.console.log("Adding image: ",source);
		return imgElDic
	}-*/;
	public ArrayList<JavaScriptObject> loadTile(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic,ImageEntry image, String context, boolean annotation, boolean isWall) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);			
		return loadTiles(list, ifn,imgDic, images, context,annotation, isWall);
	}

	public ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation, boolean isWall) {
		ArrayList<ImageEntry> imagesclone = new ArrayList<ImageEntry>();
		for (ImageEntry ie : images) {
			imagesclone.add(ie);
		}
		return processTiles(list, ifn,imgDic, imagesclone, context, annotation, isWall);
	}
	public ArrayList<AnnotationEntry> getAnnotations(){
		return osdListener.getAnnotations();
	}
//	public void setIconography(TreeStore<IconographyEntry> icoTree) {
//		this.icoTree =icoTree;
//
//	}
	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation, boolean isWall) {		
		if (!images.isEmpty()){
			ImageEntry ie=images.remove(0);
//			String url="https://iiif.saw-leipzig.de/";
//			String url="http://127.0.0.1:8182/";
//			String url = "resource?imageID=" + ie.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri();
			//Util.doLogging(url+"iiif/2/kucha%2Fimages%2F" + ie.getFilename())
			//Util.doLogging("Adding URL: "+context + ie.getFilename() + "/info.json");
			if (isWall) {
				list = addZoomeImage(list , context + "blank.png/info.json",ie.getFilename());
			} else {
				list = addZoomeImage(list , context + ie.getFilename() + "/info.json",ie.getFilename());
			}
//			list = addZoomeImage(list , url,ie.getFilename());
			ifn=addImageFileNames(ifn,ie.getFilename());
			String dummy = ie.getFilename();

			if (annotation){
				Element imgEl = Document.get().getElementById("anno_"+dummy);
				//Util.doLogging("adding anno to dic_key");
				imgDic=addImageDic(imgDic,"anno_"+ie.getFilename(), imgEl );
			}
			else
			{
				Element imgEl = Document.get().getElementById(dummy);
				imgDic=addImageDic(imgDic,ie.getFilename(), imgEl );
			}
			processTiles(list, ifn,imgDic, images, context, annotation, isWall);
		}
		ArrayList<JavaScriptObject> result= new ArrayList<JavaScriptObject>(); 
		result.add(list);//http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3321165
		result.add(imgDic);
		result.add(ifn);
		return result;
	}
}
