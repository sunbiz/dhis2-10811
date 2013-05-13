DV.core = {
	instances: []
};

Ext.onReady( function() {

DV.core.getConfig = function() {
	var conf = {};

    conf.finals = {
        ajax: {
            path_visualizer: '../',
            path_api: '../../api/',
            path_commons: '../../dhis-web-commons-ajax-json/',
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
                name: DV.i18n.data,
                dimensionName: 'dx',
                objectName: 'dx'
            },
            indicator: {
                value: 'indicator',
                name: DV.i18n.indicator,
                dimensionName: 'dx',
                objectName: 'in'
            },
            dataElement: {
                value: 'dataelement',
                name: DV.i18n.data_element,
                dimensionName: 'dx',
                objectName: 'de'
            },
            operand: {
				value: 'operand',
				name: 'Operand',
				dimensionName: 'dx',
				objectName: 'dc'
			},
            dataSet: {
				value: 'dataset',
                name: DV.i18n.dataset,
                dimensionName: 'dx',
                objectName: 'ds'
			},
            category: {
				name: DV.i18n.categories,
				dimensionName: 'co',
                objectName: 'co',
			},
            period: {
                value: 'period',
                name: DV.i18n.period,
                dimensionName: 'pe',
                objectName: 'pe',
            },
            fixedPeriod: {
				value: 'periods'
			},
			relativePeriod: {
				value: 'relativePeriods'
			},
            organisationUnit: {
                value: 'organisationUnits',
                name: DV.i18n.organisation_units,
                dimensionName: 'ou',
                objectName: 'ou',
            },
            dimension: {
				value: 'dimension'
				//objectName: 'di'
			},
			value: {
				value: 'value'
			}
        },
        chart: {
            series: 'series',
            category: 'category',
            filter: 'filter',
            column: 'column',
            stackedColumn: 'stackedColumn',
            bar: 'bar',
            stackedBar: 'stackedBar',
            line: 'line',
            area: 'area',
            pie: 'pie'
        },
        data: {
			domain: 'domain_',
			targetLine: 'targetline_',
			baseLine: 'baseline_',
			trendLine: 'trendline_'
		},
        image: {
            png: 'png',
            pdf: 'pdf'
        },
        cmd: {
            init: 'init_',
            none: 'none_',
			urlparam: 'id'
        },
        root: {
			id: 'root'
		}
    };

	dim = conf.finals.dimension;

	dim.objectNameMap = {};
	dim.objectNameMap[dim.data.objectName] = dim.data;
	dim.objectNameMap[dim.indicator.objectName] = dim.indicator;
	dim.objectNameMap[dim.dataElement.objectName] = dim.dataElement;
	dim.objectNameMap[dim.operand.objectName] = dim.operand;
	dim.objectNameMap[dim.dataSet.objectName] = dim.dataSet;
	dim.objectNameMap[dim.category.objectName] = dim.category;
	dim.objectNameMap[dim.period.objectName] = dim.period;
	dim.objectNameMap[dim.organisationUnit.objectName] = dim.organisationUnit;
	dim.objectNameMap[dim.dimension.objectName] = dim.dimension;

	conf.period = {
		relativePeriods: {
			'LAST_WEEK': 1,
			'LAST_4_WEEKS': 4,
			'LAST_12_WEEKS': 12,
			'LAST_MONTH': 1,
			'LAST_3_MONTHS': 3,
			'LAST_BIMONTH': 1,
			'LAST_6_BIMONTHS': 6,
			'LAST_12_MONTHS': 12,
			'LAST_QUARTER': 1,
			'LAST_4_QUARTERS': 4,
			'LAST_SIX_MONTH': 1,
			'LAST_2_SIXMONTHS': 2,
			'LAST_FINANCIAL_YEAR': 1,
			'LAST_5_FINANCIAL_YEARS': 6,
			'THIS_YEAR': 1,
			'LAST_YEAR': 1,
			'LAST_5_YEARS': 5
		},
		relativePeriodValueKeys: {
			'LAST_WEEK': 'lastWeek',
			'LAST_4_WEEKS': 'last4Weeks',
			'LAST_12_WEEKS': 'last12Weeks',
			'LAST_MONTH': 'lastMonth',
			'LAST_3_MONTHS': 'last3Months',
			'LAST_12_MONTHS': 'last12Months',
			'LAST_BIMONTH': 'lastBimonth',
			'LAST_6_BIMONTHS': 'last6BiMonths',
			'LAST_QUARTER': 'lastQuarter',
			'LAST_4_QUARTERS': 'last4Quarters',
			'LAST_SIX_MONTH': 'lastSixMonth',
			'LAST_2_SIXMONTHS': 'last2SixMonths',
			'LAST_FINANCIAL_YEAR': 'lastFinancialYear',
			'LAST_5_FINANCIAL_YEARS': 'last5FinancialYears',
			'THIS_YEAR': 'thisYear',
			'LAST_YEAR': 'lastYear',
			'LAST_5_YEARS': 'last5Years'
		},
		relativePeriodParamKeys: {
			'lastWeek': 'LAST_WEEK',
			'last4Weeks': 'LAST_4_WEEKS',
			'last12Weeks': 'LAST_12_WEEKS',
			'lastMonth': 'LAST_MONTH',
			'last3Months': 'LAST_3_MONTHS',
			'last12Months': 'LAST_12_MONTHS',
			'lastBimonth': 'LAST_BIMONTH',
			'last6BiMonths': 'LAST_6_BIMONTHS',
			'lastQuarter': 'LAST_QUARTER',
			'last4Quarters': 'LAST_4_QUARTERS',
			'lastSixMonth': 'LAST_SIX_MONTH',
			'last2SixMonths': 'LAST_2_SIXMONTHS',
			'lastFinancialYear': 'LAST_FINANCIAL_YEAR',
			'last5FinancialYears': 'LAST_5_FINANCIAL_YEARS',
			'thisYear': 'THIS_YEAR',
			'lastYear': 'LAST_YEAR',
			'last5Years': 'LAST_5_YEARS'
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

    conf.chart = {
        style: {
            inset: 30,
            fontFamily: 'Arial,Sans-serif,Lucida Grande,Ubuntu'
        },
        theme: {
            dv1: ['#94ae0a', '#0b3b68', '#a61120', '#ff8809', '#7c7474', '#a61187', '#ffd13e', '#24ad9a', '#a66111', '#414141', '#4500c4', '#1d5700']
        }
    };

    conf.statusbar = {
		icon: {
			error: 'error_s.png',
			warning: 'warning.png',
			ok: 'ok.png'
		}
	};

    conf.layout = {
        west_width: 424,
        west_fieldset_width: 416,
        west_width_padding: 4,
        west_fill: 2,
        west_fill_accordion_indicator: 59,
        west_fill_accordion_dataelement: 59,
        west_fill_accordion_dataset: 33,
        west_fill_accordion_period: 296,
        west_fill_accordion_organisationunit: 62,
        west_maxheight_accordion_indicator: 350,
        west_maxheight_accordion_dataelement: 350,
        west_maxheight_accordion_dataset: 350,
        west_maxheight_accordion_period: 513,
        west_maxheight_accordion_organisationunit: 500,
        west_maxheight_accordion_group: 350,
        west_scrollbarheight_accordion_indicator: 300,
        west_scrollbarheight_accordion_dataelement: 300,
        west_scrollbarheight_accordion_dataset: 300,
        west_scrollbarheight_accordion_period: 450,
        west_scrollbarheight_accordion_organisationunit: 450,
        west_scrollbarheight_accordion_group: 300,
        east_tbar_height: 31,
        east_gridcolumn_height: 30,
        form_label_width: 55,
        window_favorite_ypos: 100,
        window_confirm_width: 250,
        window_share_width: 500,
        grid_favorite_width: 420,
        grid_row_height: 27,
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

	return conf;
};

DV.core.getUtil = function(dv) {
	var util = {};

	util.array = {
		sortDimensions: function(dimensions, key) {
			key = key || 'dimensionName';

			// Sort object order
			Ext.Array.sort(dimensions, function(a,b) {
				if (a[key] < b[key]) {
					return -1;
				}
				if (a[key] > b[key]) {
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

	util.window = {
		setAnchorPosition: function(w, target) {
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
		},
		addHideOnBlurHandler: function(w) {
			var el = Ext.get(Ext.query('.x-mask')[0]);

			el.on('click', function() {
				if (w.hideOnBlur) {
					w.hide();
				}
			});

			w.hasHideOnBlurHandler = true;
		},
		addDestroyOnBlurHandler: function(w) {
			var el = Ext.get(Ext.query('.x-mask')[0]);

			el.on('click', function() {
				if (w.destroyOnBlur) {
					w.destroy();
				}
			});

			w.hasDestroyOnBlurHandler = true;
		}
	};

	util.mask = {
		showMask: function(cmp, str) {
			if (DV.mask) {
				DV.mask.destroy();
			}
			DV.mask = new Ext.LoadMask(cmp, {msg: str});
			DV.mask.show();
		},
		hideMask: function() {
			if (DV.mask) {
				DV.mask.hide();
			}
		}
	};

	util.number = {
		isInteger: function(n) {
			var str = new String(n);
			if (str.indexOf('-') > -1) {
				var d = str.substr(str.indexOf('-') + 1);
				return (d.length === 1 && d == '0');
			}
			return false;
		},
		allValuesAreIntegers: function(values) {
			for (var i = 0; i < values.length; i++) {
				if (!this.isInteger(values[i].value)) {
					return false;
				}
			}
			return true;
		},
		getChartAxisFormatRenderer: function() {
			return this.allValuesAreIntegers(DV.value.values) ? '0' : '0.0';
		}
	};

	util.value = {
		jsonfy: function(values) {
			var a = [];
			for (var i = 0; i < values.length; i++) {
				var v = {
					value: parseFloat(values[i][0]),
					data: values[i][1],
					period: values[i][2],
					organisationunit: values[i][3]
				};
				a.push(v);
			}
			return a;
		}
	};

	util.chart = {
		extendLayout: function(layout) {
			var xLayout = Ext.clone(layout),
				dimConf = dv.conf.finals.dimension,

				axisDimensions = [].concat(Ext.clone(xLayout.columns) || [], Ext.clone(xLayout.rows) || []),
				axisDimensionNames = [],
				axisObjectNames = [],
				axisItems = [],

				filterDimensions = Ext.clone(xLayout.filters) || [],
				filterDimensionNames = [],
				filterObjectNames = [],
				filterItems = [],

				objectNameDimensionMap = {},
				dimensionNameItemsMap = {},

				columns,
				columnsDimensionNames = [],
				rows,
				rowsDimensionNames = [],
				filters,
				filtersDimensionNames = [],

				objectNameRecordsMap = {};

			xLayout.extended = {};

			// Axis
			for (var i = 0, dim, items; i < axisDimensions.length; i++) {
				dim = axisDimensions[i];
				items = [];

				dim.dimensionName = dv.conf.finals.dimension.objectNameMap[dim.dimension].dimensionName;
				dim.objectName = dim.dimension;

				axisDimensionNames.push(dim.dimensionName);
				axisObjectNames.push(dim.objectName);

				for (var j = 0; j < dim.items.length; j++) {
					items.push(dim.items[j].id);
				}

				dim.items = items;

				axisItems = axisItems.concat(items);
			}

			// Filter
			for (var i = 0, dim, items; i < filterDimensions.length; i++) {
				dim = filterDimensions[i];
				items = [];

				dim.dimensionName = dv.conf.finals.dimension.objectNameMap[dim.dimension].dimensionName;
				dim.objectName = dim.dimension;

				filterDimensionNames.push(dim.dimensionName);
				filterObjectNames.push(dim.objectName);

				for (var j = 0; j < dim.items.length; j++) {
					items.push(dim.items[j].id);
				}

				dim.items = items;

				filterItems = filterItems.concat(items);
			}

			// Axis
			xLayout.extended.axisDimensions = axisDimensions;
			xLayout.extended.axisDimensionNames = Ext.Array.unique(axisDimensionNames);
			xLayout.extended.axisObjectNames = axisObjectNames;
			xLayout.extended.axisItems = axisItems;

			// Filter
			xLayout.extended.filterDimensions = filterDimensions;
			xLayout.extended.filterDimensionNames = Ext.Array.unique(filterDimensionNames);
			xLayout.extended.filterObjectNames = filterObjectNames;
			xLayout.extended.filterItems = filterItems;

			// All
			xLayout.extended.dimensions = [].concat(axisDimensions, filterDimensions);
			xLayout.extended.dimensionNames = Ext.Array.unique([].concat(axisDimensionNames, filterDimensionNames));
			xLayout.extended.objectNames = [].concat(axisObjectNames, filterObjectNames);
			xLayout.extended.items = [].concat(axisItems, filterItems);

			// Sorted axis
			xLayout.extended.sortedAxisDimensions = dv.util.array.sortDimensions(Ext.clone(axisDimensions));
			xLayout.extended.sortedAxisDimensionNames = Ext.Array.unique(Ext.clone(axisDimensionNames).sort());
			xLayout.extended.sortedAxisObjectNames = Ext.clone(axisObjectNames).sort();
			xLayout.extended.sortedAxisItems = Ext.clone(axisItems).sort();

			// Sorted filter
			xLayout.extended.sortedFilterDimensions = dv.util.array.sortDimensions(Ext.clone(filterDimensions));
			xLayout.extended.sortedFilterDimensionNames = Ext.Array.unique(Ext.clone(filterDimensionNames).sort());
			xLayout.extended.sortedFilterObjectNames = Ext.clone(filterObjectNames).sort();
			xLayout.extended.sortedFilterItems = Ext.clone(filterItems).sort();

			// Sorted all
			xLayout.extended.sortedDimensions = [].concat(xLayout.extended.sortedAxisDimensions, xLayout.extended.sortedFilterDimensions);
			xLayout.extended.sortedDimensionNames = Ext.Array.unique([].concat(xLayout.extended.sortedAxisDimensionNames, xLayout.extended.sortedFilterDimensionNames));
			xLayout.extended.sortedObjectNames = [].concat(xLayout.extended.sortedAxisObjectNames, xLayout.extended.sortedFilterObjectNames);
			xLayout.extended.sortedItems = [].concat(xLayout.extended.sortedAxisItems, xLayout.extended.sortedFilterItems);

			// Maps

			// Add dimensionName keys
			for (var i = 0, name; i < xLayout.extended.dimensionNames.length; i++) {
				name = xLayout.extended.dimensionNames[i];
				dimensionNameItemsMap[name] = [];
			}

			// Add dimensions and items
			for (var i = 0, dim; i < xLayout.extended.dimensions.length; i++) {
				dim = xLayout.extended.dimensions[i];

				// objectName : object
				objectNameDimensionMap[dim.objectName] = dim;

				// dimensionName : items
				dimensionNameItemsMap[dim.dimensionName] = dimensionNameItemsMap[dim.dimensionName].concat(Ext.clone(dim.items));
			}

			xLayout.extended.objectNameDimensionMap = objectNameDimensionMap;
			xLayout.extended.dimensionNameItemsMap = dimensionNameItemsMap;

			// Columns, rows, filters
			columns = Ext.clone(xLayout.columns);
			for (var i = 0, dim; i < columns.length; i++) {
				dim = columns[i];
				dim.objectName = dim.dimension;
				dim.dimensionName = dimConf.objectNameMap[dim.objectName].dimensionName;
				dim.records = Ext.clone(dim.items);
				dim.items = xLayout.extended.objectNameDimensionMap[dim.objectName].items;

				objectNameRecordsMap[dim.objectName] = dim.records;
			}

			rows = Ext.clone(xLayout.rows);
			for (var i = 0, dim; i < rows.length; i++) {
				dim = rows[i];
				dim.objectName = dim.dimension;
				dim.dimensionName = dimConf.objectNameMap[dim.objectName].dimensionName;
				dim.records = Ext.clone(dim.items);
				dim.items = xLayout.extended.objectNameDimensionMap[dim.objectName].items;

				objectNameRecordsMap[dim.objectName] = Ext.clone(dim.records);
			}

			filters = Ext.clone(xLayout.filters);
			for (var i = 0, dim; i < filters.length; i++) {
				dim = filters[i];
				dim.objectName = dim.dimension;
				dim.dimensionName = dimConf.objectNameMap[dim.objectName].dimensionName;
				dim.records = Ext.clone(dim.items);
				dim.items = xLayout.extended.objectNameDimensionMap[dim.objectName].items;

				objectNameRecordsMap[dim.objectName] = Ext.clone(dim.records);
			}

			xLayout.extended.columns = columns;
			xLayout.extended.rows = rows;
			xLayout.extended.filters = filters;

			xLayout.extended.objectNameRecordsMap = objectNameRecordsMap;

			// columnsDimensionNames, rowsDimensionNames, filtersDimensionNames
			for (var i = 0; i < xLayout.extended.columns.length; i++) {
				columnsDimensionNames.push(xLayout.extended.columns[i].dimensionName);
			}

			for (var i = 0; i < xLayout.extended.rows.length; i++) {
				rowsDimensionNames.push(xLayout.extended.rows[i].dimensionName);
			}

			for (var i = 0; i < xLayout.extended.filters.length; i++) {
				filtersDimensionNames.push(xLayout.extended.filters[i].dimensionName);
			}

			xLayout.extended.columnsDimensionNames = columnsDimensionNames;
			xLayout.extended.rowsDimensionNames = rowsDimensionNames;
			xLayout.extended.filtersDimensionNames = filtersDimensionNames;

			return xLayout;
		},

		createChart: function(layout, dv) {
			var dimConf = dv.conf.finals.dimension,
				getSyncronizedXLayout,
				getParamString,
				validateResponse,
				extendResponse,
				getDefaultStore,
				getDefaultNumericAxis,
				getDefaultCategoryAxis,
				getDefaultSeries,
				getDefaultTrendLines,
				getDefaultTargetLine,
				getDefaultBaseLine,
				getDefaultTips,
				getDefaultChartTitle,
				getDefaultChart,
				validateUrl,
				generator = {},
				initialize;

			getParamString = function(xLayout) {
				var sortedAxisDimensionNames = xLayout.extended.sortedAxisDimensionNames,
					sortedFilterDimensions = xLayout.extended.sortedFilterDimensions,
					paramString = '?',
					dimConf = dv.conf.finals.dimension,
					addCategoryDimension = false,
					map = xLayout.extended.dimensionNameItemsMap,
					items;

				for (var i = 0, dimensionName; i < sortedAxisDimensionNames.length; i++) {
					dimensionName = sortedAxisDimensionNames[i];

					paramString += 'dimension=' + dimensionName;

					items = Ext.clone(xLayout.extended.dimensionNameItemsMap[dimensionName]).sort();

					if (dimensionName === dimConf.dataElement.dimensionName) {
						for (var j = 0, index; j < items.length; j++) {
							index = items[j].indexOf('-');

							if (index > 0) {
								addCategoryDimension = true;
								items[j] = items[j].substr(0, index);
							}
						}

						items = Ext.Array.unique(items);
					}

					paramString += ':' + items.join(';');

					if (i < (sortedAxisDimensionNames.length - 1)) {
						paramString += '&';
					}
				}

				if (addCategoryDimension) {
					paramString += '&dimension=' + dv.conf.finals.dimension.category.dimensionName;
				}

				if (Ext.isArray(sortedFilterDimensions) && sortedFilterDimensions.length) {
					for (var i = 0, dim; i < sortedFilterDimensions.length; i++) {
						dim = sortedFilterDimensions[i];

						paramString += '&filter=' + dim.dimensionName + ':' + dim.items.join(';');
					}
				}

				return paramString;
			};

			getSyncronizedXLayout = function(xLayout, response) {
				var dimensions = [].concat(xLayout.columns, xLayout.rows, xLayout.filters),
					xOuDimension = xLayout.extended.objectNameDimensionMap[dimConf.organisationUnit.objectName],
					isUserOrgunit = Ext.Array.contains(xOuDimension.items, 'USER_ORGUNIT'),
					isUserOrgunitChildren = Ext.Array.contains(xOuDimension.items, 'USER_ORGUNIT_CHILDREN'),
					items = [],
					isDirty = false;

				// Add user orgunits
				if (xOuDimension && (isUserOrgunit || isUserOrgunitChildren)) {
					if (isUserOrgunit) {
						items.push(Ext.clone(dv.init.user.ou));
					}
					if (isUserOrgunitChildren) {
						items = items.concat(Ext.clone(dv.init.user.ouc));
					}

					for (var i = 0; i < dimensions.length; i++) {
						if (dimensions[i].dimension === dimConf.organisationUnit.objectName) {
							dimensions[i].items = items;
						}
					}

					isDirty = true;
				}

				if (isDirty) {
					delete xLayout.extended;
					xLayout = dv.util.chart.extendLayout(xLayout);
				}

				return xLayout;
			};

			validateResponse = function(response) {
				if (!(response && Ext.isObject(response))) {
					alert('Data response invalid');
					return false;
				}

				if (!(response.headers && Ext.isArray(response.headers) && response.headers.length)) {
					alert('Data response invalid');
					return false;
				}

				if (!(Ext.isNumber(response.width) && response.width > 0 &&
					  Ext.isNumber(response.height) && response.height > 0 &&
					  Ext.isArray(response.rows) && response.rows.length > 0)) {
					alert('No values found');
					return false;
				}

				if (response.headers.length !== response.rows[0].length) {
					alert('Data response invalid');
					return false;
				}

				return true;
			};

			extendResponse = function(response, xLayout) {
				response.nameHeaderMap = {};
				response.idValueMap = {};

				var extendHeaders = function() {

					// Extend headers: index, items, size
					for (var i = 0, header; i < response.headers.length; i++) {
						header = response.headers[i];
						header.index = i;

						if (header.meta) {

							// Categories
							if (header.name === dv.conf.finals.dimension.category.dimensionName) {
								header.items = [].concat(response.metaData[dv.conf.finals.dimension.category.dimensionName]);
							}
							// Periods
							else if (header.name === dv.conf.finals.dimension.period.dimensionName) {
								header.items = [].concat(response.metaData[dv.conf.finals.dimension.period.dimensionName]);
							}
							else {
								header.items = xLayout.extended.dimensionNameItemsMap[header.name];
							}

							header.size = header.items.length;
						}
					}

					// nameHeaderMap (headerName: header)
					for (var i = 0, header; i < response.headers.length; i++) {
						header = response.headers[i];

						response.nameHeaderMap[header.name] = header;
					}
				}();

				var createValueIdMap = function() {
					var valueHeaderIndex = response.nameHeaderMap[dv.conf.finals.dimension.value.value].index,
						coHeader = response.nameHeaderMap[dv.conf.finals.dimension.category.dimensionName],
						axisDimensionNames = xLayout.extended.axisDimensionNames,
						idIndexOrder = [];

					// idIndexOrder
					for (var i = 0; i < axisDimensionNames.length; i++) {
						idIndexOrder.push(response.nameHeaderMap[axisDimensionNames[i]].index);

						// If co exists in response, add co after dx
						if (coHeader && axisDimensionNames[i] === dv.conf.finals.dimension.data.dimensionName) {
							idIndexOrder.push(coHeader.index);
						}
					}

					// idValueMap
					for (var i = 0, row, id; i < response.rows.length; i++) {
						row = response.rows[i];
						id = '';

						for (var j = 0; j < idIndexOrder.length; j++) {
							id += row[idIndexOrder[j]];
						}

						response.idValueMap[id] = parseFloat(row[valueHeaderIndex]);
					}
				}();

				var getMinMax = function() {
					var valueIndex = response.nameHeaderMap.value.index,
						values = [];

					for (var i = 0; i < response.rows.length; i++) {
						values.push(parseFloat(response.rows[i][valueIndex]));
					}

					response.min = Ext.Array.min(values);
					response.max = Ext.Array.max(values);
				}();

				return response;
			};

			validateUrl = function(url) {
				if (!Ext.isString(url) || url.length > 2000) {
					var percent = ((url.length - 2000) / url.length) * 100;
					alert('Too many parameters selected. Please reduce the number of parameters by at least ' + percent.toFixed(0) + '%.');
					return;
				}

				return true;
			};

			getDefaultStore = function(xResponse, xLayout) {
				var pe = dv.conf.finals.dimension.period.dimensionName,
					columnDimensionName = xLayout.extended.columns[0].dimensionName,
					rowDimensionName = xLayout.extended.rows[0].dimensionName,

					data = [],
					columnIds = columnDimensionName === pe ? xResponse.metaData.pe : xLayout.extended.dimensionNameItemsMap[columnDimensionName],
					rowIds = rowDimensionName === pe ? xResponse.metaData.pe : xLayout.extended.dimensionNameItemsMap[rowDimensionName],
					trendLineFields = [],
					targetLineFields = [],
					baseLineFields = [],
					store;

				// Data
				for (var i = 0, obj, category; i < rowIds.length; i++) {
					obj = {};
					category = rowIds[i];

					obj[dv.conf.finals.data.domain] = xResponse.metaData.names[category];
					for (var j = 0, id; j < columnIds.length; j++) {
						id = dv.util.str.replaceAll(columnIds[j], '-', '') + rowIds[i];
						obj[columnIds[j]] = xResponse.idValueMap[id];
					}

					data.push(obj);
				}

				// Trend lines
				if (xLayout.showTrendLine) {
					for (var i = 0, regression, key; i < columnIds.length; i++) {
						regression = new SimpleRegression();
						key = dv.conf.finals.data.trendLine + columnIds[i];

						for (var j = 0; j < data.length; j++) {
							regression.addData(j, data[j][columnIds[i]]);
						}

						for (var j = 0; j < data.length; j++) {
							data[j][key] = parseFloat(regression.predict(j).toFixed(1));
						}

						trendLineFields.push(key);
						xResponse.metaData.names[key] = DV.i18n.trend + ' (' + xResponse.metaData.names[columnIds[i]] + ')';
					}
				}

				// Target line
				if (Ext.isNumber(xLayout.targetLineValue) || Ext.isNumber(parseFloat(xLayout.targetLineValue))) {
					for (var i = 0; i < data.length; i++) {
						data[i][dv.conf.finals.data.targetLine] = parseFloat(xLayout.targetLineValue);
					}

					targetLineFields.push(dv.conf.finals.data.targetLine);
				}

				// Base line
				if (Ext.isNumber(xLayout.baseLineValue) || Ext.isNumber(parseFloat(xLayout.baseLineValue))) {
					for (var i = 0; i < data.length; i++) {
						data[i][dv.conf.finals.data.baseLine] = parseFloat(xLayout.baseLineValue);
					}

					baseLineFields.push(dv.conf.finals.data.baseLine);
				}

				store = Ext.create('Ext.data.Store', {
					fields: function() {
						var fields = Ext.clone(columnIds);
						fields.push(dv.conf.finals.data.domain);
						fields = fields.concat(trendLineFields, targetLineFields, baseLineFields);

						return fields;
					}(),
					data: data
				});

				store.rangeFields = columnIds;
				store.domainFields = [dv.conf.finals.data.domain];
				store.trendLineFields = trendLineFields;
				store.targetLineFields = targetLineFields;
				store.baseLineFields = baseLineFields;
				store.numericFields = [].concat(store.rangeFields, store.trendLineFields, store.targetLineFields, store.baseLineFields);

				store.getMaximum = function() {
					var maximums = [];

					for (var i = 0; i < store.numericFields.length; i++) {
						maximums.push(store.max(store.numericFields[i]));
					}

					return Ext.Array.max(maximums);
				};

				store.getMinimum = function() {
					var minimums = [];

					for (var i = 0; i < store.numericFields.length; i++) {
						minimums.push(store.max(store.numericFields[i]));
					}

					return Ext.Array.min(minimums);
				};

				store.getMaximumSum = function() {
					var sums = [],
						recordSum = 0;

					store.each(function(record) {
						recordSum = 0;

						for (var i = 0; i < store.rangeFields.length; i++) {
							recordSum += record.data[store.rangeFields[i]];
						}

						sums.push(recordSum);
					});

					return Ext.Array.max(sums);
				};


console.log("data", data);
console.log("rangeFields", store.rangeFields);
console.log("domainFields", store.domainFields);
console.log("trendLineFields", store.trendLineFields);
console.log("targetLineFields", store.targetLineFields);
console.log("baseLineFields", store.baseLineFields);

				return store;
			};

			getDefaultNumericAxis = function(store, xResponse, xLayout) {
				var typeConf = dv.conf.finals.chart,
					minimum = store.getMinimum(),
					maximum,
					axis;

				// Set maximum if stacked + extra line
				if ((xLayout.type === typeConf.stackedColumn || xLayout.type === typeConf.stackedBar) &&
					(xLayout.showTrendLine || xLayout.targetLineValue || xLayout.baseLineValue)) {
					var a = [store.getMaximum(), store.getMaximumSum()];
					maximum = Math.ceil(Ext.Array.max(a) * 1.1);
					maximum = Math.floor(maximum / 10) * 10;
				}

				axis = {
					type: 'Numeric',
					position: 'left',
					fields: store.numericFields,
					minimum: minimum < 0 ? minimum : 0,
					label: {
						renderer: Ext.util.Format.numberRenderer('0,0')
					},
					grid: {
						odd: {
							opacity: 1,
							stroke: '#aaa',
							'stroke-width': 0.1
						},
						even: {
							opacity: 1,
							stroke: '#aaa',
							'stroke-width': 0.1
						}
					}
				};

				if (maximum) {
					axis.maximum = maximum;
				}

				if (xLayout.rangeAxisTitle) {
					axis.title = xLayout.rangeAxisTitle;
				}

				return axis;
			};

			getDefaultCategoryAxis = function(store, xLayout) {
				var axis = {
					type: 'Category',
					position: 'bottom',
					fields: store.domainFields,
					label: {
						rotate: {
							degrees: 330
						}
					}
				};

				if (xLayout.domainAxisTitle) {
					axis.title = xLayout.domainAxisTitle;
				}

				return axis;
			};

			getDefaultSeriesTitle = function(store, xResponse) {
				var a = [],
					ids;

				for (var i = 0, id; i < store.rangeFields.length; i++) {
					id = store.rangeFields[i];

					if (id.indexOf('-') !== -1) {
						ids = id.split('-');
						id = '';

						for (var j = 0; j < ids.length; j++) {
							id += j !== 0 ? ' ' : '';
							id += xResponse.metaData.names[ids[j]];
						}

						a.push(id);
					}
					else {
						a.push(xResponse.metaData.names[id]);
					}
				}

				return a;
			};

			getDefaultSeries = function(store, xResponse, xLayout) {
				var main = {
					type: 'column',
					axis: 'left',
					xField: store.domainFields,
					yField: store.rangeFields,
					style: {
						opacity: 0.8,
						lineWidth: 3
					},
					markerConfig: {
						type: 'circle',
						radius: 4
					},
					tips: getDefaultTips(),
					title: getDefaultSeriesTitle(store, xResponse)
				};

				if (xLayout.showValues) {
					main.label = {
						display: 'outside',
						'text-anchor': 'middle',
						field: store.rangeFields,
						font: dv.conf.chart.style.fontFamily
					};
				}

				return main;
			};

			getDefaultTrendLines = function(store, xResponse) {
				var a = [];

				for (var i = 0; i < store.trendLineFields.length; i++) {
					a.push({
						type: 'line',
						axis: 'left',
						xField: store.domainFields,
						yField: store.trendLineFields[i],
						style: {
							opacity: 0.8,
							lineWidth: 3,
							'stroke-dasharray': 8
						},
						markerConfig: {
							type: 'circle',
							radius: 0
						},
						title: xResponse.metaData.names[store.trendLineFields[i]]
					});
				}

				return a;
			};

			getDefaultTargetLine = function(store, xLayout) {
				return {
					type: 'line',
					axis: 'left',
					xField: store.domainFields,
					yField: store.targetLineFields,
					style: {
						opacity: 1,
						lineWidth: 2,
						'stroke-width': 1,
						stroke: '#041423'
					},
					showMarkers: false,
					title: (Ext.isString(xLayout.targetLineTitle) ? xLayout.targetLineTitle : DV.i18n.target) + ' (' + xLayout.targetLineValue + ')'
				};
			};

			getDefaultBaseLine = function(store, xLayout) {
				return {
					type: 'line',
					axis: 'left',
					xField: store.domainFields,
					yField: store.baseLineFields,
					style: {
						opacity: 1,
						lineWidth: 2,
						'stroke-width': 1,
						stroke: '#041423'
					},
					showMarkers: false,
					title: (Ext.isString(xLayout.baseLineTitle) ? xLayout.baseLineTitle : DV.i18n.base) + ' (' + xLayout.baseLineValue + ')'
				};
			};

			getDefaultTips = function() {
				return {
					trackMouse: true,
					cls: 'dv-chart-tips',
					renderer: function(si, item) {
						this.update('<div style="text-align:center"><div style="font-size:17px; font-weight:bold">' + item.value[1] + '</div><div style="font-size:10px">' + si.data[dv.conf.finals.data.domain] + '</div></div>');
					}
				};
			};

			setDefaultTheme = function(store, xLayout) {
				var colors = dv.conf.chart.theme.dv1.slice(0, store.rangeFields.length);

				if (xLayout.targetLineValue || xLayout.baseLineValue) {
					colors.push('#051a2e');
				}

				if (xLayout.targetLineValue) {
					colors.push('#051a2e');
				}

				if (xLayout.baseLineValue) {
					colors.push('#051a2e');
				}

				Ext.chart.theme.dv1 = Ext.extend(Ext.chart.theme.Base, {
					constructor: function(config) {
						Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
							seriesThemes: colors,
							colors: colors
						}, config));
					}
				});
			};

			getDefaultLegend = function(store, xResponse) {
				var itemLength = 30,
					charLength = 7,
					numberOfItems = store.rangeFields.length,
					numberOfChars = 0,
					str = '',
					width,
					isVertical = false,
					position = 'top',
					padding = 0;

				for (var i = 0; i < store.rangeFields.length; i++) {
					str += xResponse.metaData.names[store.rangeFields[i]];
				}

				numberOfChars = str.length;

				width = (numberOfItems * itemLength) + (numberOfChars * charLength);

				if (width > dv.viewport.centerRegion.getWidth() - 50) {
					isVertical = true;
					position = 'right';
				}

				if (position === 'right') {
					padding = 5;
				}

				return Ext.create('Ext.chart.Legend', {
					position: position,
					isVertical: isVertical,
					labelFont: '13px ' + dv.conf.chart.style.fontFamily,
					boxStroke: '#ffffff',
					boxStrokeWidth: 0,
					padding: padding
				});
			};

			getDefaultChartTitle = function(store, xResponse, xLayout) {
				var filterItems = xLayout.extended.filterItems,
					a = [],
					text = '';
console.log("filterItems", filterItems);
				if (Ext.isArray(filterItems) && filterItems.length) {
					for (var i = 0; i < filterItems.length; i++) {
						text += xResponse.metaData.names[filterItems[i]];
						text += i < filterItems.length - 1 ? ', ' : '';
					}
				}

				if (xLayout.title) {
					text = xLayout.title;
				}

				return Ext.create('Ext.draw.Sprite', {
					type: 'text',
					text: text,
					font: 'bold 19px ' + dv.conf.chart.style.fontFamily,
					fill: '#111',
					height: 20,
					y: 	20
				});
			};

			getDefaultChartSizeHandler = function() {
				return function() {
					this.animate = false;
					this.setWidth(dv.viewport.centerRegion.getWidth());
					this.setHeight(dv.viewport.centerRegion.getHeight() - 25);
					this.animate = true;
				};
			};

			getDefaultChartTitlePositionHandler = function() {
				return function() {
					if (this.items) {
						var title = this.items[0],
							legend = this.legend,
							legendCenterX,
							titleX;

						if (this.legend.position === 'top') {
							legendCenterX = legend.x + (legend.width / 2);
							titleX = legendCenterX - (title.el.getWidth() / 2);
						}
						else {
							var legendWidth = legend ? legend.width : 0;
							titleX = (this.width / 2) - (title.el.getWidth() / 2);
						}

						title.setAttributes({
							x: titleX
						}, true);
					}
				};
			};

			getDefaultChart = function(store, axes, series, xResponse, xLayout) {
				var chart,
					config = {
						store: store,
						axes: axes,
						series: series,
						animate: true,
						shadow: false,
						insetPadding: 35,
						width: dv.viewport.centerRegion.getWidth(),
						height: dv.viewport.centerRegion.getHeight() - 25,
						theme: 'dv1'
					};

				// Legend
				if (!xLayout.hideLegend) {
					config.legend = getDefaultLegend(store, xResponse);

					if (config.legend.position === 'right') {
						config.insetPadding = 40;
					}
				}

				// Title
				if (!xLayout.hideTitle) {
					config.items = [getDefaultChartTitle(store, xResponse, xLayout)];
				}
				else {
					config.insetPadding = 10;
				}

				chart = Ext.create('Ext.chart.Chart', config);

				chart.setChartSize = getDefaultChartSizeHandler();
				chart.setTitlePosition = getDefaultChartTitlePositionHandler();

				chart.onViewportResize = function() {
					chart.setChartSize();
					chart.redraw();
					chart.setTitlePosition();
				};

				chart.on('afterrender', function() {
					chart.setTitlePosition();
				});

				return chart;
			};

			generator.column = function(xResponse, xLayout) {
				var store = getDefaultStore(xResponse, xLayout),
					numericAxis = getDefaultNumericAxis(store, xResponse, xLayout),
					categoryAxis = getDefaultCategoryAxis(store, xLayout),
					axes = [numericAxis, categoryAxis],
					series = [getDefaultSeries(store, xResponse, xLayout)];

				// Options
				if (xLayout.showTrendLine) {
					series = getDefaultTrendLines(store, xResponse).concat(series);
				}

				if (xLayout.targetLineValue) {
					series.push(getDefaultTargetLine(store, xLayout));
				}

				if (xLayout.baseLineValue) {
					series.push(getDefaultBaseLine(store, xLayout));
				}

				// Theme
				setDefaultTheme(store, xLayout);

				return getDefaultChart(store, axes, series, xResponse, xLayout);
			};

			generator.stackedColumn = function(xResponse, xLayout) {
				var chart = this.column(xResponse, xLayout);

				for (var i = 0, item; i < chart.series.items.length; i++) {
					item = chart.series.items[i];

					if (item.type === dv.conf.finals.chart.column) {
						item.stacked = true;
					}
				}

				return chart;
			};

			generator.bar = function(xResponse, xLayout) {
				var store = getDefaultStore(xResponse, xLayout),
					numericAxis = getDefaultNumericAxis(store, xResponse, xLayout),
					categoryAxis = getDefaultCategoryAxis(store, xLayout),
					axes,
					series = getDefaultSeries(store, xResponse, xLayout),
					trendLines,
					targetLine,
					baseLine,
					chart;

				// Axes
				numericAxis.position = 'bottom';
				categoryAxis.position = 'left';
				axes = [numericAxis, categoryAxis];

				// Series
				series.type = 'bar';
				series.axis = 'bottom';

				// Options
				if (xLayout.showValues) {
					series.label = {
						display: 'outside',
						'text-anchor': 'middle',
						field: store.rangeFields
					};
				}

				series = [series];

				if (xLayout.showTrendLine) {
					trendLines = getDefaultTrendLines(store, xResponse);

					for (var i = 0; i < trendLines.length; i++) {
						trendLines[i].axis = 'bottom';
						trendLines[i].xField = store.trendLineFields[i];
						trendLines[i].yField = store.domainFields;
					}

					series = trendLines.concat(series);
				}

				if (xLayout.targetLineValue) {
					targetLine = getDefaultTargetLine(store, xLayout);
					targetLine.axis = 'bottom';
					targetLine.xField = store.targetLineFields;
					targetLine.yField = store.domainFields;

					series.push(targetLine);
				}

				if (xLayout.baseLineValue) {
					baseLine = getDefaultBaseLine(store, xLayout);
					baseLine.axis = 'bottom';
					baseLine.xField = store.baseLineFields;
					baseLine.yField = store.domainFields;

					series.push(baseLine);
				}

				// Theme
				setDefaultTheme(store, xLayout);

				return getDefaultChart(store, axes, series, xResponse, xLayout);
			};

			generator.stackedBar = function(xResponse, xLayout) {
				var chart = this.bar(xResponse, xLayout);

				for (var i = 0, item; i < chart.series.items.length; i++) {
					item = chart.series.items[i];

					if (item.type === dv.conf.finals.chart.bar) {
						item.stacked = true;
					}
				}

				return chart;
			};

			generator.line = function(xResponse, xLayout) {
				var store = getDefaultStore(xResponse, xLayout),
					numericAxis = getDefaultNumericAxis(store, xResponse, xLayout),
					categoryAxis = getDefaultCategoryAxis(store, xLayout),
					axes = [numericAxis, categoryAxis],
					series = [],
					colors = dv.conf.chart.theme.dv1.slice(0, store.rangeFields.length);

				// Series
				for (var i = 0, line; i < store.rangeFields.length; i++) {
					line = {
						type: 'line',
						axis: 'left',
						xField: store.domainFields,
						yField: store.rangeFields[i],
						style: {
							opacity: 0.8,
							lineWidth: 3
						},
						markerConfig: {
							type: 'circle',
							radius: 4
						},
						tips: getDefaultTips(),
						title: getDefaultSeriesTitle(store, xResponse)
					};

					//if (xLayout.showValues) {
						//line.label = {
							//display: 'rotate',
							//'text-anchor': 'middle',
							//field: store.rangeFields[i]
						//};
					//}

					series.push(line);
				}

				// Options, theme colors
				if (xLayout.showTrendLine) {
					series = getDefaultTrendLines(store, xResponse).concat(series);

					colors = colors.concat(colors);
				}

				if (xLayout.targetLineValue) {
					series.push(getDefaultTargetLine(store, xLayout));

					colors.push('#051a2e');
				}

				if (xLayout.baseLineValue) {
					series.push(getDefaultBaseLine(store, xLayout));

					colors.push('#051a2e');
				}

				// Theme
				Ext.chart.theme.dv1 = Ext.extend(Ext.chart.theme.Base, {
					constructor: function(config) {
						Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
							seriesThemes: colors,
							colors: colors
						}, config));
					}
				});

				return getDefaultChart(store, axes, series, xResponse, xLayout);
			};

			generator.area = function(xResponse, xLayout) {
				var store = getDefaultStore(xResponse, xLayout),
					numericAxis = getDefaultNumericAxis(store, xResponse, xLayout),
					categoryAxis = getDefaultCategoryAxis(store, xLayout),
					axes = [numericAxis, categoryAxis],
					series = getDefaultSeries(store, xResponse, xLayout);

				series.type = 'area';
				series.style.opacity = 0.7;
				series.style.lineWidth = 0;
				delete series.label;
				delete series.tips;
				series = [series];

				// Options
				if (xLayout.showTrendLine) {
					series = getDefaultTrendLines(store, xResponse).concat(series);
				}

				if (xLayout.targetLineValue) {
					series.push(getDefaultTargetLine(store, xLayout));
				}

				if (xLayout.baseLineValue) {
					series.push(getDefaultBaseLine(store, xLayout));
				}

				// Theme
				setDefaultTheme(store, xLayout);

				return getDefaultChart(store, axes, series, xResponse, xLayout);
			};

			generator.pie = function(xResponse, xLayout) {
				var store = getDefaultStore(xResponse, xLayout),
					series = [{
						type: 'pie',
						field: store.rangeFields[0],
						lengthField: store.rangeFields[0],
						donut: 7,
						showInLegend: true,
						highlight: {
							segment: {
								margin: 5
							}
						},
						label: {
							field: dv.conf.finals.data.domain,
							display: 'middle',
							contrast: true,
							font: '14px ' + dv.conf.chart.style.fontFamily,
							renderer: function(value) {
								var record = store.getAt(store.findExact(dv.conf.finals.data.domain, value));

								return record.data[store.rangeFields[0]];
							}
						},
						style: {
							opacity: 0.8,
							stroke: '#555'
						},
						tips: {
							trackMouse: true,
							cls: 'dv-chart-tips',
							renderer: function(item) {
								this.update('<div style="text-align:center"><div style="font-size:17px; font-weight:bold">' + item.data[store.rangeFields[0]] + '</div><div style="font-size:10px">' + item.data[dv.conf.finals.data.domain] + '</div></div>');
							}
						}
					}],
					colors,
					chart;

				// Theme
				colors = dv.conf.chart.theme.dv1.slice(0, xResponse.nameHeaderMap[xLayout.extended.rows[0].dimensionName].items.length);

				Ext.chart.theme.dv1 = Ext.extend(Ext.chart.theme.Base, {
					constructor: function(config) {
						Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
							seriesThemes: colors,
							colors: colors
						}, config));
					}
				});

				// Chart
				chart = getDefaultChart(store, null, series, xResponse, xLayout);

				chart.legend.position = 'right';
				chart.legend.isVertical = true;
				chart.insetPadding = 20;
				chart.shadow = true;

				return chart;
			};

			initialize = function() {
				var url,
					xLayout,
					xResponse,
					chart;

				xLayout = dv.util.chart.extendLayout(layout);

				dv.paramString = getParamString(xLayout);
				url = dv.init.contextPath + '/api/analytics.json' + dv.paramString;

				if (!validateUrl(url)) {
					return;
				}

				dv.util.mask.showMask(dv.viewport);

				Ext.Ajax.request({
					method: 'GET',
					url: url,
					callbackName: 'analytics',
					timeout: 60000,
					headers: {
						'Content-Type': 'application/json',
						'Accept': 'application/json'
					},
					disableCaching: false,
					failure: function(r) {
						dv.util.mask.hideMask();
						alert(r.responseText);
					},
					success: function(r) {
						var html,
							response = Ext.decode(r.responseText);

						if (!validateResponse(response)) {
							dv.util.mask.hideMask();
							return;
						}

						xLayout = getSyncronizedXLayout(xLayout, response);

						if (!xLayout) {
							dv.util.mask.hideMask();
							return;
						}

						xResponse = extendResponse(response, xLayout);

						chart = generator[xLayout.type](xResponse, xLayout);

						dv.viewport.centerRegion.removeAll(true);
						dv.viewport.centerRegion.add(chart);

						// After table success
						dv.util.mask.hideMask();

						if (dv.viewport.downloadButton) {
							dv.viewport.downloadButton.enable();
						}

						dv.chart = chart;
						dv.xLayout = xLayout;
						dv.xResponse = xResponse;

console.log("xResponse", xResponse);
console.log("xLayout", xLayout);
console.log("chart", chart);
					}
				});

			}();
		},

		loadChart: function(id) {
			if (!Ext.isString(id)) {
				alert('Invalid id');
				return;
			}

			Ext.Ajax.request({
				url: dv.baseUrl + '/api/charts/' + id + '.json?viewClass=dimensional&links=false',
				method: 'GET',
				failure: function(r) {
					dv.util.mask.hideMask();
					alert(r.responseText);
				},
				success: function(r) {
					var layout,
						xLayout;

					r = Ext.decode(r.responseText);
					layout = dv.api.Layout(r);
					xLayout = dv.util.chart.extendLayout(layout);

					if (xLayout) {
						dv.viewport.setFavorite(xLayout);
					}
				}
			});
		}
	};

	return util;
};

