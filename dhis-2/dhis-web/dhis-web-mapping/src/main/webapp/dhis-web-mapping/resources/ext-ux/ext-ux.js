/* ColorField */
Ext.ux.ColorField=Ext.extend(Ext.form.TriggerField,{invalidText:"'{0}' is not a valid color - it must be in a the hex format (# followed by 3 or 6 letters/numbers 0-9 A-F)",triggerClass:'x-form-color-trigger',defaultAutoCreate:{tag:"input",type:"text",size:"10",maxlength:"7",autocomplete:"off"},maskRe:/[#a-f0-9]/i,validateValue:function(value){if(!Ext.ux.ColorField.superclass.validateValue.call(this,value)){return false;}
if(value.length<1){this.setColor('');return true;}
var parseOK=this.parseColor(value);if(!value||(parseOK==false)){this.markInvalid(String.format(this.invalidText,value));return false;}
this.setColor(value);return true;},setColor:function(color){if(color==''||color==undefined)
{if(this.emptyText!=''&&this.parseColor(this.emptyText))
color=this.emptyText;else
color='transparent';}
if(this.trigger)
this.trigger.setStyle({'background-color':color});else
{this.on('render',function(){this.setColor(color)},this);}},validateBlur:function(){return!this.menu||!this.menu.isVisible();},getValue:function(){return Ext.ux.ColorField.superclass.getValue.call(this)||"";},setValue:function(color){Ext.ux.ColorField.superclass.setValue.call(this,this.formatColor(color));this.setColor(this.formatColor(color));},parseColor:function(value){return(!value||(value.substring(0,1)!='#'))?false:(value.length==4||value.length==7);},formatColor:function(value){if(!value||this.parseColor(value))
return value;if(value.length==3||value.length==6){return'#'+value;}
return'';},menuListeners:{select:function(e,c){this.setValue(c);this.fireEvent('select',this,c);},show:function(){this.onFocus();},hide:function(){this.focus.defer(10,this);var ml=this.menuListeners;this.menu.un("select",ml.select,this);this.menu.un("show",ml.show,this);this.menu.un("hide",ml.hide,this);}},onTriggerClick:function(){if(this.disabled){return;}
if(this.menu==null){this.menu=new Ext.menu.ColorMenu();}
this.menu.on(Ext.apply({},this.menuListeners,{scope:this}));this.menu.show(this.el,"tl-bl?");}});Ext.reg('colorfield',Ext.ux.ColorField);

/* IconCombo */
Ext.namespace('Ext.ux.plugins');Ext.ux.plugins.IconCombo=function(config){Ext.apply(this,config);};Ext.extend(Ext.ux.plugins.IconCombo,Ext.util.Observable,{init:function(combo){Ext.apply(combo,{tpl:'<tpl for=".">'+'<div class="x-combo-list-item ux-icon-combo-item '+'{'+combo.iconClsField+'}">'+'{'+combo.displayField+'}'+'</div></tpl>',onRender:combo.onRender.createSequence(function(ct,position){this.wrap.applyStyles({position:'relative'});this.el.addClass('ux-icon-combo-input');this.icon=Ext.DomHelper.append(this.el.up('div.x-form-field-wrap'),{tag:'div',style:'position:absolute'});}),setIconCls:function(){var rec=this.store.query(this.valueField,this.getValue()).itemAt(0);if(rec){this.icon.className='ux-icon-combo-icon '+rec.get(this.iconClsField);}},setValue:combo.setValue.createSequence(function(value){this.setIconCls();})});}});

