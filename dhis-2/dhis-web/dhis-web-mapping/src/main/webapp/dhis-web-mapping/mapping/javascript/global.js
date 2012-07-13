G.conf = {

//  Ajax requests

    path_mapping: '../',
    path_commons: '../../dhis-web-commons-ajax-json/',
    path_api: '../../api/',
    type: '.action',
    
//  Layer names

	boundary_layer: G.i18n.boundary_layer,
    thematic_layer_1: G.i18n.thematic_layer  + ' 1',
    thematic_layer_2: G.i18n.thematic_layer  + ' 2',
    symbol_layer: G.i18n.symbol_layer,
    centroid_layer: G.i18n.centroid_layer,
	
//	Help strings
    
    setup: 'gisSetup',
	thematicMap: 'gisThematicMap',
	thematicMap2: 'gisThematicMap2',
    thematicMap3: 'gisThematicMap3',
    thematicMap4: 'gisThematicMap4',
    overlayRegistration: 'gisOverlay',
	administration: 'gisAdministration',
	favorites: 'gisFavoriteMapView',
	legendSets: 'gisLegendSet',
    imageExport: 'gisImageExport',

//  Layout

    west_width: 270,
    multiselect_width: 219,
    label_width: 85,
	combo_width: 150,
	combo_width_fieldset: 127,
	combo_list_width_fieldset: 127 + 17,
	combo_number_width: 65,
	combo_number_width_small: 44,
    window_width: 251,
    window_x_right: 55,
    window_y_right: 41,
    window_x_left: 70,
    window_y_left: 45,
    window_editlayer_width: 570,
    window_editlayer_width_collapsed: 292,
    
//  GUI

    feature_data_style_name: 'color:#000',
    feature_data_style_value: 'color:#444',
    feature_data_style_empty: 'color:#555',
    
    emptyText: '',
	labelseparator: '',
	
//	DHIS variables

    map_widget_choropleth: 'choropleth',
    map_widget_point: 'point',
    map_widget_symbol: 'symbol',
    map_widget_centroid: 'centroid',    
	map_source_type_database: 'database',
	map_source_type_geojson: 'geojson',
	map_source_type_shapefile: 'shapefile',
	map_legend_symbolizer_color: 'color',
	map_legend_symbolizer_image: 'image',
	map_legendset_type_automatic: 'automatic',
	map_legendset_type_predefined: 'predefined',
    map_layer_type_baselayer: 'baselayer',
    map_layer_type_overlay: 'overlay',
    map_layer_type_thematic: 'thematic',
    map_overlay_type_wms: 'wms',
    map_overlay_type_file: 'file',
	map_value_type_indicator: 'indicator',
	map_value_type_dataelement: 'dataelement',
    map_date_type_fixed: 'fixed',
    map_date_type_start_end: 'start-end',
    map_selection_type_parent: 'parent',
    map_selection_type_level: 'level',
    map_feature_type_multipolygon: 'MultiPolygon',
    map_feature_type_multipolygon_class_name: 'OpenLayers.Geometry.MultiPolygon',
    map_feature_type_polygon: 'Polygon',
    map_feature_type_polygon_class_name: 'OpenLayers.Geometry.Polygon',
    map_feature_type_point: 'Point',
    map_feature_type_point_class_name: 'OpenLayers.Geometry.Point',
    map_view_access_level_user: 'user',
    map_view_access_level_system: 'system',
    aggregation_strategy_real_time: 'real_time',
    aggregation_strategy_batch: 'batch',
    operator_lowerthan: 'lt',
    operator_greaterthan: 'gt',
    
//  MapFish

    classify_with_bounds: 1,
    classify_by_equal_intervals: 2,
    classify_by_quantils: 3,

//  Layers

    opacityItems: [
        {text: '0.1', iconCls: 'menu-layeroptions-opacity-10'},
        {text: '0.2', iconCls: 'menu-layeroptions-opacity-20'},
        {text: '0.3', iconCls: 'menu-layeroptions-opacity-30'},
        {text: '0.4', iconCls: 'menu-layeroptions-opacity-40'},
        {text: '0.5', iconCls: 'menu-layeroptions-opacity-50'},
        {text: '0.6', iconCls: 'menu-layeroptions-opacity-60'},
        {text: '0.7', iconCls: 'menu-layeroptions-opacity-70'},
        {text: '0.8', iconCls: 'menu-layeroptions-opacity-80'},
        {text: '0.9', iconCls: 'menu-layeroptions-opacity-90'},
        {text: '1.0', iconCls: 'menu-layeroptions-opacity-100'}
    ],
    
    defaultLayerOpacity: 0.8,
    
    wmsLayerOpacity: 0.5,
    
    defaultLayerZIndex: 10000,
    
    defaultLowRadius: 5,
    
    defaultHighRadius: 20,
    
//  Measure

    sketchSymbolizers: {
        "Point": {
            pointRadius: 4,
            graphicName: "square",
            fillColor: "white",
            fillOpacity: 1,
            strokeWidth: 1,
            strokeOpacity: 1,
            strokeColor: "#333333"
        },
        "Line": {
            strokeWidth: 2,
            strokeOpacity: 1,
            strokeColor: "#444444",
            strokeDashstyle: "dash"
        },
        "Polygon": {
            strokeWidth: 2,
            strokeOpacity: 1,
            strokeColor: "#666666",
            fillColor: "white",
            fillOpacity: 0.3
        }
    }
};

