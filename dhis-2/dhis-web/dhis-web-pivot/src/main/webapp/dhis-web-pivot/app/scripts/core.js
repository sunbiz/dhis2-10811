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
                rawvalue: 'Data', //i18n PT.i18n.data,
                paramname: 'dx',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_ind_de
				}
            },
            category: {
				paramname: 'coc'
			},
            indicator: {
                value: 'indicator',
                rawvalue: 'Indicator', //i18n PT.i18n.indicator,
                paramname: 'dx'
            },
            dataelement: {
                value: 'dataelement',
                rawvalue: 'Data element', //i18n PT.i18n.data_element,
                paramname: 'dx'
            },
            dataset: {
				value: 'dataset',
                rawvalue: 'Data set', //i18n PT.i18n.dataset,
                paramname: 'dx'
			},
            period: {
                value: 'period',
                rawvalue: 'Period', //i18n PT.i18n.period,
                paramname: 'pe',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_period
				}
            },
            organisationunit: {
                value: 'organisationunit',
                rawvalue: 'Organisation unit', //i18n PT.i18n.organisation_unit,
                paramname: 'ou',
                warning: {
					filter: '...'//PT.i18n.wm_multiple_filter_orgunit
				}
            },
            organisationunitgroup: {
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
        west_width_padding: 6,
        west_fill: 6,
        west_fill_accordion_indicator: 65,
        west_fill_accordion_dataelement: 65,
        west_fill_accordion_dataset: 35,
        west_fill_accordion_period: 257,
        west_fill_accordion_organisationunit: 96,
        west_maxheight_accordion_indicator: 620,
        west_maxheight_accordion_dataelement: 620,
        west_maxheight_accordion_dataset: 620,
        west_maxheight_accordion_period: 820,
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
		getTable: function(settings, pt, container) {
			var getParamStringFromDimensions,
				extendSettings,
				validateResponse,
				extendResponse,
				extendAxis,
				extendRowAxis,
				getTableHtmlArrays,
				getTablePanel,
				initialize;

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

				// remove coc from settings if it does not exist in response
				if (Ext.Array.contains(xSettings.dimensionNames, 'coc') && !(Ext.Array.contains(headerNames, 'coc'))) {
					removeDimensionFromSettings('coc');

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

					if (sortedDim.name !== pt.conf.finals.dimension.category.paramname) {
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

									if (header.name === 'pe' && pt.conf.period.relativePeriods[item]) {
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

			extendAxis = function(axis, xResponse) {
				if (!axis || (Ext.isArray(axis) && !axis.length)) {
					return;
				}

				var axis = Ext.clone(axis),
					nCols = 1,
					aNumCols = [],
					aAccNumCols = [],
					aSpan = [],
					aGuiItems = [],
					aAllItems = [],
					aColIds = [],
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


				return {
					items: axis,
					xItems: {
						unique: aUniqueIds,
						gui: aGuiItems,
						all: aAllItems
					},
					ids: aColIds,
					span: aSpan,
					dims: aUniqueIds.length,
					size: nCols
				};
			};

			extendRowAxis = function(rowAxis, xResponse) {
				if (!rowAxis || (Ext.isArray(rowAxis) && !rowAxis.length)) {
					return;
				}

				var xRowAxis = extendAxis(rowAxis, xResponse),
					all = xRowAxis.xItems.all,
					allObjects = [];

				for (var i = 0, allRow; i < all.length; i++) {
					allRow = [];

					for (var j = 0; j < all[i].length; j++) {
						allRow.push({
							id: all[i][j]
						});
					}

					allObjects.push(allRow);
				}

				for (var i = 0; i < allObjects.length; i++) {
					for (var j = 0, object; j < allObjects[i].length; j += xRowAxis.span[i]) {
						object = allObjects[i][j];
						object.rowSpan = xRowAxis.span[i];
					}
				}

				xRowAxis.xItems.allObjects = allObjects;

				return xRowAxis;
			};

			getTableHtml = function(xColAxis, xRowAxis, xResponse) {
				var getEmptyHtmlArray,
					getColAxisHtmlArray,
					getRowAxisHtmlArray,
					getValueHtmlArray,
					getRowTotalHtmlArray,
					getColTotalHtmlArray,
					getGrandTotalHtmlArray,
					getRowHtmlArray,
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
					var multiItemDimension = 0,
						unique;

					if (!(true && xAxis && xAxis.dims > 1)) {
						return false;
					}

					unique = xAxis.xItems.unique;

					for (var i = 0; i < unique.length; i++) {
						if (unique[i].length > 1) {
							multiItemDimension++;
						}
					}

					return (multiItemDimension > 1);
				};

				getEmptyHtmlArray = function() {
					return (xColAxis && xRowAxis) ?
						'<td class="pivot-dim-empty " colspan="' + xRowAxis.dims + '" rowspan="' + xColAxis.dims + '">&nbsp;</td>' : '';
				};

				getColAxisHtmlArray = function() {
					var a = [];

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
							dimHtml.push('<td class="pivot-dim" colspan="' + colSpan + '">' + xResponse.metaData[id] + '</td>');

							//todo subtotal
							if (doSubTotals(xColAxis) && i === 0) {
								dimHtml.push('<td class="pivot-dim-subtotal" rowspan="' + xColAxis.dims + '"></td>');
							}

							if (i === 0 && j === (dimItems.length - 1)) {
								dimHtml.push('<td class="pivot-dim-total" rowspan="' + xColAxis.dims + '">Total</td>');
							}
						}

						a.push(dimHtml);
					}

					return a;
				};


				getRowAxisHtmlArray = function() {
					var a = [],
						size,
						allObjects,
						uniqueSize,
						count = 0;

					if (!(xRowAxis && Ext.isObject(xRowAxis))) {
						return a;
					}

					size = xRowAxis.size;
					allObjects = xRowAxis.xItems.allObjects;
					uniqueSize = xRowAxis.xItems.unique[xRowAxis.xItems.unique.length - 1].length;

					// Dim html items
					for (var i = 0, row, rowCount = 0; i < size; i++) {
						row = [];
						rowCount++;

						for (var j = 0, object; j < xRowAxis.dims; j++) {
							object = allObjects[j][i];

							if (object.rowSpan) {
								row.push('<td class="pivot-dim nobreak" rowspan="' + object.rowSpan + '">' + xResponse.metaData[object.id] + '</td>');
							}
						}

						a.push(row);

						//todo subtotal
						if (doSubTotals(xRowAxis) && rowCount === rowUniqueFactor) {
							row = [];
							row.push('<td class="pivot-dim-subtotal" colspan="' + xRowAxis.dims + '"></td>');
							a.push(row);
							rowCount = 0;
						}
					}

					return a;
				};

				getValueHtmlArray = function() {
					var a = [],
						htmlValueItems = [],
						htmlValueColItems = [],
						colSize = xColAxis ? xColAxis.size : 1,
						rowSize = xRowAxis ? xRowAxis.size : 1,
						rowRootSize = xRowAxis ? xRowAxis.xItems.unique[0].length : null,
						hasSubtotals,
						subtotal,
						td;

					// Value / htmlvalue items
					for (var i = 0, valueItemRow, htmlValueItemRow; i < rowSize; i++) {
						valueItemRow = [];
						htmlValueItemRow = [];

						for (var j = 0, id, value; j < colSize; j++) {
							id = (xColAxis ? xColAxis.ids[j] : '') + (xRowAxis ? xRowAxis.ids[i] : '');

							value = xResponse.idValueMap[id] ? parseFloat(xResponse.idValueMap[id]) : 0; //todo
							htmlValue = xResponse.idValueMap[id] ? parseFloat(xResponse.idValueMap[id]) : '-'; //todo

							valueItemRow.push(value);
							htmlValueItemRow.push({value: value, htmlValue: htmlValue, cls: 'pivot-value'});
						}

						valueItems.push(valueItemRow);
						htmlValueItems.push(htmlValueItemRow);
					}

					if (doSubTotals(xColAxis)) {
						var tmp = [];

						for (var i = 0, row, rowSubTotal, colCount; i < htmlValueItems.length; i++) {
							row = [];
							rowSubTotal = 0;
							colCount = 0;

							for (var j = 0, item; j < htmlValueItems[i].length; j++) {
								item = htmlValueItems[i][j];
								rowSubTotal += item.value;
								colCount++;

								row.push(item);

								if (colCount === colUniqueFactor) {
									row.push({value: rowSubTotal, htmlValue: rowSubTotal, cls: 'pivot-value-subtotal'});
									colCount = 0;
									rowSubTotal = 0;
								}
							}

							tmp.push(row);
						}

						htmlValueItems = tmp;
					}

					if (doSubTotals(xRowAxis)) {
						var tmp = [],
							subTotals = [],
							count;

						// Create sub total arrays
						for (var i = 0; i < rowRootSize; i++) {
							subTotals.push([]);
						}

						// Populate sub total arrays
						for (var i = 0, subTotal, subTotalsIndex; i < htmlValueItems[0].length; i++) {
							subTotal = 0;
							subTotalsIndex = 0;

							for (var j = 0, rowCount = 0, item; j < xRowAxis.size; j++) {
								item = htmlValueItems[j][i];
								subTotal += item.value;
								rowCount++;

								if (rowCount === rowUniqueFactor) {
									var cls = xColAxis && doSubTotals(xColAxis) && (item.cls === 'pivot-value-subtotal') ? 'pivot-value-subtotal-total' : 'pivot-value-subtotal';
									subTotals[subTotalsIndex].push({value: subTotal, htmlValue: subTotal, cls: cls});
									rowCount = 0;
									subTotal = 0;
									subTotalsIndex++;
								}
							}
						}

						// Add sub total arrays to htmlValueItems
						for (var i = 0, count = 0; i < htmlValueItems.length; i++) {
							tmp.push(htmlValueItems[i]);
							count++;

							if (count === rowUniqueFactor) {
								count = 0;

								var sta = subTotals.shift();

								tmp.push(sta);
							}
						}

						htmlValueItems = tmp;
					}

					// Value html items
					for (var i = 0, row; i < htmlValueItems.length; i++) {
						row = [];

						for (var j = 0, item, cls; j < htmlValueItems[i].length; j++) {
							item = htmlValueItems[i][j];

							//if (Ext.isNumber(value)) {
								//cls = value < 5000 ? 'bad' : (value < 20000 ? 'medium' : 'good'); //basic legendset
							//}

							row.push('<td class="' + item.cls + '">' + pt.util.number.pp(item.htmlValue) + '</td>');
						}

						a.push(row);
					}

					return a;
				};

				getRowTotalHtmlArray = function() {
					var totalRowItems = [],
						vItems = Ext.clone(valueItems),
						a = [];

					if (xColAxis) {

						// Total row items
						for (var i = 0, rowSum; i < vItems.length; i++) {
							rowSum = Ext.Array.sum(vItems[i]);
							totalRowItems.push({value: rowSum, htmlValue: rowSum, cls: 'pivot-value-total'});
						}

						if (xRowAxis && doSubTotals(xRowAxis)) {
							var tmp = [];

							for (var i = 0, rowCount = 0, subTotal = 0; i < totalRowItems.length; i++) {
								tmp.push(totalRowItems[i]);
								rowCount++;
								subTotal += totalRowItems[i].value;

								if (rowCount === rowUniqueFactor) {
									tmp.push({value: subTotal, htmlValue: subTotal, cls: 'pivot-value-total-subgrandtotal'});
									rowCount = 0;
									subTotal = 0;
								}
							}

							totalRowItems = tmp;
						}

						// Total row html items
						for (var i = 0, item; i < totalRowItems.length; i++) {
							item = totalRowItems[i];
							item.htmlValue = pt.util.number.roundIf(item.htmlValue, 1);

							a.push(['<td class="' + item.cls + '">' + pt.util.number.pp(item.htmlValue) + '</td>']);
						}
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

							totalColItems.push({value: colSum, htmlValue: colSum, cls: 'pivot-value-total'});
						}

						if (xColAxis && doSubTotals(xColAxis)) {
							var tmp = [];

							for (var i = 0, item, subTotal = 0, colCount = 0; i < totalColItems.length; i++) {
								item = totalColItems[i];
								tmp.push(item);
								subTotal += item.value;
								colCount++;

								if (colCount === colUniqueFactor) {
									tmp.push({value: subTotal, htmlValue: subTotal, cls: 'pivot-value-total-subgrandtotal'});
									subTotal = 0;
									colCount = 0;
								}
							}

							totalColItems = tmp;
						}

						// Total col html items
						for (var i = 0, item; i < totalColItems.length; i++) {
							item = totalColItems[i];
							item.htmlValue = pt.util.number.roundIf(item.htmlValue, 1);

							a.push('<td class="' + item.cls + '">' + pt.util.number.pp(item.htmlValue) + '</td>');
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
						grandTotalSum = Ext.Array.sum(values) || 0;
						grandTotalSum = pt.util.number.roundIf(grandTotalSum, 1);

						a.push('<td class="pivot-value-grandtotal">' + pt.util.number.pp(grandTotalSum) + '</td>');
					}

					return a;
				};


				getRowHtmlArray = function() {
					var axis = getRowAxisHtmlArray(),
						values = getValueHtmlArray(),
						total = getRowTotalHtmlArray(),
						a = [];

					for (var i = 0, row; i < values.length; i++) {
						row = [].concat(Ext.clone(axis[i] || []), Ext.clone(values[i] || []), Ext.clone(total[i] || []));

						a.push(row);
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
						dimTotalArray = ['<td class="pivot-dim-total" colspan="' + xRowAxis.dims + '">Total</td>'];
					}

					row = [].concat(dimTotalArray || [], Ext.clone(colTotal) || [], Ext.clone(grandTotal) || []);

					a.push(row);

					return a;
				};

				getHtml = function() {
					var s = '<table id="pivottable" class="pivot">';

					for (var i = 0; i < htmlArray.length; i++) {
						s += '<tr>' + htmlArray[i].join('') + '</tr>';
					}

					return s += '</table>';
				};

				htmlArray = [].concat(getColAxisHtmlArray(), getRowHtmlArray(), getTotalHtmlArray());
				htmlArray = Ext.Array.clean(htmlArray);

				return getHtml(htmlArray);
			};

			getTablePanel = function(html) {
				return Ext.create('Ext.panel.Panel', {
					bodyStyle: 'border:0 none',
					autoScroll: true,
					html: html
				});
			};

			initialize = function() {
				var xSettings,
					xResponse,
					xColAxis,
					xRowAxis;

				pt.util.mask.showMask(container);


				xSettings = extendSettings(settings);

				Ext.data.JsonP.request({
					method: 'GET',
					url: pt.init.contextPath + '/api/analytics.jsonp' + getParamString(xSettings),
					callbackName: 'analytics',
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
						var html,
							el;

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

						xColAxis = extendAxis(xSettings.col, xResponse);
						xRowAxis = extendRowAxis(xSettings.row, xResponse);

						html = getTableHtml(xColAxis, xRowAxis, xResponse);

						if (!pt.el && container) {
							el = Ext.get('pivottable');

							if (el) {
								el.destroy();
							}

							container.update(html);
						}

						pt.util.mask.hideMask();
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
			validateSettings;

		removeEmptyDimensions = function(axis) {
			if (!axis) {
				return;
			}

			for (var i = 0, dimension, remove; i < axis.length; i++) {
				remove = false;
				dimension = axis[i];

				if (dimension.name !== 'coc') {
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

		validateSettings = function() {
			var a = [].concat(Ext.clone(col), Ext.clone(row), Ext.clone(filter)),
				names = [];

			if (!(col || row)) {
				alert('No column or row dimensions selected'); //i18n
				return;
			}

			for (var i = 0; i < a.length; i++) {
				if (a[i]) {
					names.push(a[i].name);
				}
			}

			if (!Ext.Array.contains(names, 'dx')) {
				alert('At least one indicator, data element or dataset must be specified as column, row or filter');
				return;
			}

			if (!Ext.Array.contains(names, 'pe')) {
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

			return obj;
		}();
	};

	return api;
};

PT.core.getInstance = function(config) {
	var pt = {};

	pt.baseUrl = config && config.baseUrl ? config.baseUrl : '../../';
	pt.el = config && config.el ? config.el : null;

	pt.conf = PT.core.getConfigs();
	pt.util = PT.core.getUtils(pt);
	pt.api = PT.core.getAPI(pt);

	return pt;
};

});
