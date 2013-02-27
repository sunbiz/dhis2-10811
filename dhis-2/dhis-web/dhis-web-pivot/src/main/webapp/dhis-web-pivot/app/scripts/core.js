PT = {};

PT.core = {};

Ext.onReady( function() {

PT.core.getConfigs = function() {
	var conf = {};

	conf.finals = {
        ajax: {
            path_pivot: '../',
            path_pivot_static: 'dhis-web-pivot/',
            path_api: '../../api/',
            path_commons: '../../dhis-web-commons-ajax-json/',
            path_lib: '../../dhis-web-commons/javascripts/',
            path_images: 'images/',
            initialize: 'initialize.action',
            redirect: 'dhis-web-commons-about/redirect.action',
            data_get: 'chartValues.json',
            indicator_get: 'indicatorGroups/',
            indicator_getall: 'indicators.json?paging=false&links=false',
            indicatorgroup_get: 'indicatorGroups.json?paging=false&links=false',
            dataelement_get: 'dataElementGroups/',
            dataelement_getall: 'dataElements.json?paging=false&links=false',
            dataelementgroup_get: 'dataElementGroups.json?paging=false&links=false',
            dataset_get: 'dataSets.json?paging=false&links=false',
            organisationunit_getbygroup: 'getOrganisationUnitPathsByGroup.action',
            organisationunit_getbylevel: 'getOrganisationUnitPathsByLevel.action',
            organisationunit_getbyids: 'getOrganisationUnitPaths.action',
            organisationunitgroup_getall: 'organisationUnitGroups.json?paging=false&links=false',
            organisationunitgroupset_get: 'getOrganisationUnitGroupSetsMinified.action',
            organisationunitlevel_getall: 'organisationUnitLevels.json?paging=false&links=false&viewClass=detailed',
            organisationunitchildren_get: 'getOrganisationUnitChildren.action',
            favorite_addorupdate: 'addOrUpdateChart.action',
            favorite_addorupdatesystem: 'addOrUpdateSystemChart.action',
            favorite_updatename: 'updateChartName.action',
            favorite_get: 'charts/',
            favorite_getall: 'getSystemAndCurrentUserCharts.action',
            favorite_delete: 'deleteCharts.action'
        },
        dimension: {
            data: {
                value: 'data',
                rawValue: 'Data', //i18n PT.i18n.data,
                paramName: 'dx',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_ind_de
				}
            },
            category: {
				paramName: 'co',
				rawValue: 'Categories'
			},
            indicator: {
                value: 'indicator',
                rawValue: 'Indicators', //i18n PT.i18n.indicator,
                paramName: 'dx'
            },
            dataElement: {
                value: 'dataelement',
                rawValue: 'Data elements', //i18n PT.i18n.data_element,
                paramName: 'dx'
            },
            dataSet: {
				value: 'dataset',
                rawValue: 'Data sets', //i18n PT.i18n.dataset,
                paramName: 'dx'
			},
            period: {
                value: 'period',
                rawValue: 'Periods', //i18n PT.i18n.period,
                paramName: 'pe',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_period
				}
            },
            organisationUnit: {
                value: 'organisationunit',
                rawValue: 'Organisation units', //i18n PT.i18n.organisation_unit,
                paramName: 'ou',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_orgunit
				}
            },
            organisationUnitGroup: {
				value: 'organisationunitgroup'
			},
			value: {
				value: 'value'
			}
        },
        root: {
			id: 'root'
		}
	};

	conf.period = {
		relativePeriods: {
			LAST_MONTH: 1,
			LAST_3_MONTHS: 3,
			LAST_12_MONTHS: 12,
			LAST_QUARTER: 1,
			LAST_4_QUARTERS: 4,
			LAST_SIX_MONTH: 1,
			LAST_2_SIXMONTHS: 2,
			THIS_YEAR: 1,
			LAST_YEAR: 1,
			LAST_5_YEARS: 5
		},
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
	};

	conf.layout = {
        west_width: 424,
        west_fieldset_width: 416,
        west_width_padding: 4,
        west_fill: 2,
        west_fill_accordion_indicator: 63,
        west_fill_accordion_dataelement: 63,
        west_fill_accordion_dataset: 33,
        west_fill_accordion_period: 256,
        west_fill_accordion_organisationunit: 62,
        west_maxheight_accordion_indicator: 400,
        west_maxheight_accordion_dataelement: 400,
        west_maxheight_accordion_dataset: 400,
        west_maxheight_accordion_period: 513,
        west_maxheight_accordion_organisationunit: 900,
        west_maxheight_accordion_organisationunitgroup: 298,
        west_maxheight_accordion_options: 449,
        east_tbar_height: 31,
        east_gridcolumn_height: 30,
        form_label_width: 55,
        window_favorite_ypos: 100,
        window_confirm_width: 250,
        window_share_width: 500,
        grid_favorite_width: 420,
        treepanel_minheight: 135,
        treepanel_maxheight: 400,
        treepanel_fill_default: 310,
        treepanel_toolbar_menu_width_group: 140,
        treepanel_toolbar_menu_width_level: 120,
        multiselect_minheight: 100,
        multiselect_maxheight: 250,
        multiselect_fill_default: 345,
        multiselect_fill_reportingrates: 315
    };

	conf.pivot = {
		cellPadding: {
			'compact': '3px',
			'normal': '5px',
			'comfortable': '10px'
		},
		fontSize: {
			'small': '10px',
			'normal': '11px',
			'large': '13px'
		}
	};

    conf.util = {
		jsonEncodeString: function(str) {
			return typeof str === 'string' ? str.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'') : str;
		},
		jsonEncodeArray: function(a) {
			for (var i = 0; i < a.length; i++) {
				a[i] = pt.conf.util.jsonEncodeString(a[i]);
			}
			return a;
		}
	};

	return conf;
};

