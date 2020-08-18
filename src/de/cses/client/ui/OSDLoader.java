package de.cses.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.UriUtils;

import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.ImageEntry;

public class OSDLoader {
	private JavaScriptObject osdDic;
	private JavaScriptObject jso;
	ArrayList<ImageEntry> images;
	public OSDLoader(ArrayList<ImageEntry> images, String context) {
		this.osdDic = createDic();
		this.images=images;
		loadTiles(images, context);
	}
	public OSDLoader(ImageEntry image, String context) {
		this.osdDic = createDic();
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		this.images=images;
		loadTiles(images, context);
	}
	public void loadTiles(ImageEntry image, String context) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		ArrayList<String> filenames = new ArrayList<String>();
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic, UserLogin.getInstance().getSessionID());
	}
	public void loadTiles(ArrayList<ImageEntry> images, String context) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		ArrayList<String> filenames = new ArrayList<String>();
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID());
	}
	
	public static void destroyAllViewers(JavaScriptObject jso) {
		destroyAllViewersJava(jso);
	}
	public static native JavaScriptObject destroyAllViewersJava(JavaScriptObject viewers)
	/*-{
		for (var k in viewers) {
    		viewers[k].destroy();
    		viewers[k]=null;
    		delete viewers[k];
		};
	}-*/;
	
	public static native JavaScriptObject createZoomeImage(JavaScriptObject tiles,JavaScriptObject wheres, JavaScriptObject source, JavaScriptObject dic, String sessionID)
	/*-{
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
	 	if (!(wheres[i] in dic)){  

		 	dic[wheres[i]] =  $wnd.OpenSeadragon({
		        id: wheres[i],
		        showRotationControl: true,
		        showFlipControl: true,
		        maxZoomLevel: 100,
		        ajaxWithCredentials: true,
		        ajaxHeaders: {"SessionID": sessionID},
		        loadTilesWithAjax:true,
		        crossOriginPolicy: "Anonymous",
				prefixUrl: "scripts/openseadragon-bin-2.4.2/images/",
				tileSources: tiles[wheres[i]]
				
			}); 

			dic[wheres[i]].imagefilters({menuId:"menu"+wheres[i],
		    							 toolsLeft: 270
		    							});
			dic[wheres[i]].addHandler("pre-full-page", function (data) {
					data.preventDefaultAction=true;
					openFullscreen(data.eventSource.element);
			});

		}
	 }
	    
		
		return dic
	}-*/;
	public static native JavaScriptObject createDic()
	/*-{	
	 var dic = {};
		return dic
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
		return imgElDic
	}-*/;
	public static ArrayList<JavaScriptObject> loadTile(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic,ImageEntry image, String context) {
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		return loadTiles(list, ifn,imgDic, images, context);
	}

	public static ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context) {
		ArrayList<ImageEntry> imagesclone = new ArrayList<ImageEntry>();
		for (ImageEntry ie : images) {
			imagesclone.add(ie);
		}
		return processTiles(list, ifn,imgDic, imagesclone, context);
	}

	public static ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context) {		
		if (!images.isEmpty()){
			ImageEntry ie=images.remove(0);
			String dummy = ie.getFilename();
			Element imgEl = Document.get().getElementById(dummy);
			String url="https://iiif.saw-leipzig.de/";
//			String url="http://127.0.0.1:8182/";
//			String url = "resource?imageID=" + ie.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri();
			//Util.doLogging(url+"iiif/2/kucha%2Fimages%2F" + ie.getFilename());
			list = addZoomeImage(list , url+"iiif/2/"+context + ie.getFilename() + "/info.json",ie.getFilename());
//			list = addZoomeImage(list , url,ie.getFilename());
			ifn=addImageFileNames(ifn,ie.getFilename());
			imgDic=addImageDic(imgDic,ie.getFilename(), imgEl );
			processTiles(list, ifn,imgDic, images, context);
		}
		ArrayList<JavaScriptObject> result= new ArrayList<JavaScriptObject>(); 
		result.add(list);//http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3321165
		result.add(imgDic);
		result.add(ifn);
		return result;
	}
}