DV.core.getAPI = function(dv) {
	var api = {};

	api.Layout = function(config) {
		var layout = {
			type: 'column', // string

			columns: null, // array of {dimension: <objectName>, items: [{id, name, code}]}

			rows: null, // array of {dimension: <objectName>, items: [{id, name, code}]}

			filters: null, // array of {dimension: <objectName>, items: [{id, name, code}]}

			showTrendLine: false, // boolean

			targetLineValue: null, // number

			targetLineTitle: null, // string

			baseLineValue: null, // number

			baseLineTitle: null, // string

			showValues: true, // boolean

			hideLegend: false, // boolean

			hideTitle: false, // boolean

			title: null, // string

			domainAxisTitle: null, // string

			rangeAxisTitle: null, // string

			userOrganisationUnit: false, // boolean

			userOrganisationUnitChildren: false // boolean
		};

		var validateConfig = function() {
			var validateAxis;

			validateAxis = function(axis) {
				if (!(axis && Ext.isArray(axis) && axis.length)) {
					return;
				}

				for (var i = 0, dim; i < axis.length; i++) {
					dim = axis[i];

					if (!(Ext.isObject(dim) && Ext.isString(dim.dimension) && Ext.isArray(dim.items) && dim.items.length)) {
						return;
					}

					for (var j = 0; j < dim.items.length; j++) {
						dim.items[j].id = dim.items[j].id.replace('.', '-');
					}
				}

				return true;
			};

			if (!(config && Ext.isObject(config))) {
				alert(dv.el + ': Layout config is not an object');
				return;
			}

			if (!(validateAxis(config.columns))) {
				alert(dv.el + ': Columns config is invalid');
				return;
			}
			if (!(validateAxis(config.rows))) {
				alert(dv.el + ': Rows config is invalid');
				return;
			}
			if (!(validateAxis(config.filters))) {
				alert(dv.el + ': Filters config is invalid');
				return;
			}

			if (!Ext.isBoolean(config.showTrendLine)) {
				delete config.showTrendLine;
			}
			if (!Ext.isNumber(config.targetLineValue)) {
				delete config.targetLineValue;
			}
			if (!Ext.isString(config.targetLineTitle) || Ext.isEmpty(config.targetLineTitle)) {
				delete config.targetLineTitle;
			}
			if (!Ext.isNumber(config.baseLineValue)) {
				delete config.baseLineValue;
			}
			if (!Ext.isString(config.baseLineTitle) || Ext.isEmpty(config.baseLineTitle)) {
				delete config.baseLineTitle;
			}
			if (!Ext.isBoolean(config.showValues)) {
				delete config.showValues;
			}
			if (!Ext.isBoolean(config.hideLegend)) {
				delete config.hideLegend;
			}
			if (!Ext.isBoolean(config.hideTitle)) {
				delete config.hideTitle;
			}
			if (!Ext.isString(config.title) || Ext.isEmpty(config.title)) {
				delete config.title;
			}
			if (!Ext.isString(config.domainAxisTitle) || Ext.isEmpty(config.domainAxisTitle)) {
				delete config.domainAxisTitle;
			}
			if (!Ext.isString(config.rangeAxisTitle) || Ext.isEmpty(config.rangeAxisTitle)) {
				delete config.rangeAxisTitle;
			}
			if (!Ext.isBoolean(config.userOrganisationUnit)) {
				delete config.userOrganisationUnit;
			}
			if (!Ext.isBoolean(config.userOrganisationUnitChildren)) {
				delete config.userOrganisationUnitChildren;
			}

			return true;
		};

		return function() {
			if (!validateConfig()) {
				return;
			}

			for (var key in config) {
				if (config.hasOwnProperty(key)) {
					layout[key] = config[key];
				}
			}

			return Ext.clone(layout);
		}();
	};

	return api;
};

DV.core.getInstance = function(config) {
	var dv = {};

	dv.baseUrl = config && config.baseUrl ? config.baseUrl : '../../';
	dv.el = config && config.el ? config.el : 'app';

	dv.conf = DV.core.getConfig();
	dv.util = DV.core.getUtil(dv);
	dv.api = DV.core.getAPI(dv);

	return dv;
};

});