G.util = {
    
    expandWidget: function(widget) {
        var collapsed = widget == choropleth ? point : choropleth;
        collapsed.collapse();
        widget.expand();
    },
    
	getUrlParam: function(s) {
		var output = '';
		var href = window.location.href;
		if (href.indexOf('?') > -1 ) {
			var query = href.substr(href.indexOf('?') + 1);
			var query = query.split('&');
			for (var i = 0; i < query.length; i++) {
				if (query[i].indexOf('=') > -1) {
					var a = query[i].split('=');
					if (a[0].toLowerCase() === s) {
						output = a[1];
						break;
					}
				}
			}
		}
		return unescape(output);
	},

    getKeys: function(obj) {
        var temp = [];
        for (var k in obj) {
            if (obj.hasOwnProperty(k)) {
                temp.push(k);
            }
        }
        return temp;
    },

    validateInputNameLength: function(name) {
        return (name.length <= 25);
    },
    
    getMultiSelectHeight: function() {
        var h = screen.height;
        return h <= 800 ? 220 :
            h <= 1050 ? 310 :
                h <= 1200 ? 470 : 900;
    },

    getGridPanelHeight: function() {
        var h = screen.height;
        return h <= 800 ? 180 :
            h <= 1050 ? 480 :
                h <= 1200 ? 600 : 900;
    },

    getNumericMapView: function(mapView) {
        mapView.id = parseFloat(mapView.id);
        mapView.indicatorGroupId = parseFloat(mapView.indicatorGroupId);
        mapView.indicatorId = parseFloat(mapView.indicatorId);
        mapView.periodId = parseFloat(mapView.periodId);
        mapView.method = parseFloat(mapView.method);
        mapView.classes = parseFloat(mapView.classes);
        mapView.mapLegendSetId = parseFloat(mapView.mapLegendSetId);
        mapView.longitude = parseFloat(mapView.longitude);
        mapView.latitude = parseFloat(mapView.latitude);
        mapView.zoom = parseFloat(mapView.zoom);
        return mapView;
    },

    getNumberOfDecimals: function(x,dec_sep) {
        var tmp = new String();
        tmp = x;
        return tmp.indexOf(dec_sep) > -1 ? tmp.length-tmp.indexOf(dec_sep) - 1 : 0;
    },

    labels: {
        vector: {
            getActivatedOpenLayersStyleMap: function(widget, fsize, fweight, fstyle, fcolor) {
                return new OpenLayers.StyleMap({
                    'default' : new OpenLayers.Style(
                        OpenLayers.Util.applyDefaults({
                            'fillOpacity': widget == boundary ? 0 : 1,
                            'strokeColor': widget == boundary ? '#000' : '#fff',
                            'strokeWidth': 1,
                            'label': '${labelString}',
                            'fontFamily': 'arial,sans-serif,ubuntu,consolas',
                            'fontSize': fsize ? fsize : 13,
                            'fontWeight': fweight ? 'bold' : 'normal',
                            'fontStyle': fstyle ? 'italic' : 'normal',
                            'fontColor': fcolor ? fcolor : '#000000'
                        },
                        OpenLayers.Feature.Vector.style['default'])
                    ),
                    'select': new OpenLayers.Style({
                        'strokeColor': '#000000',
                        'strokeWidth': 2,
                        'cursor': 'pointer'
                    })
                });
            },
            getDeactivatedOpenLayersStyleMap: function(widget) {
                return new OpenLayers.StyleMap({
                    'default': new OpenLayers.Style(
                        OpenLayers.Util.applyDefaults({
                            'fillOpacity': widget == boundary ? 0 : 1,
                            'strokeColor': widget == boundary ? '#000' : '#fff',
                            'strokeWidth': 1
                        },
                        OpenLayers.Feature.Vector.style['default'])
                    ),
                    'select': new OpenLayers.Style({
                        'strokeColor': '#000000',
                        'strokeWidth': 2,
                        'cursor': 'pointer'
                    })
                });
            },
            toggleFeatureLabels: function(widget, fsize, fweight, fstyle, fcolor) {
                function activateLabels() {
                    widget.layer.styleMap = this.getActivatedOpenLayersStyleMap(widget, fsize, fweight, fstyle, fcolor);
                    widget.labels = true;
                }
                function deactivateLabels(scope) {
                    widget.layer.styleMap = this.getDeactivatedOpenLayersStyleMap(widget);
                    widget.labels = false;
                }
                
                if (widget.labels) {
                    deactivateLabels.call(this);
                }
                else {
                    activateLabels.call(this);
                }
                
                G.vars.lockPosition = true;
                widget.applyValues();
            }
        },
        fileOverlay: {
            getActivatedOpenLayersStyleMap: function(layer) {
                var style = layer.styleMap.styles['default'].defaultStyle;
                return new OpenLayers.StyleMap({
                    'default' : new OpenLayers.Style(
                        OpenLayers.Util.applyDefaults({
                            'fillOpacity': style.fillOpacity,
                            'fillColor': style.fillColor,
                            'strokeWidth': style.strokeWidth,
                            'strokeColor': style.strokeWidth,
                            'label': '${name}',
                            'fontFamily': 'arial,lucida sans unicode',
                            'fontSize': 13,
                            'fontWeight': 'normal',
                            'fontStyle': 'normal',
                            'fontColor': '#000000'
                        },
                        OpenLayers.Feature.Vector.style['default'])
                    )
                });
            },
            getDeactivatedOpenLayersStyleMap: function(layer) {
                var style = layer.styleMap.styles['default'].defaultStyle;
                return new OpenLayers.StyleMap({
                    'default' : new OpenLayers.Style(
                        OpenLayers.Util.applyDefaults({
                            'fillOpacity': style.fillOpacity,
                            'fillColor': style.fillColor,
                            'strokeWidth': style.strokeWidth,
                            'strokeColor': style.strokeWidth
                        },
                        OpenLayers.Feature.Vector.style['default'])
                    )
                });
            },
            toggleFeatureLabels: function(layer) {
                function activateLabels() {
                    layer.styleMap = this.getActivatedOpenLayersStyleMap(layer);
                    layer.labels = true;
                    layer.refresh();
                }
                function deactivateLabels(scope) {
                    layer.styleMap = this.getDeactivatedOpenLayersStyleMap(layer);
                    layer.labels = false;
                    layer.refresh();
                }
                
                if (layer.labels) {
                    deactivateLabels.call(this);
                }
                else {
                    activateLabels.call(this);
                }
            }
        }            
    },
    
    measureDistance: {
        getMeasureStyleMap: function() {
            var style = new OpenLayers.Style();    
            style.addRules([new OpenLayers.Rule({symbolizer: G.conf.sketchSymbolizers})]);    
            return new OpenLayers.StyleMap({"default": style});
        },
            
        handleMeasurements: function(e) {
            if (e.measure) {
                document.getElementById('measureDistanceDiv').innerHTML = e.measure.toFixed(2) + ' ' + e.units;
            }
        }
    },            

    sortByValue: function(a,b) {
        return b.value-a.value;
    },

    getLegendsJSON: function() {   
        var json = '{"legends":[';
        for (var i = 0; i < this.imageLegend.length; i++) {
            json += '{';
            json += '"label": "' + this.imageLegend[i].label + '",';
            json += '"color": "' + this.imageLegend[i].color + '"';
            json += i < this.imageLegend.length-1 ? '},' : '}';
        }
        json += ']}';        
        return json;
    },
    
    setCurrentValue: function(cb, mv) {
        if (cb.getValue() == cb.currentValue) {
            return true;
        }
        else {
            cb.currentValue = cb.getValue();
            mv.clearValue();
            return false;
        }
    },
    
    setLockPosition: function(cb) {
        cb.lockPosition = !cb.lockPosition ? true : cb.lockPosition;
    },
    
    mergeSvg: function(str, ext) {
        if (ext.length) {
            str = str || '<svg>';
            for (var i = 0; i < ext.length; i++) {
                str = str.replace('</svg>');
                ext[i] = ext[i].substring(ext[i].indexOf('>')+1);
                str += ext[i];
            }
        }
        return str;
    },
    
    getOverlaysSvg: function(overlays) {
        if (overlays.length) {
            for (var i = 0; i < overlays.length; i++) {
                overlays[i] = document.getElementById(overlays[i].svgId).parentNode.innerHTML;
            }
        }
        return overlays;
    },

    getTransformedFeatureArray: function(features) {
        var sourceProjection = new OpenLayers.Projection("EPSG:4326");
        var destinationProjection = new OpenLayers.Projection("EPSG:900913");
        for (var i = 0; i < features.length; i++) {
            features[i].geometry.transform(sourceProjection, destinationProjection);
        }
        return features;
    },
 
    getTransformedPointByXY: function(x, y) {
		var p = new OpenLayers.Geometry.Point(parseFloat(x), parseFloat(y));
        return p.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
    },
    
    getTransformedPoint: function(p) {
        return p.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
    },
    
    createWMSLayer: function(name, url, layers, time) {
        var options = {
            layers: layers,
            transparent: true,
            format: 'image/png'
        };
        if (time) {
            options.time = time;
        }
        var layer = new OpenLayers.Layer.WMS(name, url, options, {
            isBaseLayer: false,
            buffer: 0,
            ratio: 1,
            singleTile: true
        });
        layer.baseUrl = url;
        return layer;
    },
    
    convertWMSUrlToLegendString: function(url) {
        var str = url.replace('wms.xml','wmsfigmap');
        return str += '?REQUEST=GetLegendGraphic';
    },
    
    createOverlay: function(name, fillColor, fillOpacity, strokeColor, strokeWidth, url) {
        return new OpenLayers.Layer.Vector(name, {
            'visibility': false,
            'styleMap': new OpenLayers.StyleMap({
                'default': new OpenLayers.Style(
                    OpenLayers.Util.applyDefaults(
                        {'fillColor': fillColor, 'fillOpacity': fillOpacity, 'strokeColor': strokeColor, 'strokeWidth': strokeWidth},
                        OpenLayers.Feature.Vector.style['default']
                    )
                )
            }),
            'strategies': [new OpenLayers.Strategy.Fixed()],
            'protocol': new OpenLayers.Protocol.HTTP({
                'url': url,
                'format': new OpenLayers.Format.GeoJSON()
            })
        });
    },
    
    getVisibleLayers: function(layers) {
        var vLayers = [];
        for (var i = 0; i < layers.length; i++) {
            if (layers[i].visibility) {
                vLayers.push(layers[i]);
            }
        }
        return vLayers;
    },
    
    getVectorLayers: function() {
        var layers = [];
        for (var i = 0; i < G.vars.map.layers.length; i++) {
            if (G.vars.map.layers[i].layerType == G.conf.map_layer_type_thematic ||
            G.vars.map.layers[i].layerType == G.conf.map_layer_type_overlay) {
                layers.push(G.vars.map.layers[i]);
            }
        }
        return layers;
    },
    
    getLayersByType: function(type) {
        var layers = [];
        for (var i = 0; i < G.vars.map.layers.length; i++) {
            if (G.vars.map.layers[i].layerType == type) {
                layers.push(G.vars.map.layers[i]);
            }
        }
        return layers;
    },
    
    zoomToVisibleExtent: function() {
        if (!G.vars.lockPosition) {
            var bounds = [];
            
            var layers = this.getLayersByType(G.conf.map_layer_type_thematic);            
            for (var i = 0; i < layers.length; i++) {
                if (layers[i].getDataExtent() && layers[i].visibility) {
                    bounds.push(layers[i].getDataExtent());
                }
            }
                     
            if (bounds.length === 1) {
                G.vars.map.zoomToExtent(bounds[0]);
            }
            else if (bounds.length > 1) {
                var extended = bounds[0];
                for (var i = 1; i < bounds.length; i++) {
                    extended.extend(bounds[i]);
                }
                G.vars.map.zoomToExtent(extended);
            }
        }
		G.vars.lockPosition = false;
    },
    
    setZIndexByLayerType: function(type, index) {
        for (var i = 0; i < G.vars.map.layers.length; i++) {
            if (G.vars.map.layers[i].layerType == type) {
                G.vars.map.layers[i].setZIndex(index);
            }
        }
    },
    
    setOpacityByLayerType: function(type, opacity) {
        for (var i = 0; i < G.vars.map.layers.length; i++) {
            if (G.vars.map.layers[i].layerType == type) {
                G.vars.map.layers[i].setOpacity(opacity);
            }
            else if (G.vars.map.layers[i].overlayType == type) {
                G.vars.map.layers[i].setOpacity(opacity);
            }                
        }
    },
    
    findArrayValue: function(array, value) {
        for (var i = 0; i < array.length; i++) {
            if (value == array[i]) {
                return true;
            }
        }
        return false;
    },
    
    compareObjToObj: function(obj1, obj2, exceptions) {
        for (p in obj1) {
            if (obj1[p] !== obj2[p]) {
                if (!G.util.findArrayValue(exceptions, p)) {
                    return false;
                }
            }
        }
        return true;
    },
    
    cutString: function(str, len) {
        if (str.length > len) {
            str = str.substr(0,len) + '..';
        }
        return str;
    },
    
    getOrganisationUnitIdStringFromFeatures: function(features) {
        var str = '';
        for (var i = 0; i < features.length; i++) {
            str += features[i].attributes.id;
            str += i < (features.length - 1) ? ',' : '';
        }
        return str;
    },
    
    geoJsonDecode: function(doc) {
        doc = Ext.util.JSON.decode(doc);
        var geojson = {};
        geojson.type = 'FeatureCollection';
        geojson.crs = {
            type: 'EPSG',
            properties: {
                code: '4326'
            }
        };
        geojson.features = [];
        for (var i = 0; i < doc.length; i++) {
            geojson.features.push({
                geometry: {
                    type: doc[i].t == 1 ? 'MultiPolygon' : 'Point',
                    coordinates: doc[i].c
                },
                properties: {
                    id: doc[i].i,
                    name: doc[i].n,
                    value: doc[i].v,
                    hcwc: doc[i].h
                }
            });
        }
        return geojson;
    },
    
    mapValueDecode: function(r) {
        var r = Ext.util.JSON.decode(r.responseText),
            mapvalues = [];
        for (var i = 0; i < r.length; i++) {
            mapvalues.push({
                oi: r[i][0],
                v: r[i][1]
            });
        }
        return mapvalues;        
    },
    
    mapView: {
        layer: function(id) {
            var w = new Ext.Window({
                id: 'mapviewlayer_w',
                title: '<span id="window-favorites-title">' + G.i18n.favorite + '</span>',
                layout: 'fit',
                modal: true,
                width: 150,
                height: 98,
                items: [
                    {
                        xtype: 'panel',
                        bodyStyle: 'padding:14px;',
                        items: [
                            { html: G.i18n.open_which_layer }
                        ]
                    }
                ],
                bbar: [
                    '->',
                    {
                        xtype: 'button',
                        iconCls: 'icon-thematic1',
                        hideLabel: true,
                        handler: function() {
                            G.util.mapView.mapView.call(choropleth, id);
                            Ext.getCmp('mapviewlayer_w').destroy();
                        }
                    },
                    {
                        xtype: 'button',
                        iconCls: 'icon-thematic2',
                        hideLabel: true,
                        handler: function() {
                            G.util.mapView.mapView.call(point, id);
                            Ext.getCmp('mapviewlayer_w').destroy();
                        }
                    }
                ]                    
            });
            var c = Ext.getCmp('center').x;
            var e = Ext.getCmp('east').x;
            w.setPagePosition(c+((e-c)/2)-(w.width/2), Ext.getCmp('east').y + 100);
            w.show();
        },
        
        mapView: function(id) {
            var store = G.stores.mapView;
            if (!store.isLoaded) {
                store.load({scope: this, callback: function() {
                    var mapView = store.getAt(store.find('id', id)).data;
                    G.util.mapView.launch.call(this, mapView);
                }});
            }
            else {
                var mapView = store.getAt(store.find('id', id)).data;
                G.util.mapView.launch.call(this, mapView);
            }
        },
        
        launch: function(mapView) {
            if (!this.window.isShown) {
                this.window.show();
                this.window.hide();
            }
            this.mapView = mapView;
            this.updateValues = true;      
            
            this.legend.value = this.mapView.mapLegendType;
            this.legend.method = this.mapView.method || this.legend.method;
            this.legend.classes = this.mapView.classes || this.legend.classes;

            G.vars.map.setCenter(new OpenLayers.LonLat(this.mapView.longitude, this.mapView.latitude), this.mapView.zoom);

            this.valueType.value = this.mapView.mapValueType;
            this.cmp.mapValueType.setValue(this.valueType.value);
            
            this.setMapView();
        }
    }
};

