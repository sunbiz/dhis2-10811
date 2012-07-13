DV.conf = {
    init: {
		example: {
			series: ['Series 1', 'Series 2', 'Series 3', 'Series 4'],
			category: ['Category 1', 'Category 2', 'Category 3'],
			filter: [DV.i18n.example_chart],
			values: [84, 73, 87, 82, 91, 69, 82, 75, 83, 76, 73, 85],
			setState: function() {
				DV.c.type = DV.conf.finals.chart.column;
				DV.c.dimension.series = DV.conf.finals.dimension.data.value;
				DV.c.dimension.category = DV.conf.finals.dimension.period.value;
				DV.c.dimension.filter = DV.conf.finals.dimension.organisationunit.value;
				DV.c.series = DV.c.data = {names: this.series};
				DV.c.category = DV.c.period = {names: this.category};
				DV.c.filter = DV.c.organisationunit = {names: this.filter};
				DV.c.targetlinevalue = 78;
				DV.c.targetlinelabel = 'Target label';
				DV.c.rangeaxislabel = 'Range axis label';
				DV.c.domainaxislabel = 'Domain axis label';
			},
			setValues: function() {
				var obj1 = {}, obj2 = {}, obj3 = {}, obj4 = {}, obj5 = {}, obj6 = {}, obj7 = {}, obj8 = {}, obj9 = {}, obj10 = {}, obj11 = {}, obj12 = {};
				var s = DV.c.dimension.series, c = DV.c.dimension.category;
				obj1[s] = this.series[0];
				obj1[c] = this.category[0];
				obj1.value = this.values[0];
				obj2[s] = this.series[1];
				obj2[c] = this.category[0];
				obj2.value = this.values[1];
				obj3[s] = this.series[2];
				obj3[c] = this.category[0];
				obj3.value = this.values[2];
				obj4[s] = this.series[3];
				obj4[c] = this.category[0];
				obj4.value = this.values[3];
				obj5[s] = this.series[0];
				obj5[c] = this.category[1];
				obj5.value = this.values[4];
				obj6[s] = this.series[1];
				obj6[c] = this.category[1];
				obj6[c] = this.category[1];
				obj6.value = this.values[5];
				obj7[s] = this.series[2];
				obj7[c] = this.category[1];
				obj7.value = this.values[6];
				obj8[s] = this.series[3];
				obj8[c] = this.category[1];
				obj8.value = this.values[7];
				obj9[s] = this.series[0];
				obj9[c] = this.category[2];
				obj9.value = this.values[8];
				obj10[s] = this.series[1];
				obj10[c] = this.category[2];
				obj10.value = this.values[9];
				obj11[s] = this.series[2];
				obj11[c] = this.category[2];
				obj11.value = this.values[10];
				obj12[s] = this.series[3];
				obj12[c] = this.category[2];
				obj12.value = this.values[11];
				DV.value.values = [obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10, obj11, obj12];
			}
		},
		ajax: {
			jsonfy: function(r) {
				r = Ext.JSON.decode(r.responseText);
				var obj = {
					system: {
						rootnodes: [],
						ougs: r.system.ougs
					},
					user: {
						id: r.user.id,
						isadmin: r.user.isAdmin,
						ou: r.user.ou,
						ouc: r.user.ouc
					}
				};
				for (var i = 0; i < r.system.rn.length; i++) {
					obj.system.rootnodes.push({id: r.system.rn[i][0], text: r.system.rn[i][1], level: 1});
				}
				return obj;
			}
		}
    },
    finals: {
        ajax: {
            path_visualizer: '../',
            path_api: '../../api/',
            path_commons: '../../dhis-web-commons-ajax-json/',
            path_portal: '../../dhis-web-portal/',
            path_lib: '../../dhis-web-commons/javascripts/',
            path_images: 'images/',
            initialize: 'initialize.action',
            redirect: 'redirect.action',
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
                rawvalue: DV.i18n.data,
                warning: {
					filter: DV.i18n.wm_multiple_filter_ind_de
				}
            },
            indicator: {
                value: 'indicator',
                rawvalue: DV.i18n.indicator,
                paramname: 'in'
            },
            dataelement: {
                value: 'dataelement',
                rawvalue: DV.i18n.data_element,
                paramname: 'de'
            },
            dataset: {
				value: 'dataset',
                rawvalue: DV.i18n.dataset,
                paramname: 'ds'
			},
            period: {
                value: 'period',
                rawvalue: DV.i18n.period,
                warning: {
					filter: DV.i18n.wm_multiple_filter_period
				}
            },
            organisationunit: {
                value: 'organisationunit',
                rawvalue: DV.i18n.organisation_unit,
                paramname: 'ou',
                warning: {
					filter: DV.i18n.wm_multiple_filter_orgunit
				}
            },
            organisationunitgroup: {
				value: 'organisationunitgroup'
			}
        },        
        chart: {
            series: 'series',
            category: 'category',
            filter: 'filter',
            column: 'column',
            stackedcolumn: 'stackedcolumn',
            bar: 'bar',
            stackedbar: 'stackedbar',
            line: 'line',
            area: 'area',
            pie: 'pie'
        },
        data: {
			domain: 'domain_',
			targetline: 'targetline_',
			baseline: 'baseline_',
			trendline: 'trendline_'
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
    },
    period: {
		relativeperiodunits: {
			lastSixMonth: 1,
			thisYear: 1,
			lastYear: 1,
			last5Years: 5,
			last12Months: 12,
			last4Quarters: 4,
			last2SixMonths: 2,
			reportingMonth: 1,
			reportingQuarter: 1
		},
		periodtypes: [
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
	},
    chart: {
        style: {
            inset: 30,
            font: 'arial,sans-serif,ubuntu,consolas'
        },
        theme: {
            dv1: ['#94ae0a', '#0b3b68', '#a61120', '#ff8809', '#7c7474', '#a61187', '#ffd13e', '#24ad9a', '#a66111', '#414141', '#4500c4', '#1d5700']
        }
    },
    statusbar: {
		icon: {
			error: 'error_s.png',
			warning: 'warning.png',
			ok: 'ok.png'
		}
	},
    layout: {
        west_width: 424,
        west_fieldset_width: 402,
        west_width_subtractor: 18,
        west_fill: 117,
        west_fill_accordion_indicator: 77,
        west_fill_accordion_dataelement: 77,
        west_fill_accordion_dataset: 45,
        west_fill_accordion_fixedperiod: 77,
        west_fill_accordion_organisationunit: 103,
        west_maxheight_accordion_indicator: 478,
        west_maxheight_accordion_dataelement: 478,
        west_maxheight_accordion_dataset: 478,
        west_maxheight_accordion_relativeperiod: 396,
        west_maxheight_accordion_fixedperiod: 478,
        west_maxheight_accordion_organisationunit: 756,
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
    },
    util: {
		jsonEncodeString: function(str) {
			return typeof str === 'string' ? str.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'') : str;
		},
		jsonEncodeArray: function(a) {
			for (var i = 0; i < a.length; i++) {
				a[i] = DV.conf.util.jsonEncodeString(a[i]);
			}
			return a;
		}
	}
};

DV.cmp = {
	region: {},
	charttype: [],
	settings: {},
	dimension: {
		indicator: {},
		dataelement: {},
		dataset: {},
		relativeperiod: {
			checkbox: []
		},
		fixedperiod: {},
		organisationunit: {},
		organisationunitgroup: {}
	},
	options: {},
	toolbar: {
		menuitem: {}
	},
	statusbar: {},
	favorite: {
		rename: {}
	},
	share: {}
};

Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', DV.conf.finals.ajax.path_lib + 'ext-ux');
Ext.require('Ext.ux.form.MultiSelect');

