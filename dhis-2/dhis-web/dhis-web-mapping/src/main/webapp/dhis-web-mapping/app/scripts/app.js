GIS.app = {};
GIS.app.init = {};

GIS.logg = [];

Ext.onReady( function() {
	Ext.Ajax.method = 'GET';
    Ext.QuickTips.init();

    Ext.override(Ext.LoadMask, {
		onHide: function() {
			this.callParent();
		}
	});

	document.body.oncontextmenu = function(){return false;};

	// Init

	var gis = GIS.core.getInstance();
	GIS.app.instances = [gis];

	GIS.app.getInits = function(r) {
		var init = Ext.decode(r.responseText);

		for (var i = 0; i < init.rootNodes.length; i++) {
			init.rootNodes[i].path = '/root/' + init.rootNodes[i].id;
		}

		return init;
	};

	GIS.app.getUtils = function() {
		var util = gis.util;

		util.map.getFeaturesByLayers = function(layers) {
			var a = [];
			for (var i = 0; i < layers.length; i++) {
				a = a.concat(layers[i].features);
			}
			return a;
		};

		util.map.getPointsByFeatures = function(features) {
			var a = [];
			for (var i = 0; i < features.length; i++) {
				if (features[i].geometry.CLASS_NAME === gis.conf.finals.openLayers.point_classname) {
					a.push(features[i]);
				}
			}
			return a;
		};

		util.map.getLonLatsByPoints = function(points) {
			var lonLat,
				point,
				a = [];
			for (var i = 0; i < points.length; i++) {
				point = points[i];
				lonLat = new OpenLayers.LonLat(point.geometry.x, point.geometry.y);
				a.push(lonLat);
			}
			return a;
		};

		util.map.hasVisibleFeatures = function() {
			var layers = util.map.getVisibleVectorLayers(),
				layer;

			if (layers.length) {
				for (var i = 0; i < layers.length; i++) {
					layer = layers[i];
					if (layer.features.length) {
						return true;
					}
				}
			}

			return false;
		};

		util.map.getLayersByType = function(layerType) {
			var layers = [];
			for (var i = 0; i < gis.olmap.layers.length; i++) {
				var layer = gis.olmap.layers[i];
				if (layer.layerType === layerType) {
					layers.push(layer);
				}
			}
			return layers;
		};

		util.map.addMapControl = function(name, fn) {
			var panel = GIS.app.MapControlPanel(name, fn);
			gis.olmap.addControl(panel);
			panel.div.className += ' ' + name;
			panel.div.childNodes[0].className += ' ' + name + 'Button';
		};

		util.map.getTransformedPointByXY = function(x, y) {
			var p = new OpenLayers.Geometry.Point(parseFloat(x), parseFloat(y));
			return p.transform(new OpenLayers.Projection('EPSG:4326'), new OpenLayers.Projection('EPSG:900913'));
		};

		util.map.getLonLatByXY = function(x, y) {
			var point = util.map.getTransformedPointByXY(x, y);
			return new OpenLayers.LonLat(point.x, point.y);
		};

		util.url = {};

		util.url.getUrlParam = function(s) {
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
		};

		util.svg = {};

		util.svg.merge = function(str, strArray) {
			if (strArray.length) {
				str = str || '<svg></svg>';
				for (var i = 0; i < strArray.length; i++) {
					str = str.replace('</svg>', '');
					strArray[i] = strArray[i].substring(strArray[i].indexOf('>') + 1);
					str += strArray[i];
				}
			}
			return str;
		};

		util.svg.getString = function(title, layers) {
			var svgArray = [],
				svg = '',
				namespace,
				titleSVG,
				legendSVG = '',
				scalelineSVG,
				x = 20,
				y = 35,
				center = gis.viewport.centerRegion,
				scalelineEl = Ext.get(Ext.query('.olControlScaleLineTop')[0]);

			if (!layers.length) {
				return false;
			}

			namespace = 'xmlns="http://www.w3.org/2000/svg"';

			svg = '<svg ' + namespace + ' width="' + center.getWidth() + '" height="' + center.getHeight() + '"></svg>';

			titleSVG = '<g id="title" style="display: block; visibility: visible;">' +
					   '<text id="title" x="' + x + '" y="' + y + '" font-size="18" font-weight="bold">' +
					   '<tspan>' + title + '</tspan></text></g>';

			y += 35;

			for (var i = layers.length - 1; i > 0; i--) {
				if (layers[i].id === gis.layer.facility.id) {
					layers.splice(i, 1);
					console.log(GIS.i18n.facility_layer_export_currently_not_supported);
				}
			}

			for (var i = 0; i < layers.length; i++) {
				var layer = layers[i],
					id = layer.id,
					legendConfig = layer.core.getLegendConfig(),
					imageLegendConfig = layer.core.getImageLegendConfig(),
					what,
					when,
					where,
					legend;

				// SVG
				svgArray.push(layer.div.innerHTML);

				// Legend
				if (id !== gis.layer.boundary.id && id !== gis.layer.facility.id) {
					what = '<g id="indicator" style="display: block; visibility: visible;">' +
						   '<text id="indicator" x="' + x + '" y="' + y + '" font-size="12">' +
						   '<tspan>' + legendConfig.what + '</tspan></text></g>';

					y += 15;

					when = '<g id="period" style="display: block; visibility: visible;">' +
						   '<text id="period" x="' + x + '" y="' + y + '" font-size="12">' +
						   '<tspan>' + legendConfig.when + '</tspan></text></g>';

					y += 15;

					where = '<g id="period" style="display: block; visibility: visible;">' +
						   '<text id="period" x="' + x + '" y="' + y + '" font-size="12">' +
						   '<tspan>' + legendConfig.where + '</tspan></text></g>';

					y += 8;

					legend = '<g>';

					for (var j = 0; j < imageLegendConfig.length; j++) {
						if (j !== 0) {
							y += 15;
						}

						legend += '<rect x="' + x + '" y="' + y + '" height="15" width="30" ' +
								  'fill="' + imageLegendConfig[j].color + '" stroke="#000000" stroke-width="1"/>';

						legend += '<text id="label" x="' + (x + 40) + '" y="' + (y + 12) + '" font-size="12">' +
								  '<tspan>' + imageLegendConfig[j].label + '</tspan></text>';
					}

					legend += '</g>';

					legendSVG += (what + when + where + legend);

					y += 50;
				}
			}

			// Scale line
			scalelineSVG = '<text x="' + (x + 3) + '" y="' + y + '" fill="#000">' + scalelineEl.dom.innerHTML + '</text>';

			y += 3;
			scalelineSVG += '<line x1="' + x + '" y1="' + y + '" x2="' + x + '" y2="' + (y + 3) + '" style="stroke:#000;stroke-width:1" />';
			scalelineSVG += '<line x1="' + (x + scalelineEl.getWidth()) + '" y1="' + y + '" x2="' + (x + scalelineEl.getWidth()) + '" y2="' + (y + 3) + '" style="stroke:#000;stroke-width:1" />';

			y += 3;
			scalelineSVG += '<line x1="' + x + '" y1="' + y + '" x2="' + (x + scalelineEl.getWidth()) + '" y2="' + y + '" style="stroke:#000;stroke-width:1" />';

			// Map
			if (svgArray.length) {
				svg = util.svg.merge(svg, svgArray);
			}

			svg = svg.replace('</svg>', (titleSVG + legendSVG + scalelineSVG) + '</svg>');

			return svg;
		};

		util.json = {};

		util.json.encodeString = function(str) {
			return Ext.isString(str) ? str.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'') : str;
		};

		util.json.decodeAggregatedValues = function(responseText) {
			responseText = Ext.decode(responseText);
			var values = [];

			for (var i = 0; i < responseText.length; i++) {
				values.push({
					oi: responseText[i][0],
					v: responseText[i][1]
				});
			}
			return values;
		};

		util.gui.window = {};

		util.gui.window.setPositionTopRight = function(window) {
			var center = gis.viewport.centerRegion;
			window.setPosition(gis.viewport.width - (window.width + 7), center.y + 8);
		};

		util.gui.window.setPositionTopLeft = function(window) {
			window.setPosition(4,35);
		};

		util.gui.window.addHideOnBlurHandler = function(w) {
			var el = Ext.get(Ext.query('.x-mask')[0]);

			el.on('click', function() {
				if (w.hideOnBlur) {
					w.hide();
				}
			});

			w.hasHideOnBlurHandler = true;
		};

		util.gui.window.addDestroyOnBlurHandler = function(w) {
			var el = Ext.get(Ext.query('.x-mask')[0]);

			el.on('click', function() {
				if (w.destroyOnBlur) {
					w.destroy();
				}
			});

			w.hasDestroyOnBlurHandler = true;
		};

		return util;
	};

	GIS.app.getStores = function() {
		var stores = gis.store;

		stores.indicatorGroups = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'indicatorGroups.json?links=false&paging=false',
				reader: {
					type: 'json',
					root: 'indicatorGroups'
				}
			},
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
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
						gis.util.gui.combo.setQueryMode(this.cmp, 'local');
					}
					this.sort('name', 'ASC');
				}
			}
		});

		stores.dataElementGroups = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'dataElementGroups.json?links=false&paging=false',
				reader: {
					type: 'json',
					root: 'dataElementGroups'
				}
			},
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
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
						gis.util.gui.combo.setQueryMode(this.cmp, 'local');
					}
					this.sort('name', 'ASC');
				}
			}
		});

		stores.periodTypes = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: gis.conf.period.periodTypes
		});

		stores.infrastructuralPeriodsByType = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_gis + 'getPeriodsByPeriodType.action',
				reader: {
					type: 'json',
					root: 'periods'
				},
				extraParams: {
					name: gis.init.systemSettings.infrastructuralPeriodType
				}
			},
			autoLoad: false,
			isLoaded: false,
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
				}
			}
		});

		stores.groupSets = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'organisationUnitGroupSets.json?paging=false&links=false',
				reader: {
					type: 'json',
					root: 'organisationUnitGroupSets'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		stores.groupsByGroupSet = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'symbol'],
			proxy: {
				type: 'ajax',
				url: '',
				noCache: false,
				reader: {
					type: 'json',
					root: 'organisationUnitGroups'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		stores.legendSets = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets.json?links=false&paging=false',
				reader: {
					type: 'json',
					root: 'mapLegendSets'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		stores.maps = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated', 'access'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'maps'
				}
			},
			isLoaded: false,
			pageSize: 10,
			page: 1,
			defaultUrl: gis.baseUrl + gis.conf.url.path_api + 'maps.json?viewClass=detailed&links=false',
			loadStore: function(url) {
				this.proxy.url = url || this.defaultUrl;

				this.load({
					params: {
						pageSize: this.pageSize,
						page: this.page
					}
				});
			},
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}

					this.sort('name', 'ASC');
				}
			}
		});

		return stores;
	};

	GIS.app.createExtensions = function() {

		Ext.define('Ext.ux.panel.LayerItemPanel', {
			extend: 'Ext.panel.Panel',
			alias: 'widget.layeritempanel',
			layout: 'column',
			layer: null,
			checkbox: null,
			numberField: null,
			imageUrl: null,
			text: null,
			width: 184,
			height: 22,
			value: false,
			opacity: 0.8,
			getValue: function() {
				return this.checkbox.getValue();
			},
			setValue: function(value, opacity) {
				this.checkbox.setValue(value);
				this.numberField.setDisabled(!value);
				this.layer.setVisibility(value);

				if (opacity === 0) {
					this.numberField.setValue(0);
					this.setOpacity(0.01);
				}
				else if (opacity > 0) {
					this.numberField.setValue(opacity * 100);
					this.setOpacity(opacity);
				}
				else {
					this.numberField.setValue(this.opacity * 100);
					this.setOpacity(this.opacity);
				}
			},
			getOpacity: function() {
				return this.opacity;
			},
			setOpacity: function(opacity) {
				this.opacity = opacity === 0 ? 0.01 : opacity;
				this.layer.setLayerOpacity(this.opacity);
			},
			disableItem: function() {
				this.checkbox.setValue(false);
				this.numberField.disable();
				this.layer.setVisibility(false);
			},
			updateItem: function(value) {
				this.numberField.setDisabled(!value);
				this.layer.setVisibility(value);

				if (value && this.layer.layerType === gis.conf.finals.layer.type_base) {
					gis.olmap.setBaseLayer(this.layer);
				}

				if (this.layer.circleLayer) {
					this.layer.circleLayer.setVisibility(value);
				}
			},
			initComponent: function() {
				var that = this,
					image;

				this.checkbox = Ext.create('Ext.form.field.Checkbox', {
					width: 14,
					checked: this.value,
					listeners: {
						change: function(chb, value) {
							if (value && that.layer.layerType === gis.conf.finals.layer.type_base) {
								var layers = gis.util.map.getLayersByType(gis.conf.finals.layer.type_base);

								for (var i = 0; i < layers.length; i++) {
									if (layers[i] !== that.layer) {
										layers[i].item.checkbox.suppressChange = true;
										layers[i].item.disableItem();
									}
								}
							}
							that.updateItem(value);

							gis.viewport.downloadButton.xable();
						}
					}
				});

				image = Ext.create('Ext.Img', {
					width: 14,
					height: 14,
					src: this.imageUrl
				});

				this.numberField = Ext.create('Ext.form.field.Number', {
					cls: 'gis-numberfield',
					width: 47,
					height: 18,
					minValue: 0,
					maxValue: 100,
					value: this.opacity * 100,
					allowBlank: false,
					disabled: this.numberFieldDisabled,
					listeners: {
						change: function() {
							var value = this.getValue(),
								opacity = value === 0 ? 0.01 : value/100;

							that.setOpacity(opacity);
						}
					}
				});

				this.items = [
					{
						width: this.checkbox.width + 6,
						items: this.checkbox
					},
					{
						width: image.width + 5,
						items: image,
						bodyStyle: 'padding-top: 4px'
					},
					{
						width: 98,
						html: this.text,
						bodyStyle: 'padding-top: 4px'
					},
					{
						width: this.numberField.width,
						items: this.numberField
					}
				];

				this.layer.setOpacity(this.opacity);

				this.callParent();
			}
		});

		Ext.define('Ext.ux.panel.CheckTextNumber', {
			extend: 'Ext.panel.Panel',
			alias: 'widget.checktextnumber',
			layout: 'column',
			layer: null,
			checkbox: null,
			text: null,
			numberField: null,
			width: 184,
			height: 22,
			value: false,
			number: 5000,
			getValue: function() {
				return this.checkbox.getValue();
			},
			getNumber: function() {
				return this.numberField.getValue();
			},
			setValue: function(value, number) {
				if (value) {
					this.checkbox.setValue(value);
				}
				if (number) {
					this.numberField.setValue(number);
				}
			},
			enable: function() {
				this.numberField.enable();
			},
			disable: function() {
				this.numberField.disable();
			},
			reset: function() {
				this.checkbox.setValue(false);
				this.numberField.setValue(this.number);
				this.numberField.disable();
			},
			initComponent: function() {
				var that = this;

				this.checkbox = Ext.create('Ext.form.field.Checkbox', {
					width: 191,
					boxLabel: this.text,
					checked: this.value,
					disabled: this.disabled,
					listeners: {
						change: function(chb, value) {
							if (value) {
								that.enable();
							}
							else {
								that.disable();
							}
						}
					}
				});

				this.numberField = Ext.create('Ext.form.field.Number', {
					cls: 'gis-numberfield',
					fieldStyle: 'border-top-left-radius: 1px; border-bottom-left-radius: 1px',
					style: 'padding-bottom: 3px',
					width: 65,
					height: 21,
					minValue: 0,
					maxValue: 999999,
					value: this.number,
					allowBlank: false,
					disabled: true
				});

				this.items = [
					{
						width: this.checkbox.width + 6,
						items: this.checkbox
					},
					{
						width: this.numberField.width,
						items: this.numberField
					}
				];

				this.callParent();
			}
		});
	}();

    // Objects

    GIS.app.LayerMenu = function(layer, cls) {
		var items = [],
			item;

		item = {
			text: GIS.i18n.edit_layer,
			iconCls: 'gis-menu-item-icon-edit',
			cls: 'gis-menu-item-first',
			alwaysEnabled: true,
			handler: function() {
				layer.window.show();
			}
		};
		items.push(item);

		items.push({
			xtype: 'menuseparator',
			alwaysEnabled: true
		});

		item = {
			text: GIS.i18n.labels,
			iconCls: 'gis-menu-item-icon-labels',
			handler: function() {
				if (layer.labelWindow) {
					layer.labelWindow.show();
				}
				else {
					layer.labelWindow = GIS.app.LabelWindow(layer);
					layer.labelWindow.show();
				}
			}
		};
		items.push(item);

		if (layer.id !== gis.layer.boundary.id) {
			item = {
				text: GIS.i18n.filter + '..',
				iconCls: 'gis-menu-item-icon-filter',
				handler: function() {
					if (layer.filterWindow) {
						if (layer.filterWindow.isVisible()) {
							return;
						}
						else {
							layer.filterWindow.destroy();
						}
					}

					layer.filterWindow = layer.id === gis.layer.facility.id ?
						GIS.app.FilterWindowFacility(layer) : GIS.app.FilterWindow(layer);
					layer.filterWindow.show();
				}
			};
			items.push(item);
		}

		item = {
			text: GIS.i18n.search,
			iconCls: 'gis-menu-item-icon-search',
			handler: function() {
				if (layer.searchWindow) {
					if (layer.searchWindow.isVisible()) {
						return;
					}
					else {
						layer.searchWindow.destroy();
					}
				}

				layer.searchWindow = GIS.app.SearchWindow(layer);
				layer.searchWindow.show();
			}
		};
		items.push(item);

		items.push({
			xtype: 'menuseparator',
			alwaysEnabled: true
		});

		item = {
			text: GIS.i18n.close,
			iconCls: 'gis-menu-item-icon-clear',
			handler: function() {
				gis.viewport.interpretationButton.disable();

				layer.core.reset();
			}
		};
		items.push(item);

		return Ext.create('Ext.menu.Menu', {
			shadow: false,
			showSeparator: false,
			enableItems: function() {
				Ext.each(this.items.items, function(item) {
					item.enable();
				});
			},
			disableItems: function() {
				Ext.Array.each(this.items.items, function(item) {
					if (!item.alwaysEnabled) {
						item.disable();
					}
				});
			},
			items: items,
			listeners: {
				afterrender: function() {
					this.getEl().addCls('gis-toolbar-btn-menu');
					if (cls) {
						this.getEl().addCls(cls);
					}
				},
				show: function() {
					if (layer.features.length) {
						this.enableItems();
					}
					else {
						this.disableItems();
					}

					this.doLayout(); // show menu bug workaround
				}
			}
		});
	};

	GIS.app.LayersPanel = function() {
		var layers = gis.layer,
			layer,
			items = [],
			item,
			panel,
			visibleLayer = window.google ? layers.googleStreets : layers.openStreetMap,
			orderedLayers = gis.olmap.layers.reverse();

		for (var i = 0; i < orderedLayers.length; i++) {
			layer = orderedLayers[i];

			item = Ext.create('Ext.ux.panel.LayerItemPanel', {
				cls: 'gis-container-inner',
				height: 23,
				layer: layer,
				text: layer.name,
				imageUrl: 'images/' + layer.id + '_14.png',
				value: layer.id === visibleLayer.id ? true : false,
				opacity: layer.layerOpacity,
				numberFieldDisabled: layer.id !== visibleLayer.id
			});

			layer.item = item;
			items.push(layer.item);
		}

		if (window.google) {
			visibleLayer.item.setValue(true);
		}

        panel = Ext.create('Ext.panel.Panel', {
			renderTo: 'layerItems',
			layout: 'fit',
			cls: 'gis-container-inner',
			items: {
				cls: 'gis-container-inner',
				items: items
			}
		});

		return panel;
	};

	GIS.app.WidgetWindow = function(layer) {
		return Ext.create('Ext.window.Window', {
			autoShow: true,
			title: layer.name,
			layout: 'fit',
			iconCls: 'gis-window-title-icon-' + layer.id,
            bodyStyle: 'padding:5px',
			cls: 'gis-container-default',
			closeAction: 'hide',
			width: gis.conf.layout.widget.window_width,
			resizable: false,
			isRendered: false,
			items: layer.widget,
			bbar: [
				'->',
				{
					text: GIS.i18n.update,
					handler: function() {
						var view = layer.widget.getView();

						if (view) {
							var loader = layer.core.getLoader();
							loader.compare = (layer.id !== gis.layer.facility.id),
							loader.zoomToVisibleExtent = true;
							loader.hideMask = true;
							loader.load(view);
						}
					}
				}
			],
			listeners: {
				show: function() {
					if (!this.isRendered) {
						this.isRendered = true;
						this.hide();
					}
					else {
						gis.util.gui.window.setPositionTopLeft(this);
					}
				}
			}
		});
	};

	GIS.app.SearchWindow = function(layer) {
		var data = [],
			store = layer.core.featureStore,
			button,
			window;

		for (var i = 0; i < layer.features.length; i++) {
			data.push([layer.features[i].data.id, layer.features[i].data.name]);
		}

		if (!data.length) {
			GIS.logg.push([data, layer.id + '.search.data: feature ids/names']);
			alert(GIS.i18n.layer + ' ' + GIS.i18n.has_no_orgunits);
			return;
		}

		button = Ext.create('Ext.ux.button.ColorButton', {
			width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width,
			value: '0000ff'
		});

		window = Ext.create('Ext.window.Window', {
			title: GIS.i18n.organisationunit_search,
			layout: 'fit',
			iconCls: 'gis-window-title-icon-search',
			cls: 'gis-container-default',
			width: gis.conf.layout.tool.window_width,
			resizable: false,
			height: 400,
			items: [
				{
					cls: 'gis-container-inner',
					items: [
						{
							layout: 'column',
							cls: 'gis-container-inner',
							items: [
								{
									cls: 'gis-panel-html-label',
									html: GIS.i18n.highlight_color + ':',
									width: gis.conf.layout.tool.itemlabel_width
								},
								button
							]
						},
						{
							cls: 'gis-panel-html-separator'
						},
						{
							layout: 'column',
							cls: 'gis-container-inner',
							items: [
								{
									cls: 'gis-panel-html-label',
									html: GIS.i18n.text_filter + ':',
									width: gis.conf.layout.tool.itemlabel_width
								},
								{
									xtype: 'textfield',
									cls: 'gis-textfield',
									width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width,
									enableKeyEvents: true,
									listeners: {
										keyup: function() {
											store.clearFilter();
											if (this.getValue()) {
												store.filter('name', this.getValue());
											}
											store.sortStore();
										}
									}
								}
							]
						},
						{
							xtype: 'grid',
							cls: 'gis-grid',
							height: 290,
							width: gis.conf.layout.tool.item_width,
							scroll: 'vertical',
							hideHeaders: true,
							columns: [{
								id: 'name',
								text: 'Organisation units',
								dataIndex: 'name',
								sortable: false,
								width: gis.conf.layout.tool.item_width
							}],
							store: layer.core.featureStore,
							listeners: {
								select: function(grid, record) {
									var feature = layer.getFeaturesByAttribute('id', record.data.id)[0],
										color = button.getValue(),
										symbolizer;

									layer.redraw();

									if (feature.geometry.CLASS_NAME === gis.conf.finals.openLayers.point_classname) {
										symbolizer = new OpenLayers.Symbolizer.Point({
											pointRadius: 6,
											fillColor: '#' + color,
											strokeWidth: 1
										});
									}
									else {
										symbolizer = new OpenLayers.Symbolizer.Polygon({
											strokeColor: '#' + color,
											fillColor: '#' + color
										});
									}

									layer.drawFeature(feature, symbolizer);
								}
							}
						}
					]
				}
			],
			listeners: {
				render: function() {
					gis.util.gui.window.setPositionTopLeft(this);
					store.sortStore();
				},
				destroy: function() {
					layer.redraw();
				}
			}
		});

		return window;
	};

	GIS.app.FilterWindow = function(layer) {
		var lowerNumberField,
			greaterNumberField,
			lt,
			gt,
			filter,
			window;

		greaterNumberField = Ext.create('Ext.form.field.Number', {
			width: gis.conf.layout.tool.itemlabel_width,
			value: parseInt(layer.core.minVal),
			listeners: {
				change: function() {
					gt = this.getValue();
				}
			}
		});

		lowerNumberField = Ext.create('Ext.form.field.Number', {
			width: gis.conf.layout.tool.itemlabel_width,
			value: parseInt(layer.core.maxVal) + 1,
			listeners: {
				change: function() {
					lt = this.getValue();
				}
			}
		});

        filter = function() {
			var cache = layer.core.featureStore.features.slice(0),
				features = [];

            if (!gt && !lt) {
                features = cache;
            }
            else if (gt && lt) {
                for (var i = 0; i < cache.length; i++) {
                    if (gt < lt && (cache[i].attributes.value > gt && cache[i].attributes.value < lt)) {
                        features.push(cache[i]);
                    }
                    else if (gt > lt && (cache[i].attributes.value > gt || cache[i].attributes.value < lt)) {
                        features.push(cache[i]);
                    }
                    else if (gt === lt && cache[i].attributes.value === gt) {
                        features.push(cache[i]);
                    }
                }
            }
            else if (gt && !lt) {
                for (var i = 0; i < cache.length; i++) {
                    if (cache[i].attributes.value > gt) {
                        features.push(cache[i]);
                    }
                }
            }
            else if (!gt && lt) {
                for (var i = 0; i < cache.length; i++) {
                    if (cache[i].attributes.value < lt) {
                        features.push(cache[i]);
                    }
                }
            }

            layer.removeAllFeatures();
            layer.addFeatures(features);
        };

		window = Ext.create('Ext.window.Window', {
			title: 'Filter by value',
			iconCls: 'gis-window-title-icon-filter',
			cls: 'gis-container-default',
			width: gis.conf.layout.tool.window_width,
			resizable: false,
			filter: filter,
			items: {
				layout: 'fit',
				cls: 'gis-container-inner',
				items: [
					{
						cls: 'gis-container-inner',
						html: '<b>Show</b> organisation units with values..'
					},
					{
						cls: 'gis-panel-html-separator'
					},
					{
						cls: 'gis-panel-html-separator'
					},
					{
						layout: 'column',
						height: 22,
						cls: 'gis-container-inner',
						items: [
							{
								cls: 'gis-panel-html-label',
								html: 'Greater than:',
								width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width
							},
							greaterNumberField
						]
					},
					{
						cls: 'gis-panel-html-separator'
					},
					{
						layout: 'column',
						height: 22,
						cls: 'gis-container-inner',
						items: [
							{
								cls: 'gis-panel-html-label',
								html: 'And/or lower than:',
								width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width
							},
							lowerNumberField
						]
					}
				]
			},
			bbar: [
				'->',
				{
					xtype: 'button',
					text: GIS.i18n.update,
					handler: function() {
						filter();
					}
				}
			],
			listeners: {
				render: function() {
					gis.util.gui.window.setPositionTopLeft(this);
				},
				destroy: function() {
					layer.removeAllFeatures();
					layer.addFeatures(layer.core.featureStore.features);
				}
			}
		});

		return window;
	};

	GIS.app.FilterWindowFacility = function(layer) {
		var window,
			multiSelect,
			button,
			filter,
			selection,
			features = [],
			coreFeatures = layer.core.featureStore.features.slice(0),
			groupSetName = layer.core.view.organisationUnitGroupSet.name,
			store = gis.store.groupsByGroupSet;

		filter = function() {
			features = [];

			if (!selection.length || !selection[0]) {
				features = coreFeatures;
			}
			else {
				for (var i = 0; i < coreFeatures.length; i++) {
					for (var j = 0; j < selection.length; j++) {
						if (coreFeatures[i].attributes[groupSetName] === selection[j]) {
							features.push(coreFeatures[i]);
						}
					}
				}
			}

			layer.removeAllFeatures();
			layer.addFeatures(features);
		};

		multiSelect = Ext.create('Ext.ux.form.MultiSelect', {
			hideLabel: true,
			dataFields: ['id', 'name'],
			valueField: 'name',
			displayField: 'name',
			width: 200,
			height: 300,
			store: store
		});

		button = Ext.create('Ext.button.Button', {
			text: 'Filter',
			handler: function() {
				selection = multiSelect.getValue();
				filter();
			}
		});

		window = Ext.create('Ext.window.Window', {
			title: 'Filter by value',
			iconCls: 'gis-window-title-icon-filter',
			cls: 'gis-container-default',
			resizable: false,
			filter: filter,
			items: multiSelect,
			bbar: [
				'->',
				button
			],
			listeners: {
				render: function() {
					gis.util.gui.window.setPositionTopLeft(this);
				},
				destroy: function() {
					layer.removeAllFeatures();
					layer.addFeatures(coreFeatures);
				}
			}
		});

		return window;
	};

	GIS.app.LabelWindow = function(layer) {
		var fontSize,
			strong,
			italic,
			color,
			getValues,
			updateLabels,
			window;

		fontSize = Ext.create('Ext.form.field.Number', {
			width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width,
			allowDecimals: false,
			minValue: 8,
			value: 13,
			emptyText: 13,
			listeners: {
				change: function() {
					updateLabels();
				}
			}
		});

		strong = Ext.create('Ext.form.field.Checkbox', {
			listeners: {
				change: function() {
					updateLabels();
				}
			}
		});

		italic = Ext.create('Ext.form.field.Checkbox', {
			listeners: {
				change: function() {
					updateLabels();
				}
			}
		});

		button = Ext.create('Ext.ux.button.ColorButton', {
			width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width,
			value: '0000ff'
		});

		color = Ext.create('Ext.ux.button.ColorButton', {
			width: gis.conf.layout.tool.item_width - gis.conf.layout.tool.itemlabel_width,
			value: '000000',
			menuHandler: function() {
				updateLabels();
			}
		});

		getLabelConfig = function() {
			return {
				fontSize: fontSize.getValue(),
				strong: strong.getValue(),
				italic: italic.getValue(),
				color: color.getValue()
			};
		};

		updateLabels = function() {
			if (layer.hasLabels) {
				layer.styleMap = GIS.core.StyleMap(layer.id, getLabelConfig());

				var loader = layer.core.getLoader();
				loader.hideMask = true;
				loader.loadLegend();
			}
		};

		window = Ext.create('Ext.window.Window', {
			title: GIS.i18n.labels,
			iconCls: 'gis-window-title-icon-labels',
			cls: 'gis-container-default',
			width: gis.conf.layout.tool.window_width,
			resizable: false,
			closeAction: 'hide',
			items: {
				layout: 'fit',
				cls: 'gis-container-inner',
				items: [
					//{
						//layout: 'column',
						//cls: 'gis-container-inner',
						//items: [
							//{
								//cls: 'gis-panel-html-label',
								//html: GIS.i18n.font_size,
								//width: gis.conf.layout.tool.itemlabel_width
							//},
							//fontSize
						//]
					//},
					{
						layout: 'column',
						cls: 'gis-container-inner',
						items: [
							{
								cls: 'gis-panel-html-label',
								html: '<b>' + GIS.i18n.bold_ + '</b>:',
								width: gis.conf.layout.tool.itemlabel_width
							},
							strong
						]
					},
					{
						layout: 'column',
						cls: 'gis-container-inner',
						items: [
							{
								cls: 'gis-panel-html-label',
								html: '<i>' + GIS.i18n.italic + '</i>:',
								width: gis.conf.layout.tool.itemlabel_width
							},
							italic
						]
					},
					{
						layout: 'column',
						cls: 'gis-container-inner',
						items: [
							{
								cls: 'gis-panel-html-label',
								html: GIS.i18n.color + ':',
								width: gis.conf.layout.tool.itemlabel_width
							},
							color
						]
					}
				]
			},
			bbar: [
				'->',
				{
					xtype: 'button',
					text: GIS.i18n.showhide,
					handler: function() {
						if (layer.hasLabels) {
							layer.hasLabels = false;
							layer.styleMap = GIS.core.StyleMap(layer.id);
						}
						else {
							layer.hasLabels = true;
							layer.styleMap = GIS.core.StyleMap(layer.id, getLabelConfig());
						}

						var loader = layer.core.getLoader();
						loader.hideMask = true;
						loader.loadLegend();
					}
				}
			],
			listeners: {
				render: function() {
					gis.util.gui.window.setPositionTopLeft(this);
				}
			}
		});

		return window;
	};

	GIS.app.SharingWindow = function(sharing) {

		// Objects
		var UserGroupRow,

		// Functions
			getBody,

		// Components
			userGroupStore,
			userGroupField,
			userGroupButton,
			userGroupRowContainer,
			publicGroup,
			window;

		UserGroupRow = function(obj, isPublicAccess, disallowPublicAccess) {
			var getData,
				store,
				getItems,
				combo,
				getAccess,
				panel;

			getData = function() {
				var data = [
					{id: 'r-------', name: GIS.i18n.can_view},
					{id: 'rw------', name: GIS.i18n.can_edit_and_view}
				];

				if (isPublicAccess) {
					data.unshift({id: '-------', name: GIS.i18n.none});
				}

				return data;
			}

			store = Ext.create('Ext.data.Store', {
				fields: ['id', 'name'],
				data: getData()
			});

			getItems = function() {
				var items = [];

				combo = Ext.create('Ext.form.field.ComboBox', {
					fieldLabel: isPublicAccess ? GIS.i18n.public_access : obj.name,
					labelStyle: 'color:#333',
					cls: 'gis-combo',
					width: 380,
					labelWidth: 250,
					queryMode: 'local',
					valueField: 'id',
					displayField: 'name',
					labelSeparator: null,
					editable: false,
					disabled: !!disallowPublicAccess,
					value: obj.access || 'rw------',
					store: store
				});

				items.push(combo);

				if (!isPublicAccess) {
					items.push(Ext.create('Ext.Img', {
						src: 'images/grid-delete_16.png',
						style: 'margin-top:2px; margin-left:7px',
						overCls: 'pointer',
						width: 16,
						height: 16,
						listeners: {
							render: function(i) {
								i.getEl().on('click', function(e) {
									i.up('panel').destroy();
									window.doLayout();
								});
							}
						}
					}));
				}

				return items;
			};

			getAccess = function() {
				return {
					id: obj.id,
					name: obj.name,
					access: combo.getValue()
				};
			};

			panel = Ext.create('Ext.panel.Panel', {
				layout: 'column',
				bodyStyle: 'border:0 none',
				getAccess: getAccess,
				items: getItems()
			});

			return panel;
		};

		getBody = function() {
			var body = {
				object: {
					id: sharing.object.id,
					name: sharing.object.name,
					publicAccess: publicGroup.down('combobox').getValue(),
					user: {
						id: gis.init.user.id,
						name: gis.init.user.name
					}
				}
			};

			if (userGroupRowContainer.items.items.length > 1) {
				body.object.userGroupAccesses = [];
				for (var i = 1, item; i < userGroupRowContainer.items.items.length; i++) {
					item = userGroupRowContainer.items.items[i];
					body.object.userGroupAccesses.push(item.getAccess());
				}
			}

			return body;
		};

		// Initialize
		userGroupStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'sharing/search',
				reader: {
					type: 'json',
					root: 'userGroups'
				}
			}
		});

		userGroupField = Ext.create('Ext.form.field.ComboBox', {
			valueField: 'id',
			displayField: 'name',
			emptyText: GIS.i18n.search_for_user_groups,
			queryParam: 'key',
			queryDelay: 200,
			minChars: 1,
			hideTrigger: true,
			fieldStyle: 'height:26px; padding-left:6px; border-radius:1px; font-size:11px',
			style: 'margin-bottom:5px',
			width: 380,
			store: userGroupStore,
			listeners: {
				beforeselect: function(cb) { // beforeselect instead of select, fires regardless of currently selected item
					userGroupButton.enable();
				},
				afterrender: function(cb) {
					cb.inputEl.on('keyup', function() {
						userGroupButton.disable();
					});
				}
			}
		});

		userGroupButton = Ext.create('Ext.button.Button', {
			text: '+',
			style: 'margin-left:2px; padding-right:4px; padding-left:4px; border-radius:1px',
			disabled: true,
			height: 26,
			handler: function(b) {
				userGroupRowContainer.add(UserGroupRow({
					id: userGroupField.getValue(),
					name: userGroupField.getRawValue(),
					access: 'r-------'
				}));

				userGroupField.clearValue();
				b.disable();
			}
		});

		userGroupRowContainer = Ext.create('Ext.container.Container', {
			bodyStyle: 'border:0 none'
		});

		publicGroup = userGroupRowContainer.add(UserGroupRow({
			id: sharing.object.id,
			name: sharing.object.name,
			access: sharing.object.publicAccess
		}, true, !sharing.meta.allowPublicAccess));

		if (Ext.isArray(sharing.object.userGroupAccesses)) {
			for (var i = 0, userGroupRow; i < sharing.object.userGroupAccesses.length; i++) {
				userGroupRow = UserGroupRow(sharing.object.userGroupAccesses[i]);
				userGroupRowContainer.add(userGroupRow);
			}
		}

		window = Ext.create('Ext.window.Window', {
			title: 'Sharing settings',
			bodyStyle: 'padding:8px 8px 3px; background-color:#fff',
			width: 434,
			resizable: false,
			modal: true,
			destroyOnBlur: true,
			items: [
				{
					html: sharing.object.name,
					bodyStyle: 'border:0 none; font-weight:bold; color:#333',
					style: 'margin-bottom:8px'
				},
				{
					xtype: 'container',
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						userGroupField,
						userGroupButton
					]
				},
				userGroupRowContainer
			],
			bbar: [
				'->',
				{
					text: 'Save',
					handler: function() {
						Ext.Ajax.request({
							url: gis.baseUrl + gis.conf.url.path_api + 'sharing?type=map&id=' + sharing.object.id,
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							params: Ext.encode(getBody())
						});

						window.destroy();
					}
				}
			],
			listeners: {
				show: function(w) {
					var pos = gis.viewport.favoriteWindow.getPosition();
					w.setPosition(pos[0] + 5, pos[1] + 5);
				}
			}
		});

		return window;
	};

    GIS.app.MapControlPanel = function(name, fn) {
		var button,
			panel;

		button = new OpenLayers.Control.Button({
			displayClass: 'olControlButton',
			trigger: function() {
				fn.call(gis.olmap);
			}
		});

		panel = new OpenLayers.Control.Panel({
			defaultControl: button
		});

		panel.addControls([button]);

		return panel;
	};

	GIS.app.FavoriteWindow = function() {

		// Objects
		var NameWindow,

		// Instances
			nameWindow,

		// Components
			addButton,
			searchTextfield,
			grid,
			prevButton,
			nextButton,
			tbar,
			bbar,
			info,
			nameTextfield,
			createButton,
			updateButton,
			cancelButton,
			favoriteWindow,

		// Vars
			windowWidth = 500,
			windowCmpWidth = windowWidth - 22;

		gis.store.maps.on('load', function(store, records) {
			var pager = store.proxy.reader.jsonData.pager;

			info.setText('Page ' + pager.page + ' of ' + pager.pageCount);

			prevButton.enable();
			nextButton.enable();

			if (pager.page === 1) {
				prevButton.disable();
			}

			if (pager.page === pager.pageCount) {
				nextButton.disable();
			}
		});

		NameWindow = function(id) {
			var window,
				record = gis.store.maps.getById(id);

			nameTextfield = Ext.create('Ext.form.field.Text', {
				height: 26,
				width: 371,
				fieldStyle: 'padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
				style: 'margin-bottom:0',
				emptyText: 'Favorite name',
				value: id ? record.data.name : '',
				listeners: {
					afterrender: function() {
						this.focus();
					}
				}
			});

			createButton = Ext.create('Ext.button.Button', {
				text: GIS.i18n.create,
				handler: function() {
					var name = nameTextfield.getValue(),
						layers = gis.util.map.getVisibleVectorLayers(),
						layer,
						lonlat = gis.olmap.getCenter(),
						views = [],
						view,
						map;

					if (layers.length) {
						if (name) {
							for (var i = 0; i < layers.length; i++) {
								layer = layers[i];
								view = layer.widget.getView();

								// add
								view.layer = layer.id;

								// remove
								delete view.periodType;
								views.push(view);
							}

							map = {
								name: name,
								longitude: lonlat.lon,
								latitude: lonlat.lat,
								zoom: gis.olmap.getZoom(),
								mapViews: views,
								user: {
									id: 'currentUser'
								}
							};

							Ext.Ajax.request({
								url: gis.baseUrl + gis.conf.url.path_api + 'maps/',
								method: 'POST',
								headers: {'Content-Type': 'application/json'},
								params: Ext.encode(map),
								success: function(r) {
									var id = r.getAllResponseHeaders().location.split('/').pop();

									gis.store.maps.loadStore();

									gis.viewport.interpretationButton.enable();

									window.destroy();
								}
							});
						}
						else {
							alert('Please enter a name');
						}
					}
					else {
						alert('Please create a map first');
					}
				}
			});

			updateButton = Ext.create('Ext.button.Button', {
				text: GIS.i18n.update,
				handler: function() {
					var name = nameTextfield.getValue();

					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_gis + 'renameMap.action?id=' + id + '&name=' + name + '&user=true',
						success: function() {
							gis.store.maps.loadStore();

							window.destroy();
						}
					});
				}
			});

			cancelButton = Ext.create('Ext.button.Button', {
				text: GIS.i18n.cancel,
				handler: function() {
					window.destroy();
				}
			});

			window = Ext.create('Ext.window.Window', {
				title: id ? 'Rename favorite' : 'Create new favorite',
				iconCls: 'gis-window-title-icon-favorite',
				bodyStyle: 'padding:2px; background:#fff',
				resizable: false,
				modal: true,
				items: nameTextfield,
				bbar: [
					cancelButton,
					'->',
					id ? updateButton : createButton
				],
				listeners: {
					show: function() {
						this.setPosition(favoriteWindow.x + 14, favoriteWindow.y + 67);
					}
				}
			});

			return window;
		};

		addButton = Ext.create('Ext.button.Button', {
			text: GIS.i18n.add_new,
			width: 67,
			height: 26,
			style: 'border-radius: 1px;',
			menu: {},
			handler: function() {
				nameWindow = new NameWindow(null, 'create');
				nameWindow.show();
			}
		});

		searchTextfield = Ext.create('Ext.form.field.Text', {
			width: windowCmpWidth - addButton.width - 11,
			height: 26,
			fieldStyle: 'padding-right: 0; padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
			emptyText: GIS.i18n.search_for_favorites,
			enableKeyEvents: true,
			currentValue: '',
			listeners: {
				keyup: function() {
					if (this.getValue() !== this.currentValue) {
						this.currentValue = this.getValue();

						var value = this.getValue(),
							url = value ? gis.baseUrl + gis.conf.url.path_api +  'maps/query/' + value + '.json?links=false' : null,
							store = gis.store.maps;

						store.page = 1;
						store.loadStore(url);
					}
				}
			}
		});

		prevButton = Ext.create('Ext.button.Button', {
			text: GIS.i18n.prev,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? gis.baseUrl + gis.conf.url.path_api +  'maps/query/' + value + '.json?links=false' : null,
					store = gis.store.maps;

				store.page = store.page <= 1 ? 1 : store.page - 1;
				store.loadStore(url);
			}
		});

		nextButton = Ext.create('Ext.button.Button', {
			text: GIS.i18n.next,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? gis.baseUrl + gis.conf.url.path_api +  'maps/query/' + value + '.json?links=false' : null,
					store = gis.store.maps;

				store.page = store.page + 1;
				store.loadStore(url);
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'gis-label-info',
			width: 300,
			height: 22
		});

		grid = Ext.create('Ext.grid.Panel', {
			cls: 'gis-grid',
			scroll: false,
			hideHeaders: true,
			columns: [
				{
					dataIndex: 'name',
					sortable: false,
					width: windowCmpWidth - 108,
					renderer: function(value, metaData, record) {
						var fn = function() {
							var element = Ext.get(record.data.id);

							if (element) {
								element = element.parent('td');
								element.addClsOnOver('link');
								element.load = function() {
									favoriteWindow.hide();
									gis.map = {id: record.data.id};
									GIS.core.MapLoader(gis).load();
								};
								element.dom.setAttribute('onclick', 'Ext.get(this).load();');
							}
						};

							//var el = Ext.get(record.data.id);
							//if (el) {
								//el = el.parent('td');
								//el.addClsOnOver('link');
								//el.gis = gis;
								//el.map = {id: record.data.id};
								//el.dom.setAttribute('onclick', 'Ext.get(this).gis.map = Ext.get(this).map; GIS.core.MapLoader(Ext.get(this).gis).load();');
							//}
						//};

						Ext.defer(fn, 100);

						return '<div id="' + record.data.id + '">' + value + '</div>';
					}
				},
				{
					xtype: 'actioncolumn',
					sortable: false,
					width: 100,
					items: [
						{
							iconCls: 'gis-grid-row-icon-edit',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-edit' + (!record.data.access.update ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);

								if (record.data.access.update) {
									nameWindow = new NameWindow(record.data.id);
									nameWindow.show();
								}
							}
						},
						{
							iconCls: 'gis-grid-row-icon-overwrite',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-overwrite' + (!record.data.access.update ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									layers,
									layer,
									lonlat,
									views,
									view,
									map,
									message;

								if (record.data.access.update) {
									layers = gis.util.map.getVisibleVectorLayers();
									message = 'Overwrite favorite?\n\n' + record.data.name;

									if (layers.length) {
										if (confirm(message)) {
											lonlat = gis.olmap.getCenter();
											views = [];

											for (var i = 0; i < layers.length; i++) {
												layer = layers[i];
												view = layer.core.view;

												// add
												view.layer = layer[record.data.id];

												// remove
												delete view.periodType;

												views.push(view);
											}

											map = {
												longitude: lonlat.lon,
												latitude: lonlat.lat,
												zoom: gis.olmap.getZoom(),
												mapViews: views
											};

											Ext.Ajax.request({
												url: gis.baseUrl + gis.conf.url.path_api + 'maps/' + record.data.id,
												method: 'PUT',
												headers: {'Content-Type': 'application/json'},
												params: Ext.encode(map),
												success: function() {
													gis.map = map;
													gis.viewport.interpretationButton.enable();
													gis.store.maps.loadStore();
												}
											});
										}
									}
									else {
										alert(GIS.i18n.no_map_to_save);
									}
								}
							}
						},
						{
							iconCls: 'gis-grid-row-icon-sharing',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-sharing' + (!record.data.access.manage ? ' disabled' : '');
							},
							handler: function(grid, rowIndex) {
								var record = this.up('grid').store.getAt(rowIndex);

								if (record.data.access.manage) {
									Ext.Ajax.request({
										url: gis.baseUrl + '/api/sharing?type=map&id=' + record.data.id,
										method: 'GET',
										failure: function(r) {
                                            gis.olmap.mask.hide();
											alert(r.responseText);
										},
										success: function(r) {
											var sharing = Ext.decode(r.responseText),
												window = GIS.app.SharingWindow(sharing);
											window.show();
										}
									});
								}
							}
						},
						{
							iconCls: 'gis-grid-row-icon-dashboard',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-dashboard' + (!record.data.access.read ? ' disabled' : '');
							},
							handler: function(grid, rowIndex) {
								var record = this.up('grid').store.getAt(rowIndex),
									message;

								if (record.data.access.read) {
									message = 'Add to dashboard?\n\n' + record.data.name;

									if (confirm(message)) {
										Ext.Ajax.request({
											url: gis.baseUrl + gis.conf.url.path_gis + 'addMapViewToDashboard.action',
											params: {
												id: record.data.id
											}
										});
									}
								}
							}
						},
						{
							iconCls: 'gis-grid-row-icon-delete',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-delete' + (!record.data.access['delete'] ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									message;

								if (record.data.access['delete']) {
									message = 'Delete favorite?\n\n' + record.data.name;

									if (confirm(message)) {
										Ext.Ajax.request({
											url: gis.baseUrl + '/api/maps/' + record.data.id,
											method: 'DELETE',
											success: function() {
												gis.store.maps.loadStore();
											}
										});
									}
								}
							}
						}
					]
				},
				{
					sortable: false,
					width: 6
				}
			],
			store: gis.store.maps,
			bbar: [
				info,
				'->',
				prevButton,
				nextButton
			],
			listeners: {
				added: function() {
					gis.viewport.mapGrid = this;
				},
				render: function() {
					var size = Math.floor((gis.viewport.centerRegion.getHeight() - 155) / gis.conf.layout.grid.row_height);
					this.store.pageSize = size;
					this.store.page = 1;
					this.store.loadStore();

					gis.store.maps.on('load', function() {
						if (this.isVisible()) {
							this.fireEvent('afterrender');
						}
					}, this);
				},
				afterrender: function() {
					var fn = function() {
						var editArray = Ext.query('.tooltip-favorite-edit'),
							overwriteArray = Ext.query('.tooltip-favorite-overwrite'),
							sharingArray = Ext.query('.tooltip-favorite-sharing'),
							dashboardArray = Ext.query('.tooltip-favorite-dashboard'),
							deleteArray = Ext.query('.tooltip-favorite-delete'),
							el;

						for (var i = 0; i < editArray.length; i++) {
							var el = editArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: GIS.i18n.rename,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < overwriteArray.length; i++) {
							el = overwriteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: GIS.i18n.overwrite,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < sharingArray.length; i++) {
							el = sharingArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: GIS.i18n.share_with_other_people,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < dashboardArray.length; i++) {
							el = dashboardArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: GIS.i18n.add_to_dashboard,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < deleteArray.length; i++) {
							el = deleteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: GIS.i18n.delete_,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}
					};

					Ext.defer(fn, 100);
				},
				itemmouseenter: function(grid, record, item) {
					this.currentItem = Ext.get(item);
					this.currentItem.removeCls('x-grid-row-over');
				},
				select: function() {
					this.currentItem.removeCls('x-grid-row-selected');
				},
				selectionchange: function() {
					this.currentItem.removeCls('x-grid-row-focused');
				}
			}
		});

		favoriteWindow = Ext.create('Ext.window.Window', {
			title: 'Manage favorites',
			iconCls: 'gis-window-title-icon-favorite',
			cls: 'gis-container-default',
			bodyStyle: 'padding:5px',
			resizable: false,
			modal: true,
			width: windowWidth,
			items: [
				{
					xtype: 'panel',
					layout: 'hbox',
					cls: 'gis-container-inner',
					items: [
						addButton,
						{
							height: 24,
							width: 1,
							style: 'width:1px; margin-left:5px; margin-right:5px; margin-top:1px',
							bodyStyle: 'border-left: 1px solid #aaa'
						},
						searchTextfield
					]
				},
				grid
			],
			listeners: {
				show: function() {
					this.setPosition(115, 37);
				}
			}
		});

		return favoriteWindow;
	};

	GIS.app.LegendSetWindow = function() {

		// Stores
		var legendSetStore,
			legendStore,
			tmpLegendStore,

		// Objects
			LegendSetPanel,
			LegendPanel,

		// Instances
			legendSetPanel,
			legendPanel,

		// Components
			window,
			legendSetName,
			legendName,
			startValue,
			endValue,
			color,
			legendGrid,
			create,
			update,
			cancel,
			info,
			error1Window,
			error2Window,

		// Functions
			showUpdateLegendSet,
			deleteLegendSet,
			deleteLegend,
			getRequestBody,
			reset,
			validateLegends;

		legendSetStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets.json?links=false&paging=false',
				reader: {
					type: 'json',
					root: 'mapLegendSets'
				}
			},
			listeners: {
				load: function(store, records) {
					this.sort('name', 'ASC');

					info.setText(records.length + ' legend set' + (records.length !== 1 ? 's' : '') + ' available');
				}
			}
		});

		legendStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'startValue', 'endValue', 'color'],
			proxy: {
				type: 'ajax',
				url: '',
				reader: {
					type: 'json',
					root: 'mapLegends'
				}
			},
			deleteLegend: deleteLegend,
			listeners: {
				load: function(store, records) {
					var data = [],
						record;

					for (var i = 0; i < records.length; i++) {
						data.push(records[i].data);
					}

					Ext.Array.sort(data, function (a, b) {
						return a.startValue - b.startValue;
					});

					tmpLegendStore.add(data);

					info.setText(records.length + ' legend sets available');
				}
			}
		});

		LegendSetPanel = function() {
			var items,
				addButton;

			addButton = Ext.create('Ext.button.Button', {
				text: GIS.i18n.add_new,
				height: 26,
				style: 'border-radius: 1px',
				menu: {},
				handler: function() {
					showUpdateLegendSet();
				}
			});

			legendSetGrid = Ext.create('Ext.grid.Panel', {
				cls: 'gis-grid',
				scroll: 'vertical',
				height: true,
				hideHeaders: true,
				currentItem: null,
				columns: [
					{
						dataIndex: 'name',
						sortable: false,
						width: 369
					},
					{
						xtype: 'actioncolumn',
						sortable: false,
						width: 40,
						items: [
							{
								iconCls: 'gis-grid-row-icon-edit',
								getClass: function() {
									return 'tooltip-legendset-edit';
								},
								handler: function(grid, rowIndex, colIndex, col, event) {
									var id = this.up('grid').store.getAt(rowIndex).data.id;
									showUpdateLegendSet(id);
								}
							},
							{
								iconCls: 'gis-grid-row-icon-delete',
								getClass: function() {
									return 'tooltip-legendset-delete';
								},
								handler: function(grid, rowIndex, colIndex, col, event) {
									var record = this.up('grid').store.getAt(rowIndex),
										id = record.data.id,
										name = record.data.name,
										message = 'Delete legend set?\n\n' + name;

									if (confirm(message)) {
										deleteLegendSet(id);
									}
								}
							}
						]
					},
					{
						sortable: false,
						width: 17
					}
				],
				store: legendSetStore,
				listeners: {
					render: function() {
						var that = this,
							maxHeight = gis.viewport.centerRegion.getHeight() - 155,
							height;

						this.store.on('load', function() {
							if (Ext.isDefined(that.setHeight)) {
								height = 1 + that.store.getCount() * gis.conf.layout.grid.row_height;
								that.setHeight(height > maxHeight ? maxHeight : height);
								window.doLayout();
							}
						});

						this.store.load();
					},
					afterrender: function() {
						var fn = function() {
							var editArray = document.getElementsByClassName('tooltip-legendset-edit'),
								deleteArray = document.getElementsByClassName('tooltip-legendset-delete'),
								len = editArray.length,
								el;

							for (var i = 0; i < len; i++) {
								el = editArray[i];
								Ext.create('Ext.tip.ToolTip', {
									target: el,
									html: 'Rename',
									'anchor': 'bottom',
									anchorOffset: -14,
									showDelay: 1000
								});

								el = deleteArray[i];
								Ext.create('Ext.tip.ToolTip', {
									target: el,
									html: 'Delete',
									'anchor': 'bottom',
									anchorOffset: -14,
									showDelay: 1000
								});
							}
						};

						Ext.defer(fn, 100);
					},
					itemmouseenter: function(grid, record, item) {
						this.currentItem = Ext.get(item);
						this.currentItem.removeCls('x-grid-row-over');
					},
					select: function() {
						this.currentItem.removeCls('x-grid-row-selected');
					},
					selectionchange: function() {
						this.currentItem.removeCls('x-grid-row-focused');
					}
				}
			});

			items = [
				{
					xtype: 'panel',
					layout: 'hbox',
					cls: 'gis-container-inner',
					style: 'margin-bottom: 5px',
					items: [
						addButton
					]
				},
				legendSetGrid
			];

			return items;
		};

		LegendPanel = function(id) {
			var panel,
				addLegend,
				reset,
				data = [];

			tmpLegendStore = Ext.create('Ext.data.ArrayStore', {
				fields: ['id', 'name', 'startValue', 'endValue', 'color']
			});

			legendSetName = Ext.create('Ext.form.field.Text', {
				cls: 'gis-textfield',
				width: 428,
				height: 25,
				fieldStyle: 'padding-left: 6px; border-color: #bbb',
				fieldLabel: GIS.i18n.legend_set_name
			});

			legendName = Ext.create('Ext.form.field.Text', {
				cls: 'gis-textfield',
				fieldStyle: 'padding-left: 6px',
				width: 415,
				height: 23,
				fieldLabel: GIS.i18n.legend_name
			});

			startValue = Ext.create('Ext.form.field.Number', {
				width: 153,
				height: 23,
				allowDecimals: false,
				fieldStyle: 'padding-left: 6px; border-radius: 1px',
				value: 0
			});

			endValue = Ext.create('Ext.form.field.Number', {
				width: 154,
				height: 23,
				allowDecimals: false,
				fieldStyle: 'padding-left: 6px; border-radius: 1px',
				value: 0,
				style: 'padding-left: 3px'
			});

			color = Ext.create('Ext.ux.button.ColorButton', {
				width: 310,
				height: 23,
				fieldLabel: GIS.i18n.legend_symbolizer,
				style: 'border-radius: 1px',
				value: 'e1e1e1'
			});

			addLegend = Ext.create('Ext.button.Button', {
				text: GIS.i18n.add_legend,
				height: 26,
				style: 'border-radius: 1px',
				handler: function() {
					var date = new Date(),
						id = date.toISOString(),
						ln = legendName.getValue(),
						sv = startValue.getValue(),
						ev = endValue.getValue(),
						co = color.getValue().toUpperCase(),
						items = tmpLegendStore.data.items,
						data = [];

					if (ln && (ev > sv)) {
						for (var i = 0; i < items.length; i++) {
							data.push(items[i].data);
						}

						data.push({
							id: id,
							name: ln,
							startValue: sv,
							endValue: ev,
							color: '#' + co
						});

						Ext.Array.sort(data, function (a, b) {
							return a.startValue - b.startValue;
						});

						tmpLegendStore.removeAll();
						tmpLegendStore.add(data);

						legendName.reset();
						startValue.reset();
						endValue.reset();
						color.reset();
					}
				}
			});

			legendGrid = Ext.create('Ext.grid.Panel', {
				cls: 'gis-grid',
				bodyStyle: 'border-top: 0 none',
				width: 428,
				height: 235,
				scroll: 'vertical',
				hideHeaders: true,
				currentItem: null,
				columns: [
					{
						dataIndex: 'name',
						sortable: false,
						width: 256
					},
					{
						sortable: false,
						width: 45,
						renderer: function(value, metaData, record) {
							return '<span style="color:' + record.data.color + '">Color</span>';
						}
					},
					{
						dataIndex: 'startValue',
						sortable: false,
						width: 45
					},
					{
						dataIndex: 'endValue',
						sortable: false,
						width: 45
					},
					{
						xtype: 'actioncolumn',
						sortable: false,
						width: 20,
						items: [
							{
								iconCls: 'gis-grid-row-icon-delete',
								getClass: function() {
									return 'tooltip-legend-delete';
								},
								handler: function(grid, rowIndex, colIndex, col, event) {
									var id = this.up('grid').store.getAt(rowIndex).data.id;
									deleteLegend(id);
								}
							}
						]
					},
					{
						sortable: false,
						width: 17
					}
				],
				store: tmpLegendStore,
				listeners: {
					itemmouseenter: function(grid, record, item) {
						this.currentItem = Ext.get(item);
						this.currentItem.removeCls('x-grid-row-over');
					},
					select: function() {
						this.currentItem.removeCls('x-grid-row-selected');
					},
					selectionchange: function() {
						this.currentItem.removeCls('x-grid-row-focused');
					},
					afterrender: function() {
						var fn = function() {
							var deleteArray = document.getElementsByClassName('tooltip-legend-delete'),
								len = deleteArray.length,
								el;

							for (var i = 0; i < len; i++) {
								el = deleteArray[i];
								Ext.create('Ext.tip.ToolTip', {
									target: el,
									html: 'Delete',
									'anchor': 'bottom',
									anchorOffset: -14,
									showDelay: 1000
								});
							}
						};

						Ext.defer(fn, 100);
					}
				}
			});

			panel = Ext.create('Ext.panel.Panel', {
				cls: 'gis-container-inner',
				legendSetId: id,
				items: [
					legendSetName,
					{
						cls: 'gis-panel-html-separator'
					},
					{
						html: GIS.i18n.add_legend,
						cls: 'gis-panel-html-title'
					},
					{
						cls: 'gis-panel-html-separator'
					},
					{
						bodyStyle: 'background-color:#f1f1f1; border:1px solid #ccc; border-radius:1px; padding:5px',
						items: [
							legendName,
							{
								layout: 'hbox',
								bodyStyle: 'background: transparent',
								items: [
									{
										html: GIS.i18n.start_end_value + ':',
										width: 105,
										bodyStyle: 'background:transparent; padding-top:3px'
									},
									startValue,
									endValue
								]
							},
							{
								layout: 'column',
								cls: 'gis-container-inner',
								bodyStyle: 'background: transparent',
								items: [
									{
										cls: 'gis-panel-html-label',
										html: GIS.i18n.legend_symbolizer,
										bodyStyle: 'background: transparent',
										width: gis.conf.layout.widget.itemlabel_width + 10
									},
									color
								]
							},
						]
					},
					{
						cls: 'gis-panel-html-separator'
					},
					{
						cls: 'gis-container-inner',
						bodyStyle: 'text-align: right',
						width: 428,
						items: addLegend
					},
					{
						html: GIS.i18n.current_legends,
						cls: 'gis-panel-html-title'
					},
					{
						cls: 'gis-panel-html-separator'
					},
					legendGrid
				]
			});

			if (id) {
				legendStore.proxy.url = gis.baseUrl + gis.conf.url.path_api +  'mapLegendSets/' + id + '.json?links=false&paging=false';
				legendStore.load();

				legendSetName.setValue(legendSetStore.getById(id).data.name);
			}

			return panel;
		};

		showUpdateLegendSet = function(id) {
			legendPanel = new LegendPanel(id);
			window.removeAll();
			window.add(legendPanel);
			info.hide();
			cancel.show();

			if (id) {
				update.show();
			}
			else {
				create.show();
			}
		};

		deleteLegendSet = function(id) {
			if (id) {
				Ext.Ajax.request({
					url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets/' + id,
					method: 'DELETE',
					success: function() {
						legendSetStore.load();
						gis.store.legendSets.load();
					}
				});
			}
		};

		deleteLegend = function(id) {
			tmpLegendStore.remove(tmpLegendStore.getById(id));
		};

		getRequestBody = function() {
			var items = tmpLegendStore.data.items,
				body;

			body = {
				name: legendSetName.getValue(),
				symbolizer: gis.conf.finals.widget.symbolizer_color,
				mapLegends: []
			};

			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				body.mapLegends.push({
					name: item.data.name,
					startValue: item.data.startValue,
					endValue: item.data.endValue,
					color: item.data.color
				});
			}

			return body;
		};

		reset = function() {
			legendPanel.destroy();
			legendSetPanel = new LegendSetPanel();
			window.removeAll();
			window.add(legendSetPanel);

			info.show();
			cancel.hide();
			create.hide();
			update.hide();
		};

		validateLegends = function() {
			var items = tmpLegendStore.data.items,
				item,
				prevItem;

			if (items.length === 0) {
				alert('No legend set name');
				return false;
			}

			for (var i = 1; i < items.length; i++) {
				item = items[i].data;
				prevItem = items[i - 1].data;

				if (item.startValue < prevItem.endValue) {
					var msg = 'Overlapping legends not allowed!\n\n' +
							  prevItem.name + ' (' + prevItem.startValue + ' - ' + prevItem.endValue + ')\n' +
							  item.name + ' (' + item.startValue + ' - ' + item.endValue + ')';
					alert(msg);
					return false;
				}

				if (prevItem.endValue < item.startValue) {
					var msg = 'Legend gaps detected!\n\n' +
							  prevItem.name + ' (' + prevItem.startValue + ' - ' + prevItem.endValue + ')\n' +
							  item.name + ' (' + item.startValue + ' - ' + item.endValue + ')\n\n' +
							  'Proceed anyway?';

					if (!confirm(msg)) {
						return false;
					}
				}
			}

			return true;
		};

		create = Ext.create('Ext.button.Button', {
			text: GIS.i18n.create,
			hidden: true,
			handler: function() {
				if (legendSetName.getValue() && validateLegends()) {
					if (legendSetStore.findExact('name', legendSetName.getValue()) !== -1) {
						alert('Name already in use');
						return;
					}

					var body = Ext.encode(getRequestBody());

					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets/',
						method: 'POST',
						headers: {'Content-Type': 'application/json'},
						params: body,
						success: function() {
							gis.store.legendSets.load();
							reset();
						}
					});
				}
			}
		});

		update = Ext.create('Ext.button.Button', {
			text: GIS.i18n.update,
			hidden: true,
			handler: function() {
				if (legendSetName.getValue() && validateLegends()) {
					var body = getRequestBody(),
						id = legendPanel.legendSetId;
					body.id = id;
					body = Ext.encode(getRequestBody());

					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_api + 'mapLegendSets/' + id,
						method: 'PUT',
						headers: {'Content-Type': 'application/json'},
						params: body,
						success: function() {
							reset();
						}
					});
				}
			}
		});

		cancel = Ext.create('Ext.button.Button', {
			text: GIS.i18n.cancel,
			hidden: true,
			handler: function() {
				reset();
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'gis-label-info',
			width: 400,
			height: 22
		});

		window = Ext.create('Ext.window.Window', {
			title: GIS.i18n.legendsets,
			iconCls: 'gis-window-title-icon-legendset', //todo
            bodyStyle: 'padding:5px; background-color:#fff',
			resizable: false,
			width: 450,
			modal: true,
			items: new LegendSetPanel(),
			bbar: {
				height: 27,
				items: [
					info,
					cancel,
					'->',
					create,
					update
				]
			},
			listeners: {
				show: function() {
					this.setPosition(185, 37);
				}
			}
		});

		return window;
	};

	GIS.app.DownloadWindow = function() {
		var window,
			format,
			name,
			button;

		format = Ext.create('Ext.form.field.ComboBox', {
			cls: 'gis-combo',
			width: 60,
			style: 'margin-bottom:0; margin-left:2px',
			valueField: 'id',
			displayField: 'text',
			editable: false,
			queryMode: 'local',
			forceSelection: true,
			value: 'png',
			store: Ext.create('Ext.data.ArrayStore', {
				fields: ['id', 'text'],
				data: [
					['png', 'PNG'],
					['pdf', 'PDF']
				]
			})
		});

		name = Ext.create('Ext.form.field.Text', {
			cls: 'gis-textfield',
			//height: 23,
			width: 230,
			fieldStyle: 'padding-left:4px',
			style: 'margin-bottom:0',
			emptyText: GIS.i18n.please_enter_map_title
		});

		button = Ext.create('Ext.button.Button', {
			text: GIS.i18n.download,
			handler: function() {
				var type = format.getValue(),
					title = Ext.htmlEncode(name.getValue()),
					svg = gis.util.svg.getString(title, gis.util.map.getVisibleVectorLayers()),
					exportForm = document.getElementById('exportForm');

				if (!svg) {
					alert(GIS.i18n.please_create_map_first);
					return;
				}

				document.getElementById('typeField').value = type;
				document.getElementById('titleField').value = title;
				document.getElementById('svgField').value = svg;
				exportForm.action = '../exportImage.action';
				exportForm.method = 'post';
				exportForm.submit();

				window.destroy();
			}
		});

		window = Ext.create('Ext.window.Window', {
			title: GIS.i18n.download_map_as_png,
			layout: 'column',
			iconCls: 'gis-window-title-icon-download',
			cls: 'gis-container-default',
            bodyStyle: 'padding:2px',
			resizable: true,
			modal: true,
			items: [
				name,
				format
			],
			bbar: [
				'->',
				button
			],
			listeners: {
				show: function() {
					this.setPosition(253, 37);
				}
			}
		});

		return window;
	};

	GIS.app.InterpretationWindow = function() {
		var window,
			textarea,
			panel,
			button;

		textarea = Ext.create('Ext.form.field.TextArea', {
			cls: 'gis-textarea',
			height: 130,
			fieldStyle: 'padding-left: 4px; padding-top: 3px',
			emptyText: GIS.i18n.write_your_interpretation
		});

		panel = Ext.create('Ext.panel.Panel', {
			cls: 'gis-container-inner',
			html: '<b>' + GIS.i18n.link_ + ': </b>' + gis.init.contextPath + '/dhis-web-mapping/app/index.html?id=' + gis.map.id,
			style: 'padding:6px 0 6px 1px'
		});

		button = Ext.create('Ext.button.Button', {
			text: GIS.i18n.share,
			handler: function() {
				if (textarea.getValue() && gis.map && gis.map.id) {
					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_api + 'interpretations/map/' + gis.map.id,
						method: 'POST',
						params: textarea.getValue(),
						headers: {'Content-Type': 'text/html'},
						success: function() {
							window.destroy();
						}
					});
				}
			}
		});

		window = Ext.create('Ext.window.Window', {
			title: GIS.i18n.share_interpretation,
			layout: 'fit',
			iconCls: 'gis-window-title-icon-interpretation',
			cls: 'gis-container-default',
            bodyStyle: 'padding:5px 5px 0',
			width: 500,
			resizable: true,
			modal: true,
			items: [
				textarea,
				panel
			],
			bbar: [
				'->',
				button
			],
			listeners: {
				show: function() {
					this.setPosition(325, 37);
				},
				destroy: function() {
					document.body.oncontextmenu = function(){
						return false;
					};
				}

			}
		});

		document.body.oncontextmenu = true; // right click to copy url

		return window;
	};

	GIS.app.CircleLayer = function(features, radius) {
		var points = gis.util.map.getPointsByFeatures(features),
			lonLats = gis.util.map.getLonLatsByPoints(points),
			controls = [],
			control,
			layer = new OpenLayers.Layer.Vector(),
			deactivateControls,
			createCircles,
			params = {};

		radius = radius && Ext.isNumber(parseInt(radius)) ? parseInt(radius) : 5;

		deactivateControls = function() {
			for (var i = 0; i < controls.length; i++) {
				controls[i].deactivate();
			}
		};

		createCircles = function() {
			if (lonLats.length) {
				for (var i = 0; i < lonLats.length; i++) {
					control = new OpenLayers.Control.Circle({
						layer: layer
					});
					control.lonLat = lonLats[i];
					controls.push(control);
				}

				gis.olmap.addControls(controls);

				for (var i = 0; i < controls.length; i++) {
					control = controls[i];
					control.activate();
					control.updateCircle(control.lonLat, radius);
				}
			}
		}();

		layer.deactivateControls = deactivateControls;

		return layer;
	};

	GIS.app.LayerWidgetBoundary = function(layer) {

		// Stores
		var infrastructuralDataElementValuesStore,

		// Components
			level,
			parent,

		// Functions
			reset,
			setGui,
			getView,
			validateView,

			panel;

		// Stores

		infrastructuralDataElementValuesStore = Ext.create('Ext.data.Store', {
			fields: ['dataElementName', 'value'],
			proxy: {
				type: 'ajax',
				url: '../getInfrastructuralDataElementMapValues.action',
				reader: {
					type: 'json',
					root: 'mapValues'
				}
			},
			sortInfo: {field: 'dataElementName', direction: 'ASC'},
			autoLoad: false,
			isLoaded: false,
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
				}
			}
		});

		// Components

		level = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.level,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			mode: 'remote',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			style: 'margin-bottom: 4px',
			store: gis.store.organisationUnitLevels,
			listeners: {
				added: function() {
					this.store.cmp.push(this);
				}
			}
		});

		parent = Ext.create('Ext.tree.Panel', {
			autoScroll: true,
			lines: false,
			rootVisible: false,
			multiSelect: false,
			width: gis.conf.layout.widget.item_width,
			height: 300,
			reset: function() {
				this.collapseAll();
				this.expandPath(gis.init.rootNodes[0].path);
				this.selectPath(gis.init.rootNodes[0].path);
			},
			store: Ext.create('Ext.data.TreeStore', {
				proxy: {
					type: 'ajax',
					url: gis.baseUrl + gis.conf.url.path_gis + 'getOrganisationUnitChildren.action'
				},
				root: {
					id: 'root',
					expanded: true,
					children: gis.init.rootNodes
				},
				listeners: {
					load: function(s, node, r) {
						for (var i = 0; i < r.length; i++) {
							r[i].data.text = gis.util.json.encodeString(r[i].data.text);
						}
					}
				}
			}),
			listeners: {
				afterrender: function() {
					this.getSelectionModel().select(0);
				}
			}
		});

		// Functions

		reset = function() {

			// Components
			level.clearValue();
			parent.reset();

			// Layer options
			if (layer.searchWindow) {
				layer.searchWindow.destroy();
				layer.searchWindow = null;
			}
			if (layer.labelWindow) {
				layer.labelWindow.destroy();
				layer.labelWindow = null;
			}

			// Item
			layer.item.setValue(false);
		};

		setGui = function(view) {

			// Level and parent
			gis.store.organisationUnitLevels.loadFn( function() {
				level.setValue(view.organisationUnitLevel.id);
			});

			parent.selectPath('/root' + view.parentGraph);

			// Layer item
			layer.item.setValue(true, view.opacity);

			// Layer menu
			layer.menu.enableItems();
		};

		getView = function(config) {
			var parentArray = parent.getSelectionModel().getSelection(),
				store = gis.store.organisationUnitLevels,
				view;

			parentArray = parentArray.length ? parentArray : [{raw: gis.init.rootNodes[0]}];

			view = {
				organisationUnitLevel: {
					id: level.getValue(),
					name: level.getRawValue(),
					level: store.data.items.length && level.getValue() ? store.getById(level.getValue()).data.level : null
				},
				parentOrganisationUnit: {
					id: parentArray[0].raw.id,
					name: parentArray[0].raw.text
				},
				parentLevel: parentArray[0].raw.level,
				parentGraph: parentArray[0].raw.path,
				opacity: layer.item.getOpacity()
			};

			if (config && Ext.isObject(config)) {
				view = layer.core.extendView(view, config);
			}

			return validateView(view);
		};

		validateView = function(view) {
			if (!view.organisationUnitLevel.id || !Ext.isString(view.organisationUnitLevel.id)) {
				GIS.logg.push([view.organisationUnitLevel.id, layer.id + '.organisationUnitLevel.id: string']);
				alert(GIS.i18n.no_level_selected);
				return false;
			}
			if (!view.organisationUnitLevel.name || !Ext.isString(view.organisationUnitLevel.name)) {
				GIS.logg.push([view.organisationUnitLevel.name, layer.id + '.organisationUnitLevel.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.organisationUnitLevel.level || !Ext.isNumber(view.organisationUnitLevel.level)) {
				GIS.logg.push([view.organisationUnitLevel.level, layer.id + '.organisationUnitLevel.level: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentOrganisationUnit.id || !Ext.isString(view.parentOrganisationUnit.id)) {
				GIS.logg.push([view.parentOrganisationUnit.id, layer.id + '.parentOrganisationUnit.id: string']);
				alert(GIS.i18n.no_parent_organisationunit_selected);
				return false;
			}
			if (!view.parentOrganisationUnit.name || !Ext.isString(view.parentOrganisationUnit.name)) {
				GIS.logg.push([view.parentOrganisationUnit.name, layer.id + '.parentOrganisationUnit.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentLevel || !Ext.isNumber(view.parentLevel)) {
				GIS.logg.push([view.parentLevel, layer.id + '.parentLevel: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentGraph || !Ext.isString(view.parentGraph)) {
				GIS.logg.push([view.parentGraph, layer.id + '.parentGraph: string']);
					//alert("validation failed"); //todo
				return false;
			}

			if (view.parentOrganisationUnit.level > view.organisationUnitLevel.level) {
				GIS.logg.push([view.parentOrganisationUnit.level, view.organisationUnitLevel.level, layer.id + '.parentOrganisationUnit.level: number <= ' + layer.id + '.organisationUnitLevel.level']);
				alert(GIS.i18n.level_not_higher_than_parent_level);
				return false;
			}

			return view;
		};

		panel = Ext.create('Ext.panel.Panel', {
			map: layer.map,
			layer: layer,
			menu: layer.menu,

			reset: reset,
			setGui: setGui,
			getView: getView,

			infrastructuralDataElementValuesStore: infrastructuralDataElementValuesStore,

			cls: 'gis-form-widget el-border-0',
			border: false,
			items: [
				{
					xtype: 'form',
					cls: 'el-border-0',
					width: 270,
					items: [
						{
							html: GIS.i18n.organisation_unit_level_parent,
							cls: 'gis-form-subtitle-first'
						},
						level,
						parent
					]
				}
			]
		});

		//createSelectHandlers();

		return panel;
	};

	GIS.app.LayerWidgetThematic = function(layer) {

		// Stores
		var indicatorsByGroupStore,
			dataElementsByGroupStore,
			periodsByTypeStore,
			infrastructuralDataElementValuesStore,
			legendsByLegendSetStore,

		// Togglers
			valueTypeToggler,
			legendTypeToggler,

		// Components
			valueType,
			indicatorGroup,
			indicator,
			dataElementGroup,
			dataElement,
			periodType,
			period,
			periodPrev,
			periodNext,
			legendType,
			legendSet,
			classes,
			method,
			colorLow,
			colorHigh,
			radiusLow,
			radiusHigh,
			level,
			parent,

			periodTypePanel,
			methodPanel,
			lowPanel,
			highPanel,

		// Functions
			//createSelectHandlers,
			reset,
			setGui,
			getView,
			validateView,

			panel;

		// Stores

		indicatorsByGroupStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'legendSet'],
			proxy: {
				type: 'ajax',
				url: '',
				reader: {
					type: 'json',
					root: 'indicators'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		dataElementsByGroupStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: '',
				reader: {
					type: 'json',
					root: 'dataElements'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		periodsByTypeStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'index'],
			data: [],
			setIndex: function(periods) {
				for (var i = 0; i < periods.length; i++) {
					periods[i].index = i;
				}
			},
			sortStore: function() {
				this.sort('index', 'ASC');
			}
		});

		infrastructuralDataElementValuesStore = Ext.create('Ext.data.Store', {
			fields: ['dataElementName', 'value'],
			proxy: {
				type: 'ajax',
				url: '../getInfrastructuralDataElementMapValues.action',
				reader: {
					type: 'json',
					root: 'mapValues'
				}
			},
			sortInfo: {field: 'dataElementName', direction: 'ASC'},
			autoLoad: false,
			isLoaded: false,
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
				}
			}
		});

		legendsByLegendSetStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'startValue', 'endValue', 'color'],
			proxy: {
				type: 'ajax',
				url: '',
				reader: {
					type: 'json',
					root: 'mapLegends'
				}
			},
			isLoaded: false,
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
					this.sort('name', 'ASC');
				}
			}
		});

		// Togglers

		valueTypeToggler = function(valueType) {
			if (valueType === 'indicator') {
				indicatorGroup.show();
				indicator.show();
				dataElementGroup.hide();
				dataElement.hide();
			}
			else if (valueType === 'dataElement') {
				indicatorGroup.hide();
				indicator.hide();
				dataElementGroup.show();
				dataElement.show();
			}
		};

		legendTypeToggler = function(legendType) {
			if (legendType === 'automatic') {
				methodPanel.show();
				lowPanel.show();
				highPanel.show();
				legendSet.hide();
			}
			else if (legendType === 'predefined') {
				methodPanel.hide();
				lowPanel.hide();
				highPanel.hide();
				legendSet.show();
			}
		};

		// Components

		valueType = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.value_type,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			queryMode: 'local',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			value: gis.conf.finals.dimension.indicator.id,
			store: Ext.create('Ext.data.ArrayStore', {
				fields: ['id', 'name'],
				data: [
					[gis.conf.finals.dimension.indicator.id, GIS.i18n.indicator],
					[gis.conf.finals.dimension.dataElement.id, GIS.i18n.dataelement]
				]
			}),
			listeners: {
				select: function() {
					valueTypeToggler(this.getValue());
				}
			}
		});

		indicatorGroup = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.indicator_group,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			store: gis.store.indicatorGroups,
			listeners: {
				added: function() {
					this.store.cmp.push(this);
				},
				select: function() {
					indicator.clearValue();

					var store = indicator.store;
					store.proxy.url = gis.baseUrl + gis.conf.url.path_api +  'indicatorGroups/' + this.getValue() + '.json?links=false&paging=false';
					store.load();
				}
			}
		});

		indicator = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.indicator,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			queryMode: 'local',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			listConfig: {loadMask: false},
			store: indicatorsByGroupStore,
			listeners: {
				select: function(cb) {
					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_api + 'indicators/' + this.getValue() + '.json?links=false',
						success: function(r) {
							r = Ext.decode(r.responseText);

							if (Ext.isDefined(r.legendSet) && r.legendSet && r.legendSet.id) {
								legendType.setValue(gis.conf.finals.widget.legendtype_predefined);
								legendTypeToggler(gis.conf.finals.widget.legendtype_predefined);
								if (gis.store.legendSets.isLoaded) {
									legendSet.setValue(r.legendSet.id);
								}
								else {
									gis.store.legendSets.loadFn( function() {
										legendSet.setValue(r.legendSet.id);
									});
								}
							}
							else {
								legendType.setValue(gis.conf.finals.widget.legendtype_automatic);
								legendTypeToggler(gis.conf.finals.widget.legendtype_automatic);
							}
						}
					});
				}
			}
		});

		dataElementGroup = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.dataelement_group,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			hidden: true,
			store: gis.store.dataElementGroups,
			listeners: {
				added: function() {
					this.store.cmp.push(this);
				},
				select: function() {
					dataElement.clearValue();

					var store = dataElement.store;
					store.proxy.url = gis.baseUrl + gis.conf.url.path_api +  'dataElementGroups/' + this.getValue() + '.json?links=false&paging=false';
					store.load();
				}
			}
		});

		dataElement = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.dataelement,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			queryMode: 'local',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			listConfig: {loadMask: false},
			hidden: true,
			store: dataElementsByGroupStore,
			listeners: {
				select: function() {
					Ext.Ajax.request({
						url: gis.baseUrl + gis.conf.url.path_api + 'dataElements/' + this.getValue() + '.json?links=false',
						success: function(r) {
							r = Ext.decode(r.responseText);

							if (Ext.isDefined(r.legendSet) && r.legendSet && r.legendSet.id) {
								legendType.setValue(gis.conf.finals.widget.legendtype_predefined);
								legendTypeToggler(gis.conf.finals.widget.legendtype_predefined);

								if (gis.store.legendSets.isLoaded) {
									legendSet.setValue(r.legendSet.id);
								}
								else {
									gis.store.legendSets.loadFn( function() {
										legendSet.setValue(r.legendSet.id);
									});
								}
							}
							else {
								legendType.setValue(gis.conf.finals.widget.legendtype_automatic);
								legendTypeToggler(gis.conf.finals.widget.legendtype_automatic);
							}
						}
					});
				}
			}
		});

		periodType = Ext.create('Ext.form.field.ComboBox', {
			editable: false,
			valueField: 'id',
			displayField: 'name',
			forceSelection: true,
			queryMode: 'local',
			width: 116,
			store: gis.store.periodTypes,
			periodOffset: 0,
			listeners: {
				select: function() {
					var pt = new PeriodType(),
						type = this.getValue(),
						offset = this.periodOffset,

						periods = pt.get(type).generatePeriods({
							offset: offset,
							filterFuturePeriods: true,
							reversePeriods: true
						});

					periodsByTypeStore.setIndex(periods);
					periodsByTypeStore.loadData(periods);

					period.selectFirst();
				}
			}
		});

		period = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.period,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			queryMode: 'local',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			store: periodsByTypeStore,
			selectFirst: function() {
				this.setValue(this.store.getAt(0).data.id);
			}
		});

		periodPrev = Ext.create('Ext.button.Button', {
			xtype: 'button',
			text: '<',
			width: 20,
			style: 'margin-left: 3px',
			handler: function() {
				if (periodType.getValue()) {
					periodType.periodOffset--;
					periodType.fireEvent('select');
				}
			}
		});

		periodNext = Ext.create('Ext.button.Button', {
			xtype: 'button',
			text: '>',
			width: 20,
			style: 'margin-left: 3px',
			scope: this,
			handler: function() {
				if (periodType.getValue() && periodType.periodOffset < 0) {
					periodType.periodOffset++;
					periodType.fireEvent('select');
				}
			}
		});

		legendType = Ext.create('Ext.form.field.ComboBox', {
			editable: false,
			valueField: 'id',
			displayField: 'name',
			fieldLabel: GIS.i18n.legend_type,
			value: gis.conf.finals.widget.legendtype_automatic,
			queryMode: 'local',
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			store: Ext.create('Ext.data.ArrayStore', {
				fields: ['id', 'name'],
				data: [
					[gis.conf.finals.widget.legendtype_automatic, GIS.i18n.automatic],
					[gis.conf.finals.widget.legendtype_predefined, GIS.i18n.predefined]
				]
			}),
			listeners: {
				select: function() {
					legendTypeToggler(this.getValue());
				}
			}
		});

		legendSet = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.legendset,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			hidden: true,
			store: gis.store.legendSets
		});

		classes = Ext.create('Ext.form.field.Number', {
			editable: false,
			valueField: 'id',
			displayField: 'id',
			queryMode: 'local',
			value: 5,
			minValue: 1,
			maxValue: 7,
			width: 50,
			style: 'margin-right: 3px',
			store: Ext.create('Ext.data.ArrayStore', {
				fields: ['id'],
				data: [[1], [2], [3], [4], [5], [6], [7]]
			})
		});

		method = Ext.create('Ext.form.field.ComboBox', {
			editable: false,
			valueField: 'id',
			displayField: 'name',
			queryMode: 'local',
			value: 2,
			width: 109,
			store: Ext.create('Ext.data.ArrayStore', {
				fields: ['id', 'name'],
				data: [
					[2, GIS.i18n.equal_intervals],
					[3, GIS.i18n.equal_counts]
				]
			})
		});

		colorLow = Ext.create('Ext.ux.button.ColorButton', {
			style: 'margin-right: 3px',
			value: 'ff0000',
			scope: this
		});

		colorHigh = Ext.create('Ext.ux.button.ColorButton', {
			style: 'margin-right: 3px',
			value: '00ff00',
			scope: this
		});

		radiusLow = Ext.create('Ext.form.field.Number', {
			width: 50,
			allowDecimals: false,
			minValue: 1,
			value: 5
		});

		radiusHigh = Ext.create('Ext.form.field.Number', {
			width: 50,
			allowDecimals: false,
			minValue: 1,
			value: 15
		});

		level = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.level,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			mode: 'remote',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			style: 'margin-bottom: 4px',
			store: gis.store.organisationUnitLevels,
			listeners: {
				added: function() {
					this.store.cmp.push(this);
				}
			}
		});

		parent = Ext.create('Ext.tree.Panel', {
			autoScroll: true,
			lines: false,
			rootVisible: false,
			multiSelect: false,
			width: gis.conf.layout.widget.item_width,
			height: 210,
			reset: function() {
				this.collapseAll();
				this.expandPath(gis.init.rootNodes[0].path);
				this.selectPath(gis.init.rootNodes[0].path);
			},
			store: Ext.create('Ext.data.TreeStore', {
				proxy: {
					type: 'ajax',
					url: gis.baseUrl + gis.conf.url.path_gis + 'getOrganisationUnitChildren.action'
				},
				root: {
					id: 'root',
					expanded: true,
					children: gis.init.rootNodes
				},
				listeners: {
					load: function(s, node, r) {
						for (var i = 0; i < r.length; i++) {
							r[i].data.text = gis.util.json.encodeString(r[i].data.text);
						}
					}
				}
			}),
			listeners: {
				afterrender: function() {
					this.getSelectionModel().select(0);
				}
			}
		});

		periodTypePanel = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					html: GIS.i18n.period_type + ':',
					width: 100,
					bodyStyle: 'color: #444',
					style: 'padding: 3px 0 0 4px'
				},
				periodType,
				periodPrev,
				periodNext
			]
		});

		methodPanel = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					html: GIS.i18n.classes_method,
					width: 100,
					bodyStyle: 'color: #444',
					style: 'padding: 3px 0 0 4px'
				},
				classes,
				method
			]
		});

		lowPanel = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					html: GIS.i18n.low_color_size,
					width: 100,
					bodyStyle: 'color: #444',
					style: 'padding: 3px 0 0 4px'
				},
				colorLow,
				radiusLow
			]
		});

		highPanel = Ext.create('Ext.panel.Panel', {
			layout: 'hbox',
			items: [
				{
					html: GIS.i18n.high_color_size,
					width: 100,
					bodyStyle: 'color: #444',
					style: 'padding: 3px 0 0 4px'
				},
				colorHigh,
				radiusHigh
			]
		});

		// Functions

		reset = function() {

			// Components
			valueType.reset();
			valueTypeToggler(gis.conf.finals.dimension.indicator.id);

			indicatorGroup.clearValue();
			indicator.clearValue();
			dataElementGroup.clearValue();
			dataElement.clearValue();

			periodType.clearValue();
			period.clearValue();

			legendType.reset();
			legendTypeToggler(gis.conf.finals.widget.legendtype_automatic);
			legendSet.clearValue();
			classes.reset();
			method.reset();
			colorLow.reset();
			colorHigh.reset();
			radiusLow.reset();
			radiusHigh.reset();
			level.clearValue();
			parent.reset();

			// Layer options
			if (layer.searchWindow) {
				layer.searchWindow.destroy();
				layer.searchWindow = null;
			}
			if (layer.filterWindow) {
				layer.filterWindow.destroy();
				layer.filterWindow = null;
			}
			if (layer.labelWindow) {
				layer.labelWindow.destroy();
				layer.labelWindow = null;
			}

			// Item
			layer.item.setValue(false);
		};

		setGui = function(view) {

			// Value type
			valueType.setValue(view.valueType);

			// Indicator and data element
			valueTypeToggler(view.valueType);

			var	indeGroupView = view.valueType === gis.conf.finals.dimension.indicator.id ? indicatorGroup : dataElementGroup,
				indeGroupStore = indeGroupView.store,
				indeGroupValue = view.valueType === gis.conf.finals.dimension.indicator.id ? view.indicatorGroup.id : view.dataElementGroup.id,

				indeStore = view.valueType === gis.conf.finals.dimension.indicator.id ? indicatorsByGroupStore : dataElementsByGroupStore,
				indeView = view.valueType === gis.conf.finals.dimension.indicator.id ? indicator : dataElement,
				indeValue = view.valueType === gis.conf.finals.dimension.indicator.id ? view.indicator.id : view.dataElement.id;

			indeGroupStore.loadFn( function() {
				indeGroupView.setValue(indeGroupValue);
			});

			indeStore.proxy.url = gis.baseUrl + gis.conf.url.path_api + view.valueType + 'Groups/' + indeGroupValue + '.json?links=false&paging=false';
			indeStore.loadFn( function() {
				indeView.setValue(indeValue);
			});

			// Period
			periodType.setValue(view.periodType);
			periodType.fireEvent('select');
			period.setValue(view.period.id);

			// Legend
			legendType.setValue(view.legendType);
			legendTypeToggler(view.legendType);

			if (view.legendType === gis.conf.finals.widget.legendtype_automatic) {
				classes.setValue(view.classes);
				method.setValue(view.method);
				colorLow.setValue(view.colorLow);
				colorHigh.setValue(view.colorHigh);
				radiusLow.setValue(view.radiusLow);
				radiusHigh.setValue(view.radiusHigh);
			}
			else if (view.legendType === gis.conf.finals.widget.legendtype_predefined) {
				gis.store.legendSets.loadFn( function() {
					legendSet.setValue(view.legendSet.id);
				});
			}

			// Level and parent
			gis.store.organisationUnitLevels.loadFn( function() {
				level.setValue(view.organisationUnitLevel.id);
			});

			parent.selectPath('/root' + view.parentGraph);

			// Layer item
			layer.item.setValue(true, view.opacity);

			// Layer menu
			layer.menu.enableItems();

			// Filter
			if (layer.filterWindow && layer.filterWindow.isVisible()) {
				layer.filterWindow.filter();
			}
		};

		getView = function(config) {
			var parentArray = parent.getSelectionModel().getSelection(),
				store = gis.store.organisationUnitLevels,
				view;

			parentArray = parentArray.length ? parentArray : [{raw: gis.init.rootNodes[0]}];

			view = {
				valueType: valueType.getValue(),
				indicatorGroup: {
					id: indicatorGroup.getValue(),
					name: indicatorGroup.getRawValue()
				},
				indicator: {
					id: indicator.getValue(),
					name: indicator.getRawValue()
				},
				dataElementGroup: {
					id: dataElementGroup.getValue(),
					name: dataElementGroup.getRawValue()
				},
				dataElement: {
					id: dataElement.getValue(),
					name: dataElement.getRawValue()
				},
				periodType: periodType.getValue(),
				period: {
					id: period.getValue()
				},
				legendType: legendType.getValue(),
				legendSet: {
					id: legendSet.getValue(),
					name: legendSet.getRawValue()
				},
				classes: parseInt(classes.getValue()),
				method: parseInt(method.getValue()),
				colorLow: colorLow.getValue(),
				colorHigh: colorHigh.getValue(),
				radiusLow: parseInt(radiusLow.getValue()),
				radiusHigh: parseInt(radiusHigh.getValue()),
				organisationUnitLevel: {
					id: level.getValue(),
					name: level.getRawValue(),
					level: store.data.items.length && level.getValue() ? store.getById(level.getValue()).data.level : null
				},
				parentOrganisationUnit: {
					id: parentArray[0].raw.id,
					name: parentArray[0].raw.text
				},
				parentLevel: parentArray[0].raw.level,
				parentGraph: parentArray[0].raw.path,
				opacity: layer.item.getOpacity()
			};

			if (config && Ext.isObject(config)) {
				view = layer.core.extendView(view, config);
			}

			return validateView(view);
		};

		validateView = function(view) {
			if (view.valueType === gis.conf.finals.dimension.indicator.id) {
				if (!view.indicatorGroup.id || !Ext.isString(view.indicatorGroup.id)) {
					GIS.logg.push([view.indicatorGroup.id, layer.id + '.indicatorGroup.id: string']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.indicator.id || !Ext.isString(view.indicator.id)) {
					GIS.logg.push([view.indicator.id, layer.id + '.indicator.id: string']);
					alert(GIS.i18n.no_indicator_selected);
					return false;
				}
			}
			else if (view.valueType === gis.conf.finals.dimension.dataElement.id) {
				if (!view.dataElementGroup.id || !Ext.isString(view.dataElementGroup.id)) {
					GIS.logg.push([view.dataElementGroup.id, layer.id + '.dataElementGroup.id: string']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.dataElement.id || !Ext.isString(view.dataElement.id)) {
					GIS.logg.push([view.dataElement.id, layer.id + '.dataElement.id: string']);
					alert(GIS.i18n.no_dataelement_selected);
					return false;
				}
			}

			if (!view.periodType || !Ext.isString(view.periodType)) {
				GIS.logg.push([view.periodType, layer.id + '.periodType: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.period.id || !Ext.isString(view.period.id)) {
				GIS.logg.push([view.period.id, layer.id + '.period.id: string']);
					alert(GIS.i18n.no_period_selected);
				return false;
			}

			if (view.legendType === gis.conf.finals.widget.legendtype_automatic) {
				if (!view.classes || !Ext.isNumber(view.classes)) {
					GIS.logg.push([view.classes, layer.id + '.classes: number']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.method || !Ext.isNumber(view.method)) {
					GIS.logg.push([view.method, layer.id + '.method: number']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.colorLow || !Ext.isString(view.colorLow)) {
					GIS.logg.push([view.colorLow, layer.id + '.colorLow: string']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.radiusLow || !Ext.isNumber(view.radiusLow)) {
					GIS.logg.push([view.radiusLow, layer.id + '.radiusLow: number']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.colorHigh || !Ext.isString(view.colorHigh)) {
					GIS.logg.push([view.colorHigh, layer.id + '.colorHigh: string']);
					//alert("validation failed"); //todo
					return false;
				}
				if (!view.radiusHigh || !Ext.isNumber(view.radiusHigh)) {
					GIS.logg.push([view.radiusHigh, layer.id + '.radiusHigh: number']);
					//alert("validation failed"); //todo
					return false;
				}
			}
			else if (view.legendType === gis.conf.finals.widget.legendtype_predefined) {
				if (!view.legendSet.id || !Ext.isString(view.legendSet.id)) {
					GIS.logg.push([view.legendSet.id, layer.id + '.legendSet.id: string']);
					alert(GIS.i18n.no_legendset_selected);
					return false;
				}
			}

			if (!view.organisationUnitLevel.id || !Ext.isString(view.organisationUnitLevel.id)) {
				GIS.logg.push([view.organisationUnitLevel.id, layer.id + '.organisationUnitLevel.id: string']);
					alert(GIS.i18n.no_level_selected);
				return false;
			}
			if (!view.organisationUnitLevel.name || !Ext.isString(view.organisationUnitLevel.name)) {
				GIS.logg.push([view.organisationUnitLevel.name, layer.id + '.organisationUnitLevel.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.organisationUnitLevel.level || !Ext.isNumber(view.organisationUnitLevel.level)) {
				GIS.logg.push([view.organisationUnitLevel.level, layer.id + '.organisationUnitLevel.level: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentOrganisationUnit.id || !Ext.isString(view.parentOrganisationUnit.id)) {
				GIS.logg.push([view.parentOrganisationUnit.id, layer.id + '.parentOrganisationUnit.id: string']);
					alert(GIS.i18n.no_parent_organisationunit_selected);
				return false;
			}
			if (!view.parentOrganisationUnit.name || !Ext.isString(view.parentOrganisationUnit.name)) {
				GIS.logg.push([view.parentOrganisationUnit.name, layer.id + '.parentOrganisationUnit.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentLevel || !Ext.isNumber(view.parentLevel)) {
				GIS.logg.push([view.parentLevel, layer.id + '.parentLevel: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentGraph || !Ext.isString(view.parentGraph)) {
				GIS.logg.push([view.parentGraph, layer.id + '.parentGraph: string']);
					//alert("validation failed"); //todo
				return false;
			}

			if (view.parentOrganisationUnit.level > view.organisationUnitLevel.level) {
				GIS.logg.push([view.parentOrganisationUnit.level, view.organisationUnitLevel.level, layer.id + '.parentOrganisationUnit.level: number <= ' + layer.id + '.organisationUnitLevel.level']);
				alert(GIS.i18n.level_not_higher_than_parent_level);
				return false;
			}

			return view;
		};

		panel = Ext.create('Ext.panel.Panel', {
			map: layer.map,
			layer: layer,
			menu: layer.menu,

			reset: reset,
			setGui: setGui,
			getView: getView,

			infrastructuralDataElementValuesStore: infrastructuralDataElementValuesStore,

			cls: 'gis-form-widget el-border-0',
			border: false,
			items: [
				{
					xtype: 'form',
					cls: 'el-border-0',
					width: 270,
					items: [
						{
							html: GIS.i18n.data_options,
							cls: 'gis-form-subtitle-first'
						},
						valueType,
						indicatorGroup,
						indicator,
						dataElementGroup,
						dataElement,
						periodTypePanel,
						period,
						{
							html: GIS.i18n.legend_options,
							cls: 'gis-form-subtitle'
						},
						legendType,
						legendSet,
						methodPanel,
						lowPanel,
						highPanel,
						{
							html: GIS.i18n.organisation_unit_level_parent,
							cls: 'gis-form-subtitle'
						},
						level,
						parent
					]
				}
			]
		});

		//createSelectHandlers();

		return panel;
	};

	GIS.app.LayerWidgetFacility = function(layer) {

		// Stores
		var infrastructuralDataElementValuesStore,

		// Components
			groupSet,
			level,
			parent,
			areaRadius,

			periodTypePanel,
			methodPanel,
			lowPanel,
			highPanel,

		// Functions
			//createSelectHandlers,
			reset,
			setGui,
			getView,
			validateView,

			panel;

		// Stores

		infrastructuralDataElementValuesStore = Ext.create('Ext.data.Store', {
			fields: ['dataElementName', 'value'],
			proxy: {
				type: 'ajax',
				url: '../getInfrastructuralDataElementMapValues.action',
				reader: {
					type: 'json',
					root: 'mapValues'
				}
			},
			sortInfo: {field: 'dataElementName', direction: 'ASC'},
			autoLoad: false,
			isLoaded: false,
			listeners: {
				load: function() {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}
				}
			}
		});

		// Components

		groupSet = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel: GIS.i18n.groupset,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            width: gis.conf.layout.widget.item_width,
            labelWidth: gis.conf.layout.widget.itemlabel_width,
            currentValue: false,
            store: gis.store.groupSets
        });

		level = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel: GIS.i18n.level,
			editable: false,
			valueField: 'id',
			displayField: 'name',
			mode: 'remote',
			forceSelection: true,
			width: gis.conf.layout.widget.item_width,
			labelWidth: gis.conf.layout.widget.itemlabel_width,
			style: 'margin-bottom: 4px',
			store: gis.store.organisationUnitLevels,
			listeners: {
				added: function() {
					this.store.cmp.push(this);
				}
			}
		});

		parent = Ext.create('Ext.tree.Panel', {
			autoScroll: true,
			lines: false,
			rootVisible: false,
			multiSelect: false,
			width: gis.conf.layout.widget.item_width,
			height: 210,
			reset: function() {
				this.collapseAll();
				this.expandPath(gis.init.rootNodes[0].path);
				this.selectPath(gis.init.rootNodes[0].path);
			},
			store: Ext.create('Ext.data.TreeStore', {
				proxy: {
					type: 'ajax',
					url: gis.baseUrl + gis.conf.url.path_gis + 'getOrganisationUnitChildren.action'
				},
				root: {
					id: 'root',
					expanded: true,
					children: gis.init.rootNodes
				},
				listeners: {
					load: function(s, node, r) {
						for (var i = 0; i < r.length; i++) {
							r[i].data.text = gis.util.json.encodeString(r[i].data.text);
						}
					}
				}
			}),
			listeners: {
				afterrender: function() {
					this.getSelectionModel().select(0);
				}
			}
		});

		areaRadius = Ext.create('Ext.ux.panel.CheckTextNumber', {
			width: 262,
			text: GIS.i18n.show_circular_area + ':'
		});

		// Functions

		reset = function() {

			// Components
			groupSet.clearValue();

			level.clearValue();
			parent.reset();

			areaRadius.reset();

			// Layer
			if (layer.searchWindow) {
				layer.searchWindow.destroy();
				layer.searchWindow = null;
			}
			if (layer.filterWindow) {
				layer.filterWindow.destroy();
				layer.filterWindow = null;
			}
			if (layer.labelWindow) {
				layer.labelWindow.destroy();
				layer.labelWindow = null;
			}

			if (layer.circleLayer) {
				layer.circleLayer.deactivateControls();
				layer.circleLayer = null;
			}

			// Item
			layer.item.setValue(false);
		};

		setGui = function(view) {
			// Components
            gis.store.groupSets.loadFn( function() {
                groupSet.setValue(view.organisationUnitGroupSet.id);
            });

			gis.store.organisationUnitLevels.loadFn( function() {
				level.setValue(view.organisationUnitLevel.id);
			});

			parent.selectPath('/root' + view.parentGraph);

			// Layer item
			layer.item.setValue(true, view.opacity);

			// Layer menu
			layer.menu.enableItems();

			// Update filter window
			if (layer.filterWindow && layer.filterWindow.isVisible()) {
				layer.filterWindow.filter();
			}
		};

		getView = function(config) {
			var parentArray = parent.getSelectionModel().getSelection(),
				store = gis.store.organisationUnitLevels,
				view;

			parentArray = parentArray.length ? parentArray : [{raw: gis.init.rootNodes[0]}];

			view = {
				organisationUnitGroupSet: {
					id: groupSet.getValue(),
					name: groupSet.getRawValue()
				},
				organisationUnitLevel: {
					id: level.getValue(),
					name: level.getRawValue(),
					level: store.data.items.length && level.getValue() ? store.getById(level.getValue()).data.level : null
				},
				parentOrganisationUnit: {
					id: parentArray[0].raw.id,
					name: parentArray[0].raw.text
				},
				areaRadius: areaRadius.getValue() ? areaRadius.getNumber() : null,
				parentLevel: parentArray[0].raw.level,
				parentGraph: parentArray[0].raw.path,
				opacity: layer.item.getOpacity()
			};

			if (config && Ext.isObject(config)) {
				view = layer.core.extendView(view, config);
			}

			return validateView(view);
		};

		validateView = function(view) {
			if (!view.organisationUnitGroupSet.id || !Ext.isString(view.organisationUnitGroupSet.id)) {
				GIS.logg.push([view.organisationUnitGroupSet.id, layer.id + '.organisationUnitGroupSet.id: string']);
				alert(GIS.i18n.no_groupset_selected);
				return false;
			}

			if (!view.organisationUnitLevel.id || !Ext.isString(view.organisationUnitLevel.id)) {
				GIS.logg.push([view.organisationUnitLevel.id, layer.id + '.organisationUnitLevel.id: string']);
				alert(GIS.i18n.no_level_selected);
				return false;
			}
			if (!view.organisationUnitLevel.name || !Ext.isString(view.organisationUnitLevel.name)) {
				GIS.logg.push([view.organisationUnitLevel.name, layer.id + '.organisationUnitLevel.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.organisationUnitLevel.level || !Ext.isNumber(view.organisationUnitLevel.level)) {
				GIS.logg.push([view.organisationUnitLevel.level, layer.id + '.organisationUnitLevel.level: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentOrganisationUnit.id || !Ext.isString(view.parentOrganisationUnit.id)) {
				GIS.logg.push([view.parentOrganisationUnit.id, layer.id + '.parentOrganisationUnit.id: string']);
				alert(GIS.i18n.no_parent_organisationunit_selected);
				return false;
			}
			if (!view.parentOrganisationUnit.name || !Ext.isString(view.parentOrganisationUnit.name)) {
				GIS.logg.push([view.parentOrganisationUnit.name, layer.id + '.parentOrganisationUnit.name: string']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentLevel || !Ext.isNumber(view.parentLevel)) {
				GIS.logg.push([view.parentLevel, layer.id + '.parentLevel: number']);
					//alert("validation failed"); //todo
				return false;
			}
			if (!view.parentGraph || !Ext.isString(view.parentGraph)) {
				GIS.logg.push([view.parentGraph, layer.id + '.parentGraph: string']);
					//alert("validation failed"); //todo
				return false;
			}

			if (view.parentOrganisationUnit.level > view.organisationUnitLevel.level) {
				GIS.logg.push([view.parentOrganisationUnit.level, view.organisationUnitLevel.level, layer.id + '.parentOrganisationUnit.level: number <= ' + layer.id + '.organisationUnitLevel.level']);
				alert(GIS.i18n.level_not_higher_than_parent_level);
				return false;
			}

			return view;
		};

		panel = Ext.create('Ext.panel.Panel', {
			map: layer.map,
			layer: layer,
			menu: layer.menu,

			reset: reset,
			setGui: setGui,
			getView: getView,

			infrastructuralDataElementValuesStore: infrastructuralDataElementValuesStore,

			cls: 'gis-form-widget el-border-0',
			border: false,
			items: [
				{
					xtype: 'form',
					cls: 'el-border-0',
					width: 270,
					items: [
						{
							html: GIS.i18n.organisationunit_groupset,
							cls: 'gis-form-subtitle-first'
						},
						groupSet,
						{
							html: GIS.i18n.organisation_unit_level_parent,
							cls: 'gis-form-subtitle'
						},
						level,
						parent,
						{
							html: GIS.i18n.surrounding_areas,
							cls: 'gis-form-subtitle'
						},
						areaRadius
					]
				}
			]
		});

		//createSelectHandlers();

		return panel;
	};

	// GUI

	GIS.app.init.onInitialize = function(r) {
		var layer,
			createViewport,
			initialize;

		createViewport = function() {
			var centerRegion,
				eastRegion,
				downloadButton,
				interpretationButton,
				resizeButton,
				viewport,
				onRender,
				afterRender;

			resizeButton = Ext.create('Ext.button.Button', {
				text: '>>>',
				handler: function() {
					eastRegion.toggleCollapse();
				}
			});

			centerRegion = new GeoExt.panel.Map({
				region: 'center',
				map: gis.olmap,
				cmp: {
					tbar: {}
				},
				tbar: {
					defaults: {
						height: 26
					},
					items: function() {
						var a = [];
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.facility.id,
							menu: gis.layer.facility.menu,
							width: 26
						});
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.boundary.id,
							menu: gis.layer.boundary.menu,
							width: 26
						});
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.thematic1.id,
							menu: gis.layer.thematic1.menu,
							width: 26
						});
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.thematic2.id,
							menu: gis.layer.thematic2.menu,
							width: 26
						});
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.thematic3.id,
							menu: gis.layer.thematic3.menu,
							width: 26
						});
						a.push({
							iconCls: 'gis-btn-icon-' + gis.layer.thematic4.id,
							menu: gis.layer.thematic4.menu,
							width: 26
						});
						a.push({
							text: GIS.i18n.favorites,
							menu: {},
							handler: function() {
								if (viewport.favoriteWindow && viewport.favoriteWindow.destroy) {
									viewport.favoriteWindow.destroy();
								}

								viewport.favoriteWindow = GIS.app.FavoriteWindow();
								viewport.favoriteWindow.show();
							}
						});
						if (gis.init.user.isAdmin) {
							a.push({
								text: GIS.i18n.legend,
								menu: {},
								handler: function() {
									if (viewport.legendSetWindow && viewport.legendSetWindow.destroy) {
										viewport.legendSetWindow.destroy();
									}

									viewport.legendSetWindow = GIS.app.LegendSetWindow();
									viewport.legendSetWindow.show();
								}
							});
						}
						a.push({
							xtype: 'tbseparator',
							height: 18,
							style: 'border-color: transparent #d1d1d1 transparent transparent; margin-right: 4px',
						});
						a.push({
							text: GIS.i18n.download,
							menu: {},
							disabled: true,
							handler: function() {
								if (viewport.downloadWindow && viewport.downloadWindow.destroy) {
									viewport.downloadWindow.destroy();
								}

								viewport.downloadWindow = GIS.app.DownloadWindow();
								viewport.downloadWindow.show();
							},
							xable: function() {
								if (gis.util.map.hasVisibleFeatures()) {
									this.enable();
								}
								else {
									this.disable();
								}
							},
							listeners: {
								added: function() {
									downloadButton = this;
								}
							}
						});
						a.push({
							text: GIS.i18n.share,
							menu: {},
							disabled: true,
							handler: function() {
								if (viewport.interpretationWindow && viewport.interpretationWindow.destroy) {
									viewport.interpretationWindow.destroy();
								}

								viewport.interpretationWindow = GIS.app.InterpretationWindow();
								viewport.interpretationWindow.show();
							},
							listeners: {
								added: function() {
									interpretationButton = this;
								}
							}
						});
						a.push('->');

						a.push({
							text: GIS.i18n.table,
                            toggleGroup: 'module',
							handler: function(b) {
                                window.location.href = '../../dhis-web-pivot/app/index.html';
							}
						});

						a.push({
							text: GIS.i18n.chart,
                            toggleGroup: 'module',
							handler: function(b) {
                                window.location.href = '../../dhis-web-visualizer/app/index.html';
							}
						});

						a.push({
							text: GIS.i18n.map,
                            toggleGroup: 'module',
                            pressed: true
						});

						a.push({
							xtype: 'tbseparator',
							height: 18,
							style: 'border-color: transparent #d1d1d1 transparent transparent; margin-right: 6px; margin-left: 3px',
						});

                        a.push({
                            xtype: 'button',
                            text: GIS.i18n.home,
                            handler: function() {
                                window.location.href = '../../dhis-web-commons-about/redirect.action';
                            }
						});

						a.push(resizeButton);

						return a;
					}()
				}
			});

			eastRegion = Ext.create('Ext.panel.Panel', {
				region: 'east',
				layout: 'anchor',
				width: 200,
				preventHeader: true,
				collapsible: true,
				collapseMode: 'mini',
				items: [
					{
						title: GIS.i18n.layer_stack_transparency,
						bodyStyle: 'padding: 4px 6px 3px',
						items: GIS.app.LayersPanel(),
						collapsible: true,
						animCollapse: false
					},
					{
						title: GIS.i18n.thematic_layer_1_legend,
						bodyStyle: 'padding: 4px 6px 6px; border: 0 none',
						collapsible: true,
						collapsed: true,
						animCollapse: false,
						listeners: {
							added: function() {
								gis.layer.thematic1.legendPanel = this;
							}
						}
					},
					{
						title: GIS.i18n.thematic_layer_2_legend,
						bodyStyle: 'padding: 4px 6px 6px; border: 0 none',
						collapsible: true,
						collapsed: true,
						animCollapse: false,
						listeners: {
							added: function() {
								gis.layer.thematic2.legendPanel = this;
							}
						}
					},
					{
						title: GIS.i18n.thematic_layer_3_legend,
						bodyStyle: 'padding: 4px 6px 6px; border: 0 none',
						collapsible: true,
						collapsed: true,
						animCollapse: false,
						listeners: {
							added: function() {
								gis.layer.thematic3.legendPanel = this;
							}
						}
					},
					{
						title: GIS.i18n.thematic_layer_4_legend,
						bodyStyle: 'padding: 4px 6px 6px; border: 0 none',
						collapsible: true,
						collapsed: true,
						animCollapse: false,
						listeners: {
							added: function() {
								gis.layer.thematic4.legendPanel = this;
							}
						}
					},
					{
						title: GIS.i18n.facility_layer_legend,
						bodyStyle: 'padding: 4px 6px 6px; border: 0 none',
						collapsible: true,
						collapsed: true,
						animCollapse: false,
						listeners: {
							added: function() {
								gis.layer.facility.legendPanel = this;
							}
						}
					}
				],
				listeners: {
					collapse: function() {
						resizeButton.setText('<<<');
					},
					expand: function() {
						resizeButton.setText('>>>');
					}
				}
			});

			onRender = function(vp) {
				gis.olmap.mask = Ext.create('Ext.LoadMask', vp.getEl(), {
					msg: 'Loading'
				});
			};

			afterRender = function() {

				// Map tools
				Ext.query('.zoomInButton')[0].innerHTML = '<img src="images/zoomin_24.png" />';
				Ext.query('.zoomOutButton')[0].innerHTML = '<img src="images/zoomout_24.png" />';
				Ext.query('.zoomVisibleButton')[0].innerHTML = '<img src="images/zoomvisible_24.png" />';
				Ext.query('.measureButton')[0].innerHTML = '<img src="images/measure_24.png" />';

				gis.olmap.events.register('click', null, function(e) {
					if (gis.olmap.relocate.active) {
						var el = document.getElementById('mouseposition').childNodes[0],
							coordinates = '[' + el.childNodes[1].data + ',' + el.childNodes[3].data + ']',
							center = gis.viewport.centerRegion;

						Ext.Ajax.request({
							url: gis.baseUrl + gis.conf.url.path_gis + 'updateOrganisationUnitCoordinates.action',
							method: 'POST',
							params: {id: gis.olmap.relocate.feature.attributes.id, coordinates: coordinates},
							success: function(r) {
								gis.olmap.relocate.active = false;
								gis.olmap.relocate.window.destroy();

								gis.olmap.relocate.feature.move({x: parseFloat(e.clientX - center.x), y: parseFloat(e.clientY - 28)});
								gis.olmap.getViewport().style.cursor = 'auto';

								console.log(gis.olmap.relocate.feature.attributes.name + ' relocated to ' + coordinates);
							}
						});
					}
				});

				// Favorite
				var id = gis.util.url.getUrlParam('id');

				if (id) {
					gis.map = {
						id: id
					};
					GIS.core.MapLoader(gis).load();
				}

                // Background
				if (!window.google) {
					gis.layer.openStreetMap.item.setValue(false);
				}
			};

			viewport = Ext.create('Ext.container.Viewport', {
				id: 'viewport',
				layout: 'border',
				eastRegion: eastRegion,
				centerRegion: centerRegion,
				downloadButton: downloadButton,
				interpretationButton: interpretationButton,
				items: [
					centerRegion,
					eastRegion
				],
				listeners: {
					render: function() {
						onRender(this);
					},
					afterrender: function() {
						afterRender();
					}
				}
			});

			return viewport;
		};

		initialize = function() {
			gis.init = GIS.app.getInits(r);
			gis.baseUrl = gis.init.contextPath;

			gis.util = GIS.app.getUtils();
			gis.store = GIS.app.getStores();

			layer = gis.layer.facility;
			layer.menu = GIS.app.LayerMenu(layer, 'gis-toolbar-btn-menu-first');
			layer.widget = GIS.app.LayerWidgetFacility(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			layer = gis.layer.boundary;
			layer.menu = GIS.app.LayerMenu(layer);
			layer.widget = GIS.app.LayerWidgetBoundary(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			layer = gis.layer.thematic1;
			layer.menu = GIS.app.LayerMenu(layer);
			layer.widget = GIS.app.LayerWidgetThematic(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			layer = gis.layer.thematic2;
			layer.menu = GIS.app.LayerMenu(layer);
			layer.widget = GIS.app.LayerWidgetThematic(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			layer = gis.layer.thematic3;
			layer.menu = GIS.app.LayerMenu(layer);
			layer.widget = GIS.app.LayerWidgetThematic(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			layer = gis.layer.thematic4;
			layer.menu = GIS.app.LayerMenu(layer);
			layer.widget = GIS.app.LayerWidgetThematic(layer);
			layer.window = GIS.app.WidgetWindow(layer);
			GIS.core.createSelectHandlers(gis, layer);

			gis.viewport = createViewport();
			gis.viewport.gis = gis;
		}();
	};

	Ext.Ajax.request({
		url: gis.baseUrl + gis.conf.url.path_gis + 'initialize.action',
		success: function(r) {
			GIS.app.init.onInitialize(r);
	}});
});