G.date = {
    getNowHMS: function(date) {
        date = date || new Date();      
        return G.date.getDoubleDigit(date.getHours()) + ':' +
               G.date.getDoubleDigit(date.getMinutes()) + ':' +
               G.date.getDoubleDigit(date.getSeconds());
    },
    
    getDoubleDigit: function(unit) {
        unit = '' + unit;
        return unit.length < 2 ? '0' + unit : unit;
    }
};

G.vars = {
    map: null,
    
    parameter: null,
    
    mask: null,
    
    activePanel: {
        value: G.conf.thematicMap,
        setPolygon: function() {
            this.value = G.conf.thematicMap;
        },
        setPoint: function() {
            this.value = G.conf.thematicMap2;
        },
        setSymbol: function() {
            this.value = G.conf.thematicMap3;
        },
        setCentroid: function() {
            this.value = G.conf.thematicMap4;
        },
        isPolygon: function() {
            return this.value === G.conf.thematicMap;
        },
        isPoint: function() {
            return this.value === G.conf.thematicMap2;
        },
        isSymbol: function() {
            return this.value === G.conf.thematicMap3;
        },
        isCentroid: function() {
            return this.value === G.conf.thematicMap4;
        }
    },
    
    activeWidget: null,
    
    lockPosition: false,
    
    relocate: {},
    
    mouseMove: {}
};