Ext.onReady( function() {
    Ext.QuickTips.init();
    document.body.oncontextmenu = function(){return false;}; 
    
    Ext.Ajax.request({
        url: DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.initialize,
        success: function(r) {
            
    DV.init = DV.conf.init.ajax.jsonfy(r);
    DV.init.initialize = function() {
		DV.cmp.dimension.indicator.panel.expand();
		
		DV.c = DV.chart.model;
        DV.util.combobox.filter.category();
        
        DV.init.cmd = DV.util.getUrlParam(DV.conf.finals.cmd.urlparam) || DV.conf.finals.cmd.init;
		
        DV.exe.execute(DV.init.cmd);
    };
    
    DV.util = {
        getCmp: function(q) {
            return DV.viewport.query(q)[0];
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
        viewport: {
            getSize: function() {
                return {x: DV.cmp.region.center.getWidth(), y: DV.cmp.region.center.getHeight()};
            },
            getXY: function() {
                return {x: DV.cmp.region.center.x + 15, y: DV.cmp.region.center.y + 43};
            },
            getPageCenterX: function(cmp) {
                return ((screen.width/2)-(cmp.width/2));
            },
            getPageCenterY: function(cmp) {
                return ((screen.height/2)-((cmp.height/2)-100));
            },
            resizeDimensions: function() {
				var a = [DV.cmp.dimension.indicator.panel, DV.cmp.dimension.dataelement.panel, DV.cmp.dimension.dataset.panel,
						DV.cmp.dimension.relativeperiod.panel, DV.cmp.dimension.fixedperiod.panel, DV.cmp.dimension.organisationunit.panel,
						DV.cmp.dimension.organisationunitgroup.panel, DV.cmp.options.panel];
				for (var i = 0; i < a.length; i++) {
					if (!a[i].collapsed) {
						a[i].fireEvent('expand');
					}
				}
			}
        },
        multiselect: {
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
            selectAll: function(a, s) {
                var array = [];
                a.store.each( function(r) {
                    array.push({id: r.data.id, name: r.data.name});
                });
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
                    var filter = true;
                    s.store.each( function(r2) {
                        if (r.data.id === r2.data.id) {
                            filter = false;
                        }
                    });
                    return filter;
                });
                a.store.sortStore();
            },
            setHeight: function(ms, panel, fill) {
				for (var i = 0; i < ms.length; i++) {
					ms[i].setHeight(panel.getHeight() - fill);
				}
			}
        },
        treepanel: {
			getHeight: function() {
				var h1 = DV.cmp.region.west.getHeight();
				var h2 = DV.cmp.options.panel.getHeight();
				var h = h1 - h2 - DV.conf.layout.treepanel_fill_default;
				var mx = DV.conf.layout.treepanel_maxheight;
				var mn = DV.conf.layout.treepanel_minheight;
				return h > mx ? mx : h < mn ? mn : h;
			}
		},
        button: {
            type: {
                getValue: function() {
                    for (var i = 0; i < DV.cmp.charttype.length; i++) {
                        if (DV.cmp.charttype[i].pressed) {
                            return DV.cmp.charttype[i].name;
                        }
                    }
                },
                setValue: function(type) {
                    for (var i = 0; i < DV.cmp.charttype.length; i++) {
                        DV.cmp.charttype[i].toggle(DV.cmp.charttype[i].name === type);
                    }
                },
                toggleHandler: function(b) {
                    if (!b.pressed) {
                        b.toggle();
                    }
                }
            }
        },
        store: {
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
        },
        dimension: {
            indicator: {
                getRecords: function() {
                    var a = [];
                    DV.cmp.dimension.indicator.selected.store.each( function(r) {
                        a.push({id: r.data.id, name: r.data.name});
                    });
                    return a;
                },
                getIds: function() {
					var obj = DV.c.indicator.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				}
            },
            dataelement: {
                getRecords: function() {
					var a = [];
					DV.cmp.dimension.dataelement.selected.store.each( function(r) {
						a.push({id: r.data.id, name: r.data.name});
					});
					return a;
                },
                getIds: function() {
					var obj = DV.c.dataelement.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				}
            },
            dataset: {
                getRecords: function() {
					var a = [];
					DV.cmp.dimension.dataset.selected.store.each( function(r) {
						a.push({id: r.data.id, name: r.data.name});
					});
					return a;
                },
                getIds: function() {
					var obj = DV.c.dataset.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				}
            },
            data: {
				getRecords: function() {
					var records = DV.c.indicator.records;
					records = records.concat(DV.c.dataelement.records);
					records = records.concat(DV.c.dataset.records);
					return records;
				},
                getUrl: function(isFilter) {
					var obj = DV.c.indicator.records,
						a = [];
                    for (var i = 0; i < obj.length; i++) {
						a.push(DV.conf.finals.dimension.indicator.paramname + '=' + obj[i].id);
					}
					obj = DV.c.dataelement.records;
                    for (var i = 0; i < obj.length; i++) {
						a.push(DV.conf.finals.dimension.dataelement.paramname + '=' + obj[i].id);
					}
					obj = DV.c.dataset.records;
                    for (var i = 0; i < obj.length; i++) {
						a.push(DV.conf.finals.dimension.dataset.paramname + '=' + obj[i].id);
					}
					return (isFilter && a.length > 1) ? a.slice(0,1) : a;
				}
            },
            relativeperiod: {
                getRecordsByRelativePeriods: function(obj) {
					var a = [],
						count = 0;
                    for (var rp in obj) {
                        if (obj[rp]) {
							count += DV.conf.period.relativeperiodunits[rp];
                        }
                    }
                    for (var i = 0; i < count; i++) {
						a.push({});
					}
					return a;
				},
                getIds: function() {
					var obj = DV.c.relativeperiod.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				},
                getRelativePeriodObject: function() {
                    var a = {},
                        cmp = DV.cmp.dimension.relativeperiod.checkbox;
                    Ext.Array.each(cmp, function(item) {
                        a[item.paramName] = item.getValue();
                    });
                    return a;
                },
                relativePeriodObjectIsValid: function(obj) {
					for (var rp in obj) {
						if (obj[rp]) {
							return true;
						}
					}
					return false;
				}
            },
            fixedperiod: {
                getRecords: function() {
                    var a = [];
                    DV.cmp.dimension.fixedperiod.selected.store.each( function(r) {
                        a.push({id: r.data.id, name: r.data.name});
                    });
                    return a;
                },
                getIds: function() {
					var obj = DV.c.fixedperiod.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				}
			},
			period: {
				getRecords: function() {
					var a = DV.util.dimension.relativeperiod.getRecordsByRelativePeriods(DV.c.relativeperiod.rp);
					return a.concat(DV.c.fixedperiod.records);
				},
                getUrl: function() {
					var a = [],
						obj = DV.c.relativeperiod.rp;
					for (var rp in obj) {
						if (obj[rp]) {
							a.push(rp + '=true');
						}
					}
					
					var array = DV.c.fixedperiod.records;
					for (var i = 0; i < array.length; i++) {
						a.push('p=' + array[i].id);
					}
										
					return a;
				}
			},
            organisationunit: {
                getRecords: function() {
                    var a = [],
					tp = DV.cmp.dimension.organisationunit.treepanel,
	                selection = tp.getSelectionModel().getSelection();
					if (!selection.length) {
						var root = tp.selectRootIf();
						selection = [root];
					}
					Ext.Array.each(selection, function(r) {
						a.push({id: r.data.id, name: r.data.text});
					});
					return a;
                },
                getUrl: function(isFilter) {
					var ou = DV.c.organisationunit,
						a = [];
					for (var i = 0; i < ou.records.length; i++) {
						a.push(DV.conf.finals.dimension.organisationunit.paramname + '=' + ou.records[i].id);
					}
					if (isFilter && a.length > 1) {
						a = a.slice(0,1);
					}
					return a;
                },
                getIds: function() {
					var obj = DV.c.organisationunit.records,
						a = [];
					for (var i = 0; i < obj.length; i++) {
						a.push(obj[i].id);
					}
					return a;
				},
                getGroupSetId: function() {
					var value = DV.cmp.dimension.organisationunitgroup.panel.groupsets.getValue();
					return !value || value === DV.i18n.none || value === DV.conf.finals.cmd.none ? null : value;
				}
            },
            panel: {
				setHeight: function(mx) {
					var h = DV.cmp.region.west.getHeight() - DV.conf.layout.west_fill;
					DV.cmp.dimension.panel.setHeight(h > mx ? mx : h);
				}
			}
        },
        notification: {
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
				DV.cmp.statusbar.panel.setWidth(DV.cmp.region.center.getWidth());
				DV.cmp.statusbar.panel.update('<img src="' + DV.conf.finals.ajax.path_images + DV.conf.statusbar.icon.error + '" style="padding:0 5px 0 0"/>' + text);
			},
			warning: function(text) {
				text = text || '';
				DV.cmp.statusbar.panel.setWidth(DV.cmp.region.center.getWidth());
				DV.cmp.statusbar.panel.update('<img src="' + DV.conf.finals.ajax.path_images + DV.conf.statusbar.icon.warning + '" style="padding:0 5px 0 0"/>' + text);
			},
			ok: function() {
				DV.cmp.statusbar.panel.setWidth(DV.cmp.region.center.getWidth());
				DV.cmp.statusbar.panel.update('<img src="' + DV.conf.finals.ajax.path_images + DV.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>&nbsp;&nbsp;');
			},
			interpretation: function(text) {
				DV.cmp.statusbar.panel.setWidth(DV.cmp.region.center.getWidth());
				DV.cmp.statusbar.panel.update('<img src="' + DV.conf.finals.ajax.path_images + DV.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>' + text);
			}				
		},
        mask: {
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
        },
        chart: {
			def: {
				getChart: function(axes, series) {
					return Ext.create('Ext.chart.Chart', {
						animate: true,
						shadow: false,
						store: DV.store.chart,
						insetPadding: DV.conf.chart.style.inset,
						items: DV.c.hidesubtitle ? false : DV.util.chart.def.getTitle(),
						legend: DV.c.hidelegend ? false : DV.util.chart.def.getLegend(),
						axes: axes,
						series: series,
						theme: 'dv1'
					});
				},
				getLegend: function(len) {
					len = len ? len : DV.store.chart.range.length;
					return {
						position: len > 5 ? 'right' : 'top',
						labelFont: '15px ' + DV.conf.chart.style.font,
						boxStroke: '#ffffff',
						boxStrokeWidth: 0,
						padding: 0
					};
				},
				getTitle: function() {
					return {
						type: 'text',
						text: DV.c.currentFavorite ? DV.c.currentFavorite.name + ' (' + DV.c.filter.names[0] + ')' : DV.c.filter.names[0],
						font: 'bold 15px ' + DV.conf.chart.style.font,
						fill: '#222',
						width: 300,
						height: 20,
						x: 28,
						y: 16
					};
				},
				label: {
					getCategory: function() {
						return {
							font: '14px ' + DV.conf.chart.style.font,
							rotate: {
								degrees: 330
							}
						};
					},
					getNumeric: function() {
						return {
							font: '13px ' + DV.conf.chart.style.font,
							renderer: Ext.util.Format.numberRenderer(DV.util.number.getChartAxisFormatRenderer())
						};
					}
				},
				axis: {
					getNumeric: function(stacked) {
						var axis = {
							type: 'Numeric',
							position: 'left',
							title: DV.c.rangeaxislabel || false,
							labelTitle: {
								font: '17px ' + DV.conf.chart.style.font
							},
							minimum: 0,
							fields: stacked ? DV.c.series.names : DV.store.chart.range,
							label: DV.util.chart.def.label.getNumeric(),
							grid: {
								odd: {
									opacity: 1,
									fill: '#fefefe',
									stroke: '#aaa',
									'stroke-width': 0.1
								},									
								even: {
									opacity: 1,
									fill: '#f1f1f1',
									stroke: '#aaa',
									'stroke-width': 0.1
								}
							}
						};
						if (DV.init.cmd === DV.conf.finals.cmd.init) {
							axis.maximum = 100;
							axis.majorTickSteps = 10;
						}
						return axis;
					},
					getCategory: function() {
						return {
							type: 'Category',
							position: 'bottom',
							title: DV.c.domainaxislabel || false,
							labelTitle: {
								font: '17px ' + DV.conf.chart.style.font
							},
							fields: DV.conf.finals.data.domain,
							label: DV.util.chart.def.label.getCategory()
						};
					}
				},
				series: {
					getTips: function() {
						return {
							trackMouse: true,
							cls: 'dv-chart-tips',
							renderer: function(si, item) {
								this.update('' + item.value[1]);
							}
						};
					},
					getTargetLine: function() {
						var title = DV.c.targetlinelabel || DV.i18n.target;
						title += ' (' + DV.c.targetlinevalue + ')';
						return {
							type: 'line',
							axis: 'left',
							xField: DV.conf.finals.data.domain,
							yField: DV.conf.finals.data.targetline,
							style: {
								opacity: 1,
								lineWidth: 3,
								stroke: '#041423'
							},
							markerConfig: {
								type: 'circle',
								radius: 0
							},
							title: title
						};
					},
					getBaseLine: function() {
						var title = DV.c.baselinelabel || DV.i18n.base;
						title += ' (' + DV.c.baselinevalue + ')';
						return {
							type: 'line',
							axis: 'left',
							xField: DV.conf.finals.data.domain,
							yField: DV.conf.finals.data.baseline,
							style: {
								opacity: 1,
								lineWidth: 3,
								stroke: '#041423'
							},
							markerConfig: {
								type: 'circle',
								radius: 0
							},
							title: title
						};
					},
					getTrendLineArray: function() {
						var a = [];
						for (var i = 0; i < DV.chart.trendline.length; i++) {
							a.push({
								type: 'line',
								axis: 'left',
								xField: DV.conf.finals.data.domain,
								yField: DV.chart.trendline[i].key,
								style: {
									opacity: 0.8,
									lineWidth: 3
								},
								markerConfig: {
									type: 'circle',
									radius: 4
								},
								tips: DV.util.chart.def.series.getTips(),
								title: DV.chart.trendline[i].name
							});
						}
						return a;
					},
					setTheme: function() {
						var colors = DV.conf.chart.theme.dv1.slice(0, DV.c.series.names.length);
						if (DV.c.targetlinevalue || DV.c.baselinevalue) {
							colors.push('#051a2e');
						}					
						if (DV.c.targetlinevalue) {
							colors.push('#051a2e');
						}					
						if (DV.c.baselinevalue) {
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
					}
				}
			},
            bar: {
				label: {
					getCategory: function() {
						return {
							font: '14px ' + DV.conf.chart.style.font
						};
					}
				},
				axis: {
					getNumeric: function() {
						var num = DV.util.chart.def.axis.getNumeric();
						num.position = 'bottom';
						return num;
					},
					getCategory: function() {
						var cat = DV.util.chart.def.axis.getCategory();
						cat.position = 'left';
						cat.label = DV.util.chart.bar.label.getCategory();
						return cat;
					}
				},
				series: {
					getTips: function() {
						return {
							trackMouse: true,
							cls: 'dv-chart-tips',
							renderer: function(si, item) {
								this.update('' + item.value[0]);
							}
						};
					},
					getTargetLine: function() {
						var line = DV.util.chart.def.series.getTargetLine();
						line.axis = 'bottom';
						line.xField = DV.conf.finals.data.targetline;
						line.yField = DV.conf.finals.data.domain;
						return line;
					},
					getBaseLine: function() {
						var line = DV.util.chart.def.series.getBaseLine();
						line.axis = 'bottom';
						line.xField = DV.conf.finals.data.baseline;
						line.yField = DV.conf.finals.data.domain;
						return line;
					},
					getTrendLineArray: function() {
						var a = [];
						for (var i = 0; i < DV.chart.trendline.length; i++) {
							a.push({
								type: 'line',
								axis: 'bottom',
								xField: DV.chart.trendline[i].key,
								yField: DV.conf.finals.data.domain,
								style: {
									opacity: 0.8,
									lineWidth: 3
								},
								markerConfig: {
									type: 'circle',
									radius: 4
								},
								tips: DV.util.chart.bar.series.getTips(),
								title: DV.chart.trendline[i].name
							});
						}
						return a;
					}
				}
            },
            line: {
				series: {
					getArray: function() {
						var series = [];
						for (var i = 0; i < DV.c.series.names.length; i++) {
							var main = {
								type: 'line',
								axis: 'left',
								xField: DV.conf.finals.data.domain,
								yField: DV.c.series.names[i],
								style: {
									opacity: 0.8,
									lineWidth: 3
								},
								markerConfig: {
									type: 'circle',
									radius: 4
								},
								tips: DV.util.chart.def.series.getTips()
							};
							series.push(main);
						}
						return series;
					},
					setTheme: function() {
						var colors = DV.conf.chart.theme.dv1.slice(0, DV.c.series.names.length);
						if (DV.c.trendline) {
							colors = colors.concat(colors);
						}
						if (DV.c.targetlinevalue) {
							colors.push('#051a2e');
						}
						if (DV.c.baselinevalue) {
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
					}
				}
            },
            pie: {
                getTitle: function() {
                    return [
                        {
                            type: 'text',
                            text: DV.c.filter.names[0],
                            font: 'bold 15px ' + DV.conf.chart.style.font,
                            fill: '#222',
                            width: 300,
                            height: 20,
                            x: 28,
                            y: 16
                        },
                        {
                            type: 'text',
                            text: DV.c.series.names[0],
                            font: '13px ' + DV.conf.chart.style.font,
                            fill: '#444',
                            width: 300,
                            height: 20,
                            x: 28,
                            y: 36
                        }
                    ];                        
                },
                series: {
					getTips: function() {
						return {
							trackMouse: true,
							cls: 'dv-chart-tips-pie',
							renderer: function(item) {
								this.update(item.data[DV.conf.finals.data.domain] + '<br/><b>' + item.data[DV.c.series.names[0]] + '</b>');
							}
						};
					},
					setTheme: function() {
						var colors = DV.conf.chart.theme.dv1.slice(0, DV.c.category.names.length);
						Ext.chart.theme.dv1 = Ext.extend(Ext.chart.theme.Base, {
							constructor: function(config) {
								Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
									seriesThemes: colors,
									colors: colors
								}, config));
							}
						});
					}
				}
            }
        },
        combobox: {
            filter: {
                category: function() {
                    var cbs = DV.cmp.settings.series,
                        cbc = DV.cmp.settings.category,
                        cbf = DV.cmp.settings.filter,
                        v = cbs.getValue(),
                        d = DV.conf.finals.dimension.data.value,
                        p = DV.conf.finals.dimension.period.value,
                        o = DV.conf.finals.dimension.organisationunit.value,
                        index = 0;
                        
                    this.clearValue(v, cbc);
                    this.clearValue(v, cbf);
                    
                    cbc.filterArray = [!(v === d), !(v === p), !(v === o)];
                    cbc.store.filterBy( function(r) {
                        return cbc.filterArray[index++];
                    });
                    
                    if (!cbc.getValue() && cbf.getValue()) {
						cbc.setValue(this.getAutoSelectOption(cbs.getValue(), cbf.getValue()));
                    }
                    
                    this.filter();
                },                
                filter: function() {
                    var cbs = DV.cmp.settings.series,
                        cbc = DV.cmp.settings.category,
                        cbf = DV.cmp.settings.filter,
                        v = cbc.getValue(),
                        d = DV.conf.finals.dimension.data.value,
                        p = DV.conf.finals.dimension.period.value,
                        o = DV.conf.finals.dimension.organisationunit.value,
                        index = 0;
                        
                    this.clearValue(v, cbf);
                        
                    cbf.filterArray = Ext.Array.clone(cbc.filterArray);
                    cbf.filterArray[0] = cbf.filterArray[0] ? !(v === d) : false;
                    cbf.filterArray[1] = cbf.filterArray[1] ? !(v === p) : false;
                    cbf.filterArray[2] = cbf.filterArray[2] ? !(v === o) : false;
                    
                    cbf.store.filterBy( function(r) {
                        return cbf.filterArray[index++];
                    });                    
                    
                    if (!cbf.getValue()) {
						cbf.setValue(this.getAutoSelectOption(cbs.getValue(), cbc.getValue()));
                    }
                },
                clearValue: function(v, cb, i, d) {
                    if (v === cb.getValue()) {
                        cb.clearValue();
                    }
                },
                getAutoSelectOption: function(o1, o2) {
					var a = [DV.conf.finals.dimension.data.value, DV.conf.finals.dimension.period.value, DV.conf.finals.dimension.organisationunit.value];
					for (var i = 0; i < a.length; i++) {
						if (a[i] != o1 && a[i] != o2) {
							return a[i];
						}
					}
				}
            }
        },
        checkbox: {
            setRelativePeriods: function(rp) {
                for (var r in rp) {
                    var cmp = DV.util.getCmp('checkbox[paramName="' + r + '"]');
                    if (cmp) {
                        cmp.setValue(rp[r]);
                    }
                }
            }
        },
        toolbar: {
			separator: {
				xtype: 'tbseparator',
				height: 26,
				style: 'border-left: 1px solid #d1d1d1; border-right: 1px solid #f1f1f1'
			}
		},
        number: {
            isInteger: function(n) {
                var str = new String(n);
                if (str.indexOf('.') > -1) {
                    var d = str.substr(str.indexOf('.') + 1);
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
        },
        variable: {
			isNotEmpty: function(str) {
				return (str !== 0 && str !== '0' && str !== '');
			}
		},
        value: {
            jsonfy: function(values) {
                var a = [];
                for (var i = 0; i < values.length; i++) {
                    var v = {
						value: DV.conf.util.jsonEncodeString(parseFloat(values[i][0])),
						data: DV.conf.util.jsonEncodeString(values[i][1]),
						period: DV.conf.util.jsonEncodeString(values[i][2]),
						organisationunit: DV.conf.util.jsonEncodeString(values[i][3])
					};
					a.push(v);					
                }
                return a;
            }
        },
        crud: {
            favorite: {
                create: function(fn, isupdate) {
                    DV.util.mask.showMask(DV.cmp.favorite.window, DV.i18n.saving + '...');
                    
                    var p = DV.state.getParams();
                    p.name = DV.cmp.favorite.name.getValue();
                    
                    if (isupdate) {
                        p.uid = DV.store.favorite.getAt(DV.store.favorite.findExact('name', p.name)).data.id;
                    }
					
                    var url = DV.cmp.favorite.system.getValue() ? DV.conf.finals.ajax.favorite_addorupdatesystem : DV.conf.finals.ajax.favorite_addorupdate;
                    Ext.Ajax.request({
                        url: DV.conf.finals.ajax.path_visualizer + url,
                        method: 'POST',
                        params: p,
                        success: function(r) {
                            DV.store.favorite.load({callback: function() {								
								var id = r.responseText;
								var name = DV.store.favorite.getAt(DV.store.favorite.findExact('id', id)).data.name;
								DV.c.currentFavorite = {
									id: id,
									name: name
								};								
								DV.cmp.toolbar.share.xable();
                                DV.util.mask.hideMask();
                                if (fn) {
                                    fn();
                                }
                            }});
                        }
                    });
                },
                update: function(fn) {
                    DV.util.crud.favorite.create(fn, true);
                },
                updateName: function(name) {
                    if (DV.store.favorite.findExact('name', name) != -1) {
                        return;
                    }
                    DV.util.mask.showMask(DV.cmp.favorite.window, DV.i18n.renaming + '...');
                    var r = DV.cmp.favorite.grid.getSelectionModel().getSelection()[0];
                    var url = DV.cmp.favorite.system.getValue() ? DV.conf.finals.ajax.favorite_addorupdatesystem : DV.conf.finals.ajax.favorite_addorupdate;
                    Ext.Ajax.request({
                        url: DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.favorite_updatename,
                        method: 'POST',
                        params: {uid: r.data.id, name: name},
                        success: function() {
                            DV.store.favorite.load({callback: function() {
                                DV.cmp.favorite.rename.window.close();
                                DV.util.mask.hideMask();
                                DV.cmp.favorite.grid.getSelectionModel().select(DV.store.favorite.getAt(DV.store.favorite.findExact('name', name)));
                                DV.cmp.favorite.name.setValue(name);
                            }});
                        }
                    });
                },
                del: function(fn) {
                    DV.util.mask.showMask(DV.cmp.favorite.window, DV.i18n.deleting + '...');
                    var baseurl = DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.favorite_delete,
                        selection = DV.cmp.favorite.grid.getSelectionModel().getSelection();
                    Ext.Array.each(selection, function(item) {
                        baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
                    });
                    Ext.Ajax.request({
                        url: baseurl,
                        method: 'POST',
                        success: function() {
                            DV.store.favorite.load({callback: function() {
                                DV.util.mask.hideMask();
                                if (fn) {
                                    fn();
                                }
                            }});
                        }
                    }); 
                }
            }
        }
    };
    
    DV.store = {
        dimension: function() {
            return Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: [
                    {id: DV.conf.finals.dimension.data.value, name: DV.conf.finals.dimension.data.rawvalue},
                    {id: DV.conf.finals.dimension.period.value, name: DV.conf.finals.dimension.period.rawvalue},
                    {id: DV.conf.finals.dimension.organisationunit.value, name: DV.conf.finals.dimension.organisationunit.rawvalue}
                ]
            });
        },
        indicator: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                proxy: {
                    type: 'ajax',
                    url: '',
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
						s.each( function(r) {
							r.data.name = DV.conf.util.jsonEncodeString(r.data.name);
						});
						DV.util.store.addToStorage(s);
                        DV.util.multiselect.filterAvailable(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
                    }
                }
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        },
        dataelement: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                proxy: {
                    type: 'ajax',
                    url: DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.dataelement_get,
                    reader: {
                        type: 'json',
                        root: 'dataElements'
                    }
                },
                storage: {},
                sortStore: function() {
					this.sort('name', 'ASC');
				},
                listeners: {
                    load: function(s) {
						s.each( function(r) {
							r.data.name = DV.conf.util.jsonEncodeString(r.data.name);
						});
						DV.util.store.addToStorage(s);
                        DV.util.multiselect.filterAvailable(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
                    }
                }
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        },
        dataset: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                proxy: {
                    type: 'ajax',
                    url: DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.dataset_get,
                    reader: {
                        type: 'json',
                        root: 'dataSets'
                    }
                },
                storage: {},
                sortStore: function() {
					this.sort('name', 'ASC');
				},
                isloaded: false,
                listeners: {
                    load: function(s) {
						this.isloaded = true;
						s.each( function(r) {
							r.data.name = DV.conf.util.jsonEncodeString(r.data.name);
						});
						DV.util.store.addToStorage(s);
                        DV.util.multiselect.filterAvailable(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
						s.sort('name', 'ASC');
                    }
                }
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        },
        periodtype: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: DV.conf.period.periodtypes
		}),
        fixedperiod: {
            available: Ext.create('Ext.data.Store', {
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
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        },
        groupset: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'index'],
			proxy: {
				type: 'ajax',
				url: DV.conf.finals.ajax.path_commons + DV.conf.finals.ajax.organisationunitgroupset_get,
				reader: {
					type: 'json',
					root: 'organisationUnitGroupSets'
				}
			},
			isloaded: false,
			listeners: {
				load: function() {
					this.isloaded = true;
					this.add({id: DV.conf.finals.cmd.none, name: DV.i18n.none, index: -1});
					this.sort('index', 'ASC');
				}
			}
		}),
        group: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.organisationunitgroup_getall,
				reader: {
					type: 'json',
					root: 'organisationUnitGroups'
				}
			},
			isloaded: false,
			listeners: {
				load: function() {
					this.isloaded = true;
				}
			}
		}),
        chart: null,
        getChartStore: function(exe) {
            var keys = [];
            Ext.Array.each(DV.chart.data, function(item) {
				keys = Ext.Array.merge(keys, Ext.Object.getKeys(item));
            });
            
            this.chart = Ext.create('Ext.data.Store', {
                fields: keys,
                data: DV.chart.data
            });
            
            this.chart.range = keys.slice(0);
            for (var i = 0; i < this.chart.range.length; i++) {
                if (this.chart.range[i] === DV.conf.finals.data.domain) {
                    this.chart.range.splice(i, 1);
                }
            }
            
            if (exe) {
                DV.chart.getChart(true);
            }
            else {
                return this.chart;
            }
        },
        datatable: null,
        getDataTableStore: function(exe) {
            this.datatable = Ext.create('Ext.data.Store', {
                fields: [
					DV.c.dimension.series,
					DV.c.dimension.category,
                    'value'
                ],
                data: DV.value.values
            });
            
            if (exe) {
                DV.datatable.getDataTable(true);
            }
            else {
                return this.datatable;
            }
            
        },
        favorite: Ext.create('Ext.data.Store', {
            fields: ['id', 'name', 'lastUpdated', 'userId'],
            proxy: {
                type: 'ajax',
                url: DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.favorite_getall,
                reader: {
                    type: 'json',
                    root: 'charts'
                }
            },
            isloaded: false,
            sorting: {
                field: 'name',
                direction: 'ASC'
            },
            sortStore: function() {
                this.sort(this.sorting.field, this.sorting.direction);
            },
            filtersystem: function() {
				if (!DV.init.user.isadmin) {
					this.filterBy( function(r) {
						return r.data.userId ? true : false;
					});
				}
			},
            listeners: {
                load: function(s) {
					s.isloaded = !s.isloaded ? true : false;
					
                    s.sortStore();
                    s.each(function(r) {
                        r.data.lastUpdated = r.data.lastUpdated.substr(0,16);
                        r.data.icon = '<img src="' + DV.conf.finals.ajax.path_images + 'favorite.png" />';
                        r.commit();
                    });
                }
            }
        })
    };
    
    DV.state = {
		setChart: function(exe, id) {
			DV.chart.reset();
			
			if (id) {
                Ext.Ajax.request({
                    url: DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.favorite_get + id + '.json?links=false&paging=false',
                    scope: this,
                    success: function(r) {
						if (!this.validation.response(r)) {
							return;
						}
						
						var f = Ext.JSON.decode(r.responseText);
						
						if (!this.validation.favorite(f)) {
							return;
						}
						
                        DV.c.type = f.type.toLowerCase();
                        DV.c.dimension.series = f.series.toLowerCase();
                        DV.c.dimension.category = f.category.toLowerCase();
                        DV.c.dimension.filter = f.filter.toLowerCase();
                        
                        DV.c.indicator.records = [];
                        DV.c.dataelement.records = [];
                        DV.c.dataset.records = [];
                        DV.c.relativeperiod.rp = {};
                        DV.c.fixedperiod.records = [];
                        DV.c.organisationunit.records = [];
                        
                        if (f.indicators) {
							for (var i = 0; i < f.indicators.length; i++) {
								DV.c.indicator.records.push({id: f.indicators[i].id, name: DV.conf.util.jsonEncodeString(f.indicators[i].name)});
							}
						}
						
						if (f.dataElements) {
							for (var i = 0; i < f.dataElements.length; i++) {
								DV.c.dataelement.records.push({id: f.dataElements[i].id, name: DV.conf.util.jsonEncodeString(f.dataElements[i].name)});
							}
						}
						
						if (f.dataSets) {
							for (var i = 0; i < f.dataSets.length; i++) {
								DV.c.dataset.records.push({id: f.dataSets[i].id, name: DV.conf.util.jsonEncodeString(f.dataSets[i].name)});
							}
						}
						
						DV.c.relativeperiod.rp = f.relativePeriods;
						
						if (f.periods) {
							for (var i = 0; i < f.periods.length; i++) {
								//DV.c.fixedperiod.records.push({id: f.periods[i].id, name: DV.conf.util.jsonEncodeString(f.periods[i].name)});
								DV.c.fixedperiod.records.push({id: f.periods[i], name: f.periods[i]});
							}
						}							
						
						for (var i = 0; i < f.organisationUnits.length; i++) {
							DV.c.organisationunit.records.push({id: f.organisationUnits[i].id, name: DV.conf.util.jsonEncodeString(f.organisationUnits[i].name)});
						}
						DV.c.organisationunit.groupsetid = f.organisationUnitGroupSet ? f.organisationUnitGroupSet.id : null;
						
                        DV.c.hidesubtitle = f.hideSubtitle;
                        DV.c.hidelegend = f.hideLegend;
                        DV.c.trendline = f.regression;
                        DV.c.userorganisationunit = f.userOrganisationUnit;
                        DV.c.userorganisationunitchildren = f.userOrganisationUnitChildren;
                        DV.c.showdata = f.showData;
                        DV.c.domainaxislabel = f.domainAxisLabel;
                        DV.c.rangeaxislabel = f.rangeAxisLabel;
                        DV.c.targetlinevalue = f.targetLineValue ? parseFloat(f.targetLineValue) : null;
                        DV.c.targetlinelabel = f.targetLineLabel ? f.targetLineLabel : null;
                        DV.c.baselinevalue = f.baseLineValue ? parseFloat(f.baseLineValue) : null;
                        DV.c.baselinelabel = f.baseLineLabel ? f.baseLineLabel : null;
                        
                        if (exe) {
							this.extendChart(exe, id);
						}
					}
				});
			}
			else {
				DV.c.type = DV.util.button.type.getValue();
				DV.c.dimension.series = DV.cmp.settings.series.getValue();
				DV.c.dimension.category = DV.cmp.settings.category.getValue();
				DV.c.dimension.filter = DV.cmp.settings.filter.getValue();
				DV.c.indicator.records = DV.util.dimension.indicator.getRecords();
				DV.c.dataelement.records = DV.util.dimension.dataelement.getRecords();
				DV.c.dataset.records = DV.util.dimension.dataset.getRecords();
				DV.c.relativeperiod.rp = DV.util.dimension.relativeperiod.getRelativePeriodObject();
				DV.c.fixedperiod.records = DV.util.dimension.fixedperiod.getRecords();
				DV.c.organisationunit.records = DV.util.dimension.organisationunit.getRecords();
				DV.c.organisationunit.groupsetid = DV.util.dimension.organisationunit.getGroupSetId();
				this.setOptions();
                        
				if (exe) {
					this.extendChart(exe);
				}
			}
		},
		extendChart: function(exe, id) {
			DV.chart.warnings = [];
			
			if (!this.validation.dimensions()) {
				return;
			}
			
			DV.c.data = {};
			DV.c.data.records = DV.util.dimension.data.getRecords();
			DV.c.period.records = DV.util.dimension.period.getRecords();
			
			if (!this.validation.records.selection()) {
				return;
			}
			
			this.validation.records[DV.c.dimension.series]();
			this.validation.records[DV.c.dimension.category]();
			this.validation.records[DV.c.dimension.filter](true);
			
			DV.c.series = DV.c[DV.c.dimension.series];
			DV.c.category = DV.c[DV.c.dimension.category];
			DV.c.filter = DV.c[DV.c.dimension.filter];
			
			DV.c.series.dimension = DV.conf.finals.chart.series;
			DV.c.category.dimension = DV.conf.finals.chart.category;
			DV.c.filter.dimension = DV.conf.finals.chart.filter;
			
			DV.c.series.url = DV.util.dimension[DV.c.dimension.series].getUrl();
			DV.c.category.url = DV.util.dimension[DV.c.dimension.category].getUrl();
			DV.c.filter.url = DV.util.dimension[DV.c.dimension.filter].getUrl(true);
			
			DV.c.indicator.ids = DV.util.dimension.indicator.getIds();
			DV.c.dataelement.ids = DV.util.dimension.dataelement.getIds();
			DV.c.dataset.ids = DV.util.dimension.dataset.getIds();
			DV.c.fixedperiod.ids = DV.util.dimension.fixedperiod.getIds();
			DV.c.organisationunit.ids = DV.util.dimension.organisationunit.getIds();
            
            if (id) {
				this.setUI();
			}
			
            if (exe) {
                DV.value.getValues(true);
            }
        },
        setOptions: function() {
            DV.c.hidesubtitle = DV.cmp.favorite.hidesubtitle.getValue();
            DV.c.hidelegend = DV.cmp.favorite.hidelegend.getValue();
            DV.c.trendline = DV.cmp.favorite.trendline.getValue();
            DV.c.userorganisationunit = DV.cmp.favorite.userorganisationunit.getValue();
            DV.c.userorganisationunitchildren = DV.cmp.favorite.userorganisationunitchildren.getValue();
            DV.c.showdata = DV.cmp.favorite.showdata.getValue();
            DV.c.domainaxislabel = DV.cmp.favorite.domainaxislabel.getValue();
            DV.c.rangeaxislabel = DV.cmp.favorite.rangeaxislabel.getValue();
            DV.c.targetlinevalue = parseFloat(DV.cmp.favorite.targetlinevalue.getValue());
            DV.c.targetlinelabel = DV.cmp.favorite.targetlinelabel.getValue();
            DV.c.baselinevalue = parseFloat(DV.cmp.favorite.baselinevalue.getValue());
            DV.c.baselinelabel = DV.cmp.favorite.baselinelabel.getValue();
		},
		getParams: function() {
            var p = {};
            p.type = DV.c.type.toUpperCase();
            p.series = DV.c.dimension.series.toUpperCase();
            p.category = DV.c.dimension.category.toUpperCase();
            p.filter = DV.c.dimension.filter.toUpperCase();
			p.hideSubtitle = DV.c.hidesubtitle;
			p.hideLegend = DV.c.hidelegend;
			p.trendLine = DV.c.trendline;
			p.userOrganisationUnit = DV.c.userorganisationunit;
			p.userOrganisationUnitChildren = DV.c.userorganisationunitchildren;
			p.showData = DV.c.showdata;
			if (DV.c.domainaxislabel) {
				p.domainAxisLabel = DV.c.domainaxislabel;
			}
			if (DV.c.rangeaxislabel) {
				p.rangeAxisLabel = DV.c.rangeaxislabel;
			}
			if (DV.c.targetlinevalue) {
				p.targetLineValue = DV.c.targetlinevalue;
			}
			if (DV.c.targetlinelabel) {
				p.targetLineLabel = DV.c.targetlinelabel;
			}
			if (DV.c.baselinevalue) {
				p.baseLineValue = DV.c.baselinevalue;
			}
			if (DV.c.baselinelabel) {
				p.baseLineLabel = DV.c.baselinelabel;
			}
            p.indicatorIds = DV.c.indicator.ids;
            p.dataElementIds = DV.c.dataelement.ids;
            p.dataSetIds = DV.c.dataset.ids;
            p = Ext.Object.merge(p, DV.c.relativeperiod.rp);
            p.periodIds = DV.c.fixedperiod.ids;
            p.organisationUnitIds = DV.c.organisationunit.ids;
            if (DV.c.organisationunit.groupsetid) {
				p.organisationUnitGroupSetId = DV.c.organisationunit.groupsetid;
			}
            return p;
        },
        setUI: function() {
			DV.util.button.type.setValue(DV.c.type);			
			DV.cmp.favorite.hidesubtitle.setValue(DV.c.hidesubtitle);
			DV.cmp.favorite.hidelegend.setValue(DV.c.hidelegend);
			DV.cmp.favorite.trendline.setValue(DV.c.trendline);
			DV.cmp.favorite.userorganisationunit.setValue(DV.c.userorganisationunit);
			DV.cmp.favorite.userorganisationunitchildren.setValue(DV.c.userorganisationunitchildren);
			DV.cmp.favorite.userorganisationunit.fireEvent('change');
			DV.cmp.favorite.userorganisationunitchildren.fireEvent('change');
			DV.cmp.favorite.showdata.setValue(DV.c.showdata);
			DV.cmp.favorite.domainaxislabel.setValue(DV.c.domainaxislabel);
			DV.cmp.favorite.rangeaxislabel.setValue(DV.c.rangeaxislabel);
			DV.cmp.favorite.targetlinevalue.setValue(DV.c.targetlinevalue);
			DV.cmp.favorite.targetlinelabel.xable();
			DV.cmp.favorite.targetlinelabel.setValue(DV.c.targetlinelabel);
			DV.cmp.favorite.baselinevalue.setValue(DV.c.baselinevalue);
			DV.cmp.favorite.baselinelabel.xable();
			DV.cmp.favorite.baselinelabel.setValue(DV.c.baselinelabel);
			
			DV.cmp.settings.series.setValue(DV.conf.finals.dimension[DV.c.dimension.series].value);
			DV.util.combobox.filter.category();                        
			DV.cmp.settings.category.setValue(DV.conf.finals.dimension[DV.c.dimension.category].value);
			DV.util.combobox.filter.filter();                        
			DV.cmp.settings.filter.setValue(DV.conf.finals.dimension[DV.c.dimension.filter].value);
			
			DV.store.indicator.selected.removeAll();
			if (DV.c.indicator.records) {
				DV.store.indicator.selected.add(DV.c.indicator.records);
				DV.util.store.addToStorage(DV.store.indicator.available, DV.c.indicator.records);
				DV.util.multiselect.filterAvailable(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
			}
			
			DV.store.dataelement.selected.removeAll();
			if (DV.c.dataelement.records) {
				DV.store.dataelement.selected.add(DV.c.dataelement.records);
				DV.util.store.addToStorage(DV.store.dataelement.available, DV.c.dataelement.records);
				DV.util.multiselect.filterAvailable(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
			}
												
			DV.store.dataset.selected.removeAll();
			if (DV.c.dataset.records) {
				DV.store.dataset.selected.add(DV.c.dataset.records);
				DV.util.store.addToStorage(DV.store.dataset.available, DV.c.dataset.records);
				DV.util.multiselect.filterAvailable(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
			}
			
			DV.util.checkbox.setRelativePeriods(DV.c.period.rp);
			
			DV.store.fixedperiod.selected.removeAll();
			if (DV.c.fixedperiod.records) {
				DV.store.fixedperiod.selected.add(DV.c.fixedperiod.records);
				DV.util.multiselect.filterAvailable(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
			}
			
			DV.cmp.dimension.organisationunit.treepanel.selectByIds(DV.c.organisationunit.ids);
			
			if (DV.c.organisationunit.groupsetid) {
				if (DV.store.groupset.isloaded) {
					DV.cmp.dimension.organisationunitgroup.panel.groupsets.setValue(DV.c.organisationunit.groupsetid);
				}
				else {
					DV.store.groupset.load({
						callback: function() {
							DV.cmp.dimension.organisationunitgroup.panel.groupsets.setValue(DV.c.organisationunit.groupsetid);
						}
					});
				}
			}
			else {
				DV.cmp.dimension.organisationunitgroup.panel.groupsets.setValue(DV.store.isloaded ? DV.conf.finals.cmd.none : DV.i18n.none);
			}
		},
		validation: {
			dimensions: function() {
				if (!DV.c.dimension.series || !DV.c.dimension.category || !DV.c.dimension.filter) {
					DV.util.notification.error(DV.i18n.et_invalid_dimension_setup, DV.i18n.em_invalid_dimension_setup);
					return false;
				}
				return true;
			},
			records: {
				selection: function() {
					if (!DV.c.data.records.length) {
						DV.util.notification.error(DV.i18n.et_no_indicators_dataelements_datasets, DV.i18n.em_no_indicators_dataelements_datasets);
						return false;
					}
					if (!DV.c.period.records.length) {
						DV.util.notification.error(DV.i18n.et_no_periods, DV.i18n.em_no_periods);
						return false;
					}
					if (DV.c.organisationunit.groupsetid) {
						if (DV.init.system.ougs[DV.c.organisationunit.groupsetid]) {
							if (DV.init.system.ougs[DV.c.organisationunit.groupsetid].length < 1) {
								DV.util.notification.error(DV.i18n.et_no_orgunitgroups, DV.i18n.em_no_orgunitgroups);
								return false;
							}
						}
					}
					else if (!DV.c.organisationunit.records.length) {
						DV.util.notification.error(DV.i18n.et_no_orgunits, DV.i18n.em_no_orgunits);
						return false;
					}						
					return true;
				},
				data: function(isFilter) {
					if (isFilter && DV.c.data.records.length > 1) {
						DV.chart.warnings.push(DV.i18n.wm_multiple_filter_ind_de_ds + ' ' + DV.i18n.wm_first_filter_used);
					}
				},
				period: function(isFilter) {
					if (isFilter && DV.c.period.records.length > 1) {
						DV.chart.warnings.push(DV.i18n.wm_multiple_filter_period + ' ' + DV.i18n.wm_first_filter_used);
					}
				},
				organisationunit: function(isFilter) {
					if (isFilter) {
						if (DV.c.organisationunit.groupsetid) {
							if (DV.init.system.ougs[DV.c.organisationunit.groupsetid]) {
								if (DV.init.system.ougs[DV.c.organisationunit.groupsetid].length > 1) {
									DV.chart.warnings.push(DV.i18n.wm_multiple_filter_groups + ' ' + DV.i18n.wm_first_filter_used);
								}
							}
						}
						else {
							if (DV.c.userorganisationunit || DV.c.userorganisationunitchildren) {
								var i = 0;
								i += DV.c.userorganisationunit ? 1 : 0;
								i += DV.c.userorganisationunitchildren && DV.init.user.ouc ? DV.init.user.ouc.length : 0;
								if (i > 1) {
									DV.chart.warnings.push(DV.i18n.wm_multiple_filter_orgunit + ' ' + DV.i18n.wm_first_filter_used);
								}
							}
							else if (DV.c.organisationunit.records.length > 1) {
								DV.chart.warnings.push(DV.i18n.wm_multiple_filter_orgunit + ' ' + DV.i18n.wm_first_filter_used);
							}
						}
					}
				}
			},
			categories: function() {
				if (DV.c.category.names.length < 2 && (DV.c.type === DV.conf.finals.chart.line || DV.c.type === DV.conf.finals.chart.area)) {
					DV.util.notification.error(DV.i18n.et_line_area_categories, DV.i18n.em_line_area_categories);
					DV.util.mask.hideMask();
					return false;
				}
				return true;
			},
			trendline: function() {
				if (DV.c.trendline) {
					var warnings = [];
					if (DV.c.type === DV.conf.finals.chart.stackedcolumn || DV.c.type === DV.conf.finals.chart.stackedbar || DV.c.type === DV.conf.finals.chart.area) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_stacked_chart);
						DV.c.trendline = false;
					}
					else if (DV.c.type === DV.conf.finals.chart.pie) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_pie_chart);
						DV.c.trendline = false;
					}
					
					if (DV.c.category.names.length < 2) {					
						warnings.push(DV.i18n.wm_required_categories);
						DV.c.trendline = false;
					}
					
					if (warnings.length) {
						var text = DV.i18n.wm_trendline_deactivated + ' (';
						for (var i = 0; i < warnings.length; i++) {
							text += i > 0 ? ' + ' : '';
							text += warnings[i];
						}
						text += ').';
						DV.chart.warnings.push(text);
					}
				}
			},
			targetline: function() {
				if (DV.c.targetlinevalue) {
					var warnings = [];
					if (DV.c.type === DV.conf.finals.chart.stackedcolumn || DV.c.type === DV.conf.finals.chart.stackedbar || DV.c.type === DV.conf.finals.chart.area) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_stacked_chart);
						DV.c.targetlinevalue = null;
					}
					else if (DV.c.type === DV.conf.finals.chart.pie) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_pie_chart);
						DV.c.targetlinevalue = null;
					}
					
					if (DV.c.category.names.length < 2) {
						warnings.push(DV.i18n.wm_required_categories);
						DV.c.targetlinevalue = null;
					}
					
					if (warnings.length) {
						var text = DV.i18n.wm_targetline_deactivated + ' (';
						for (var i = 0; i < warnings.length; i++) {
							text += i > 0 ? ' + ' : '';
							text += warnings[i];
						}
						text += ').';
						DV.chart.warnings.push(text);
					}
				}
			},
			baseline: function() {
				if (DV.c.baselinevalue) {
					var warnings = [];
					if (DV.c.type === DV.conf.finals.chart.stackedcolumn || DV.c.type === DV.conf.finals.chart.stackedbar || DV.c.type === DV.conf.finals.chart.area) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_stacked_chart);
						DV.c.baselinevalue = null;
					}
					else if (DV.c.type === DV.conf.finals.chart.pie) {
						warnings.push(DV.i18n.wm_not_applicable + ' ' + DV.i18n.wm_pie_chart);
						DV.c.baselinevalue = null;
					}
					
					if (DV.c.category.names.length < 2) {
						warnings.push(DV.i18n.wm_required_categories);
						DV.c.baselinevalue = null;
					}
					
					if (warnings.length) {
						var text = DV.i18n.wm_baseline_deactivated + ' (';
						for (var i = 0; i < warnings.length; i++) {
							text += i > 0 ? ' + ' : '';
							text += warnings[i];
						}
						text += ').';
						DV.chart.warnings.push(text);
					}
				}
			},
			render: function() {
				if (!DV.c.rendered) {
					DV.cmp.toolbar.datatable.enable();
					DV.cmp.toolbar.datatable.setTooltip('');
					DV.c.rendered = true;
				}
			},
			response: function(r) {
				if (!r.responseText) {
					DV.util.mask.hideMask();
					DV.util.notification.error(DV.i18n.et_invalid_uid, DV.i18n.em_invalid_uid);
					return false;
				}
				return true;
			},
			favorite: function(f) {				
				if (!f.organisationUnits || !f.organisationUnits.length) {
					console.log(DV.i18n.favorite_no_orgunits);
					return false;
				}
				return true;
			},
			value: function(r) {
				if (!r.v) {
					DV.util.mask.hideMask();
					DV.util.notification.error(DV.i18n.et_no_data, DV.i18n.em_no_data);
					return false;
				}
				return true;
			}
		}
	};
    
    DV.value = {
        values: [],
        getValues: function(exe) {
            DV.util.mask.showMask(DV.cmp.region.center, DV.i18n.loading);
            
            var params = [];
            params = params.concat(DV.c.data.url);
            params = params.concat(DV.c.period.url);
            params = params.concat(DV.c.organisationunit.url);
            
            var baseurl = DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.data_get;
            Ext.Array.each(params, function(item) {
                baseurl = Ext.String.urlAppend(baseurl, item);
            });
            
            params = {
				periodIsFilter: (DV.c.dimension.filter === DV.conf.finals.dimension.period.value),
				userOrganisationUnit: DV.c.userorganisationunit,
				userOrganisationUnitChildren: DV.c.userorganisationunitchildren
			};
			if (DV.c.organisationunit.groupsetid) {
				params.organisationUnitGroupSetId = DV.c.organisationunit.groupsetid;
			}
			
            Ext.Ajax.request({
                url: baseurl,
                method: 'GET',
                params: params,
                disableCaching: false,
                success: function(r) {
					r = Ext.JSON.decode(r.responseText);
                    
                    if (!DV.state.validation.value(r)) {
						return;
					}
					
                    DV.value.values = DV.util.value.jsonfy(r.v);
                                        
					DV.c.data.names = DV.conf.util.jsonEncodeArray(r.d);
					DV.c.period.names = DV.conf.util.jsonEncodeArray(r.p);
					DV.c.organisationunit.names = DV.conf.util.jsonEncodeArray(r.o);
								
					if (!DV.state.validation.categories()) {
						return;
					}
            
					DV.state.validation.trendline();
					
					DV.state.validation.targetline();
					
					DV.state.validation.baseline();
            
					DV.state.validation.render();
							
                    if (exe) {
                        DV.chart.getData(true);
                    }
                    else {
                        return DV.value.values;
                    }
                }
            });
        }
    };
    
    DV.chart = {
		model: {
			type: DV.conf.finals.chart.column,
			dimension: {},
			indicator: {},
			dataelement: {},
			dataset: {},
			relativeperiod: {},
			fixedperiod: {},
			organisationunit: {},
			hidesubtitle: false,
			hidelegend: false,
			trendline: false,
			userorganisationunit: false,
			userorganisationunitchildren: false,
			showdata: false,
			domainaxislabel: null,
			rangeaxislabel: null,
			targetlinevalue: null,
			targetlinelabel: null,
			baselinevalue: null,
			baselinelabel: null,
			rendered: false,
			currentFavorite: null
		},
		reset: function() {
			this.model.type = DV.conf.finals.chart.column;
			this.model.dimension = {};
			this.model.series = null;
			this.model.category = null;
			this.model.filter = null;
			this.model.indicator = {};
			this.model.dataelement = {};
			this.model.dataset = {};
			this.model.period = {};
			this.model.organisationunit = {};
			this.model.hidesubtitle = false;
			this.model.hidelegend = false;
			this.model.trendline = false;
			this.model.userorganisationunit = false;
			this.model.userorganisationunitchildren = false;
			this.model.showdata = false;
			this.model.domainaxislabel = null;
			this.model.rangeaxislabel = null;
			this.model.targetlinevalue = null;
			this.model.targetlinelabel = null;
			this.model.baselinevalue = null;
			this.model.baselinelabel = null;
		},
        data: [],
        getData: function(exe) {
            this.data = [];
            Ext.Array.each(DV.c.category.names, function(item) {
                var obj = {};
                obj[DV.conf.finals.data.domain] = item;
                DV.chart.data.push(obj);
            });
            
            Ext.Array.each(DV.chart.data, function(item) {
                for (var i = 0; i < DV.c.series.names.length; i++) {
                    item[DV.c.series.names[i]] = 0;
                }
            });

            Ext.Array.each(DV.chart.data, function(item) {
                for (var i = 0; i < DV.c.series.names.length; i++) {
                    for (var j = 0; j < DV.value.values.length; j++) {
                        if (DV.value.values[j][DV.c.dimension.category] === item[DV.conf.finals.data.domain] && DV.value.values[j][DV.c.dimension.series] === DV.c.series.names[i]) {
							item[DV.c.series.names[i]] = DV.value.values[j].value;
                            break;
                        }
                    }
                }
            });
            
			if (DV.c.trendline) {
				DV.chart.trendline = [];
				for (var i = 0; i < DV.c.series.names.length; i++) {
					var s = DV.c.series.names[i],
						reg = new SimpleRegression();
					for (var j = 0; j < DV.chart.data.length; j++) {
						reg.addData(j, DV.chart.data[j][s]);
					}
					var key = DV.conf.finals.data.trendline + s;
					for (var j = 0; j < DV.chart.data.length; j++) {
						var n = reg.predict(j);
						DV.chart.data[j][key] = parseFloat(reg.predict(j).toFixed(1));
					}
					DV.chart.trendline.push({
						key: key,
						name: DV.i18n.trend + ' (' + s + ')'
					});
				}
			}

			if (DV.c.targetlinevalue) {
				Ext.Array.each(DV.chart.data, function(item) {
					item[DV.conf.finals.data.targetline] = DV.c.targetlinevalue;
				});
			}

			if (DV.c.baselinevalue) {
				Ext.Array.each(DV.chart.data, function(item) {
					item[DV.conf.finals.data.baseline] = DV.c.baselinevalue;
				});
			}
            
            if (exe) {
                DV.store.getChartStore(true);
            }
            else {
                return this.data;
            }
        },
        chart: null,
        getChart: function(exe) {
            this[this.model.type]();
            if (exe) {
                this.reload();
            }
            else {
                return this.chart;
            }
        },
        column: function(stacked) {
			var series = [];
			if (DV.c.trendline) {
				var a = DV.util.chart.def.series.getTrendLineArray();
				for (var i = 0; i < a.length; i++) {
					series.push(a[i]);
				}
			}
			var main = {
				type: 'column',
				axis: 'left',
				xField: DV.conf.finals.data.domain,
				yField: DV.c.series.names,
				stacked: stacked,
				style: {
					opacity: 0.8
				},
				tips: DV.util.chart.def.series.getTips()
			};
			if (DV.c.showdata) {
				main.label = {display: 'outside', field: DV.c.series.names};
			}
			series.push(main);
			if (DV.c.targetlinevalue) {
				series.push(DV.util.chart.def.series.getTargetLine());
			}
			if (DV.c.baselinevalue) {
				series.push(DV.util.chart.def.series.getBaseLine());
			}
			
			var axes = [];
			var numeric = DV.util.chart.def.axis.getNumeric(stacked);
			axes.push(numeric);
			axes.push(DV.util.chart.def.axis.getCategory());
			
			DV.util.chart.def.series.setTheme();
			this.chart = DV.util.chart.def.getChart(axes, series);
        },
        stackedcolumn: function() {
            this.column(true);
        },
        bar: function(stacked) {
			var series = [];
			if (DV.c.trendline) {
				var a = DV.util.chart.bar.series.getTrendLineArray();
				for (var i = 0; i < a.length; i++) {
					series.push(a[i]);
				}
			}
			var main = {
				type: 'bar',
				axis: 'bottom',
				xField: DV.conf.finals.data.domain,
				yField: DV.c.series.names,
				stacked: stacked,
				style: {
					opacity: 0.8
				},
				tips: DV.util.chart.def.series.getTips()
			};
			if (DV.c.showdata) {
				main.label = {display: 'outside', field: DV.c.series.names};
			}
			series.push(main);
			if (DV.c.targetlinevalue) {
				series.push(DV.util.chart.bar.series.getTargetLine());
			}
			if (DV.c.baselinevalue) {
				series.push(DV.util.chart.bar.series.getBaseLine());
			}
			
			var axes = [];
			var numeric = DV.util.chart.bar.axis.getNumeric(stacked);
			axes.push(numeric);
			axes.push(DV.util.chart.bar.axis.getCategory());
			
			DV.util.chart.def.series.setTheme();			
			this.chart = DV.util.chart.def.getChart(axes, series);
        },
        stackedbar: function() {
            this.bar(true);
        },
        line: function() {
			var series = [];
			if (DV.c.trendline) {
				var a = DV.util.chart.def.series.getTrendLineArray();
				for (var i = 0; i < a.length; i++) {
					series.push(a[i]);
				}
			}
			series = series.concat(DV.util.chart.line.series.getArray());
			if (DV.c.targetlinevalue) {
				series.push(DV.util.chart.def.series.getTargetLine());
			}
			if (DV.c.baselinevalue) {
				series.push(DV.util.chart.def.series.getBaseLine());
			}
			
			var axes = [];
			var numeric = DV.util.chart.def.axis.getNumeric();
			axes.push(numeric);
			axes.push(DV.util.chart.def.axis.getCategory());
			
			DV.util.chart.line.series.setTheme();
			this.chart = DV.util.chart.def.getChart(axes, series);
        },
        area: function() {
			var series = [];
			series.push({
				type: 'area',
				axis: 'left',
				xField: DV.conf.finals.data.domain,
				yField: DV.c.series.names,
				style: {
					opacity: 0.65
				}
			});
			
			var axes = [];
			var numeric = DV.util.chart.def.axis.getNumeric();
			axes.push(numeric);
			axes.push(DV.util.chart.def.axis.getCategory());
			
			DV.util.chart.line.series.setTheme();
			this.chart = DV.util.chart.def.getChart(axes, series);
        },
        pie: function() {
			DV.util.chart.pie.series.setTheme();
            this.chart = Ext.create('Ext.chart.Chart', {
                animate: true,
                shadow: false,
                store: DV.store.chart,
                insetPadding: 60,
                items: DV.c.hidesubtitle ? false : DV.util.chart.pie.getTitle(),
                legend: DV.c.hidelegend ? false : DV.util.chart.def.getLegend(DV.c.category.names.length),
                series: [{
                    type: 'pie',
                    field: DV.c.series.names[0],
                    showInLegend: true,
                    label: {
                        field: DV.conf.finals.data.domain
                    },
                    highlight: {
                        segment: {
                            margin: 8
                        }
                    },
                    style: {
                        opacity: 0.9,
						stroke: '#555'
                    },
                    tips: DV.util.chart.pie.series.getTips()
                }],
                theme: 'dv1'
            });
        },
        warnings: [],
        getWarnings: function() {
			var t = '';
			for (var i = 0; i < this.warnings.length; i++) {
				if (i > 0) {
					t += '<img src="' + DV.conf.finals.ajax.path_images + DV.conf.statusbar.icon.warning + '" style="padding:0 5px 0 8px" />';
				}
				t += this.warnings[i] + ' ';
			}
			return t;
		},
        reload: function() {
            DV.cmp.region.center.removeAll(true);
            DV.cmp.region.center.add(this.chart);
            
			DV.util.mask.hideMask();
            
            if (DV.chart.warnings.length) {
				DV.util.notification.warning(this.getWarnings());
			}
			else {
				DV.util.notification.ok();
			}
            
            if (DV.init.cmd !== DV.conf.finals.cmd.init) {
                DV.store.getDataTableStore(true);
            }
            
            DV.init.cmd = false;
			DV.cmp.toolbar.share.xable();
        }
    };
    
    DV.datatable = {
        datatable: null,
        getDataTable: function(exe) {
            this.datatable = Ext.create('Ext.grid.Panel', {
                height: DV.util.viewport.getSize().y - 1,
                scroll: 'vertical',
                cls: 'dv-datatable',
                columns: [
                    {
                        text: DV.conf.finals.dimension[DV.c.dimension.series].rawvalue,
                        dataIndex: DV.conf.finals.dimension[DV.c.dimension.series].value,
                        width: 150,
                        height: DV.conf.layout.east_gridcolumn_height,
                        sortable: DV.c.dimension.series != DV.conf.finals.dimension.period.value
                    },
                    {
                        text: DV.conf.finals.dimension[DV.c.dimension.category].rawvalue,
                        dataIndex: DV.conf.finals.dimension[DV.c.dimension.category].value,
                        width: 150,
                        height: DV.conf.layout.east_gridcolumn_height,
                        sortable: DV.c.dimension.category != DV.conf.finals.dimension.period.value
                    },
                    {
                        text: DV.i18n.value,
                        dataIndex: 'value',
                        width: 80,
                        height: DV.conf.layout.east_gridcolumn_height
                    }
                ],
                store: DV.store.datatable
            });
            
            if (exe) {
                this.reload();
            }
            else {
                return this.datatable;
            }
        },
        reload: function() {
            DV.cmp.region.east.removeAll(true);
            DV.cmp.region.east.add(this.datatable);
        }            
    };
    
    DV.exe = {
		execute: function(cmd) {
			if (cmd) {
				if (cmd === DV.conf.finals.cmd.init) {
					this.init();
				}
				else {
					this.favorite(cmd);
				}
			}
			else {
				this.update();
			}
		},
		init: function() {
			DV.conf.init.example.setState();
			DV.conf.init.example.setValues();
			DV.chart.getData(true);
		},			
		update: function() {
			DV.state.setChart(true);
		},
		favorite: function(id) {
			DV.state.setChart(true, id);
		},
		datatable: function() {
			DV.store.getDataTableStore(true);
		}
    };
        
    DV.viewport = Ext.create('Ext.container.Viewport', {
        layout: 'border',
        renderTo: Ext.getBody(),
        items: [
            {
                region: 'west',
                preventHeader: true,
                collapsible: true,
                collapseMode: 'mini',
                items: [
                    {
                        xtype: 'toolbar',
                        cls: 'dv-toolbar-2',
                        height: 45,
                        style: 'padding-top:0px; border-style:none',
                        defaults: {
                            height: 40,
                            toggleGroup: 'chartsettings',
                            handler: DV.util.button.type.toggleHandler,
                            listeners: {
                                afterrender: function(b) {
                                    if (b.xtype === 'button') {
                                        DV.cmp.charttype.push(b);
                                    }
                                }
                            }
                        },
                        items: [
                            {
                                xtype: 'label',
                                text: DV.i18n.chart_type,
                                style: 'font-size:11px; font-weight:bold; padding:13px 8px 0 13px'
                            },
                            {
								xtype: 'button',
                                icon: 'images/column.png',
                                name: DV.conf.finals.chart.column,
                                tooltip: DV.i18n.column_chart,
								width: 40,
                                pressed: true
                            },
                            {
								xtype: 'button',
                                icon: 'images/column-stacked.png',
                                name: DV.conf.finals.chart.stackedcolumn,
                                tooltip: DV.i18n.stacked_column_chart,
								width: 40
                            },
                            {
								xtype: 'button',
                                icon: 'images/bar.png',
                                name: DV.conf.finals.chart.bar,
                                tooltip: DV.i18n.bar_chart,
								width: 40
                            },
                            {
								xtype: 'button',
                                icon: 'images/bar-stacked.png',
                                name: DV.conf.finals.chart.stackedbar,
                                tooltip: DV.i18n.stacked_bar_chart,
								width: 40
                            },
                            {
								xtype: 'button',
                                icon: 'images/line.png',
                                name: DV.conf.finals.chart.line,
                                tooltip: DV.i18n.line_chart,
								width: 40
                            },
                            {
								xtype: 'button',
                                icon: 'images/area.png',
                                name: DV.conf.finals.chart.area,
                                tooltip: DV.i18n.area_chart,
								width: 40
                            },
                            {
								xtype: 'button',
                                icon: 'images/pie.png',
                                name: DV.conf.finals.chart.pie,
                                tooltip: DV.i18n.pie_chart,
								width: 40
                            }
                        ]
                    },
                    {
                        xtype: 'toolbar',
                        id: 'chartsettings_tb',
                        height: 48,
                        items: [
                            {
                                xtype: 'panel',
                                bodyStyle: 'border-style:none; background-color:transparent; padding:0 2px',
                                items: [
                                    {
                                        xtype: 'label',
                                        text: DV.i18n.series,
                                        style: 'font-size:11px; font-weight:bold; padding:0 3px'
                                    },
                                    { bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
                                    {
                                        xtype: 'combobox',
                                        cls: 'dv-combo',
                                        baseBodyCls: 'small',
                                        name: DV.conf.finals.chart.series,
                                        emptyText: DV.i18n.series,
                                        queryMode: 'local',
                                        editable: false,
                                        valueField: 'id',
                                        displayField: 'name',
                                        width: (DV.conf.layout.west_fieldset_width / 3) - 4,
                                        store: DV.store.dimension(),
                                        value: DV.conf.finals.dimension.data.value,
                                        listeners: {
                                            added: function() {
                                                DV.cmp.settings.series = this;
                                            },
                                            select: function() {
                                                DV.util.combobox.filter.category();
                                            }
                                        }
                                    }
                                ]
                            },                            
                            {
                                xtype: 'panel',
                                bodyStyle: 'border-style:none; background-color:transparent; padding:0 2px',
                                items: [
                                    {
                                        xtype: 'label',
                                        text: DV.i18n.category,
                                        style: 'font-size:11px; font-weight:bold; padding:0 3px'
                                    },
                                    { bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
                                    {
                                        xtype: 'combobox',
                                        cls: 'dv-combo',
                                        baseBodyCls: 'small',
                                        name: DV.conf.finals.chart.category,
                                        emptyText: DV.i18n.category,
                                        queryMode: 'local',
                                        editable: false,
                                        lastQuery: '',
                                        valueField: 'id',
                                        displayField: 'name',
                                        width: (DV.conf.layout.west_fieldset_width / 3) - 4,
                                        store: DV.store.dimension(),
                                        value: DV.conf.finals.dimension.period.value,
                                        listeners: {
                                            added: function(cb) {
                                                DV.cmp.settings.category = this;
                                            },
                                            select: function(cb) {
                                                DV.util.combobox.filter.filter();
                                            }
                                        }
                                    }
                                ]
                            },                            
                            {
                                xtype: 'panel',
                                bodyStyle: 'border-style:none; background-color:transparent; padding:0 2px',
                                items: [
                                    {
                                        xtype: 'label',
                                        text: 'Filter',
                                        style: 'font-size:11px; font-weight:bold; padding:0 3px'
                                    },
                                    { bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
                                    {
                                        xtype: 'combobox',
                                        cls: 'dv-combo',
                                        baseBodyCls: 'small',
                                        name: DV.conf.finals.chart.filter,
                                        emptyText: DV.i18n.filter,
                                        queryMode: 'local',
                                        editable: false,
                                        lastQuery: '',
                                        valueField: 'id',
                                        displayField: 'name',
                                        width: (DV.conf.layout.west_fieldset_width / 3) - 4,
                                        store: DV.store.dimension(),
                                        value: DV.conf.finals.dimension.organisationunit.value,
                                        listeners: {
                                            added: function(cb) {
                                                DV.cmp.settings.filter = this;
                                            }
                                        }
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        xtype: 'panel',
                        bodyStyle: 'border-style:none; border-top:2px groove #eee; padding:10px 10px 0 10px;',
                        layout: 'fit',
                        items: [
							{
								xtype: 'panel',
								layout: 'accordion',
								activeOnTop: true,
								cls: 'dv-accordion',
								bodyStyle: 'border:0 none',
								height: 430,
								items: [
									{
										title: '<div style="height:17px; background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.indicators + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'combobox',
												cls: 'dv-combo',
												style: 'margin-bottom:8px',
												width: DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: DV.i18n.select_group,
												labelStyle: 'padding-left:7px;',
												labelWidth: 90,
												editable: false,
												queryMode: 'remote',
												store: Ext.create('Ext.data.Store', {
													fields: ['id', 'name', 'index'],
													proxy: {
														type: 'ajax',
														url: DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.indicatorgroup_get,
														reader: {
															type: 'json',
															root: 'indicatorGroups'
														}
													},
													listeners: {
														load: function(s) {
															s.add({id: 0, name: DV.i18n.all_indicator_groups, index: -1});
															s.sort([																
																{ property: 'index', direction: 'ASC' },
																{ property: 'name', direction: 'ASC' }
															]);
														}
													}
												}),
												listeners: {
													select: function(cb) {
														var store = DV.store.indicator.available;
														store.parent = cb.getValue();
														
														if (DV.util.store.containsParent(store)) {
															DV.util.store.loadFromStorage(store);
															DV.util.multiselect.filterAvailable(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
														}
														else {
															if (cb.getValue() === 0) {
																store.proxy.url = DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.indicator_getall;
																store.load();
															}
															else {
																store.proxy.url = DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.indicator_get + cb.getValue() + '.json';
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
													{
														xtype: 'multiselect',
														name: 'availableIndicators',
														cls: 'dv-toolbar-multiselect-left',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														valueField: 'id',
														displayField: 'name',
														queryMode: 'remote',
														store: DV.store.indicator.available,
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
																	DV.util.multiselect.select(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.selectAll(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
																}
															},
															' '
														],
														listeners: {
															added: function() {
																DV.cmp.dimension.indicator.available = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.select(this, DV.cmp.dimension.indicator.selected);
																}, this);
															}
														}
													},                                            
													{
														xtype: 'multiselect',
														name: 'selectedIndicators',
														cls: 'dv-toolbar-multiselect-right',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'local',
														store: DV.store.indicator.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselectAll(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected);
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
															added: function() {
																DV.cmp.dimension.indicator.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.indicator.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.indicator.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_indicator);
												DV.util.multiselect.setHeight(
													[DV.cmp.dimension.indicator.available, DV.cmp.dimension.indicator.selected],
													DV.cmp.dimension.indicator.panel,
													DV.conf.layout.west_fill_accordion_indicator
												);
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.data_elements + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'combobox',
												cls: 'dv-combo',
												style: 'margin-bottom:8px',
												width: DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: DV.i18n.select_group,
												labelStyle: 'padding-left:7px;',
												labelWidth: 90,
												editable: false,
												queryMode: 'remote',
												store: Ext.create('Ext.data.Store', {
													fields: ['id', 'name', 'index'],
													proxy: {
														type: 'ajax',
														url: DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.dataelementgroup_get,
														reader: {
															type: 'json',
															root: 'dataElementGroups'
														}
													},
													listeners: {
														load: function(s) {
															s.add({id: 0, name: '[ All data element groups ]', index: -1});
															s.sort([																
																{ property: 'index', direction: 'ASC' },
																{ property: 'name', direction: 'ASC' }
															]);
														}
													}
												}),
												listeners: {
													select: function(cb) {
														var store = DV.store.dataelement.available;
														store.parent = cb.getValue();
														
														if (DV.util.store.containsParent(store)) {
															DV.util.store.loadFromStorage(store);
															DV.util.multiselect.filterAvailable(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
														}
														else {
															if (cb.getValue() === 0) {
																store.proxy.url = DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.dataelement_getall;
																store.load();
															}
															else {
																store.proxy.url = DV.conf.finals.ajax.path_api + DV.conf.finals.ajax.dataelement_get + cb.getValue() + '.json';
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
													Ext.create('Ext.ux.form.MultiSelect', {
														name: 'availableDataElements',
														cls: 'dv-toolbar-multiselect-left',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														queryMode: 'remote',
														store: DV.store.dataelement.available,
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
																	DV.util.multiselect.select(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.selectAll(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
																}
															},
															' '
														],
														listeners: {
															added: function() {
																DV.cmp.dimension.dataelement.available = this;
															},                                                                
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.select(this, DV.cmp.dimension.dataelement.selected);
																}, this);
															}
														}
													}),                                            
													{
														xtype: 'multiselect',
														name: 'selectedDataElements',
														cls: 'dv-toolbar-multiselect-right',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'remote',
														store: DV.store.dataelement.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselectAll(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected);
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
															added: function() {
																DV.cmp.dimension.dataelement.selected = this;
															},          
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.dataelement.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.dataelement.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_dataelement);
												DV.util.multiselect.setHeight(
													[DV.cmp.dimension.dataelement.available, DV.cmp.dimension.dataelement.selected],
													DV.cmp.dimension.dataelement.panel,
													DV.conf.layout.west_fill_accordion_dataelement
												);
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.reporting_rates + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													Ext.create('Ext.ux.form.MultiSelect', {
														name: 'availableDataSets',
														cls: 'dv-toolbar-multiselect-left',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														queryMode: 'remote',
														store: DV.store.dataset.available,
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
																	DV.util.multiselect.select(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.selectAll(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
																}
															},
															' '
														],
														listeners: {
															added: function() {
																DV.cmp.dimension.dataset.available = this;
															},                                                                
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.select(this, DV.cmp.dimension.dataset.selected);
																}, this);
															}
														}
													}),                                            
													{
														xtype: 'multiselect',
														name: 'selectedDataSets',
														cls: 'dv-toolbar-multiselect-right',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'remote',
														store: DV.store.dataset.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselectAll(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected);
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
															added: function() {
																DV.cmp.dimension.dataset.selected = this;
															},          
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.dataset.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.dataset.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_dataset);
												DV.util.multiselect.setHeight(
													[DV.cmp.dimension.dataset.available, DV.cmp.dimension.dataset.selected],
													DV.cmp.dimension.dataset.panel,
													DV.conf.layout.west_fill_accordion_dataset
												);
												
												if (!DV.store.dataset.available.isloaded) {
													DV.store.dataset.available.load();
												}
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.relative_periods + '</div>',
										hideCollapseTool: true,
										autoScroll: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 10px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		DV.cmp.dimension.relativeperiod.checkbox.push(chb);
																	}
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
																paramName: 'reportingMonth',
																boxLabel: DV.i18n.last_month
															},
															{
																xtype: 'checkbox',
																paramName: 'last12Months',
																boxLabel: DV.i18n.last_12_months,
																checked: true
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 32px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		DV.cmp.dimension.relativeperiod.checkbox.push(chb);
																	}
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
																paramName: 'reportingQuarter',
																boxLabel: DV.i18n.last_quarter
															},
															{
																xtype: 'checkbox',
																paramName: 'last4Quarters',
																boxLabel: DV.i18n.last_4_quarters
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 32px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		DV.cmp.dimension.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: DV.i18n.six_months,
																cls: 'dv-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'lastSixMonth',
																boxLabel: DV.i18n.last_six_month
															},
															{
																xtype: 'checkbox',
																paramName: 'last2SixMonths',
																boxLabel: DV.i18n.last_two_six_month
															}
														]
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:5px 0 0 10px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		DV.cmp.dimension.relativeperiod.checkbox.push(chb);
																	}
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
																paramName: 'thisYear',
																boxLabel: DV.i18n.this_year
															},
															{
																xtype: 'checkbox',
																paramName: 'lastYear',
																boxLabel: DV.i18n.last_year
															},
															{
																xtype: 'checkbox',
																paramName: 'last5Years',
																boxLabel: DV.i18n.last_5_years
															}
														]
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.relativeperiod.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_relativeperiod);
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.fixed_periods + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'combobox',
												cls: 'dv-combo',
												style: 'margin-bottom:8px',
												width: DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: DV.i18n.select_type,
												labelStyle: 'padding-left:7px;',
												labelWidth: 90,
												editable: false,
												queryMode: 'remote',
												store: DV.store.periodtype,
												listeners: {
													select: function(cb) {														
														var pt = new PeriodType();
														var periods = pt.reverse( pt.filterFuturePeriods( pt.get(cb.getValue()).generatePeriods(0) ) );
														DV.store.fixedperiod.available.setIndex(periods);
														DV.store.fixedperiod.available.loadData(periods);
														DV.util.multiselect.filterAvailable(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
													}
												}
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'multiselect',
														name: 'availableFixedPeriods',
														cls: 'dv-toolbar-multiselect-left',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														valueField: 'id',
														displayField: 'name',
														store: DV.store.fixedperiod.available,
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
																	DV.util.multiselect.select(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.selectAll(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
																}
															},
															' '
														],
														listeners: {
															added: function() {
																DV.cmp.dimension.fixedperiod.available = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.select(this, DV.cmp.dimension.fixedperiod.selected);
																}, this);
															}
														}
													},                                            
													{
														xtype: 'multiselect',
														name: 'selectedFixedPeriods',
														cls: 'dv-toolbar-multiselect-right',
														width: (DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor) / 2,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'local',
														store: DV.store.fixedperiod.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselectAll(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected);
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
															added: function() {
																DV.cmp.dimension.fixedperiod.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	DV.util.multiselect.unselect(DV.cmp.dimension.fixedperiod.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.fixedperiod.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_fixedperiod);
												DV.util.multiselect.setHeight(
													[DV.cmp.dimension.fixedperiod.available, DV.cmp.dimension.fixedperiod.selected],
													DV.cmp.dimension.fixedperiod.panel,
													DV.conf.layout.west_fill_accordion_fixedperiod
												);
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/organisationunit.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.organisation_units + '</div>',
										hideCollapseTool: true,
										collapsed: false,
										items: [
											{
												layout: 'column',
												bodyStyle: 'border:0 none; padding-bottom:4px',
												items: [
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.user_orgunit,
														labelWidth: DV.conf.layout.form_label_width,
														handler: function(chb, checked) {
															DV.cmp.dimension.organisationunit.toolbar.xable(checked, DV.cmp.favorite.userorganisationunitchildren.getValue());
															DV.cmp.dimension.organisationunit.treepanel.xable(checked, DV.cmp.favorite.userorganisationunitchildren.getValue());
														},
														listeners: {
															added: function() {
																DV.cmp.favorite.userorganisationunit = this;
															}
														}
													},
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.user_orgunit_children,
														labelWidth: DV.conf.layout.form_label_width,
														handler: function(chb, checked) {
															DV.cmp.dimension.organisationunit.toolbar.xable(checked, DV.cmp.favorite.userorganisationunit.getValue());
															DV.cmp.dimension.organisationunit.treepanel.xable(checked, DV.cmp.favorite.userorganisationunit.getValue());
														},
														listeners: {
															added: function() {
																DV.cmp.favorite.userorganisationunitchildren = this;
															}
														}
													}
												]
											},
											{
												id: 'organisationunit_t',
												xtype: 'toolbar',
												cls: 'dv-toolbar-tbar',
												style: 'margin-bottom: 5px',
												width: DV.conf.layout.west_fieldset_width - 18,
												xable: function(checked, value) {
													if (checked || value) {
														this.disable();
													}
													else {
														this.enable();
													}
												},
												defaults: {
													height: 28
												},
												items: [
													{
														xtype: 'label',
														text: 'Auto-select organisation units by',
														style: 'padding-left:8px; color:#666; line-height:28px'
													},
													'->',
													{
														text: 'Group..',
														cls: 'dv-toolbar-btn-2',
														handler: function() {},
														listeners: {
															added: function() {
																this.menu = Ext.create('Ext.menu.Menu', {
																	shadow: false,
																	showSeparator: false,
																	width: DV.conf.layout.treepanel_toolbar_menu_width_group,
																	items: [
																		{
																			xtype: 'grid',
																			cls: 'dv-menugrid',
																			width: DV.conf.layout.treepanel_toolbar_menu_width_group,
																			scroll: 'vertical',
																			columns: [
																				{
																					dataIndex: 'name',
																					width: DV.conf.layout.treepanel_toolbar_menu_width_group,
																					style: 'display:none'
																				}
																			],
																			setHeightInMenu: function(store) {
																				var h = store.getCount() * 24,
																					sh = DV.util.viewport.getSize().y * 0.6;
																				this.setHeight(h > sh ? sh : h);
																				this.doLayout();
																				this.up('menu').doLayout();
																			},
																			store: DV.store.group,
																			listeners: {
																				itemclick: function(g, r) {
																					g.getSelectionModel().select([], false);
																					this.up('menu').hide();
																					DV.cmp.dimension.organisationunit.treepanel.selectByGroup(r.data.id);
																				}
																			}
																		}
																	],
																	listeners: {
																		show: function() {
																			if (!DV.store.group.isloaded) {
																				DV.store.group.load({scope: this, callback: function() {
																					this.down('grid').setHeightInMenu(DV.store.group);
																				}});
																			}
																			else {
																				this.down('grid').setHeightInMenu(DV.store.group);
																			}
																		}
																	}
																});
															}
														}
													}
												],
												listeners: {
													added: function() {
														DV.cmp.dimension.organisationunit.toolbar = this;
													}
												}
											},
											{
												xtype: 'treepanel',
												cls: 'dv-tree',
												width: DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor,
												rootVisible: false,
												autoScroll: true,
												multiSelect: true,
												rendered: false,
												selectRootIf: function() {
													if (this.getSelectionModel().getSelection().length < 1) {
														var node = this.getRootNode().findChild('id', DV.init.system.rootnodes[0].id, true);
														if (this.rendered) {
															this.getSelectionModel().select(node);
														}
														return node;
													}
												},
												numberOfRecords: 0,
												recordsToSelect: [],
												multipleSelectIf: function() {
													if (this.recordsToSelect.length === this.numberOfRecords) {
														this.getSelectionModel().select(this.recordsToSelect);
														this.recordsToSelect = [];
														this.numberOfRecords = 0;
													}
												},
												multipleExpand: function(id, path) {
													this.expandPath('/' + DV.conf.finals.root.id + path, 'id', '/', function() {
														var record = this.getRootNode().findChild('id', id, true);
														this.recordsToSelect.push(record);
														this.multipleSelectIf();
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
															var a = Ext.JSON.decode(r.responseText).organisationUnits;
															this.numberOfRecords = a.length;
															for (var i = 0; i < a.length; i++) {
																this.multipleExpand(a[i].id, a[i].path);
															}
														}
													});
												},
												selectByGroup: function(id) {
													if (id) {
														var url = DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.organisationunit_getbygroup,
															params = {id: id};															
														this.select(url, params);
													}
												},
												selectByLevel: function(level) {
													if (level) {
														var url = DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.organisationunit_getbylevel,
															params = {level: level};															
														this.select(url, params);
													}
												},
												selectByIds: function(ids) {
													if (ids) {
														var url = DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.organisationunit_getbyids;
														Ext.Array.each(ids, function(item) {
															url = Ext.String.urlAppend(url, 'ids=' + item);
														});
														if (!this.rendered) {
															DV.cmp.dimension.organisationunit.panel.expand();
														}
														this.select(url);
													}
												},														
												store: Ext.create('Ext.data.TreeStore', {
													proxy: {
														type: 'ajax',
														url: DV.conf.finals.ajax.path_visualizer + DV.conf.finals.ajax.organisationunitchildren_get
													},
													root: {
														id: DV.conf.finals.root.id,
														expanded: true,
														children: DV.init.system.rootnodes
													},
													listeners: {
														load: function(s, node, r) {
															for (var i = 0; i < r.length; i++) {
																r[i].data.text = DV.conf.util.jsonEncodeString(r[i].data.text);
															}
														}
													}
												}),
												xable: function(checked, value) {
													if (checked || value) {
														this.disable();
													}
													else {
														this.enable();
													}
												},
												listeners: {
													added: function() {
														DV.cmp.dimension.organisationunit.treepanel = this;
													},
													render: function() {
														this.rendered = true;
													},
													itemcontextmenu: function(v, r, h, i, e) {
														v.getSelectionModel().select(r, false);
														
														if (v.menu) {
															v.menu.destroy();
														}
														v.menu = Ext.create('Ext.menu.Menu', {
															id: 'treepanel-contextmenu',
															showSeparator: false
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
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.organisationunit.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_organisationunit);
												DV.cmp.dimension.organisationunit.treepanel.setHeight(DV.cmp.dimension.organisationunit.panel.getHeight() - DV.conf.layout.west_fill_accordion_organisationunit);
												DV.cmp.dimension.organisationunit.treepanel.selectRootIf();
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/organisationunit.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.organisation_unit_groups + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'label',
												style: 'font-style:italic; font-size:11px; color:#666',
												margin: '0 0 0 7',
												text: DV.i18n.groups_replace_orgunits
											},
											{
												xtype: 'combobox',
												cls: 'dv-combo',
												style: 'margin-top:10px',
												width: DV.conf.layout.west_fieldset_width - DV.conf.layout.west_width_subtractor,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: 'By group set',
												labelWidth: 85,
												labelStyle: 'padding-left:7px;',
												editable: false,
												queryMode: 'remote',
												value: DV.i18n.none,
												store: DV.store.groupset,
												listeners: {
													added: function() {
														this.up('panel').groupsets = this;
													}
												}
											}
										],
										listeners: {
											added: function() {
												DV.cmp.dimension.organisationunitgroup.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_organisationunitgroup);
											}
										}
									},
									{
										title: '<div style="height:17px; background-image:url(images/options.png); background-repeat:no-repeat; padding-left:20px">' + DV.i18n.chart_options + '</div>',
										hideCollapseTool: true,
										cls: 'dv-accordion-options',
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'padding-bottom:3px',
												items: [
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.show_data,
														labelWidth: DV.conf.layout.form_label_width,
														listeners: {
															added: function() {
																DV.cmp.favorite.showdata = this;
															}
														}
													},
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.hide_subtitle,
														labelWidth: DV.conf.layout.form_label_width,
														listeners: {
															added: function() {
																DV.cmp.favorite.hidesubtitle = this;
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'padding-bottom:15px',
												items: [
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.trend_line,
														labelWidth: DV.conf.layout.form_label_width,
														listeners: {
															added: function() {
																DV.cmp.favorite.trendline = this;
															}
														}
													},
													{
														xtype: 'checkbox',
														columnWidth: 0.5,
														boxLabel: DV.i18n.hide_legend,
														labelWidth: DV.conf.layout.form_label_width,
														listeners: {
															added: function() {
																DV.cmp.favorite.hidelegend = this;
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'padding-bottom:8px',
												items: [
													{
														xtype: 'textfield',
														cls: 'dv-textfield-alt1',
														style: 'margin-right:6px',
														fieldLabel: DV.i18n.domain_axis_label,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														labelWidth: DV.conf.layout.form_label_width,
														width: 187,
														listeners: {
															added: function() {
																DV.cmp.favorite.domainaxislabel = this;
															}
														}
													},
													{
														xtype: 'textfield',
														cls: 'dv-textfield-alt1',
														fieldLabel: DV.i18n.range_axis_label,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														labelWidth: DV.conf.layout.form_label_width,
														width: 187,
														listeners: {
															added: function() {
																DV.cmp.favorite.rangeaxislabel = this;
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'padding-bottom:8px',
												items: [
													{
														xtype: 'numberfield',
														cls: 'dv-textfield-alt1',
														style: 'margin-right:6px',
														hideTrigger: true,
														fieldLabel: DV.i18n.target_line_value,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														width: 187,
														spinUpEnabled: true,
														spinDownEnabled: true,
														listeners: {
															added: function() {
																DV.cmp.favorite.targetlinevalue = this;
															},
															change: function() {
																DV.cmp.favorite.targetlinelabel.xable();
															}
														}
													},
													{
														xtype: 'textfield',
														cls: 'dv-textfield-alt1',
														fieldLabel: DV.i18n.target_line_label,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														width: 187,
														disabled: true,
														xable: function() {
															if (DV.cmp.favorite.targetlinevalue.getValue()) {
																this.enable();
															}
															else {
																this.disable();
															}
														},
														listeners: {
															added: function() {
																DV.cmp.favorite.targetlinelabel = this;
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'padding-bottom:5px',
												items: [
													{
														xtype: 'numberfield',
														cls: 'dv-textfield-alt1',
														style: 'margin-right:6px',
														hideTrigger: true,
														fieldLabel: DV.i18n.base_line_value,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														width: 187,
														spinUpEnabled: true,
														spinDownEnabled: true,
														listeners: {
															added: function() {
																DV.cmp.favorite.baselinevalue = this;
															},
															change: function() {
																DV.cmp.favorite.baselinelabel.xable();
															}
														}
													},
													{
														xtype: 'textfield',
														cls: 'dv-textfield-alt1',
														fieldLabel: DV.i18n.base_line_label,
														labelAlign: 'top',
														labelSeparator: '',
														maxLength: 100,
														enforceMaxLength: true,
														width: 187,
														disabled: true,
														xable: function() {
															if (DV.cmp.favorite.baselinevalue.getValue()) {
																this.enable();
															}
															else {
																this.disable();
															}
														},
														listeners: {
															added: function() {
																DV.cmp.favorite.baselinelabel = this;
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												DV.cmp.options.panel = this;
											},
											expand: function() {
												DV.util.dimension.panel.setHeight(DV.conf.layout.west_maxheight_accordion_options);
											}
										}
									}
								],
								listeners: {
									added: function() {
										DV.cmp.dimension.panel = this;
									}
								}
							}
						]
					}					
				],
                listeners: {
                    added: function() {
                        DV.cmp.region.west = this;
                    },
                    collapse: function() {                    
                        this.collapsed = true;
                        DV.cmp.toolbar.resizewest.setText('>>>');
                    },
                    expand: function() {
                        this.collapsed = false;
                        DV.cmp.toolbar.resizewest.setText('<<<');
                    }
                }
            },
            {
                id: 'center',
                region: 'center',
                layout: 'fit',
                bodyStyle: 'padding-top:5px',
                tbar: {
                    xtype: 'toolbar',
                    cls: 'dv-toolbar-1',
                    defaults: {
                        height: 30
                    },
                    items: [
                        {
                            xtype: 'button',
                            name: 'resizewest',
							cls: 'dv-toolbar-btn-2',
                            text: '<<<',
                            handler: function() {
                                var p = DV.cmp.region.west;
                                if (p.collapsed) {
                                    p.expand();
                                }
                                else {
                                    p.collapse();
                                }
                            },
                            listeners: {
                                added: function() {
                                    DV.cmp.toolbar.resizewest = this;
                                }
                            }
                        },
                        {
                            xtype: 'button',
							cls: 'dv-toolbar-btn-1',
                            text: DV.i18n.update,
                            handler: function() {
								DV.c.currentFavorite = null;
                                DV.exe.execute();
                            }
                        },
                        {
                            xtype: 'button',
							cls: 'dv-toolbar-btn-2',
                            text: DV.i18n.favorites + '..',
                            listeners: {
                                afterrender: function(b) {
                                    this.menu = Ext.create('Ext.menu.Menu', {
                                        shadow: false,
                                        showSeparator: false,
                                        items: [
                                            {
                                                text: DV.i18n.manage_favorites,
                                                iconCls: 'dv-menu-item-edit',
                                                handler: function() {
													DV.store.favorite.filtersystem();
                                                    if (DV.cmp.favorite.window) {
                                                        DV.cmp.favorite.window.show();
                                                    }
                                                    else {
                                                        DV.cmp.favorite.window = Ext.create('Ext.window.Window', {
                                                            title: DV.i18n.manage_favorites,
                                                            iconCls: 'dv-window-title-favorite',
                                                            bodyStyle: 'padding:8px; background-color:#fff',
															width: DV.conf.layout.grid_favorite_width,
                                                            closeAction: 'hide',
                                                            resizable: false,
                                                            modal: true,
                                                            resetForm: function() {
                                                                DV.cmp.favorite.name.setValue('');
                                                                DV.cmp.favorite.system.setValue(false);
                                                            },
                                                            items: [
                                                                {
                                                                    xtype: 'form',
                                                                    bodyStyle: 'border-style:none',
                                                                    items: [
                                                                        {
                                                                            xtype: 'textfield',
                                                                            cls: 'dv-textfield',
                                                                            fieldLabel: 'Name',
                                                                            maxLength: 160,
                                                                            enforceMaxLength: true,
                                                                            labelWidth: DV.conf.layout.form_label_width,
                                                                            width: DV.conf.layout.grid_favorite_width - 28,
                                                                            listeners: {
                                                                                added: function() {
                                                                                    DV.cmp.favorite.name = this;
                                                                                },
                                                                                change: function() {
                                                                                    DV.cmp.favorite.system.check();
                                                                                    DV.cmp.favorite.save.xable();
                                                                                }
                                                                            }
                                                                        },
                                                                        {
                                                                            xtype: 'checkbox',
                                                                            cls: 'dv-checkbox',
                                                                            style: 'padding-bottom:2px',
                                                                            fieldLabel: DV.i18n.system,
                                                                            labelWidth: DV.conf.layout.form_label_width,
                                                                            disabled: !DV.init.user.isadmin,
                                                                            check: function() {
                                                                                if (!DV.init.user.isadmin) {
                                                                                    if (DV.store.favorite.findExact('name', DV.cmp.favorite.name.getValue()) === -1) {
                                                                                        this.setValue(false);
                                                                                    }
                                                                                }
                                                                            },
                                                                            listeners: {
                                                                                added: function() {
                                                                                    DV.cmp.favorite.system = this;
                                                                                }
                                                                            }
                                                                        }
                                                                    ]
                                                                },
                                                                {
                                                                    xtype: 'grid',
                                                                    width: DV.conf.layout.grid_favorite_width - 28,
                                                                    scroll: 'vertical',
                                                                    multiSelect: true,
                                                                    columns: [
                                                                        {
                                                                            dataIndex: 'name',
                                                                            width: DV.conf.layout.grid_favorite_width - 139,
                                                                            style: 'display:none'
                                                                        },
                                                                        {
                                                                            dataIndex: 'lastUpdated',
                                                                            width: 111,
                                                                            style: 'display:none'
                                                                        }
                                                                    ],
                                                                    setHeightInWindow: function(store) {
                                                                        var h = (store.getCount() * 23) + 30,
                                                                            sh = DV.util.viewport.getSize().y * 0.6;
                                                                        this.setHeight(h > sh ? sh : h);
                                                                        this.doLayout();
                                                                        this.up('window').doLayout();
                                                                    },
                                                                    store: DV.store.favorite,
                                                                    tbar: {
                                                                        id: 'favorite_t',
                                                                        cls: 'dv-toolbar-tbar',
                                                                        defaults: {
                                                                            height: 28
                                                                        },
                                                                        items: [
                                                                            {
                                                                                text: DV.i18n.sort_by + '..',
                                                                                cls: 'dv-toolbar-btn-2',
                                                                                listeners: {
                                                                                    added: function() {
                                                                                        DV.cmp.favorite.sortby = this;
                                                                                    },
                                                                                    afterrender: function(b) {
                                                                                        this.addCls('dv-menu-togglegroup');
                                                                                        this.menu = Ext.create('Ext.menu.Menu', {
																							margin: '-1 0 0 -1',
																							shadow: false,
                                                                                            showSeparator: false,
                                                                                            width: 109,
                                                                                            height: 67,
                                                                                            items: [
                                                                                                {
                                                                                                    xtype: 'radiogroup',
                                                                                                    cls: 'dv-radiogroup',
                                                                                                    columns: 1,
                                                                                                    vertical: true,
                                                                                                    items: [
                                                                                                        {
                                                                                                            boxLabel: DV.i18n.name,
                                                                                                            name: 'sortby',
                                                                                                            handler: function() {
                                                                                                                if (this.getValue()) {
                                                                                                                    var store = DV.store.favorite;
                                                                                                                    store.sorting.field = 'name';
                                                                                                                    store.sorting.direction = 'ASC';
                                                                                                                    store.sortStore();
                                                                                                                    this.up('menu').hide();
                                                                                                                }
                                                                                                            }
                                                                                                        },
                                                                                                        {
                                                                                                            boxLabel: DV.i18n.system,
                                                                                                            name: 'sortby',
                                                                                                            handler: function() {
                                                                                                                if (this.getValue()) {
                                                                                                                    var store = DV.store.favorite;
                                                                                                                    store.sorting.field = 'userId';
                                                                                                                    store.sorting.direction = 'ASC';
                                                                                                                    store.sortStore();
                                                                                                                    this.up('menu').hide();
                                                                                                                }
                                                                                                            }
                                                                                                        },
                                                                                                        {
                                                                                                            boxLabel:  DV.i18n.last_updated,
                                                                                                            name: 'sortby',
                                                                                                            checked: true,
                                                                                                            handler: function() {
                                                                                                                if (this.getValue()) {
                                                                                                                    var store = DV.store.favorite;
                                                                                                                    store.sorting.field = 'lastUpdated';
                                                                                                                    store.sorting.direction = 'DESC';
                                                                                                                    store.sortStore();
                                                                                                                    this.up('menu').hide();
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    ]
                                                                                                }
                                                                                            ]
                                                                                        });
                                                                                    }
                                                                                }
                                                                            },
                                                                            '->',
                                                                            {
                                                                                text: DV.i18n.rename + '..',
                                                                                cls: 'dv-toolbar-btn-2',
                                                                                disabled: true,
                                                                                xable: function() {
                                                                                    if (DV.cmp.favorite.grid.getSelectionModel().getSelection().length == 1) {
                                                                                        DV.cmp.favorite.rename.button.enable();
                                                                                    }
                                                                                    else {
                                                                                        DV.cmp.favorite.rename.button.disable();
                                                                                    }
                                                                                },
                                                                                handler: function() {
                                                                                    var selected = DV.cmp.favorite.grid.getSelectionModel().getSelection()[0];
                                                                                    var w = Ext.create('Ext.window.Window', {
                                                                                        title: DV.i18n.rename_favorite,
                                                                                        layout: 'fit',
                                                                                        width: DV.conf.layout.window_confirm_width,
                                                                                        bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
                                                                                        modal: true,
                                                                                        cmp: {},
                                                                                        items: [
                                                                                            {
                                                                                                xtype: 'textfield',
                                                                                                cls: 'dv-textfield',
                                                                                                maxLength: 160,
                                                                                                enforceMaxLength: true,
                                                                                                value: selected.data.name,
                                                                                                listeners: {
                                                                                                    added: function() {
                                                                                                        this.up('window').cmp.name = this;
                                                                                                    },
                                                                                                    change: function() {
                                                                                                        this.up('window').cmp.rename.xable();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        ],
                                                                                        bbar: {
																							cls: 'dv-toolbar-bbar',
																							defaults: {
																								height: 28
																							},
																							items: [
																								{
																									xtype: 'label',
																									style: 'padding-left:2px; line-height:22px; font-size:10px; color:#666; width:50%',
																									listeners: {
																										added: function() {
																											DV.cmp.favorite.rename.label = this;
																										}
																									}
																								},
																								'->',
																								{
																									text: DV.i18n.cancel,
																									handler: function() {
																										this.up('window').close();
																									}
																								},
																								{
																									text: DV.i18n.rename,
																									disabled: true,
																									xable: function() {
																										var value = this.up('window').cmp.name.getValue();
																										if (value) {
																											if (DV.store.favorite.findExact('name', value) == -1) {
																												this.enable();
																												DV.cmp.favorite.rename.label.setText('');
																												return;
																											}
																											else {
																												DV.cmp.favorite.rename.label.setText(DV.i18n.name_already_in_use);
																											}
																										}
																										this.disable();
																									},
																									handler: function() {
																										DV.util.crud.favorite.updateName(this.up('window').cmp.name.getValue());
																									},
																									listeners: {
																										afterrender: function() {
																											this.up('window').cmp.rename = this;
																										},
																										change: function() {
																											this.xable();
																										}
																									}
																								}
																							]
																						},
                                                                                        listeners: {
                                                                                            afterrender: function() {
                                                                                                DV.cmp.favorite.rename.window = this;
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                    w.setPosition((screen.width/2)-(DV.conf.layout.window_confirm_width/2), DV.conf.layout.window_favorite_ypos + 100, true);
                                                                                    w.show();
                                                                                },
                                                                                listeners: {
                                                                                    added: function() {
                                                                                        DV.cmp.favorite.rename.button = this;
                                                                                    }
                                                                                }
                                                                            },
                                                                            {
                                                                                text: DV.i18n.delete_object + '..',
                                                                                cls: 'dv-toolbar-btn-2',
                                                                                disabled: true,
                                                                                xable: function() {
                                                                                    if (DV.cmp.favorite.grid.getSelectionModel().getSelection().length) {
                                                                                        DV.cmp.favorite.del.enable();
                                                                                    }
                                                                                    else {
                                                                                        DV.cmp.favorite.del.disable();
                                                                                    }
                                                                                },
                                                                                handler: function() {
                                                                                    var sel = DV.cmp.favorite.grid.getSelectionModel().getSelection();
                                                                                    if (sel.length) {
                                                                                        var str = '';
                                                                                        for (var i = 0; i < sel.length; i++) {
                                                                                            var out = sel[i].data.name.length > 35 ? (sel[i].data.name.substr(0,35) + '...') : sel[i].data.name;
                                                                                            str += '<br/>' + out;
                                                                                        }
                                                                                        var w = Ext.create('Ext.window.Window', {
                                                                                            title: DV.i18n.delete_favorite,
                                                                                            width: DV.conf.layout.window_confirm_width,
                                                                                            bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
                                                                                            modal: true,
                                                                                            items: [
                                                                                                {
                                                                                                    html: DV.i18n.are_you_sure,
                                                                                                    bodyStyle: 'border-style:none'
                                                                                                },
                                                                                                {
                                                                                                    html: str,
                                                                                                    cls: 'dv-window-confirm-list'
                                                                                                }                                                                                                    
                                                                                            ],
                                                                                            bbar: {
																								cls: 'dv-toolbar-bbar',
																								defaults: {
																									height: 28
																								},
																								items: [
																									{
																										text: DV.i18n.cancel,
																										handler: function() {
																											this.up('window').close();
																										}
																									},
																									'->',
																									{
																										text: DV.i18n.delete_object,
																										handler: function() {
																											this.up('window').close();
																											DV.util.crud.favorite.del(function() {
																												DV.cmp.favorite.name.setValue('');
																												DV.cmp.favorite.window.down('grid').setHeightInWindow(DV.store.favorite);
																											});                                                                                                        
																										}
																									}
																								]
																							}
                                                                                        });
                                                                                        w.setPosition((screen.width/2)-(DV.conf.layout.window_confirm_width/2), DV.conf.layout.window_favorite_ypos + 100, true);
                                                                                        w.show();
                                                                                    }
                                                                                },
                                                                                listeners: {
                                                                                    added: function() {
                                                                                        DV.cmp.favorite.del = this;
                                                                                    }
                                                                                }
                                                                            }
                                                                        ]
                                                                    },
                                                                    listeners: {
                                                                        added: function() {
                                                                            DV.cmp.favorite.grid = this;
                                                                        },
                                                                        itemclick: function(g, r) {
                                                                            DV.cmp.favorite.name.setValue(r.data.name);
                                                                            DV.cmp.favorite.system.setValue(r.data.userId ? false : true);
                                                                            DV.cmp.favorite.rename.button.xable();
                                                                            DV.cmp.favorite.del.xable();
                                                                        },
                                                                        itemdblclick: function() {
                                                                            if (DV.cmp.favorite.save.xable()) {
                                                                                DV.cmp.favorite.save.handler();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            ],
                                                            bbar: {
																cls: 'dv-toolbar-bbar',
																defaults: {
																	height: 28
																},
																items: [
																	{
																		xtype: 'label',
																		style: 'padding-left:6px; line-height:29px; font-size:10px; color:#666; width:70%',
																		listeners: {
																			added: function() {
																				DV.cmp.favorite.label = this;
																			}
																		}
																	},																
																	'->',
																	{
																		text: DV.i18n.save,
																		disabled: true,
																		xable: function() {
																			if (DV.c.rendered) {
																				if (DV.cmp.favorite.name.getValue()) {
																					var index = DV.store.favorite.findExact('name', DV.cmp.favorite.name.getValue());
																					if (index != -1) {
																						if (DV.store.favorite.getAt(index).data.userId || DV.init.user.isadmin) {
																							this.enable();
																							DV.cmp.favorite.label.setText('');
																							return true;
																						}
																						else {
																							DV.cmp.favorite.label.setText(DV.i18n.system_favorite_overwrite_not_allowed);
																						}
																					}
																					else {
																						this.enable();
																						DV.cmp.favorite.label.setText('');
																						return true;
																					}
																				}
																				else {
																					DV.cmp.favorite.label.setText('');
																				}
																			}
																			else {
																				if (DV.cmp.favorite.name.getValue()) {
																					DV.cmp.favorite.label.setText('* ' + DV.i18n.create_chart_before_saving);
																				}
																				else {
																					DV.cmp.favorite.label.setText('');
																				}																				
																			}
																			this.disable();
																			return false;
																		},
																		handler: function() {
																			if (this.xable()) {
																				var value = DV.cmp.favorite.name.getValue();
																				if (DV.store.favorite.findExact('name', value) != -1) {
																					var item = value.length > 40 ? (value.substr(0,40) + '...') : value;
																					var w = Ext.create('Ext.window.Window', {
																						title: DV.i18n.save_favorite,
																						width: DV.conf.layout.window_confirm_width,
																						bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
																						modal: true,
																						items: [
																							{
																								html: DV.i18n.are_you_sure,
																								bodyStyle: 'border-style:none'
																							},
																							{
																								html: '<br/>' + item,
																								cls: 'dv-window-confirm-list'
																							}
																						],
																						bbar: {
																							cls: 'dv-toolbar-bbar',
																							defaults: {
																								height: 28
																							},
																							items: [
																								{
																									text: DV.i18n.cancel,
																									handler: function() {
																										this.up('window').close();
																									}
																								},
																								'->',
																								{
																									text: DV.i18n.overwrite,
																									handler: function() {
																										this.up('window').close();
																										DV.util.crud.favorite.update(function() {
																											DV.cmp.favorite.window.resetForm();
																										});
																										
																									}
																								}
																							]
																						}
																					});
																					w.setPosition((screen.width/2)-(DV.conf.layout.window_confirm_width/2), DV.conf.layout.window_favorite_ypos + 100, true);
																					w.show();
																				}
																				else {
																					DV.util.crud.favorite.create(function() {
																						DV.cmp.favorite.window.resetForm();
																						DV.cmp.favorite.window.down('grid').setHeightInWindow(DV.store.favorite);
																					});
																				}                                                                                    
																			}
																		},
																		listeners: {
																			added: function() {
																				DV.cmp.favorite.save = this;
																			}
																		}
																	}
																]
															},
                                                            listeners: {
                                                                show: function() {                                               
                                                                    DV.cmp.favorite.save.xable();
                                                                    this.down('grid').setHeightInWindow(DV.store.favorite);
                                                                },
                                                                hide: function() {
																	DV.store.favorite.clearFilter();
																}
                                                            }
                                                        });
                                                        var w = DV.cmp.favorite.window;
                                                        w.setPosition((screen.width/2)-(DV.conf.layout.grid_favorite_width/2), DV.conf.layout.window_favorite_ypos, true);
                                                        w.show();
                                                    }
                                                },
                                                listeners: {
                                                    added: function() {
                                                        DV.cmp.toolbar.menuitem.datatable = this;
                                                    }
                                                }
                                            },
                                            {
												xtype: 'menuseparator',
												height: 1,
												style: 'margin:1px 0; border-color:#dadada'
											},
                                            {
                                                xtype: 'grid',
                                                cls: 'dv-menugrid',
                                                width: 420,
                                                scroll: 'vertical',
                                                columns: [
                                                    {
                                                        dataIndex: 'icon',
                                                        width: 25,
                                                        style: 'display:none'
                                                    },
                                                    {
                                                        dataIndex: 'name',
                                                        width: 285,
                                                        style: 'display:none'
                                                    },
                                                    {
                                                        dataIndex: 'lastUpdated',
                                                        width: 110,
                                                        style: 'display:none'
                                                    }
                                                ],
                                                setHeightInMenu: function(store) {
                                                    var h = store.getCount() * 24,
                                                        sh = DV.util.viewport.getSize().y * 0.6;
                                                    this.setHeight(h > sh ? sh : h);
                                                    this.doLayout();
                                                    this.up('menu').doLayout();
                                                },
                                                store: DV.store.favorite,
                                                listeners: {
                                                    itemclick: function(g, r) {
                                                        g.getSelectionModel().select([], false);
                                                        this.up('menu').hide();
                                                        DV.c.currentFavorite = {
															id: r.data.id,
															name: r.data.name
														};
                                                        DV.exe.execute(r.data.id);
                                                    }
                                                }
                                            }
                                        ],
                                        listeners: {
                                            show: function() {
                                                if (!DV.store.favorite.isloaded) {
                                                    DV.store.favorite.load({scope: this, callback: function() {
                                                        this.down('grid').setHeightInMenu(DV.store.favorite);
                                                    }});
                                                }
                                                else {
                                                    this.down('grid').setHeightInMenu(DV.store.favorite);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        },
                        {
							xtype: 'button',
							cls: 'dv-toolbar-btn-2',
							text: DV.i18n.share + '..',
							disabled: true,
							xable: function() {
								if (DV.c.currentFavorite) {
									this.enable();
									this.setTooltip('');
								}
								else {
									this.disable();
									this.setTooltip(DV.i18n.save_load_favorite_before_sharing);
								}
							},
							getTitle: function() {
								return DV.i18n.share + ' ' + DV.i18n.interpretation +
										': <span style="font-weight:normal; font-size:10px">' + DV.c.currentFavorite.name + '</span>';
							},
							handler: function() {
								if (DV.cmp.share.window) {
									DV.cmp.share.window.setTitle(this.getTitle());
									DV.cmp.share.window.show();
								}
								else {
									DV.cmp.share.window = Ext.create('Ext.window.Window', {
										title: this.getTitle(),
										iconCls: 'dv-window-title-interpretation',
										layout: 'fit',
										bodyStyle: 'padding:8px 8px 3px; background-color:#fff',
										width: DV.conf.layout.window_share_width,
										height: 200,
										closeAction: 'hide',
										resizable: true,
										modal: true,
										items: [
											{
												xtype: 'textarea',
												cls: 'dv-textarea',
												width: 350,
												emptyText: DV.i18n.write_your_interpretation + '...',
												enableKeyEvents: true,
												listeners: {
													added: function() {
														DV.cmp.share.textarea = this;
													},
													keyup: function() {
														DV.cmp.share.button.xable();
													}
												}
											}
										],
										bbar: {
											cls: 'dv-toolbar-bbar',
											defaults: {
												height: 28
											},
											items: [
												'->',
												{
													text: DV.i18n.share,
													disabled: true,
													xable: function() {
														if (DV.cmp.share.textarea.getValue()) {
															this.enable();
														}
														else {
															this.disable();
														}
													},
													handler: function() {
														if (DV.cmp.share.textarea.getValue() && DV.c.currentFavorite) {
															Ext.Ajax.request({
																url: DV.conf.finals.ajax.path_api + 'interpretations/chart/' + DV.c.currentFavorite.id,
																method: 'POST',
																params: DV.cmp.share.textarea.getValue(),
																headers: {'Content-Type': 'text/html'},
																success: function() {
																	DV.cmp.share.textarea.reset();
																	DV.cmp.share.button.disable();
																	DV.cmp.share.window.hide();
																	DV.util.notification.interpretation(DV.i18n.interpretation_was_shared + '.');
																}
															});
														}
													},
													listeners: {
														added: function() {
															DV.cmp.share.button = this;
														}
													}
												}
											]
										}
									}).show();
								}
							},
                            listeners: {
                                added: function() {
                                    DV.cmp.toolbar.share = this;
                                }
                            }
						},
                        {
                            xtype: 'button',
							cls: 'dv-toolbar-btn-2',
                            text: DV.i18n.download + '..',
                            execute: function(type) {
                                var svg = document.getElementsByTagName('svg');
                                
                                if (svg.length < 1) {
									DV.util.notification.error(DV.i18n.et_svg_browser, DV.i18n.em_svg_browser);
                                    return;
                                }
                                
                                document.getElementById('titleField').value = DV.c.filter.names[0] || 'Example chart';
                                document.getElementById('svgField').value = svg[0].parentNode.innerHTML;
                                document.getElementById('typeField').value = type;
                                
                                var exportForm = document.getElementById('exportForm');
                                exportForm.action = '../exportImage.action';
                                
                                if (svg[0].parentNode.innerHTML && type) {
                                    exportForm.submit();
                                }
                                else {
                                    alert(DV.i18n.no_svg_format);
                                }
                            },
                            listeners: {
                                afterrender: function(b) {
                                    this.menu = Ext.create('Ext.menu.Menu', {
										cls: 'dv-menu',
                                        shadow: false,
                                        showSeparator: false,
                                        items: [
                                            {
                                                text: DV.i18n.image_png,
                                                iconCls: 'dv-menu-item-png',
                                                minWidth: 105,
                                                handler: function() {
                                                    b.execute(DV.conf.finals.image.png);
                                                }
                                            },
                                            {
                                                text: 'PDF',
                                                iconCls: 'dv-menu-item-pdf',
                                                minWidth: 105,
                                                handler: function() {
                                                    b.execute(DV.conf.finals.image.pdf);
                                                }
                                            }
                                        ]                                            
                                    });
                                }
                            }
                        },
                        {
                            xtype: 'button',
							cls: 'dv-toolbar-btn-2',
                            text: DV.i18n.data_table,
                            disabled: true,
                            tooltip: DV.i18n.create_chart_before_datatable,
                            handler: function() {
                                var p = DV.cmp.region.east;
                                if (p.collapsed && p.items.length) {
                                    p.expand();
                                    DV.cmp.toolbar.resizeeast.show();
                                    DV.exe.datatable();
                                }
                                else {
                                    p.collapse();
                                    DV.cmp.toolbar.resizeeast.hide();
                                }
                            },
                            listeners: {
                                added: function() {
                                    DV.cmp.toolbar.datatable = this;
                                }
                            }
                        },
                        '->',
                        {
                            xtype: 'button',
							cls: 'dv-toolbar-btn-2',
                            text: 'Exit',
                            handler: function() {
                                window.location.href = DV.conf.finals.ajax.path_portal + DV.conf.finals.ajax.redirect;
                            }
                        },
                        {
                            xtype: 'button',
                            name: 'resizeeast',
							cls: 'dv-toolbar-btn-2',
                            text: '>>>',
                            hidden: true,
                            handler: function() {
                                DV.cmp.region.east.collapse();
                                this.hide();
                            },
                            listeners: {
                                added: function() {
                                    DV.cmp.toolbar.resizeeast = this;
                                }
                            }
                        }
                    ]
                },
                bbar: {
					items: [
						{
							xtype: 'panel',
							cls: 'dv-statusbar',
							height: 24,
							listeners: {
								added: function() {
									DV.cmp.statusbar.panel = this;
								}
							}
						}
					]
				},					
                listeners: {
                    added: function() {
                        DV.cmp.region.center = this;
                    },
                    resize: function() {
						if (DV.cmp.statusbar.panel) {
							DV.cmp.statusbar.panel.setWidth(DV.cmp.region.center.getWidth());
						}
					}
                }
            },
            {
                region: 'east',
                preventHeader: true,
                collapsible: true,
                collapsed: true,
                collapseMode: 'mini',
                width: 398,
                listeners: {
                    afterrender: function() {
                        DV.cmp.region.east = this;
                    }
                }
            }
        ],
        listeners: {
            afterrender: function(vp) {
                DV.init.initialize(vp);
            },
            resize: function(vp) {
                DV.cmp.region.west.setWidth(DV.conf.layout.west_width);
                
				DV.util.viewport.resizeDimensions();
                
                if (DV.datatable.datatable) {
                    DV.datatable.datatable.setHeight(DV.util.viewport.getSize().y - DV.conf.layout.east_tbar_height);
                }
            }
        }
    });
    
    }});
});
