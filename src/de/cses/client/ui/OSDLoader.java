package de.cses.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.PreservationAttributeEntry;

public class OSDLoader {
	private JavaScriptObject osdDic;
	private JavaScriptObject jso;
	ArrayList<ImageEntry> images;
	private boolean annoation;
	private TreeStore<IconographyEntry> icoTree =null;
	//private Object osdLoader;
	private OSDListener osdListener;
	DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public OSDLoader(ArrayList<ImageEntry> images, boolean annotation, TreeStore<IconographyEntry> icoTree, OSDListener osdListener) {
		this.osdDic = createDic();
		this.images=images;
		this.annoation =annotation;
		this.icoTree=icoTree;
		//this.osdLoader=this;
		this.osdListener=osdListener;
	}
	public OSDLoader(ImageEntry image) {
		this.osdDic = createDic();
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		this.images=images;
		this.annoation =false;
		this.icoTree=null;
		//this.osdLoader=this;
	}
	public void getResults(String id ,String tags, String polygon, String image, Boolean delete, Boolean update) {
		Util.doLogging("Annotation recieved: Tag: "+tags+", Polygone: "+polygon+", Image: "+image+", delete: "+Boolean.toString(delete)+", update: "+Boolean.toString(update));
		ArrayList<IconographyEntry> newTags = new ArrayList<IconographyEntry>();
		String dummy[] = tags.split(";", -1);
		for (String tag : dummy) {
			IconographyEntry icoEntry= icoTree.findModelWithKey(tag);
			//Util.doLogging("found tag value: "+icoEntry.getText());
			newTags.add(icoEntry);
		}
		AnnotationEntry annoEntry = new AnnotationEntry(osdListener.getDepictionID(), id, newTags, polygon.substring(22,polygon.indexOf("\"></polygon></svg>")), image, delete, update);
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
		dbService.setAnnotationResults(annoEntry, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Util.doLogging(caught.getLocalizedMessage());
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean result) {
				Info.display("Annotation Saved",Boolean.toString(result));
			}
		});
		
	}
	public void startLoadingTiles(String context) {
		loadTiles(images, context, annoation, icoTree.getRootItems());
	}
	public JavaScriptObject listConverter(List<IconographyEntry> icoTree) {
		JavaScriptObject list = null;
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
		return list;
	}
	public void loadTiles(ImageEntry image, String context, boolean annotation, List<IconographyEntry> icoTree) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic, UserLogin.getInstance().getSessionID(), annotation, icos, this,null);
	}
	public void loadTiles(ArrayList<ImageEntry> images, String context, boolean annotation, List<IconographyEntry> icoTree) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		JavaScriptObject annos= generateW3CAnnotations(osdListener.getAnnotations());
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, icos, this, annos);