G.user = {
    isAdmin: false
};

G.system = {    
    infrastructuralPeriodType: null,
    
    rootNode: null
};

G.func = {
	storeLoadListener: function() {
		this.isLoaded = true;
	},
    
    loadStart: function() {
        G.vars.mask.msg = G.i18n.loading;
        G.vars.mask.show();
    },
    
    loadEnd: function() {
        G.vars.mask.hide();
    }
};

G.cls = {
    vectorLayerButton: function(iconCls, tooltip, widget) {
        return new Ext.Button({
            iconCls: iconCls,
            tooltip: tooltip,
            widget: widget,
            style: 'margin-top:1px',
            enableItems: function(bool) {
                var menuItems = widget == boundary ? [2,3,5,6,8] : [2,3,5,6,7,9];
                for (var i = 0, items = this.menu.items.items; i < menuItems.length; i++) {
                    if (bool) {
                        items[menuItems[i]].enable();
                    }
                    else {
                        items[menuItems[i]].disable();
                    }
                }
            },
            handler: function() {
                this.enableItems(this.widget.layer.features ? this.widget.layer.features.length : false);
            },
            listeners: {
                'afterrender': function(b) {
                    this.menu = new Ext.menu.Menu({
                        parent: b,
                        items: [
                            {
                                text:  G.i18n.edit_layer + '..',
                                iconCls: 'menu-layeroptions-edit',
                                scope: this,
                                handler: function() {
                                    this.widget.window.show(this.id);
                                }
                            },
                            '-',
                            {
                                text: G.i18n.refresh,
                                iconCls: 'menu-layeroptions-refresh',
                                scope: this,
                                handler: function() {
                                    this.widget.updateValues = true;
                                    this.widget.classify();
                                }
                            },
                            {
                                text: G.i18n.clear ,
                                iconCls: 'menu-layeroptions-clear',
                                scope: this,
                                handler: function() {
                                    this.widget.formValues.clearForm.call(this.widget, true);
                                }
                            },
                            '-',
                            {
                                text: G.i18n.filter + '..',
                                iconCls: 'menu-layeroptions-filter',
                                scope: this,
                                handler: function() {
                                    this.widget.filtering.showFilteringWindow.call(this.widget);
                                }
                            },
                            {
                                text: G.i18n.search + '..',
                                iconCls: 'menu-layeroptions-locate',
                                showSearchWindow: function() {
                                    var layer = this.parentMenu.parent.widget.layer;

                                    var data = [];
                                    for (var i = 0; i < layer.features.length; i++) {
                                        data.push([layer.features[i].data.id || i, layer.features[i].data.name]);
                                    }
                                    
                                    if (data.length) {
                                        var featureStore = new Ext.data.ArrayStore({
                                            mode: 'local',
                                            idProperty: 'id',
                                            fields: ['id','name'],
                                            sortInfo: {field: 'name', direction: 'ASC'},
                                            autoDestroy: true,
                                            data: data
                                        });
                                        
                                        this.window = new Ext.Window({
                                            title: '<span id="window-locate-title">' + G.i18n.organisationunit_search +'</span>',
                                            layout: 'fit',
                                            width: G.conf.window_width,
                                            height: G.util.getMultiSelectHeight() + 140,
                                            items: [
                                                {
                                                    xtype: 'form',
                                                    bodyStyle:'padding:8px',
                                                    labelWidth: G.conf.label_width,
                                                    items: [
                                                        { html: '<div class="window-info">' + G.i18n.locate_organisationunit_on_map + '</div>' },
                                                        {
                                                            xtype: 'colorfield',
                                                            id: 'highlightcolor',
                                                            emptyText: G.conf.emptytext,
                                                            labelSeparator: G.conf.labelseparator,
                                                            fieldLabel: G.i18n.highlight_color,
                                                            allowBlank: false,
                                                            width: G.conf.combo_width_fieldset,
                                                            value: "#0000FF"
                                                        },
                                                        {
                                                            xtype: 'textfield',
                                                            emptyText: G.conf.emptytext,
                                                            labelSeparator: G.conf.labelseparator,
                                                            fieldLabel: G.i18n.text_filter,
                                                            width: G.conf.combo_width_fieldset,
                                                            enableKeyEvents: true,
                                                            listeners: {
                                                                'keyup': function(tf) {
                                                                    featureStore.filter('name', tf.getValue(), true, false);
                                                                }
                                                            }
                                                        },                                                    
                                                        { html: '<div class="window-p"></div>' },
                                                        {
                                                            xtype: 'grid',
                                                            height: G.util.getMultiSelectHeight(),
                                                            cm: new Ext.grid.ColumnModel({
                                                                columns: [{id: 'name', header: 'Features', dataIndex: 'name', width: 250}]
                                                            }),
                                                            sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
                                                            viewConfig: {forceFit: true},
                                                            sortable: true,
                                                            autoExpandColumn: 'name',
                                                            store: featureStore,
                                                            listeners: {
                                                                'cellclick': {
                                                                    scope: this,
                                                                    fn: function(g, ri, ci, e) {
                                                                        layer.redraw();
                                                                        
                                                                        var id, feature;
                                                                        id = g.getStore().getAt(ri).data.id;
                                                                        
                                                                        for (var i = 0; i < layer.features.length; i++) {
                                                                            if (layer.features[i].data.id == id) {
                                                                                feature = layer.features[i];
                                                                                break;
                                                                            }
                                                                        }
                                                                        var color = Ext.getCmp('highlightcolor').getValue();
                                                                        var symbolizer;
                                                                        
                                                                        if (feature.geometry.CLASS_NAME == G.conf.map_feature_type_multipolygon_class_name ||
                                                                            feature.geometry.CLASS_NAME == G.conf.map_feature_type_polygon_class_name) {
                                                                            symbolizer = new OpenLayers.Symbolizer.Polygon({
                                                                                'strokeColor': color,
                                                                                'fillColor': color
                                                                            });
                                                                        }
                                                                        else if (feature.geometry.CLASS_NAME == G.conf.map_feature_type_point_class_name) {
                                                                            symbolizer = new OpenLayers.Symbolizer.Point({
                                                                                'pointRadius': 7,
                                                                                'fillColor': color
                                                                            });
                                                                        }

                                                                        layer.drawFeature(feature,symbolizer);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    ]
                                                }
                                            ],
                                            listeners: {
                                                'hide': function() {
                                                    layer.redraw();
                                                }
                                            }
                                        });
                                        this.window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
                                        this.window.show(this.parentMenu.parent.id);
                                    }
                                    else {
                                        Ext.message.msg(false, '<span class="x-msg-hl">' + layer.name + '</span>: ' + G.i18n.no_features_rendered);
                                    }
                                },
                                handler: function() {
                                    this.showSearchWindow();
                                }
                            },
                            {
                                name: 'labels',
                                text: G.i18n.labels + '..',
                                iconCls: 'menu-layeroptions-labels',
                                cmp: {
                                    fontSize: new Ext.form.NumberField({
                                        name: 'fontsize',
                                        fieldLabel: G.i18n.font_size,
                                        labelSeparator: G.conf.labelseparator,
                                        width: G.conf.combo_number_width_small,
                                        enableKeyEvents: true,
                                        allowDecimals: false,
                                        allowNegative: false,
                                        value: 13,
                                        emptyText: 13,
                                        listeners: {
                                            'keyup': {
                                                scope: this,
                                                fn: function(nf) {  
                                                    var item = this.menu.find('name','labels')[0];
                                                                                                    
                                                    if (this.widget.labels) {
                                                        this.widget.labels = false;
                                                        G.util.labels.vector.toggleFeatureLabels(this.widget, nf.getValue(), item.cmp.strong.getValue(),
                                                            item.cmp.italic.getValue(), item.cmp.color.getValue());
                                                    }
                                                }
                                            }
                                        }
                                    }),
                                    strong: new Ext.form.Checkbox({
                                        fieldLabel: '<b>' + G.i18n.bold_ + '</b>',
                                        labelSeparator: G.conf.labelseparator,
                                        listeners: {
                                            'check': {
                                                scope: this,
                                                fn: function(chb, checked) {
                                                    var item = this.menu.find('name','labels')[0];
                                                    
                                                    if (this.widget.labels) {
                                                        this.widget.labels = false;
                                                        G.util.labels.vector.toggleFeatureLabels(this.widget, item.cmp.fontSize.getValue(),
                                                            checked, item.cmp.italic.getValue(), item.cmp.color.getValue());
                                                    }
                                                }
                                            }
                                        }
                                    }),
                                    italic: new Ext.form.Checkbox({
                                        fieldLabel: '<i>' + G.i18n.italic + '</i>',
                                        labelSeparator: G.conf.labelseparator,
                                        listeners: {
                                            'check': {
                                                scope: this,
                                                fn: function(chb, checked) {
                                                    var item = this.menu.find('name','labels')[0];
                                                    
                                                    if (this.widget.labels) {
                                                        this.widget.labels = false;
                                                        G.util.labels.vector.toggleFeatureLabels(this.widget, item.cmp.fontSize.getValue(),
                                                            item.cmp.strong.getValue(), checked, item.cmp.color.getValue());
                                                    }
                                                }
                                            }
                                        }
                                    }),
                                    color: new Ext.ux.ColorField({
                                        fieldLabel: G.i18n.color,
                                        labelSeparator: G.conf.labelseparator,
                                        allowBlank: false,
                                        width: G.conf.combo_width_fieldset,
                                        value: "#000000",
                                        listeners: {
                                            'select': {
                                                scope: this,
                                                fn: function(cf) {
                                                    var item = this.menu.find('name','labels')[0];
                                                    
                                                    if (this.widget.labels) {
                                                        this.widget.labels = false;
                                                        G.util.labels.vector.toggleFeatureLabels(this.widget, item.cmp.fontSize.getValue(),
                                                            item.cmp.strong.getValue(), item.cmp.italic.getValue(), cf.getValue());
                                                    }
                                                }
                                            }
                                        }
                                    })
                                },
                                showLabelWindow: function() {
                                    var layer = this.parentMenu.parent.widget.layer;
                                    if (layer.features.length) {
                                        if (this.cmp.labelWindow) {
                                            this.cmp.labelWindow.show();
                                        }
                                        else {
                                            this.cmp.labelWindow = new Ext.Window({
                                                title: '<span id="window-labels-title">' + G.i18n.labels + '</span>',
                                                layout: 'fit',
                                                closeAction: 'hide',
                                                width: G.conf.window_width,
                                                height: 200,
                                                items: [
                                                    {
                                                        xtype: 'form',
                                                        bodyStyle: 'padding:8px',
                                                        labelWidth: G.conf.label_width,
                                                        items: [
                                                            {html: '<div class="window-info">' + G.i18n.show_hide_feature_labels + '</div>'},
                                                            this.cmp.fontSize,
                                                            this.cmp.strong,
                                                            this.cmp.italic,
                                                            this.cmp.color
                                                        ]
                                                    }
                                                ],
                                                bbar: [
                                                    '->',
                                                    {
                                                        xtype: 'button',
                                                        iconCls: 'icon-assign',
                                                        hideLabel: true,
                                                        text: G.i18n.showhide,
                                                        scope: this,
                                                        handler: function() {
                                                            if (layer.features.length) {
                                                                G.util.labels.vector.toggleFeatureLabels(layer.widget, this.cmp.fontSize.getValue(),
                                                                    this.cmp.strong.getValue(), this.cmp.italic.getValue(), this.cmp.color.getValue());
                                                            }
                                                            else {
                                                                Ext.message.msg(false, '<span class="x-msg-hl">' + layer.name + '</span>: ' + Gi.i18n.no_features_rednered );
                                                            }
                                                        }
                                                    }
                                                ]
                                            });
                                            this.cmp.labelWindow.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
                                            this.cmp.labelWindow.show(this.parentMenu.parent.id);
                                        }
                                    }
                                    else {
                                        Ext.message.msg(false, '<span class="x-msg-hl">' + layer.name + '</span>: ' + Gi.i18n.no_features_rednered );
                                    }
                                },
                                handler: function() {
                                    this.showLabelWindow();
                                }
                            },
                            '-',
                            {
                                text: G.i18n.opacity,
                                iconCls: 'menu-layeroptions-opacity',
                                menu: { 
                                    items: G.conf.opacityItems,
                                    listeners: {
                                        'itemclick': {
                                            scope: this,
                                            fn: function(item) {
                                                this.widget.layer.setOpacity(item.text);
                                            }
                                        }
                                    }
                                }
                            },
                            '-',
                            {
                                name: 'history',
                                text: G.i18n.history,
                                iconCls: 'menu-history',
                                disabled: true,
                                menu: {},
                                addMenu: function() {
                                    this.menu = new Ext.menu.Menu({
                                        defaults: {
                                            itemCls: 'x-menu-item x-menu-item-custom'
                                        },
                                        items: [],
                                        listeners: {
                                            'add': function(menu) {
                                                var items = menu.items.items;
                                                var keys = menu.items.keys;
                                                items.unshift(items.pop());
                                                keys.unshift(keys.pop());
                                                
                                                if (items.length > 10) {
                                                    items[items.length-1].destroy();
                                                }
                                            },
                                            'click': {
                                                scope: this,
                                                fn: function(menu, item) {
                                                    G.util.mapView.launch.call(this.parentMenu.parent.widget, item.mapView);
                                                }
                                            }
                                        }
                                    });
                                },
                                addItem: function(scope) {
                                    if (!this.menu.items) {
                                        this.addMenu();
                                    }

                                    var mapView = scope.formValues.getAllValues.call(scope);
                                    mapView.widget = scope;
                                    mapView.timestamp = G.date.getNowHMS();
                                    var c1 = '<span class="menu-item-inline-c1">';
                                    var c2 = '<span class="menu-item-inline-c2">';
                                    var spanEnd = '</span>';
                                    mapView.label = '<span class="menu-item-inline-bg">' +
                                                    c1 + mapView.timestamp + spanEnd +
                                                    c2 + mapView.parentOrganisationUnitName + spanEnd +
                                                    c1 + '( ' + mapView.organisationUnitLevelName + ' )' + spanEnd + 
                                                    c2 + (mapView.mapValueType == G.conf.map_value_type_indicator ? mapView.indicatorName : mapView.dataElementName) + spanEnd +
                                                    c1 + mapView.periodName + spanEnd +
                                                    spanEnd;
                                    
                                    for (var i = 0; i < this.menu.items.items.length; i++) {
                                        if (G.util.compareObjToObj(mapView, this.menu.items.items[i].mapView, ['longitude','latitude','zoom','widget','timestamp','label'])) {
                                            this.menu.items.items[i].destroy();
                                        }
                                    }
                                    
                                    this.menu.addMenuItem({
                                        html: mapView.label,
                                        mapView: mapView
                                    });
                                    
                                    this.enable();
                                }
                            }
                        ]
                    });
                    
                    this.widget.button = this;
                }
            }
        });
    }
};
