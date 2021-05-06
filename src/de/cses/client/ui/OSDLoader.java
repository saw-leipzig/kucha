package de.cses.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.server.WKT2SVG;
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
	private Collection<IconographyEntry> icoEntries = null;
	//private Object osdLoader;
	private OSDListener osdListener;
	DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public OSDLoader(ArrayList<ImageEntry> images, boolean annotation, TreeStore<IconographyEntry> icoTree, OSDListener osdListener) {
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
		this.osdDic = createDic();
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(image);
		this.images=images;
		this.annoation =false;
		this.icoTree=null;
		//this.osdLoader=this;
	}
	public void getResults(String id ,String tags, String polygon, String image, Boolean delete, Boolean update) {
		Util.doLogging("Annotation recieved: Tag: "+tags+", Polygone: "+polygon+", Image: "+image+", delete: "+Boolean.toString(delete)+", update: "+Boolean.toString(update)+", DepictionID: "+osdListener.getDepictionID());
		ArrayList<IconographyEntry> newTags = new ArrayList<IconographyEntry>();
		String dummy[] = tags.split(";", -1);
		for (String tag : dummy) {
			IconographyEntry icoEntry= icoTree.findModelWithKey(tag);
			//Util.doLogging("found tag value: "+icoEntry.getText());
			newTags.add(icoEntry);
		}
		//AnnotationEntry annoEntry = new AnnotationEntry(osdListener.getDepictionID(), id, newTags, polygon.substring(22,polygon.indexOf("\"></polygon></svg>")), image, delete, update);
		String svgRaw = polygon.replace("<svg><path d=\"","").replace("\"></path></svg>","");
		Util.doLogging("Starting Converter for SVG to WKT");
		String newPoly = toWKT(svgRaw);
		if (newPoly != "") {
			Util.doLogging("Starting Converter for WKT to GeoJSON");
			String newPolyGeoJson=toGeopJson(newPoly);
			Util.doLogging("Writing Annotation");
			AnnotationEntry annoEntry = new AnnotationEntry(osdListener.getDepictionID(), id, newTags, newPolyGeoJson, image, delete, update);
			annoEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());		
			Util.doLogging("Poly after toGeoJson"+newPoly);
			AnnotationEntry annoEntryDB = new AnnotationEntry(annoEntry.getDepictionID(), annoEntry.getAnnotoriousID(), annoEntry.getTags(), newPoly, annoEntry.getImage(), annoEntry.getDelete(), annoEntry.getUpdate());
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

	public void startLoadingTiles(String context) {
		Util.doLogging("start loading osd");
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
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic, UserLogin.getInstance().getSessionID(), annotation, icos, this,null);
	}
	public void loadTiles(ArrayList<ImageEntry> images, String context, boolean annotation, Collection<IconographyEntry> icoTree) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject icos = listConverter(icoTree);
		JavaScriptObject annos = null;
		if (osdListener!=null) {
			annos= generateW3CAnnotations(osdListener.getAnnotations());			
		}
		jso= createZoomeImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, icos, this, annos);	
	}
	
	public void destroyAllViewers() {
		destroyAllViewersJava(jso);
	}
	private JavaScriptObject generateW3CAnnotations(ArrayList<AnnotationEntry> annoEntries) {
		JavaScriptObject w3cAnnotation = createList();
		if (annoEntries!=null) {
			for (AnnotationEntry annoEntry : annoEntries) {
				//Util.doLogging(annoEntry.getPolygone());
				JavaScriptObject bodies = createList();
				for (IconographyEntry ie : annoEntry.getTags()) {
					bodies = addToBody(bodies,ie.getIconographyID(), ie.getText(), annoEntry.getImage());
				}
				w3cAnnotation = addAnnotation(w3cAnnotation,annoEntry.getAnnotoriousID(), bodies, annoEntry.getPolygone(), annoEntry.getImage());
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
		var root = $wnd.JSON.parse(polygone);
		//$wnd.console.log(root);
		var geoGenerator=$wnd.d3.geoPath()
		selector["value"]="<svg><path d=\""+geoGenerator(root)+"\"></path></svg>";
		target["selector"]=selector;
		target["source"]= image,
		anno["target"]=target;
		annos.push(anno);
	    return annos;
	}-*/;
	public void removeOrAddAnnotations(ArrayList<AnnotationEntry> annos, boolean add) {
		JavaScriptObject jsAnnos= generateW3CAnnotations(annos);
		removeOrAddAnnotationsJS(jso, jsAnnos, add);

	}
	public static native void removeOrAddAnnotationsJS(JavaScriptObject viewers, JavaScriptObject annos, Boolean add) 
	/*-{
	    //$wnd.console.log("Adding Annotation: ", annos)
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
				
				viewers["annotorious"][v].loadAnnotationsfromObject(annosForViewer);	
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
		$wnd.console.log("destroy all viewers Started");
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
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].highlightAnnotation(annoID);
		};

	}-*/;
	public void deHighlightAnnotation(String annoID) {
		deHighlightAnnotationJS(jso, annoID);
	}
	public static native void deHighlightAnnotationJS(JavaScriptObject viewers, String annoID)
	/*-{
	 	//$wnd.console.log("deHighlightAnnotationsJS started");
		for (var k in viewers["annotorious"]) {
    		viewers["annotorious"][k].dehighlightAnnotation(annoID);
		};

	}-*/;
	public static native String toWKT(String polygon)
	/*-{
		try {
	        var union =null;
	        $wnd.console.log("toWKT triggered");
	        //polygon = "M319.91400146484375,1052.1756591796875 L297.0713806152344,1021.1749877929688 L285.65008544921875,967.3316650390625 L288.9132995605469,939.5941772460938 L300.3346252441406,874.3295288085938 L310.12432861328125,835.1707763671875 L324.8088684082031,779.69580078125 L342.7566223144531,742.1686401367188 L368.86248779296875,686.6937255859375 L406.3896484375,654.0614013671875 L427.60064697265625,641.0084838867188 L453.7065124511719,624.6923217773438 L470.02264404296875,621.4290771484375 L489.60205078125,621.4290771484375 L536.9188842773438,587.1651611328125 L530.3924560546875,572.4806518554688 L522.234375,557.7960815429688 L518.9711303710938,548.0064086914062 L520.6027221679688,531.6902465820312 L523.865966796875,523.5321655273438 L533.6557006835938,510.4792175292969 L548.3402099609375,495.794677734375 L572.814453125,494.1630554199219 L589.130615234375,486.0050048828125 L598.9202880859375,477.846923828125 L608.7100219726562,474.58367919921875 L626.6577758789062,474.58367919921875 L665.8165283203125,489.2682189941406 L677.2378540039062,494.1630554199219 L687.0275268554688,507.21600341796875 L693.5540161132812,539.8483276367188 L693.5540161132812,549.6380004882812 L693.5540161132812,556.1644897460938 L687.0275268554688,561.059326171875 L683.7643432617188,580.6387329101562 L678.8694458007812,598.5864868164062 L701.7120971679688,595.3232421875 L711.5017700195312,596.954833984375 L726.1863403320312,605.1129150390625 L739.2392578125,610.0078125 L750.6605834960938,621.4290771484375 L758.8186645507812,637.7452392578125 L760.4502563476562,658.9562377929688 L760.4502563476562,681.7988891601562 L750.6605834960938,725.8524780273438 L734.3444213867188,769.9061279296875 L734.3444213867188,781.3274536132812 L734.3444213867188,787.8538818359375 L704.975341796875,825.3810424804688 L677.2378540039062,848.2236938476562 L669.0797729492188,848.2236938476562 L667.4481811523438,840.0656127929688 L660.9216918945312,849.8552856445312 L659.2901000976562,858.0133666992188 L641.34228515625,869.4346923828125 L634.8158569335938,869.4346923828125 L628.2893676757812,866.1714477539062 L613.6048583984375,880.8560180664062 L589.130615234375,890.6456909179688 L567.9196166992188,902.0670166015625 L530.3924560546875,908.5934448242188 L504.2865905761719,905.3302612304688 L491.2336730957031,897.1721801757812 L486.33880615234375,890.6456909179688 L474.9175109863281,911.856689453125 L463.4961853027344,933.0676879882812 L455.3381042480469,964.0684204101562 L450.4432678222656,980.3845825195312 L422.705810546875,1003.2271728515625 L414.5477294921875,1016.2800903320312 L411.28448486328125,1039.1226806640625 L408.0212707519531,1061.96533203125 L414.5477294921875,1094.59765625 L414.5477294921875,1104.3873291015625 L419.44256591796875,1143.546142578125 L427.60064697265625,1159.8623046875 L430.8638916015625,1164.7572021484375 L409.6528625488281,1168.0203857421875 L394.96832275390625,1151.7042236328125 L390.073486328125,1179.441650390625 L375.3889465332031,1192.49462890625 L350.9147033691406,1207.17919921875 L342.7566223144531,1197.389404296875 L347.6514892578125,1171.2835693359375 L355.8095703125,1140.282958984375 L360.70440673828125,1133.7564697265625 L357.441162109375,1123.966796875 L346.0198669433594,1141.91455078125 L341.125,1159.8623046875 L331.3353271484375,1185.9681396484375 L310.12432861328125,1203.9158935546875 L300.3346252441406,1205.5474853515625 L293.80816650390625,1200.6527099609375 L290.544921875,1189.2313232421875 L295.43975830078125,1176.178466796875 L298.7030029296875,1166.3887939453125 L301.96624755859375,1151.7042236328125 L303.59783935546875,1133.7564697265625 L301.96624755859375,1120.7034912109375 L297.0713806152344,1065.2286376953125 L298.7030029296875,1061.96533203125 L300.3346252441406,1055.4388427734375 L301.96624755859375,1053.8072509765625 L311.75592041015625,1060.333740234375 L323.17724609375,1057.070556640625 L326.4404602050781,1058.7021484375 L319.91400146484375,1052.1756591796875 ZM553.2350463867188,670.3775634765625 L554.86669921875,743.80029296875 L548.3402099609375,771.5377197265625 L549.9718017578125,786.2222900390625 L551.6034545898438,807.4332885742188 L543.4453735351562,838.4340209960938 L563.0247802734375,828.644287109375 L571.182861328125,825.3810424804688 L600.5519409179688,823.7494506835938 L615.2364501953125,828.644287109375 L620.1312866210938,825.3810424804688 L616.8681030273438,804.1700439453125 L620.1312866210938,784.5906982421875 L625.0261840820312,774.8009643554688 L628.2893676757812,760.116455078125 L636.4474487304688,748.6951293945312 L641.34228515625,735.6422119140625 L654.395263671875,727.484130859375 L664.1849365234375,720.9576416015625 L677.2378540039062,719.3260498046875 L685.3959350585938,719.3260498046875 L696.8172607421875,680.167236328125 L672.343017578125,673.6408081054688 L664.1849365234375,670.3775634765625 L652.7636108398438,660.587890625 L646.2371826171875,662.219482421875 L642.9739379882812,676.904052734375 L636.4474487304688,686.6937255859375 L620.1312866210938,703.0098876953125 L603.8151245117188,704.6414794921875 L595.6570434570312,704.6414794921875 L585.8673706054688,698.1150512695312 L569.5512084960938,686.6937255859375 L553.2350463867188,672.0092163085938 L553.2350463867188,670.3775634765625 Z"
	        $wnd.console.log("polygon: ",polygon);
	        var results =polygon.split('M');
	        $wnd.console.log("results: ");
	        $wnd.console.log("results: ",results);
	        results.forEach(function (result, index) {
	          $wnd.console.log("result: ",result);
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
	          	  $wnd.console.log("loop");
	          	  $wnd.console.log("coord", parseFloat(coord.split(",")[0]).toFixed(2).toString(), " ",parseFloat(coord.split(",")[1]).toFixed(2).toString());
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
	        	
	            var leser = new $wnd.jsts.io.WKTReader();
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
		   $wnd.alert("Failed to convert polygon.");
		   return "";
		}			
	}-*/;
	public static native String toGeopJson(String polygon)
	/*-{
        $wnd.console.log("WKT: ",polygon);
        var leser = new $wnd.wellknown(polygon);
        var res = JSON.stringify(leser)
		$wnd.console.log("result: ",res);
		return res
	}-*/;
	
	public static native JavaScriptObject createZoomeImage(JavaScriptObject tiles,JavaScriptObject wheres, JavaScriptObject source, JavaScriptObject dic, String sessionID, boolean anno, JavaScriptObject icoTree, OSDLoader osdLoader, JavaScriptObject annos)
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
	 for (var i = 0, length = wheres.length; i < length; i++){
		//$wnd.console.log("Processing image, request header: ",$wnd.request.headers);
	 	if (!(wheres[i] in dic)){  
			//$wnd.console.log(wheres[i], tiles[wheres[i]);
			
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
					if (annos[k].target.source===wheres[i]){
						savedAnnos.push(annos[k]);
						foundAnno=true;
					}
					
				}
				
				//$wnd.console.log("config");
				var config = {};
				config["locale"] = 'auto',
				config["readOnly"]=false;
				config["widgets"]=[{ widget: 'TREE', tree: icoTree }];
				config["image"]=wheres[i];
				$wnd.console.log("Adding Annotorious",config);
				annotorious[wheres[i]] = $wnd.OpenSeadragon.Annotorious(dic[wheres[i]],config);
				annotorious[wheres[i]].setDrawingTool("polygon");
				annotorious[wheres[i]].setDrawingEnabled(true);
				$wnd.console.log("Adding Handler");
				annotorious[wheres[i]].on('createAnnotation',function(annotation) {
					  $wnd.console.log("annotation",annotation);
						
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
						if (annotation.target.source.includes("kucha%2Fimages%2F")){
					 		image=annotation.target.source.split("kucha%2Fimages%2F")[1]
						} else {
							image=annotation.target.source
						}
							
					}		
					//$wnd.console.log("results: ",results);
					osdLoader.@de.cses.client.ui.OSDLoader::getResults(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;)(annotation.id,results,polygonRes,image, false, false);
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
						if (annotation.target.source.includes("kucha%2Fimages%2F")){
					 		image=annotation.target.source.split("kucha%2Fimages%2F")[1]
						} else {
							image=annotation.target.source
						}
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
						if (annotation.target.source.includes("kucha%2Fimages%2F")){
					 		image=annotation.target.source.split("kucha%2Fimages%2F")[1]
						} else {
							image=annotation.target.source
						}
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
		//$wnd.console.log("Adding image: ",source);
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
//	public void setIconography(TreeStore<IconographyEntry> icoTree) {
//		this.icoTree =icoTree;
//
//	}
	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, ArrayList<ImageEntry> images, String context, boolean annotation) {		
		if (!images.isEmpty()){
			ImageEntry ie=images.remove(0);
//			String url="https://iiif.saw-leipzig.de/";
//			String url="http://127.0.0.1:8182/";
//			String url = "resource?imageID=" + ie.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri();
			//Util.doLogging(url+"iiif/2/kucha%2Fimages%2F" + ie.getFilename())
			//Util.doLogging("Adding URL: "+context + ie.getFilename() + "/info.json");
			list = addZoomeImage(list , context + ie.getFilename() + "/info.json",ie.getFilename());
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