//		if (depictionID>0) {
//			dbService.getAnnotations(depictionID,new AsyncCallback<ArrayList<AnnotationEntry>>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					caught.printStackTrace();
//				}
//
//				@Override
//				public void onSuccess(ArrayList<AnnotationEntry> result) {
//					
//					JavaScriptObject annos= generateW3CAnnotations(result);
//					jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, icos, osdLoader, annos);
//				}
//			});
//
//		}
		
	}
	
	public void destroyAllViewers() {
		destroyAllViewersJava(jso);
	}
	private JavaScriptObject generateW3CAnnotations(ArrayList<AnnotationEntry> annoEntries) {
		JavaScriptObject w3cAnnotation = createList();
		for (AnnotationEntry annoEntry : annoEntries) {
			JavaScriptObject bodies = createList();
			for (IconographyEntry ie : annoEntry.getTags()) {
				bodies = addToBody(bodies,ie.getIconographyID(), ie.getText(), annoEntry.getImage());
			}
			w3cAnnotation = addAnnotation(w3cAnnotation,annoEntry.getAnnotoriousID(), bodies, annoEntry.getPolygone(), annoEntry.getImage());
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
	public static native JavaScriptObject addAnnotation(JavaScriptObject annos,String annotoriousID, JavaScriptObject tags, String polygone, String image)
	/*-{
		var anno={};
		anno["@context"]="http://www.w3.org/ns/anno.jsonld";
		anno["id"]=annotoriousID;
		anno["type"]="Annotation";
		anno["body"]=tags;
		var target={};
		var selector={};
		selector["type"]="SvgSelector";
		selector["conformsTo"]="http://www.w3.org/TR/media-frags/";
		selector["value"]="<svg><polygon points=\""+polygone+"\"></polygon></svg>";
		annos.push(anno);
		target["selector"]=selector;
		target["id"]= image,
		anno["target"]=target;
		//$wnd.console.log(annos);
	    return annos;
	}-*/;
	public void removeOrAddAnnotations(ArrayList<AnnotationEntry> annos, boolean add) {
		JavaScriptObject jsAnnos= generateW3CAnnotations(annos);
		removeOrAddAnnotationsJS(jso, jsAnnos, add);

	}
	public static native void removeOrAddAnnotationsJS(JavaScriptObject viewers, JavaScriptObject annos, Boolean add) 
	/*-{
	    $wnd.console.log("Adding Annotation: ", annos)
		for (var v in viewers["annotorious"]) {
				var savedAnnos=[];
				var foundAnno=false;
				for (var k = 0, length2 = annos.length; k < length2; k++){
					//$wnd.console.log("Comparing ",annos[k].target.id," to ",v)
					if (annos[k].target.id===v)
						if (add){
							viewers["annotorious"][v].addAnnotation(annos[k]);
							$wnd.console.log("Adding ",annos[k]," to ",v)
						}
						else{
							$wnd.console.log("removing ",annos[k]," from ",v)
							viewers["annotorious"][v].removeAnnotation(annos[k]);
						}
					
				}
				
		}
	}-*/;
	public void removeAllAnnotations() {
		removeAllAnnotationsJS(jso);
	}
	public static native void removeAllAnnotationsJS(JavaScriptObject viewers) 
	/*-{
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
		//$wnd.console.log(viewers);
		for (var k in viewers["dic"]) {
    		viewers["dic"][k].destroy();
    		viewers["dic"][k]=null;
    		delete viewers["dic"][k];
		};		
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].destroy();
    		viewers["annotorious"][k]=null;
    		delete viewers["annotorious"][k];
		};

	}-*/;
	public void highlightAnnotation(String annoID) {
		highlightAnnotationJS(jso, annoID);
	}
	public static native void highlightAnnotationJS(JavaScriptObject viewers, String annoID)
	/*-{
	 	//$wnd.console.log(viewers["annotorious"])
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].highlightAnnotation(annoID);
		};

	}-*/;
	public void deHighlightAnnotation(String annoID) {
		deHighlightAnnotationJS(jso, annoID);
	}
	public static native void deHighlightAnnotationJS(JavaScriptObject viewers, String annoID)
	/*-{
	 	//$wnd.console.log(viewers["annotorious"])
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].dehighlightAnnotation(annoID);
		};

	}-*/;

	
	public static native JavaScriptObject createZoomeImage(JavaScriptObject tiles,JavaScriptObject wheres, JavaScriptObject source, JavaScriptObject dic, String sessionID, boolean anno, JavaScriptObject icoTree, OSDLoader osdLoader, JavaScriptObject annos)
	/*-{
	 annotorious={};
	 function openFullscreen(where) {
  			if (where.requestFullscreen) {
    			where.requestFullscreen();
  			} else if (where.mozRequestFullScreen) { 
    			where.mozRequestFullScreen();
  			} else if (where.webkitRequestFullscreen) { 
    			where.webkitRequestFullscreen();
  			} else if (where.msRequestFullscreen) {
    			where.msRequestFullscreen();
  			}
  			else {
      			var el = $doc.documentElement;
      			el.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
    		}
  			
		}
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
	
	 for (var i = 0, length = wheres.length; i < length; i++){
		//$wnd.console.log("Processing image: ",wheres[i]," of ",wheres);
	 	if (!(wheres[i] in dic)){  
			//$wnd.console.log(wheres[i], "not in dic");
			
		 	dic[wheres[i]] =  $wnd.OpenSeadragon({
		        id: wheres[i],
		        showRotationControl: true,
		        showFlipControl: true,
		        maxZoomLevel: 100,
		        minZoomLevel: 0.001,
		        ajaxWithCredentials: true,
		        ajaxHeaders: {"SessionID": sessionID},
		        loadTilesWithAjax:true,
		        crossOriginPolicy: "Anonymous",
				prefixUrl: "scripts/openseadragon-bin-2.4.2/images/",
				tileSources: tiles[wheres[i]]
				
			}); 
			
			if (anno){
				var savedAnnos=[];
				var foundAnno=false;
				for (var k = 0, length2 = annos.length; k < length2; k++){
					if (annos[k].target.id===wheres[i]){
						savedAnnos.push(annos[k]);
						foundAnno=true;
					}
					
				}
				
				//$wnd.console.log("config");
				var config = {};
				config["readOnly"]=false;
				config["tagVocabulary"]=[];
				config["tree"]=icoTree;
				config["image"]=wheres[i];
				//$wnd.console.log("Adding Annotorious");
				annotorious[wheres[i]] = $wnd.OpenSeadragon.Annotorious(dic[wheres[i]],config);
				annotorious[wheres[i]].setDrawingTool("polygon");
				//$wnd.console.log("Adding Handler");
				annotorious[wheres[i]].on('createAnnotation',function(annotation) {
					//$wnd.console.log("annotation created");
					//$wnd.console.log(annotation);
					results="";
					var image = "";
					for (var key in annotation.body){
						if (results===""){
							results=annotation.body[key].id;
						}
						else{
							results=results+";"+annotation.body[key].id;
						}
					 	image=annotation.body[key].image;
					}		
					//$wnd.console.log("results: ",results);
					osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, false, false);
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
					 	image=annotation.body[key].image;
					}
					
					//$wnd.console.log("results: ",results);
					osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, true, false);
				});
				annotorious[wheres[i]].on('selectAnnotation', function(annotation) {
					$wnd.console.log("annotation selected.");
				});
				annotorious[wheres[i]].on('mouseEnterAnnotation', function(annotation, event) {
					$wnd.console.log("annotation entered.");
				});
				annotorious[wheres[i]].on('mouseLeaveAnnotation', function(annotation, event) {
					$wnd.console.log("annotation left.");
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
					 	image=annotation.body[key].image;
					}
					
					//$wnd.console.log("results: ",results);
					osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;)(annotation.id,results,annotation.target.selector.value,image, false, true);
				});
				//$wnd.console.log("Adding Annotations");
//				if (foundAnno){
//					$wnd.console.log("Found Annotation for picture ",wheres[i],": ",savedAnnos);
//					annotorious[wheres[i]].setAnnotations(savedAnnos);
//				}
			}		
				//$wnd.console.log("Adding Annotations");
			dic[wheres[i]].imagefilters({menuId:"menu"+wheres[i],
		    							 toolsLeft: 270
		    							});
			dic[wheres[i]].addHandler("pre-full-page", function (data) {
					data.preventDefaultAction=true;
					openFullscreen(data.eventSource.element);
			});

			//$wnd.console.log("Processing ended, i=",i, wheres.length);
			//$wnd.console.log(dic[wheres[i]].viewport.getContainerSize().x+" / "+dic[wheres[i]].viewport.getContainerSize().y);
  

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
		$wnd.console.log("Adding image: ",source);
		return imgElDic
	}-*/;
	public ArrayList<JavaScriptObject> loadTile(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic,ImageEntry image, String context, boolean annotation) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);			
		return loadTiles(list, ifn,imgDic, images, context,annotation);
	}

	public ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation) {
		ArrayList<ImageEntry> imagesclone = new ArrayList<ImageEntry>();
		for (ImageEntry ie : images) {
			imagesclone.add(ie);
		}
		return processTiles(list, ifn,imgDic, imagesclone, context, annotation);
	}
	public ArrayList<AnnotationEntry> getAnnotations(){
		return osdListener.getAnnotations();
	}
	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation) {		
		if (!images.isEmpty()){
			ImageEntry ie=images.remove(0);
			String url="https://iiif.saw-leipzig.de/";
//			String url="http://127.0.0.1:8182/";
//			String url = "resource?imageID=" + ie.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri();
			//Util.doLogging(url+"iiif/2/kucha%2Fimages%2F" + ie.getFilename())
			list = addZoomeImage(list , url+"iiif/2/"+context + ie.getFilename() + "/info.json",ie.getFilename());
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
			processTiles(list, ifn,imgDic, images, context, annotation);
		}
		ArrayList<JavaScriptObject> result= new ArrayList<JavaScriptObject>(); 
		result.add(list);//http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3321165
		result.add(imgDic);
		result.add(ifn);
		return result;
	}
}
