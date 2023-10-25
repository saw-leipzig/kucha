package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
import de.cses.shared.PositionEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallSketchEntry;

interface ExportProperties extends PropertyAccess<ExportEntry> {

	ModelKeyProvider<ExportEntry> uniqueID();

	LabelProvider<ExportEntry> name();

}
interface ExportViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml exportLabel(String name);
}

public class OSDLoaderWallDimension extends AbstractOSDLoader {
	
	private WallDimensionEntry wde;
	
	public OSDLoaderWallDimension(WallDimensionEntry wde, boolean annotation, OSDListener osdListener) {
		
		super(annotation, null, osdListener, "rect", false, getHighlighter());
		this.osdDic = createDic();
		this.editor = constructsEditorRegisterJS(this);
		this.annos = null;
		this.wde = wde;
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		ImageEntry image = new ImageEntry();
		image.setFilename(wde.getWallSketch().getFilename());
		images.add(image);
		this.images=images;
		this.annotation =true;
		//this.osdLoader=this;
		
		// TODO Auto-generated constructor stub
	}
	public static native JavaScriptObject createMap(JavaScriptObject depictionDic, int rows, int cols, float x, float y, float height, float width, boolean isRect, String filename, int direction) 
	/*-{
        function isOdd(num) { return num % 2;}
        function addRhombus(row,col, rows, cols, height, width, rowNumber, colNumber){
          var heightRhomb = 0; 
          heightRhomb = (rows>1 ? (height*2)/(rows-1) : (height*2)/(1))*(((row)/2));
          var heightrow = rows>1 ? (height*2)/(rows-1) : (height*2)/(1);
          var newPath = "";
          if (isOdd(row) === 0){
            newPath = "M"+ (x + ((width*((2*col)-1)) / ((2*cols) - 2))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"L" + (x + ((col === cols) ? width : ((((col * width) / ((cols) - 1)))))) + "," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow+(heightrow/2))))+"L"+ (x + (((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2))) +"," + (y + ((row === rows) ? height : (heightRhomb)))+"L" + (x + ((((col+1) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width))*2))) + "," + (y + ((heightrow/2)+heightRhomb-heightrow))+"L"+(x + (((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2))) +"," + (y + (heightRhomb-heightrow))+"z"; 
          } else {
            newPath = "M"+(x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"L" + (x + ((col === cols) ? width : ((((width * (2*col-1)) / ((2 * cols) - 2)))))) + "," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow+(heightrow/2))))+"L"+(x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) + "," + (y + ((row === rows) ? height : (heightRhomb)))+"L" + (x + ((col === 1) ? 0 : ((((col) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width)))-((((1) / ((cols) - 1) * width)/2))))) + "," + (y + ((heightrow/2)+heightRhomb-heightrow))+"L"+ (x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"z";
          }
          var depictionIDs = null
          if (rowNumber in depictionDic){
          	if (colNumber in depictionDic[rowNumber]){
          		depictionIDs = depictionDic[rowNumber][colNumber]
          	} else {
          		depictionIDs = []
          	}
          } else {
          	depictionIDs = []
          }
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "Reg. "+rowNumber+", No. "+colNumber,
            "type": "Annotation",
            "body": [
              {
                  "type": "TextualBody",
                  "value": "{\"Reg\": \"" + rowNumber + "\", \"No\": \"" + colNumber + "\", \"Depiction\": \"" + depictionIDs.toString()  +"\"}",
                  "id": "Reg. "+rowNumber+", No. "+colNumber,
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
        function addRect(row,col, rows, cols, height, width, rowNumber, colNumber){
          var heightRhomb = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var heightrow = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var newPath = "";
          newPath = "xywh=pixel:"+(width-((width/cols)*col))+","+(height-((height/rows)*row))+","+(width/cols)+","+(height/rows);
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "Reg. "+rowNumber+", No. "+colNumber,
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
        var rowNumber = 0;
        for (var j = 1; j< rows+1; j++){
        	rowNumber += 1;
        	var colNumber = direction === 0 ? 0: cols        
          for(var i = 1; i < (cols+1); i+=1){
        	if (direction === 0){
        		colNumber += 1
        	} else {
        		colNumber -= 1
        	}
          	var annotationObject = {};
            if (isRect){
                annotationObject = addRect(j,i,rows,cols,height, width, rowNumber, colNumber)
            } else {
              if (!((i === cols) && (isOdd(j) === 0))){
                annotationObject = addRhombus(j,i,rows,cols,height, width, rowNumber, colNumber)
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
	public void changePosition() {
		setEditor(constructsEditorDimensionJS(this));
		makeEditableAllViewers();
	}
	public void setosd() {
		 dbService.getOSDContextWallSketches(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(String result) {
				loadTilesStart(result);
			}
		});
	}
	
	public void loadTilesStart(String context) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, context, annotation, readOnly);
		JavaScriptObject tiles = results.remove(0);
		JavaScriptObject imgDic=results.remove(0);
		JavaScriptObject ifn=results.remove(0);
		JavaScriptObject depictionDic = createDic();
		for (PositionEntry pe: wde.getPositions()) {
			if (!pe.isdeleted()) {
				depictionDic = createDepictionDic(depictionDic, pe.getRegister(), pe.getNumber(), pe.getDepictionID());				
			}
		}
		annos = ConcatList(null, createMap(depictionDic, wde.getRegisters(),wde.getColumns(), wde.getX(), wde.getY(), wde.getH() , wde.getW(), wde.getType() == 0? false: true, wde.getWallSketch().getFilename(), wde.getDirection()));
		viewers = createZoomImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, editor, this, annos, readOnly, annotationType, disableEditor, highlighter);	
		removeOrAddAnnotationsJS(viewers, annos, true);
		makeReadOnlyAllViewersJava(viewers);
		setEditor(constructsEditorRegisterJS(this));
	}
	
	public static native JavaScriptObject createDepictionDic(JavaScriptObject depictionDic, int reg, int no, int depictionID)
	/*-{
		if (!(reg in depictionDic)){
			depictionDic[reg] = {}
		}
		if (!(no in depictionDic[reg])){
			depictionDic[reg][no] = []			
		}
		depictionDic[reg][no].push(depictionID)
		return depictionDic
	}-*/;	
	
