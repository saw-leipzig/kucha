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
import de.cses.shared.DepictionEntry;
import de.cses.shared.EmptySpotEntry;
import de.cses.shared.ExportEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.CoordinateEntry;
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
	private Integer depictionID;
	private String depictionText;
	private String postfix;
	protected Boolean editorReadOnly = false;
	
	public OSDLoaderWallDimension(WallDimensionEntry wde, boolean annotation, OSDListener osdListener, String postfix) {
		super(annotation, null, osdListener, "rect", false, getHighlighter(0));
		this.osdDic = createDic();
		this.editor = constructsEditorRegisterJS(this, -1, "", editorReadOnly);
		this.annos = null;
		this.depictionID = -1;
		this.wde = wde;
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		ImageEntry image = new ImageEntry();
		if (wde.getWallSketch() == null) {
			Util.doLogging("ups, no wallsketch");
		}
		image.setFilename(wde.getWallSketch().getFilename());
		images.add(image);
		this.images=images;
		this.annotation =true;
		this.postfix = postfix;
	}
	public OSDLoaderWallDimension(WallDimensionEntry wde, DepictionEntry de, boolean annotation, OSDListener osdListener, String postfix) {
		super(annotation, null, osdListener, "rect", false, getHighlighter(de.getDepictionID()));
		this.osdDic = createDic();
		this.annos = null;
		this.wde = wde;
		this.depictionID = de == null ? -1: de.getDepictionID();
		this.depictionText = de == null ? "": de.getShortName();
		this.editor = constructsEditorRegisterJS(this, depictionID, depictionText, editorReadOnly);
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		ImageEntry image = new ImageEntry();
		if (wde.getWallSketch() == null) {
			Util.doLogging("ups, no wallsketch");
		} else {
			image.setFilename(wde.getWallSketch().getFilename());
			images.add(image);
			this.images=images;			
		}
		this.annotation =true;
		this.postfix = postfix;
	}
	public static native JavaScriptObject createMap(JavaScriptObject depictionDic, int rows, int cols, float x, float y, float height, float width, boolean isRect, String filename, int direction, OSDLoaderWallDimension _self) 
	/*-{
        function isOdd(num) { return num % 2;}
        var label = ""
      	var getLabel = function(rowNumber, colNumber, depictionIDs, row, col){
    		if (rowNumber !== -1 && colNumber !== -1){
          		label = "{\"Reg\": \"" + rowNumber + "\", \"No\": \"" + colNumber + "\", \"x\": \"" + col.toString() +"\", \"y\": \"" + row.toString() + "\" , \"Depiction\": " + JSON.stringify(depictionIDs)  +"}"
          	} else {
          		label =  "{\"Reg\": \"-1\", \"No\": \"-1\", \"x\": \"" + col.toString() +"\", \"y\": \"" + row.toString() + "\" ,\"Depiction\": []}"
          	}
//          	$wnd.console.log("label", label)
          	return label
        }
        function addRhombus(row,col, rows, cols, height, width, rowNumber, colNumber){
          var depictionIDs = getDepictionIDs(rowNumber, colNumber);
          var heightRhomb = 0; 
          heightRhomb = (rows>1 ? (height*2)/(rows-1) : (height*2)/(1))*(((row)/2));
          var heightrow = rows>1 ? (height*2)/(rows-1) : (height*2)/(1);
          var newPath = "";
          if (isOdd(row) === 0){
            newPath = "M"+ (x + ((width*((2*col)-1)) / ((2*cols) - 2))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"L" + (x + ((col === cols) ? width : ((((col * width) / ((cols) - 1)))))) + "," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow+(heightrow/2))))+"L"+ (x + (((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2))) +"," + (y + ((row === rows) ? height : (heightRhomb)))+"L" + (x + ((((col+1) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width))*2))) + "," + (y + ((heightrow/2)+heightRhomb-heightrow))+"L"+(x + (((col) / ((cols) - 1) * width)-((((1) / ((cols) - 1) * width))/2))) +"," + (y + (heightRhomb-heightrow))+"z"; 
          } else {
            newPath = "M"+(x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"L" + (x + ((col === cols) ? width : ((((width * (2*col-1)) / ((2 * cols) - 2)))))) + "," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow+(heightrow/2))))+"L"+(x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) + "," + (y + ((row === rows) ? height : (heightRhomb)))+"L" + (x + ((col === 1) ? 0 : ((((col) / ((cols) - 1) * width))-((((1) / ((cols) - 1) * width)))-((((1) / ((cols) - 1) * width)/2))))) + "," + (y + ((heightrow/2)+heightRhomb-heightrow))+"L"+ (x + ((col === 1) ? 0 : (((col-1) / ((cols) - 1) * width)))) +"," + (y + ((row === 1) ? 0 : (heightRhomb-heightrow)))+"z";
          }
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "x. "+row+", y. "+col,
            "type": "Annotation",
            "body": [
              {
                  "type": "TextualBody",
                  "value": getLabel(rowNumber, colNumber,depictionIDs, row, col),
                  "id": "x. "+row+", y. "+col,
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
          var depictionIDs = getDepictionIDs(rowNumber, colNumber);
          var heightRhomb = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var heightrow = (rows>1 ? (height)/(rows+1) : (height)/(1));
          var newPath = "";
          newPath = "xywh=pixel:"+(x + (((width/cols)*(col - 1)))) + ","+(y + (((height/rows)*(row - 1))))+","+(width/cols)+","+(height/rows);
          var annotation = {
            "@context": "http://www.w3.org/ns/anno.jsonld",
            "id": "x. "+row+", y. "+col,
            "type": "Annotation",
            "body": [
              {
                  "type": "TextualBody",
                  "value": getLabel(rowNumber, colNumber,depictionIDs, row, col),
                  "id": "x. "+row+", y. "+col,
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
        function getDepictionIDs(rowNumber, colNumber){
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
		  return depictionIDs
        }
        var annotations = [];
        var rowNumber = 0;
        for (var j = 1; j< rows+1; j++){
        	if (!_self.@de.cses.client.walls.OSDLoaderWallDimension::isEmptyRow(Ljava/lang/String;)(j.toString())){
        		rowNumber += 1;
        	}
        	var colNumber = direction === 0 ? 0: isRect? cols + 1: isOdd(rowNumber) === 0? cols + 1 : cols    
          for(var i = 1; i < (cols+1); i+=1){
          	var depictionIDs = getDepictionIDs(i,j)
          	var isEmpty = _self.@de.cses.client.walls.OSDLoaderWallDimension::isEmpty(Ljava/lang/String;Ljava/lang/String;)(i.toString(), j.toString())
        	if (direction === 0){
        		colNumber += 1
        	} else {
        		colNumber -= 1
        	}
        	numEmptySpots = _self.@de.cses.client.walls.OSDLoaderWallDimension::hasEmptySpotsForSubtraction(Ljava/lang/String;Ljava/lang/String;)(i.toString(), j.toString())
          	var annotationObject = {};
            if (isRect){
                annotationObject = addRect(j,i,rows,cols,height, width, isEmpty? -1: (rowNumber), isEmpty? -1: (colNumber - numEmptySpots))
            } else {
              if (!((i === cols) && (isOdd(j) === 0))){
                annotationObject = addRhombus(j,i,rows,cols,height, width, isEmpty? -1: rowNumber, isEmpty? -1: (colNumber - numEmptySpots))
              }
            }
//            if(isEmpty){
//            	$wnd.console.log("empty spot: ", annotationObject, i, j)
//            }
            if (Object.keys(annotationObject).length > 0){
            	annotations.push(annotationObject);
            }
          }
        }
//        $wnd.console.log("all annotations blubb: ", annotations)
        return annotations;
	}-*/;
	public void changePosition() {
		setEditor(constructsEditorDimensionJS(this));
		makeEditableAllViewers();
	}
	public void setosd() {
		 destroyAllViewers();
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
		if (results != null) {
			JavaScriptObject tiles = results.remove(0);
			JavaScriptObject imgDic=results.remove(0);
			JavaScriptObject ifn=results.remove(0);
			JavaScriptObject depictionDic = createDic();
			for (CoordinateEntry pe: wde.getCoordinates()) {
				if (!pe.isdeleted()) {
					depictionDic = createDepictionDic(depictionDic, pe.getRegister(), pe.getNumber(), pe.getDepictionID(), pe.getName(), pe.getExact());				
				}
			}
			annos = ConcatList(null, createMap(depictionDic, wde.getRegisters(),wde.getColumns(), wde.getX(), wde.getY(), wde.getH() , wde.getW(), wde.getType() == 0? false: true, "wallDimensionentry" + Integer.toString(wde.getWallDimensionID()) + postfix, wde.getDirection(), this));
			viewers = createZoomImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, editor, this, annos, readOnly, annotationType, disableEditor, highlighter, "");	
			removeOrAddAnnotationsJS(viewers, annos, true);
			makeReadOnlyAllViewersJava(viewers);
			setEditor(constructsEditorRegisterJS(this, depictionID, depictionText, editorReadOnly));			
		}
	}
	
	public static native JavaScriptObject createDepictionDic(JavaScriptObject depictionDic, int reg, int no, int depictionID, String depictionText, Boolean exact)
	/*-{
		if (!(reg in depictionDic)){
			depictionDic[reg] = {}
		}
		if (!(no in depictionDic[reg])){
			depictionDic[reg][no] = []			
		}
		depictionDic[reg][no].push({"depictionID": depictionID, "depictionText": depictionText, "depictionExact": exact});
//		$wnd.console.log("depictionDic", depictionDic)
		return depictionDic
	}-*/;	
	
	public ArrayList<JavaScriptObject> loadTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, String context, boolean annotation, boolean readOnly) {
		return processTiles(list, ifn,imgDic, wde.getWallSketch(),context, annotation, readOnly);
	}
	public ArrayList<JavaScriptObject> processTiles(JavaScriptObject list,JavaScriptObject ifn, JavaScriptObject imgDic, WallSketchEntry wse, String context, boolean annotation, boolean readOnly) {
		if (wse == null) {
			return null;
		}
		list = addZoomImage(list , context + wse.getFilename() + "/info.json","wallDimensionentry"+Integer.toString(wde.getWallDimensionID()) + postfix);
		ifn=addImageFileNames(ifn,"wallDimensionentry" + Integer.toString(wde.getWallDimensionID()) + postfix);
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
	private static native JavaScriptObject getHighlighter(int depictionID) 
	/*-{

  		MyHighlightFormatter = function(annotation) {
  			if (annotation.body.length === 0){
  				return 'unset'
  			}
		  	var position
		  	if (typeof annotation.body[0].value !== 'object'){
		  		var position = JSON.parse(annotation.body[0].value)
//		  		$wnd.console.log("annotation ", position)
		  	} else {
		  		var position = annotation.body[0].value
		  	}
		  	if (position.Reg === "-1" && position.No === "-1"){
  				return 'empty'
		  	} else if (position["Depiction"].length === 0) {
		  		return 'unset'
		  	} else if (position["Depiction"].filter(function checkForLost(e) {return e.depictionID === -1}).length > 0){
  				return 'lost'
  			} else if (position["Depiction"] instanceof Array){
  				var exact = false;
		  		for (var i = 0; i < position["Depiction"].length; i++){
//		  			$wnd.console.log("position", position);
					if (position["Depiction"][i]["depictionID"] === -1){
  						return 'lost'
					} else if (position["Depiction"][i] === "empty"){
//		  				$wnd.console.log("found empty ", position["Depiction"].filter(function checkForLost(e) {return e === "empty"}))
  						return 'empty'
					} else if (position["Depiction"][i]["depictionID"] === depictionID){
//						$wnd.console.log("Position", position, position["Depiction"][i]["depictionExact"])
						if (position["Depiction"][i]["depictionExact"] === true){
	  						return 'set'
						} else {
							return 'setUnsecure'
						}
					} else if (position["Depiction"][i]["depictionExact"] === true){
						exact = true
					}
		  		}
		  		if (exact){
		  			return 'occupied'
		  		} else {
		  			return 'occupiedUnsecure'
		  		}

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
        String[] parts = res.split("[:=,]");
        if (parts.length == 6 && parts[0].equals("xywh") && parts[1].equals("pixel")) {
        	wde.setX(Float.parseFloat(parts[2]));
        	wde.setY(Float.parseFloat(parts[3]));
        	wde.setW(Float.parseFloat(parts[4]));
        	wde.setH(Float.parseFloat(parts[5]));
        } else {
            Util.doLogging("Invalid input format");
        }
        removeAllAnnotations();
		JavaScriptObject depictionDic = createDic();
		for (CoordinateEntry pe: wde.getCoordinates()) {
			Util.doLogging("adding coordinate entry " + Boolean.toString(pe.getExact()));
			depictionDic = createDepictionDic(depictionDic, pe.getRegister(), pe.getNumber(), pe.getDepictionID(), pe.getName(), pe.getExact());
		}
		annos = ConcatList(null, createMap(depictionDic, wde.getRegisters(),wde.getColumns(), wde.getX(), wde.getY(), wde.getH() , wde.getW(), wde.getType() == 0? false: true, "wallDimensionentry" + Integer.toString(wde.getWallDimensionID()) + postfix, wde.getDirection(), this));
		removeOrAddAnnotationsJS(viewers, annos, true);
		makeReadOnlyAllViewersJava(viewers);
		setEditor(constructsEditorRegisterJS(this, depictionID, depictionText, editorReadOnly));
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
	public static native JavaScriptObject constructsEditorRegisterJS(OSDLoaderWallDimension _self, int depictionID, String depictionText, boolean editorReadOnly)
	/*-{
	  var getLabel = function(depictions){
	  	var label = ""
	  	if (depictions.filter(function checkForLost(e) {return e.depictionID === -1}).length > 0){
	  		
	  		return depictions.filter(function checkForLost(e) {return e.depictionID === -1})[0].depictionText
	  	} else if (depictions.filter(function checkForEmpty(e) {return e === "empty"}).length > 0){
	  		return ""
	  	} else {
			function myFunction(depiction) {
				var exact = ""
				if (depiction.depictionExact){
					exact = " - exact"
				} else {
					exact = " - vague"
				}
			  label += "Painted Representation " + depiction.depictionID + " (" + depiction.depictionText + ")" + exact + "; ";
			}
			depictions.forEach(myFunction);			
	  	}
	  	return label
	  }

	 var SetPositionWidget = function(args) {
//	 	 $wnd.console.log("Editor", args)
	 	  var getPosition = function(position){
  			  if (typeof position !== 'object'){
			  	return JSON.parse(position)
			  } else {
			  	return position
			  }
	 	  }
		  var annoLost = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
	 		position["Depiction"] = [{"depictionID": -1, "depictionText": $doc.getElementById("lostText").value, "depictionExact": true}]
	 		var newBody = args.annotation.body
	 		newBody[0].value = position;
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::setPositionLost(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"], $doc.getElementById("lostText").value);		  
			}
		  var annoDeLost = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
	 		position["Depiction"] = []
	 		var newBody = args.annotation.body
	 		newBody[0].value = JSON.stringify(position);
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::deletePosition(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"], "-1");		  
			}
		  var annoEmpty = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
		  	if (!_self.@de.cses.client.walls.OSDLoaderWallDimension::hasHinderingCoordinateEntryForEmptySpot(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"])){
		 		position["Depiction"] = [{"depictionID": 0, "depictionText": "empty", "depictionExact": true}]
		 		var newBody = args.annotation.body
		 		newBody[0].value = position;
		 		args.onUpdateBody(args.annotation.body, newBody, true);
				_self.@de.cses.client.walls.OSDLoaderWallDimension::setPositionEmpty(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(position["y"], position["x"], "empty");		  		  	
		  	}	
		  }
		  var annoDeEmpty = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
		  	
		  	
	 		position["Depiction"] = []
	 		var newBody = args.annotation.body
	 		newBody[0].value = JSON.stringify(position);
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::deleteEmptySpot(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(position.y, position.x, "0");		  
			}
		  var setPosition = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
		  	if (!position["Depiction"].includes(-1)){
		  		if (Array.isArray(position["Depiction"])){
		  			position["Depiction"].push({"depictionID": depictionID, "depictionText": depictionText, "depictionExact": true})
			 		var newBody = args.annotation.body
			 		newBody[0].value = position;
			 		args.onUpdateBody(args.annotation.body, newBody, true);
					_self.@de.cses.client.walls.OSDLoaderWallDimension::setPosition(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)(position["Reg"], position["No"], true);		  
		  		}
		  		
		  	} else {
		  		_self.@de.cses.client.walls.OSDLoaderWallDimension::errorSetPositionLost(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"]);
		  	}
			}
		  var setUnsecurePosition = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
		  	if (!position["Depiction"].includes(-1)){
		  		if (Array.isArray(position["Depiction"])){
		  			position["Depiction"].push({"depictionID": depictionID, "depictionText": depictionText, "depictionExact": false})
			 		var newBody = args.annotation.body
			 		newBody[0].value = position;
			 		args.onUpdateBody(args.annotation.body, newBody, true);
					_self.@de.cses.client.walls.OSDLoaderWallDimension::setPosition(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)(position["Reg"], position["No"], false);		  
		  		}
		  		
		  	} else {
		  		_self.@de.cses.client.walls.OSDLoaderWallDimension::errorSetPositionLost(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"]);
		  	}
			}
		  var deleteDepictionPosition = function(evt) {
		  	var position = getPosition(args.annotation.body[0].value)
	 		var newBody = args.annotation.body
	 		var depictionIDIndex = position["Depiction"].indexOf(depictionID);
			position["Depiction"].splice(depictionIDIndex, 1);
	 		newBody[0].value = position;
	 		args.onUpdateBody(args.annotation.body, newBody, true);
			_self.@de.cses.client.walls.OSDLoaderWallDimension::deleteDepictionPosition(Ljava/lang/String;Ljava/lang/String;)(position["Reg"], position["No"]);		  
			}
		  var lostButton = null
		  var emptyButton = null
		  var setPositionButton = null
		  var setUnsecurePositionButton = null
		  var position = getPosition(args.annotation.body[0].value)
		  if (position["Depiction"].length === 0 && position.Reg === "-1"){
			  emptyButton = function(value) {
			    var button = document.createElement('button');
				button.innerHTML = 'remove empty'
			    button.addEventListener('click', annoDeEmpty); 
			    return button;
			  }
		  } else {
//		  	$wnd.console.log("position", position)
			  if (position["Depiction"].filter(function checkIfLost(e) {return e.depictionID === -1}).length > 0){
				  lostButton = function(value) {
				    var button = document.createElement('button');
					button.innerHTML = 'remove Lost'
				    button.addEventListener('click', annoDeLost); 
				    return button;
				  }
			  } else {
			  	if (position["Depiction"].length === 0 && position.Reg !== "-1"){
				  emptyButton = function(value) {
				    var button = document.createElement('button');
					button.innerHTML = 'set empty'
				    button.addEventListener('click', annoEmpty); 
				    return button;
				  }	
				  lostButton = function(value) {
	  			    var lostForm = document.createElement("div");
			  		var lostText = document.createElement("input");
					lostText.type = "text";
				    lostText.id = "lostText"
				    var button = document.createElement('button');
				    button.style.marginRight = "10px"
					button.innerHTML = 'Mark as Lost with note:'
				    button.addEventListener('click', annoLost); 
				    lostForm.appendChild(button);
				    lostForm.appendChild(lostText);
				    return lostForm;
				  }			  		
			  	}
		  	  	if (position["Depiction"].filter(function checkIfSet(e) {return e.depictionID === depictionID}).length > 0) {
			  	  setPositionButton = function(value) {
				      var button = document.createElement('button');
					  button.innerHTML = 'reset connetction to current PR'
				      button.addEventListener('click', deleteDepictionPosition); 
				      return button;
				    }
		  	  	  } else {
//		  	  	  	$wnd.console.log("depiction not set", position)
				  	  setPositionButton = function(value) {
					    var button = document.createElement('button');
						button.innerHTML = 'connect to current PR'
					    button.addEventListener('click', setPosition); 
					    return button;
					  }		  	
				  	  setUnsecurePositionButton = function(value) {
					    var button = document.createElement('button');
						button.innerHTML = 'make probable connection'
					    button.addEventListener('click', setUnsecurePosition); 
					    return button;
					  }		  	
			  	  }
			  }
		  }
		  var container = document.createElement('div');
		  container.className = 'export-widget';
		  var labelcontainer = document.createElement('div');
		  labelcontainer.className = 'export-widget';
		  var buttonEmptyContainer = document.createElement('div');
		  buttonEmptyContainer.className = 'export-widget';
		  var buttoncontainer = document.createElement('div');
		  buttoncontainer.className = 'export-widget';
		  var labelReg = document.createElement('label');
//		  $wnd.console.log("position getLabel", position)
		  labelReg.innerHTML = position.Reg === "-1"? "Empty spot" : "Reg. " + position.Reg + ", No. " + position.No;    
		  labelcontainer.appendChild(labelReg);
		  var labelcontainerDepiction = document.createElement('div');
		  labelcontainerDepiction.className = 'export-widget';
		  var labelDepiction = document.createElement('label');
//		  $wnd.console.log(position)
		  labelDepiction.innerHTML = getLabel(position["Depiction"]);
		  labelcontainerDepiction.appendChild(labelDepiction);
		  container.appendChild(labelcontainer);
		  container.appendChild(labelcontainerDepiction);
		  if (!editorReadOnly){
			  if (lostButton){
				  var button1 = lostButton();
				  buttoncontainer.appendChild(button1);
			  	  container.appendChild(buttoncontainer);
			  }  
			  if (emptyButton){
				  var button2 = emptyButton();
				  buttonEmptyContainer.appendChild(button2);
			  	  container.appendChild(buttonEmptyContainer);
			  }  
			  if (depictionID !== -1){
				  if (setPositionButton){
				    var buttoncontainerSecureDepiction = document.createElement('div');
				    buttoncontainerSecureDepiction.className = 'export-widget';
				    var buttonSecureDepiction = setPositionButton();
				    buttoncontainerSecureDepiction.appendChild(buttonSecureDepiction);
				  	container.appendChild(buttoncontainerSecureDepiction);
				  }
				  if(setUnsecurePositionButton){
				    var buttoncontainerUnsecureDepiction = document.createElement('div');
				    buttoncontainerUnsecureDepiction.className = 'export-widget';
				    var buttonUnsecureDepiction = setUnsecurePositionButton();
				  	buttoncontainerUnsecureDepiction.appendChild(buttonUnsecureDepiction);
				  	container.appendChild(buttoncontainerUnsecureDepiction);
				  }	  	
			  }		  	
		  }


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
//		$wnd.console.log(ifn)
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
		   $wnd.alert("Failed to convert polygon.");
		   return "";
		}			
	}-*/;

	public void getResults(String id ,String tags, String polygon, String image, Boolean delete, Boolean update, Double creationTime, Double modificationTime, Boolean isProposed) {
		
	}
	public void setPositionLost(String reg, String number, String depictionText) {
		if (wde.hasCorrespondingCoordinateEntry(Integer.parseInt(reg), Integer.parseInt(number))) {
			Info.display("Register cannot be marked lost", "Register is still connected to other entries");
		} else {
			CoordinateEntry pe = new CoordinateEntry(-1, -1, depictionText, Integer.parseInt(reg), Integer.parseInt(number), true);
			wde.addPosition(pe);
		}
	}
	public boolean hasHinderingCoordinateEntryForEmptySpot(String reg, String number) {
		wde.hasHinderingCoordinateEntryForEmptySpot(Integer.parseInt(reg), Integer.parseInt(number));
		Util.doLogging(wde.log);
		if (wde.hasHinderingCoordinateEntryForEmptySpot(Integer.parseInt(reg), Integer.parseInt(number))) {
			Info.display("Register cannot be marked Empty", "Register is still connected to other entries, which would be out of range!");
			Util.doLogging("cannot set " + reg + " - " + number + " empty");
			return true;
		} else {
			return false;
		}
	}
	public void setPositionEmpty(String reg, String number, String depictionText) {
		GWT.debugger();
		int num = wde.getRegisters() - Integer.parseInt(number) + 1;
		int minusOneForEvenRowRombus = 0;
		if (wde.getType() == 0 && (Integer.parseInt(reg) % 2) == 0 && wde.getDirection() == 1 ) {
			minusOneForEvenRowRombus = 1;
		}
		int emptySpotNumber = wde.getDirection() == 1? wde.getRegisters() - Integer.parseInt(number): Integer.parseInt(number);
		emptySpotNumber = emptySpotNumber - minusOneForEvenRowRombus;
		Util.doLogging("Setting position empty new " + reg + " - " + number + " - " + Integer.toString(emptySpotNumber) + " - " + Integer.toString(minusOneForEvenRowRombus));
		EmptySpotEntry ese = new EmptySpotEntry(-1, Integer.parseInt(reg), Integer.parseInt(number) , depictionText, false);
		wde.addEmptySpot(ese);
		setosd();
	}
	public String hasEmptySpotsForSubtraction(String x, String y) {
		Integer numEmptySpots = 0;
		for (EmptySpotEntry ese: wde.getEmptySpots()) {
			if (ese.getY() == Integer.parseInt(y) && !ese.isdeleted()) {
				if (wde.getDirection() == 1) {
					if (ese.getX() > Integer.parseInt(x)) {
						numEmptySpots += 1;
					}					
				} else {
					if (ese.getX() < Integer.parseInt(x)) {
						numEmptySpots += 1;
					}					
					
				}

			}
		}
		return numEmptySpots.toString();
	}
	public void errorSetPositionLost(String reg, String number) {
		Info.display("Position not set!", "Unmark lost at position first!");
	}
	public void setPosition(String reg, String number, Boolean exact) {
		Util.doLogging("set position called" + Boolean.toString(exact));
		if (wde.hasCorrespondingCoordinateLostEntry(Integer.parseInt(reg), Integer.parseInt(number))) {
			Info.display("Possition marked lost", "Unmark lost first!");
		} else {
			CoordinateEntry pe = new CoordinateEntry(-1, depictionID, depictionText, Integer.parseInt(reg), Integer.parseInt(number), exact);
			wde.addPosition(pe);
		}
	}
	public void deleteDepictionPosition(String reg, String number) {
		for (CoordinateEntry pe: wde.getCoordinates()){
			if ((pe.getRegister() == Integer.parseInt(reg)) & (pe.getNumber() == Integer.parseInt(number))) {
				pe.delete();
			}
		}
	}
	public void deletePosition(String reg, String number, String depictionID) {
		for (CoordinateEntry pe: wde.getCoordinates()){
			if ((pe.getRegister() == Integer.parseInt(reg)) & (pe.getNumber() == Integer.parseInt(number))) {
				pe.delete();
			}
		}
	}
	public void deleteEmptySpot(String y, String x, String depictionID) {
		Util.doLogging("deleteEmptySpot initiated " + y + " - " + x);
		for (EmptySpotEntry ese: wde.getEmptySpots()){
			if ((ese.getY() == Integer.parseInt(y)) & (ese.getX() == Integer.parseInt(x))) {
				ese.delete();
			}
		}
		setosd();
	}
	public boolean isEmpty(String x, String y) {
		for (EmptySpotEntry ese: wde.getEmptySpots()){
			if ((ese.getY() == Integer.parseInt(y)) && (ese.getX() == Integer.parseInt(x)) && (!ese.isdeleted())) {
				return true;
			}
		}
		return false;
	}
	public boolean isEmptyRow(String y) {
		int numberEmptySpot = 0;
		for (EmptySpotEntry ese: wde.getEmptySpots()){
			if ((ese.getY() == Integer.parseInt(y)) && (!ese.isdeleted())) {
				numberEmptySpot += 1;
			}
		}
		if (wde.getType() == 0 && (Integer.parseInt(y) % 2) == 0) {
			numberEmptySpot += 1;
		}
		if (numberEmptySpot == wde.getColumns()) {
			return true;
		}
		return false;
	}
	
}
