package de.cses.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
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
import de.cses.shared.ExportEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.WallDimensionEntry;

public abstract class AbstractOSDLoader {
	protected JavaScriptObject osdDic;
	protected JavaScriptObject viewers;
	protected ArrayList<ImageEntry> images;
	protected boolean annotation;
	protected JavaScriptObject editor =null;
	protected String annotationType = "";
	protected boolean disableEditor = false;
	protected Collection<IconographyEntry> icoEntries = null;
	protected boolean readOnly=false;
	protected JavaScriptObject highlighter = null;
	protected HashMap<String, JavaScriptObject> peAnnos = new HashMap<String, JavaScriptObject>();
	//private Object osdLoader;
	protected OSDListener osdListener;
	protected JavaScriptObject annos = null;
	protected DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public abstract void annoCreated (JavaScriptObject annotation);
	public abstract void annoChanged (JavaScriptObject annotation);
	public abstract void createSelection (JavaScriptObject annotation);
	public abstract void startSelection (JavaScriptObject annotation);
	public abstract void selectAnnotation (JavaScriptObject annotation);
	public abstract void changeSelectionTarget (JavaScriptObject annotation);
	public abstract void annoDeleted (JavaScriptObject annotation);
	
	
	public AbstractOSDLoader(boolean annotation, JavaScriptObject editor, OSDListener osdListener, String annotationType, boolean disableEditor, JavaScriptObject highlighter) {
		// Constructor for DepictionEntry
		// We need to initiate a Javascript Object in Javascript Code
		this.osdDic = createDic();
		this.images=images;
		this.annotation =annotation;
		this.editor=editor;
		this.osdListener=osdListener;
		this.annotationType = annotationType;
		this.disableEditor = disableEditor;
		this.highlighter = highlighter;
	}
	abstract public void setosd();
	