	public ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, String context, boolean annotation, boolean readOnly) {
		return processTiles(list, ifn,imgDic, wde.getWallSketch(),context, annotation, readOnly);
	}
	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, WallSketchEntry wse, String context, boolean annotation, boolean readOnly) {		
		list = addZoomImage(list , context + wse.getFilename() + "/info.json",wse.getFilename());
		ifn=addImageFileNames(ifn,wse.getFilename());
		String dummy = wse.getFilename();
		if (annotation){
			Element imgEl = Document.get().getElementById("anno_"+dummy);
			imgDic=addImageDic(imgDic,"anno_" + wse.getFilename(), imgEl );
		}
		else
		{
			Element imgEl = Document.get().getElementById(dummy);
			imgDic=addImageDic(imgDic,wse.getFilename(), imgEl );
		}
		ArrayList<JavaScriptObject> result= new ArrayList<JavaScriptObject>(); 
		result.add(list);//http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3321165
		result.add(imgDic);
		result.add(ifn);
		return result;
	}
	public static native JavaScriptObject addZoomImageWall(JavaScriptObject tiles, String source, String fileName)
	/*-{
		if (tiles==null){
			
			tiles={};
		}
		tiles[fileName]={
			url: source,
			type: "image"
		};
		
		return tiles
	}-*/;	
	private static native JavaScriptObject getHighlighter() 
	/*-{

  		MyHighlightFormatter = function(annotation) {
  			if (annotation.body.length === 0){
  				return 'unset'
  			}
		  	var position
		  	if (typeof annotation.body[0].value !== 'object'){
		  		var position = JSON.parse(annotation.body[0].value)
		  	} else {
		  		var position = annotation.body[0].value
		  	}  	
		  	if (position["Depiction"] === "") {
		  		return 'unset'
		  	} else if (position["Depiction"] === "-1"){
  				return 'lost'
  			} else if (position["Depiction"] instanceof Array){
		  		for (var i = 0; i < position["Depiction"].length; i++){
					if (position["Depiction"][i] === "-1"){
  						return 'lost'
					}
		  		}
  				$wnd.console.log("returning set in highlighter", position)
	  			return 'set'
		  	}
  			return 'highlight'
  		}
  		return MyHighlightFormatter
	}-*/;
	public void createSelection(JavaScriptObject anno) {
	}
	public void startSelection(JavaScriptObject anno) {
	}
	public void selectAnnotation(JavaScriptObject anno) {
	}
	public void changeSelectionTarget(JavaScriptObject anno) {
	}
	public void annoCreated (JavaScriptObject annotation) {
		removeAllAnnotations();
		String res = annoCreatedJS(annotation, this);
		Util.doLogging("res: " + res);
        String[] parts = res.split("[:=,]");
        if (parts.length == 6 && parts[0].equals("xywh") && parts[1].equals("pixel")) {
        	wde.setX(Float.parseFloat(parts[2]));
        	wde.setY(Float.parseFloat(parts[3]));
        	wde.setW(Float.parseFloat(parts[4]));
        	wde.setH(Float.parseFloat(parts[5]));
        } else {
            System.out.println("Invalid input format");
        }
        removeAllAnnotations();
		JavaScriptObject depictionDic = createDic();
		for (PositionEntry pe: wde.getPositions()) {
			depictionDic = createDepictionDic(depictionDic, pe.getRegister(), pe.getNumber(), pe.getDepictionID());
		}
		annos = ConcatList(null, createMap(depictionDic, wde.getRegisters(),wde.getColumns(), wde.getX(), wde.getY(), wde.getH() , wde.getW(), wde.getType() == 0? false: true, wde.getWallSketch().getFilename(), wde.getDirection()));
		removeOrAddAnnotationsJS(viewers, annos, true);
		makeReadOnlyAllViewersJava(viewers);
		setEditor(constructsEditorRegisterJS(this));
	}
	public static native String annoCreatedJS(JavaScriptObject annotation, OSDLoaderWallDimension _self)
	/*-{
			return annotation.target.selector.value
	}-*/;
	public void annoChanged (JavaScriptObject annotation) {
		annoChangedJS(annotation, this);
	};	
	public static native void annoChangedJS(JavaScriptObject annotation, OSDLoaderWallDimension _self)
	/*-{
	}-*/;
	public void annoDeleted (JavaScriptObject annotation) {
		annoDeletedJS(annotation, this);
	};
	public static native void annoDeletedJS(JavaScriptObject annotation, OSDLoaderWallDimension _self)
	/*-{
	}-*/;
	public static native JavaScriptObject constructsEditorRegisterJS(OSDLoaderWallDimension _self)
	/*-{
	 var SetPositionWidget = function(args) {
	 	  $wnd.console.log("widget", args.annotation.body[0])
		  var annoLost = function(evt) {
		  	var position
		  	if (typeof args.annotation.body[0].value !== 'object'){
		  		var position = JSON.parse(args.annotation.body[0].value)
		  	} else {
		  		var position = args.annotation.body[0].value
		  	}
		  	 
	 		$wnd.console.log(position["Reg"], position["No"])
	 		position["Depiction"] = "-1"
	 		var newBody = args.annotation.body
	 		newBody[0].value = position;
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::setLost(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"]);		  
			}
		  var annoDeLost = function(evt) {
		  	var position
		  	if (typeof args.annotation.body[0].value !== 'object'){
		  		var position = JSON.parse(args.annotation.body[0].value)
		  	} else {
		  		var position = args.annotation.body[0].value
		  	}
	 		$wnd.console.log(position["Reg"], position["No"])
	 		position["Depiction"] = ""
	 		var newBody = args.annotation.body
	 		newBody[0].value = JSON.stringify(position);
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::unSetLost(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"]);		  
			}
		  var lostButton
		  var position
		  if (typeof args.annotation.body[0].value !== 'object'){
		  	var position = JSON.parse(args.annotation.body[0].value)
		  } else {
		  	var position = args.annotation.body[0].value
		  }
		  if (position["Depiction"] === "-1"){
			  lostButton = function(value) {
			    var button = document.createElement('button');
				button.innerHTML = 'remove Lost'
			    button.addEventListener('click', annoDeLost); 
			    return button;
			  }
		  } else {
			  lostButton = function(value) {
			    var button = document.createElement('button');
				button.innerHTML = 'Mark as Lost'
			    button.addEventListener('click', annoLost); 
			    return button;
			  }		  }
		  var container = document.createElement('div');
		  container.className = 'export-widget';
		  var labelcontainer = document.createElement('div');
		  container.className = 'export-widget';
		  var buttoncontainer = document.createElement('div');
		  container.className = 'export-widget';
		  var label = document.createElement('label');
		  label.innerHTML = args.annotation.underlying.id;    
		  labelcontainer.appendChild(label);
		  
		  var button1 = lostButton();
		
		  buttoncontainer.appendChild(button1);
		  container.appendChild(labelcontainer);
		  container.appendChild(buttoncontainer);
		  return container;
		}
		return [SetPositionWidget];
		
	}-*/;

	public static native JavaScriptObject constructsEditorDimensionJS(OSDLoaderWallDimension _self)
	/*-{
		return [];
		
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
		
	}
	public void setLost(String reg, String number) {
		Util.doLogging("Marking as lost: Reg: " + reg + ", No:" + number);
		if (wde.hasCorrespondingPositionEntry(Integer.parseInt(reg), Integer.parseInt(number))) {
			Info.display("Register cannot be marked lost", "Register is still connected to other entries");
		} else {
			PositionEntry pe = new PositionEntry(-1, -1, "lost", Integer.parseInt(reg), Integer.parseInt(number), true);
			wde.addPosition(pe);
		}
	}
	public void unSetLost(String reg, String number) {
		Util.doLogging("Remove Marking as lost: Reg: " + reg + ", No:" + number);
		for (PositionEntry pe: wde.getPositions()){
			if ((pe.getRegister() == Integer.parseInt(reg)) & (pe.getNumber() == Integer.parseInt(number))) {
				pe.delete();
			}
		}
	}

}