/* DD View */
Ext.ux.DDView=function(config){if(!config.itemSelector){var tpl=config.tpl;if(this.classRe.test(tpl)){config.tpl=tpl.replace(this.classRe,'class=$1x-combo-list-item $2$1');}
else{config.tpl=tpl.replace(this.tagRe,'$1 class="x-combo-list-item" $2');}
config.itemSelector=".x-combo-list-item";}
Ext.ux.DDView.superclass.constructor.call(this,Ext.apply(config,{border:false}));};Ext.extend(Ext.ux.DDView,Ext.DataView,{sortDir:'ASC',isFormField:true,classRe:/class=(['"])(.*)\1/,tagRe:/(<\w*)(.*?>)/,reset:Ext.emptyFn,clearInvalid:Ext.form.Field.prototype.clearInvalid,afterRender:function(){Ext.ux.DDView.superclass.afterRender.call(this);if(this.dragGroup){this.setDraggable(this.dragGroup.split(","));}
if(this.dropGroup){this.setDroppable(this.dropGroup.split(","));}
if(this.deletable){this.setDeletable();}
this.isDirtyFlag=false;this.addEvents("drop");},validate:function(){return true;},destroy:function(){this.purgeListeners();this.getEl().removeAllListeners();this.getEl().remove();if(this.dragZone){if(this.dragZone.destroy){this.dragZone.destroy();}}
if(this.dropZone){if(this.dropZone.destroy){this.dropZone.destroy();}}},getName:function(){return this.name;},setValue:function(v){if(!this.store){throw"DDView.setValue(). DDView must be constructed with a valid Store";}
var data={};data[this.store.reader.meta.root]=v?[].concat(v):[];this.store.proxy=new Ext.data.MemoryProxy(data);this.store.load();},getValue:function(){var result='(';this.store.each(function(rec){result+=rec.id+',';});return result.substr(0,result.length-1)+')';},getIds:function(){var i=0,result=new Array(this.store.getCount());this.store.each(function(rec){result[i++]=rec.id;});return result;},isDirty:function(){return this.isDirtyFlag;},getTargetFromEvent:function(e){var target=e.getTarget();while((target!==null)&&(target.parentNode!=this.el.dom)){target=target.parentNode;}
if(!target){target=this.el.dom.lastChild||this.el.dom;}
return target;},getDragData:function(e){var target=this.findItemFromChild(e.getTarget());if(target){if(!this.isSelected(target)){delete this.ignoreNextClick;this.onItemClick(target,this.indexOf(target),e);this.ignoreNextClick=true;}
var dragData={sourceView:this,viewNodes:[],records:[],copy:this.copy||(this.allowCopy&&e.ctrlKey)};if(this.getSelectionCount()==1){var i=this.getSelectedIndexes()[0];var n=this.getNode(i);dragData.viewNodes.push(dragData.ddel=n);dragData.records.push(this.store.getAt(i));dragData.repairXY=Ext.fly(n).getXY();}else{dragData.ddel=document.createElement('div');dragData.ddel.className='multi-proxy';this.collectSelection(dragData);}
return dragData;}
return false;},getRepairXY:function(e){return this.dragData.repairXY;},collectSelection:function(data){data.repairXY=Ext.fly(this.getSelectedNodes()[0]).getXY();if(this.preserveSelectionOrder===true){Ext.each(this.getSelectedIndexes(),function(i){var n=this.getNode(i);var dragNode=n.cloneNode(true);dragNode.id=Ext.id();data.ddel.appendChild(dragNode);data.records.push(this.store.getAt(i));data.viewNodes.push(n);},this);}else{var i=0;this.store.each(function(rec){if(this.isSelected(i)){var n=this.getNode(i);var dragNode=n.cloneNode(true);dragNode.id=Ext.id();data.ddel.appendChild(dragNode);data.records.push(this.store.getAt(i));data.viewNodes.push(n);}
i++;},this);}},setDraggable:function(ddGroup){if(ddGroup instanceof Array){Ext.each(ddGroup,this.setDraggable,this);return;}
if(this.dragZone){this.dragZone.addToGroup(ddGroup);}else{this.dragZone=new Ext.dd.DragZone(this.getEl(),{containerScroll:true,ddGroup:ddGroup});if(!this.multiSelect){this.singleSelect=true;}
this.dragZone.getDragData=this.getDragData.createDelegate(this);this.dragZone.getRepairXY=this.getRepairXY;this.dragZone.onEndDrag=this.onEndDrag;}},setDroppable:function(ddGroup){if(ddGroup instanceof Array){Ext.each(ddGroup,this.setDroppable,this);return;}
if(this.dropZone){this.dropZone.addToGroup(ddGroup);}else{this.dropZone=new Ext.dd.DropZone(this.getEl(),{owningView:this,containerScroll:true,ddGroup:ddGroup});this.dropZone.getTargetFromEvent=this.getTargetFromEvent.createDelegate(this);this.dropZone.onNodeEnter=this.onNodeEnter.createDelegate(this);this.dropZone.onNodeOver=this.onNodeOver.createDelegate(this);this.dropZone.onNodeOut=this.onNodeOut.createDelegate(this);this.dropZone.onNodeDrop=this.onNodeDrop.createDelegate(this);}},getDropPoint:function(e,n,dd){if(n==this.el.dom){return"above";}
var t=Ext.lib.Dom.getY(n),b=t+n.offsetHeight;var c=t+(b-t)/2;var y=Ext.lib.Event.getPageY(e);if(y<=c){return"above";}else{return"below";}},isValidDropPoint:function(pt,n,data){if(!data.viewNodes||(data.viewNodes.length!=1)){return true;}
var d=data.viewNodes[0];if(d==n){return false;}
if((pt=="below")&&(n.nextSibling==d)){return false;}
if((pt=="above")&&(n.previousSibling==d)){return false;}
return true;},onNodeEnter:function(n,dd,e,data){if(this.highlightColor&&(data.sourceView!=this)){this.el.highlight(this.highlightColor);}
return false;},onNodeOver:function(n,dd,e,data){var dragElClass=this.dropNotAllowed;var pt=this.getDropPoint(e,n,dd);if(this.isValidDropPoint(pt,n,data)){if(this.appendOnly||this.sortField){return"x-tree-drop-ok-below";}
if(pt){var targetElClass;if(pt=="above"){dragElClass=n.previousSibling?"x-tree-drop-ok-between":"x-tree-drop-ok-above";targetElClass="x-view-drag-insert-above";}else{dragElClass=n.nextSibling?"x-tree-drop-ok-between":"x-tree-drop-ok-below";targetElClass="x-view-drag-insert-below";}
if(this.lastInsertClass!=targetElClass){Ext.fly(n).replaceClass(this.lastInsertClass,targetElClass);this.lastInsertClass=targetElClass;}}}
return dragElClass;},onNodeOut:function(n,dd,e,data){this.removeDropIndicators(n);},onNodeDrop:function(n,dd,e,data){if(this.fireEvent("drop",this,n,dd,e,data)===false){return false;}
var pt=this.getDropPoint(e,n,dd);var insertAt=(this.appendOnly||(n==this.el.dom))?this.store.getCount():n.viewIndex;if(pt=="below"){insertAt++;}
if(data.sourceView==this){if(pt=="below"){if(data.viewNodes[0]==n){data.viewNodes.shift();}}else{if(data.viewNodes[data.viewNodes.length-1]==n){data.viewNodes.pop();}}
if(!data.viewNodes.length){return false;}
if(insertAt>this.store.indexOf(data.records[0])){insertAt--;}}
if(data.node instanceof Ext.tree.TreeNode){var r=data.node.getOwnerTree().recordFromNode(data.node);if(r){data.records=[r];}}
if(!data.records){alert("Programming problem. Drag data contained no Records");return false;}
for(var i=0;i<data.records.length;i++){var r=data.records[i];var dup=this.store.getById(r.id);if(dup&&(dd!=this.dragZone)){if(!this.allowDup&&!this.allowTrash){Ext.fly(this.getNode(this.store.indexOf(dup))).frame("red",1);return true}
var x=new Ext.data.Record();r.id=x.id;delete x;}
if(data.copy){this.store.insert(insertAt++,r.copy());}else{if(data.sourceView){data.sourceView.isDirtyFlag=true;data.sourceView.store.remove(r);}
if(!this.allowTrash)this.store.insert(insertAt++,r);}
if(this.sortField){this.store.sort(this.sortField,this.sortDir);}
this.isDirtyFlag=true;}
this.dragZone.cachedTarget=null;return true;},onEndDrag:function(data,e){var d=Ext.get(this.dragData.ddel);if(d&&d.hasClass("multi-proxy")){d.remove();}},removeDropIndicators:function(n){if(n){Ext.fly(n).removeClass(["x-view-drag-insert-above","x-view-drag-insert-left","x-view-drag-insert-right","x-view-drag-insert-below"]);this.lastInsertClass="_noclass";}},setDeletable:function(imageUrl){if(!this.singleSelect&&!this.multiSelect){this.singleSelect=true;}
var c=this.getContextMenu();this.contextMenu.on("itemclick",function(item){switch(item.id){case"delete":this.remove(this.getSelectedIndexes());break;}},this);this.contextMenu.add({icon:imageUrl||AU.resolveUrl("/images/delete.gif"),id:"delete",text:AU.getMessage("deleteItem")});},getContextMenu:function(){if(!this.contextMenu){this.contextMenu=new Ext.menu.Menu({id:this.id+"-contextmenu"});this.el.on("contextmenu",this.showContextMenu,this);}
return this.contextMenu;},disableContextMenu:function(){if(this.contextMenu){this.el.un("contextmenu",this.showContextMenu,this);}},showContextMenu:function(e,item){item=this.findItemFromChild(e.getTarget());if(item){e.stopEvent();this.select(this.getNode(item),this.multiSelect&&e.ctrlKey,true);this.contextMenu.showAt(e.getXY());}},remove:function(selectedIndices){selectedIndices=[].concat(selectedIndices);for(var i=0;i<selectedIndices.length;i++){var rec=this.store.getAt(selectedIndices[i]);this.store.remove(rec);}},onDblClick:function(e){var item=this.findItemFromChild(e.getTarget());if(item){if(this.fireEvent("dblclick",this,this.indexOf(item),item,e)===false){return false;}
if(this.dragGroup){var targets=Ext.dd.DragDropMgr.getRelated(this.dragZone,true);while(targets.indexOf(this.dropZone)!==-1){targets.remove(this.dropZone);}
if((targets.length==1)&&(targets[0].owningView)){this.dragZone.cachedTarget=null;var el=Ext.get(targets[0].getEl());var box=el.getBox(true);targets[0].onNodeDrop(el.dom,{target:el.dom,xy:[box.x,box.y+box.height-1]},null,this.getDragData(e));}}}},onItemClick:function(item,index,e){if(this.ignoreNextClick){delete this.ignoreNextClick;return;}
if(this.fireEvent("beforeclick",this,index,item,e)===false){return false;}
if(this.multiSelect||this.singleSelect){if(this.multiSelect&&e.shiftKey&&this.lastSelection){this.select(this.getNodes(this.indexOf(this.lastSelection),index),false);}else if(this.isSelected(item)&&e.ctrlKey){this.deselect(item);}else{this.deselect(item);this.select(item,this.multiSelect&&e.ctrlKey);this.lastSelection=item;}
e.preventDefault();}
return true;}});

/* Msg */
Ext.message=function(){var msgCt;function createBox(bool,s){var icon=bool?'../../images/check.png':'../../images/error2.png';return['<div class="msg">','<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>','<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><img src="'+icon+'" class="icon"/>',s,'</div></div></div>','<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>','</div>'].join('');}
return{msg:function(bool,format){if(!msgCt){msgCt=Ext.DomHelper.insertFirst(document.body,{id:'msg-div'},true);}
msgCt.alignTo(document,'t-t');var s=String.format.apply(String,Array.prototype.slice.call(arguments,1));var m=Ext.DomHelper.append(msgCt,{html:createBox(bool,s)},true);m.slideIn('t').pause(3).ghost("t",{remove:true});},init:function(){var t=Ext.get('exttheme');if(!t){return;}
var theme=Cookies.get('exttheme')||'aero';if(theme){t.dom.value=theme;Ext.getBody().addClass('x-'+theme);}
t.on('change',function(){Cookies.set('exttheme',t.getValue());setTimeout(function(){window.location.reload();},250);});var lb=Ext.get('lib-bar');if(lb){lb.show();}}};}();

/* MultiSelect */
Ext.ux.Multiselect=Ext.extend(Ext.form.Field,{appendOnly:false,dataFields:[],data:[],width:100,height:100,displayField:0,valueField:1,allowBlank:true,minLength:0,maxLength:Number.MAX_VALUE,blankText:Ext.form.TextField.prototype.blankText,minLengthText:'Minimum {0} item(s) required',maxLengthText:'Maximum {0} item(s) allowed',delimiter:',',copy:false,allowDup:false,allowTrash:false,focusClass:undefined,sortDir:'ASC',defaultAutoCreate:{tag:"div"},initComponent:function(){Ext.ux.Multiselect.superclass.initComponent.call(this);this.addEvents({'dblclick':true,'click':true,'change':true,'drop':true});},onRender:function(ct,position){Ext.ux.Multiselect.superclass.onRender.call(this,ct,position);var cls='ux-mselect';var fs=new Ext.form.FieldSet({renderTo:this.el,title:this.legend,height:this.height,width:this.width,style:"padding:0;",tbar:this.tbar});fs.body.addClass(cls);var tpl='<tpl for="."><div class="'+cls+'-item';if(Ext.isIE||Ext.isIE7){tpl+='" unselectable=on';}else{tpl+=' x-unselectable"';}
tpl+='>{'+this.displayField+'}</div></tpl>';if(!this.store){this.store=new Ext.data.SimpleStore({fields:this.dataFields,data:this.data});}
this.view=new Ext.ux.DDView({multiSelect:true,store:this.store,selectedClass:cls+"-selected",tpl:tpl,allowDup:this.allowDup,copy:this.copy,allowTrash:this.allowTrash,dragGroup:this.dragGroup,dropGroup:this.dropGroup,itemSelector:"."+cls+"-item",isFormField:false,applyTo:fs.body,appendOnly:this.appendOnly,sortField:this.sortField,sortDir:this.sortDir});fs.add(this.view);this.view.on('click',this.onViewClick,this);this.view.on('beforeClick',this.onViewBeforeClick,this);this.view.on('dblclick',this.onViewDblClick,this);this.view.on('drop',function(ddView,n,dd,e,data){return this.fireEvent("drop",ddView,n,dd,e,data);},this);this.hiddenName=this.name;var hiddenTag={tag:"input",type:"hidden",value:"",name:this.name};if(this.isFormField){this.hiddenField=this.el.createChild(hiddenTag);}else{this.hiddenField=Ext.get(document.body).createChild(hiddenTag);}
fs.doLayout();},initValue:Ext.emptyFn,onViewClick:function(vw,index,node,e){var arrayIndex=this.preClickSelections.indexOf(index);if(arrayIndex!=-1)
{this.preClickSelections.splice(arrayIndex,1);this.view.clearSelections(true);this.view.select(this.preClickSelections);}
this.fireEvent('change',this,this.getValue(),this.hiddenField.dom.value);this.hiddenField.dom.value=this.getValue();this.fireEvent('click',this,e);this.validate();},onViewBeforeClick:function(vw,index,node,e){this.preClickSelections=this.view.getSelectedIndexes();if(this.disabled){return false;}},onViewDblClick:function(vw,index,node,e){return this.fireEvent('dblclick',vw,index,node,e);},getValue:function(valueField){var returnArray=[];var selectionsArray=this.view.getSelectedIndexes();if(selectionsArray.length==0){return'';}
for(var i=0;i<selectionsArray.length;i++){returnArray.push(this.store.getAt(selectionsArray[i]).get(((valueField!=null)?valueField:this.valueField)));}
return returnArray.join(this.delimiter);},setValue:function(values){var index;var selections=[];this.view.clearSelections();this.hiddenField.dom.value='';if(!values||(values=='')){return;}
if(!(values instanceof Array)){values=values.split(this.delimiter);}
for(var i=0;i<values.length;i++){index=this.view.store.indexOf(this.view.store.query(this.valueField,new RegExp('^'+values[i]+'$',"i")).itemAt(0));selections.push(index);}
this.view.select(selections);this.hiddenField.dom.value=this.getValue();this.validate();},reset:function(){this.setValue('');},getRawValue:function(valueField){var tmp=this.getValue(valueField);if(tmp.length){tmp=tmp.split(this.delimiter);}
else{tmp=[];}
return tmp;},setRawValue:function(values){setValue(values);},validateValue:function(value){if(value.length<1){if(this.allowBlank){this.clearInvalid();return true;}else{this.markInvalid(this.blankText);return false;}}
if(value.length<this.minLength){this.markInvalid(String.format(this.minLengthText,this.minLength));return false;}
if(value.length>this.maxLength){this.markInvalid(String.format(this.maxLengthText,this.maxLength));return false;}
return true;}});Ext.reg("multiselect",Ext.ux.Multiselect);

