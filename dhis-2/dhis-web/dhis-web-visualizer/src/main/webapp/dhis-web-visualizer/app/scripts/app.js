DV.app = {};
DV.app.init = {};

Ext.onReady( function() {
	Ext.Ajax.method = 'GET';
	Ext.QuickTips.init();

	document.body.oncontextmenu = function() {
		return false;
	};

    Ext.override(Ext.LoadMask, {
		onHide: function() {
			this.callParent();
		}
	});

	// Init

	var dv = DV.core.getInstance();
	DV.core.instances.push(dv);

	DV.app.getInit = function(r) {
		var init = Ext.decode(r.responseText);

		for (var i = 0; i < init.rootNodes.length; i++) {
			init.rootNodes[i].path = '/' + dv.conf.finals.root.id + '/' + init.rootNodes[i].id;
		}

		// Dynamic dimensions

			// Sort by name
		init.dimensions = dv.util.array.sortObjectsByString(init.dimensions);

			// Extend
		for (var i = 0, dim; i < init.dimensions.length; i++) {
			dim = init.dimensions[i];
			dim.dimensionName = dim.id;
			dim.objectName = dv.conf.finals.dimension.dimension.objectName;
			dv.conf.finals.dimension.objectNameMap[dim.id] = dim;
		}

		init.afterRender = function() {

			// Resize event handler
			dv.viewport.westRegion.on('resize', function() {
				var panel = dv.util.dimension.panel.getExpanded();

				if (panel) {
					panel.onExpand();
				}
			});

			// Left gui
			var viewportHeight = dv.viewport.westRegion.getHeight(),
				numberOfTabs = dv.init.dimensions.length + 5,
				tabHeight = 28,
				minPeriodHeight = 380,
				settingsHeight = 91;

			if (viewportHeight > numberOfTabs * tabHeight + minPeriodHeight + settingsHeight) {
				if (!Ext.isIE) {
					dv.viewport.accordion.setAutoScroll(false);
					dv.viewport.westRegion.setWidth(dv.conf.layout.west_width);
					dv.viewport.accordion.doLayout();
				}
			}
			else {
				dv.viewport.westRegion.hasScrollbar = true;
			}

			dv.cmp.dimension.panels[0].expand();

			// Load favorite from url
			var id = dv.util.url.getUrlParam('id');

			if (id) {
				dv.util.chart.loadTable(id);
			}

			// Fade in
			Ext.defer( function() {
				Ext.getBody().fadeIn({
					duration: 400
				});
			}, 500 );
		};

		return init;
	};

	DV.app.getCmp = function() {
		var cmp = {
			region: {},
			charttype: [],
			layout: {},
			dimension: {
				panels: [],

				indicator: {},
				dataElement: {},
				dataSet: {},
				relativePeriod: {
					checkbox: []
				},
				fixedPeriod: {},
				organisationUnit: {}
			},
			favorite: {}
		};

		return cmp;
	};

	DV.app.getUtil = function() {
		var util = dv.util || {};

		util.chart.getLayoutConfig = function() {
			var panels = dv.cmp.dimension.panels,
				seriesDimensionName = dv.viewport.series.getValue(),
				categoryDimensionName = dv.viewport.category.getValue(),
				filterDimensionNames = dv.viewport.filter.getValue(),
				config = dv.viewport.optionsWindow.getOptions();

			config.columns = [];
			config.rows = [];
			config.filters = [];

			// Columns, rows, filters
			for (var i = 0, dxItems = [], dim; i < panels.length; i++) {
				dim = panels[i].getData();
				if (dim) {
					if (dim.dimensionName === seriesDimensionName) {
						config.columns.push({
							dimension: dim.objectName,
							items: dim.items
						});
					}
					else if (dim.dimensionName === categoryDimensionName) {
						config.rows.push({
							dimension: dim.objectName,
							items: dim.items
						});
					}
					else if (Ext.Array.contains(filterDimensionNames, dim.dimensionName)) {
						config.filters.push({
							dimension: dim.objectName,
							items: dim.items
						});
					}
				}
			}

			config.type = dv.viewport.chartType.getChartType();
			config.userOrganisationUnit = dv.viewport.userOrganisationUnit.getValue();
			config.userOrganisationUnitChildren = dv.viewport.userOrganisationUnitChildren.getValue();

			return config;
		};

		util.chart.submitSvgForm = function(type) {
			var svg = Ext.query('svg'),
				form = Ext.query('#exportForm')[0];

			if (!(Ext.isArray(svg) && svg.length)) {
				alert('Browser does not support SVG');
				return;
			}

			svg = Ext.get(svg[0]);
			svg = svg.parent().dom.innerHTML;

			Ext.query('#svgField')[0].value = svg;
			Ext.query('#typeField')[0].value = type;
			Ext.query('#nameField')[0].value = 'test';

			form.action = '../exportImage.action';
			form.submit();
		};

		util.dimension = {
			panel: {
				setHeight: function(mx) {
					var settingsHeight = 91,
						panelHeight = settingsHeight + dv.cmp.dimension.panels.length * 28,
						height;

					if (dv.viewport.westRegion.hasScrollbar) {
						height = panelHeight + mx;
						dv.viewport.accordion.setHeight(dv.viewport.getHeight() - settingsHeight - 2);
						dv.viewport.accordionBody.setHeight(height - settingsHeight - 2);
					}
					else {
						height = dv.viewport.westRegion.getHeight() - dv.conf.layout.west_fill - settingsHeight;
						mx += panelHeight;
						dv.viewport.accordion.setHeight((height > mx ? mx : height) - 2);
						dv.viewport.accordionBody.setHeight((height > mx ? mx : height) - 2);
					}
				},

				getExpanded: function() {
					for (var i = 0, panel; i < dv.cmp.dimension.panels.length; i++) {
						panel = dv.cmp.dimension.panels[i];

						if (!panel.collapsed) {
							return panel;
						}
					}

					return null;
				}
			}
		};

		util.url = {
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
			}
		};

		util.multiselect = {
			select: function(a, s) {
				var selected = a.getValue();
				if (selected.length) {
					var array = [];
					Ext.Array.each(selected, function(item) {
						array.push({id: item, name: a.store.getAt(a.store.findExact('id', item)).data.name});
					});
					s.store.add(array);
				}
				this.filterAvailable(a, s);
			},
			selectAll: function(a, s, doReverse) {
				var array = [];
				a.store.each( function(r) {
					array.push({id: r.data.id, name: r.data.name});
				});
				if (doReverse) {
					array.reverse();
				}
				s.store.add(array);
				this.filterAvailable(a, s);
			},
			unselect: function(a, s) {
				var selected = s.getValue();
				if (selected.length) {
					Ext.Array.each(selected, function(item) {
						s.store.remove(s.store.getAt(s.store.findExact('id', item)));
					});
					this.filterAvailable(a, s);
				}
			},
			unselectAll: function(a, s) {
				s.store.removeAll();
				a.store.clearFilter();
				this.filterAvailable(a, s);
			},
			filterAvailable: function(a, s) {
				a.store.filterBy( function(r) {
					var keep = true;
					s.store.each( function(r2) {
						if (r.data.id == r2.data.id) {
							keep = false;
						}

					});
					return keep;
				});
				a.store.sortStore();
			},
			setHeight: function(ms, panel, fill) {
				for (var i = 0; i < ms.length; i++) {
					ms[i].setHeight(panel.getHeight() - fill);
				}
			}
		};

		util.button = {
			type: {
				getValue: function() {
					for (var i = 0; i < dv.cmp.charttype.length; i++) {
						if (dv.cmp.charttype[i].pressed) {
							return dv.cmp.charttype[i].name;
						}
					}
				},
				setValue: function(type) {
					for (var i = 0; i < dv.cmp.charttype.length; i++) {
						dv.cmp.charttype[i].toggle(dv.cmp.charttype[i].name === type);
					}
				},
				toggleHandler: function(b) {
					if (!b.pressed) {
						b.toggle();
					}
				}
			}
		};

		util.checkbox = {
			setRelativePeriods: function(rp) {
				if (rp) {
					for (var key in rp) {
						var cmp = dv.util.getCmp('checkbox[relativePeriodId="' + key + '"]');
						if (cmp) {
							cmp.setValue(rp[key]);
						}
					}
				}
				else {
					dv.util.checkbox.setAllFalse();
				}
			},
			setAllFalse: function() {
				var a = dv.cmp.dimension.relativePeriod.checkbox;
				for (var i = 0; i < a.length; i++) {
					a[i].setValue(false);
				}
			},
			isAllFalse: function() {
				var a = dv.cmp.dimension.relativePeriod.checkbox;
				for (var i = 0; i < a.length; i++) {
					if (a[i].getValue()) {
						return false;
					}
				}
				return true;
			}
		};

		util.toolbar = {
			separator: {
				xtype: 'tbseparator',
				height: 26,
				style: 'border-left: 1px solid #d1d1d1; border-right: 1px solid #f1f1f1'
			}
		};

		util.window = dv.util.window || {};

		util.window.setAnchorPosition = function(w, target) {
			var vpw = dv.viewport.getWidth(),
				targetx = target ? target.getPosition()[0] : 4,
				winw = w.getWidth(),
				y = target ? target.getPosition()[1] + target.getHeight() + 4 : 33;

			if ((targetx + winw) > vpw) {
				w.setPosition((vpw - winw - 2), y);
			}
			else {
				w.setPosition(targetx, y);
			}
		};

        util.notification = {
            error: function(title, text) {
                title = title || '';
                text = text || '';
                Ext.create('Ext.window.Window', {
                    title: title,
                    cls: 'dv-messagebox',
                    iconCls: 'dv-window-title-messagebox',
                    modal: true,
                    width: 300,
                    items: [
                        {
                            xtype: 'label',
                            width: 40,
                            text: text
                        }
                    ]
                }).show();
                dv.cmp.statusbar.panel.setWidth(dv.cmp.region.center.getWidth());
                dv.cmp.statusbar.panel.update('<img src="' + dv.conf.finals.ajax.path_images + dv.conf.statusbar.icon.error + '" style="padding:0 5px 0 0"/>' + text);
            },
            warning: function(text) {
                text = text || '';
                dv.cmp.statusbar.panel.setWidth(dv.cmp.region.center.getWidth());
                dv.cmp.statusbar.panel.update('<img src="' + dv.conf.finals.ajax.path_images + dv.conf.statusbar.icon.warning + '" style="padding:0 5px 0 0"/>' + text);
            },
            ok: function() {
                dv.cmp.statusbar.panel.setWidth(dv.cmp.region.center.getWidth());
                dv.cmp.statusbar.panel.update('<img src="' + dv.conf.finals.ajax.path_images + dv.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>&nbsp;&nbsp;');
            },
            interpretation: function(text) {
                dv.cmp.statusbar.panel.setWidth(dv.cmp.region.center.getWidth());
                dv.cmp.statusbar.panel.update('<img src="' + dv.conf.finals.ajax.path_images + dv.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>' + text);
            }
        };

		util.store = {
			addToStorage: function(s, records) {
				s.each( function(r) {
					if (!s.storage[r.data.id]) {
						s.storage[r.data.id] = {id: r.data.id, name: r.data.name, parent: s.parent};
					}
				});
				if (records) {
					Ext.Array.each(records, function(r) {
						if (!s.storage[r.data.id]) {
							s.storage[r.data.id] = {id: r.data.id, name: r.data.name, parent: s.parent};
						}
					});
				}
			},
			loadFromStorage: function(s) {
				var items = [];
				s.removeAll();
				for (var obj in s.storage) {
					if (s.storage[obj].parent === s.parent) {
						items.push(s.storage[obj]);
					}
				}
				s.add(items);
				s.sort('name', 'ASC');
			},
			containsParent: function(s) {
				for (var obj in s.storage) {
					if (s.storage[obj].parent === s.parent) {
						return true;
					}
				}
				return false;
			}
		};

		util.mask = {
			showMask: function(cmp, msg) {
				cmp = cmp || dv.viewport;
				msg = msg || 'Loading..';

				if (dv.viewport.mask) {
					dv.viewport.mask.destroy();
				}
				dv.viewport.mask = new Ext.create('Ext.LoadMask', cmp, {
					id: 'dv-loadmask',
					shadow: false,
					msg: msg,
					style: 'box-shadow:0',
					bodyStyle: 'box-shadow:0'
				});
				dv.viewport.mask.show();
			},
			hideMask: function() {
				if (dv.viewport.mask) {
					dv.viewport.mask.hide();
				}
			}
		};

		util.object = {
			getLength: function(object) {
				var size = 0;

				for (var key in object) {
					if (object.hasOwnProperty(key)) {
						size++;
					}
				}

				return size;
			}
		};

		util.number = {
			getNumberOfDecimals: function(x) {
				var tmp = new String(x);
				return (tmp.indexOf(".") > -1) ? (tmp.length - tmp.indexOf(".") - 1) : 0;
			},

			roundIf: function(x, fix) {
				if (Ext.isString(x)) {
					x = parseFloat(x);
				}

				if (Ext.isNumber(x) && Ext.isNumber(fix)) {
					var dec = dv.util.number.getNumberOfDecimals(x);
					return parseFloat(dec > fix ? x.toFixed(fix) : x);
				}
				return x;
			},

			pp: function(x, nf) {
				nf = nf || 'space';

				if (nf === 'none') {
					return x;
				}

				return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, dv.conf.pivot.digitGroupSeparator[nf]);
			}
		};

		util.str = {
			replaceAll: function(str, find, replace) {
				return str.replace(new RegExp(find, 'g'), replace);
			}
		};

		return util;
	};

	DV.app.getStores = function() {
		var store = dv.store || {};

		store.indicatorAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'indicators'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			listeners: {
				load: function(s) {
					dv.util.store.addToStorage(s);
					dv.util.multiselect.filterAvailable({store: s}, {store: store.indicatorSelected});
				}
			}
		});

		store.indicatorSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.dataElementAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'dataElementId', 'optionComboId', 'operandName'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'dataElements'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			setTotalsProxy: function(uid) {
				var path;

				if (Ext.isString(uid)) {
					path = dv.conf.finals.ajax.dataelement_get + uid + '.json?links=false&paging=false';
				}
				else if (uid === 0) {
					path = dv.conf.finals.ajax.dataelement_getall;
				}

				if (!path) {
					alert('Invalid parameter');
					return;
				}

				this.setProxy({
					type: 'ajax',
					url: dv.conf.finals.ajax.path_api + path,
					reader: {
						type: 'json',
						root: 'dataElements'
					}
				});

				this.load({
					scope: this,
					callback: function() {
						this.sortStore();
					}
				});
			},
			setDetailsProxy: function(uid) {
				if (Ext.isString(uid)) {
					this.setProxy({
						type: 'ajax',
						url: dv.conf.finals.ajax.path_commons + 'getOperands.action?uid=' + uid,
						reader: {
							type: 'json',
							root: 'operands'
						}
					});

					this.load({
						scope: this,
						callback: function() {
							this.each(function(r) {
								r.set('id', r.data.dataElementId + '-' + r.data.optionComboId);
								r.set('name', r.data.operandName);
							});

							this.sortStore();
						}
					});
				}
				else {
					alert('Invalid parameter');
				}
			}
		});

		store.dataElementSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.dataSetAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: dv.conf.finals.ajax.path_api + dv.conf.finals.ajax.dataset_get,
				reader: {
					type: 'json',
					root: 'dataSets'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			isLoaded: false,
			listeners: {
				load: function(s) {
					this.isLoaded = true;
					dv.util.store.addToStorage(s);
					dv.util.multiselect.filterAvailable({store: s}, {store: store.dataSetSelected});
				}
			}
		});

		store.dataSetSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.periodType = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: dv.conf.period.periodTypes
		});

		store.fixedPeriodAvailable = Ext.create('Ext.data.Store', {
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

		store.fixedPeriodSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.charts = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated', 'access'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'charts'
				}
			},
			isLoaded: false,
			pageSize: 10,
			page: 1,
			defaultUrl: dv.baseUrl + '/api/charts.json?links=false',
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
				load: function(s) {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}

					this.sort('name', 'ASC');
				}
			}
		});

		store.getDimensionStore = function() {
			return Ext.create('Ext.data.Store', {
				fields: ['id', 'name'],
				data: function() {
					var dimConf = dv.conf.finals.dimension,
						data = [
							{id: dimConf.data.dimensionName, name: dimConf.data.name},
							{id: dimConf.period.dimensionName, name: dimConf.period.name},
							{id: dimConf.organisationUnit.dimensionName, name: dimConf.organisationUnit.name}
						];

					return data.concat(Ext.clone(dv.init.dimensions));
				}()
			});
		};

		return store;
	};

	DV.app.OptionsWindow = function() {
		var showTrendLine,
			targetLineValue,
			targetLineTitle,
			baseLineValue,
			baseLineTitle,

			showValues,
			hideLegend,
			hideTitle,
			title,
			domainAxisTitle,
			rangeAxisTitle,

			data,
			style,

			window;

		showTrendLine = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: DV.i18n.trend_line,
			style: 'margin-bottom:6px'
		});
		dv.viewport.showTrendLine = showTrendLine;

		targetLineValue = Ext.create('Ext.form.field.Number', {
			//cls: 'gis-numberfield',
			width: 60,
			height: 18,
			listeners: {
				change: function(nf) {
					targetLineTitle.xable();
				}
			}
		});
		dv.viewport.targetLineValue = targetLineValue;

		targetLineTitle = Ext.create('Ext.form.field.Text', {
			//cls: 'dv-textfield-alt1',
			style: 'margin-left:2px; margin-bottom:2px',
			fieldStyle: 'padding-left:3px',
			emptyText: DV.i18n.target,
			width: 120,
			maxLength: 100,
			enforceMaxLength: true,
			disabled: true,
			xable: function() {
				this.setDisabled(!targetLineValue.getValue() && !Ext.isNumber(targetLineValue.getValue()));
			}
		});
		dv.viewport.targetLineTitle = targetLineTitle;

		baseLineValue = Ext.create('Ext.form.field.Number', {
			//cls: 'gis-numberfield',
			width: 60,
			height: 18,
			listeners: {
				change: function(nf) {
					baseLineTitle.xable();
				}
			}
		});
		dv.viewport.baseLineValue = baseLineValue;

		baseLineTitle = Ext.create('Ext.form.field.Text', {
			//cls: 'dv-textfield-alt1',
			style: 'margin-left:2px',
			fieldStyle: 'padding-left:3px',
			emptyText: DV.i18n.base,
			width: 120,
			maxLength: 100,
			enforceMaxLength: true,
			disabled: true,
			xable: function() {
				this.setDisabled(!baseLineValue.getValue() && !Ext.isNumber(baseLineValue.getValue()));
			}
		});
		dv.viewport.baseLineTitle = baseLineTitle;

		showValues = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: DV.i18n.show_values,
			style: 'margin-bottom:4px',
			checked: true
		});
		dv.viewport.showValues = showValues;

		hideLegend = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: DV.i18n.hide_legend,
			style: 'margin-bottom:4px'
		});
		dv.viewport.hideLegend = hideLegend;

		hideTitle = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: DV.i18n.hide_chart_title,
			style: 'margin-bottom:6px',
			listeners: {
				change: function() {
					title.xable();
				}
			}
		});
		dv.viewport.hideTitle = hideTitle;

		title = Ext.create('Ext.form.field.Text', {
			//cls: 'dv-textfield-alt1',
			style: 'margin-bottom:2px; margin-left:2px',
			width: 310,
			fieldLabel: DV.i18n.chart_title,
			fieldLabel: 'Chart title',
			labelStyle: 'padding-left:16px',
			labelWidth: 123,
			maxLength: 100,
			enforceMaxLength: true,
			xable: function() {
				this.setDisabled(hideTitle.getValue());
			}
		});
		dv.viewport.title = title;

		domainAxisTitle = Ext.create('Ext.form.field.Text', {
			//cls: 'dv-textfield-alt1',
			style: 'margin-bottom:2px; margin-left:2px',
			width: 310,
			fieldLabel: DV.i18n.domain_axis_label,
			labelStyle: 'padding-left:16px',
			labelWidth: 123,
			maxLength: 100,
			enforceMaxLength: true
		});
		dv.viewport.domainAxisTitle = domainAxisTitle;

		rangeAxisTitle = Ext.create('Ext.form.field.Text', {
			//cls: 'dv-textfield-alt1',
			style: 'margin-bottom:0; margin-left:2px',
			width: 310,
			fieldLabel: DV.i18n.range_axis_label,
			labelStyle: 'padding-left:16px',
			labelWidth: 123,
			maxLength: 100,
			enforceMaxLength: true
		});
		dv.viewport.rangeAxisTitle = rangeAxisTitle;

		data = {
			xtype: 'container',
			bodyStyle: 'border:0 none',
			style: 'margin-left:14px',
			items: [
				showTrendLine,
				{
					xtype: 'container',
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						{
							bodyStyle: 'border:0 none; padding-top:3px; padding-left:18px; margin-right:5px',
							width: 130,
							html: 'Target value / title:'
						},
						targetLineValue,
						targetLineTitle
					]
				},
				{
					xtype: 'container',
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						{
							bodyStyle: 'border:0 none; padding-top:3px; padding-left:18px; margin-right:5px',
							width: 130,
							html: 'Base value / title:'
						},
						baseLineValue,
						baseLineTitle
					]
				}
			]
		};

		style = {
			bodyStyle: 'border:0 none',
			style: 'margin-left:14px',
			items: [
				showValues,
				hideLegend,
				hideTitle,
				title,
				domainAxisTitle,
				rangeAxisTitle
			]
		};

		window = Ext.create('Ext.window.Window', {
			title: DV.i18n.table_options,
			bodyStyle: 'background-color:#fff; padding:8px 8px 8px',
			closeAction: 'hide',
			autoShow: true,
			modal: true,
			resizable: false,
			hideOnBlur: true,
			getOptions: function() {
				return {
					showTrendLine: showTrendLine.getValue(),
					targetLineValue: targetLineValue.getValue(),
					targetLineTitle: targetLineTitle.getValue(),
					baseLineValue: baseLineValue.getValue(),
					baseLineTitle: baseLineTitle.getValue(),
					showValues: showValues.getValue(),
					hideLegend: hideLegend.getValue(),
					hideTitle: hideTitle.getValue(),
					title: title.getValue(),
					domainAxisTitle: domainAxisTitle.getValue(),
					rangeAxisTitle: rangeAxisTitle.getValue()
				};
			},
			items: [
				{
					bodyStyle: 'border:0 none; color:#222; font-size:12px; font-weight:bold',
					style: 'margin-bottom:6px',
					html: DV.i18n.data
				},
				data,
				{
					bodyStyle: 'border:0 none; padding:7px'
				},
				{
					bodyStyle: 'border:0 none; color:#222; font-size:12px; font-weight:bold',
					style: 'margin-bottom:6px',
					html: DV.i18n.style
				},
				style
			],
			bbar: [
				'->',
				{
					text: DV.i18n.hide,
					handler: function() {
						window.hide();
					}
				},
				{
					text: '<b>' + DV.i18n.update + '</b>',
					handler: function() {
						dv.viewport.updateViewport();
						window.hide();
					}
				}
			],
			listeners: {
				show: function(w) {
					dv.util.window.setAnchorPosition(w, dv.viewport.optionsButton);

					if (!w.hasHideOnBlurHandler) {
						dv.util.window.addHideOnBlurHandler(w);
					}
				}
			}
		});

		return window;
	};

	DV.app.FavoriteWindow = function() {

		// Objects
		var NameWindow,

		// Instances
			nameWindow,

		// Functions
			//getBody,

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

		dv.store.charts.on('load', function(store, records) {
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
				bodify,
				record = dv.store.charts.getById(id);

			bodify = function(xLayout) {
				if (xLayout.extended.objectNameRecordsMap[dv.conf.finals.dimension.operand.objectName]) {
					for (var i = 0, id; i < xLayout.columns[0].items.length; i++) {
						id = xLayout.columns[0].items[i].id;
						if (id.indexOf('-') !== -1) {
							xLayout.columns[0].items[i].id = id.substr(0, id.indexOf('-'));
							console.log(xLayout.columns[0].items[i].id);
						}
					}

					for (var i = 0, dim; i < xLayout.rows.length; i++) {
						dim = xLayout.rows[i];
						for (var j = 0, id; j < dim.items.length; j++) {
							id = dim.items[j].id;
							if (id.indexOf('-') !== -1) {
								id = id.substr(0, id.indexOf('-'));
							}
						}
					}

					for (var i = 0, dim; i < xLayout.filters.length; i++) {
						dim = xLayout.filters[i];
						for (var j = 0, id; j < dim.items.length; j++) {
							id = dim.items[j].id;
							if (id.indexOf('-') !== -1) {
								id = id.substr(0, id.indexOf('-'));
							}
						}
					}
				}

				return xLayout;
			};

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
				text: 'Create', //i18n
				handler: function() {
					var favorite = bodify(Ext.clone(dv.xLayout));
					favorite.name = nameTextfield.getValue();

					if (favorite && favorite.name) {

						// Server sync
						favorite.showData = favorite.showValues;
						favorite.targetLineLabel = favorite.targetLineTitle;
						favorite.baseLineLabel = favorite.baseLineTitle;
						favorite.domainAxisLabel = favorite.domainAxisTitle;
						favorite.rangeAxisLabel = favorite.rangeAxisTitle;

						delete favorite.showValues;
						delete favorite.targetLineTitle;
						delete favorite.baseLineTitle;
						delete favorite.domainAxisTitle;
						delete favorite.rangeAxisTitle;

						delete favorite.extended;

						// Request
						Ext.Ajax.request({
							url: dv.init.contextPath + '/api/charts/',
							method: 'POST',
							headers: {'Content-Type': 'application/json'},
							params: Ext.encode(favorite),
							failure: function(r) {
								dv.util.mask.hideMask();
								alert(r.responseText);
							},
							success: function(r) {
								var id = r.getAllResponseHeaders().location.split('/').pop();

								dv.favorite = favorite;

								dv.store.charts.loadStore();

								//dv.viewport.interpretationButton.enable();

								window.destroy();
							}
						});
					}
				}
			});

			updateButton = Ext.create('Ext.button.Button', {
				text: 'Update', //i18n
				handler: function() {
					var name = nameTextfield.getValue(),
						favorite;

					if (id && name) {
						Ext.Ajax.request({
							url: dv.init.contextPath + '/api/charts/' + id + '.json?links=false',
							method: 'GET',
							failure: function(r) {
								dv.util.mask.hideMask();
								alert(r.responseText);
							},
							success: function(r) {
								favorite = Ext.decode(r.responseText);
								favorite.name = name;

								Ext.Ajax.request({
									url: dv.init.contextPath + '/api/charts/' + favorite.id,
									method: 'PUT',
									headers: {'Content-Type': 'application/json'},
									params: Ext.encode(favorite),
									failure: function(r) {
										dv.util.mask.hideMask();
										alert(r.responseText);
									},
									success: function(r) {
										dv.store.charts.loadStore();
										window.destroy();
									}
								});
							}
						});
					}
				}
			});

			cancelButton = Ext.create('Ext.button.Button', {
				text: 'Cancel', //i18n
				handler: function() {
					window.destroy();
				}
			});

			window = Ext.create('Ext.window.Window', {
				title: id ? 'Rename favorite' : 'Create new favorite',
				//iconCls: 'dv-window-title-icon-favorite',
				bodyStyle: 'padding:2px; background:#fff',
				resizable: false,
				modal: true,
				items: nameTextfield,
				destroyOnBlur: true,
				bbar: [
					cancelButton,
					'->',
					id ? updateButton : createButton
				],
				listeners: {
					show: function(w) {
						dv.util.window.setAnchorPosition(w, addButton);

						if (!w.hasDestroyBlurHandler) {
							dv.util.window.addDestroyOnBlurHandler(w);
						}

						dv.viewport.favoriteWindow.destroyOnBlur = false;
					},
					destroy: function() {
						dv.viewport.favoriteWindow.destroyOnBlur = true;
					}
				}
			});

			return window;
		};

		addButton = Ext.create('Ext.button.Button', {
			text: 'Add new', //i18n
			width: 67,
			height: 26,
			style: 'border-radius: 1px;',
			menu: {},
			disabled: !Ext.isObject(dv.xLayout),
			handler: function() {
				nameWindow = new NameWindow(null, 'create');
				nameWindow.show();
			}
		});

		searchTextfield = Ext.create('Ext.form.field.Text', {
			width: windowCmpWidth - addButton.width - 11,
			height: 26,
			fieldStyle: 'padding-right: 0; padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
			emptyText: DV.i18n.search_for_favorites,
			enableKeyEvents: true,
			currentValue: '',
			listeners: {
				keyup: function() {
					if (this.getValue() !== this.currentValue) {
						this.currentValue = this.getValue();

						var value = this.getValue(),
							url = value ? dv.baseUrl + '/api/charts/query/' + value + '.json?links=false' : null,
							store = dv.store.charts;

						store.page = 1;
						store.loadStore(url);
					}
				}
			}
		});

		prevButton = Ext.create('Ext.button.Button', {
			text: DV.i18n.prev,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? dv.baseUrl + '/api/charts/query/' + value + '.json?links=false' : null,
					store = dv.store.charts;

				store.page = store.page <= 1 ? 1 : store.page - 1;
				store.loadStore(url);
			}
		});

		nextButton = Ext.create('Ext.button.Button', {
			text: DV.i18n.next,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? dv.baseUrl + '/api/charts/query/' + value + '.json?links=false' : null,
					store = dv.store.charts;

				store.page = store.page + 1;
				store.loadStore(url);
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'dv-label-info',
			width: 300,
			height: 22
		});

		grid = Ext.create('Ext.grid.Panel', {
			cls: 'dv-grid',
			scroll: false,
			hideHeaders: true,
			columns: [
				{
					dataIndex: 'name',
					sortable: false,
					width: windowCmpWidth - 88,
					renderer: function(value, metaData, record) {
						var fn = function() {
							var element = Ext.get(record.data.id);

							if (element) {
								element = element.parent('td');
								element.addClsOnOver('link');
								element.load = function() {
									favoriteWindow.hide();
									dv.util.chart.loadChart(record.data.id);
								};
								element.dom.setAttribute('onclick', 'Ext.get(this).load();');
							}
						};

						Ext.defer(fn, 100);

						return '<div id="' + record.data.id + '">' + value + '</div>';
					}
				},
				{
					xtype: 'actioncolumn',
					sortable: false,
					width: 80,
					items: [
						{
							iconCls: 'dv-grid-row-icon-edit',
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
							iconCls: 'dv-grid-row-icon-overwrite',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-overwrite' + (!record.data.access.update ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									message,
									favorite;

								if (record.data.access.update) {
									message = DV.i18n.overwrite_favorite + '?\n\n' + record.data.name;
									favorite = getBody();

									if (favorite) {
										favorite.name = record.data.name;

										if (confirm(message)) {
											Ext.Ajax.request({
												url: dv.baseUrl + '/api/charts/' + record.data.id,
												method: 'PUT',
												headers: {'Content-Type': 'application/json'},
												params: Ext.encode(favorite),
												success: function() {
													dv.favorite = favorite;
													//dv.viewport.interpretationButton.enable();
													dv.store.charts.loadStore();
												}
											});
										}
									}
									else {
										alert(DV.i18n.please_create_a_table_first);
									}
								}
							}
						},
						{
							iconCls: 'dv-grid-row-icon-sharing',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-sharing' + (!record.data.access.manage ? ' disabled' : '');
							},
							handler: function(grid, rowIndex) {
								var record = this.up('grid').store.getAt(rowIndex);

								if (record.data.access.manage) {
									Ext.Ajax.request({
										url: dv.init.contextPath + '/api/sharing?type=chart&id=' + record.data.id,
										method: 'GET',
										failure: function(r) {
											dv.util.mask.hideMask();
											alert(r.responseText);
										},
										success: function(r) {
											var sharing = Ext.decode(r.responseText),
												window = DV.app.SharingWindow(sharing);
											window.show();
										}
									});
								}
							}
						},
						{
							iconCls: 'dv-grid-row-icon-delete',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-delete' + (!record.data.access['delete'] ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									message;

								if (record.data.access['delete']) {
									message = DV.i18n.delete_favorite + '?\n\n' + record.data.name;

									if (confirm(message)) {
										Ext.Ajax.request({
											url: dv.baseUrl + '/api/charts/' + record.data.id,
											method: 'DELETE',
											success: function() {
												dv.store.charts.loadStore();
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
			store: dv.store.charts,
			bbar: [
				info,
				'->',
				prevButton,
				nextButton
			],
			listeners: {
				added: function() {
					dv.viewport.favoriteGrid = this;
				},
				render: function() {
					var size = Math.floor((dv.viewport.centerRegion.getHeight() - 155) / dv.conf.layout.grid_row_height);
					this.store.pageSize = size;
					this.store.page = 1;
					this.store.loadStore();

					dv.store.charts.on('load', function() {
						if (this.isVisible()) {
							this.fireEvent('afterrender');
						}
					}, this);
				},
				afterrender: function() {
					var fn = function() {
						var editArray = Ext.query('.tooltip-favorite-edit'),
							overwriteArray = Ext.query('.tooltip-favorite-overwrite'),
							//dashboardArray = Ext.query('.tooltip-favorite-dashboard'),
							sharingArray = Ext.query('.tooltip-favorite-sharing'),
							deleteArray = Ext.query('.tooltip-favorite-delete'),
							el;

						for (var i = 0; i < editArray.length; i++) {
							var el = editArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: DV.i18n.rename,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < overwriteArray.length; i++) {
							el = overwriteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: DV.i18n.overwrite,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < sharingArray.length; i++) {
							el = sharingArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: DV.i18n.share_with_other_people,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < deleteArray.length; i++) {
							el = deleteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: DV.i18n.delete_,
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
			title: DV.i18n.manage_favorites,
			//iconCls: 'dv-window-title-icon-favorite',
			bodyStyle: 'padding:5px; background-color:#fff',
			resizable: false,
			modal: true,
			width: windowWidth,
			destroyOnBlur: true,
			items: [
				{
					xtype: 'panel',
					layout: 'hbox',
					bodyStyle: 'border:0 none',
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
				show: function(w) {
					dv.util.window.setAnchorPosition(w, dv.viewport.favoriteButton);

					if (!w.hasDestroyOnBlurHandler) {
						dv.util.window.addDestroyOnBlurHandler(w);
					}
				}
			}
		});

		return favoriteWindow;
	};

	DV.app.SharingWindow = function(sharing) {

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
					{id: 'r-------', name: 'Can view'}, //i18n
					{id: 'rw------', name: 'Can edit and view'}
				];

				if (isPublicAccess) {
					data.unshift({id: '-------', name: 'None'});
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
					fieldLabel: isPublicAccess ? 'Public access' : obj.name, //i18n
					labelStyle: 'color:#333',
					cls: 'dv-combo',
					fieldStyle: 'padding-left:5px',
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
						id: dv.init.user.id,
						name: dv.init.user.name
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
				url: dv.init.contextPath + '/api/sharing/search',
				reader: {
					type: 'json',
					root: 'userGroups'
				}
			}
		});

		userGroupField = Ext.create('Ext.form.field.ComboBox', {
			valueField: 'id',
			displayField: 'name',
			emptyText: 'Search for user groups', //i18n
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
			title: 'Sharing layout',
			bodyStyle: 'padding:6px 6px 0px; background-color:#fff',
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
							url: dv.init.contextPath + '/api/sharing?type=chart&id=' + sharing.object.id,
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
					var pos = dv.viewport.favoriteWindow.getPosition();
					w.setPosition(pos[0] + 5, pos[1] + 5);

					if (!w.hasDestroyOnBlurHandler) {
						dv.util.window.addDestroyOnBlurHandler(w);
					}

					dv.viewport.favoriteWindow.destroyOnBlur = false;
				},
				destroy: function() {
					dv.viewport.favoriteWindow.destroyOnBlur = true;
				}
			}
		});

		return window;
	};

	DV.app.init.onInitialize = function(r) {
		var createViewport;

		createViewport = function() {
			var buttons = [],
				buttonAddedListener,
				column,
				stackedColumn,
				bar,
				stackedBar,
				line,
				area,
				pie,
				buttons = [],
				buttonAddedListener,
				chartType,
				seriesStore,
				categoryStore,
				filterStore,
				series,
				category,
				filter,
				layout,

				indicatorAvailable,
				indicatorSelected,
				indicator,
				dataElementAvailable,
				dataElementSelected,
				dataElementGroupStore,
				dataElementGroupComboBox,
				dataElementDetailLevel,
				dataElement,
				dataSetAvailable,
				dataSetSelected,
				dataSet,
				rewind,
				relativePeriod,
				fixedPeriodAvailable,
				fixedPeriodSelected,
				period,
				userOrganisationUnit,
				userOrganisationUnitChildren,
				treePanel,
				organisationUnit,
				dimensionIdAvailableStoreMap = {},
				dimensionIdSelectedStoreMap = {},
				getDimensionPanels,
				validateSpecialCases,
				update,

				optionsButton,
				favoriteButton,
				downloadButton,

				accordionBody,
				accordion,
				westRegion,
				centerRegion,

				setFavorite,

				viewport,
				addListeners;

			buttonAddedListener = function(b) {
				buttons.push(b);
			};

			column = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.column,
				icon: 'images/column.png',
				name: dv.conf.finals.chart.column,
				tooltipText: DV.i18n.column_chart,
				pressed: true,
				listeners: {
					added: buttonAddedListener
				}
			});

			stackedColumn = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.stackedColumn,
				icon: 'images/column-stacked.png',
				name: dv.conf.finals.chart.stackedcolumn,
				tooltipText: DV.i18n.stacked_column_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			bar = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.bar,
				icon: 'images/bar.png',
				name: dv.conf.finals.chart.bar,
				tooltipText: DV.i18n.bar_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			stackedBar = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.stackedBar,
				icon: 'images/bar-stacked.png',
				name: dv.conf.finals.chart.stackedbar,
				tooltipText: DV.i18n.stacked_bar_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			line = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.line,
				icon: 'images/line.png',
				name: dv.conf.finals.chart.line,
				tooltipText: DV.i18n.line_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			area = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.area,
				icon: 'images/area.png',
				name: dv.conf.finals.chart.area,
				tooltipText: DV.i18n.area_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			pie = Ext.create('Ext.button.Button', {
				xtype: 'button',
				chartType: dv.conf.finals.chart.pie,
				icon: 'images/pie.png',
				name: dv.conf.finals.chart.pie,
				tooltipText: DV.i18n.pie_chart,
				listeners: {
					added: buttonAddedListener
				}
			});

			chartType = Ext.create('Ext.toolbar.Toolbar', {
				height: 45,
				style: 'padding-top:0px; border-style:none',
				getChartType: function() {
					for (var i = 0; i < buttons.length; i++) {
						if (buttons[i].pressed) {
							return buttons[i].chartType;
						}
					}
				},
				setChartType: function(type) {
					for (var i = 0; i < buttons.length; i++) {
						if (buttons[i].chartType === type) {
							buttons[i].toggle(true);
						}
					}
				},
				defaults: {
					height: 40,
					toggleGroup: 'charttype',
					handler: dv.util.button.type.toggleHandler,
					listeners: {
						afterrender: function(b) {
							if (b.xtype === 'button') {
								Ext.create('Ext.tip.ToolTip', {
									target: b.getEl(),
									html: b.tooltipText,
									'anchor': 'bottom'
								});
							}
						}
					}
				},
				items: [
					{
						xtype: 'label',
						text: DV.i18n.chart_type,
						style: 'font-size:11px; font-weight:bold; padding:13px 8px 0 6px'
					},
					column,
					stackedColumn,
					bar,
					stackedBar,
					line,
					area,
					pie
				]
			});

			seriesStore = dv.store.getDimensionStore();
			categoryStore = dv.store.getDimensionStore();
			filterStore = dv.store.getDimensionStore();

			series = Ext.create('Ext.form.field.ComboBox', {
				cls: 'dv-combo',
				baseBodyCls: 'small',
				style: 'margin-bottom:0',
				name: dv.conf.finals.chart.series,
				queryMode: 'local',
				editable: false,
				valueField: 'id',
				displayField: 'name',
				width: (dv.conf.layout.west_fieldset_width / 3) - 1,
				value: dv.conf.finals.dimension.data.dimensionName,
				filterNext: function() {
					category.filter(this.getValue());
					filter.filter([this.getValue(), category.getValue()]);
				},
				store: seriesStore,
				listeners: {
					added: function(cb) {
						dv.cmp.layout.series = this;
						cb.filterNext();
					},
					select: function(cb) {
						cb.filterNext();
					}
				}
			});

			category = Ext.create('Ext.form.field.ComboBox', {
				cls: 'dv-combo',
				baseBodyCls: 'small',
				style: 'margin-bottom:0',
				name: dv.conf.finals.chart.category,
				queryMode: 'local',
				editable: false,
				lastQuery: '',
				valueField: 'id',
				displayField: 'name',
				width: (dv.conf.layout.west_fieldset_width / 3) - 1,
				value: dv.conf.finals.dimension.period.dimensionName,
				filter: function(value) {
					if (Ext.isString(value)) {
						if (value === this.getValue()) {
							this.clearValue();
						}

						this.store.clearFilter();

						this.store.filterBy(function(record, id) {
							return id !== value;
						});
					}
				},
				filterNext: function() {
					filter.filter([series.getValue(), this.getValue()]);
				},
				store: categoryStore,
				listeners: {
					added: function(cb) {
						dv.cmp.layout.category = this;
						cb.filterNext();
					},
					select: function(cb) {
						cb.filterNext();
					}
				}
			});

			filter = Ext.create('Ext.form.field.ComboBox', {
				cls: 'dv-combo',
				multiSelect: true,
				baseBodyCls: 'small',
				style: 'margin-bottom:0',
				name: dv.conf.finals.chart.filter,
				queryMode: 'local',
				editable: false,
				lastQuery: '',
				valueField: 'id',
				displayField: 'name',
				width: (dv.conf.layout.west_fieldset_width / 3) - 1,
				value: dv.conf.finals.dimension.organisationUnit.dimensionName,
				filter: function(values) {
					var a = Ext.clone(this.getValue()),
						b = [];

					for (var i = 0; i < a.length; i++) {
						if (!Ext.Array.contains(values, a[i])) {
							b.push(a[i]);
						}
					}

					this.clearValue();
					this.setValue(b);

					this.store.filterBy(function(record, id) {
						return !Ext.Array.contains(values, id);
					});
				},
				store: filterStore,
				listeners: {
					added: function() {
						dv.cmp.layout.filter = this;
					},
					beforedeselect: function(cb) {
						return cb.getValue().length !== 1;
					}
				}
			});

			layout = Ext.create('Ext.toolbar.Toolbar', {
				id: 'chartlayout_tb',
				style: 'padding:2px 0 0 2px; background:#f5f5f5; border:0 none; border-top:1px dashed #ccc; border-bottom:1px solid #ccc',
				height: 46,
				items: [
					{
						xtype: 'panel',
						bodyStyle: 'border-style:none; background-color:transparent; padding:0',
						items: [
							{
								xtype: 'label',
								text: DV.i18n.series,
								style: 'font-size:11px; font-weight:bold; padding:0 4px'
							},
							{ bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
							series
						]
					},
					{
						xtype: 'panel',
						bodyStyle: 'border-style:none; background-color:transparent; padding:0',
						items: [
							{
								xtype: 'label',
								text: DV.i18n.category,
								style: 'font-size:11px; font-weight:bold; padding:0 4px'
							},
							{ bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
							category
						]
					},
					{
						xtype: 'panel',
						bodyStyle: 'border-style:none; background-color:transparent; padding:0',
						items: [
							{
								xtype: 'label',
								text: DV.i18n.filters,
								style: 'font-size:11px; font-weight:bold; padding:0 4px'
							},
							{ bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
							filter
						]
					}
				]
			});

			indicatorAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-left',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: dv.store.indicatorAvailable,
				tbar: [
					{
						xtype: 'label',
						text: DV.i18n.available,
						cls: 'dv-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.select(indicatorAvailable, indicatorSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.selectAll(indicatorAvailable, indicatorSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.select(this, indicatorSelected);
						}, this);
					}
				}
			});

			indicatorSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-right',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: dv.store.indicatorSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselectAll(indicatorAvailable, indicatorSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselect(indicatorAvailable, indicatorSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: DV.i18n.selected,
						cls: 'dv-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.unselect(indicatorAvailable, this);
						}, this);
					}
				}
			});

			indicator = {
				xtype: 'panel',
				title: '<div class="dv-panel-title-data">' + DV.i18n.indicators + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
						dimensionName: dv.conf.finals.dimension.indicator.dimensionName,
						objectName: dv.conf.finals.dimension.indicator.objectName,
						items: []
					};

					dv.store.indicatorSelected.each( function(r) {
						data.items.push({id: r.data.id});
					});

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = dv.viewport.westRegion.hasScrollbar ?
						dv.conf.layout.west_scrollbarheight_accordion_indicator : dv.conf.layout.west_maxheight_accordion_indicator;
					dv.util.dimension.panel.setHeight(h);
					dv.util.multiselect.setHeight(
						[indicatorAvailable, indicatorSelected],
						this,
						dv.conf.layout.west_fill_accordion_indicator
					);
				},
				items: [
					{
						xtype: 'combobox',
						cls: 'dv-combo',
						style: 'margin-bottom:2px; margin-top:0px',
						width: dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding,
						valueField: 'id',
						displayField: 'name',
						emptyText: DV.i18n.select_indicator_group,
						editable: false,
						store: {
							xtype: 'store',
							fields: ['id', 'name', 'index'],
							proxy: {
								type: 'ajax',
								url: dv.conf.finals.ajax.path_api + dv.conf.finals.ajax.indicatorgroup_get,
								reader: {
									type: 'json',
									root: 'indicatorGroups'
								}
							},
							listeners: {
								load: function(s) {
									s.add({
										id: 0,
										name: '[ ' + DV.i18n.all_indicator_groups + ' ]',
										index: -1
									});
									s.sort([
										{
											property: 'index',
											direction: 'ASC'
										},
										{
											property: 'name',
											direction: 'ASC'
										}
									]);
								}
							}
						},
						listeners: {
							select: function(cb) {
								var store = dv.store.indicatorAvailable;
								store.parent = cb.getValue();

								if (dv.util.store.containsParent(store)) {
									dv.util.store.loadFromStorage(store);
									dv.util.multiselect.filterAvailable(indicatorAvailable, indicatorSelected);
								}
								else {
									if (cb.getValue() === 0) {
										store.proxy.url = dv.conf.finals.ajax.path_api + dv.conf.finals.ajax.indicator_getall;
										store.load();
									}
									else {
										store.proxy.url = dv.conf.finals.ajax.path_api + dv.conf.finals.ajax.indicator_get + cb.getValue() + '.json';
										store.load();
									}
								}
							}
						}
					},
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							indicatorAvailable,
							indicatorSelected
						]
					}
				],
				listeners: {
					added: function() {
						dv.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			dataElementAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-left',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: dv.store.dataElementAvailable,
				tbar: [
					{
						xtype: 'label',
						text: DV.i18n.available,
						cls: 'dv-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.select(dataElementAvailable, dataElementSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.selectAll(dataElementAvailable, dataElementSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.select(this, dataElementSelected);
						}, this);
					}
				}
			});

			dataElementSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-right',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: dv.store.dataElementSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselectAll(dataElementAvailable, dataElementSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselect(dataElementAvailable, dataElementSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: DV.i18n.selected,
						cls: 'dv-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.unselect(dataElementAvailable, this);
						}, this);
					}
				}
			});

			dataElementGroupStore = Ext.create('Ext.data.Store', {
				fields: ['id', 'name', 'index'],
				proxy: {
					type: 'ajax',
					url: dv.conf.finals.ajax.path_api + dv.conf.finals.ajax.dataelementgroup_get,
					reader: {
						type: 'json',
						root: 'dataElementGroups'
					}
				},
				listeners: {
					load: function(s) {
						if (dataElementDetailLevel.getValue() === dv.conf.finals.dimension.dataElement.objectName) {
							s.add({
								id: 0,
								name: '[ ' + DV.i18n.all_data_element_groups + ' ]',
								index: -1
							});
						}

						s.sort([
							{property: 'index', direction: 'ASC'},
							{property: 'name', direction: 'ASC'}
						]);
					}
				}
			});

			dataElementGroupComboBox = Ext.create('Ext.form.field.ComboBox', {
				cls: 'dv-combo',
				style: 'margin:0 2px 2px 0',
				width: dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding - 110,
				valueField: 'id',
				displayField: 'name',
				emptyText: DV.i18n.select_data_element_group,
				editable: false,
				store: dataElementGroupStore,
				loadAvailable: function() {
					var store = dv.store.dataElementAvailable,
						detailLevel = dataElementDetailLevel.getValue(),
						value = this.getValue();

					if (value !== null) {
						if (detailLevel === dv.conf.finals.dimension.dataElement.objectName) {
							store.setTotalsProxy(value);
						}
						else {
							store.setDetailsProxy(value);
						}
					}
				},
				listeners: {
					select: function(cb) {
						cb.loadAvailable();
					}
				}
			});

			dataElementDetailLevel = Ext.create('Ext.form.field.ComboBox', {
				cls: 'dv-combo',
				style: 'margin-bottom:2px',
				baseBodyCls: 'small',
				queryMode: 'local',
				editable: false,
				valueField: 'id',
				displayField: 'text',
				width: 110 - 2,
				value: dv.conf.finals.dimension.dataElement.objectName,
				store: {
					fields: ['id', 'text'],
					data: [
						{id: dv.conf.finals.dimension.dataElement.objectName, text: DV.i18n.totals},
						{id: dv.conf.finals.dimension.operand.objectName, text: DV.i18n.details}
					]
				},
				listeners: {
					select: function(cb) {
						var record = dataElementGroupStore.getById(0);

						if (cb.getValue() === dv.conf.finals.dimension.operand.objectName && record) {
							dataElementGroupStore.remove(record);
						}

						if (cb.getValue() === dv.conf.finals.dimension.dataElement.objectName && !record) {
							dataElementGroupStore.insert(0, {
								id: 0,
								name: '[ ' + DV.i18n.all_data_element_groups + ' ]',
								index: -1
							});
						}

						dataElementGroupComboBox.loadAvailable();
						dv.store.dataElementSelected.removeAll();
					}
				}
			});

			dataElement = {
				xtype: 'panel',
				title: '<div class="dv-panel-title-data">' + DV.i18n.data_elements + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var optionComboIds = [],
						data = {
							dimensionName: dv.conf.finals.dimension.dataElement.dimensionName,
							objectName: dataElementDetailLevel.getValue(),
							items: []
						};

					dv.store.dataElementSelected.each( function(r) {
						data.items.push({id: r.data.id});

						if (dataElementDetailLevel.getValue() === dv.conf.finals.dimension.operand.objectName) {
							optionComboIds.push(r.data.optionComboId);
						}
					});

					if (optionComboIds.length) {
						data.optionComboIds = optionComboIds;
					}

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = dv.viewport.westRegion.hasScrollbar ?
						dv.conf.layout.west_scrollbarheight_accordion_dataelement : dv.conf.layout.west_maxheight_accordion_dataelement;
					dv.util.dimension.panel.setHeight(h);
					dv.util.multiselect.setHeight(
						[dataElementAvailable, dataElementSelected],
						this,
						dv.conf.layout.west_fill_accordion_indicator
					);
				},
				items: [
					{
						xtype: 'container',
						layout: 'column',
						items: [
							dataElementGroupComboBox,
							dataElementDetailLevel
						]
					},
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							dataElementAvailable,
							dataElementSelected
						]
					}
				],
				listeners: {
					added: function() {
						dv.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			dataSetAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-left',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: dv.store.dataSetAvailable,
				tbar: [
					{
						xtype: 'label',
						text: DV.i18n.available,
						cls: 'dv-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.select(dataSetAvailable, dataSetSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.selectAll(dataSetAvailable, dataSetSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.select(this, dataSetSelected);
						}, this);
					}
				}
			});

			dataSetSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-right',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: dv.store.dataSetSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselectAll(dataSetAvailable, dataSetSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselect(dataSetAvailable, dataSetSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: DV.i18n.selected,
						cls: 'dv-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.unselect(dataSetAvailable, this);
						}, this);
					}
				}
			});

			dataSet = {
				xtype: 'panel',
				title: '<div class="dv-panel-title-data">' + DV.i18n.reporting_rates + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
						dimensionName: dv.conf.finals.dimension.dataSet.dimensionName,
						objectName: dv.conf.finals.dimension.dataSet.objectName,
						items: []
					};

					dv.store.dataSetSelected.each( function(r) {
						data.items.push({id: r.data.id});
					});

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = dv.viewport.westRegion.hasScrollbar ?
						dv.conf.layout.west_scrollbarheight_accordion_dataset : dv.conf.layout.west_maxheight_accordion_dataset;
					dv.util.dimension.panel.setHeight(h);
					dv.util.multiselect.setHeight(
						[dataSetAvailable, dataSetSelected],
						this,
						dv.conf.layout.west_fill_accordion_dataset
					);

					if (!dv.store.dataSetAvailable.isLoaded) {
						dv.store.dataSetAvailable.load();
					}
				},
				items: [
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							dataSetAvailable,
							dataSetSelected
						]
					}
				],
				listeners: {
					added: function() {
						dv.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			rewind = Ext.create('Ext.form.field.Checkbox', {
				relativePeriodId: 'rewind',
				boxLabel: 'Rewind one period',
				xable: function() {
					this.setDisabled(dv.util.checkbox.isAllFalse());
				}
			});

			relativePeriod = {
				xtype: 'panel',
				hideCollapseTool: true,
				autoScroll: true,
				bodyStyle: 'border:0 none',
				valueComponentMap: {},
				items: [
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.34,
								bodyStyle: 'border-style:none; padding:0 0 0 8px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.weeks,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_WEEK',
										boxLabel: DV.i18n.last_week
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_4_WEEKS',
										boxLabel: DV.i18n.last_4_weeks
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_12_WEEKS',
										boxLabel: DV.i18n.last_12_weeks
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.months,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_MONTH',
										boxLabel: DV.i18n.last_month
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_3_MONTHS',
										boxLabel: DV.i18n.last_3_months
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_12_MONTHS',
										boxLabel: DV.i18n.last_12_months,
										checked: true
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.bimonths,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_BIMONTH',
										boxLabel: DV.i18n.last_bimonth
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_6_BIMONTHS',
										boxLabel: DV.i18n.last_6_bimonths
									}
								]
							}
						]
					},
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.34,
								bodyStyle: 'border-style:none; padding:5px 0 0 10px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.quarters,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_QUARTER',
										boxLabel: DV.i18n.last_quarter
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_4_QUARTERS',
										boxLabel: DV.i18n.last_4_quarters
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none; padding:5px 0 0',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.sixmonths,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_SIX_MONTH',
										boxLabel: DV.i18n.last_sixmonth
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_2_SIXMONTHS',
										boxLabel: DV.i18n.last_2_sixmonths
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none; padding:5px 0 0',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.financial_years,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_FINANCIAL_YEAR',
										boxLabel: DV.i18n.last_financial_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_5_FINANCIAL_YEARS',
										boxLabel: DV.i18n.last_5_financial_years
									}
								]
							}

							//{
								//xtype: 'panel',
								//layout: 'anchor',
								//bodyStyle: 'border-style:none; padding:5px 0 0 46px',
								//defaults: {
									//labelSeparator: '',
									//style: 'margin-bottom:2px',
								//},
								//items: [
									//{
										//xtype: 'label',
										//text: 'Options',
										//cls: 'dv-label-period-heading-options'
									//},
									//rewind
								//]
							//}
						]
					},
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.35,
								bodyStyle: 'border-style:none; padding:5px 0 0 10px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												dv.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: DV.i18n.years,
										cls: 'dv-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'THIS_YEAR',
										boxLabel: DV.i18n.this_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_YEAR',
										boxLabel: DV.i18n.last_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_5_YEARS',
										boxLabel: DV.i18n.last_5_years
									}
								]
							}
						]
					}
				]
			};

			fixedPeriodAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-left',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				height: 180,
				valueField: 'id',
				displayField: 'name',
				store: dv.store.fixedPeriodAvailable,
				tbar: [
					{
						xtype: 'label',
						text: DV.i18n.available,
						cls: 'dv-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.select(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.selectAll(fixedPeriodAvailable, fixedPeriodSelected, true);
						}
					},
					' '
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.select(fixedPeriodAvailable, fixedPeriodSelected);
						}, this);
					}
				}
			});

			fixedPeriodSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'dv-toolbar-multiselect-right',
				width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
				height: 180,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: dv.store.fixedPeriodSelected,
				tbar: [
					' ',
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselectAll(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							dv.util.multiselect.unselect(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: DV.i18n.selected,
						cls: 'dv-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							dv.util.multiselect.unselect(fixedPeriodAvailable, fixedPeriodSelected);
						}, this);
					}
				}
			});

			period = {
				xtype: 'panel',
				title: '<div class="dv-panel-title-period">Periods</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
							dimensionName: dv.conf.finals.dimension.period.dimensionName,
							objectName: dv.conf.finals.dimension.period.objectName,
							items: []
						},
						chb = dv.cmp.dimension.relativePeriod.checkbox;

					dv.store.fixedPeriodSelected.each( function(r) {
						data.items.push({id: r.data.id});
					});

					for (var i = 0; i < chb.length; i++) {
						if (chb[i].getValue()) {
							data.items.push({id: chb[i].relativePeriodId});
						}
					}

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = dv.viewport.westRegion.hasScrollbar ?
						dv.conf.layout.west_scrollbarheight_accordion_period : dv.conf.layout.west_maxheight_accordion_period;
					dv.util.dimension.panel.setHeight(h);
					dv.util.multiselect.setHeight(
						[fixedPeriodAvailable, fixedPeriodSelected],
						this,
						dv.conf.layout.west_fill_accordion_period
					);
				},
				items: [
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						style: 'margin-top:0px',
						items: [
							{
								xtype: 'combobox',
								cls: 'dv-combo',
								style: 'margin-bottom:2px',
								width: dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding - 67 - 67 - 4,
								valueField: 'id',
								displayField: 'name',
								emptyText: DV.i18n.select_period_type,
								editable: false,
								queryMode: 'remote',
								store: dv.store.periodType,
								periodOffset: 0,
								listeners: {
									select: function() {
										var ptype = new PeriodType(),
											periodType = this.getValue();

										var periods = ptype.get(periodType).generatePeriods({
											offset: this.periodOffset,
											filterFuturePeriods: true,
											reversePeriods: true
										});

										dv.store.fixedPeriodAvailable.setIndex(periods);
										dv.store.fixedPeriodAvailable.loadData(periods);
										dv.util.multiselect.filterAvailable(fixedPeriodAvailable, fixedPeriodSelected);
									}
								}
							},
							{
								xtype: 'button',
								text: DV.i18n.prev_year,
								style: 'margin-left:2px; border-radius:2px',
								width: 67,
								height: 24,
								handler: function() {
									var cb = this.up('panel').down('combobox');
									if (cb.getValue()) {
										cb.periodOffset--;
										cb.fireEvent('select');
									}
								}
							},
							{
								xtype: 'button',
								text: DV.i18n.next_year,
								style: 'margin-left:2px; border-radius:2px',
								width: 67,
								height: 24,
								handler: function() {
									var cb = this.up('panel').down('combobox');
									if (cb.getValue() && cb.periodOffset < 0) {
										cb.periodOffset++;
										cb.fireEvent('select');
									}
								}
							}
						]
					},
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none; padding-bottom:2px',
						items: [
							fixedPeriodAvailable,
							fixedPeriodSelected
						]
					},
					relativePeriod
				],
				listeners: {
					added: function() {
						dv.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			treePanel = Ext.create('Ext.tree.Panel', {
				cls: 'dv-tree',
				style: 'border-top: 1px solid #ddd; padding-top: 1px',
				width: dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding,
				rootVisible: false,
				autoScroll: true,
				multiSelect: true,
				rendered: false,
				reset: function() {
					var rootNode = this.getRootNode().findChild('id', dv.init.rootNodes[0].id);
					this.collapseAll();
					this.expandPath(rootNode.getPath());
					this.getSelectionModel().select(rootNode);
				},
				selectRootIf: function() {
					if (this.getSelectionModel().getSelection().length < 1) {
						var node = this.getRootNode().findChild('id', dv.init.rootNodes[0].id);
						if (this.rendered) {
							this.getSelectionModel().select(node);
						}
						return node;
					}
				},
				numberOfRecords: 0,
				recordsToSelect: [],
				multipleSelectIf: function(doUpdate) {
					if (this.recordsToSelect.length === this.numberOfRecords) {
						this.getSelectionModel().select(this.recordsToSelect);
						this.recordsToSelect = [];
						this.numberOfRecords = 0;

						if (doUpdate) {
							update();
						}
					}
				},
				multipleExpand: function(id, path, doUpdate) {
					this.expandPath('/' + dv.conf.finals.root.id + path, 'id', '/', function() {
						var record = this.getRootNode().findChild('id', id, true);
						this.recordsToSelect.push(record);
						this.multipleSelectIf(doUpdate);
					}, this);
				},
				select: function(url, params) {
					if (!params) {
						params = {};
					}
					Ext.Ajax.request({
						url: url,
						method: 'GET',
						params: params,
						scope: this,
						success: function(r) {
							var a = Ext.decode(r.responseText).organisationUnits;
							this.numberOfRecords = a.length;
							for (var i = 0; i < a.length; i++) {
								this.multipleExpand(a[i].id, a[i].path);
							}
						}
					});
				},
				selectByGroup: function(id) {
					if (id) {
						var url = dv.conf.finals.ajax.path_visualizer + dv.conf.finals.ajax.organisationunit_getbygroup,
							params = {id: id};
						this.select(url, params);
					}
				},
				selectByLevel: function(level) {
					if (level) {
						var url = dv.conf.finals.ajax.path_visualizer + dv.conf.finals.ajax.organisationunit_getbylevel,
							params = {level: level};
						this.select(url, params);
					}
				},
				selectByIds: function(ids) {
					if (ids) {
						var url = dv.conf.finals.ajax.path_visualizer + dv.conf.finals.ajax.organisationunit_getbyids;
						Ext.Array.each(ids, function(item) {
							url = Ext.String.urlAppend(url, 'ids=' + item);
						});
						if (!this.rendered) {
							dv.cmp.dimension.organisationUnit.panel.expand();
						}
						this.select(url);
					}
				},
				store: Ext.create('Ext.data.TreeStore', {
					proxy: {
						type: 'ajax',
						url: dv.conf.finals.ajax.path_visualizer + dv.conf.finals.ajax.organisationunitchildren_get
					},
					root: {
						id: dv.conf.finals.root.id,
						expanded: true,
						children: dv.init.rootNodes
					}
				}),
				xable: function(checked, value) {
					this.setDisabled(!!(checked || value));
				},
				listeners: {
					added: function() {
						dv.cmp.dimension.organisationUnit.treepanel = this;
					},
					render: function() {
						this.rendered = true;
					},
					afterrender: function() {
						this.getSelectionModel().select(0);
					},
					itemcontextmenu: function(v, r, h, i, e) {
						v.getSelectionModel().select(r, false);

						if (v.menu) {
							v.menu.destroy();
						}
						v.menu = Ext.create('Ext.menu.Menu', {
							id: 'treepanel-contextmenu',
							showSeparator: false,
							shadow: false
						});
						if (!r.data.leaf) {
							v.menu.add({
								id: 'treepanel-contextmenu-item',
								text: DV.i18n.select_all_children,
								icon: 'images/node-select-child.png',
								handler: function() {
									r.expand(false, function() {
										v.getSelectionModel().select(r.childNodes, true);
										v.getSelectionModel().deselect(r);
									});
								}
							});
						}
						else {
							return;
						}

						v.menu.showAt(e.xy);
					}
				}
			});

			userOrganisationUnit = Ext.create('Ext.form.field.Checkbox', {
				columnWidth: 0.5,
				boxLabel: DV.i18n.user_organisation_unit,
				labelWidth: dv.conf.layout.form_label_width,
				handler: function(chb, checked) {
					treePanel.xable(checked, userOrganisationUnitChildren.getValue());
				}
			});

			userOrganisationUnitChildren = Ext.create('Ext.form.field.Checkbox', {
				columnWidth: 0.5,
				boxLabel: DV.i18n.user_organisation_unit_children,
				labelWidth: dv.conf.layout.form_label_width,
				handler: function(chb, checked) {
					treePanel.xable(checked, userOrganisationUnit.getValue());
				}
			});

			organisationUnit = {
				xtype: 'panel',
				title: '<div class="dv-panel-title-organisationunit">' + DV.i18n.organisation_units + '</div>',
				bodyStyle: 'padding-top:5px',
				hideCollapseTool: true,
				collapsed: false,
				getData: function() {
					var r = treePanel.getSelectionModel().getSelection(),
						data = {
							dimensionName: dv.conf.finals.dimension.organisationUnit.dimensionName,
							objectName: dv.conf.finals.dimension.organisationUnit.objectName,
							items: []
						};

					for (var i = 0; i < r.length; i++) {
						data.items.push({id: r[i].data.id});
					}

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = dv.viewport.westRegion.hasScrollbar ?
						dv.conf.layout.west_scrollbarheight_accordion_organisationunit : dv.conf.layout.west_maxheight_accordion_organisationunit;
					dv.util.dimension.panel.setHeight(h);
					treePanel.setHeight(this.getHeight() - dv.conf.layout.west_fill_accordion_organisationunit);
				},
				items: [
					{
						layout: 'column',
						bodyStyle: 'border:0 none; padding-bottom:3px; padding-left:7px',
						items: [
							userOrganisationUnit,
							userOrganisationUnitChildren
						]
					},
					treePanel
				],
				listeners: {
					added: function() {
						dv.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			getDimensionPanels = function(dimensions, iconCls) {
				var	getAvailableStore,
					getSelectedStore,

					createPanel,
					getPanels;

				getAvailableStore = function(dimension) {
					return Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						proxy: {
							type: 'ajax',
							url: dv.baseUrl + '/api/dimensions/' + dimension.id + '.json',
							reader: {
								type: 'json',
								root: 'items'
							}
						},
						isLoaded: false,
						storage: {},
						sortStore: function() {
							this.sort('name', 'ASC');
						},
						reset: function() {
							if (this.isLoaded) {
								this.removeAll();
								dv.util.store.loadFromStorage(this);
								this.sortStore();
							}
						},
						listeners: {
							load: function(s) {
								s.isLoaded = true;
								dv.util.store.addToStorage(s);
							}
						}
					});
				};

				getSelectedStore = function() {
					return Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data: []
					});
				};

				createPanel = function(dimension) {
					var getAvailable,
						getSelected,

						availableStore,
						selectedStore,
						available,
						selected,

						panel;

					getAvailable = function(availableStore) {
						return Ext.create('Ext.ux.form.MultiSelect', {
							cls: 'dv-toolbar-multiselect-left',
							width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
							valueField: 'id',
							displayField: 'name',
							store: availableStore,
							tbar: [
								{
									xtype: 'label',
									text: DV.i18n.available,
									cls: 'dv-toolbar-multiselect-left-label'
								},
								'->',
								{
									xtype: 'button',
									icon: 'images/arrowright.png',
									width: 22,
									handler: function() {
										dv.util.multiselect.select(available, selected);
									}
								},
								{
									xtype: 'button',
									icon: 'images/arrowrightdouble.png',
									width: 22,
									handler: function() {
										dv.util.multiselect.selectAll(available, selected);
									}
								}
							],
							listeners: {
								afterrender: function() {
									this.boundList.on('itemdblclick', function() {
										dv.util.multiselect.select(available, selected);
									}, this);
								}
							}
						});
					};

					getSelected = function(selectedStore) {
						return Ext.create('Ext.ux.form.MultiSelect', {
							cls: 'dv-toolbar-multiselect-right',
							width: (dv.conf.layout.west_fieldset_width - dv.conf.layout.west_width_padding) / 2,
							valueField: 'id',
							displayField: 'name',
							ddReorder: true,
							store: selectedStore,
							tbar: [
								{
									xtype: 'button',
									icon: 'images/arrowleftdouble.png',
									width: 22,
									handler: function() {
										dv.util.multiselect.unselectAll(available, selected);
									}
								},
								{
									xtype: 'button',
									icon: 'images/arrowleft.png',
									width: 22,
									handler: function() {
										dv.util.multiselect.unselect(available, selected);
									}
								},
								'->',
								{
									xtype: 'label',
									text: DV.i18n.selected,
									cls: 'dv-toolbar-multiselect-right-label'
								}
							],
							listeners: {
								afterrender: function() {
									this.boundList.on('itemdblclick', function() {
										dv.util.multiselect.unselect(available, selected);
									}, this);
								}
							}
						});
					};

					availableStore = getAvailableStore(dimension);
					selectedStore = getSelectedStore();

					dimensionIdAvailableStoreMap[dimension.id] = availableStore;
					dimensionIdSelectedStoreMap[dimension.id] = selectedStore;

					available = getAvailable(availableStore);
					selected = getSelected(selectedStore);

					availableStore.on('load', function() {
						dv.util.multiselect.filterAvailable(available, selected);
					});

					panel = {
						xtype: 'panel',
						title: '<div class="' + iconCls + '">' + dimension.name + '</div>',
						hideCollapseTool: true,
						availableStore: availableStore,
						selectedStore: selectedStore,
						getData: function() {
							var data = {
								dimensionName: dimension.id,
								objectName: dimension.id,
								items: []
							};

							selectedStore.each( function(r) {
								data.items.push({id: r.data.id});
							});

							return data.items.length ? data : null;
						},
						onExpand: function() {
							if (!availableStore.isLoaded) {
								availableStore.load();
							}

							var h = dv.viewport.westRegion.hasScrollbar ?
								dv.conf.layout.west_scrollbarheight_accordion_group : dv.conf.layout.west_maxheight_accordion_group;
							dv.util.dimension.panel.setHeight(h);

							dv.util.multiselect.setHeight(
								[available, selected],
								this,
								dv.conf.layout.west_fill_accordion_dataset
							);
						},
						items: [
							{
								xtype: 'panel',
								layout: 'column',
								bodyStyle: 'border-style:none',
								items: [
									available,
									selected
								]
							}
						],
						listeners: {
							added: function() {
								dv.cmp.dimension.panels.push(this);
							},
							expand: function(p) {
								p.onExpand();
							}
						}
					};

					return panel;
				};

				getPanels = function() {
					var panels = [];

					for (var i = 0, panel; i < dimensions.length; i++) {
						panel = createPanel(dimensions[i]);

						panels.push(panel);
					}

					return panels;
				};

				return getPanels();
			};

			validateSpecialCases = function(layout) {
				var dimConf = dv.conf.finals.dimension;

				// Indicator as filter
				for (var i = 0; i < layout.filters.length; i++) {
					if (layout.filters[i].dimension === dimConf.indicator.objectName) {
						alert(DV.i18n.indicators_cannot_be_specified_as_filter);
						return;
					}
				}

				// Categories as filter
				//if (layout.filter && dv.viewport.layoutWindow.filterStore.getById(dimConf.category.dimensionName)) {
					//alert(DV.i18n.categories_cannot_be_specified_as_filter);
					//return;
				//}

				// Degs and datasets in the same query
				//if (Ext.Array.contains(dimensionNames, dimConf.data.dimensionName) && dv.store.dataSetSelected.data.length) {
					//for (var i = 0; i < dv.init.degs.length; i++) {
						//if (Ext.Array.contains(dimensionNames, dv.init.degs[i].id)) {
							//alert(DV.i18n.data_element_group_sets_cannot_be_specified_together_with_data_sets);
							//return;
						//}
					//}
				//}

				return true;
			};

			update = function() {
				var config = dv.util.chart.getLayoutConfig(),
					layout = dv.api.Layout(config);

				if (!layout) {
					return;
				}
				if (!validateSpecialCases(layout)) {
					return;
				}

				dv.util.chart.createChart(layout, dv);
			};

			accordionBody = Ext.create('Ext.panel.Panel', {
				layout: 'accordion',
				activeOnTop: true,
				cls: 'dv-accordion',
				bodyStyle: 'border:0 none; margin-bottom:2px',
				height: 700,
				items: function() {
					var panels = [
						indicator,
						dataElement,
						dataSet,
						period,
						organisationUnit
					],
					dims = Ext.clone(dv.init.dimensions);

					dv.util.array.sortObjectsByString(dims);

					panels = panels.concat(getDimensionPanels(dims, 'dv-panel-title-dimension'));

					last = panels[panels.length - 1];
					last.cls = 'dv-accordion-last';

					return panels;
				}()
			});

			accordion = Ext.create('Ext.panel.Panel', {
				bodyStyle: 'border-style:none; padding:2px; padding-bottom:0; overflow-y:scroll;',
				items: accordionBody,
				listeners: {
					added: function() {
						dv.cmp.dimension.accordion = this;
					}
				}
			});

			westRegion = Ext.create('Ext.panel.Panel', {
				region: 'west',
				preventHeader: true,
				collapsible: true,
				collapseMode: 'mini',
				width: function() {
					if (Ext.isWebKit) {
						return dv.conf.layout.west_width + 8;
					}
					else {
						if (Ext.isLinux && Ext.isGecko) {
							return dv.conf.layout.west_width + 13;
						}
						return dv.conf.layout.west_width + 17;
					}
				}(),
				items: [
					chartType,
					layout,
					accordion
				]
			});

			optionsButton = Ext.create('Ext.button.Button', {
				text: DV.i18n.options,
				menu: {},
				handler: function() {
					if (!dv.viewport.optionsWindow) {
						dv.viewport.optionsWindow = DV.app.OptionsWindow();
					}

					dv.viewport.optionsWindow.show();
				}
			});

			favoriteButton = Ext.create('Ext.button.Button', {
				text: DV.i18n.favorites,
				menu: {},
				handler: function() {
					if (dv.viewport.favoriteWindow) {
						dv.viewport.favoriteWindow.destroy();
					}

					dv.viewport.favoriteWindow = DV.app.FavoriteWindow();
					dv.viewport.favoriteWindow.show();
				}
			});

			downloadButton = Ext.create('Ext.button.Button', {
				text: DV.i18n.download,
				disabled: true,
				menu: {
					cls: 'dv-menu',
					width: 105,
					shadow: false,
					showSeparator: false,
					items: [
						{
							text: DV.i18n.image_png,
							iconCls: 'dv-menu-item-image',
							handler: function() {
								dv.util.chart.submitSvgForm('png');
							}
						},
						{
							text: 'PDF',
							iconCls: 'dv-menu-item-image',
							handler: function() {
								dv.util.chart.submitSvgForm('pdf');
							}
						},
						{
							text: 'Excel (XLS)',
							iconCls: 'dv-menu-item-data',
							handler: function() {
								if (dv.baseUrl && dv.paramString) {
									window.location.href = dv.baseUrl + '/api/analytics.xls' + dv.paramString;
								}
							}
						},
						{
							text: 'CSV',
							iconCls: 'dv-menu-item-data',
							handler: function() {
								if (dv.baseUrl && dv.paramString) {
									window.location.href = dv.baseUrl + '/api/analytics.csv' + dv.paramString;
								}
							}
						},
						{
							text: 'JSON',
							iconCls: 'dv-menu-item-data',
							handler: function() {
								if (dv.baseUrl && dv.paramString) {
									window.open(dv.baseUrl + '/api/analytics.json' + dv.paramString);
								}
							}
						},
						{
							text: 'XML',
							iconCls: 'dv-menu-item-data',
							handler: function() {
								if (dv.baseUrl && dv.paramString) {
									window.open(dv.baseUrl + '/api/analytics.xml' + dv.paramString);
								}
							}
						}
					],
					listeners: {
						afterrender: function() {
							this.getEl().addCls('dv-toolbar-btn-menu');
						}
					}
				}
			});

			centerRegion = Ext.create('Ext.panel.Panel', {
				region: 'center',
				bodyStyle: 'padding:0; text-align:center',
				tbar: {
                    defaults: {
                        height: 26
                    },
					items: function() {
						var items = [],
							getSeparator;

						getSeparator = function() {
							return {
								xtype: 'tbseparator',
								height: 18,
								style: 'border-color:transparent; border-right-color:#d1d1d1; margin-right:4px'
							};
						};

						items.push({
							text: '<<<',
							handler: function(b) {
								var text = b.getText();
								text = text === '<<<' ? '>>>' : '<<<';
								b.setText(text);

								westRegion.toggleCollapse();
							}
						},
						{
							text: '<b>' + DV.i18n.update + '</b>',
							handler: function() {
								update();
							}
						},
						optionsButton,
						getSeparator(),
						favoriteButton,
						downloadButton,
						'->',
						{
							text: DV.i18n.table,
							toggleGroup: 'module',
							handler: function(b) {
								window.location.href = '../../dhis-web-pivot/app/index.html';
							}
						},
						{
							text: DV.i18n.chart,
							toggleGroup: 'module',
							pressed: true,
							handler: dv.util.button.type.toggleHandler
						},
						{
							text: DV.i18n.map,
							toggleGroup: 'module',
							handler: function(b) {
								window.location.href = '../../dhis-web-mapping/app/index.html';
							}
						},
						getSeparator(),
						{
							xtype: 'button',
							text: DV.i18n.home,
							handler: function() {
								window.location.href = '../../dhis-web-commons-about/redirect.action';
							}
						});

						return items;
					}()
				},
				listeners: {
					resize: function(p) {
						if (dv.xLayout && dv.chart) {
							dv.chart.onViewportResize();
						}
					}
				}
			});

			setFavorite = function(xLayout) {
				var seriesId = xLayout.extended.columnsDimensionNames[0],
					categoryId = xLayout.extended.rowsDimensionNames[0],
					filterIds = xLayout.extended.filtersDimensionNames,
					dimMap = xLayout.extended.objectNameDimensionMap,
					recMap = xLayout.extended.objectNameRecordsMap,
					graphMap = xLayout.extended.parentGraphMap,
					dimConf = dv.conf.finals.dimension,
					objectName,
					periodRecords,
					fixedPeriodRecords = [];
console.log("xLayout", xLayout);

				// Type
				dv.viewport.chartType.setChartType(xLayout.type);

				// Series, category, filter
				dv.viewport.series.setValue(seriesId);
				dv.viewport.category.setValue(categoryId);
				dv.viewport.filter.setValue(filterIds);

				// Indicators
				dv.store.indicatorSelected.removeAll();
				objectName = dimConf.indicator.objectName;
				if (dimMap[objectName]) {
					dv.store.indicatorSelected.add(Ext.clone(recMap[objectName]));
				}

				// Data elements
				dv.store.dataElementSelected.removeAll();
				objectName = dimConf.dataElement.objectName;
				if (dimMap[objectName]) {
					dv.store.dataElementSelected.add(Ext.clone(recMap[objectName]));
					dv.viewport.dataElementDetailLevel.setValue(objectName);
				}

				// Operands
				objectName = dimConf.operand.objectName;
				if (dimMap[objectName]) {
					dv.store.dataElementSelected.add(Ext.clone(recMap[objectName]));
					dv.viewport.dataElementDetailLevel.setValue(objectName);
				}

				// Data sets
				dv.store.dataSetSelected.removeAll();
				objectName = dimConf.dataSet.objectName;
				if (dimMap[objectName]) {
					dv.store.dataSetSelected.add(Ext.clone(recMap[objectName]));
				}

				// Periods
				dv.store.fixedPeriodSelected.removeAll();
				dv.util.checkbox.setAllFalse();
				periodRecords = recMap[dimConf.period.objectName] || [];
				for (var i = 0, peroid, checkbox; i < periodRecords.length; i++) {
					period = periodRecords[i];
					checkbox = relativePeriod.valueComponentMap[period.id];
					if (checkbox) {
						checkbox.setValue(true);
					}
					else {
						fixedPeriodRecords.push(period);
					}
				}
				dv.store.fixedPeriodSelected.add(fixedPeriodRecords);

				// Group sets

					// Reset stores
				for (var key in dimensionIdSelectedStoreMap) {
					if (dimensionIdSelectedStoreMap.hasOwnProperty(key)) {
						var a = dimensionIdAvailableStoreMap[key],
							s = dimensionIdSelectedStoreMap[key];

						if (s.getCount() > 0) {
							a.reload();
							s.removeAll();
						}
					}
				}

					// Add records
				for (var key in dimensionIdSelectedStoreMap) {
					if (dimensionIdSelectedStoreMap.hasOwnProperty(key) && recMap[key]) {
						dimensionIdSelectedStoreMap[key].add(recMap[key]);
					}
				}

				// Options
				//var showTrendLine,
			//targetLineValue,
			//targetLineTitle,
			//baseLineValue,
			//baseLineTitle,

			//showValues,
			//hideLegend,
			//hideTitle,
			//title,
			//domainAxisTitle,
			//rangeAxisTitle,

				if (Ext.isBoolean(xLayout.regression)) {
					dv.viewport.showTrendLine.setValue(xLayout.regression);
				}
				if (Ext.isBoolean(xLayout.showData)) {
					dv.viewport.showValues.setValue(xLayout.showData);
				}
				if (Ext.isBoolean(xLayout.hideLegend)) {
					dv.viewport.hideLegend.setValue(xLayout.hideLegend);
				}
				if (Ext.isBoolean(xLayout.hideTitle)) {
					dv.viewport.hideTitle.setValue(xLayout.hideTitle);
				}









				// Organisation units
				userOrganisationUnit.setValue(xLayout.userOrganisationUnit);
				userOrganisationUnitChildren.setValue(xLayout.userOrganisationUnitChildren);

				// If fav has organisation units, wait for tree callback before update
				if (recMap[dimConf.organisationUnit.objectName] && graphMap) {
					treePanel.numberOfRecords = dv.util.object.getLength(graphMap);
					for (var key in graphMap) {
						if (graphMap.hasOwnProperty(key)) {
							treePanel.multipleExpand(key, graphMap[key], true);
						}
					}
				}
				else {
					treePanel.reset();
					update();
				}
			};

			viewport = Ext.create('Ext.container.Viewport', {
				layout: 'border',
				chartType: chartType,
				series: series,
				category: category,
				filter: filter,
				accordion: accordion,
				accordionBody: accordionBody,
				westRegion: westRegion,
				centerRegion: centerRegion,
				updateViewport: update,
				optionsButton: optionsButton,
				favoriteButton: favoriteButton,
				downloadButton: downloadButton,
				userOrganisationUnit: userOrganisationUnit,
				userOrganisationUnitChildren: userOrganisationUnitChildren,
				dataElementDetailLevel: dataElementDetailLevel,
				setFavorite: setFavorite,
				items: [
					westRegion,
					centerRegion
				],
				listeners: {
					render: function(vp) {
						dv.viewport = vp;
					},
					afterrender: function() {
						dv.init.afterRender();
					}
				}
			});

			addListeners = function() {
				dv.store.indicatorAvailable.on('load', function() {
					dv.util.multiselect.filterAvailable(indicatorAvailable, indicatorSelected);
				});

				dv.store.dataElementAvailable.on('load', function() {
					dv.util.multiselect.filterAvailable(dataElementAvailable, dataElementSelected);
				});

				dv.store.dataSetAvailable.on('load', function(s) {
					dv.util.multiselect.filterAvailable(dataSetAvailable, dataSetSelected);
					s.sort('name', 'ASC');
				});
			}();

			return viewport;
		};

		dv.init = DV.app.getInit(r);
		dv.baseUrl = dv.init.contextPath;

		dv.cmp = DV.app.getCmp();
		dv.util = DV.app.getUtil();
		dv.store = DV.app.getStores();

		dv.viewport = createViewport();

		dv.viewport.optionsWindow = DV.app.OptionsWindow();
		dv.viewport.optionsWindow.hide();
	};

	Ext.Ajax.request({
		url: dv.conf.finals.ajax.path_visualizer + 'initialize.action',
		success: function(r) {
			DV.app.init.onInitialize(r);
	}});
});

