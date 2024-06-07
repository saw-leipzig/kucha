package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
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
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractOSDLoader;
import de.cses.client.ui.OSDListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.ExportEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;

interface ExportProperties extends PropertyAccess<ExportEntry> {

	ModelKeyProvider<ExportEntry> uniqueID();

	LabelProvider<ExportEntry> name();

}
interface ExportViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml exportLabel(String name);
}

public class OSDLoaderEditorImageAnnotation extends AbstractOSDLoader {
	
	private ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
	private TreeStore<IconographyEntry> icoTree =null;
	private Collection<IconographyEntry> icoEntries = null;
	ExportProperties exportProps = GWT.create(ExportProperties.class);
	
	public OSDLoaderEditorImageAnnotation(ArrayList<ImageEntry> images, boolean annotation, TreeStore<IconographyEntry> icoTree, OSDListener osdListener) {
		
		super(annotation, null, osdListener, "multipolygon", false, getHighlighter());
		Util.doLogging("initiating icotree");
		this.images = images;
		this.icoTree=icoTree;
		if (icoTree!=null) {
			icoTree.setEnableFilters(false);
			icoEntries = icoTree.getRootItems();
		}
		JavaScriptObject icos;
		icos = listConverter(icoEntries);
		this.editor = constructsEditorsJS(icos, this);
		
		// TODO Auto-generated constructor stub
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
		loadTilesStart(images, context, annotation, editor);
	}
	public void loadTilesStart(ArrayList<ImageEntry> images, String context, boolean annotation, JavaScriptObject editor) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation, readOnly);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		if (osdListener!=null) {
			annos= generateW3CAnnotations(osdListener.getAnnotations());
		}
		viewers= createZoomImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, editor, this, annos, readOnly, annotationType, disableEditor, highlighter, "");	
	}

	private static native JavaScriptObject getHighlighter() 
	/*-{
	  		MyHighlightFormatter = function(annotation) {
	        	return 'highlight';
	        }
	  		return MyHighlightFormatter
	}-*/;
	public void selectAnnotation(JavaScriptObject anno) {
	}

	public void changeSelectionTarget(JavaScriptObject anno) {
	}

	public void createSelection(JavaScriptObject anno) {
	}

	public void startSelection(JavaScriptObject anno) {
	}

	public void annoCreated (JavaScriptObject annotation) {
		annoCreatedJS(annotation, this);
	};
	public static native void annoCreatedJS(JavaScriptObject annotation, OSDLoaderEditorImageAnnotation _self)
	/*-{
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
		_self.@de.cses.client.depictions.OSDLoaderEditorImageAnnotation::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,polygonRes,image, false, false, annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
	}-*/;
	public void annoChanged (JavaScriptObject annotation) {
		annoChangedJS(annotation, this);
	};	
	public static native void annoChangedJS(JavaScriptObject annotation, OSDLoaderEditorImageAnnotation _self)
	/*-{
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
		_self.@de.cses.client.depictions.OSDLoaderEditorImageAnnotation::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, false, true, annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
	}-*/;
	
	public void annoDeleted (JavaScriptObject annotation) {
		annoDeletedJS(annotation, this);
	};
	
	public static native void annoDeletedJS(JavaScriptObject annotation, OSDLoaderEditorImageAnnotation _self)
	/*-{
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
		_self.@de.cses.client.depictions.OSDLoaderEditorImageAnnotation::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, true, false,annotation.created?annotation.created:-1, annotation.modified?annotation.modified:-1, annotation.creator.type === "computer"?true:false);
	}-*/;
	
	

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

	public static native JavaScriptObject constructsEditorsJS(JavaScriptObject icoTree, OSDLoaderEditorImageAnnotation _self)
	/*-{
	 var ExportWidget = function(args) {
		  var annoExport = function(evt) {
			_self.@de.cses.client.depictions.OSDLoaderEditorImageAnnotation::export(Ljava/lang/String;Ljava/lang/String;)(args.annotation.id, args.annotation.targets[0].source);		  
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
		var tw = $wnd.TreeWidget;
		return [{ widget: tw, tree: icoTree }, ExportWidget];
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

	public static native String toGeopJson(String polygon)
	/*-{
        // $wnd.console.log("WKT: ",polygon);
        var leser = new $wnd.wellknown(polygon);
        var res = JSON.stringify(leser)
		// $wnd.console.log("result: ",res);
		return res
	}-*/;

	protected static native String toWKT(String polygon)
	/*-{
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

	public void getResults(String id ,String tags, String polygon, String image, Boolean delete, Boolean update, Double creationTime, Double modificationTime, Boolean isProposed) {
		// Function for sending the Annotations to MYSQL-Backend
		Util.doLogging("Annotation recieved: Tag: "+tags+", Polygone: "+polygon+", Image: "+image+", delete: "+Boolean.toString(delete)+", update: "+Boolean.toString(update)+", DepictionID: " + osdListener.getDepictionID());
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
	public void export(String annotoriousID, String imageID) {
		Info.display("export", annotoriousID);
		PopupPanel modifiedPopUp = new PopupPanel();
		FramedPanel modifiedFP = new FramedPanel();
		modifiedFP.setHeading("Link Annotation " + annotoriousID);	    
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

}
