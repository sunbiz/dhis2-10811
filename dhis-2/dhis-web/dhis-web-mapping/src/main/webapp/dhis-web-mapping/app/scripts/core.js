Ext.onReady( function() {

if (!Ext.isDefined(GIS)) {
	GIS = {};
}

GIS.core = {};

GIS.core.getConfigs = function() {
	return {
		finals: {
			layer: {
				type_base: 'base',
				type_vector: 'vector'
			},
			dimension: {
				indicator: {
					id: 'indicator',
					param: 'in'
				},
				dataElement: {
					id: 'dataElement',
					param: 'de'
				},
				period: {
					id: 'period',
					param: 'pe'
				},
				organisationUnit: {
					id: 'organisationUnit',
					param: 'ou'
				}
			},
			widget: {
				value: 'value',
				legendtype_automatic: 'automatic',
				legendtype_predefined: 'predefined',
				symbolizer_color: 'color',
				symbolizer_image: 'image',
				loadtype_organisationunit: 'organisationUnit',
				loadtype_data: 'data',
				loadtype_legend: 'legend'
			},
			openLayers: {
				point_classname: 'OpenLayers.Geometry.Point'
			},
			mapfish: {
				classify_with_bounds: 1,
				classify_by_equal_intervals: 2,
				classify_by_quantils: 3
			}
		},
		url: {
			path_api: '/api/',
			path_gis: '/dhis-web-mapping/'
		},
		layout: {
			widget: {
				item_width: 262,
				itemlabel_width: 95,
				window_width: 284
			},
			tool: {
				item_width: 222,
				itemlabel_width: 95,
				window_width: 250
			},
			grid: {
				row_height: 27
			}
		},
		period: {
			periodTypes: [
				{id: 'Daily', name: 'Daily'},
				{id: 'Weekly', name: 'Weekly'},
				{id: 'Monthly', name: 'Monthly'},
				{id: 'BiMonthly', name: 'BiMonthly'},
				{id: 'Quarterly', name: 'Quarterly'},
				{id: 'SixMonthly', name: 'SixMonthly'},
				{id: 'Yearly', name: 'Yearly'},
				{id: 'FinancialOct', name: 'FinancialOct'},
				{id: 'FinancialJuly', name: 'FinancialJuly'},
				{id: 'FinancialApril', name: 'FinancialApril'}
			]
		}
	};
};

GIS.core.getUtils = function(gis) {
	var conf = gis.conf,
		util = {};

	util.google = {};

	util.google.openTerms = function() {
		window.open('http://www.google.com/intl/en-US_US/help/terms_maps.html', '_blank');
	};

	util.map = {};

	util.map.getVisibleVectorLayers = function() {
		var layers = [],
			layer;

		for (var i = 0; i < gis.olmap.layers.length; i++) {
			layer = gis.olmap.layers[i];
			if (layer.layerType === conf.finals.layer.type_vector &&
				layer.visibility &&
				layer.features.length) {
				layers.push(layer);
			}
		}
		return layers;
	};

	util.map.getExtendedBounds = function(layers) {
		var bounds = null;
		if (layers.length) {
			bounds = layers[0].getDataExtent();
			if (layers.length > 1) {
				for (var i = 1; i < layers.length; i++) {
					bounds.extend(layers[i].getDataExtent());
				}
			}
		}
		return bounds;
	};

	util.map.zoomToVisibleExtent = function(olmap) {
		var bounds = util.map.getExtendedBounds(util.map.getVisibleVectorLayers(olmap));
		if (bounds) {
			olmap.zoomToExtent(bounds);
		}
	};

	util.map.getTransformedFeatureArray = function(features) {
		var sourceProjection = new OpenLayers.Projection("EPSG:4326"),
			destinationProjection = new OpenLayers.Projection("EPSG:900913");
		for (var i = 0; i < features.length; i++) {
			features[i].geometry.transform(sourceProjection, destinationProjection);
		}
		return features;
	};

	util.geojson = {};

	util.geojson.decode = function(doc) {
		var geojson = {};
		geojson.type = 'FeatureCollection';
		geojson.crs = {
			type: 'EPSG',
			properties: {
				code: '4326'
			}
		};
		geojson.features = [];

		for (var i = 0; i < doc.geojson.length; i++) {
			geojson.features.push({
				geometry: {
					type: parseInt(doc.geojson[i].ty) === 1 ? 'MultiPolygon' : 'Point',
					coordinates: doc.geojson[i].co
				},
				properties: {
					id: doc.geojson[i].uid,
					internalId: doc.geojson[i].iid,
					name: doc.geojson[i].na,
					hcwc: doc.geojson[i].hc,
					path: doc.geojson[i].path,
					parentId: doc.geojson[i].pi,
					parentName: doc.geojson[i].pn,
					hasCoordinatesUp: doc.properties.hasCoordinatesUp
				}
			});
		}

		return geojson;
	};

	util.gui = {};
	util.gui.combo = {};

	util.gui.combo.setQueryMode = function(cmpArray, mode) {
		for (var i = 0; i < cmpArray.length; i++) {
			cmpArray[i].queryMode = mode;
		}
	};

	return util;
};

GIS.core.getStores = function(gis) {
	var stores = {};

	stores.organisationUnitLevels = GIS.core.OrganisationUnitLevelStore(gis);

	return stores;
};

GIS.core.getOLMap = function(gis) {
	var olmap,
		addControl;

	addControl = function(name, fn) {
		var button,
			panel;

		button = new OpenLayers.Control.Button({
			displayClass: 'olControlButton',
			trigger: function() {
				fn.call(gis.olmap);
				gis.layer.googleHybrid.redraw();
			}
		});

		panel = new OpenLayers.Control.Panel({
			defaultControl: button
		});

		panel.addControls([button]);

		olmap.addControl(panel);

		panel.div.className += ' ' + name;
		panel.div.childNodes[0].className += ' ' + name + 'Button';
	};

	olmap = new OpenLayers.Map({
		controls: [
			new OpenLayers.Control.Navigation({
				documentDrag: true
			}),
			new OpenLayers.Control.MousePosition({
				id: 'mouseposition',
				prefix: '<span class="el-fontsize-10"><span class="text-mouseposition-lonlat">LON </span>',
				separator: '<span class="text-mouseposition-lonlat">&nbsp;&nbsp;LAT </span>',
				suffix: '<div id="google-logo" onclick="javascript:gis.util.google.openTerms();"></div></span>'
			}),
			new OpenLayers.Control.Permalink()
		],
		displayProjection: new OpenLayers.Projection('EPSG:4326'),
		maxExtent: new OpenLayers.Bounds(-20037508, -20037508, 20037508, 20037508),
		mouseMove: {}, // Track all mouse moves
		relocate: {} // Relocate organisation units
	});

	// Map events
	olmap.events.register('mousemove', null, function(e) {
		gis.olmap.mouseMove.x = e.clientX;
		gis.olmap.mouseMove.y = e.clientY;
	});

	olmap.zoomToVisibleExtent = function() {
		gis.util.map.zoomToVisibleExtent(this);
	};

	olmap.closeAllLayers = function() {
		gis.layer.boundary.core.reset();
		gis.layer.thematic1.core.reset();
		gis.layer.thematic2.core.reset();
		gis.layer.facility.core.reset();
	};

	addControl('zoomIn', olmap.zoomIn);
	addControl('zoomOut', olmap.zoomOut);
	addControl('zoomVisible', olmap.zoomToVisibleExtent);
	addControl('measure', function() {
		GIS.core.MeasureWindow(gis).show();
	});

	return olmap;
};

GIS.core.getLayers = function(gis) {
	var layers = {},
		createSelectionHandlers;

	if (window.google) {
		layers.googleStreets = new OpenLayers.Layer.Google('Google Streets', {
			numZoomLevels: 20,
			animationEnabled: true,
			layerType: gis.conf.finals.layer.type_base,
			layerOpacity: 1,
			setLayerOpacity: function(number) {
				if (number) {
					this.layerOpacity = parseFloat(number);
				}
				this.setOpacity(this.layerOpacity);
			}
		});
		layers.googleStreets.id = 'googleStreets';

		layers.googleHybrid = new OpenLayers.Layer.Google('Google Hybrid', {
			type: google.maps.MapTypeId.HYBRID,
			numZoomLevels: 20,
			animationEnabled: true,
			layerType: gis.conf.finals.layer.type_base,
			layerOpacity: 1,
			setLayerOpacity: function(number) {
				if (number) {
					this.layerOpacity = parseFloat(number);
				}
				this.setOpacity(this.layerOpacity);
			}
		});
		layers.googleHybrid.id = 'googleHybrid';
	}
	else {
		layers.openStreetMap = new OpenLayers.Layer.OSM('OpenStreetMap', {
			layerType: gis.conf.finals.layer.type_base,
			layerOpacity: 1,
			setLayerOpacity: function(number) {
				if (number) {
					this.layerOpacity = parseFloat(number);
				}
				this.setOpacity(this.layerOpacity);
			}
		});
		layers.openStreetMap.id = 'openStreetMap';
	}

	layers.boundary = GIS.core.VectorLayer(gis, 'boundary', 'Boundary layer', {opacity: 0.8});
	layers.boundary.core = new mapfish.GeoStat.Boundary(gis.olmap, {
		layer: layers.boundary,
		gis: gis
	});

	layers.thematic1 = GIS.core.VectorLayer(gis, 'thematic1', 'Thematic layer 1', {opacity: 0.8});
	layers.thematic1.core = new mapfish.GeoStat.Thematic1(gis.olmap, {
		layer: layers.thematic1,
		gis: gis
	});

	layers.thematic2 = GIS.core.VectorLayer(gis, 'thematic2', 'Thematic layer 2', {opacity: 0.8});
	layers.thematic2.core = new mapfish.GeoStat.Thematic2(gis.olmap, {
		layer: layers.thematic2,
		gis: gis
	});

	layers.facility = GIS.core.VectorLayer(gis, 'facility', 'Facility layer', {opacity: 0.8});
	layers.facility.core = new mapfish.GeoStat.Facility(gis.olmap, {
		layer: layers.facility,
		gis: gis
	});

	return layers;
};

GIS.core.createSelectHandlers = function(gis, layer) {
	var isRelocate = !!GIS.app ? (gis.init.user.isAdmin ? true : false) : false,

		window,
		infrastructuralPeriod,
		onHoverSelect,
		onHoverUnselect,
		onClickSelect;

	onHoverSelect = function fn(feature) {
		if (window) {
			window.destroy();
		}
		window = Ext.create('Ext.window.Window', {
			cls: 'gis-window-widget-feature',
			preventHeader: true,
			shadow: false,
			resizable: false,
			items: {
				html: feature.attributes.label
			}
		});

		window.show();

		var eastX = gis.viewport.eastRegion.getPosition()[0],
			centerX = gis.viewport.centerRegion.getPosition()[0],
			centerRegionCenterX = centerX + ((eastX - centerX) / 2),
			centerRegionY = gis.viewport.centerRegion.getPosition()[1] + (GIS.app ? 32 : 0);

		window.setPosition(centerRegionCenterX - (window.getWidth() / 2), centerRegionY);
	};

	onHoverUnselect = function fn(feature) {
		window.destroy();
	};

	onClickSelect = function fn(feature) {
		var showInfo,
			showRelocate,
			drill,
			menu,
			selectHandlers,
			isPoint = feature.geometry.CLASS_NAME === gis.conf.finals.openLayers.point_classname;

		// Relocate
		showRelocate = function() {
			if (gis.olmap.relocate.window) {
				gis.olmap.relocate.window.destroy();
			}

			gis.olmap.relocate.window = Ext.create('Ext.window.Window', {
				title: 'Relocate facility',
				layout: 'fit',
				iconCls: 'gis-window-title-icon-relocate',
				cls: 'gis-container-default',
				setMinWidth: function(minWidth) {
					this.setWidth(this.getWidth() < minWidth ? minWidth : this.getWidth());
				},
				items: {
					html: feature.attributes.name,
					cls: 'gis-container-inner'
				},
				bbar: [
					'->',
					{
						xtype: 'button',
						hideLabel: true,
						text: GIS.i18n.cancel,
						handler: function() {
							gis.olmap.relocate.active = false;
							gis.olmap.relocate.window.destroy();
							gis.olmap.getViewport().style.cursor = 'auto';
						}
					}
				],
				listeners: {
					close: function() {
						gis.olmap.relocate.active = false;
						gis.olmap.getViewport().style.cursor = 'auto';
					}
				}
			});

			gis.olmap.relocate.window.show();
			gis.olmap.relocate.window.setMinWidth(220);

			gis.util.gui.window.setPositionTopRight(gis.olmap.relocate.window);
		};

		// Infrastructural data
		showInfo = function() {
			Ext.Ajax.request({
				url: gis.baseUrl + gis.conf.url.path_gis + 'getFacilityInfo.action',
				params: {
					id: feature.attributes.id
				},
				success: function(r) {
					var ou = Ext.decode(r.responseText);

					if (layer.infrastructuralWindow) {
						layer.infrastructuralWindow.destroy();
					}

					layer.infrastructuralWindow = Ext.create('Ext.window.Window', {
						title: 'Facility information', //i18n
						layout: 'column',
						iconCls: 'gis-window-title-icon-information',
						cls: 'gis-container-default',
						width: 460,
						height: 400, //todo
						period: null,
						items: [
							{
								cls: 'gis-container-inner',
								columnWidth: 0.4,
								bodyStyle: 'padding-right:4px',
								items: [
									{
										html: GIS.i18n.name,
										cls: 'gis-panel-html-title'
									},
									{
										html: feature.attributes.name,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.type,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.ty,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.code,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.co,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.address,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.ad,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.contact_person,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.cp,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.email,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.em,
										cls: 'gis-panel-html'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										html: GIS.i18n.phone_number,
										cls: 'gis-panel-html-title'
									},
									{
										html: ou.pn,
										cls: 'gis-panel-html'
									}
								]
							},
							{
								xtype: 'form',
								cls: 'gis-container-inner gis-form-widget',
								columnWidth: 0.6,
								bodyStyle: 'padding-left:4px',
								items: [
									{
										html: GIS.i18n.infrastructural_data,
										cls: 'gis-panel-html-title'
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										xtype: 'combo',
										fieldLabel: GIS.i18n.period,
										editable: false,
										valueField: 'id',
										displayField: 'name',
										forceSelection: true,
										width: 255, //todo
										labelWidth: 70,
										store: gis.store.infrastructuralPeriodsByType,
										lockPosition: false,
										listeners: {
											select: function() {
												infrastructuralPeriod = this.getValue();

												layer.widget.infrastructuralDataElementValuesStore.load({
													params: {
														periodId: infrastructuralPeriod,
														organisationUnitId: feature.attributes.internalId
													}
												});
											}
										}
									},
									{
										cls: 'gis-panel-html-separator'
									},
									{
										xtype: 'grid',
										cls: 'gis-grid',
										height: 300, //todo
										width: 255,
										scroll: 'vertical',
										columns: [
											{
												id: 'dataElementName',
												text: 'Data element',
												dataIndex: 'dataElementName',
												sortable: true,
												width: 195
											},
											{
												id: 'value',
												header: 'Value',
												dataIndex: 'value',
												sortable: true,
												width: 60
											}
										],
										disableSelection: true,
										store: layer.widget.infrastructuralDataElementValuesStore
									}
								]
							}
						],
						listeners: {
							show: function() {
								if (infrastructuralPeriod) {
									this.down('combo').setValue(infrastructuralPeriod);
									infrastructuralDataElementValuesStore.load({
										params: {
											periodId: infrastructuralPeriod,
											organisationUnitId: feature.attributes.internalId
										}
									});
								}
							}
						}
					});

					layer.infrastructuralWindow.show();
					gis.util.gui.window.setPositionTopRight(layer.infrastructuralWindow);
				}
			});
		};

		// Drill or float
		drill = function(direction) {
			var store = gis.store.organisationUnitLevels,
				view = layer.core.view,
				config,
				loader;

			store.loadFn( function() {
				if (direction === 'up') {
					var rootNode = gis.init.rootNodes[0],
						level = store.getAt(store.find('level', view.organisationUnitLevel.level - 1));

					config = {
						organisationUnitLevel: {
							id: level.data.id,
							name: level.data.name,
							level: level.data.level
						},
						parentOrganisationUnit: {
							id: rootNode.id,
							name: rootNode.text,
							level: rootNode.level
						},
						parentGraph: '/' + gis.init.rootNodes[0].id
					};
				}
				else if (direction === 'down') {
					var level = store.getAt(store.find('level', view.organisationUnitLevel.level + 1));

					config = {
						organisationUnitLevel: {
							id: level.data.id,
							name: level.data.name,
							level: level.data.level
						},
						parentOrganisationUnit: {
							id: feature.attributes.id,
							name: feature.attributes.name,
							level: view.organisationUnitLevel.level
						},
						parentGraph: feature.attributes.path
					};
				}

				view = layer.core.extendView(null, config);

				if (view) {
					loader = layer.core.getLoader();
					loader.updateGui = true;
					loader.zoomToVisibleExtent = true;
					loader.hideMask = true;
					loader.load(view);
				}
			});
		};

		// Menu
		var menuItems = [
			Ext.create('Ext.menu.Item', {
				text: 'Float up',
				iconCls: 'gis-menu-item-icon-float',
				disabled: !feature.attributes.hasCoordinatesUp,
				handler: function() {
					drill('up');
				}
			}),
			Ext.create('Ext.menu.Item', {
				text: 'Drill down',
				iconCls: 'gis-menu-item-icon-drill',
				cls: 'gis-menu-item-first',
				disabled: !feature.attributes.hcwc,
				handler: function() {
					drill('down');
				}
			})
		];

		if (isRelocate && isPoint) {
			menuItems.push({
				xtype: 'menuseparator'
			});

			menuItems.push( Ext.create('Ext.menu.Item', {
				text: GIS.i18n.relocate,
				iconCls: 'gis-menu-item-icon-relocate',
				disabled: !gis.init.user.isAdmin,
				handler: function(item) {
					gis.olmap.relocate.active = true;
					gis.olmap.relocate.feature = feature;
					gis.olmap.getViewport().style.cursor = 'crosshair';
					showRelocate();
				}
			}));

			menuItems.push( Ext.create('Ext.menu.Item', {
				text: 'Show information', //i18n
				iconCls: 'gis-menu-item-icon-information',
				handler: function(item) {
					if (gis.store.infrastructuralPeriodsByType.isLoaded) {
						showInfo();
					}
					else {
						gis.store.infrastructuralPeriodsByType.load({
							params: {
								name: gis.init.systemSettings.infrastructuralPeriodType
							},
							callback: function() {
								showInfo();
							}
						});
					}
				}
			}));
		}

		menuItems[menuItems.length - 1].addCls('gis-menu-item-last');

		menu = new Ext.menu.Menu({
			shadow: false,
			showSeparator: false,
			defaults: {
				bodyStyle: 'padding-right:6px'
			},
			items: menuItems,
			listeners: {
				afterrender: function() {
					this.getEl().addCls('gis-toolbar-btn-menu');
				}
			}
		});

		menu.showAt([gis.olmap.mouseMove.x, gis.olmap.mouseMove.y]);
	};

	selectHandlers = new OpenLayers.Control.newSelectFeature(layer, {
		onHoverSelect: onHoverSelect,
		onHoverUnselect: onHoverUnselect,
		onClickSelect: onClickSelect
	});

	gis.olmap.addControl(selectHandlers);
	selectHandlers.activate();
};

GIS.core.OrganisationUnitLevelStore = function(gis) {
	return Ext.create('Ext.data.Store', {
		fields: ['id', 'name', 'level'],
		proxy: {
			type: 'jsonp',
			url: gis.baseUrl + gis.conf.url.path_api + 'organisationUnitLevels.jsonp?viewClass=detailed&links=false&paging=false',
			reader: {
				type: 'json',
				root: 'organisationUnitLevels'
			}
		},
		autoLoad: true,
		cmp: [],
		isLoaded: false,
		loadFn: function(fn) {
			if (this.isLoaded) {
				fn.call();
			}
			else {
				this.load(fn);
			}
		},
		getRecordByLevel: function(level) {
			return this.getAt(this.findExact('level', level));
		},
		listeners: {
			load: function() {
				if (!this.isLoaded) {
					this.isLoaded = true;
					gis.util.gui.combo.setQueryMode(this.cmp, 'local');
				}
				this.sort('level', 'ASC');
			}
		}
	});
};

GIS.core.StyleMap = function(id, labelConfig) {
	var defaults = {
			fillOpacity: 1,
			strokeColor: '#fff',
			strokeWidth: 1
		},
		select = {
			strokeColor: '#000000',
			strokeWidth: 2,
			cursor: 'pointer'
		};

	if (id === 'boundary') {
		defaults.fillOpacity = 0;
		defaults.strokeColor = '#000';

		select.fillColor = '#000';
		select.fillOpacity = 0.2;
		select.strokeWidth = 1;
	}

	if (labelConfig) {
		defaults.label = '\${label}';
		defaults.fontFamily = 'arial,sans-serif,ubuntu,consolas';
		defaults.fontSize = labelConfig.fontSize ? labelConfig.fontSize + 'px' : '13px';
		defaults.fontWeight = labelConfig.strong ? 'bold' : 'normal';
		defaults.fontStyle = labelConfig.italic ? 'italic' : 'normal';
		defaults.fontColor = labelConfig.color ? '#' + labelConfig.color : '#000000';
	}

	return new OpenLayers.StyleMap({
		'default': new OpenLayers.Style(
			OpenLayers.Util.applyDefaults(defaults),
			OpenLayers.Feature.Vector.style['default']),
		select: new OpenLayers.Style(select)
	});
};

GIS.core.VectorLayer = function(gis, id, name, config) {
	var layer = new OpenLayers.Layer.Vector(name, {
		strategies: [
			new OpenLayers.Strategy.Refresh({
				force:true
			})
		],
		styleMap: GIS.core.StyleMap(id),
		visibility: false,
		displayInLayerSwitcher: false,
		layerType: gis.conf.finals.layer.type_vector,
		layerOpacity: config ? config.opacity || 1 : 1,
		setLayerOpacity: function(number) {
			if (number) {
				this.layerOpacity = parseFloat(number);
			}
			this.setOpacity(this.layerOpacity);
		},
		hasLabels: false
	});

	layer.id = id;

	return layer;
};

GIS.core.MeasureWindow = function(gis) {
	var window,
		label,
		handleMeasurements,
		control,
		styleMap;

	styleMap = new OpenLayers.StyleMap({
		'default': new OpenLayers.Style()
	});

	control = new OpenLayers.Control.Measure(OpenLayers.Handler.Path, {
		persist: true,
		immediate: true,
		handlerOption: {
			layerOptions: {
				styleMap: styleMap
			}
		}
	});

	handleMeasurements = function(e) {
		if (e.measure) {
			label.setText(e.measure.toFixed(2) + ' ' + e.units);
		}
	};

	gis.olmap.addControl(control);

	control.events.on({
		measurepartial: handleMeasurements,
		measure: handleMeasurements
	});

	control.geodesic = true;
	control.activate();

	label = Ext.create('Ext.form.Label', {
		style: 'height: 20px',
		text: '0 km'
	});

	window = Ext.create('Ext.window.Window', {
		title: 'Measure distance', //i18n
		layout: 'fit',
		cls: 'gis-container-default',
		bodyStyle: 'text-align: center',
		width: 130,
		minWidth: 130,
		resizable: false,
		items: label,
		listeners: {
			show: function() {
				var x = gis.viewport.eastRegion.x - this.getWidth() - 5,
					y = 60;
				this.setPosition(x, y);
			},
			destroy: function() {
				control.deactivate();
				gis.olmap.removeControl(control);
			}
		}
	});

	return window;
};

GIS.core.MapLoader = function(gis) {
	var getMap,
		setMap,
		afterLoad,
		callBack,
		register = [],
		loader;

	getMap = function() {
		Ext.data.JsonP.request({
			url: gis.baseUrl + gis.conf.url.path_api + 'maps/' + gis.map.id + '.jsonp?links=false',
			success: function(r) {
				gis.map = r;
				setMap();
			},
			failure: function() {
				gis.olmap.mask.hide();
				alert('Map id not recognized' + (gis.el ? ' (' + gis.el + ')' : ''));
				return;
			}
		});
	};

	setMap = function() {
		var view,
			views = gis.map.mapViews,
			loader;

		if (!(Ext.isArray(views) && views.length)) {
			gis.olmap.mask.hide();
			alert('Favorite is outdated - please create a new one'); //i18n
			return;
		}

		if (gis.viewport.favoriteWindow && gis.viewport.favoriteWindow.isVisible()) {
			gis.viewport.favoriteWindow.destroy();
		}

		gis.olmap.closeAllLayers();

		for (var i = 0; i < views.length; i++) {
			view = views[i];
			loader = gis.layer[view.layer].core.getLoader();
			loader.updateGui = !gis.el;
			loader.callBack = callBack;
			loader.load(view);
		}
	};

	callBack = function(layer) {
		register.push(layer);

		if (register.length === gis.map.mapViews.length) {
			afterLoad();
		}
	};

	afterLoad = function() {
		register = [];

		if (gis.el) {
			gis.olmap.zoomToVisibleExtent();
		}
		else {
			if (gis.map.longitude && gis.map.latitude && gis.map.zoom) {
				gis.olmap.setCenter(new OpenLayers.LonLat(gis.map.longitude, gis.map.latitude), gis.map.zoom);
			}
			else {
				gis.olmap.zoomToVisibleExtent();
			}
		}

		if (gis.viewport.interpretationButton) {
			gis.viewport.interpretationButton.enable();
		}

		gis.olmap.mask.hide();
	};

	loader = {
		load: function() {
			gis.olmap.mask.show();

			if (gis.map.id) {
				getMap();
			}
			else {
				setMap();
			}
		}
	};

	return loader;
};

GIS.core.LayerLoaderBoundary = function(gis, layer) {
	var olmap = layer.map,
		compareView,
		loadOrganisationUnits,
		loadData,
		loadLegend,
		afterLoad,
		loader;

	compareView = function(view, doExecute) {
		var src = layer.core.view;

		if (!src) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.organisationUnitLevel.id !== src.organisationUnitLevel.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.parentOrganisationUnit.id !== src.parentOrganisationUnit.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

        gis.olmap.mask.hide();
	};

    loadOrganisationUnits = function(view) {
		Ext.data.JsonP.request({
			url: gis.baseUrl + gis.conf.url.path_gis + 'getGeoJson.action',
			params: {
				parentId: view.parentOrganisationUnit.id,
				level: view.organisationUnitLevel.id
			},
			scope: this,
			disableCaching: false,
			success: function(r) {
				var geojson = gis.util.geojson.decode(r),
					format = new OpenLayers.Format.GeoJSON(),
					features = gis.util.map.getTransformedFeatureArray(format.read(geojson));

				if (!Ext.isArray(features)) {
					olmap.mask.hide();
					alert('Invalid coordinates');
					return;
				}

				if (!features.length) {
					olmap.mask.hide();
					alert('No valid coordinates found'); //todo //i18n
					return;
				}

				loadData(view, features);
			},
			failure: function(r) {
				olmap.mask.hide();
				alert('Server error while loading coordinates');
			}
		});
    };

    loadData = function(view, features) {
		view = view || layer.core.view;
		features = features || layer.features.slice(0);;

		for (var i = 0; i < features.length; i++) {
			features[i].attributes.label = features[i].attributes.name;
			features[i].attributes.value = 0;
		}

		layer.removeFeatures(layer.features);
		layer.addFeatures(features);

		layer.core.featureStore.loadFeatures(layer.features.slice(0));

		loadLegend(view);
	};

	loadLegend = function(view) {
		view = view || layer.core.view;

		var options = {
            indicator: gis.conf.finals.widget.value,
            method: 2,
            numClasses: 5,
            colors: layer.core.getColors('000000', '000000'),
            minSize: 6,
            maxSize: 6
        };

		layer.core.view = view;

		layer.core.applyClassification(options);

		afterLoad(view);
	};

	afterLoad = function(view) {

		// Layer
		if (layer.item) {
			layer.item.setValue(true, view.opacity);
		}
		else {
			layer.setLayerOpacity(view.opacity);
		}

		// Gui
		if (loader.updateGui && Ext.isObject(layer.widget)) {
			layer.widget.setGui(view);
		}

		// Zoom
		if (loader.zoomToVisibleExtent) {
			olmap.zoomToVisibleExtent();
		}

		// Mask
		if (loader.hideMask) {
			olmap.mask.hide();
		}

		// Map callback
		if (loader.callBack) {
			loader.callBack(layer);
		}
		else {
			gis.map = null;
			gis.viewport.interpretationButton.disable();
		}
	};

	loader = {
		compare: false,
		updateGui: false,
		zoomToVisibleExtent: false,
		hideMask: false,
		callBack: null,
		load: function(view) {
			gis.olmap.mask.show();

			if (this.compare) {
				compareView(view, true);
			}
			else {
				loadOrganisationUnits(view);
			}
		},
		loadData: loadData,
		loadLegend: loadLegend
	};

	return loader;
};

GIS.core.LayerLoaderThematic = function(gis, layer) {
	var olmap = layer.map,
		compareView,
		loadOrganisationUnits,
		loadData,
		loadLegend,
		afterLoad,
		loader;

	compareView = function(view, doExecute) {
		var src = layer.core.view;

		if (!src) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.organisationUnitLevel.id !== src.organisationUnitLevel.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.parentOrganisationUnit.id !== src.parentOrganisationUnit.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.valueType !== src.valueType) {
			if (doExecute) {
				loadData(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}
		else {
			if (view.valueType === gis.conf.finals.dimension.indicator.id && view.indicator.id !== src.indicator.id) {
				if (doExecute) {
					loadData(view);
				}
				return gis.conf.finals.widget.loadtype_data;
			}
			if (view.valueType === gis.conf.finals.dimension.dataElement.id && view.dataElement.id !== src.dataElement.id) {
				if (doExecute) {
					loadData(view);
				}
				return gis.conf.finals.widget.loadtype_data;
			}
		}

		if (view.period.id !== src.period.id) {
			if (doExecute) {
				loadData(view);
			}
			return gis.conf.finals.widget.loadtype_data;
		}

		if (view.legendType !== src.legendType) {
			if (doExecute) {
				loadLegend(view);
			}
			return gis.conf.finals.widget.loadtype_legend;
		}
		else {
			if (view.legendType === gis.conf.finals.widget.legendtype_automatic) {
				if (view.classes !== src.classes || view.method !== src.method || view.colorLow !== src.colorLow || view.radiusLow !== src.radiusLow ||	view.colorHigh !== src.colorHigh || view.radiusHigh !== src.radiusHigh) {
					if (doExecute) {
						loadLegend(view);
					}
					return gis.conf.finals.widget.loadtype_legend;
				}
			}

			if (view.legendType === gis.conf.finals.widget.legendtype_predefined && view.legendSet.id !== src.legendSet.id) {
				if (doExecute) {
					loadLegend(view);
				}
				return gis.conf.finals.widget.loadtype_legend;
			}
		}

        gis.olmap.mask.hide();
	};

    loadOrganisationUnits = function(view) {
		Ext.data.JsonP.request({
			url: gis.baseUrl + gis.conf.url.path_gis + 'getGeoJson.action',
			params: {
				parentId: view.parentOrganisationUnit.id,
				level: view.organisationUnitLevel.id
			},
			scope: this,
			disableCaching: false,
			success: function(r) {
				var geojson = gis.util.geojson.decode(r),
					format = new OpenLayers.Format.GeoJSON(),
					features = gis.util.map.getTransformedFeatureArray(format.read(geojson));

				if (!Ext.isArray(features)) {
					olmap.mask.hide();
					alert('Coordinates are invalid');
					return;
				}

				if (!features.length) {
					olmap.mask.hide();
					alert('No valid coordinates found'); //todo //i18n
					return;
				}

				loadData(view, features);
			},
			failure: function(r) {
				olmap.mask.hide();
				alert('Server error while loading coordinates');
			}
		});
    };

    loadData = function(view, features) {
		view = view || layer.core.view;
		features = features || layer.features.slice(0);

		var type = view.valueType,
			dataUrl = 'mapValues/' + gis.conf.finals.dimension[type].param + '.jsonp',
			indicator = gis.conf.finals.dimension.indicator,
			dataElement = gis.conf.finals.dimension.dataElement,
			period = gis.conf.finals.dimension.period,
			organisationUnit = gis.conf.finals.dimension.organisationUnit,
			params = {};

		params[type === indicator.id ? indicator.param : dataElement.param] = view[type].id;
		params[period.param] = view.period.id;
		params[organisationUnit.param] = view.parentOrganisationUnit.id;
		params.le = view.organisationUnitLevel.id;

		Ext.data.JsonP.request({
			url: gis.baseUrl + gis.conf.url.path_api + dataUrl,
			params: params,
			disableCaching: false,
			scope: this,
			success: function(r) {
				var values = r,
					featureMap = {},
					valueMap = {},
					newFeatures = [];

				if (values.length === 0) {
					alert('No aggregated data values found'); //todo //i18n
					olmap.mask.hide();
					return;
				}

				for (var i = 0; i < features.length; i++) {
					var iid = features[i].attributes.internalId;
					featureMap[iid] = true;
				}
				for (var i = 0; i < values.length; i++) {
					var iid = values[i].organisationUnitId,
						value = values[i].value;
					valueMap[iid] = value;
				}

				for (var i = 0; i < features.length; i++) {
					var feature = features[i],
						iid = feature.attributes.internalId;
					if (featureMap.hasOwnProperty(iid) && valueMap.hasOwnProperty(iid)) {
						feature.attributes.value = valueMap[iid];
						feature.attributes.label = feature.attributes.name + ' (' + feature.attributes.value + ')';
						newFeatures.push(feature);
					}
				}

				layer.removeFeatures(layer.features);
				layer.addFeatures(newFeatures);

				layer.core.featureStore.loadFeatures(layer.features.slice(0));

				loadLegend(view);
			}
		});
	};

	loadLegend = function(view) {
		view = view || layer.core.view;

		var options,
			predefined = gis.conf.finals.widget.legendtype_predefined,
			classificationMethod = mapfish.GeoStat.Distribution.CLASSIFY_WITH_BOUNDS,
			method = view.legendType === predefined ? classificationMethod : view.method,
			bounds,
			colors,
			names,
			legend,
			legends,
			fn;

		fn = function() {
			options = {
				indicator: gis.conf.finals.widget.value,
				method: method,
				numClasses: view.classes,
				bounds: bounds,
				colors: layer.core.getColors(view.colorLow, view.colorHigh),
				minSize: view.radiusLow,
				maxSize: view.radiusHigh
			};

			view.legendSet = view.legendSet || {};
			view.legendSet.names = names;
			layer.core.view = view;
			layer.core.colorInterpolation = colors;
			layer.core.applyClassification(options);

			afterLoad(view);
		};

		if (view.legendType === gis.conf.finals.widget.legendtype_predefined) {
				bounds = [];
				colors = [];
				names = [];

			Ext.Ajax.request({
				url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets/' + view.legendSet.id + '.json?links=false&paging=false',
				scope: this,
				success: function(r) {
					legends = Ext.decode(r.responseText).mapLegends;

					Ext.Array.sort(legends, function (a, b) {
						return a.startValue - b.startValue;
					});

					for (var i = 0; i < legends.length; i++) {
						if (bounds[bounds.length-1] !== legends[i].startValue) {
							if (bounds.length !== 0) {
								colors.push(new mapfish.ColorRgb(240,240,240));
								names.push('');
							}
							bounds.push(legends[i].startValue);
						}
						colors.push(new mapfish.ColorRgb());
						colors[colors.length - 1].setFromHex(legends[i].color);
						names.push(legends[i].name);
						bounds.push(legends[i].endValue);
					}

					fn();
				}
			});
		}
		else {
			fn();
		}
	};

	afterLoad = function(view) {

		// Legend
		gis.viewport.eastRegion.doLayout();
		layer.legendPanel.expand();

		// Layer
		layer.setLayerOpacity(view.opacity);

		if (layer.item) {
			layer.item.setValue(true);
		}

		// Filter
		if (layer.filterWindow && layer.filterWindow.isVisible()) {
			layer.filterWindow.filter();
		}

		// Gui
		if (loader.updateGui && Ext.isObject(layer.widget)) {
			layer.widget.setGui(view);
		}

		// Zoom
		if (loader.zoomToVisibleExtent) {
			olmap.zoomToVisibleExtent();
		}

		// Mask
		if (loader.hideMask) {
			olmap.mask.hide();
		}

		// Map callback
		if (loader.callBack) {
			loader.callBack(layer);
		}
		else {
			gis.map = null;
			if (gis.viewport.interpretationButton) {
				gis.viewport.interpretationButton.disable();
			}
		}
	};

	loader = {
		compare: false,
		updateGui: false,
		zoomToVisibleExtent: false,
		hideMask: false,
		callBack: null,
		load: function(view) {
			gis.olmap.mask.show();

			if (this.compare) {
				compareView(view, true);
			}
			else {
				loadOrganisationUnits(view);
			}
		},
		loadData: loadData,
		loadLegend: loadLegend
	};

	return loader;
};

GIS.core.LayerLoaderFacility = function(gis, layer) {
	var olmap = layer.map,
		compareView,
		loadOrganisationUnits,
		loadData,
		loadLegend,
		addCircles,
		afterLoad,
		loader;

	compareView = function(view, doExecute) {
		var src = layer.core.view;

		if (!src) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.organisationUnitGroupSet.id !== src.organisationUnitGroupSet.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.organisationUnitLevel.id !== src.organisationUnitLevel.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.parentOrganisationUnit.id !== src.parentOrganisationUnit.id) {
			if (doExecute) {
				loadOrganisationUnits(view);
			}
			return gis.conf.finals.widget.loadtype_organisationunit;
		}

		if (view.areaRadius !== src.areaRadius) {
			if (doExecute) {
				loadLegend(view);
			}
			return gis.conf.finals.widget.loadtype_legend;
		}

        gis.olmap.mask.hide();
	};

    loadOrganisationUnits = function(view) {
		Ext.data.JsonP.request({
			url: gis.baseUrl + gis.conf.url.path_gis + 'getGeoJsonFacilities.action',
			params: {
				parentId: view.parentOrganisationUnit.id,
				level: view.organisationUnitLevel.id
			},
			disableCaching: false,
			success: function(r) {
				var geojson = layer.core.decode(r),
					format = new OpenLayers.Format.GeoJSON(),
					features = gis.util.map.getTransformedFeatureArray(format.read(geojson));

				if (!Ext.isArray(features)) {
					olmap.mask.hide();
					alert('Coordinates are invalid');
					return;
				}

				if (!features.length) {
					olmap.mask.hide();
					alert('No valid coordinates found'); //todo //i18n
					return;
				}

				loadData(view, features);
			},
			failure: function(r) {
				olmap.mask.hide();
				alert('Server error while loading coordinates');
			}
		});
    };

    loadData = function(view, features) {
		view = view || layer.core.view;
		features = features || layer.features.slice(0);

		for (var i = 0; i < features.length; i++) {
			features[i].attributes.label = features[i].attributes.name;
		}

		layer.removeFeatures(layer.features);
		layer.addFeatures(features);

		layer.core.featureStore.loadFeatures(layer.features.slice(0));

		loadLegend(view);
	};

	loadLegend = function(view) {
		view = view || layer.core.view;

		var store = gis.store.groupsByGroupSet,
			options;

		store.proxy.url = gis.baseUrl + gis.conf.url.path_gis + 'getOrganisationUnitGroupsByGroupSet.action?id=' + view.organisationUnitGroupSet.id;
		store.load({
			scope: this,
			callback: function() {
				options = {
					indicator: view.organisationUnitGroupSet.name
				};

				layer.core.view = view;

				layer.core.applyClassification(options);

				addCircles(view);

				afterLoad(view);
			}
		});
	};

	addCircles = function(view) {
		var radius = view.areaRadius;

		if (layer.circleLayer) {
			layer.circleLayer.deactivateControls();
			layer.circleLayer = null;
		}
		if (Ext.isDefined(radius) && radius) {
			layer.circleLayer = GIS.app.CircleLayer(layer.features, radius);
		}
	};

	afterLoad = function(view) {

		// Legend
		gis.viewport.eastRegion.doLayout();
		layer.legendPanel.expand();

		// Layer
		if (layer.item) {
			layer.item.setValue(true, view.opacity);
		}
		else {
			layer.setLayerOpacity(view.opacity);
		}

		// Gui
		if (loader.updateGui && Ext.isObject(layer.widget)) {
			layer.widget.setGui(view);
		}

		// Zoom
		if (loader.zoomToVisibleExtent) {
			olmap.zoomToVisibleExtent();
		}

		// Mask
		if (loader.hideMask) {
			olmap.mask.hide();
		}

		// Map callback
		if (loader.callBack) {
			loader.callBack(layer);
		}
		else {
			gis.map = null;
			gis.viewport.interpretationButton.disable();
		}
	};

	loader = {
		compare: false,
		updateGui: false,
		zoomToVisibleExtent: false,
		hideMask: false,
		callBack: null,
		load: function(view) {
			gis.olmap.mask.show();

			if (this.compare) {
				compareView(view, true);
			}
			else {
				loadOrganisationUnits(view);
			}
		},
		loadData: loadData,
		loadLegend: loadLegend
	};

	return loader;
};

GIS.core.getInstance = function(config) {
	var gis = {};

	gis.baseUrl = config && config.baseUrl ? config.baseUrl : '../..';
	gis.el = config && config.el ? config.el : null;

	gis.conf = GIS.core.getConfigs();
	gis.util = GIS.core.getUtils(gis);
	gis.store = GIS.core.getStores(gis);
	gis.olmap = GIS.core.getOLMap(gis);
	gis.layer = GIS.core.getLayers(gis);

	for (var key in gis.layer) {
		if (gis.layer.hasOwnProperty(key)) {
			gis.olmap.addLayer(gis.layer[key]);
		}
	}

	return gis;
};

});