	public void destroyAllViewers() {
		destroyAllViewersJava(viewers);
	}
	public void makeEditableAllViewers() {
		makeEditableAllViewersJava(viewers);
	}
	public void makeReadOnlyAllViewers() {
		makeReadOnlyAllViewersJava(viewers);
	}
	public void setEditor(JavaScriptObject editor) {
		setEditorJS(viewers, editor);
	}
	public static native void setEditorJS(JavaScriptObject viewers, JavaScriptObject editor)
	/*-{
		if (viewers!=null){
			if (viewers["annotorious"]!=null){				
				for (var k in viewers["annotorious"]) {
		    		viewers["annotorious"][k].widgets = editor;
				};
			}
		}

	}-*/;
	protected JavaScriptObject generateW3CAnnotations(ArrayList<AnnotationEntry> annoEntries) {
		JavaScriptObject w3cAnnotation = createList();
		if (annoEntries!=null) {
			for (AnnotationEntry annoEntry : annoEntries) {
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
		removeOrAddAnnotationsJS(viewers, jsAnnos, add);

	}
	public static native void removeOrAddAnnotationsJS(JavaScriptObject viewers, JavaScriptObject annos, Boolean add) 
	/*-{
		$wnd.console.log("adding", viewers, annos);
	    if (viewers["annotorious"]!=null){
	    	for (var v in viewers["annotorious"]) {
				var annosForViewer = [];
				for (var k = 0, length2 = annos.length; k < length2; k++){
					if (annos[k].target.source===v){
						if (add){
							annosForViewer.push(annos[k]);
						}
						else{
							viewers["annotorious"][v].removeAnnotation(annos[k]);
						}
					}	
				}
				viewers["annotorious"][v].setAnnotations(annosForViewer);	
			}	
	    }
	}-*/;
	public void removeAllAnnotations() {
		removeAllAnnotationsJS(viewers);
	}
	public void cancelSelected() {
		cancelSelectedJS(viewers);
	}
	public void updateAnnotation(JavaScriptObject anno) {
		updateAnnotationJS(viewers, anno);
	}
	
	public static native void updateAnnotationJS(JavaScriptObject viewers, JavaScriptObject anno) 
	/*-{
		for (var v in viewers["annotorious"]) {
				viewers["annotorious"][v].updateAnnotation(anno);
		}
	}-*/;
	
	public static native void cancelSelectedJS(JavaScriptObject viewers) 
	/*-{
		for (var v in viewers["annotorious"]) {
				viewers["annotorious"][v].cancelSelected();
		}
	}-*/;

	
	public static native void removeAllAnnotationsJS(JavaScriptObject viewers) 
	/*-{
		for (var v in viewers["annotorious"]) {
				var emptyAnnos=[];
				viewers["annotorious"][v].setAnnotations(emptyAnnos);
		}
	}-*/;

	public static native JavaScriptObject destroyAllViewersJava(JavaScriptObject viewers)
	/*-{
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
	public static native JavaScriptObject makeEditableAllViewersJava(JavaScriptObject viewers)
	/*-{
		if (viewers!=null){
			if (viewers["annotorious"]!=null){				
				for (var k in viewers["annotorious"]) {
		    		viewers["annotorious"][k].readOnly = false;
		    		viewers["annotorious"][k].setDrawingEnabled(true);
				};
			}
		}

	}-*/;
	public static native JavaScriptObject makeReadOnlyAllViewersJava(JavaScriptObject viewers)
	/*-{
		if (viewers!=null){
			if (viewers["annotorious"]!=null){				
				for (var k in viewers["annotorious"]) {
		    		viewers["annotorious"][k].setDrawingEnabled(false);
		    		viewers["annotorious"][k].readOnly = true;
				};
			}
		}

	}-*/;
	public static native JavaScriptObject createZoomImage(JavaScriptObject tiles,JavaScriptObject wheres, JavaScriptObject source, JavaScriptObject dic, String sessionID, boolean anno, JavaScriptObject editor, AbstractOSDLoader osdLoader, JavaScriptObject annos, boolean readOnly, String annotationType, boolean disableEditor, JavaScriptObject highlighter)
	/*-{
	 	annotorious={};
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
		 	for (var i = 0, length = wheres.length; i < length; i++){
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
						prefixUrl: "scripts/openseadragon-bin-2.4.2/images/",
					}); 
					$wnd.console.log(tiles[wheres[i]], sessionID);
			        dic[wheres[i]].open({
			            // The headers specified here will be combined with those in the Viewer object (if any)
			            ajaxHeaders:  {"SessionID": sessionID},
			            tileSource: tiles[wheres[i]]
			        });			
					if (anno){
						var savedAnnos=[];
						var foundAnno=false;
						for (var k = 0, length2 = annos.length; k < length2; k++){
							if (annos[k].target.source===wheres[i]){
								savedAnnos.push(annos[k]);
								foundAnno=true;
							}
						}
						var config = {};
						config["locale"] = 'auto',
						config["allowEmpty"] = true;
						config["inverted"] = false;
						config["drawOnSingleClick"] = false
						config["widgets"]=editor;
						if (readOnly){
							config["readOnly"]=true;
						} else {
							config["readOnly"]=false;
						}
						config["image"]=wheres[i];
						config["formatter"] = [highlighter];
						config["disableEditor"] = disableEditor;
						annotorious[wheres[i]] = $wnd.OpenSeadragon.Annotorious(dic[wheres[i]],config);
				        $wnd.Annotorious.SelectorPack(annotorious[wheres[i]]);
				        annotorious[wheres[i]].setDrawingTool(annotationType);
						annotorious[wheres[i]].on('createAnnotation',function(annotation) {
						annotation["creator"] = annotation.creator?annotation.creator:{"type":"human"}
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::annoCreated(Lcom/google/gwt/core/client/JavaScriptObject;)(annotation);
						});
						annotorious[wheres[i]].on('deleteAnnotation', function(annotation) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::annoDeleted(Lcom/google/gwt/core/client/JavaScriptObject;)(annotation);
						});
						annotorious[wheres[i]].on('selectAnnotation', function(annotation) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::selectAnnotation(Lcom/google/gwt/core/client/JavaScriptObject;)(annotation);
						});
						annotorious[wheres[i]].on('changeSelectionTarget', function(target) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::changeSelectionTarget(Lcom/google/gwt/core/client/JavaScriptObject;)(target);
						});
						annotorious[wheres[i]].on('startSelection', function(point) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::startSelection(Lcom/google/gwt/core/client/JavaScriptObject;)(point);
						});
						annotorious[wheres[i]].on('createSelection', function(selection) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::createSelection(Lcom/google/gwt/core/client/JavaScriptObject;)(selection);
						});
						annotorious[wheres[i]].on('selectAnnotation', function(annotation, element) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::selectAnnotation(Lcom/google/gwt/core/client/JavaScriptObject;)(annotation);
						});
						annotorious[wheres[i]].on('updateAnnotation', function(annotation, previous) {
							osdLoader.@de.cses.client.ui.AbstractOSDLoader::annoChanged(Lcom/google/gwt/core/client/JavaScriptObject;)(annotation);
						});
					}		
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
						} else {
      						var el = $doc.documentElement;
	      					el.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
	    				}
					});
				}
	 		}
	 	}
		return {"dic":dic,"annotorious":annotorious}
	}-*/;
	public ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation, boolean readOnly) {
		ArrayList<ImageEntry> imagesclone = new ArrayList<ImageEntry>();
		for (ImageEntry ie : images) {
			imagesclone.add(ie);
		}
		return processTiles(list, ifn,imgDic, imagesclone, context, annotation, readOnly);
	}
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

	public static native JavaScriptObject addZoomImage(JavaScriptObject tiles, String source, String fileName)
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
		return imgElDic
	}-*/;


	public ArrayList<AnnotationEntry> getAnnotations(){
		return osdListener.getAnnotations();
	}

	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation, boolean readOnly) {		
		if (!images.isEmpty()){
			ImageEntry ie=images.remove(0);
			Util.doLogging(context + ie.getFilename() + "/info.json");
			list = addZoomImage(list , context + ie.getFilename() + "/info.json",ie.getFilename());
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
			processTiles(list, ifn,imgDic, images, context, annotation, readOnly);
		}
		ArrayList<JavaScriptObject> result= new ArrayList<JavaScriptObject>(); 
		result.add(list);//http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3321165
		result.add(imgDic);
		result.add(ifn);
		return result;
	}

}
