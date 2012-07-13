Ext.onReady( function() {
    Ext.BLANK_IMAGE_URL = '../resources/ext-ux/theme/gray-extend/gray-extend/s.gif';
    Ext.Ajax.method = 'GET';
	Ext.layout.FormLayout.prototype.trackLabels = true;
    Ext.QuickTips.init();
	document.body.oncontextmenu = function(){return false;};
    
	G.vars.map = new OpenLayers.Map({
        controls: [new OpenLayers.Control.MouseToolbar()],
        displayProjection: new OpenLayers.Projection('EPSG:4326'),
        maxExtent: new OpenLayers.Bounds(-20037508, -20037508, 20037508, 20037508)
    });
    
    G.vars.mask = new Ext.LoadMask(Ext.getBody(),{msg:G.i18n.loading,msgCls:'x-mask-loading2'});
    G.vars.parameter = G.util.getUrlParam('id') ? {id: G.util.getUrlParam('id')} : {id: null};
    G.vars.parameter.base = G.util.getUrlParam('base') ? G.util.getUrlParam('base') : 'none';
    
    Ext.Ajax.request({
        url: G.conf.path_mapping + 'initialize' + G.conf.type,
        params: {id: G.vars.parameter.id || null},
        success: function(r) {
            var init = Ext.util.JSON.decode(r.responseText);
            G.vars.parameter.mapView = init.mapView;
            G.user.initBaseLayers = init.baseLayers;
            G.user.initOverlays = init.overlays;
            G.user.isAdmin = init.security.isAdmin;
            G.system.infrastructuralDataElements = init.systemSettings.infrastructuralDataElements;
            G.system.infrastructuralPeriodType = init.systemSettings.infrastructuralPeriodType;
            G.system.rootNode = init.rootNode;

    /* Section: stores */
    var mapViewStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllMapViews' + G.conf.type,
        root: 'mapViews',
        fields: [
            'id', 'name', 'userId', 'mapValueType', 'indicatorGroupId', 'indicatorId', 'dataElementGroupId', 'dataElementId',
            'periodTypeId', 'periodId', 'parentOrganisationUnitId', 'parentOrganisationUnitName', 'parentOrganisationUnitLevel',
            'organisationUnitLevel', 'organisationUnitLevelName', 'mapLegendType', 'method', 'classes', 'bounds', 'colorLow', 'colorHigh',
            'mapLegendSetId', 'radiusLow', 'radiusHigh', 'longitude', 'latitude', 'zoom'
        ],
        autoLoad: false,
        isLoaded: false,
        sortInfo: {field: 'userId', direction: 'ASC'},
        listeners: {
            'load': G.func.storeLoadListener
        }
    });

    var indicatorGroupStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllIndicatorGroups' + G.conf.type,
        root: 'indicatorGroups',
        fields: ['id', 'name'],
        idProperty: 'id',
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
	var indicatorStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllIndicators' + G.conf.type,
        root: 'indicators',
        fields: ['id', 'shortName'],
        sortInfo: {field: 'shortName', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
  
    var dataElementGroupStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllDataElementGroups' + G.conf.type,
        root: 'dataElementGroups',
        fields: ['id', 'name'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
    var dataElementStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllDataElements' + G.conf.type,
        root: 'dataElements',
        fields: ['id', 'shortName'],
        sortInfo: {field: 'shortName', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': function(store) {
                store.isLoaded = true;
            }
        }
    });
    
    var periodTypeStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllPeriodTypes' + G.conf.type,
        root: 'periodTypes',
        fields: ['name', 'displayName'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
    var infrastructuralPeriodTypeStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllPeriodTypes' + G.conf.type,
        root: 'periodTypes',
        fields: ['name', 'displayName'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
        
    var infrastructuralPeriodsByTypeStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getPeriodsByPeriodType' + G.conf.type,
        root: 'periods',
        fields: ['id', 'name'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
	var predefinedMapLegendStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllMapLegends' + G.conf.type,
        root: 'mapLegends',
        fields: ['id', 'name', 'startValue', 'endValue', 'color', 'image', 'displayString'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });    
    
    var predefinedMapLegendSetStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getMapLegendSetsByType' + G.conf.type,
        baseParams: {type: G.conf.map_legendset_type_predefined},
        root: 'mapLegendSets',
        fields: ['id', 'name', 'symbolizer', 'indicators', 'dataelements'],
        sortInfo: {field:'name', direction:'ASC'},
        autoLoad: false,
        isLoaded: false,
        legendType: null,
        listeners: {
            'load': G.func.storeLoadListener
        }
    }); 
    
    var predefinedColorMapLegendSetStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getMapLegendSetsByType' + G.conf.type,
        baseParams: {type: G.conf.map_legendset_type_predefined, symbolizer: G.conf.map_legend_symbolizer_color},
        root: 'mapLegendSets',
        fields: ['id', 'name', 'symbolizer', 'indicators', 'dataelements'],
        sortInfo: {field:'name', direction:'ASC'},
        autoLoad: false,
        isLoaded: false,
        legendType: null,
        listeners: {
            'load': G.func.storeLoadListener
        }
    }); 
    
    var predefinedImageMapLegendSetStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getMapLegendSetsByType' + G.conf.type,
        baseParams: {type: G.conf.map_legendset_type_predefined, symbolizer: G.conf.map_legend_symbolizer_image},
        root: 'mapLegendSets',
        fields: ['id', 'name', 'symbolizer', 'indicators', 'dataelements'],
        sortInfo: {field:'name', direction:'ASC'},
        autoLoad: false,
        isLoaded: false,
        legendType: null,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
	var organisationUnitLevelStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getAllOrganisationUnitLevels' + G.conf.type,
        root: 'organisationUnitLevels',
        fields: ['id', 'level', 'name'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
	var organisationUnitsAtLevelStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getOrganisationUnitsAtLevel' + G.conf.type,
        baseParams: {level: 1},
        root: 'organisationUnits',
        fields: ['id', 'name'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
	var geojsonFilesStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getGeoJsonFiles' + G.conf.type,
        root: 'files',
        fields: ['name'],
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
    var baseLayerStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getMapLayersByType' + G.conf.type,
        baseParams: {type: G.conf.map_layer_type_baselayer},
        root: 'mapLayers',
        fields: ['id', 'name', 'url', 'layers'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });    
    
    var overlayStore = new Ext.data.JsonStore({
        url: G.conf.path_mapping + 'getMapLayersByType' + G.conf.type,
        baseParams: {type: G.conf.map_layer_type_overlay},
        root: 'mapLayers',
        fields: ['id', 'name', 'type', 'url', 'fillColor', 'fillOpacity', 'strokeColor', 'strokeWidth'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
    var groupSetStore = new Ext.data.JsonStore({
        url: G.conf.path_commons + 'getOrganisationUnitGroupSets' + G.conf.type,
        root: 'organisationUnitGroupSets',
        fields: ['id', 'name'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': function(s) {
                this.isLoaded = true;
            }
        }
    });
    
    var groupsByGroupSetStore = new Ext.data.JsonStore({
        url: G.conf.path_commons + 'getOrganisationUnitGroupsByGroupSet' + G.conf.type,
        root: 'organisationUnitGroups',
        fields: ['id', 'name'],
        sortInfo: {field: 'name', direction: 'ASC'},
        autoLoad: false,
        isLoaded: false,
        listeners: {
            'load': G.func.storeLoadListener
        }
    });
    
    var mapLegendTypeIconStore = new Ext.data.ArrayStore({
        fields: ['name', 'css'],
        data: [
            ['0','ux-ic-icon-maplegend-type-0'],
            ['1','ux-ic-icon-maplegend-type-1'],
            ['2','ux-ic-icon-maplegend-type-2'],
            ['3','ux-ic-icon-maplegend-type-3'],
            ['4','ux-ic-icon-maplegend-type-4'],
            ['5','ux-ic-icon-maplegend-type-5']
        ]
    });
    
    G.stores = {
		mapView: mapViewStore,
        indicatorGroup: indicatorGroupStore,
        indicator: indicatorStore,
        dataElementGroup: dataElementGroupStore,
        dataElement: dataElementStore,
        periodType: periodTypeStore,
        infrastructuralPeriodType: infrastructuralPeriodTypeStore,
        infrastructuralPeriodsByType: infrastructuralPeriodsByTypeStore,
        predefinedMapLegend: predefinedMapLegendStore,
        predefinedMapLegendSet: predefinedMapLegendSetStore,
        predefinedColorMapLegendSet: predefinedColorMapLegendSetStore,
        predefinedImageMapLegendSet: predefinedImageMapLegendSetStore,
        organisationUnitLevel: organisationUnitLevelStore,
        organisationUnitsAtLevel: organisationUnitsAtLevelStore,
        geojsonFiles: geojsonFilesStore,
        overlay: overlayStore,
        baseLayer: baseLayerStore,
        groupSet: groupSetStore,
        groupsByGroupSet: groupsByGroupSetStore,
        mapLegendTypeIcon: mapLegendTypeIconStore
    };
    
	/* Thematic layers */
	boundaryLayer = new OpenLayers.Layer.Vector(G.i18n.boundary_layer, {
        strategies: [ new OpenLayers.Strategy.Refresh({force:true}) ],
        'visibility': false,
        'displayInLayerSwitcher': false,
        'styleMap': new OpenLayers.StyleMap({
            'default': new OpenLayers.Style(
                OpenLayers.Util.applyDefaults(
                    {'fillOpacity': 0, 'strokeColor': '#000', 'strokeWidth': 1, 'pointRadius': 5},
                    OpenLayers.Feature.Vector.style['default']
                )
            )
        })
    });
    
    boundaryLayer.layerType = G.conf.map_layer_type_thematic;
    G.vars.map.addLayer(boundaryLayer);
    
    polygonLayer = new OpenLayers.Layer.Vector(G.conf.thematic_layer_1, {
        strategies: [ new OpenLayers.Strategy.Refresh({force:true}) ],
        'visibility': false,
        'displayInLayerSwitcher': false,
        'styleMap': new OpenLayers.StyleMap({
            'default': new OpenLayers.Style(
                OpenLayers.Util.applyDefaults(
                    {'fillOpacity': 1, 'strokeColor': '#fff', 'strokeWidth': 1, 'pointRadius': 5},
                    OpenLayers.Feature.Vector.style['default']
                )
            ),
            'select': new OpenLayers.Style(
                {'strokeColor': '#111', 'strokeWidth': 2, 'cursor': 'pointer'}
            )
        })
    });
    
    polygonLayer.layerType = G.conf.map_layer_type_thematic;
    G.vars.map.addLayer(polygonLayer);
    
    pointLayer = new OpenLayers.Layer.Vector(G.conf.thematic_layer_2, {
        'visibility': false,
        'displayInLayerSwitcher': false,
        'styleMap': new OpenLayers.StyleMap({
            'default': new OpenLayers.Style(
                OpenLayers.Util.applyDefaults(
                    {'fillOpacity': 1, 'strokeColor': '#fff', 'strokeWidth': 1, 'pointRadius': 5},
                    OpenLayers.Feature.Vector.style['default']
                )
            ),
            'select': new OpenLayers.Style(
                {'strokeColor': '#555', 'strokeWidth': 2, 'cursor': 'pointer'}
            )
        })
    });
    
    pointLayer.layerType = G.conf.map_layer_type_thematic;
    G.vars.map.addLayer(pointLayer);
    
    symbolLayer = new OpenLayers.Layer.Vector(G.conf.symbol_layer, {
        'visibility': false,
        'displayInLayerSwitcher': false,
        'styleMap': new OpenLayers.StyleMap({
            'default': new OpenLayers.Style(
                OpenLayers.Util.applyDefaults(
                    {'fillOpacity': 1, 'strokeColor': '#fff', 'strokeWidth': 1, 'pointRadius': 5},
                    OpenLayers.Feature.Vector.style['default']
                )
            ),
            'select': new OpenLayers.Style(
                {'strokeColor': '#555', 'strokeWidth': 2, 'cursor': 'pointer'}
            )
        })
    });
    
    symbolLayer.layerType = G.conf.map_layer_type_thematic;
    G.vars.map.addLayer(symbolLayer);
    
    centroidLayer = new OpenLayers.Layer.Vector(G.conf.centroid_layer, {
        'visibility': false,
        'displayInLayerSwitcher': false,
        'styleMap': new OpenLayers.StyleMap({
            'default': new OpenLayers.Style(
                OpenLayers.Util.applyDefaults(
                    {'fillOpacity': 1, 'strokeColor': '#222222', 'strokeWidth': 1, 'pointRadius': 5},
                    OpenLayers.Feature.Vector.style['default']
                )
            ),
            'select': new OpenLayers.Style(
                {'strokeColor': '#000000', 'strokeWidth': 2, 'cursor': 'pointer'}
            )
        })
    });
    
    centroidLayer.layerType = G.conf.map_layer_type_thematic;
    G.vars.map.addLayer(centroidLayer);    
    
    /* Init base layers */
    if (window.google) {
        var gmap = new OpenLayers.Layer.Google(
            "Google Streets",
            {numZoomLevels: 20, animationEnabled: true}
        );        
        gmap.layerType = G.conf.map_layer_type_baselayer;
        G.vars.map.addLayer(gmap);
        
        var ghyb = new OpenLayers.Layer.Google(
            "Google Hybrid",
            {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20, animationEnabled: true}
        );        
        ghyb.layerType = G.conf.map_layer_type_baselayer;
        G.vars.map.addLayer(ghyb);
    }
	
    var osm = new OpenLayers.Layer.OSM("OpenStreetMap");
    osm.layerType = G.conf.map_layer_type_baselayer;
    G.vars.map.addLayer(osm);
    
    /* Init base layers */
	function addBaseLayersToMap(init) {
        function add(r) {
            if (r.length) {                
                for (var i = 0; i < r.length; i++) {
                    var baseLayer = G.util.createWMSLayer(r[i].data.name, r[i].data.url, r[i].data.layers);                    
                    baseLayer.layerType = G.conf.map_layer_type_baselayer;
                    baseLayer.overlayType = G.conf.map_overlay_type_wms;
                    baseLayer.setVisibility(false);                    
                    G.vars.map.addLayer(baseLayer);
                }
            }
        }
        
        if (init) {
            add(G.user.initBaseLayers);
        }
        else {
            G.stores.baseLayer.load({callback: function(r) {
                add(r);
            }});
        }
	}
	addBaseLayersToMap(true);
    
    /* Init overlays */
	function addOverlaysToMap(init) {
        function add(r) {
            if (r.length) {                
                for (var i = 0; i < r.length; i++) {
                    var overlay = G.util.createOverlay(
                        r[i].data.name, r[i].data.fillColor, 1, r[i].data.strokeColor, parseFloat(r[i].data.strokeWidth),
                        G.conf.path_mapping + 'getGeoJsonFromFile.action?name=' + r[i].data.url
                    );
                    
                    overlay.layerType = G.conf.map_layer_type_overlay;
                    
                    overlay.events.register('loadstart', null, G.func.loadStart);
                    overlay.events.register('loadend', null, G.func.loadEnd);
                    
                    G.vars.map.addLayer(overlay);
					G.vars.map.getLayersByName(r[i].data.name)[0].setZIndex(G.conf.defaultLayerZIndex);
                }
            }
        }
        
        if (init) {
            add(G.user.initOverlays);
        }
        else {
            G.stores.overlay.load({callback: function(r) {
                add(r);
            }});
        }
	}
	addOverlaysToMap(true);
			
	/* Section: mapview */
	var favoriteWindow = new Ext.Window({
        id: 'favorite_w',
        title: '<span id="window-favorites-title">' + G.i18n.favorite_map_views + '</span>',
		bodyStyle: 'padding:8px; background-color:#fff',
		layout: 'fit',
        closeAction: 'hide',
		width: G.conf.window_width,
        items: [
            {
                xtype: 'form',
                labelWidth: G.conf.label_width,
                items: [
                    {html: '<div class="window-info">' + G.i18n.register_map_favorite + '</div>'},
                    {
                        xtype: 'textfield',
                        id: 'favoritename_tf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.display_name,
                        width: G.conf.combo_width_fieldset,
                        autoCreate: {tag: 'input', type: 'text', size: '20', autocomplete: 'off', maxlength: '255'}
                    },
                    {
                        xtype: 'combo',
                        id: 'favoritelayer_cb',
                        fieldLabel: G.i18n.layer,
                        labelSeparator: G.conf.labelseparator,
                        editable: false,
                        valueField: 'id',
                        displayField: 'name',
                        width: G.conf.combo_width_fieldset,
                        minListWidth: G.conf.combo_width_fieldset,
                        mode: 'local',
                        triggerAction: 'all',
                        value: G.conf.map_widget_choropleth,
                        store: new Ext.data.ArrayStore({
                            fields: ['id', 'name'],
                            data: [
                                [G.conf.map_widget_choropleth, G.conf.thematic_layer_1],
                                [G.conf.map_widget_point, G.conf.thematic_layer_2]
                            ]
                        })
                    },
                    {
                        xtype: 'checkbox',
                        id: 'favoritesystem_chb',
                        disabled: !G.user.isAdmin,
                        fieldLabel: G.i18n.system,
                        labelSeparator: G.conf.labelseparator,
                        editable: false
                    },
                    
                    {html: '<div class="window-p"></div>'},
                    {html: '<div class="window-info">' + G.i18n.delete_favorite + ' / ' + G.i18n.add_to_dashboard +'</div>'},
                    {
                        xtype: 'combo',
                        id: 'favorite_cb',
                        fieldLabel: G.i18n.favorite,
                        editable: false,
                        valueField: 'id',
                        displayField: 'name',
                        mode: 'remote',
                        forceSelection: true,
                        triggerAction: 'all',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        selectOnFocus: true,
                        width: G.conf.combo_width_fieldset,
                        store: G.stores.mapView
                    }
                ]
            }
        ],
        bbar: [
            '->',
            {
                xtype: 'button',
                id: 'newview_b',
                iconCls: 'icon-add',
				hideLabel: true,
				text: G.i18n.register,
				handler: function() {
					var vn = Ext.getCmp('favoritename_tf').getValue();
                    if (!vn) {
						Ext.message.msg(false, G.i18n.form_is_not_complete);
						return;
					}
                    var widget = Ext.getCmp('favoritelayer_cb').getValue() == G.conf.map_widget_choropleth ?
                        choropleth : point;
                    
                    if (!widget.layer.features.length) {
						Ext.message.msg(false, '<span class="x-msg-hl">' + widget.layer.name + '</span> is empty');
						return;
                    }   
                    
                    var params = widget.formValues.getAllValues.call(widget);                    
                    params.name = vn;
                    params.system = Ext.getCmp('favoritesystem_chb').getValue();
                    
                    Ext.Ajax.request({
                        url: G.conf.path_mapping + 'addMapView' + G.conf.type,
                        method: 'POST',
                        params: params,
                        success: function(r) {
                            Ext.message.msg(true, G.i18n.favorite + ' <span class="x-msg-hl">' + vn + '</span> ' + G.i18n.registered);
                            G.stores.mapView.load({callback: function() {
                                favoriteButton.reloadMenu();
                            }});
                            Ext.getCmp('favoritename_tf').reset();
                            Ext.getCmp('favoritesystem_chb').reset();
                        }
                    });
				}
			},
            {
                xtype: 'tbseparator',
                cls: 'xtb-sep-panel'
            },
            {
                xtype: 'button',
                id: 'deleteview_b',
                iconCls: 'icon-remove',
				hideLabel: true,
				text: G.i18n.delete_,
				handler: function() {
					var v = Ext.getCmp('favorite_cb').getValue();
					var rw = Ext.getCmp('favorite_cb').getRawValue();
                    
                    if (v) {
                        var userId = G.stores.mapView.getAt(G.stores.mapView.findExact('id', v)).data.userId;
                        if (userId || G.user.isAdmin) {                            
                            Ext.Ajax.request({
                                url: G.conf.path_mapping + 'deleteMapView' + G.conf.type,
                                method: 'POST',
                                params: {id: v},
                                success: function(r) {
                                    Ext.message.msg(true, G.i18n.favorite + ' <span class="x-msg-hl">' + rw + '</span> ' + G.i18n.deleted);
                                    Ext.getCmp('favorite_cb').clearValue();
                                    
                                    G.stores.mapView.load({callback: function() {
                                        favoriteButton.reloadMenu();
                                    }});
                                    
                                    if (v == choropleth.cmp.mapview.getValue()) {
                                        choropleth.cmp.mapView.clearValue();
                                    }
                                    if (v == point.cmp.mapview.getValue()) {
                                        point.cmp.mapView.clearValue();
                                    }
                                }
                            });
                        }
                        else {
                            Ext.message.msg(false, 'Access denied');
                        }
                    }
                    else {
                        Ext.message.msg(false, G.i18n.please_select_a_map_view);
                        return;
                    }

				}
			},
            {
                xtype: 'button',
                id: 'dashboardview_b',
                iconCls: 'icon-assign',
				hideLabel: true,
				text: G.i18n.add,
				handler: function() {
					var v = Ext.getCmp('favorite_cb').getValue();
					var rv = Ext.getCmp('favorite_cb').getRawValue();
					
					if (!v) {
						Ext.message.msg(false, G.i18n.please_select_a_map_view);
						return;
					}
					
					Ext.Ajax.request({
						url: G.conf.path_mapping + 'addMapViewToDashboard' + G.conf.type,
						method: 'POST',
						params: {id: v},
						success: function(r) {
                            Ext.getCmp('favorite_cb').clearValue();
							Ext.message.msg(true, G.i18n.favorite + ' <span class="x-msg-hl">' + rv + '</span> ' + G.i18n.added_to_dashboard);
						}
					});
				}
            }
        ],
        listeners: {
            'show': function() {
                if (!polygonLayer.visibility && pointLayer.visibility) {
                    Ext.getCmp('favoritelayer_cb').setValue(G.conf.map_widget_point);
                }
            }
        }
    });
    favoriteWindow.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
	
	/* Section: export map */
	var exportImageWindow = new Ext.Window({
        id: 'exportimage_w',
        title: '<span id="window-image-title">Export image</span>',
		bodyStyle: 'padding:8px; background-color:#fff',
        layout: 'fit',
        closeAction: 'hide',
		width: G.conf.window_width,
        items: [
            {
                xtype: 'form',
                labelWidth: G.conf.label_width,
                items: [
                    {html: '<div class="window-info">' + G.i18n.export_thematic_map_to_png + '</div>'},
                    {
                        xtype: 'textfield',
                        id: 'exportimagetitle_tf',
                        fieldLabel: G.i18n.title,
                        labelSeparator: G.conf.labelseparator,
                        editable: true,
                        valueField: 'id',
                        displayField: 'text',
                        width: G.conf.combo_width_fieldset,
                        triggerAction: 'all'
                    },
                    {
                        xtype: 'checkbox',
                        id: 'exportimageincludelegend_chb',
                        fieldLabel: G.i18n.legend,
                        labelSeparator: '',
                        isFormField: true,
                        checked: true
                    }
                ]
            }
        ],
        bbar: [
            '->',
            {
                xtype: 'button',
                id: 'exportimage_b',
				labelSeparator: G.conf.labelseparator,
                iconCls: 'icon-assign',
				text: G.i18n.export_,
				handler: function() {
                    var values, svg;
                    
                    if (polygonLayer.visibility && pointLayer.visibility) {
                        if (choropleth.formValidation.validateForm.call(choropleth)) {
                            if (point.formValidation.validateForm.call(point)) {
                                document.getElementById('layerField').value = 3;
                                document.getElementById('imageLegendRowsField').value = choropleth.imageLegend.length;
                                
                                values = choropleth.formValues.getImageExportValues.call(choropleth);
                                document.getElementById('periodField').value = values.dateValue;
                                document.getElementById('indicatorField').value = values.mapValueTypeValue;
                                document.getElementById('legendsField').value = G.util.getLegendsJSON.call(choropleth);
                                
                                values = point.formValues.getImageExportValues.call(point);
                                document.getElementById('periodField2').value = values.dateValue;
                                document.getElementById('indicatorField2').value = values.mapValueTypeValue;
                                document.getElementById('legendsField2').value = G.util.getLegendsJSON.call(point);
                                
                                var str1 = document.getElementById(polygonLayer.svgId).parentNode.innerHTML;
                                var str2 = document.getElementById(pointLayer.svgId).parentNode.innerHTML;
                                svg = G.util.mergeSvg(str1, [str2]);                                
                            }
                            else {
                                Ext.message.msg(false, '<span class="x-msg-hl">' + G.conf.thematic_layer_1 + '</span> not rendered');
                                return;
                            }
                        }
                        else {
                            Ext.message.msg(false, '<span class="x-msg-hl">' + G.conf.thematic_layer_2 + '</span> not rendered');
                            return;
                        }
                    }
                    else if (polygonLayer.visibility) {
                        if (choropleth.formValidation.validateForm.call(choropleth)) {
                            values = choropleth.formValues.getImageExportValues.call(choropleth);
                            document.getElementById('layerField').value = 1;
                            document.getElementById('periodField').value = values.dateValue;
                            document.getElementById('indicatorField').value = values.mapValueTypeValue;
                            document.getElementById('legendsField').value = G.util.getLegendsJSON.call(choropleth);
                            svg = document.getElementById(polygonLayer.svgId).parentNode.innerHTML;
                        }
                        else {
                            Ext.message.msg(false, '<span class="x-msg-hl">' + G.conf.thematic_layer_1 + '</span> not rendered');
                            return;
                        }
                    }
                    else if (pointLayer.visibility) {
                        if (point.formValidation.validateForm.call(point)) {
                            values = point.formValues.getImageExportValues.call(point);
                            document.getElementById('layerField').value = 2;
                            document.getElementById('periodField').value = values.dateValue;  
                            document.getElementById('indicatorField').value = values.mapValueTypeValue;
                            document.getElementById('legendsField').value = G.util.getLegendsJSON.call(point);
                            svg = document.getElementById(pointLayer.svgId).parentNode.innerHTML;
                        }
                        else {
                            Ext.message.msg(false, '<span class="x-msg-hl">' + G.conf.thematic_layer_2 + '</span> not rendered');
                            return;
                        }
                    }
                    else {                        
                        document.getElementById('layerField').value = 0;
                    }
                    
                    var overlays = G.util.getVisibleLayers(G.util.getLayersByType(G.conf.map_layer_type_overlay));
                    svg = G.util.mergeSvg(svg, G.util.getOverlaysSvg(overlays));
                    
                    if (!svg) {
                        Ext.message.msg(false, 'No layers to export');
                        return;
                    }                        
                    
                    var title = Ext.getCmp('exportimagetitle_tf').getValue();
                                        
                    if (!title) {
                        Ext.message.msg(false, G.i18n.form_is_not_complete);
                    }
                    else {
                        var exportForm = document.getElementById('exportForm');
                        exportForm.action = '../exportImage.action';
                        exportForm.method = 'post';
                        
                        document.getElementById('titleField').value = title;
                        document.getElementById('svgField').value = svg;
                        document.getElementById('includeLegendsField').value = Ext.getCmp('exportimageincludelegend_chb').getValue();

                        exportForm.submit();
                        Ext.getCmp('exportimagetitle_tf').reset();
                    }
				}
            }
        ]    
    });
    exportImageWindow.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
	
	/* Section: predefined map legend set */
    var predefinedMapLegendSetWindow = new Ext.Window({
        id: 'predefinedmaplegendset_w',
        title: '<span id="window-predefinedlegendset-title">' + G.i18n.predefined_legend_sets + '</span>',
		layout: 'accordion',
        closeAction: 'hide',
		autoHeight: true,
		height: 'auto',
		width: G.conf.window_width,
        items: [
            {
                title: G.i18n.legend,
				bodyStyle: 'padding:8px; background-color:#fff',
				autoHeight: true,
				height: 'auto',
                items: [
                    {
                        xtype: 'form',
                        labelWidth: G.conf.label_width,
                        items: [
                            {html: '<div class="window-info">' + G.i18n.register_new_legend +'</div>'},
                            {
                                xtype: 'textfield',
                                id: 'predefinedmaplegendname_tf',
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.display_name,
                                width: G.conf.combo_width_fieldset
                            },
                            {
                                xtype: 'numberfield',
                                id: 'predefinedmaplegendstartvalue_nf',
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.start_value,
                                width: G.conf.combo_number_width_small
                            },
                            {
                                xtype: 'numberfield',
                                id: 'predefinedmaplegendendvalue_nf',
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.end_value,
                                width: G.conf.combo_number_width_small
                            },
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendtype_cb',
                                fieldLabel: G.i18n.legend_symbolizer,
                                labelSeparator: G.conf.labelseparator,
                                editable: false,
                                valueField: 'id',
                                displayField: 'symbolizer',
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                mode: 'local',
                                triggerAction: 'all',
                                value: 'color',
                                store: new Ext.data.ArrayStore({
                                    fields: ['id','symbolizer'],
                                    data: [
                                        [G.conf.map_legend_symbolizer_color, G.i18n.color],
                                        [G.conf.map_legend_symbolizer_image, G.i18n.image]
                                    ]
                                }),
                                listeners: {
                                    'select': function(cb) {
                                        if (cb.getValue() == G.conf.map_legend_symbolizer_color) {
                                            Ext.getCmp('predefinedmaplegendcolor_cf').show();
                                            Ext.getCmp('predefinedmaplegendimage_cb').hide();
                                        }
                                        else if (cb.getValue() == G.conf.map_legend_symbolizer_image) {
                                            Ext.getCmp('predefinedmaplegendcolor_cf').hide();
                                            Ext.getCmp('predefinedmaplegendimage_cb').show();
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'colorfield',
                                id: 'predefinedmaplegendcolor_cf',
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.color,
                                allowBlank: false,
                                width: G.conf.combo_width_fieldset,
                                value:"#C0C0C0"
                            },
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendimage_cb',
                                plugins: new Ext.ux.plugins.IconCombo(),
                                valueField: 'name',
                                displayField: 'css',
                                iconClsField: 'css',
                                editable: false,
                                triggerAction: 'all',
                                mode: 'local',
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.image,
                                hidden: true,
                                width: G.conf.combo_number_width_small,
                                listWidth: G.conf.combo_number_width_small,
                                store: G.stores.mapLegendTypeIcon
                            },
                            {html: '<div class="window-p"></div>'},
                            {html: '<div class="window-info">' + G.i18n.delete_legend + '</div>'},
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegend_cb',
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                mode: 'remote',
                                forceSelection: true,
                                triggerAction: 'all',
                                selectOnFocus: true,
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.legend,
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                store: G.stores.predefinedMapLegend,
                                listeners: {
                                    'focus': function(cb) {
                                        cb.getStore().clearFilter();
                                    }
                                }
                            }
                        ]
                    }
                ],
				bbar: [
					'->',
					{
						xtype: 'button',
						id: 'newpredefinedmaplegend_b',
						text: G.i18n.register,
						iconCls: 'icon-add',
						handler: function() {
							var mln = Ext.getCmp('predefinedmaplegendname_tf').getValue();
							var mlsv = parseFloat(Ext.getCmp('predefinedmaplegendstartvalue_nf').getValue());
							var mlev = parseFloat(Ext.getCmp('predefinedmaplegendendvalue_nf').getValue());
							var type = Ext.getCmp('predefinedmaplegendtype_cb').getValue();
							var mlc = type == G.conf.map_legend_symbolizer_color ?
								Ext.getCmp('predefinedmaplegendcolor_cf').getValue() : null;
							var mli = type == G.conf.map_legend_symbolizer_image ?
								Ext.getCmp('predefinedmaplegendimage_cb').getRawValue() : null;
							
							if (!Ext.isNumber(parseFloat(mlsv)) || !Ext.isNumber(mlev)) {
								Ext.message.msg(false, G.i18n.form_is_not_complete);
								return;
							}
							
							if (!mln || (!mlc && !mli)) {
								Ext.message.msg(false, G.i18n.form_is_not_complete);
								return;
							}
							
							if (!G.util.validateInputNameLength(mln)) {
								Ext.message.msg(false, G.i18n.name + ': ' + G.i18n.max + ' 25 ' + G.i18n.characters);
								return;
							}
							
							if (G.stores.predefinedMapLegend.findExact('name', mln) !== -1) {
								Ext.message.msg(false, G.i18n.legend + ' <span class="x-msg-hl">' + mln + '</span> ' + G.i18n.already_exists);
								return;
							}
							
							var params = {};
							params.name = mln;
							params.startValue = mlsv;
							params.endValue = mlev;                                            
							if (type == G.conf.map_legend_symbolizer_color) {
								params.color = mlc;
							}
							else if (type == G.conf.map_legend_symbolizer_image) {
								params.image = mli;
							}
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'addOrUpdateMapLegend' + G.conf.type,
								method: 'POST',
								params: params,
								success: function(r) {
									Ext.message.msg(true, G.i18n.legend + ' <span class="x-msg-hl">' + mln + '</span> ' + G.i18n.was_registered);
									G.stores.predefinedMapLegend.load();
									Ext.getCmp('predefinedmaplegendname_tf').reset();
									Ext.getCmp('predefinedmaplegendstartvalue_nf').reset();
									Ext.getCmp('predefinedmaplegendendvalue_nf').reset();
								}
							});
						}
					},
					{
						xtype: 'tbseparator',
						cls: 'xtb-sep-panel'
					},
					{
						xtype: 'button',
						id: 'deletepredefinedmaplegend_b',
						text: G.i18n.delete_,
						iconCls: 'icon-remove',
						handler: function() {
							var mlv = Ext.getCmp('predefinedmaplegend_cb').getValue();
							var mlrv = Ext.getCmp('predefinedmaplegend_cb').getRawValue();
							
							if (!mlv) {
								Ext.message.msg(false, G.i18n.please_select_a_legend);
								return;
							}
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'deleteMapLegend' + G.conf.type,
								method: 'POST',
								params: {id: mlv},
								success: function(r) {
									Ext.message.msg(true, G.i18n.legend + ' <span class="x-msg-hl">' + mlrv + '</span> ' + G.i18n.was_deleted);
									G.stores.predefinedMapLegend.load();
									Ext.getCmp('predefinedmaplegend_cb').clearValue();
								}
							});
						}
					}
				],
                listeners: {
                    expand: function(w) {
                        predefinedMapLegendSetWindow.syncSize();
                    },
                    collapse: function(w) {
                        predefinedMapLegendSetWindow.syncSize();
					}
                }
            },
            
            {
                title: G.i18n.legendset,
				bodyStyle: 'padding:8px; background-color:#fff',
				autoHeight: true,
				height: 'auto',
                items: [
                    {
                        xtype: 'form',
                        labelWidth: G.conf.label_width,
                        items: [
                            {html: '<div class="window-info">' + G.i18n.register_new_legend_set + '</div>'},
                            {
                                xtype: 'textfield',
                                id: 'predefinedmaplegendsetname_tf',
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.display_name,
                                width: G.conf.combo_width_fieldset
                            },
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendsettype_cb',
                                fieldLabel: G.i18n.legend_symbolizer,
                                labelSeparator: G.conf.labelseparator,
                                editable: false,
                                valueField: 'id',
                                displayField: 'symbolizer',
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                mode: 'local',
                                triggerAction: 'all',
                                store: new Ext.data.ArrayStore({
                                    fields: ['id','symbolizer'],
                                    data: [
                                        [G.conf.map_legend_symbolizer_color, G.i18n.color],
                                        [G.conf.map_legend_symbolizer_image, G.i18n.image]
                                    ]
                                }),
                                listeners: {
                                    'select': function(cb) {
                                        G.stores.predefinedMapLegend.filterBy( function(r, rid) {
                                            if (cb.getValue() == G.conf.map_legend_symbolizer_color) {
                                                return r.data.color;
                                            }
                                            else if (cb.getValue() == G.conf.map_legend_symbolizer_image) {
                                                return r.data.image;
                                            }
                                        });
                                    }
                                }
                            },
                            {html: '<div class="window-field-label">' + G.i18n.legends + '</div>'},
                            {
                                xtype: 'multiselect',
                                id: 'predefinednewmaplegend_ms',
                                hideLabel: true,
                                dataFields: ['id', 'name', 'startValue', 'endValue', 'color', 'displayString'],
                                valueField: 'id',
                                displayField: 'displayString',
                                width: G.conf.multiselect_width,
                                height: G.util.getMultiSelectHeight() / 2,
                                store: G.stores.predefinedMapLegend
                            },
                            {html: '<div class="window-p"></div>'},
                            {html: '<div class="window-info">' + G.i18n.delete_legend_set + '</div>'},
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendset_cb',
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                mode: 'remote',
                                forceSelection: true,
                                triggerAction: 'all',
                                selectOnFocus: true,
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.legendset,
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                store: G.stores.predefinedMapLegendSet
                            }
                        ]
                    }
				],
				bbar: [
					'->',
					{
						xtype: 'button',
						id: 'newpredefinedmaplegendset_b',
						text: G.i18n.register,
						iconCls: 'icon-add',
						handler: function() {
							var mlsv = Ext.getCmp('predefinedmaplegendsetname_tf').getValue();
							var mlms = Ext.getCmp('predefinednewmaplegend_ms').getValue();
							var array = [];
							
							if (mlms) {
								array = mlms.split(',');
								if (array.length > 1) {
									for (var i = 0; i < array.length; i++) {
										var sv = G.stores.predefinedMapLegend.getById(array[i]).get('startValue');
										var ev = G.stores.predefinedMapLegend.getById(array[i]).get('endValue');
										for (var j = 0; j < array.length; j++) {
											if (j != i) {
												var temp_sv = G.stores.predefinedMapLegend.getById(array[j]).get('startValue');
												var temp_ev = G.stores.predefinedMapLegend.getById(array[j]).get('endValue');
												for (var k = sv+1; k < ev; k++) {
													if (k > temp_sv && k < temp_ev) {
														Ext.message.msg(false, G.i18n.overlapping_legends_are_not_allowed);
														return;
													}
												}
											}
										}
									}
								}
							}
							else {
								Ext.message.msg(false, G.i18n.form_is_not_complete);
								return;
							}
							
							if (!mlsv) {
								Ext.message.msg(false, G.i18n.form_is_not_complete);
								return;
							}
							
							array = mlms.split(',');
							var params = '?mapLegends=' + array[0];
							if (array.length > 1) {
								for (var l = 1; l < array.length; l++) {
									array[l] = '&mapLegends=' + array[l];
									params += array[l];
								}
							}
							
							var symbolizer = Ext.getCmp('predefinedmaplegendsettype_cb').getValue();
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'addOrUpdateMapLegendSet.action' + params,
								method: 'POST',
								params: {name: mlsv, type: G.conf.map_legendset_type_predefined, symbolizer: symbolizer},
								success: function(r) {
									Ext.message.msg(true, G.i18n.new_legend_set+' <span class="x-msg-hl">' + mlsv + '</span> ' + G.i18n.was_registered);
									Ext.getCmp('predefinedmaplegendsetname_tf').reset();
									Ext.getCmp('predefinednewmaplegend_ms').reset();			
									G.stores.predefinedMapLegendSet.load();
									if (symbolizer == G.conf.map_legend_symbolizer_color) {
										G.stores.predefinedColorMapLegendSet.load();
									}
									else if (symbolizer == G.conf.map_legend_symbolizer_image) {
										G.stores.predefinedImageMapLegendSet.load();
									}
								}
							});
						}
					},
					{
						xtype: 'tbseparator',
						cls: 'xtb-sep-panel'
					},
					{
						xtype: 'button',
						id: 'deletepredefinedmaplegendset_b',
						text: G.i18n.delete_,
						iconCls: 'icon-remove',
						handler: function() {
							var mlsv = Ext.getCmp('predefinedmaplegendset_cb').getValue();
							var mlsrv = Ext.getCmp('predefinedmaplegendset_cb').getRawValue();
							
							if (!mlsv) {
								Ext.message.msg(false, G.i18n.please_select_a_legend_set);
								return;
							}
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'deleteMapLegendSet' + G.conf.type,
								method: 'POST',
								params: {id: mlsv},
								success: function(r) {
									Ext.message.msg(true, G.i18n.legendset + ' <span class="x-msg-hl">' + mlsrv + '</span> ' + G.i18n.was_deleted);
									var store = G.stores.predefinedMapLegendSet;
									var symbolizer = store.getAt(store.find('id', mlsv)).data.symbolizer;
									
									G.stores.predefinedMapLegendSet.load();
									store = symbolizer == G.conf.map_legend_symbolizer_color ?
										G.stores.predefinedColorMapLegendSet : G.stores.predefinedImageMapLegendSet;
									store.load();
									
									Ext.getCmp('predefinedmaplegendset_cb').clearValue();
									if (mlsv == Ext.getCmp('predefinedmaplegendsetindicator_cb').getValue) {
										Ext.getCmp('predefinedmaplegendsetindicator_cb').clearValue();
									}
									if (mlsv == Ext.getCmp('predefinedmaplegendsetdataelement_cb').getValue) {
										Ext.getCmp('predefinedmaplegendsetdataelement_cb').clearValue();
									}
									if (mlsv == choropleth.cmp.mapLegendSet.getValue()) {
										choropleth.cmp.mapLegendSet.clearValue();
									}
									if (mlsv == point.cmp.mapLegendSet.getValue()) {
										point.cmp.mapLegendSet.clearValue();
									}
									if (mlsv == centroid.cmp.mapLegendSet.getValue()) {
										centroid.cmp.mapLegendSet.clearValue();
									}
								}
							});
						}
					}
				],
                listeners: {
                    expand: function() {                        
                        var pmlst = Ext.getCmp('predefinedmaplegendsettype_cb');
                        if (pmlst.getValue()) {
                            G.stores.predefinedMapLegend.filterBy( function(r) {
                                if (pmlst.getValue() == G.conf.map_legend_symbolizer_color) {
                                    return r.data.color;
                                }
                                else if (pmlst.getValue() == G.conf.map_legend_symbolizer_image) {
                                    return r.data.image;
                                }
                            });
                        }
                        else {
                            pmlst.setValue(G.conf.map_legend_symbolizer_color);
                            G.stores.predefinedMapLegend.filterBy( function(r, rid) {
                                return r.data.color;
                            });
                        }
						
						predefinedMapLegendSetWindow.syncSize();
                    },
                    collapse: function() {
                        predefinedMapLegendSetWindow.syncSize();
                    }
                }
            },
            
            {
                title: G.i18n.indicators,
				bodyStyle: 'padding:8px; background-color:#fff',
				autoHeight: true,
				height: 'auto',
                items: [
                    {
                        xtype: 'form',
                        labelWidth: G.conf.label_width,
                        items: [
                            {html: '<div class="window-info">' + G.i18n.assign_indicators_to_legend_set +'</div>'},
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendsetindicator_cb',
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                mode: 'remote',
                                forceSelection: true,
                                triggerAction: 'all',
                                selectOnFocus: true,
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.legendset,
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                store: G.stores.predefinedMapLegendSet,
                                listeners: {
                                    'select': {
                                        fn: function(cb, record) {
                                            var indicators = record.data.indicators || [];
                                            var indicatorString = '';
                                            
                                            for (var i = 0; i < indicators.length; i++) {
                                                indicatorString += indicators[i];
                                                if (i < indicators.length-1) {
                                                    indicatorString += ',';
                                                }
                                            }
                                            
                                            Ext.getCmp('predefinedmaplegendsetindicator_ms').setValue(indicatorString);
                                        }
                                    }
                                }	
                            },
                            {html: '<div class="window-field-label">' + G.i18n.indicators + '</div>'},
                            {
                                xtype: 'multiselect',
                                id: 'predefinedmaplegendsetindicator_ms',
                                hideLabel:true,
                                dataFields: ['id', 'shortName'],
                                valueField: 'id',
                                displayField: 'shortName',
                                width:G.conf.multiselect_width,
                                height: G.util.getMultiSelectHeight(),
                                store: G.stores.indicator
                            }
                        ]
                    }
				],
				bbar: [
					'->',
					{
						xtype: 'button',
						id: 'assignpredefinedmaplegendsetindicator_b',
						text: G.i18n.assign,
						iconCls: 'icon-assign',
						handler: function() {
							var ls = Ext.getCmp('predefinedmaplegendsetindicator_cb').getValue();
							var lsrw = Ext.getCmp('predefinedmaplegendsetindicator_cb').getRawValue();
							var lims = Ext.getCmp('predefinedmaplegendsetindicator_ms').getValue();
							
							if (!ls) {
								Ext.message.msg(false, G.i18n.please_select_a_legend_set);
								return;
							}
							
							if (!lims) {
								Ext.message.msg(false, G.i18n.please_select_at_least_one_indicator);
								return;
							}
							
							var array = [];
							array = lims.split(',');
							var params = '?indicators=' + array[0];
							
							if (array.length > 1) {
								for (var i = 1; i < array.length; i++) {
									array[i] = '&indicators=' + array[i];
									params += array[i];
								}
							}
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'assignIndicatorsToMapLegendSet.action' + params,
								method: 'POST',
								params: {id: ls},
								success: function(r) {
									Ext.message.msg(true, G.i18n.legendset+' <span class="x-msg-hl">' + lsrw + '</span> ' + G.i18n.was_updated);
									G.stores.predefinedMapLegendSet.load();
								}
							});
						}
					}
				],
                listeners: {
                    expand: function() {
                        if (!G.stores.indicator.isLoaded) {
                            G.stores.indicator.load();
                        }
						
						predefinedMapLegendSetWindow.syncSize();
                    },
                    collapse: function() {
                        predefinedMapLegendSetWindow.syncSize();
                    }
                }
            },
            
            {
                title: G.i18n.dataelements,
				bodyStyle: 'padding:8px; background-color:#fff',
				autoHeight: true,
				height: 'auto',
                items: [
                    {
                        xtype: 'form',
                        labelWidth: G.conf.label_width,
                        items: [
                            {html: '<div class="window-info">' + G.i18n. assign_dataelements_to_legend_set + '</div>'},
                            {
                                xtype: 'combo',
                                id: 'predefinedmaplegendsetdataelement_cb',
                                editable: false,
                                valueField: 'id',
                                displayField: 'name',
                                mode: 'remote',
                                forceSelection: true,
                                triggerAction: 'all',
                                selectOnFocus: true,
                                emptyText: G.conf.emptytext,
                                labelSeparator: G.conf.labelseparator,
                                fieldLabel: G.i18n.legendset,
                                width: G.conf.combo_width_fieldset,
                                minListWidth: G.conf.combo_width_fieldset,
                                store: G.stores.predefinedMapLegendSet,
                                listeners:{
                                    'select': {
                                        fn: function(cb, record) {
                                            var dataElements = record.data.dataElements || [];
                                            var dataElementString = '';

                                            for (var i = 0; i < dataElements.length; i++) {
                                                dataElementString += dataElements[i];
                                                if (i < dataElements.length-1) {
                                                    dataElementString += ',';
                                                }
                                            }
                                            
                                            Ext.getCmp('predefinedmaplegendsetdataelement_ms').setValue(dataElementString);
                                        }
                                    }
                                }					
                            },
                            {html: '<div class="window-field-label">' + G.i18n.dataelements + '</div>'},
                            {
                                xtype: 'multiselect',
                                id: 'predefinedmaplegendsetdataelement_ms',
                                hideLabel: true,
                                dataFields: ['id', 'shortName'],
                                valueField: 'id',
                                displayField: 'shortName',
                                width: G.conf.multiselect_width,
                                height: G.util.getMultiSelectHeight(),
                                store: G.stores.dataElement
                            }
                        ]
                    }
				],
				bbar: [
					'->',
					{
						xtype: 'button',
						id: 'assignpredefinedmaplegendsetdataelement_b',
						text: G.i18n.assign,
						iconCls: 'icon-assign',
						handler: function() {
							var ls = Ext.getCmp('predefinedmaplegendsetdataelement_cb').getValue();
							var lsrw = Ext.getCmp('predefinedmaplegendsetdataelement_cb').getRawValue();
							var lims = Ext.getCmp('predefinedmaplegendsetdataelement_ms').getValue();
							
							if (!ls) {
								Ext.message.msg(false, G.i18n.please_select_a_legend_set);
								return;
							}
							
							if (!lims) {
								Ext.message.msg(false, G.i18n.please_select_at_least_one_indicator);
								return;
							}
							
							var array = [];
							array = lims.split(',');
							var params = '?dataElements=' + array[0];
							
							if (array.length > 1) {
								for (var i = 1; i < array.length; i++) {
									array[i] = '&dataElements=' + array[i];
									params += array[i];
								}
							}
							
							Ext.Ajax.request({
								url: G.conf.path_mapping + 'assignDataElementsToMapLegendSet.action' + params,
								method: 'POST',
								params: {id: ls},
								success: function(r) {
									Ext.message.msg(true, G.i18n.legendset+' <span class="x-msg-hl">' + lsrw + '</span> ' + G.i18n.was_updated);
									G.stores.predefinedMapLegendSet.load();
								}
							});
						}
					}
				],
                listeners: {
                    expand: function() {
                        if (!G.stores.dataElement.isLoaded) {
                            G.stores.dataElement.load();
                        }
						
						predefinedMapLegendSetWindow.syncSize();
                    },
                    collapse: function() {
                        predefinedMapLegendSetWindow.syncSize();
                    }
                }
            }
        ]
    });
    predefinedMapLegendSetWindow.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
			
    /* Section: help */
	function setHelpText(topic, tab) {
		Ext.Ajax.request({
			url: '../../dhis-web-commons-about/getHelpContent.action',
			params: {id: topic},
			success: function(r) {
				tab.body.update('<div id="help">' + r.responseText + '</div>');
			}
		});
	}
    
	var helpWindow = new Ext.Window({
        id: 'help_w',
        title: '<span id="window-help-title">' + G.i18n.help + '</span>',
        bodyStyle: 'padding:5px; background-color:#fff',
		layout: 'fit',
        closeAction: 'hide',
        height: 300,
		width: 579,
        items: [
            {
                xtype: 'tabpanel',
                activeTab: 0,
				layoutOnTabChange: true,
                deferredRender: false,
                plain: true,
                defaults: {layout: 'fit'},
                listeners: {
                    tabchange: function(panel, tab) {
                        if (tab.id == 'help0') {
							setHelpText(G.conf.thematicMap, tab);
                        }
                        else if (tab.id == 'help1') {
							setHelpText(G.conf.favorites, tab);
                        }
                        else if (tab.id == 'help2') {
                            setHelpText(G.conf.legendSets, tab);
                        }
						if (tab.id == 'help3') { 
                            setHelpText(G.conf.imageExport, tab);
                        }
                        else if (tab.id == 'help4') {
                            setHelpText(G.conf.administration, tab);
                        }
                        else if (tab.id == 'help5') {
                            setHelpText(G.conf.overlayRegistration, tab);
                        }
                        else if (tab.id == 'help6') {
                            setHelpText(G.conf.setup, tab);
                        }
                    }
                },
                items: [
                    {
                        id: 'help0',
                        title: '<span class="panel-tab-title">' + G.i18n.thematic_map + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help1',
                        title: '<span class="panel-tab-title">' + G.i18n.favorites + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help2',
                        title: '<span class="panel-tab-title">' + G.i18n.legendset + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help3',
                        title: '<span class="panel-tab-title">' + G.i18n.image_export + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help4',
                        title: '<span class="panel-tab-title">' + G.i18n.administrator + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help5',
                        title: '<span class="panel-tab-title">' + G.i18n.overlays_ + '</span>',
                        autoScroll: true
                    },
                    {
                        id: 'help6',
                        title: '<span class="panel-tab-title">' + G.i18n.setup + '</span>',
                        autoScroll: true
                    }
                ]
            }
        ]
    });

    /* Section: base layers */
	var baseLayersWindow = new Ext.Window({
        id: 'baselayers_w',
        title: '<span id="window-baselayer-title">WMS ' + G.i18n.overlays + '</span>',
		bodyStyle: 'padding:8px; background-color:#fff',
		layout: 'fit',
        closeAction: 'hide',
		width: G.conf.window_width,
        items: [
            {
                xtype: 'form',
                labelWidth: G.conf.label_width,
                items: [
                    {html: '<div class="window-info">' + G.i18n.register_new_wms_overlay + '</div>'},
                    {
                        xtype: 'textfield',
                        id: 'baselayername_tf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.display_name,
                        width: G.conf.combo_width_fieldset,
                        autoCreate: {tag: 'input', type: 'text', size: '20', autocomplete: 'off', maxlength: '50'}
                    },
                    {
                        xtype: 'textfield',
                        id: 'baselayerurl_tf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.url,
                        width: G.conf.combo_width_fieldset
                    },
                    {
                        xtype: 'textfield',
                        id: 'baselayerlayer_tf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.layer,
                        width: G.conf.combo_width_fieldset
                    },
                    {
                        xtype: 'textfield',
                        id: 'baselayertime_tf',
                        emptyText: 'Optional',
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: 'Time',
                        width: G.conf.combo_width_fieldset
                    },
                    {html: '<div class="window-p"></div>'},
                    {html: '<div class="window-info">' + G.i18n.delete_ + ' WMS ' + G.i18n.overlay + '</div>'},
                    {
                        xtype: 'combo',
                        id: 'baselayer_cb',
                        editable: false,
                        valueField: 'id',
                        displayField: 'name',
                        mode: 'remote',
                        forceSelection: true,
                        triggerAction: 'all',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.overlay_,
                        width: G.conf.combo_width_fieldset,                
                        store: G.stores.baseLayer
                    }
                ]
            }
        ],
        bbar: [
            '->',
            {
                xtype: 'button',
                id: 'newbaselayer_b',
                text: 'Register',
                iconCls: 'icon-add',
                handler: function() {
                    var bln = Ext.getCmp('baselayername_tf').getRawValue();
                    var blu = Ext.getCmp('baselayerurl_tf').getRawValue();
                    var bll = Ext.getCmp('baselayerlayer_tf').getRawValue();
                    var blt = Ext.getCmp('baselayertime_tf').getRawValue();
                    
                    if (!bln || !blu || !bll) {
                        Ext.message.msg(false, G.i18n.form_is_not_complete);
                        return;
                    }
                    
                    var params = {};
                    params.name = bln;
                    params.type = G.conf.map_layer_type_baselayer;
                    params.url = blu;
                    params.layers = bll;
                    if (blt) {
                        params.time = blt;
                    }
                    
                    Ext.Ajax.request({
                        url: G.conf.path_mapping + 'addOrUpdateMapLayer' + G.conf.type,
                        method: 'POST',
                        params: params,
                        success: function(r) {
                            Ext.message.msg(true, 'WMS ' + G.i18n.overlay + ' <span class="x-msg-hl">' + bln + '</span> ' + G.i18n.registered);
                            G.stores.baseLayer.load();
                            
                            if (G.vars.map.getLayersByName(bln).length) {
                                G.vars.map.getLayersByName(bln)[0].destroy();
                            }
                            
                            var baselayer = G.util.createWMSLayer(bln, blu, bll, blt);  
                            baselayer.layerType = G.conf.map_layer_type_baselayer;
                            baselayer.overlayType = G.conf.map_overlay_type_wms;
                            baselayer.setVisibility(false);                            
                            G.vars.map.addLayer(baselayer);
                            
                            Ext.getCmp('baselayername_tf').reset();
                        }
                    });
                }
            },
            {
                xtype: 'tbseparator',
                cls: 'xtb-sep-panel'
            },
            {
                xtype: 'button',
                id: 'deletebaselayer_b',
                text: G.i18n.delete_,
                iconCls: 'icon-remove',
                handler: function() {
                    var bl = Ext.getCmp('baselayer_cb').getValue();
                    var bln = Ext.getCmp('baselayer_cb').getRawValue();
                    
                    if (!bl) {
                        Ext.message.msg(false, G.i18n.please_select_a_baselayer);
                        return;
                    }
                    
                    Ext.Ajax.request({
                        url: G.conf.path_mapping + 'deleteMapLayer' + G.conf.type,
                        method: 'POST',
                        params: {id: bl},
                        success: function(r) {
                            Ext.message.msg(true, 'WMS ' + G.i18n.overlay + ' <span class="x-msg-hl">' + bln + '</span> '+ G.i18n.deleted);
                            G.stores.baseLayer.load();
                            Ext.getCmp('baselayer_cb').clearValue();
                        }
                    });
                    
                    G.vars.map.getLayersByName(bln)[0].destroy();
                }
            }
        ]
    });

    /* Section: overlays */
	var overlaysWindow = new Ext.Window({
        id: 'overlays_w',
        title: '<span id="window-maplayer-title">Vector ' + G.i18n.overlays + '</span>',
		bodyStyle: 'padding:8px; background-color:#fff',
		layout: 'fit',
        closeAction: 'hide',
		width: G.conf.window_width,
        items: [
            {
                xtype: 'form',
                labelWidth: G.conf.label_width,
                items: [
                    {html: '<div class="window-info">' + G.i18n.register_new_vector_overlay + '</div>'},
                    {
                        xtype: 'textfield',
                        id: 'maplayername_tf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.display_name,
                        width: G.conf.combo_width_fieldset,
                        autoCreate: {tag: 'input', type: 'text', size: '20', autocomplete: 'off', maxlength: '35'}
                    },
                    {
                        xtype: 'combo',
                        id:'maplayermapsourcefile_cb',
                        editable: false,
                        displayField: 'name',
                        valueField: 'name',
                        triggerAction: 'all',
                        mode: 'remote',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.geojson_file,
                        width: G.conf.combo_width_fieldset,
                        store:G.stores.geojsonFiles
                    },
                    {
                        xtype: 'colorfield',
                        id: 'maplayerfillcolor_cf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.fill_color,
                        allowBlank: false,
                        width: G.conf.combo_width_fieldset,
                        value:"#C0C0C0"
                    },
                    {
                        xtype: 'combo',
                        id: 'maplayerfillopacity_cb',
                        editable: true,
                        valueField: 'value',
                        displayField: 'value',
                        mode: 'local',
                        triggerAction: 'all',
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.fill_opacity,
                        width: G.conf.combo_number_width_small,
                        minListWidth: G.conf.combo_number_width_small,
                        value: 0.5,
                        store: {
                            xtype: 'arraystore',
                            fields: ['value'],
                            data: [['0'],['0.1'],['0.2'],['0.3'],['0.4'],['0.5'],['0.6'],['0.7'],['0.8'],['0.9'],['1.0']]
                        }
                    },
                    {
                        xtype: 'colorfield',
                        id: 'maplayerstrokecolor_cf',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.stroke_color,
                        allowBlank: false,
                        width: G.conf.combo_width_fieldset,
                        value:"#000000"
                    },
                    {
                        xtype: 'combo',
                        id: 'maplayerstrokewidth_cb',
                        editable: true,
                        valueField: 'value',
                        displayField: 'value',
                        mode: 'local',
                        triggerAction: 'all',
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.stroke_width,
                        width: G.conf.combo_number_width_small,
                        minListWidth: G.conf.combo_number_width_small,
                        value: 2,
                        store: {
                            xtype: 'arraystore',
                            fields: ['value'],
                            data: [[0],[1],[2],[3],[4],[5]]
                        }
                    },
                    {html: '<div class="window-p"></div>'},
                    {html: '<div class="window-info">' + G.i18n.delete_ + ' vector ' + G.i18n.overlay + '</div>'},
                    {
                        xtype: 'combo',
                        id: 'maplayer_cb',
                        editable: false,
                        valueField: 'id',
                        displayField: 'name',
                        mode: 'remote',
                        forceSelection: true,
                        triggerAction: 'all',
                        emptyText: G.conf.emptytext,
                        labelSeparator: G.conf.labelseparator,
                        fieldLabel: G.i18n.overlay_,
                        width: G.conf.combo_width_fieldset,                
                        store: G.stores.overlay
                    }
                ]
            }
        ],
        bbar: [
            '->',
            {
                xtype: 'button',
                id: 'newmaplayer_b',
                text: 'Register',
                iconCls: 'icon-add',
                handler: function() {
                    var mln = Ext.getCmp('maplayername_tf').getRawValue();
                    var mlfc = Ext.getCmp('maplayerfillcolor_cf').getValue();
                    var mlfo = Ext.getCmp('maplayerfillopacity_cb').getRawValue();
                    var mlsc = Ext.getCmp('maplayerstrokecolor_cf').getValue();
                    var mlsw = Ext.getCmp('maplayerstrokewidth_cb').getRawValue();
                    var mlmsf = Ext.getCmp('maplayermapsourcefile_cb').getValue();
                    
                    if (!mln || !mlmsf) {
                        Ext.message.msg(false, G.i18n.form_is_not_complete);
                        return;
                    }
                    
                    Ext.Ajax.request({
                        url: G.conf.path_mapping + 'addOrUpdateMapLayer' + G.conf.type,
                        method: 'POST',
                        params: {name: mln, type: 'overlay', url: mlmsf, fillColor: mlfc, fillOpacity: 1, strokeColor: mlsc, strokeWidth: mlsw},
                        success: function(r) {
                            Ext.message.msg(true, 'Vector ' + G.i18n.overlay + ' <span class="x-msg-hl">' + mln + '</span> ' + G.i18n.registered);
                            G.stores.overlay.load();
                            
                            if (G.vars.map.getLayersByName(mln).length) {
                                G.vars.map.getLayersByName(mln)[0].destroy();
                            }
                            
                            var overlay = G.util.createOverlay(mln, mlfc, 1, mlsc, mlsw,
                                G.conf.path_mapping + 'getGeoJsonFromFile.action?name=' + mlmsf);
                                
                            overlay.events.register('loadstart', null, G.func.loadStart);
                            overlay.events.register('loadend', null, G.func.loadEnd);
                            overlay.setOpacity(mlfo);
                            overlay.layerType = G.conf.map_layer_type_overlay;
                            
                            G.vars.map.addLayer(overlay);
                            G.vars.map.getLayersByName(mln)[0].setZIndex(G.conf.defaultLayerZIndex);
                            
                            Ext.getCmp('maplayername_tf').reset();
                            Ext.getCmp('maplayermapsourcefile_cb').clearValue();
                        }
                    });
                }
            },
            {
                xtype: 'tbseparator',
                cls: 'xtb-sep-panel'
            },
            {
                xtype: 'button',
                id: 'deletemaplayer_b',
                text: G.i18n.delete_,
                iconCls: 'icon-remove',
                handler: function() {
                    var ml = Ext.getCmp('maplayer_cb').getValue();
                    var mln = Ext.getCmp('maplayer_cb').getRawValue();
                    
                    if (!ml) {
                        Ext.message.msg(false, G.i18n.please_select_an_overlay);
                        return;
                    }
                    
                    Ext.Ajax.request({
                        url: G.conf.path_mapping + 'deleteMapLayer' + G.conf.type,
                        method: 'POST',
                        params: {id: ml},
                        success: function(r) {
                            Ext.message.msg(true, 'Vector ' + G.i18n.overlay + ' <span class="x-msg-hl">' + mln + '</span> '+ G.i18n.deleted);
                            G.stores.overlay.load();
                            Ext.getCmp('maplayer_cb').clearValue();
                        }
                    });
                    
                    G.vars.map.getLayersByName(mln)[0].destroy();
                    
                    G.util.setZIndexByLayerType(G.conf.map_layer_type_overlay, G.conf.defaultLayerZIndex);
                }
            }
        ]
    });

    var layerTree = new Ext.tree.TreePanel({
        id: 'layertree_tp',
        title: '<span class="panel-title">' + G.i18n.map_layers + '</span>',
        bodyStyle: 'padding-bottom:5px',
        enableDD: false,
        rootVisible: false,
        collapsible: true,
        root: {
            nodeType: 'async',
            children: [
                {
                    nodeType: 'gx_baselayercontainer',
                    expanded: true,
                    text: G.i18n.baselayers
                },
                {
                    nodeType: 'gx_overlaylayercontainer',
                    text: G.i18n.overlays_
                },
                {
                    nodeType: 'gx_layer',
                    layer: G.conf.boundary_layer,
                    iconCls: 'treepanel-node-icon-boundary'
                },
                {
                    nodeType: 'gx_layer',
                    layer: G.conf.thematic_layer_1,
                    iconCls: 'treepanel-node-icon-thematic1'
                },
                {
                    nodeType: 'gx_layer',
                    layer: G.conf.thematic_layer_2,
                    iconCls: 'treepanel-node-icon-thematic2'
                },
                {
                    nodeType: 'gx_layer',
                    layer: G.conf.symbol_layer,
                    iconCls: 'treepanel-node-icon-symbol'
                },
                {
                    nodeType: 'gx_layer',
                    layer: G.conf.centroid_layer,
                    iconCls: 'treepanel-node-icon-centroid'
                }
            ]
        },
        contextMenuBaselayer: new Ext.menu.Menu({
            items: [
                {
                    text: 'Opacity',
                    iconCls: 'menu-layeroptions-opacity',
                    menu: { 
                        items: G.conf.opacityItems,
                        listeners: {
                            'itemclick': function(item) {
                                item.parentMenu.parentMenu.contextNode.layer.setOpacity(item.text);
                            }
                        }
                    }
                }
            ]
        }),
        contextMenuOverlayWMS: new Ext.menu.Menu({
            items: [
                {
                    text: 'Show legend',
                    iconCls: 'menu-layeroptions-legend',
                    handler: function(item) {
                        var layer = item.parentMenu.contextNode.layer;
                        var url = G.util.convertWMSUrlToLegendString(layer.baseUrl);
                        var window = new Ext.Window({
                            title: '<span id="window-baselayer-title">' + item.parentMenu.contextNode.text + ' legend</span>',
                            layout: 'fit',
                            bodyStyle: 'padding:10px 8px 0 0; background:#fff',
                            items: [
                                { html: '<img src="' + url + '" />' }
                            ]
                        });
                        window.setPagePosition(Ext.getCmp('east').x - 481, Ext.getCmp('center').y + 25);
                        window.show();
                    }
                },
                {
                    text: 'Change period',
                    iconCls: 'menu-layeroptions-period',
                    handler: function(item) {
                        var layer = item.parentMenu.contextNode.layer;
                        
                        var textfield = new Ext.form.TextField({
                            width: G.conf.combo_width
                        });
                        
                        var button = {
                            text: 'Update',
                            iconCls: 'icon-assign',
                            handler: function() {
                                var params = {};
                                if (textfield.getValue()) {
                                    params.time = textfield.getValue();
                                }
                                layer.mergeNewParams(params);
                                window.destroy();
                            }
                        };
                        
                        var window = new Ext.Window({
                            title: '<span id="window-period-title">Change period</span>',
                            layout: 'fit',
                            bodyStyle: 'padding:8px; background:#fff',
                            width: G.conf.combo_width + 26,
                            items: [ textfield ],
                            bbar: [
                                '->',
                                button
                            ]
                        });
                        
                        window.setPagePosition(Ext.getCmp('east').x - (window.width + 15), Ext.getCmp('center').y + 41);
                        window.show();
                    }
                },
                {
                    text: 'Opacity',
                    iconCls: 'menu-layeroptions-opacity',
                    menu: { 
                        items: G.conf.opacityItems,
                        listeners: {
                            'itemclick': function(item) {
                                item.parentMenu.parentMenu.contextNode.layer.setOpacity(item.text);
                            }
                        }
                    }
                }
            ]
        }),
        contextMenuOverlayFile: new Ext.menu.Menu({
            items: [
                {
                    text: 'Labels',
                    iconCls: 'menu-layeroptions-labels',
                    handler: function(item) {
                        var layer = item.parentMenu.contextNode.layer;
                        G.util.labels.fileOverlay.toggleFeatureLabels(layer);
                    }
                },
                {
                    text: 'Opacity',
                    iconCls: 'menu-layeroptions-opacity',
                    menu: { 
                        items: G.conf.opacityItems,
                        listeners: {
                            'itemclick': function(item) {
                                item.parentMenu.parentMenu.contextNode.layer.setOpacity(item.text);
                            }
                        }
                    }
                }
            ]
        }),
        clickEventFn: function(node, e) {
            if (node.attributes.text !== G.i18n.baselayers && node.attributes.text !== G.i18n.overlays_ ) {
                node.select();
                
                if (node.parentNode.attributes.text === G.i18n.baselayers ) {
                    var cmb = node.getOwnerTree().contextMenuBaselayer;
                    cmb.contextNode = node;
                    cmb.showAt(e.getXY());
                }
                
                else if (node.parentNode.attributes.text === G.i18n.overlays_ ) {
                    var cmo = node.layer.overlayType === 'wms' ?
                        node.getOwnerTree().contextMenuOverlayWMS : node.getOwnerTree().contextMenuOverlayFile;
                    cmo.contextNode = node;
                    cmo.showAt(e.getXY());
                }
            }
        },
		listeners: {
            'contextmenu': function(node, e) {
                node.getOwnerTree().clickEventFn(node, e);
            },
            'click': function(node, e) {
                node.getOwnerTree().clickEventFn(node, e);
            }
		},
        bbar: [
            {
                xtype: 'button',
                id: 'baselayers_b',
                text: G.i18n.wms_overlays,
                iconCls: 'icon-baselayer',
                handler: function() {
                    Ext.getCmp('baselayers_w').setPagePosition(Ext.getCmp('east').x - (Ext.getCmp('overlays_w').width + 15), Ext.getCmp('center').y + 41);
                    Ext.getCmp('baselayers_w').show(this.id);
                }
            },
            {
                xtype: 'button',
                id: 'overlays_b',
                text: G.i18n.file_overlays,
                iconCls: 'icon-overlay',
                handler: function() {
                    Ext.getCmp('overlays_w').setPagePosition(Ext.getCmp('east').x - (Ext.getCmp('overlays_w').width + 15), Ext.getCmp('center').y + 41);
                    Ext.getCmp('overlays_w').show(this.id);
                }
            }
        ]
	});
	
    /* Section: widgets */
    boundary = new mapfish.widgets.geostat.Boundary({
        map: G.vars.map,
        layer: boundaryLayer,
        featureSelection: false,
        defaults: {width: 130},
        listeners: {
            'expand': function() {
                //G.vars.activePanel.setPolygon();
            },
            'afterrender': function() {
                this.layer.widget = this;
            }
        }
    });
    
    boundary.window = new Ext.Window({
        title: '<span id="window-boundary-title">' + G.i18n.boundary_layer + '</span>',
        layout: 'fit',
        bodyStyle: 'padding:8px 8px 11px 8px; background-color:#fff',
        closeAction: 'hide',
        width: 287,
        collapsed: false,
        isUpdate: false,
        cmp: {},
        items: boundary,
        bbar: [
            '->',
            {
                xtype: 'button',
                text: G.i18n.update,
                iconCls: 'icon-assign',
                disabled: true,
                scope: boundary,
                handler: function() {
                    var node = this.cmp.parent.selectedNode;
                    this.organisationUnitSelection.setValues(node.attributes.id, node.attributes.text, node.attributes.level,
                        this.cmp.level.getValue(), this.cmp.level.getRawValue());
                    this.window.isUpdate = true;                    
                    this.loadGeoJson();
                },
                listeners: {
                    'render': {
                        fn: function(b) {
                            b.scope.window.cmp.apply = b;
                        }
                    }
                }
            },
            ' ',
            {
                xtype: 'button',
                text: G.i18n.close,
                iconCls: 'icon-cancel',
                scope: boundary,
                handler: function() {
                    this.window.hide();
                }
            }
        ],
        listeners: {
            'show': {
                scope: boundary,
                fn: function() {
                    this.cmp.parent.isLoaded = true;
                    this.window.isShown = true;
                }
            }
        }
    });    
    boundary.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
    choropleth = new mapfish.widgets.geostat.Choropleth({
        map: G.vars.map,
        layer: polygonLayer,
        featureSelection: false,
        legendDiv: 'polygonlegend',
        defaults: {width: 130},
        listeners: {
            'expand': function() {
                G.vars.activePanel.setPolygon();
            },
            'afterrender': function() {
                this.layer.widget = this;
            }
        }
    });
    
    choropleth.window = new Ext.Window({
        title: '<span id="window-thematic1-title">' + G.conf.thematic_layer_1 + '</span>',
        layout: 'fit',
        bodyStyle: 'padding:8px; background-color:#fff',
        closeAction: 'hide',
        width: 570,
        collapsed: false,
        isUpdate: false,
        cmp: {},
        items: choropleth,
        bbar: [
            {
                xtype: 'button',
                text: G.i18n.resize,
                iconCls: 'icon-resize',
                scope: choropleth,
                handler: function() {
                    if (this.window.collapsed) {
                        this.window.setWidth(G.conf.window_editlayer_width);
                        this.window.collapsed = false;
                        this.window.syncSize();
                    }
                    else {
                        this.window.setWidth(G.conf.window_editlayer_width_collapsed);
                        this.window.collapsed = true;
                    }
                }
            },
            '->',
            {
                xtype: 'button',
                text: G.i18n.update,
                iconCls: 'icon-assign',
                disabled: true,
                scope: choropleth,
                handler: function() {
                    var node = this.cmp.parent.selectedNode;
                    this.organisationUnitSelection.setValues(node.attributes.id, node.attributes.text, node.attributes.level,
                        this.cmp.level.getValue(), this.cmp.level.getRawValue());
                    this.window.isUpdate = true;                    
                    this.loadGeoJson();
                },
                listeners: {
                    'render': {
                        fn: function(b) {
                            b.scope.window.cmp.apply = b;
                        }
                    }
                }
            },
            ' ',
            {
                xtype: 'button',
                text: G.i18n.close,
                iconCls: 'icon-cancel',
                scope: choropleth,
                handler: function() {
                    this.window.hide();
                }
            }
        ],
        listeners: {
            'show': {
                scope: choropleth,
                fn: function() {
                    this.cmp.parent.isLoaded = true;
                    this.window.isShown = true;
                }
            }
        }
    });    
    choropleth.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
    point = new mapfish.widgets.geostat.Point({
        map: G.vars.map,
        layer: pointLayer,
        featureSelection: false,
        legendDiv: 'pointlegend',
        defaults: {width: 130},
        listeners: {
            'expand': function() {
                G.vars.activePanel.setPoint();
            },
            'afterrender': function() {
                this.layer.widget = this;
            }
        }
    });
    
    point.window = new Ext.Window({
        title: '<span id="window-thematic2-title">' + G.conf.thematic_layer_2 + '</span>',
        layout: 'fit',
        bodyStyle: 'padding:8px; background-color:#fff',
        closeAction: 'hide',
        width: 570,
        collapsed: false,
        isUpdate: false,
        cmp: {},
        items: point,
        bbar: [
            {
                xtype: 'button',
                text: G.i18n.resize,
                iconCls: 'icon-resize',
                scope: point,
                handler: function() {
                    if (this.window.collapsed) {
                        this.window.setWidth(G.conf.window_editlayer_width);
                        this.window.collapsed = false;
                        this.window.syncSize();
                    }
                    else {
                        this.window.setWidth(G.conf.window_editlayer_width_collapsed);
                        this.window.collapsed = true;
                    }
                }
            },
            '->',
            {
                xtype: 'button',
                text: G.i18n.update,
                iconCls: 'icon-assign',
                disabled: true,
                scope: point,
                handler: function() {
                    var node = this.cmp.parent.selectedNode;
                    this.organisationUnitSelection.setValues(node.attributes.id, node.attributes.text, node.attributes.level,
                        this.cmp.level.getValue(), this.cmp.level.getRawValue());
                    this.window.isUpdate = true;
                    this.loadGeoJson();
                },
                listeners: {
                    'render': {
                        fn: function(b) {
                            b.scope.window.cmp.apply = b;
                        }
                    }
                }
            },
            ' ',
            {
                xtype: 'button',
                text: G.i18n.close,
                iconCls: 'icon-cancel',
                scope: point,
                handler: function() {
                    this.window.hide();
                }
            }
        ],
        listeners: {
            'show': {
                scope: point,
                fn: function() {
                    this.cmp.parent.isLoaded = true;
                    this.window.isShown = true;
                }
            }
        }
    });    
    point.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
    symbol = new mapfish.widgets.geostat.Symbol({
        map: G.vars.map,
        layer: symbolLayer,
        featureSelection: false,
        legendDiv: 'symbollegend',
        defaults: {width: 130},
        listeners: {
            'expand': function() {
                G.vars.activePanel.setSymbol();
            },
            'afterrender': function() {
                this.layer.widget = this;
            }
        }
    });
    
    symbol.window = new Ext.Window({
        title: '<span id="window-symbol-title">' + G.conf.symbol_layer + '</span>',
        layout: 'fit',
        bodyStyle: 'padding:8px; background-color:#fff',
        closeAction: 'hide',
        width: 570,
        isUpdate: false,
        cmp: {},
        items: symbol,
        bbar: [
            {
                xtype: 'button',
                text: G.i18n.resize,
                iconCls: 'icon-resize',
                scope: symbol,
                handler: function() {
                    if (this.window.collapsed) {
                        this.window.setWidth(G.conf.window_editlayer_width);
                        this.window.collapsed = false;
                        this.window.syncSize();
                    }
                    else {
                        this.window.setWidth(G.conf.window_editlayer_width_collapsed);
                        this.window.collapsed = true;
                    }
                }
            },
            '->',
            {
                xtype: 'button',
                text: G.i18n.update,
                iconCls: 'icon-assign',
                disabled: true,
                scope: symbol,
                handler: function() {
                    var node = this.cmp.parent.selectedNode;
                    this.organisationUnitSelection.setValues(node.attributes.id, node.attributes.text, node.attributes.level,
                        this.cmp.level.getValue(), this.cmp.level.getRawValue());
                    this.window.isUpdate = true;
                    this.loadGeoJson();
                },
                listeners: {
                    'render': {
                        fn: function(b) {
                            b.scope.window.cmp.apply = b;
                        }
                    }
                }
            },
            ' ',
            {
                xtype: 'button',
                text: G.i18n.close,
                iconCls: 'icon-cancel',
                scope: symbol,
                handler: function() {
                    this.window.hide();
                }
            }
        ]
    });    
    symbol.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
    centroid = new mapfish.widgets.geostat.Centroid({
        map: G.vars.map,
        layer: centroidLayer,
        featureSelection: false,
        legendDiv: 'centroidlegend',
        defaults: {width: 130},
        listeners: {
            'expand': function() {
                G.vars.activePanel.setCentroid();
            },
            'afterrender': function() {
                this.layer.widget = this;
            }
        }
    });
    
    centroid.window = new Ext.Window({
        title: '<span id="window-centroid-title">' + G.conf.centroid_layer + '</span>',
        layout: 'fit',
        bodyStyle: 'padding:8px 8px 11px 8px; background-color:#fff',
        closeAction: 'hide',
        width: 570,
        isUpdate: false,
        cmp: {},
        items: centroid,
        bbar: [
            {
                xtype: 'button',
                text: G.i18n.resize,
                iconCls: 'icon-resize',
                scope: centroid,
                handler: function() {
                    if (this.window.collapsed) {
                        this.window.setWidth(G.conf.window_editlayer_width);
                        this.window.collapsed = false;
                        this.window.syncSize();
                    }
                    else {
                        this.window.setWidth(G.conf.window_editlayer_width_collapsed);
                        this.window.collapsed = true;
                    }
                }
            },
            '->',
            {
                xtype: 'button',
                text: G.i18n.update,
                iconCls: 'icon-assign',
                disabled: true,
                scope: centroid,
                handler: function() {
                    var node = this.cmp.parent.selectedNode;
                    this.organisationUnitSelection.setValues(node.attributes.id, node.attributes.text, node.attributes.level,
                        this.cmp.level.getValue(), this.cmp.level.getRawValue());
                    this.window.isUpdate = true;
                    
                    this.loadGeoJson();
                },
                listeners: {
                    'render': {
                        fn: function(b) {       
                            b.scope.window.cmp.apply = b;
                        }
                    }
                }
            },
            ' ',
            {
                xtype: 'button',
                text: G.i18n.close,
                iconCls: 'icon-cancel',
                scope: centroid,
                handler: function() {
                    this.window.hide();
                }
            }
        ]
    });    
    centroid.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
    
	/* Section: map toolbar */
	var mapLabel = new Ext.form.Label({
		text: G.i18n.map,
		style: 'font:bold 11px arial; color:#333;'
	});
	var layersLabel = new Ext.form.Label({
		text: G.i18n.layers,
		style: 'font:bold 11px arial; color:#333;'
	});
	var toolsLabel = new Ext.form.Label({
		text: G.i18n.tools,
		style: 'font:bold 11px arial; color:#333;'
	});
	
	var zoomInButton = new Ext.Button({
		iconCls: 'icon-zoomin',
		tooltip: G.i18n.zoom_in,
        style: 'margin-top:1px',
		handler: function() {
			G.vars.map.zoomIn();
		}
	});
	
	var zoomOutButton = new Ext.Button({
		iconCls: 'icon-zoomout',
		tooltip: G.i18n.zoom_out,
        style: 'margin-top:1px',
		handler: function() {
			G.vars.map.zoomOut();
		}
	});
	
	var zoomToVisibleExtentButton = new Ext.Button({
		iconCls: 'icon-zoommin',
		tooltip: G.i18n.zoom_to_visible_extent,
        style: 'margin-top:1px',
		handler: function() {
            G.util.zoomToVisibleExtent();
        }
	});
	
	var boundaryButton = new G.cls.vectorLayerButton('icon-boundary', G.i18n.boundary_layer, boundary);
    
    var choroplethButton = new G.cls.vectorLayerButton('icon-thematic1', G.i18n.thematic_layer + ' 1', choropleth);
    
    var pointButton = new G.cls.vectorLayerButton('icon-thematic2', G.i18n.thematic_layer + ' 2', point);
    
    var symbolButton = new G.cls.vectorLayerButton('icon-symbol', G.conf.symbol_layer, symbol);
    
    var centroidButton = new G.cls.vectorLayerButton('icon-centroid', G.conf.centroid_layer, centroid);
	
	var favoriteButton = new Ext.Button({
		iconCls: 'icon-favorite',
		tooltip: G.i18n.favorite_map_views,
        style: 'margin-top:1px',
        addMenuItems: function(store) {
            this.menu.addItem('-');
            
            for (var i = 0; i < store.data.items.length; i++) {
                var item = new Ext.menu.Item({
                    text: store.data.items[i].data.name,
                    iconCls: 'menu-mapview',
                    mapViewId: store.data.items[i].data.id,
                    scope: choropleth,
                    handler: function(i) {
                        G.util.mapView.layer(i.mapViewId);
                    }
                });
                this.menu.addItem(item);
            }            
        },
        reloadMenu: function() {
            if (this.menu) {
                this.menu.destroy();
            }            
            this.menu = new Ext.menu.Menu({
                items: [
                    {
                        text:  G.i18n.manage_favorites,
                        iconCls: 'menu-layeroptions-edit',
                        scope: this,
                        handler: function() {
                            if (!favoriteWindow.hidden) {
                                favoriteWindow.hide();
                            }
                            else {
                                favoriteWindow.show(this.id);
                            }
                        }
                    }
                ]
            });
            
            var store = G.stores.mapView;            
            if (!store.isLoaded) {
                store.load({scope: this, callback: function() {
                    this.addMenuItems(store);
                }});
            }
            else {
                this.addMenuItems(store);
            }
        },
        listeners: {
            'afterrender': function(b) {
                b.reloadMenu();
            }
        }
	});
	
	var predefinedMapLegendSetButton = new Ext.Button({
		iconCls: 'icon-predefinedlegendset',
		tooltip: G.i18n.predefined_legend_sets,
		disabled: !G.user.isAdmin,
        style: 'margin-top:1px',
		handler: function() {		
			if (!predefinedMapLegendSetWindow.hidden) {
				predefinedMapLegendSetWindow.hide();
			}
			else {
				predefinedMapLegendSetWindow.show(this.id);         
                if (!G.stores.predefinedMapLegend.isLoaded) {
                    G.stores.predefinedMapLegend.load();
                }
			}
		}
	});
	
	var exportImageButton = new Ext.Button({
		iconCls: 'icon-image',
		tooltip: G.i18n.export_map_as_image,
        style: 'margin-top:1px',
		handler: function() {
			if (Ext.isIE) {
				Ext.message.msg(false, 'SVG not supported by browser');
				return;
			}
            
            if (!exportImageWindow.hidden) {
				exportImageWindow.hide();
			}
			else {
				exportImageWindow.show(this.id);
			}
		}
	});
    
    var measureDistanceButton = new Ext.Button({
        iconCls: 'icon-measure',
        tooltip: G.i18n.measure_distance,
        style: 'margin-top:1px',
        handler: function() {
            var control = G.vars.map.getControl('measuredistance');
            
            if (!control.active) {
                if (!control.window) {
                    control.window = new Ext.Window({
                        title: '<span id="window-measure-title">' + G.i18n.measure_distance + '</span>',
                        layout: 'fit',
                        closeAction: 'hide',
                        width: 150,
                        height: 90,
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'anchor',
                                bodyStyle: 'padding:8px',
                                items: [
                                    {html: '<div class="window-info">' + G.i18n.total_distance + '</div>'},
                                    {html: '<div id="measureDistanceDiv"></div>'}
                                ]
                            }
                        ],
                        listeners: {
                            'hide': function() {
                                G.vars.map.getControl('measuredistance').deactivate();
                            }
                        }
                    });
                }
                control.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
                control.window.show(this.id);
                document.getElementById('measureDistanceDiv').innerHTML = '0 km';                
                control.setImmediate(true);
                control.geodesic = true;
                control.activate();
            }
            else {
                control.deactivate();
                control.window.hide();
            }
        }
    });  
	
	var helpButton = new Ext.Button({
		iconCls: 'icon-help',
		tooltip: G.i18n.help,
        style: 'margin-top:1px',
		handler: function() {
            if (!helpWindow.hidden) {
                helpWindow.hide();
            }
            else {
                helpWindow.show(this.id);
            }
		}
	});

	var exitButton = new Ext.Button({
		text: G.i18n.exit_gis,
        iconCls: 'icon-exit',
		tooltip: G.i18n.return_to_DHIS_2_dashboard,
        style: 'margin-top:1px',
		handler: function() {
			window.location.href = '../../dhis-web-portal/redirect.action';
		}
	});
	
	var mapToolbar = new Ext.Toolbar({
		id: 'map_tb',
		items: [
			' ',' ',' ',' ',' ',
			mapLabel,
			' ',' ',
            zoomInButton,
			zoomOutButton,
			zoomToVisibleExtentButton,
            ' ',
			'-',
			' ',' ',' ',
			layersLabel,
			' ',' ',
			boundaryButton,
            choroplethButton,
            pointButton,
            symbolButton,
            centroidButton,
            ' ',
			'-',
			' ',' ',' ',
			toolsLabel,
			' ',' ',
			favoriteButton,
            predefinedMapLegendSetButton,
			exportImageButton,
            measureDistanceButton,
            ' ',
			'-',
			helpButton,
			'->',
			exitButton,' '
		]
	});

	/* Section: viewport */
    var viewport = new Ext.Viewport({
        id: 'viewport',
        layout: 'border',
        margins: '0 0 5 0',
        items:
        [
            new Ext.BoxComponent(
            {
                region: 'north',
                id: 'north',
                el: 'north',
                height: 0
            }),
            {
                region: 'east',
                id: 'east',
                layout: 'anchor',
                collapsible: true,
				header: false,
                margins: '0 0 0 1px',
                defaults: {
                    border: true,
                    frame: true,
                    collapsible: true
                },
                items:
                [
                    layerTree,
                    {
                        title: '<span class="panel-title">'+ G.i18n.cursor_position +'</span>',
                        contentEl: 'mouseposition'
                    },
					{
						title: '<span class="panel-title">' + G.i18n.feature_data + '</span>',
                        contentEl: 'featuredatatext'
					},
                    {
                        title: '<span class="panel-title">' + G.conf.thematic_layer_1 + ' legend</span>',
                        contentEl: 'polygonlegend'
                    },
                    {
                        title: '<span class="panel-title">' + G.conf.thematic_layer_2 + ' legend</span>',
                        contentEl: 'pointlegend'
                    },
                    {
                        title: '<span class="panel-title">' + G.conf.symbol_layer + ' legend</span>',
                        contentEl: 'symbollegend'
                    },
                    {
                        title: '<span class="panel-title">' + G.conf.centroid_layer + ' legend</span>',
                        contentEl: 'centroidlegend'
                    }
                ]
            },
            {
                xtype: 'gx_mappanel',
                region: 'center',
                id: 'center',
                height: 1000,
                width: 800,
                map: G.vars.map,
                zoom: 3,
				tbar: mapToolbar
            }
        ],
        listeners: {
            'afterrender': function() {
                G.util.setOpacityByLayerType(G.conf.map_layer_type_overlay, G.conf.defaultLayerOpacity);
                G.util.setOpacityByLayerType(G.conf.map_overlay_type_wms, G.conf.wmsLayerOpacity);                
                G.util.setOpacityByLayerType(G.conf.map_layer_type_thematic, G.conf.defaultLayerOpacity);
                symbolLayer.setOpacity(1);
                centroidLayer.setOpacity(1);
                
				if (G.vars.parameter.base === 'googlestreets' && window.google) {
					G.vars.map.getLayersByName('Google Streets')[0].setVisibility(true);
				}
				else if (G.vars.parameter.base === 'googlehybrid' && window.google) {
					G.vars.map.getLayersByName('Google Hybrid')[0].setVisibility(true);
				}
				else if (G.vars.parameter.base !== 'osm' && window.google) {
					G.vars.map.getLayersByName('Google Streets')[0].setVisibility(false);
				}
				else if (G.vars.parameter.base === 'osm') {
					G.vars.map.getLayersByName('OpenStreetMap')[0].setVisibility(true);
				}
				else {
					G.vars.map.getLayersByName('OpenStreetMap')[0].setVisibility(false);
				}
                
                var svg = document.getElementsByTagName('svg');
                
                if (!Ext.isIE) {
					boundaryLayer.svgId = svg[0].id;
                    polygonLayer.svgId = svg[1].id;
                    pointLayer.svgId = svg[2].id;
                    symbolLayer.svgId = svg[3].id;
                    centroidLayer.svgId = svg[4].id;
                }
                
                for (var i = 0, j = 4; i < G.vars.map.layers.length; i++) {
                    if (G.vars.map.layers[i].layerType == G.conf.map_layer_type_overlay) {
                        G.vars.map.layers[i].svgId = svg[j++].id;
                    }
                }
                
                choropleth.prepareMapViewValueType();
                choropleth.prepareMapViewPeriod();
                choropleth.prepareMapViewLegend();
                
                point.prepareMapViewValueType();
                point.prepareMapViewPeriod();
                point.prepareMapViewLegend();
                
                centroid.prepareMapViewValueType();
                centroid.prepareMapViewPeriod();                
                
                G.vars.map.events.register('addlayer', null, function(e) {
                    var svg = document.getElementsByTagName('svg');
                    e.layer.svgId = svg[svg.length-1].id;
                });
                
                G.vars.map.events.register('mousemove', null, function(e) {
                    G.vars.mouseMove.x = e.clientX;
                    G.vars.mouseMove.y = e.clientY;
                });
                
                G.vars.map.events.register('click', null, function(e) {
                    if (G.vars.relocate.active) {
                        var mp = document.getElementById('mouseposition');
                        var coordinates = '[' + mp.childNodes[1].data + ',' + mp.childNodes[3].data + ']';
                        var center = Ext.getCmp('center').x;
	
                        Ext.Ajax.request({
                            url: G.conf.path_mapping + 'updateOrganisationUnitCoordinates' + G.conf.type,
                            method: 'POST',
                            params: {id: G.vars.relocate.feature.attributes.id, coordinates: coordinates},
                            success: function(r) {
                                G.vars.relocate.active = false;
                                G.vars.relocate.widget.featureOptions.coordinate.destroy();
                                                                
                                G.vars.relocate.feature.move({x: parseFloat(e.clientX - center), y: parseFloat(e.clientY - 28)});
                                document.getElementById('OpenLayers.Map_3_OpenLayers_ViewPort').style.cursor = 'auto';
                                Ext.message.msg(true, '<span class="x-msg-hl">' + G.vars.relocate.feature.attributes.name + 
                                    ' </span>relocated to ' +
                                    '[<span class="x-msg-hl">' + mp.childNodes[1].data + '</span>,' + 
                                    '<span class="x-msg-hl">' + mp.childNodes[3].data + '</span>]');
                            }
                        });
                    }
                });
                
                document.getElementById('featuredatatext').innerHTML = '<div style="color:#666">' + G.i18n.no_feature_selected + '</div>';
                
				boundaryButton.menu.remove(boundaryButton.menu.items.items[5]);
                boundaryButton.menu.remove(boundaryButton.menu.items.last());
                boundaryButton.menu.remove(boundaryButton.menu.items.last());
                symbolButton.menu.remove(symbolButton.menu.items.last());
                symbolButton.menu.remove(symbolButton.menu.items.last());
                centroidButton.menu.remove(centroidButton.menu.items.last());
                centroidButton.menu.remove(centroidButton.menu.items.last());
                
                var c = Ext.getCmp('center').x;
                var e = Ext.getCmp('east').x;
                
                helpWindow.setPagePosition(c+((e-c)/2)-(helpWindow.width/2), Ext.getCmp('east').y + 100);
                
                if (G.vars.parameter.id) {
                    G.util.mapView.launch.call(choropleth, G.vars.parameter.mapView);
                    G.vars.parameter.id = null;
                }
            }
        }
    });
    
    G.vars.map.addControl(new OpenLayers.Control.ZoomBox());
	
	G.vars.map.addControl(new OpenLayers.Control.MousePosition({
        displayClass: 'void',
        div: $('mouseposition'),
        prefix: '<span style="color:#666">Lon: </span>',
        separator: '<span style="color:#666">&nbsp;&nbsp;Lat: </span>'
    }));
    
    G.vars.map.addControl(new OpenLayers.Control.PanPanel({
        slideFactor: 100
    }));
    
    G.vars.map.addControl(new OpenLayers.Control.Measure( OpenLayers.Handler.Path, {
        id: 'measuredistance',
        persist: true,
        handlerOptions: {
            layerOptions: {styleMap: G.util.measureDistance.getMeasureStyleMap()}
        }
    }));
    
    G.vars.map.getControl('measuredistance').events.on({
        "measurepartial": G.util.measureDistance.handleMeasurements,
        "measure": G.util.measureDistance.handleMeasurements
    });
    
	}});
});