PT.core.getUtils = function(pt) {
	var util = {};

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

    util.url = {
		getParam: function(s) {
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

	util.treepanel = {
		getHeight: function() {
			var h1 = PT.cmp.region.west.getHeight();
			var h2 = PT.cmp.options.panel.getHeight();
			var h = h1 - h2 - PT.conf.layout.treepanel_fill_default;
			var mx = PT.conf.layout.treepanel_maxheight;
			var mn = PT.conf.layout.treepanel_minheight;
			return h > mx ? mx : h < mn ? mn : h;
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
			cmp = cmp || pt.viewport;
			msg = msg || 'Loading..';

			if (pt.viewport.mask) {
				pt.viewport.mask.destroy();
			}
			pt.viewport.mask = new Ext.create('Ext.LoadMask', cmp, {
				id: 'pt-loadmask',
				shadow: false,
				msg: msg,
				style: 'box-shadow:0',
				bodyStyle: 'box-shadow:0'
			});
			pt.viewport.mask.show();
		},
		hideMask: function() {
			if (pt.viewport.mask) {
				pt.viewport.mask.hide();
			}
		}
	};

	util.checkbox = {
		setRelativePeriods: function(rp) {
			if (rp) {
				for (var r in rp) {
					var cmp = pt.util.getCmp('checkbox[paramName="' + r + '"]');
					if (cmp) {
						cmp.setValue(rp[r]);
					}
				}
			}
			else {
				PT.util.checkbox.setAllFalse();
			}
		},
		setAllFalse: function() {
			var a = pt.cmp.dimension.relativePeriod.checkbox;
			for (var i = 0; i < a.length; i++) {
				a[i].setValue(false);
			}
		},
		isAllFalse: function() {
			var a = pt.cmp.dimension.relativePeriod.checkbox;
			for (var i = 0; i < a.length; i++) {
				if (a[i].getValue()) {
					return false;
				}
			}
			return true;
		}
	};

	util.array = {
		sortDimensions: function(dimensions) {

			// Sort object order
			Ext.Array.sort(dimensions, function(a,b) {
				if (a.name < b.name) {
					return -1;
				}
				if (a.name > b.name) {
					return 1;
				}
				return 0;
			});

			// Sort object items order
			for (var i = 0, dim; i < dimensions.length; i++) {
				dim = dimensions[i];

				if (dim.items) {
					dimensions[i].items.sort();
				}
			}

			return dimensions;
		},

		sortObjectsByString: function(array, key) {
			key = key || 'name';
			array.sort( function(a, b) {
				var nameA = a[key].toLowerCase(),
					nameB = b[key].toLowerCase();

				if (nameA < nameB) {
					return -1;
				}
				if (nameA > nameB) {
					return 1;
				}
				return 0;
			});
			return array;
		}
	};

	util.number = {
		getNumberOfDecimals: function(x) {
			var tmp = new String(x);
			return (tmp.indexOf(".") > -1) ? (tmp.length - tmp.indexOf(".") - 1) : 0;
		},

		roundIf: function(x, fix) {
			var dec = pt.util.number.getNumberOfDecimals(x);
			return parseFloat(dec > fix ? x.toFixed(fix) : x);
		},

		pp: function(x) {
			return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");
		}
	};

	util.pivot = {
		getTdHtml: function(options, config) {
			var cls,
				colSpan,
				rowSpan,
				htmlValue,
				cellPadding,
				fontSize;

			if (!(config && Ext.isObject(config))) {
				return '';
			}

			cls = config.cls ? config.cls : '';
			cls += config.hidden ? ' td-hidden' : '';
			cls += config.collapsed ? ' td-collapsed' : '';
			colSpan = config.colSpan ? 'colspan="' + config.colSpan + '"' : '';
			rowSpan = config.rowSpan ? 'rowspan="' + config.rowSpan + '"' : '';
			htmlValue = config.collapsed ? '&nbsp;' : config.htmlValue || config.value || '&nbsp;';
			htmlValue = config.type !== 'dimension' ? pt.util.number.pp(htmlValue) : htmlValue;
			cellPadding = pt.conf.pivot.cellPadding[config.cellPadding] || pt.conf.pivot.cellPadding[options.cellPadding];
			fontSize = pt.conf.pivot.fontSize[config.fontSize] || pt.conf.pivot.fontSize[options.fontSize];

			return '<td class="' + cls + '" ' + colSpan + ' ' + rowSpan + ' style="padding:' + cellPadding + '; font-size:' + fontSize + ';">' + htmlValue + '</td>';
		},

		getTable: function(settings, pt) {
			var options = settings.options,
				getParamStringFromDimensions,
				extendSettings,
				validateResponse,
				extendResponse,
				extendAxis,
				extendRowAxis,
				getTableHtmlArrays,
				initialize,

				dimConf = pt.conf.finals.dimension;

			extendSettings = function(settings) {
				var xSettings = Ext.clone(settings),
					addDimensions,
					addDimensionNames,
					addSortedDimensions,
					addSortedFilterDimensions;

				addDimensions = function() {
					xSettings.dimensions = [].concat(Ext.clone(xSettings.col) || [], Ext.clone(xSettings.row) || []);
				}();

				addDimensionNames = function() {
					var a = [],
						dimensions = Ext.clone(xSettings.dimensions) || [];

					for (var i = 0; i < dimensions.length; i++) {
						a.push(dimensions[i].name);
					}

					xSettings.dimensionNames = a;
				}();

				addSortedDimensions = function() {
					xSettings.sortedDimensions = pt.util.array.sortDimensions(Ext.clone(xSettings.dimensions) || []);
				}();

				addSortedFilterDimensions = function() {
						xSettings.sortedFilterDimensions = pt.util.array.sortDimensions(Ext.clone(xSettings.filter) || []);
				}();

				addNameItemsMap = function() {
					var map = {},
						dimensions = Ext.clone(xSettings.dimensions) || [];

					for (var i = 0, dim; i < dimensions.length; i++) {
						dim = dimensions[i];

						map[dim.name] = dim.items || [];
					}

					xSettings.nameItemsMap = map;
				}();

				return xSettings;
			};

			getSyncronizedXSettings = function(xSettings, response) {
				var getHeaderNames,

					headerNames,
					newSettings;

				getHeaderNames = function() {
					var a = [];

					for (var i = 0; i < response.headers.length; i++) {
						a.push(response.headers[i].name);
					}

					return a;
				};

				removeDimensionFromSettings = function(dimensionName) {
					var getCleanAxis;

					getAxis = function(axis) {
						var axis = Ext.clone(axis),
							dimension;

						for (var i = 0; i < axis.length; i++) {
							if (axis[i].name === dimensionName) {
								dimension = axis[i];
							}
						}

						if (dimension) {
							Ext.Array.remove(axis, dimension);
						}

						return axis;
					};

					if (settings.col) {
						settings.col = getAxis(settings.col);
					}
					if (settings.row) {
						settings.row = getAxis(settings.row);
					}
					if (settings.filter) {
						settings.filter = getAxis(settings.filter);
					}
				};

				headerNames = getHeaderNames();

				// remove co from settings if it does not exist in response
				if (Ext.Array.contains(xSettings.dimensionNames, dimConf.category.paramName) && !(Ext.Array.contains(headerNames, dimConf.category.paramName))) {
					removeDimensionFromSettings(dimConf.category.paramName);

					newSettings = pt.api.Settings(settings);

					if (!newSettings) {
						return;
					}

					return extendSettings(newSettings);
				}
				else {
					return xSettings;
				}
			};

			getParamString = function(xSettings) {
				var sortedDimensions = xSettings.sortedDimensions,
					sortedFilterDimensions = xSettings.sortedFilterDimensions,
					paramString = '?';

				for (var i = 0, sortedDim; i < sortedDimensions.length; i++) {
					sortedDim = sortedDimensions[i];

					paramString += 'dimension=' + sortedDim.name;

					if (sortedDim.name !== pt.conf.finals.dimension.category.paramName) {
						paramString += ':' + sortedDim.items.join(';');
					}

					if (i < (sortedDimensions.length - 1)) {
						paramString += '&';
					}
				}

				if (sortedFilterDimensions) {
					for (var i = 0, sortedFilterDim; i < sortedFilterDimensions.length; i++) {
						sortedFilterDim = sortedFilterDimensions[i];

						paramString += '&filter=' + sortedFilterDim.name + ':' + sortedFilterDim.items.join(';');
					}
				}

				return paramString;
			};

			validateResponse = function(response) {
				if (!(response && Ext.isObject(response))) {
					alert('Data invalid');
					return false;
				}

				if (!(response.headers && Ext.isArray(response.headers) && response.headers.length)) {
					alert('Data invalid');
					return false;
				}

				if (!(Ext.isNumber(response.width) && response.width > 0 &&
					  Ext.isNumber(response.height) && response.height > 0 &&
					  Ext.isArray(response.rows) && response.rows.length > 0)) {
					alert('No values found');
					return false;
				}

				if (response.headers.length !== response.rows[0].length) {
					alert('Data invalid');
					return false;
				}

				return true;
			};

			extendResponse = function(response, xSettings) {
				var headers = response.headers,
					metaData = response.metaData,
					rows = response.rows;

				response.nameHeaderMap = {};
				response.idValueMap = {};

				var extendHeaders = function() {

					// Extend headers: index, items (ordered), size
					for (var i = 0, header, settingsItems, responseItems, orderedResponseItems; i < headers.length; i++) {
						header = headers[i];
						settingsItems = xSettings.nameItemsMap[header.name],
						responseItems = [];
						orderedResponseItems = [];

						// index
						header.index = i;

						if (header.meta) {

							// items
							for (var j = 0; j < rows.length; j++) {
								responseItems.push(rows[j][header.index]);
							}

							responseItems = Ext.Array.unique(responseItems);

							if (settingsItems.length) {
								for (var j = 0, item; j < settingsItems.length; j++) {
									item = settingsItems[j];

									if (header.name === dimConf.period.paramName && pt.conf.period.relativePeriods[item]) {
										orderedResponseItems = responseItems;
										orderedResponseItems.sort();
									}
									else {
										if (Ext.Array.contains(responseItems, item)) {
											orderedResponseItems.push(item);
										}
									}
								}
							}
							else {
								orderedResponseItems = responseItems.sort();
							}

							header.items = orderedResponseItems;

							// size
							header.size = header.items.length;
						}
					}

					// nameHeaderMap (headerName: header)
					for (var i = 0, header; i < headers.length; i++) {
						header = headers[i];

						response.nameHeaderMap[header.name] = header;
					}
				}();

				var createValueIds = function() {
					var valueHeaderIndex = response.nameHeaderMap[pt.conf.finals.dimension.value.value].index,
						dimensionNames = xSettings.dimensionNames,
						idIndexOrder = [];

					// idIndexOrder
					for (var i = 0; i < dimensionNames.length; i++) {
						idIndexOrder.push(response.nameHeaderMap[dimensionNames[i]].index);
					}

					// idValueMap
					for (var i = 0, row, id; i < rows.length; i++) {
						row = rows[i];
						id = '';

						for (var j = 0; j < idIndexOrder.length; j++) {
							id += row[idIndexOrder[j]];
						}

						response.idValueMap[id] = row[valueHeaderIndex];
					}
				}();

				return response;
			};

			extendAxis = function(type, axis, xResponse) {
				if (!axis || (Ext.isArray(axis) && !axis.length)) {
					return;
				}

				var axis = Ext.clone(axis),
					spanType = type === 'col' ? 'colSpan' : 'rowSpan',
					nCols = 1,
					aNumCols = [],
					aAccNumCols = [],
					aSpan = [],
					aGuiItems = [],
					aAllItems = [],
					aColIds = [],
					aAllObjects = [],
					aUniqueIds;

				aUniqueIds = function() {
					var a = [];

					for (var i = 0, dim; i < axis.length; i++) {
						dim = axis[i];

						a.push(xResponse.nameHeaderMap[dim.name].items);
					}

					return a;
				}();
//aUniqueIds	= [ [de1, de2, de3],
//					[p1],
//					[ou1, ou2, ou3, ou4] ]


				for (var i = 0, dim; i < aUniqueIds.length; i++) {
					nNumCols = aUniqueIds[i].length;

					aNumCols.push(nNumCols);
					nCols = nCols * nNumCols;
					aAccNumCols.push(nCols);
				}
	//aNumCols		= [3, 1, 4]
	//nCols			= (12) [3, 3, 12] (3 * 1 * 4)
	//aAccNumCols	= [3, 3, 12]

	//nCols			= 12


				for (var i = 0; i < aUniqueIds.length; i++) {
					if (aNumCols[i] === 1) {
						if (i === 0) {
							aSpan.push(nCols); //if just one and top level, span all
						}
						else {
							aSpan.push(aSpan[0]); //if just one and not top level, span same as top level
						}
					}
					else {
						aSpan.push(nCols / aAccNumCols[i]);
					}
				}
	//aSpan			= [4, 12, 1]


				aGuiItems.push(aUniqueIds[0]);

				if (aUniqueIds.length > 1) {
					for (var i = 1, a, n; i < aUniqueIds.length; i++) {
						a = [];
						n = aNumCols[i] === 1 ? aNumCols[0] : aAccNumCols[i-1];

						for (var j = 0; j < n; j++) {
							a = a.concat(aUniqueIds[i]);
						}

						aGuiItems.push(a);
					}
				}
	//aGuiItems	= [ [d1, d2, d3], (3)
	//				[p1, p2, p3, p4, p5, p1, p2, p3, p4, p5, p1, p2, p3, p4, p5], (15)
	//				[o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2...] (30)
	//		  	  ]


				for (var i = 0, aAllRow, aUniqueRow, span, factor; i < aUniqueIds.length; i++) {
					aAllRow = [];
					aUniqueRow = aUniqueIds[i];
					span = aSpan[i];
					factor = nCols / (span * aUniqueRow.length);

					for (var j = 0; j < factor; j++) {
						for (var k = 0; k < aUniqueRow.length; k++) {
							for (var l = 0; l < span; l++) {
								aAllRow.push(aUniqueRow[k]);
							}
						}
					}

					aAllItems.push(aAllRow);
				}
	//aAllItems	= [ [d1, d1, d1, d1, d1, d1, d1, d1, d1, d1, d2, d2, d2, d2, d2, d2, d2, d2, d2, d2, d3, d3, d3, d3, d3, d3, d3, d3, d3, d3], (30)
	//				[p1, p2, p3, p4, p5, p1, p2, p3, p4, p5, p1, p2, p3, p4, p5, p1, p2, p3, p4, p5, p1, p2, p3, p4, p5, p1, p2, p3, p4, p5], (30)
	//				[o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2, o1, o2] (30)
	//		  	  ]


				for (var i = 0, id; i < nCols; i++) {
					id = '';

					for (var j = 0; j < aAllItems.length; j++) {
						id += aAllItems[j][i];
					}

					aColIds.push(id);
				}
	//aColIds	= [ aaaaaaaaBBBBBBBBccccccc, aaaaaaaaaccccccccccbbbbbbbbbb, ... ]



				// allObjects

				for (var i = 0, allRow; i < aAllItems.length; i++) {
					allRow = [];

					for (var j = 0; j < aAllItems[i].length; j++) {
						allRow.push({
							id: aAllItems[i][j]
						});
					}

					aAllObjects.push(allRow);
				}

				// add span
				for (var i = 0; i < aAllObjects.length; i++) {
					for (var j = 0, obj; j < aAllObjects[i].length; j += aSpan[i]) {
						obj = aAllObjects[i][j];
						obj[spanType] = aSpan[i];
						obj.children = aSpan[i] === 1 ? 0 : aSpan[i];

						if (i === 0) {
							obj.root = true;
						}
					}
				}

				// add parents
				if (aAllObjects.length > 1) {
					for (var i = 1, allRow; i < aAllObjects.length; i++) {
						allRow = aAllObjects[i];

						for (var j = 0, obj, sizeCount = 0, span = aSpan[i - 1], parentObj = aAllObjects[i - 1][0]; j < allRow.length; j++) {
							obj = allRow[j];
							obj.parent = parentObj;
							sizeCount++;

							if (sizeCount === span) {
								parentObj = aAllObjects[i - 1][j + 1];
								sizeCount = 0;
							}
						}
					}
				}

				return {
					type: type,
					items: axis,
					xItems: {
						unique: aUniqueIds,
						gui: aGuiItems,
						all: aAllItems
					},
					objects: {
						all: aAllObjects
					},
					ids: aColIds,
					span: aSpan,
					dims: aUniqueIds.length,
					size: nCols
				};
			};

			getTableHtml = function(xColAxis, xRowAxis, xResponse) {
				var doSubTotals,
					getColAxisHtmlArray,
					getRowHtmlArray,
					rowAxisHtmlArray,
					getColTotalHtmlArray,
					getGrandTotalHtmlArray,
					getTotalHtmlArray,
					getHtml,

					getUniqueFactor = function(xAxis) {
						if (!xAxis) {
							return null;
						}

						var unique = xAxis.xItems.unique;

						if (unique) {
							if (unique.length < 2) {
								return 1;
							}
							else {
								return xAxis.size / unique[0].length;
							}
						}

						return null;
					},

					colUniqueFactor = getUniqueFactor(xColAxis),
					rowUniqueFactor = getUniqueFactor(xRowAxis),

					valueItems = [],
					totalColItems = [],
					htmlArray;

				doSubTotals = function(xAxis) {
					return !!options.showSubTotals && xAxis && xAxis.dims > 1;

					//var multiItemDimension = 0,
						//unique;

					//if (!(options.showSubTotals && xAxis && xAxis.dims > 1)) {
						//return false;
					//}

					//unique = xAxis.xItems.unique;

					//for (var i = 0; i < unique.length; i++) {
						//if (unique[i].length > 1) {
							//multiItemDimension++;
						//}
					//}

					//return (multiItemDimension > 1);
				};

				getColAxisHtmlArray = function() {
					var a = [],
						getEmptyHtmlArray;

					getEmptyHtmlArray = function() {
						return (xColAxis && xRowAxis) ?
							pt.util.pivot.getTdHtml(options, {cls: 'pivot-dim-empty', colSpan: xRowAxis.dims, rowSpan: xColAxis.dims}) : '';
					};

					if (!(xColAxis && Ext.isObject(xColAxis))) {
						return a;
					}

					for (var i = 0, dimItems, colSpan, dimHtml; i < xColAxis.dims; i++) {
						dimItems = xColAxis.xItems.gui[i];
						colSpan = xColAxis.span[i];
						dimHtml = [];

						if (i === 0) {
							dimHtml.push(getEmptyHtmlArray());
						}

						for (var j = 0, id; j < dimItems.length; j++) {
							id = dimItems[j];
							dimHtml.push(pt.util.pivot.getTdHtml(options, {
								type: 'dimension',
								cls: 'pivot-dim',
								colSpan: colSpan,
								htmlValue: xResponse.metaData[id]
							}));

							if (doSubTotals(xColAxis) && i === 0) {
								dimHtml.push(pt.util.pivot.getTdHtml(options, {
									type: 'dimensionSubtotal',
									cls: 'pivot-dim-subtotal',
									rowSpan: xColAxis.dims
								}));
							}

							if (i === 0 && j === (dimItems.length - 1)) {
								dimHtml.push(pt.util.pivot.getTdHtml(options, {
									type: 'dimensionTotal',
									cls: 'pivot-dim-total',
									rowSpan: xColAxis.dims,
									htmlValue: 'Total'
								}));
							}
						}

						a.push(dimHtml);
					}

					return a;
				};

				getRowHtmlArray = function() {
					var a = [],
						axisObjects = [],
						valueObjects = [],
						totalValueObjects = [],
						mergedObjects = [],
						valueItemsCopy,
						colSize = xColAxis ? xColAxis.size : 1,
						rowSize = xRowAxis ? xRowAxis.size : 1,
						recursiveReduce;

					recursiveReduce = function(obj) {
						if (!obj.children) {
							obj.collapsed = true;

							if (obj.parent) {
								obj.parent.children--;
								obj.parent.rowSpan = obj.parent.parent ? obj.parent.rowSpan-- : obj.parent.rowSpan;
							}
						}

						if (obj.parent) {
							recursiveReduce(obj.parent);
						}
					};

					// Populate dim objects
					if (xRowAxis) {
						for (var i = 0, row; i < xRowAxis.objects.all[0].length; i++) {
							row = [];

							for (var j = 0, obj, newObj; j < xRowAxis.objects.all.length; j++) {
								obj = xRowAxis.objects.all[j][i];
								obj.type = 'dimension';
								obj.cls = 'pivot-dim td-nobreak';
								obj.noBreak = true;
								obj.hidden = !(obj.rowSpan || obj.colSpan);
								obj.htmlValue = xResponse.metaData[obj.id];

								row.push(obj);
							}

							axisObjects.push(row);
						}
					}

					// Value objects
					for (var i = 0, valueItemsRow, valueObjectsRow, map = Ext.clone(xResponse.idValueMap); i < rowSize; i++) {
						valueItemsRow = [];
						valueObjectsRow = [];

						for (var j = 0, id, value, htmlValue, empty; j < colSize; j++) {
							id = (xColAxis ? xColAxis.ids[j] : '') + (xRowAxis ? xRowAxis.ids[i] : '');
							empty = false;

							if (map[id]) {
								value = parseFloat(map[id]);
								htmlValue = pt.util.number.roundIf(map[id], 1).toString();
							}
							else {
								value = 0;
								htmlValue = '&nbsp;';
								empty = true;
							}

							valueItemsRow.push(value);
							valueObjectsRow.push({
								type: 'value',
								cls: 'pivot-value',
								value: value,
								htmlValue: htmlValue,
								empty: empty
							});
						}

						valueItems.push(valueItemsRow);
						valueObjects.push(valueObjectsRow);
					}

					// Value total objects
					if (xColAxis) {
						for (var i = 0, empty, total; i < valueObjects.length; i++) {
							empty = [];
							total = 0;

							for (j = 0, obj; j < valueObjects[i].length; j++) {
								obj = valueObjects[i][j];

								empty.push(obj.empty);
								total += obj.value;
							}

							totalValueObjects.push({
								type: 'valueTotal',
								cls: 'pivot-value-total',
								value: total,
								htmlValue: Ext.Array.contains(empty, false) ? pt.util.number.roundIf(total, 1).toString() : '&nbsp;'
							});
						}
					}

					// Hide empty rows/totals
					if (xColAxis && xRowAxis) {
						if (options.hideEmptyRows) {
							for (var i = 0, valueRow, empty, parent; i < valueObjects.length; i++) {
								valueRow = valueObjects[i];
								empty = [];

								for (var j = 0; j < valueRow.length; j++) {
									empty.push(!!valueRow[j].empty);
								}

								if (!Ext.Array.contains(empty, false) && xRowAxis) {

									// Hide values
									for (var j = 0; j < valueRow.length; j++) {
										valueRow[j].collapsed = true;
									}

									// Hide total
									totalValueObjects[i].collapsed = true;

									// Hide/reduce parent dim span
									parent = axisObjects[i][xRowAxis.dims-1];
									recursiveReduce(parent);
								}
							}
						}
					}

					if (doSubTotals(xColAxis)) {
						var tmpValueObjects = [];

						for (var i = 0, row, rowSubTotal, colCount; i < valueObjects.length; i++) {
							row = [];
							rowSubTotal = 0;
							colCount = 0;

							for (var j = 0, item, collapsed = [], empty = []; j < valueObjects[i].length; j++) {
								item = valueObjects[i][j];
								rowSubTotal += item.value;
								empty.push(!!item.empty);
								collapsed.push(!!item.collapsed);
								colCount++;

								row.push(item);

								if (colCount === colUniqueFactor) {
									rowSubTotal = pt.util.number.roundIf(rowSubTotal, 1);

									row.push({
										type: 'valueSubtotal',
										cls: 'pivot-value-subtotal',
										value: rowSubTotal,
										htmlValue: Ext.Array.contains(empty, false) ? rowSubTotal.toString() : '&nbsp',
										collapsed: !Ext.Array.contains(collapsed, false)
									});
									colCount = 0;
									rowSubTotal = 0;
									empty = [];
									collapsed = [];
								}
							}

							tmpValueObjects.push(row);
						}

						valueObjects = tmpValueObjects;
					}

					if (doSubTotals(xRowAxis)) {
						var tmpAxisObjects = [],
							tmpValueObjects = [],
							tmpTotalValueObjects = [];

						getAxisSubTotalRow = function(collapsed) {
							var row = [];

							for (var i = 0, obj; i < xRowAxis.dims; i++) {
								obj = {};
								obj.type = 'dimensionSubtotal';
								obj.cls = 'pivot-dim-subtotal';
								obj.collapsed = !Ext.Array.contains(collapsed, false);

								if (i === 0) {
									obj.htmlValue = '&nbsp;'; //i18n
									obj.colSpan = xRowAxis.dims;
								}
								else {
									obj.hidden = true;
								}

								row.push(obj);
							}

							return row;
						};

						// Row axis objects
						for (var i = 0, row, collapsed = [], count = 0; i < axisObjects.length; i++) {
							tmpAxisObjects.push(axisObjects[i]);
							collapsed.push(!!axisObjects[i][0].collapsed);
							count++;

							if (count === xRowAxis.span[0]) {
								tmpAxisObjects.push(getAxisSubTotalRow(collapsed));

								collapsed = [];
								count = 0;
							}
						}

						// Create tmp value object arrays
						for (var i = 0; i < tmpAxisObjects.length; i++) {
							tmpValueObjects.push([]);
						}

						// Populate tmp value object arrays
						for (var i = 0; i < valueObjects[0].length; i++) {
							for (var j = 0, rowCount = 0, tmpCount = 0, subTotal = 0, collapsed, item; j < valueObjects.length; j++) {
								item = valueObjects[j][i];
								tmpValueObjects[tmpCount++].push(item);
								subTotal += item.value;
								rowCount++;

								if (rowCount === 1) {
									collapsed = !!tmpAxisObjects[j][0].collapsed;
								}

								if (rowCount === rowUniqueFactor) {
									tmpValueObjects[tmpCount++].push({
										type: item.cls === 'pivot-value-subtotal' ? 'valueSubtotal' : 'valueSubtotalTotal',
										value: subTotal,
										htmlValue: pt.util.number.roundIf(subTotal, 1).toString(),
										collapsed: collapsed,
										cls: item.cls === 'pivot-value-subtotal' ? 'pivot-value-subtotal-total' : 'pivot-value-subtotal'
									});
									rowCount = 0;
									subTotal = 0;
								}
							}
						}

						// Total value objects
						for (var i = 0, obj, collapsed = [], subTotal = 0, count = 0; i < totalValueObjects.length; i++) {
							obj = totalValueObjects[i];
							tmpTotalValueObjects.push(obj);

							collapsed.push(!!obj.collapsed);
							subTotal += obj.value;
							count++;

							if (count === xRowAxis.span[0]) {
								tmpTotalValueObjects.push({
									type: 'valueTotalSubgrandtotal',
									cls: 'pivot-value-total-subgrandtotal',
									value: subTotal,
									htmlValue: pt.util.number.roundIf(subTotal, 1).toString(),
									collapsed: !Ext.Array.contains(collapsed, false)
								});

								collapsed = [];
								subTotal = 0;
								count = 0;
							}
						}

						axisObjects = tmpAxisObjects;
						valueObjects = tmpValueObjects;
						totalValueObjects = tmpTotalValueObjects;
					}

					// Merge dim, value, total
					for (var i = 0, row; i < valueObjects.length; i++) {
						row = [];

						if (xRowAxis) {
							row = row.concat(axisObjects[i]);
						}

						row = row.concat(valueObjects[i]);

						if (xColAxis) {
							row = row.concat(totalValueObjects[i]);
						}

						mergedObjects.push(row);
					}

					// Create html items
					for (var i = 0, row; i < mergedObjects.length; i++) {
						row = [];

						for (var j = 0; j < mergedObjects[i].length; j++) {
							row.push(pt.util.pivot.getTdHtml(options, mergedObjects[i][j]));
						}

						a.push(row);
					}

					return a;
				};

				getColTotalHtmlArray = function() {
					var a = [];

					if (xRowAxis) {

						// Total col items
						for (var i = 0, colSum; i < valueItems[0].length; i++) {
							colSum = 0;

							for (var j = 0; j < valueItems.length; j++) {
								colSum += valueItems[j][i];
							}

							totalColItems.push({
								type: 'valueTotal',
								value: colSum,
								htmlValue: pt.util.number.roundIf(colSum, 1).toString(),
								cls: 'pivot-value-total'
							});
						}

						if (xColAxis && doSubTotals(xColAxis)) {
							var tmp = [];

							for (var i = 0, item, subTotal = 0, colCount = 0; i < totalColItems.length; i++) {
								item = totalColItems[i];
								tmp.push(item);
								subTotal += item.value;
								colCount++;

								if (colCount === colUniqueFactor) {
									tmp.push({
										type: 'valueTotalSubgrandtotal',
										value: subTotal,
										htmlValue: pt.util.number.roundIf(subTotal, 1).toString(),
										cls: 'pivot-value-total-subgrandtotal'
									});

									subTotal = 0;
									colCount = 0;
								}
							}

							totalColItems = tmp;
						}

						// Total col html items
						for (var i = 0, item; i < totalColItems.length; i++) {
							item = totalColItems[i];
							item.htmlValue = pt.util.number.roundIf(item.htmlValue, 1).toString();

							a.push(pt.util.pivot.getTdHtml(options, {
								cls: item.cls,
								htmlValue: item.htmlValue
							}));
						}
					}

					return a;
				};

				getGrandTotalHtmlArray = function() {
					var grandTotalSum,
						values = [],
						a = [];

					for (var i = 0; i < totalColItems.length; i++) {
						values.push(totalColItems[i].value);
					}

					if (xColAxis && xRowAxis) {
						grandTotalSum = Ext.Array.sum(values);

						a.push(pt.util.pivot.getTdHtml(options, {
							cls: 'pivot-value-grandtotal',
							htmlValue: pt.util.number.roundIf(grandTotalSum, 1).toString()
						}));
					}

					return a;
				};

				getTotalHtmlArray = function() {
					var dimTotalArray,
						colTotal = getColTotalHtmlArray(),
						grandTotal = getGrandTotalHtmlArray(),
						row,
						a = [];

					if (xRowAxis) {
						dimTotalArray = [pt.util.pivot.getTdHtml(options, {
							cls: 'pivot-dim-total',
							colSpan: xRowAxis.dims,
							htmlValue: 'Total'
						})];
					}

					row = [].concat(dimTotalArray || [], Ext.clone(colTotal) || [], Ext.clone(grandTotal) || []);

					a.push(row);

					return a;
				};

				getHtml = function() {
					var s = '<table id="' + pt.el + '" class="pivot">';

					for (var i = 0; i < htmlArray.length; i++) {
						s += '<tr>' + htmlArray[i].join('') + '</tr>';
					}

					return s += '</table>';
				};

				htmlArray = [].concat(getColAxisHtmlArray(), getRowHtmlArray(), getTotalHtmlArray());
				htmlArray = Ext.Array.clean(htmlArray);

				return getHtml(htmlArray);
			};

			initialize = function() {
				var url,
					xSettings,
					xResponse,
					xColAxis,
					xRowAxis;

				xSettings = extendSettings(settings);

				pt.paramString = getParamString(xSettings);
				url = pt.init.contextPath + '/api/analytics.jsonp' + pt.paramString;

				if (url.length > 2000) {
					var percent = ((url.length - 2000) / url.length) * 100;

					alert('Too many parameters selected. Please reduce the number of parameters by minimum ' + percent.toFixed(0) + '%');
					return;
				}

				pt.util.mask.showMask(pt.viewport);

				Ext.data.JsonP.request({
					method: 'GET',
					url: url,
					callbackName: 'analytics',
					timeout: 60000,
					headers: {
						'Content-Type': 'application/json',
						'Accept': 'application/json'
					},
					disableCaching: false,
					failure: function() {
						pt.util.mask.hideMask();
						alert('Data request failed');
					},
					success: function(response) {
						var html;

						if (!validateResponse(response)) {
							pt.util.mask.hideMask();
							console.log(response);
							return;
						}

						xSettings = getSyncronizedXSettings(xSettings, response);

						if (!xSettings) {
							pt.util.mask.hideMask();
							return;
						}

						xResponse = extendResponse(response, xSettings);

						xColAxis = extendAxis('col', xSettings.col, xResponse);
						xRowAxis = extendAxis('row', xSettings.row, xResponse);

						html = getTableHtml(xColAxis, xRowAxis, xResponse);

						pt.container.removeAll(true);
						pt.container.update(html);

						// After table success
						pt.util.mask.hideMask();

						if (pt.viewport.downloadButton) {
							pt.viewport.downloadButton.enable();
						}
					}
				});

			}();
		}
	};

	return util;
};

PT.core.getAPI = function(pt) {
	var api = {};

	api.Settings = function(config) {
		var col,
			row,
			filter,

			removeEmptyDimensions,
			getValidatedAxis,
			validateSettings,

			defaultOptions = {
				showSubTotals: true,
				cellPadding: 'normal',
				fontSize: 'normal'
			};

		removeEmptyDimensions = function(axis) {
			if (!axis) {
				return;
			}

			for (var i = 0, dimension, remove; i < axis.length; i++) {
				remove = false;
				dimension = axis[i];

				if (dimension.name !== pt.conf.finals.dimension.category.paramName) {
					if (!(Ext.isArray(dimension.items) && dimension.items.length)) {
						remove = true;
					}
					else {
						for (var j = 0; j < dimension.items.length; j++) {
							if (!Ext.isString(dimension.items[j])) {
								remove = true;
							}
						}
					}
				}

				if (remove) {
					axis = Ext.Array.erase(axis, i, 1);
					i = i - 1;
				}
			}

			return axis;
		};

		getValidatedAxis = function(axis) {
			if (!(axis && Ext.isArray(axis) && axis.length)) {
				return;
			}

			for (var i = 0, dimension; i < axis.length; i++) {
				dimension = axis[i];

				if (!(Ext.isObject(dimension) && Ext.isString(dimension.name))) {
					return;
				}
			}

			axis = removeEmptyDimensions(axis);

			return axis.length ? axis : null;
		};

		getValidatedOptions = function(options) {
			if (!(options && Ext.isObject(options))) {
				return defaultOptions;
			}

			options.showSubTotals = Ext.isDefined(options.showSubTotals) ? options.showSubTotals : defaultOptions.showSubTotals;
			options.cellPadding = options.cellPadding || defaultOptions.cellPadding;
			options.fontSize = options.fontSize || defaultOptions.fontSize;

			return options;
		};

		validateSettings = function() {
			var a = [].concat(Ext.clone(col), Ext.clone(row), Ext.clone(filter)),
				names = [],
				dimConf = pt.conf.finals.dimension;

			if (!(col || row)) {
				alert('At least one dimension must be specified as row or column'); //i18n
				return;
			}

			// Selected dimensions
			for (var i = 0; i < a.length; i++) {
				if (a[i]) {
					names.push(a[i].name);
				}
			}

			//if (!Ext.Array.contains(names, dimConf.data.paramName)) {
				//alert('At least one indicator, data element or dataset must be specified as column, row or filter');
				//return;
			//}

			if (!Ext.Array.contains(names, dimConf.period.paramName)) {
				alert('At least one period must be specified as column, row or filter');
				return;
			}

			return true;
		};

		return function() {
			var obj = {};

			if (!(config && Ext.isObject(config))) {
				alert('Settings config is not an object'); //i18n
				return;
			}

			col = getValidatedAxis(config.col);
			row = getValidatedAxis(config.row);
			filter = getValidatedAxis(config.filter);

			if (!validateSettings()) {
				return;
			}

			if (col) {
				obj.col = col;
			}
			if (row) {
				obj.row = row;
			}
			if (filter) {
				obj.filter = filter;
			}

			obj.options = getValidatedOptions(config.options);

			return obj;
		}();
	};

	return api;
};

PT.core.getInstance = function(config) {
	var pt = {};

	pt.baseUrl = config && config.baseUrl ? config.baseUrl : '../../';
	pt.el = config && config.el ? config.el : 'app';

	pt.conf = PT.core.getConfigs();
	pt.util = PT.core.getUtils(pt);
	pt.api = PT.core.getAPI(pt);

	return pt;
};

});
