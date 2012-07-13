Ext.namespace('mapfish.widgets');mapfish.widgets.RadioTreeNodeUI=Ext.extend(Ext.tree.TreeNodeUI,{renderElements:function(n,a,targetNode,bulkRender){this.indentMarkup=n.parentNode?n.parentNode.ui.getChildIndent():'';var cb=typeof a.checked=='boolean';var radioGrp=n.attributes.radioGrp||"radioGrp";var href=a.href?a.href:Ext.isGecko?"":"#";var buf=['<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ',a.cls,'" unselectable="on">','<span class="x-tree-node-indent">',this.indentMarkup,"</span>",'<img src="',this.emptyIcon,'" class="x-tree-ec-icon x-tree-elbow" />','<img src="',a.icon||this.emptyIcon,'" class="x-tree-node-icon',(a.icon?" x-tree-node-inline-icon":""),(a.iconCls?" "+a.iconCls:""),'" unselectable="on" />',cb?('<input class="x-tree-node-cb" type="radio" id="'+n.id+'" name="'+radioGrp+'" '+(a.checked?'checked="checked" />':'/>')):'','<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',a.hrefTarget?' target="'+a.hrefTarget+'"':"",'><span unselectable="on">',n.text,"</span></a></div>",'<ul class="x-tree-node-ct" style="display:none;"></ul>',"</li>"].join('');var nel;if(bulkRender!==true&&n.nextSibling&&(nel=n.nextSibling.ui.getEl())){this.wrap=Ext.DomHelper.insertHtml("beforeBegin",nel,buf);}else{this.wrap=Ext.DomHelper.insertHtml("beforeEnd",targetNode,buf);}
this.elNode=this.wrap.childNodes[0];this.ctNode=this.wrap.childNodes[1];var cs=this.elNode.childNodes;this.indentNode=cs[0];this.ecNode=cs[1];this.iconNode=cs[2];var index=3;if(cb){this.checkbox=cs[3];index++;}
this.anchor=cs[index];this.textNode=cs[index].firstChild;},_unused_renderElements:function(n,a,targetNode,bulkRender){mapfish.widgets.RadioTreeNodeUI.superclass.renderElements.apply(this,arguments);var cbNode=Ext.DomQuery.selectNode(".x-tree-node-cb",this.elNode);var radioGrp=n.attributes.radioGrp||"radioGrp";cbNode.setAttribute("type","radio");cbNode.setAttribute("id",n.id);cbNode.setAttribute("name",radioGrp);},onRadioChange:function(){var checked=this.checkbox.checked;this.node.attributes.checked=checked;this.fireEvent('radiochange',this.node,checked);}});mapfish.widgets.LayerTreeEventModel=Ext.extend(Ext.tree.TreeEventModel,{delegateClick:function(e,t){if(!this.beforeEvent(e)){return;}
if(e.getTarget('input[type=checkbox]',1)){this.onCheckboxClick(e,this.getNode(e));}
else if(e.getTarget('input[type=radio]',1)){this.onRadioClick(e,this.getNode(e));}
else if(e.getTarget('.x-tree-ec-icon',1)){this.onIconClick(e,this.getNode(e));}
else if(this.getNodeTarget(e)){this.onNodeClick(e,this.getNode(e));}},onRadioClick:function(e,node){if(!node.ui.onRadioChange){OpenLayers.Console.error("Invalid TreeNodeUI Class, no "+"onRadioChange is available");return;}
node.ui.onRadioChange(e);}});mapfish.widgets.LayerTree=function(config){Ext.apply(this,config);mapfish.widgets.LayerTree.superclass.constructor.call(this);};Ext.extend(mapfish.widgets.LayerTree,Ext.tree.TreePanel,{separator:":",model:null,showWmsLegend:false,rootVisible:false,animate:true,autoScroll:true,loader:new Ext.tree.TreeLoader({}),enableDD:false,containerScroll:true,ascending:true,_automaticModel:true,layerNameToLayer:{},baseLayerNames:[],layersWithSublayers:{},layerToNodeIds:{},nodeIdToNode:{},nodeIdToLayers:{},hasCheckbox:function(node){return typeof(node.attributes.checked)=="boolean";},setNodeChecked:function(nodeOrId,checked,fireEvent){var node=(nodeOrId instanceof Ext.data.Node)?nodeOrId:this.getNodeById(nodeOrId);if(!node||!this.hasCheckbox(node)){return;}
if(checked===undefined){checked=!node.attributes.checked;}
node.attributes.checked=checked;if(node.ui&&node.ui.checkbox){node.ui.checkbox.checked=checked;}
if(fireEvent||(fireEvent===undefined)){node.fireEvent('checkchange',node,checked);}},_updateCachedObjects:function(){if(!this.map){OpenLayers.Console.error("map Object needs to be available when "+"calling _updateCachedObjects");return;}
this.layerNameToLayer={};this.baseLayerNames=[];this.layersWithSublayers={};this.layerToNodeIds={};this.nodeIdToNode={};this.nodeIdToLayers={};Ext.each(this.map.layers,function(layer){var name=layer.name;this.layerNameToLayer[name]=layer;if(layer.isBaseLayer)
this.baseLayerNames.push(name);},this);this.getRootNode().cascade(function(node){if(!node.attributes.layerNames)
return true;var layerNames=node.attributes.layerNames;for(var i=0;i<layerNames.length;i++){var name=layerNames[i];if(name.indexOf(this.separator)!=-1){var name=name.split(this.separator)[0];this.layersWithSublayers[name]=true;}
if(!this.nodeIdToLayers[node.id])
this.nodeIdToLayers[node.id]=[];this.nodeIdToLayers[node.id].push(this.layerNameToLayer[name]);}},this);this.getRootNode().cascade(function(node){var checked=node.attributes.checked;var layerNames=node.attributes.layerNames;if(!layerNames)
return;for(var i=0;i<layerNames.length;i++){var layerName=layerNames[i];if(!layerName)
continue;if(!this.layerToNodeIds[layerName])
this.layerToNodeIds[layerName]=[];this.layerToNodeIds[layerName].push(node.id);this.nodeIdToNode[node.id]=node;}},this);},_updateCheckboxAncestors:function(){var unvisitedNodeIds={};var tree=this;function updateNodeCheckbox(node){if(!tree.hasCheckbox(node)){throw new Error(arguments.callee.name+" should only be called on checkbox nodes");}
var checkboxChildren=[];node.eachChild(function(child){if(tree.hasCheckbox(child))
checkboxChildren.push(child);},this);if(checkboxChildren.length==0){return node.attributes.checked;}
var allChecked=true;Ext.each(checkboxChildren,function(child){if(!updateNodeCheckbox(child)){allChecked=false;return false;}},this);tree.setNodeChecked(node,allChecked,false);delete unvisitedNodeIds[node.id];return allChecked;}
var checkboxNodes=[];this.getRootNode().cascade(function(node){if(this.hasCheckbox(node)){checkboxNodes.push(node);unvisitedNodeIds[node.id]=true;}},this);var node;while(node=checkboxNodes.shift()){if(unvisitedNodeIds[node.id])
updateNodeCheckbox(node);}},_handleModelChange:function LT__handleModelChange(clickedNode,checked){if(clickedNode){clickedNode.cascade(function(node){this.setNodeChecked(node,checked,false);},this);}
this._updateCheckboxAncestors();if(!this.map){return;}
this._updateCachedObjects();function getVisibilityFromMap(){var layerVisibility={};Ext.each(this.map.layers,function(layer){var name=layer.name;layerVisibility[name]=layer.visibility;if(!(layer instanceof OpenLayers.Layer.WMS)&&!(layer instanceof OpenLayers.Layer.WMS.Untiled)&&!(layer instanceof OpenLayers.Layer.MapServer))
{return;}
if(!this.layersWithSublayers[layer.name])
return;if(layer.isBaseLayer){OpenLayers.Console.error("Using sublayers on a base layer " + "is not supported (base layer is "+
name+")");}
if(!layer._origLayers){layer._origLayers=layer.params.LAYERS||layer.params.layers;}
var sublayers=layer._origLayers;if(sublayers instanceof Array){for(var j=0;j<sublayers.length;j++){var sublayer=sublayers[j];layerVisibility[name+this.separator+sublayer]=layer.visibility;}}},this);return layerVisibility;}
function updateVisibilityFromTree(layerVisibility){var forcedVisibility={};this.getRootNode().cascade(function(node){var checked=node.attributes.checked;var layerNames=node.attributes.layerNames;var radioGrp=null;if(!layerNames)
return;for(var i=0;i<layerNames.length;i++){var layerName=layerNames[i];if(!layerName)
continue;if(layerVisibility[layerName]==undefined)
OpenLayers.Console.error("Invalid layer: ",layerName);if(node.attributes.radio){radioGrp=node.attributes.radioGrp||"radioGrp";if(!radioButton[radioGrp])
radioButton[radioGrp]={};radioButton[radioGrp][layerName]=checked;}
if(forcedVisibility[layerName])
continue;if(node==clickedNode){if(this.baseLayerNames.indexOf(layerName)!=-1){clickedBaseLayer=layerName;}
if(radioGrp){clickedRadioButton[0]=radioGrp;clickedRadioButton[1]=layerName;}
forcedVisibility[layerName]=true;}
layerVisibility[layerName]=checked;}},this);return layerVisibility;}
function applyBaseLayerRestriction(layerVisibility,clickedBaseLayer,currentBaseLayerName){var numBaseLayer=0;for(var i=0;i<this.baseLayerNames.length;i++){if(layerVisibility[this.baseLayerNames[i]])
numBaseLayer++;}
if(numBaseLayer==1)
return layerVisibility;for(var i=0;i<this.baseLayerNames.length;i++){layerVisibility[this.baseLayerNames[i]]=false;}
if(clickedBaseLayer){layerVisibility[clickedBaseLayer]=true;return layerVisibility;}
if(!currentBaseLayerName)
return layerVisibility;layerVisibility[currentBaseLayerName]=true;return layerVisibility;}
function applyRadioButtonRestriction(layerVisibility,clickedRadioButton,radioButton){for(var radioGrp in radioButton){for(var layerName in radioButton[radioGrp]){if(clickedRadioButton[0]==radioGrp){layerVisibility[layerName]=layerName==clickedRadioButton[1];}else{layerVisibility[layerName]=radioButton[radioGrp][layerName];}}}
return layerVisibility;}
function updateTreeFromVisibility(layerVisibility){for(var layerName in layerVisibility){var nodeIds=this.layerToNodeIds[layerName];if(!nodeIds)
continue;for(var i=0;i<nodeIds.length;i++){var node=this.nodeIdToNode[nodeIds[i]];if(!node)
continue;var layerNames=node.attributes.layerNames;if(!layerNames){OpenLayers.Console.error("unexpected state");continue;}
var allChecked=true;for(var j=0;j<layerNames.length;j++){var layerName=layerNames[j];if(!layerName)
continue;if(!layerVisibility[layerName]){allChecked=false;break;}}
this.setNodeChecked(node,allChecked,false);}}}
function updateMapFromVisibility(layerVisibility){var wmsLayers={};for(var layerName in layerVisibility){var visible=layerVisibility[layerName];var splitName=layerName.split(this.separator);if(splitName.length!=2)
continue;delete layerVisibility[layerName];layerName=splitName[0];var sublayerName=splitName[1];if(!wmsLayers[layerName]){wmsLayers[layerName]=[];}
if(visible){wmsLayers[layerName].push(sublayerName);}}
for(layerName in wmsLayers){if(layerVisibility[layerName]!==undefined)
delete layerVisibility[layerName];}
for(var layerName in layerVisibility){var layer=this.layerNameToLayer[layerName];if(!layer){OpenLayers.Console.error("Non existing layer name",layerName);continue;}
if(this.baseLayerNames.indexOf(layerName)!=-1){if(layerVisibility[layerName]){this.map.setBaseLayer(layer);}}else{layer.setVisibility(layerVisibility[layerName]);}}
for(var layerName in wmsLayers){var layer=this.layerNameToLayer[layerName];var sublayers=wmsLayers[layerName];if(layer.isBaseLayer){OpenLayers.Console.error("base layer for sublayer "+"are not supported");return;}
if(sublayers.length==0){layer.setVisibility(false,true);}else{if(!this.enableDD){if(!layer._origLayers){OpenLayers.Console.error("Assertion failure");}
var origLayers=layer._origLayers;var orderedLayers=[];for(var i=0;i<origLayers.length;i++){var l=origLayers[i];if(sublayers.indexOf(l)!=-1)
orderedLayers.push(l);}
sublayers=orderedLayers;}
var layerParamName=layer.params.LAYERS?"LAYERS":"layers";if(!mapfish.Util.arrayEqual(layer.params[layerParamName],sublayers)){layer.params[layerParamName]=sublayers;layer.redraw();}
layer.setVisibility(true,true);}}}
var currentBaseLayerName;if(this.map.baseLayer)
currentBaseLayerName=this.map.baseLayer.name;var clickedBaseLayer;var radioButton={};var clickedRadioButton=[];var layerVisibility=getVisibilityFromMap.call(this);layerVisibility=updateVisibilityFromTree.call(this,layerVisibility);applyBaseLayerRestriction.call(this,layerVisibility,clickedBaseLayer,currentBaseLayerName);applyRadioButtonRestriction.call(this,layerVisibility,clickedRadioButton,radioButton);updateTreeFromVisibility.call(this,layerVisibility);updateMapFromVisibility.call(this,layerVisibility);},_extractOLModel:function LT__extractOLModel(){var getLegendParams={service:"WMS",version:"1.1.1",request:"GetLegendGraphic",exceptions:"application/vnd.ogc.se_inimage",format:"image/png"};var layers=[];var layersArray=this.map.layers.slice();if(!this.ascending){layersArray.reverse();}
for(var i=0;i<layersArray.length;i++){var l=layersArray[i];var wmsChildren=[];if(l instanceof OpenLayers.Layer.WMS||l instanceof OpenLayers.Layer.WMS.Untiled||l instanceof OpenLayers.Layer.MapServer){var sublayers=l.params.LAYERS||l.params.layers;if(sublayers instanceof Array){for(var j=0;j<sublayers.length;j++){var w=sublayers[j];var iconUrl;if(this.showWmsLegend){var params=OpenLayers.Util.extend({LAYER:w},getLegendParams);var paramsString=OpenLayers.Util.getParameterString(params);iconUrl=l.url+paramsString;}
var wmsChild={text:w,checked:l.getVisibility(),icon:iconUrl,layerName:l.name+this.separator+w,children:[],cls:"cf-wms-node"};if(this.ascending){wmsChildren.push(wmsChild);}else{wmsChildren.unshift(wmsChild);}}}}
var className='';if(!l.displayInLayerSwitcher){className='x-hidden';}
layers.push({text:l.name,checked:l.getVisibility(),cls:className,layerName:(wmsChildren.length>0?null:l.name),children:wmsChildren});}
return layers;},_updateOrder:function(){this._updateCachedObjects();function layerIndex(layers,name){for(var i=0;i<layers.length;i++){var l=layers[i];if(l.name==name)
return i;}
return-1;}
var orderedLayers=this.map.layers.slice();var seenLayers={};var nodes=[];this.getRootNode().cascade(function(node){if(this.ascending)
nodes.push(node);else
nodes.unshift(node);},this);Ext.each(nodes,function(node){var layers=this.nodeIdToLayers[node.id];if(!layers)
return;Ext.each(layers,function(layer){var layerName=layer.name;if(seenLayers[layerName])
return;seenLayers[layerName]=true;var index=layerIndex(orderedLayers,layerName);if(index==-1||!this.layerNameToLayer[layerName]){throw new Error("Layer "+layerName+" not available");}
orderedLayers.splice(index,1);orderedLayers.push(this.layerNameToLayer[layerName]);},this);},this);this._updateCheckboxAncestors();this.map.layers=orderedLayers;for(var i=0;i<this.map.layers.length;i++){this.map.setLayerZIndex(this.map.layers[i],i);}},_fixupModel:function(){this.getRootNode().cascade(function(node){var attrs=node.attributes;if(!attrs.layerNames&&attrs.layerName){attrs.layerNames=[attrs.layerName];delete attrs.layerName;}},this);if(this.map)
this._updateCachedObjects();this.getRootNode().cascade(function(node){var layers;if(!node.attributes.radio&&(!this.map||!(layers=this.nodeIdToLayers[node.id])))
return;var isBaseLayer=false;if(layers){isBaseLayer=true;Ext.each(layers,function(layer){if(!layer.isBaseLayer){isBaseLayer=false;return false;}},this);}
if(isBaseLayer||node.attributes.radio){node.attributes.uiProvider=mapfish.widgets.RadioTreeNodeUI;if(node.ui)
node.ui=new mapfish.widgets.RadioTreeNodeUI(node);}},this);},initComponent:function(){this.eventModel=new mapfish.widgets.LayerTreeEventModel(this);mapfish.widgets.LayerTree.superclass.initComponent.call(this);this.addListener("checkchange",function checkChange(node,checked){this._handleModelChange(node,checked);},this);this.addListener("radiochange",function radioChange(node,checked){this._handleModelChange(node,checked);},this);this._automaticModel=!this.model;if(!this.model){this.model=this._extractOLModel();}
var root={text:'Root',draggable:false,id:'source',children:this.model,leaf:false};function buildTree(attributes){var node=new Ext.tree.TreeNode(attributes);var cs=attributes.children;node.leaf=!cs;if(!cs)
return node;for(var i=0;i<cs.length;i++){if(!cs[i]){continue;}
node.appendChild(buildTree(cs[i]));}
return node;}
var rootNode=buildTree(root);this.setRootNode(rootNode);this._fixupModel();this.addListener("dragdrop",function(){this._updateOrder(arguments);},this);if(!this._automaticModel){this._handleModelChange(null,null);if(this.enableDD)
this._updateOrder();}},onRender:function(container,position){if(!this.el){this.el=document.createElement('div');}
mapfish.widgets.LayerTree.superclass.onRender.apply(this,arguments);}});Ext.reg('layertree',mapfish.widgets.LayerTree);