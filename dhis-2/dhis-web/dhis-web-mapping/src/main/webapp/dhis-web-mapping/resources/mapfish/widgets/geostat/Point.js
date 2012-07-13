/*
 * Copyright (C) 2007-2008  Camptocamp|
 *
 * This file is part of MapFish Client
 *
 * MapFish Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MapFish Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MapFish Client.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @requires core/GeoStat/Point.js
 * @requires core/Color.js
 */

Ext.namespace('mapfish.widgets', 'mapfish.widgets.geostat');

mapfish.widgets.geostat.Point = Ext.extend(Ext.Panel, {

    layer: null,

    format: null,

    url: null,

    featureSelection: true,

    nameAttribute: null,

    indicator: null,

    indicatorText: null,

    coreComp: null,

    classificationApplied: false,

    ready: false,

    border: false,

    loadMask: false,

    labelGenerator: null,

    colorInterpolation: false,

    legend: false,

	bounds: false,

    mapView: false,
    
    labels: false,
    
    valueType: false,
    
    selectFeatures: false,
    
    organisationUnitSelection: false,
    
    updateValues: false,
    
    isDrillDown: false,

	imageLegend: false,
    
    stores: false,
    
    infrastructuralPeriod: false,
    
    featureOptions: {},
    
    cmp: {},
    
    requireUpdate: false,
    
    featureStorage: [],
    
    filtering: {
        cache: [],
        options: {
            gt: null,
            lt: null
        },
        cmp: {
            gt: null,
            lt: null,
            button: null
        },
        filter: function() {
            var gt = this.filtering.options.gt;
            var lt = this.filtering.options.lt;
            var add = [];
            if (!gt && !lt) {
                add = this.filtering.cache.slice(0);
            }
            else if (gt && lt) {
                for (var i = 0; i < this.filtering.cache.length; i++) {
                    if (gt < lt && (this.filtering.cache[i].attributes.value > gt && this.filtering.cache[i].attributes.value < lt)) {
                        add.push(this.filtering.cache[i]);
                    }
                    else if (gt > lt && (this.filtering.cache[i].attributes.value > gt || this.filtering.cache[i].attributes.value < lt)) {
                        add.push(this.filtering.cache[i]);
                    }
                    else if (gt == lt && this.filtering.cache[i].attributes.value == gt) {
                        add.push(this.filtering.cache[i]);
                    }
                }
            }
            else if (gt && !lt) {
                for (var i = 0; i < this.filtering.cache.length; i++) {
                    if (this.filtering.cache[i].attributes.value > gt) {
                        add.push(this.filtering.cache[i]);
                    }
                }
            }
            else if (!gt && lt) {
                for (var i = 0; i < this.filtering.cache.length; i++) {
                    if (this.filtering.cache[i].attributes.value < lt) {
                        add.push(this.filtering.cache[i]);
                    }
                }
            }
            this.layer.removeAllFeatures();
            this.layer.addFeatures(add);
        },
        showFilteringWindow: function() {
            var window = new Ext.Window({
                title: '<span class="window-filter-title">Organisation unit filter</span>',
                layout: 'fit',
                autoHeight: true,
                height: 'auto',
                width: G.conf.window_width,
                items: [
                    {
                        xtype: 'form',
                        bodyStyle:'padding:8px',
                        autoHeight: true,
                        height: 'auto',
                        labelWidth: G.conf.label_width,
                        items: [
                            { html: 'Show organisation units where <b>value</b> is..' },
                            { html: '<div class="window-p"></div>' },
                            {
                                xtype: 'numberfield',
                                fieldLabel: 'Greater than',
                                width: G.conf.combo_number_width_small,
                                listeners: {
                                    'afterrender': {
                                        scope: this,
                                        fn: function(nf) {
                                            this.filtering.cmp.gt = nf;
                                        }
                                    },
                                    'change': {
                                        scope: this,
                                        fn: function(nf) {
                                            this.filtering.options.gt = nf.getValue();
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'numberfield',
                                fieldLabel: 'Lower than',
                                width: G.conf.combo_number_width_small,
                                listeners: {
                                    'afterrender': {
                                        scope: this,
                                        fn: function(nf) {
                                            this.filtering.cmp.lt = nf;
                                        }
                                    },
                                    'change': {
                                        scope: this,
                                        fn: function(nf) {
                                            this.filtering.options.lt = nf.getValue();
                                        }
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
                        text: G.i18n.update,
                        iconCls: 'icon-assign',
                        scope: this,
                        handler: function() {
                            this.filtering.filter.call(this);
                        },
                        listeners: {
                            'afterrender': {
                                scope: this,
                                fn: function(b) {
                                    this.filtering.cmp.button = b;
                                }
                            }
                        }
                    }
                ],
                listeners: {
                    'afterrender': {
                        scope: this,
                        fn: function() {
                            this.filtering.cache = this.layer.features.slice(0);
                        }
                    },
                    'close': {
                        scope: this,
                        fn: function() {
                            this.layer.removeAllFeatures();
                            this.layer.addFeatures(this.filtering.cache);
                            this.filtering.options.gt = null;
                            this.filtering.options.lt = null;
                        }
                    }
                }
            });
            window.setPagePosition(G.conf.window_x_left,G.conf.window_y_left);
            window.show();
        }
    },
    
    initComponent: function() {
    
        this.initProperties();
        
        this.createItems();
        
        this.addItems();
        
        this.createSelectFeatures();
        
		mapfish.widgets.geostat.Point.superclass.initComponent.apply(this);
    },
    
    setUrl: function(url) {
        this.url = url;
        this.coreComp.setUrl(this.url);
    },

    requestSuccess: function(request) {
        this.ready = true;

        if (this.loadMask && this.rendered) {
            this.loadMask.hide();
        }
    },

    requestFailure: function(request) {
        OpenLayers.Console.error(G.i18n.ajax_request_failed);
    },
    
    getColors: function() {
        var startColor = new mapfish.ColorRgb();
        startColor.setFromHex(this.cmp.startColor.getValue());
        var endColor = new mapfish.ColorRgb();
        endColor.setFromHex(this.cmp.endColor.getValue());
        return [startColor, endColor];
    },
    
    initProperties: function() {
        this.legend = {
            value: G.conf.map_legendset_type_automatic,
            method: G.conf.classify_by_equal_intervals,
            classes: 5,
            reset: function() {
                this.value = G.conf.map_legendset_type_automatic;
                this.method = G.conf.classify_by_equal_intervals;
                this.classes = 5;
            }
        };
        
        this.organisationUnitSelection = {
            parent: {
                id: null,
                name: null,
                level: null
            },
            level: {
                level: null,
                name: null
            },
            setValues: function(pid, pn, pl, ll, ln) {
                this.parent.id = pid || this.parent.id;
                this.parent.name = pn || this.parent.name;
                this.parent.level = pl || this.parent.level;
                this.level.level = ll || this.level.level;
                this.level.name = ln || this.level.name;
            },
            getValues: function() {
                return {
                    parent: {
                        id: this.parent.id,
                        name: this.parent.name,
                        level: this.parent.level
                    },
                    level: {
                        level: this.level.level,
                        name: this.level.name
                    }                    
                };
            },
            setValuesOnDrillDown: function(pid, pn) {
                this.parent.id = pid;
                this.parent.name = pn;
                this.parent.level = this.level.level;
                this.level.level++;
                this.level.name = G.stores.organisationUnitLevel.getAt(
                    G.stores.organisationUnitLevel.find('level', this.level.level)).data.name;
            }                
        };
        
        this.valueType = {
            value: G.conf.map_value_type_indicator,
            setIndicator: function() {
                this.value = G.conf.map_value_type_indicator;
            },
            setDatElement: function() {
                this.value = G.conf.map_value_type_dataelement;
            },
            isIndicator: function() {
                return this.value == G.conf.map_value_type_indicator;
            },
            isDataElement: function() {
                return this.value == G.conf.map_value_type_dataelement;
            }
        };
        
        this.stores = {
            infrastructuralDataElementMapValue: new Ext.data.JsonStore({
                url: G.conf.path_mapping + 'getInfrastructuralDataElementMapValues' + G.conf.type,
                root: 'mapValues',
                fields: ['dataElementName', 'value'],
                sortInfo: {field: 'dataElementName', direction: 'ASC'},
                autoLoad: false,
                isLoaded: false,
                listeners: {
                    'load': G.func.storeLoadListener
                }
            }),
            indicatorsByGroup: new Ext.data.JsonStore({
                url: G.conf.path_mapping + 'getIndicatorsByIndicatorGroup' + G.conf.type,
                root: 'indicators',
                fields: ['id', 'name', 'shortName'],
                idProperty: 'id',
                sortInfo: {field: 'name', direction: 'ASC'},
                autoLoad: false,
                isLoaded: false,
                listeners: {
                    'load': function(store) {
                        store.isLoaded = true;
                        store.each(
                            function fn(record) {
                                var name = record.get('name');
                                name = name.replace('&lt;', '<').replace('&gt;', '>');
                                record.set('name', name);
                            }
                        );
                    }
                }
            }),
            dataElementsByGroup: new Ext.data.JsonStore({
                url: G.conf.path_mapping + 'getDataElementsByDataElementGroup' + G.conf.type,
                root: 'dataElements',
                fields: ['id', 'name', 'shortName'],
                sortInfo: {field: 'name', direction: 'ASC'},
                autoLoad: false,
                isLoaded: false,
                listeners: {
                    'load': function(store) {
                        store.isLoaded = true;
                        store.each(
                            function fn(record) {
                                var name = record.get('name');
                                name = name.replace('&lt;', '<').replace('&gt;', '>');
                                record.set('name', name);
                            }
                        );
                    }
                }
            }),
            periodsByType: new Ext.data.JsonStore({
                url: G.conf.path_mapping + 'getPeriodsByPeriodType' + G.conf.type,
                root: 'periods',
                fields: ['id', 'name'],
                autoLoad: false,
                isLoaded: false,
                listeners: {
                    'load': G.func.storeLoadListener
                }
            })
        };
    },
    
    createItems: function() {
        
        this.cmp.mapview = new Ext.form.ComboBox({
            fieldLabel: G.i18n.favorite,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            emptyText: G.i18n.optional,
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: G.stores.mapView,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        G.util.mapView.mapView.call(this, cb.getValue());
                    }
                }
            }
        });
        
        this.cmp.mapValueType = new Ext.form.ComboBox({
            fieldLabel: G.i18n.mapvaluetype,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'local',
            triggerAction: 'all',
            width: G.conf.combo_width,
            value: G.conf.map_value_type_indicator,
            store: new Ext.data.ArrayStore({
                fields: ['id', 'name'],
                data: [
                    [G.conf.map_value_type_indicator, 'Indicator'],
                    [G.conf.map_value_type_dataelement, 'Data element']
                ]
            }),
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.valueType.value = cb.getValue();
                        this.prepareMapViewValueType();
                        this.classify(false, true);
                    }
                }
            }
        });
        
        this.cmp.indicatorGroup = new Ext.form.ComboBox({
            fieldLabel: G.i18n.indicator_group,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: G.stores.indicatorGroup,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.cmp.indicator.clearValue();
                        this.stores.indicatorsByGroup.setBaseParam('indicatorGroupId', cb.getValue());
                        this.stores.indicatorsByGroup.load();
                    }
                }
            }
        });
        
        this.cmp.indicator = new Ext.form.ComboBox({
            fieldLabel: G.i18n.indicator,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'shortName',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            currentValue: null,
            lockPosition: false,
            reloadStore: function(id) {
                this.cmp.mapLegendSet.setValue(id);
                this.applyPredefinedLegend();
            },
            store: this.stores.indicatorsByGroup,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        if (G.util.setCurrentValue.call(this, cb, this.cmp.mapview)) {
                            return;
                        }
                        
                        this.updateValues = true;
                        Ext.Ajax.request({
                            url: G.conf.path_mapping + 'getMapLegendSetByIndicator' + G.conf.type,
                            params: {indicatorId: cb.getValue()},
                            scope: this,
                            success: function(r) {
                                var mapLegendSet = Ext.util.JSON.decode(r.responseText).mapLegendSet[0];
                                if (mapLegendSet.id) {
                                    this.legend.value = G.conf.map_legendset_type_predefined;
                                    this.prepareMapViewLegend();
                                    
                                    if (!G.stores.predefinedColorMapLegendSet.isLoaded) {
                                        G.stores.predefinedColorMapLegendSet.load({scope: this, callback: function() {
                                            cb.reloadStore.call(this, mapLegendSet.id);
                                        }});
                                    }
                                    else {
                                        cb.reloadStore.call(this, mapLegendSet.id);
                                    }
                                }
                                else {
                                    this.legend.value = G.conf.map_legendset_type_automatic;
                                    this.prepareMapViewLegend();
                                    this.classify(false, cb.lockPosition);
                                    G.util.setLockPosition(cb);
                                }
                            }
                        });
                    }
                }
            }
        });
        
        this.cmp.dataElementGroup = new Ext.form.ComboBox({
            fieldLabel: G.i18n.dataelement_group,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: G.stores.dataElementGroup,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.cmp.dataElement.clearValue();
                        this.stores.dataElementsByGroup.setBaseParam('dataElementGroupId', cb.getValue());
                        this.stores.dataElementsByGroup.load();
                    }
                }
            }
        });
        
        this.cmp.dataElement = new Ext.form.ComboBox({
            fieldLabel: G.i18n.dataelement,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'shortName',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: this.stores.dataElementsByGroup,
            lockPosition: false,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        if (G.util.setCurrentValue.call(this, cb, this.cmp.mapview)) {
                            return;
                        }
                        
                        this.updateValues = true;
                        Ext.Ajax.request({
                            url: G.conf.path_mapping + 'getMapLegendSetByDataElement' + G.conf.type,
                            params: {dataElementId: cb.getValue()},
                            scope: this,
                            success: function(r) {
                                var mapLegendSet = Ext.util.JSON.decode(r.responseText).mapLegendSet[0];
                                if (mapLegendSet.id) {
                                    this.legend.value = G.conf.map_legendset_type_predefined;
                                    this.prepareMapViewLegend();
                                    
                                    function load() {
                                        this.cmp.mapLegendSet.setValue(mapLegendSet.id);
                                        this.applyPredefinedLegend();
                                    }
                                    
                                    if (!G.stores.predefinedMapLegendSet.isLoaded) {
                                        G.stores.predefinedMapLegendSet.load({scope: this, callback: function() {
                                            load.call(this);
                                        }});
                                    }
                                    else {
                                        load.call(this);
                                    }
                                }
                                else {
                                    this.legend.value = G.conf.map_legendset_type_automatic;
                                    this.prepareMapViewLegend();
                                    this.classify(false, cb.lockPosition);
                                    G.util.setLockPosition(cb);
                                }
                            }
                        });
                    }
                }
            }
        });
        
        this.cmp.periodType = new Ext.form.ComboBox({
            fieldLabel: G.i18n.period_type,
            typeAhead: true,
            editable: false,
            valueField: 'name',
            displayField: 'displayName',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: G.stores.periodType,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.cmp.period.clearValue();
                        this.stores.periodsByType.setBaseParam('name', cb.getValue());
                        this.stores.periodsByType.load();
                    }
                }
            }
        });
        
        this.cmp.period = new Ext.form.ComboBox({
            fieldLabel: G.i18n.period,
            typeAhead: true,
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            width: G.conf.combo_width,
            store: this.stores.periodsByType,
            lockPosition: false,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        if (G.util.setCurrentValue.call(this, cb, this.cmp.mapview)) {
                            return;
                        }
                        
                        this.updateValues = true;
                        this.classify(false, cb.lockPosition);                        
                        G.util.setLockPosition(cb);
                    }
                }
            }
        });
        
        this.cmp.mapLegendType = new Ext.form.ComboBox({
            editable: false,
            valueField: 'value',
            displayField: 'text',
            mode: 'local',
            fieldLabel: G.i18n.legend_type,
            value: this.legend.value,
            triggerAction: 'all',
            width: G.conf.combo_width,
            store: new Ext.data.ArrayStore({
                fields: ['value', 'text'],
                data: [
                    [G.conf.map_legendset_type_automatic, G.i18n.automatic],
                    [G.conf.map_legendset_type_predefined, G.i18n.predefined]
                ]
            }),
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        if (cb.getValue() == G.conf.map_legendset_type_predefined && cb.getValue() != this.legend.value) {
                            this.legend.value = G.conf.map_legendset_type_predefined;
                            this.prepareMapViewLegend();
                            
                            if (this.cmp.mapLegendSet.getValue()) {
                                this.applyPredefinedLegend();
                            }
                        }
                        else if (cb.getValue() == G.conf.map_legendset_type_automatic && cb.getValue() != this.legend.value) {
                            this.legend.value = G.conf.map_legendset_type_automatic;
                            this.prepareMapViewLegend();                            
                            this.classify(false, true);
                        }
                    }
                }
            }
        });
        
        this.cmp.mapLegendSet = new Ext.form.ComboBox({
            editable: false,
            valueField: 'id',
            displayField: 'name',
            mode: 'remote',
            fieldLabel: G.i18n.legendset,
            triggerAction: 'all',
            width: G.conf.combo_width,
            hidden: true,
            store: G.stores.predefinedColorMapLegendSet,
            listeners: {
                'select': {
                    scope: this,
                    fn: function() {
                        this.cmp.mapview.clearValue();
                        this.applyPredefinedLegend();
                    }
                }
            }
        });
                
        this.cmp.method = new Ext.form.ComboBox({
            fieldLabel: G.i18n.method,
            editable: false,
            valueField: 'value',
            displayField: 'text',
            mode: 'local',
            value: this.legend.method,
            triggerAction: 'all',
            width: G.conf.combo_width,
            store: new Ext.data.ArrayStore({
                fields: ['value', 'text'],
                data: [
                    [2, G.i18n.equal_intervals],
                    [3, G.i18n.equal_group_count],
                    [1, G.i18n.fixed_intervals]
                ]
            }),
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.cmp.mapview.clearValue();
                        if (cb.getValue() == G.conf.classify_with_bounds && cb.getValue() != this.legend.method) {
                            this.legend.method = G.conf.classify_with_bounds;
                            this.prepareMapViewLegend();
                        }
                        else if (cb.getValue() != this.legend.method) {
                            this.legend.method = cb.getValue();
                            this.prepareMapViewLegend();
                            this.classify(false, true);
                        }
                    }
                }
            }
        });
        
        this.cmp.bounds = new Ext.form.ComboBox({
            fieldLabel: G.i18n.bounds,
            width: G.conf.combo_width,
            hidden: true,
            listeners: {
                'change': {
                    scope: this,
                    fn: function() {
                        this.classify(false, true);
                    }
                }  
            }
        });
        
        this.cmp.classes = new Ext.form.ComboBox({
            fieldLabel: G.i18n.classes,
            editable: false,
            valueField: 'value',
            displayField: 'value',
            mode: 'local',
            value: this.legend.classes,
            triggerAction: 'all',
            width: G.conf.combo_width,
            store: new Ext.data.ArrayStore({
                fields: ['value'],
                data: [[1], [2], [3], [4], [5], [6], [7]]
            }),
            listeners: {
                'select': {
                    scope: this,
                    fn: function(cb) {
                        this.cmp.mapview.clearValue();
                        
                        if (cb.getValue() != this.legend.classes) {
                            this.legend.classes = cb.getValue();
                            this.classify(false, true);
                        }
                    }
                }
            }
        });

        this.cmp.startColor = new Ext.ux.ColorField({
            fieldLabel: G.i18n.low_color,
            allowBlank: false,
            width: 73,
            value: "#FF0000",
            listeners: {
                'select': {
                    scope: this,
                    fn: function() {
                        this.classify(false, true);
                    }
                }
            }
        });
        
        this.cmp.endColor = new Ext.ux.ColorField({
            allowBlank: false,
            width: 73,
            value: "#FFFF00",
            listeners: {
                'select': {
                    scope: this,
                    fn: function() {
                        this.classify(false, true);
                    }
                }
            }
        });
        
        this.cmp.radiusLow = new Ext.form.NumberField({
            fieldLabel: G.i18n.low_point_size,
            width: 73,
            allowDecimals: false,
            allowNegative: false,
            minValue: 1,
            value: 5,
            listeners: {
                'change': {
                    scope: this,
                    fn: function() {
                        this.classify(false, true);
                    }
                }
            }
        });
        
        this.cmp.radiusHigh = new Ext.form.NumberField({
            fieldLabel: G.i18n.high_point_size,
            width: 73,
            allowDecimals: false,
            allowNegative: false,
            minValue: 1,
            value: 20,
            listeners: {
                'change': {
                    scope: this,
                    fn: function() {
                        this.classify(false, true);
                    }
                }
            }
        });
        
        this.cmp.level = new Ext.form.ComboBox({
            fieldLabel: G.i18n.level,
            editable: false,
            valueField: 'level',
            displayField: 'name',
            mode: 'remote',
            forceSelection: true,
            triggerAction: 'all',
            selectOnFocus: true,
            fieldLabel: G.i18n.level,
            width: G.conf.combo_width,
            store: G.stores.organisationUnitLevel,
            listeners: {
                'select': {
                    scope: this,
                    fn: function(c) {
                        this.requireUpdate = true;
                        this.formValidation.validateForm.call(this);
                        this.organisationUnitSelection.setValues(null, null, null, c.getValue(), c.getRawValue());
                    }
                }
            }
        });
        
        this.cmp.parent = new Ext.tree.TreePanel({
            cls: 'treepanel-layer-border',
            autoScroll: true,
            lines: false,
            loader: new Ext.tree.TreeLoader({
                dataUrl: G.conf.path_mapping + 'getOrganisationUnitChildren' + G.conf.type
            }),
            root: {
                id: G.system.rootNode.id,
                text: G.system.rootNode.name,
                level: G.system.rootNode.level,
                hasChildrenWithCoordinates: G.system.rootNode.hasChildrenWithCoordinates,
                nodeType: 'async',
                draggable: false,
                expanded: true
            },
            widget: this,
            isLoaded: false,
            reset: function() {
                if (this.getSelectionModel().getSelectedNode()) {
                    this.getSelectionModel().getSelectedNode().unselect();
                }                
                this.collapseAll();
                this.getRootNode().expand();
                this.widget.window.cmp.apply.disable();
            },
            listeners: {
                'click': {
                    scope: this,
                    fn: function(n) {
                        var tree = n.getOwnerTree();
                        tree.selectedNode = n;
                        this.requireUpdate = true;
                        this.formValidation.validateForm.call(this);
                        this.organisationUnitSelection.setValues(n.attributes.id, n.attributes.text, n.attributes.level);
                    }
                }
            }
        });
        
        this.cmp.colorPanel = new Ext.Panel({
			layout: 'hbox',
			style: 'padding-bottom:4px',
			items: [
				{
					html: 'Low / high color:',
					width: 107,
					style: 'padding:3px 0 0 4px; color:#444'
				},
				this.cmp.startColor,
				{
					style: 'width:4px'
				},
				this.cmp.endColor,
				{
					style: 'height:4px'
				}
			]
		});
		
		this.cmp.radiusPanel = new Ext.Panel({
			layout: 'hbox',
			items: [
				{
					html: 'Low / high radius:',
					width: 107,
					style: 'padding:3px 0 0 4px; color:#444'
				},
				this.cmp.radiusLow,
				{
					style: 'width:4px'
				},
				this.cmp.radiusHigh
			]
		});
    },
    
    addItems: function() {
        
        this.items = [
            {
                xtype: 'panel',
                layout: 'column',
                width: 570,
                style: 'padding-bottom:3px',
                items: [
                    {
                        xtype: 'form',
                        width: 270,
                        items: [
                            { html: '<div class="window-info">' + G.i18n.data_options + '</div>' },
                            this.cmp.mapview,
                            { html: '<div class="thematic-br">' },
                            this.cmp.mapValueType,
                            this.cmp.indicatorGroup,
                            this.cmp.indicator,
                            this.cmp.dataElementGroup,
                            this.cmp.dataElement,
                            this.cmp.periodType,
                            this.cmp.period,
                            { html: '<div class="thematic-br">' },
                            { html: '<div class="window-info">' + G.i18n.legend_options + '</div>' },
                            this.cmp.mapLegendType,
                            this.cmp.mapLegendSet,
                            this.cmp.method,
                            this.cmp.bounds,
                            this.cmp.classes,
                            this.cmp.colorPanel,
                            this.cmp.radiusPanel
                        ]
                    },
                    {
                        xtype: 'panel',
                        width: 270,
                        bodyStyle: 'padding:0 0 0 8px;',
                        items: [
                            { html: '<div class="window-info">' + G.i18n.organisation_unit_level + '</div>' },                            
                            {
                                xtype: 'panel',
                                layout: 'form',
                                items: [
                                    this.cmp.level
                                ]
                            },                            
                            { html: '<div class="thematic-br"></div>' },                            
                            { html: '<div class="window-info">' + G.i18n.parent_organisation_unit + '</div>' },
                            this.cmp.parent
                        ]
                    }
                ]
            }
        ];
    },
    
    createSelectFeatures: function() {
        var scope = this;
        
        var onHoverSelect = function onHoverSelect(feature) {
            if (feature.attributes.fixedName) {
                document.getElementById('featuredatatext').innerHTML =
                    '<div style="' + G.conf.feature_data_style_name + '">' + feature.attributes.fixedName + '</div>' +
                    '<div style="' + G.conf.feature_data_style_value + '">' + feature.attributes.value + '</div>';
            }
            else {
                document.getElementById('featuredatatext').innerHTML = '';
            }
        };
        
        var onHoverUnselect = function onHoverUnselect(feature) {
            if (feature.attributes.name) {
                document.getElementById('featuredatatext').innerHTML = 
                    '<div style="' + G.conf.feature_data_style_empty + '">' + G.i18n.no_feature_selected + '</div>';
            }
            else {
                document.getElementById('featuredatatext').innerHTML = '';
            }
        };
        
        var onClickSelect = function onClickSelect(feature) {
						
			function drill() {
				if (G.vars.locateFeatureWindow) {
					G.vars.locateFeatureWindow.destroy();
				}
						 
				scope.updateValues = true;
				scope.isDrillDown = true;
				
				function organisationUnitLevelCallback() {
					this.organisationUnitSelection.setValuesOnDrillDown(feature.attributes.id, feature.attributes.name);
					
					this.cmp.parent.reset();
					this.cmp.parent.selectedNode = {attributes: {
						id: this.organisationUnitSelection.parent.id,
						text: this.organisationUnitSelection.parent.name,
						level: this.organisationUnitSelection.parent.level
					}};
					
					this.cmp.level.setValue(this.organisationUnitSelection.level.level);
					this.loadGeoJson();
				}
				
				if (G.stores.organisationUnitLevel.isLoaded) {
					organisationUnitLevelCallback.call(scope);
				}
				else {
					G.stores.organisationUnitLevel.load({scope: scope, callback: function() {
						organisationUnitLevelCallback.call(this);
					}});
				}
			}
			
            if (feature.geometry.CLASS_NAME == G.conf.map_feature_type_point_class_name) {
                if (scope.featureOptions.menu) {
                    scope.featureOptions.menu.destroy();
                }
                
                scope.featureOptions.menu = new Ext.menu.Menu({
                    showInfo: function() {
                        Ext.Ajax.request({
                            url: G.conf.path_mapping + 'getFacilityInfo' + G.conf.type,
                            params: {id: feature.attributes.id},
                            success: function(r) {
                                var ou = Ext.util.JSON.decode(r.responseText);
                                
                                if (scope.featureOptions.info) {
                                    scope.featureOptions.info.destroy();
                                }
                                
                                scope.featureOptions.info = new Ext.Window({
                                    title: '<span class="window-information-title">Facility information sheet</span>',
                                    layout: 'table',
                                    width: G.conf.window_width + 178,
                                    height: G.util.getMultiSelectHeight() + 100,
                                    bodyStyle: 'background-color:#fff',
                                    defaults: {
                                        bodyStyle: 'vertical-align:top',
                                        labelSeparator: G.conf.labelseparator,
                                        emptyText: G.conf.emptytext
                                    },
                                    layoutConfig: {
                                        columns: 2
                                    },
                                    items: [
                                        {
                                            xtype: 'panel',
                                            layout: 'anchor',
                                            bodyStyle: 'padding:8px 4px 8px 8px',
                                            width: 160,
                                            items: [
                                                {html: '<div class="window-info">' + G.i18n.name + '<p style="font-weight:normal">' + feature.attributes.name + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.type + '<p style="font-weight:normal">' + ou.ty + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.code + '<p style="font-weight:normal">' + ou.co + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.address + '<p style="font-weight:normal">' + ou.ad + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.contact_person + '<p style="font-weight:normal">' + ou.cp + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.email + '<p style="font-weight:normal">' + ou.em + '</p></div>'},
                                                {html: '<div class="window-info">' + G.i18n.phone_number + '<p style="font-weight:normal">' + ou.pn + '</p></div>'}
                                            ]
                                        },
                                        {
                                            xtype: 'form',
                                            bodyStyle: 'padding:8px 8px 8px 4px',
                                            width: G.conf.window_width + 20,
                                            labelWidth: G.conf.label_width,
                                            items: [
                                                {html: '<div class="window-info">' + G.i18n.infrastructural_data + '</div>'},
                                                {
                                                    xtype: 'combo',
                                                    name: 'period',
                                                    fieldLabel: G.i18n.period,
                                                    typeAhead: true,
                                                    editable: false,
                                                    valueField: 'id',
                                                    displayField: 'name',
                                                    mode: 'remote',
                                                    forceSelection: true,
                                                    triggerAction: 'all',
                                                    selectOnFocus: true,
                                                    width: G.conf.combo_width,
                                                    store: G.stores.infrastructuralPeriodsByType,
                                                    lockPosition: false,
                                                    listeners: {
                                                        'select': function(cb) {
                                                            scope.infrastructuralPeriod = cb.getValue();
                                                            scope.stores.infrastructuralDataElementMapValue.setBaseParam('periodId', cb.getValue());
                                                            scope.stores.infrastructuralDataElementMapValue.setBaseParam('organisationUnitId', feature.attributes.id);
                                                            scope.stores.infrastructuralDataElementMapValue.load();
                                                        }
                                                    }
                                                },
                                                {html: '<div style="padding:4px 0 0 0"></div>'},
                                                {
                                                    xtype: 'grid',
                                                    height: G.util.getMultiSelectHeight(),
                                                    width: 242,
                                                    cm: new Ext.grid.ColumnModel({
                                                        columns: [
                                                            {id: 'dataElementName', header: 'Data element', dataIndex: 'dataElementName', sortable: true, width: 150},
                                                            {id: 'value', header: 'Value', dataIndex: 'value', sortable: true, width: 50}
                                                        ]
                                                    }),
                                                    disableSelection: true,
                                                    viewConfig: {forceFit: true},
                                                    store: scope.stores.infrastructuralDataElementMapValue
                                                }
                                            ]
                                        }
                                    ]
                                });
            
                                if (scope.infrastructuralPeriod) {
                                    scope.featureOptions.info.find('name', 'period')[0].setValue(scope.infrastructuralPeriod);
                                    scope.stores.infrastructuralDataElementMapValue.setBaseParam('periodId', scope.infrastructuralPeriod);
                                    scope.stores.infrastructuralDataElementMapValue.setBaseParam('organisationUnitId', feature.attributes.id);
                                    scope.stores.infrastructuralDataElementMapValue.load();
                                }
                                scope.featureOptions.info.setPagePosition(Ext.getCmp('east').x - (scope.featureOptions.info.width + 15), Ext.getCmp('center').y + 41);
                                scope.featureOptions.info.show();
                                scope.featureOptions.menu.destroy();
                            }
                        });
                    },
                    showRelocate: function() {
                        if (scope.featureOptions.coordinate) {
                            scope.featureOptions.coordinate.destroy();
                        }
                        
                        scope.featureOptions.coordinate = new Ext.Window({
                            title: '<span class="window-relocate-title">' + feature.attributes.name + '</span>',
							bodyStyle: 'padding:8px; background-color:#fff',
                            layout: 'fit',
                            width: G.conf.window_width,
                            items: [
                                {
                                    xtype: 'panel',
                                    items: [
                                        {html: G.i18n.select_new_location_on_map}
                                    ]
                                }
                            ],
                            bbar: [
                                '->',
                                {
                                    xtype: 'button',
                                    iconCls: 'icon-cancel',
                                    hideLabel: true,
                                    text: G.i18n.cancel,
                                    handler: function() {
                                        G.vars.relocate.active = false;
                                        scope.featureOptions.coordinate.destroy();
                                        document.getElementById('OpenLayers.Map_3_OpenLayers_ViewPort').style.cursor = 'auto';
                                    }
                                }
                            ],
                            listeners: {
                                'close': function() {
                                    G.vars.relocate.active = false;
                                    document.getElementById('OpenLayers.Map_3_OpenLayers_ViewPort').style.cursor = 'auto';
                                }
                            }
                        });
                        scope.featureOptions.coordinate.setPagePosition(Ext.getCmp('east').x - (scope.featureOptions.coordinate.width + 15), Ext.getCmp('center').y + 41);
                        scope.featureOptions.coordinate.show();                        
                    },
                    items: [
                        {
                            text: G.i18n.show_information_sheet,
                            iconCls: 'menu-featureoptions-info',
                            handler: function(item) {
                                if (G.stores.infrastructuralPeriodsByType.isLoaded) {
                                    item.parentMenu.showInfo();
                                }
                                else {
                                    G.stores.infrastructuralPeriodsByType.setBaseParam('name', G.system.infrastructuralPeriodType);
                                    G.stores.infrastructuralPeriodsByType.load({callback: function() {
                                        item.parentMenu.showInfo();
                                    }});
                                }
                            }
                        },
                        {
                            text: G.i18n.relocate,
                            iconCls: 'menu-featureoptions-relocate',
                            disabled: !G.user.isAdmin,
                            handler: function(item) {
                                G.vars.relocate.active = true;
                                G.vars.relocate.widget = scope;
                                G.vars.relocate.feature = feature;
                                document.getElementById('OpenLayers.Map_3_OpenLayers_ViewPort').style.cursor = 'crosshair';
                                item.parentMenu.showRelocate();
                            }
                        }
                    ]
                });
                
                if (feature.attributes.hcwc) {
					scope.featureOptions.menu.add({
						text: 'Drill down',
						iconCls: 'menu-featureoptions-drilldown',
						scope: this,
						handler: function() {
							drill.call(this);
						}
					});
				}
                
                scope.featureOptions.menu.showAt([G.vars.mouseMove.x, G.vars.mouseMove.y]);
            }
            else {
                if (feature.attributes.hcwc) {
					drill.call(this);
                }
                else {
                    Ext.message.msg(false, G.i18n.no_coordinates_found);
                }
            }
        };
        
        this.selectFeatures = new OpenLayers.Control.newSelectFeature(
            this.layer, {
                onHoverSelect: onHoverSelect,
                onHoverUnselect: onHoverUnselect,
                onClickSelect: onClickSelect
            }
        );
        
        G.vars.map.addControl(this.selectFeatures);
        this.selectFeatures.activate();
    },
    
    prepareMapViewValueType: function() {
        var obj = {};
        if (this.valueType.isIndicator()) {
            this.cmp.indicatorGroup.show();
            this.cmp.indicator.show();
            this.cmp.dataElementGroup.hide();
            this.cmp.dataElement.hide();
            obj.components = {
                valueTypeGroup: this.cmp.indicatorGroup,
                valueType: this.cmp.indicator
            };
            obj.stores = {
                valueTypeGroup: G.stores.indicatorGroup,
                valueType: this.stores.indicatorsByGroup
            };
            obj.mapView = {
                valueTypeGroup: 'indicatorGroupId',
                valueType: 'indicatorId'
            };
        }
        else if (this.valueType.isDataElement()) {
            this.cmp.indicatorGroup.hide();
            this.cmp.indicator.hide();
            this.cmp.dataElementGroup.show();
            this.cmp.dataElement.show();
            obj.components = {
                valueTypeGroup: this.cmp.dataElementGroup,
                valueType: this.cmp.dataElement
            };
            obj.stores = {
                valueTypeGroup: G.stores.dataElementGroup,
                valueType: this.stores.dataElementsByGroup
            };
            obj.mapView = {
                valueTypeGroup: 'dataElementGroupId',
                valueType: 'dataElementId'
            };
        }
        return obj;
    },
    
    prepareMapViewPeriod: function() {
        var obj = {};        
        this.cmp.periodType.show();
        this.cmp.period.show();
        obj.components = {
            c1: this.cmp.periodType,
            c2: this.cmp.period
        };
        obj.stores = {
            c1: G.stores.periodType,
            c2: this.stores.periodsByType
        };
        obj.mapView = {
            c1: 'periodTypeId',
            c2: 'periodId'
        };
        return obj;
    },
    
    prepareMapViewLegend: function() {
        this.cmp.mapLegendType.setValue(this.legend.value);
        
        if (this.legend.value == G.conf.map_legendset_type_automatic) {
            this.cmp.method.show();
            this.cmp.colorPanel.show();
            this.cmp.mapLegendSet.hide();
            
            if (this.legend.method == G.conf.classify_with_bounds) {
                this.cmp.classes.hide();
                this.cmp.bounds.show();
            }
            else {
                this.cmp.classes.show();
                this.cmp.bounds.hide();
            }                
        }
        else if (this.legend.value == G.conf.map_legendset_type_predefined) {
            this.cmp.method.hide();
            this.cmp.classes.hide();
            this.cmp.bounds.hide();
            this.cmp.colorPanel.hide();
            this.cmp.mapLegendSet.show();
        }
    },
    
    setMapView: function() {
        var obj = this.prepareMapViewValueType();
        
        function valueTypeGroupStoreCallback() {
            obj.components.valueTypeGroup.setValue(this.mapView[obj.mapView.valueTypeGroup]);
            obj.stores.valueType.setBaseParam(obj.mapView.valueTypeGroup, obj.components.valueTypeGroup.getValue());
            obj.stores.valueType.load({scope: this, callback: function() {
                obj.components.valueType.setValue(this.mapView[obj.mapView.valueType]);
                obj.components.valueType.currentValue = this.mapView[obj.mapView.valueType];
                
                obj = this.prepareMapViewPeriod();
                if (obj.stores.c1.isLoaded) {
                    dateTypeGroupStoreCallback.call(this);
                }
                else {
                    obj.stores.c1.load({scope: this, callback: function() {
                        dateTypeGroupStoreCallback.call(this);
                    }});
                }
            }});
        }
        
        function dateTypeGroupStoreCallback() {
            obj.components.c1.setValue(this.mapView[obj.mapView.c1]);
            
            obj.stores.c2.setBaseParam('name', this.mapView[obj.mapView.c1]);
            obj.stores.c2.load({scope: this, callback: function() {
                obj.components.c2.setValue(this.mapView[obj.mapView.c2]);
                obj.components.c2.currentValue = this.mapView[obj.mapView.c2];
                obj.components.c2.lockPosition = true;
                
                this.setMapViewLegend();
            }});
        }

        if (obj.stores.valueTypeGroup.isLoaded) {
            valueTypeGroupStoreCallback.call(this);
        }
        else {
            obj.stores.valueTypeGroup.load({scope: this, callback: function() {
                valueTypeGroupStoreCallback.call(this);
            }});
        }
    },
    
    setMapViewLegend: function() {
        this.prepareMapViewLegend();

        function predefinedMapLegendSetStoreCallback() {
            this.cmp.mapLegendSet.setValue(this.mapView.mapLegendSetId);
            this.applyPredefinedLegend(true);
        }
        
        this.cmp.radiusLow.setValue(this.mapView.radiusLow || G.conf.defaultLowRadius);
        this.cmp.radiusHigh.setValue(this.mapView.radiusHigh || G.conf.defaultHighRadius);
        
        if (this.legend.value == G.conf.map_legendset_type_automatic) {
            this.cmp.method.setValue(this.mapView.method);
            this.cmp.startColor.setValue(this.mapView.colorLow);
            this.cmp.endColor.setValue(this.mapView.colorHigh);

            if (this.legend.method == G.conf.classify_with_bounds) {
                this.cmp.bounds.setValue(this.mapView.bounds);
            }
            else {
                this.cmp.classes.setValue(this.mapView.classes);
            }

            this.setMapViewMap();
        }
        else if (this.legend.value == G.conf.map_legendset_type_predefined) {
            if (G.stores.predefinedColorMapLegendSet.isLoaded) {
                predefinedMapLegendSetStoreCallback.call(this);
            }
            else {
                G.stores.predefinedColorMapLegendSet.load({scope: this, callback: function() {
                    predefinedMapLegendSetStoreCallback.call(this);
                }});
            }
        }
    },
    
    setMapViewMap: function() {
        this.organisationUnitSelection.setValues(this.mapView.parentOrganisationUnitId, this.mapView.parentOrganisationUnitName,
            this.mapView.parentOrganisationUnitLevel, this.mapView.organisationUnitLevel, this.mapView.organisationUnitLevelName);
            
        this.cmp.parent.reset();
        
        this.cmp.parent.selectedNode = {attributes: {
            id: this.mapView.parentOrganisationUnitId,
            text: this.mapView.parentOrganisationUnitName,
            level: this.mapView.parentOrganisationUnitLevel
        }};
            
        G.stores.organisationUnitLevel.load({scope: this, callback: function() {
            this.cmp.level.setValue(this.mapView.organisationUnitLevel);
            G.vars.activePanel.setPoint();
            this.loadGeoJson();
        }});
    },
	
	applyPredefinedLegend: function(isMapView) {
        this.legend.value = G.conf.map_legendset_type_predefined;
		var mls = this.cmp.mapLegendSet.getValue();
		Ext.Ajax.request({
			url: G.conf.path_mapping + 'getMapLegendsByMapLegendSet' + G.conf.type,
			params: {mapLegendSetId: mls},
            scope: this,
			success: function(r) {
				var mapLegends = Ext.util.JSON.decode(r.responseText).mapLegends,
                    colors = [],
                    bounds = [],
                    names = [];
				for (var i = 0; i < mapLegends.length; i++) {
					if (bounds[bounds.length-1] != mapLegends[i].startValue) {
						if (bounds.length !== 0) {
							colors.push(new mapfish.ColorRgb(240,240,240));
                            names.push('');
						}
						bounds.push(mapLegends[i].startValue);
					}
					colors.push(new mapfish.ColorRgb());
					colors[colors.length-1].setFromHex(mapLegends[i].color);
                    names.push(mapLegends[i].name);
					bounds.push(mapLegends[i].endValue);
				}

				this.colorInterpolation = colors;
				this.bounds = bounds;
                this.legendNames = names;
                
                if (isMapView) {
                    this.setMapViewMap();
                }
                else {
                    this.classify(false, true);
                }
			}
		});
	},
    
    formValidation: {
        validateForm: function() {
            if (this.cmp.mapValueType.getValue() == G.conf.map_value_type_indicator) {
                if (!this.cmp.indicator.getValue()) {
                    this.window.cmp.apply.disable();
                    return false;
                }
            }
            else if (this.cmp.mapValueType.getValue() == G.conf.map_value_type_dataelement) {
                if (!this.cmp.dataElement.getValue()) {
                    this.window.cmp.apply.disable();
                    return false;
                }
            }
            
            if (!this.cmp.period.getValue()) {
                this.window.cmp.apply.disable();
                return false;
            }

            if (!this.cmp.parent.selectedNode || !this.cmp.level.getValue()) {
                this.window.cmp.apply.disable();
                return false;
            }
            
            if (this.cmp.parent.selectedNode.attributes.level > this.cmp.level.getValue()) {
                this.window.cmp.apply.disable();
                return false;
            }

            if (this.cmp.mapLegendType.getValue() == G.conf.map_legendset_type_automatic) {
                if (this.cmp.method.getValue() == G.conf.classify_with_bounds) {
                    if (!this.cmp.bounds.getValue()) {
                        this.window.cmp.apply.disable();
                        return false;
                    }
                }
            }
            else if (this.cmp.mapLegendType.getValue() == G.conf.map_legendset_type_predefined) {
                if (!this.cmp.mapLegendSet.getValue()) {
                    this.window.cmp.apply.disable();
                    return false;
                }
            }
            
            if (!this.cmp.radiusLow.getValue() || !this.cmp.radiusHigh.getValue()) {
                this.window.cmp.apply.disable();
                return false;
            }
            
            if (this.requireUpdate) {
                if (this.window.isUpdate) {
                    this.window.cmp.apply.disable();
                    this.requireUpdate = false;
                    this.window.isUpdate = false;
                }
                else {
                    this.window.cmp.apply.enable();
                }
            }
            
            return true;
        }
    },
    
    formValues: {
		getAllValues: function() {
			return {
                mapValueType: this.cmp.mapValueType.getValue(),
                indicatorGroupId: this.valueType.isIndicator() ? this.cmp.indicatorGroup.getValue() : null,
                indicatorId: this.valueType.isIndicator() ? this.cmp.indicator.getValue() : null,
				indicatorName: this.valueType.isIndicator() ? this.cmp.indicator.getRawValue() : null,
                dataElementGroupId: this.valueType.isDataElement() ? this.cmp.dataElementGroup.getValue() : null,
                dataElementId: this.valueType.isDataElement() ? this.cmp.dataElement.getValue() : null,
				dataElementName: this.valueType.isDataElement() ? this.cmp.dataElement.getRawValue() : null,
                periodTypeId: this.cmp.periodType.getValue(),
                periodId: this.cmp.period.getValue(),
                periodName: this.cmp.period.getRawValue(),
                parentOrganisationUnitId: this.organisationUnitSelection.parent.id,
                parentOrganisationUnitLevel: this.organisationUnitSelection.parent.level,
                parentOrganisationUnitName: this.organisationUnitSelection.parent.name,
                organisationUnitLevel: this.organisationUnitSelection.level.level,
                organisationUnitLevelName: this.organisationUnitSelection.level.name,
                mapLegendType: this.cmp.mapLegendType.getValue(),
                method: this.legend.value == G.conf.map_legendset_type_automatic ? this.cmp.method.getValue() : null,
                classes: this.legend.value == G.conf.map_legendset_type_automatic ? this.cmp.classes.getValue() : null,
                bounds: this.legend.value == G.conf.map_legendset_type_automatic && this.legend.method == G.conf.classify_with_bounds ? this.cmp.bounds.getValue() : null,
                colorLow: this.legend.value == G.conf.map_legendset_type_automatic ? this.cmp.startColor.getValue() : null,
                colorHigh: this.legend.value == G.conf.map_legendset_type_automatic ? this.cmp.endColor.getValue() : null,
                mapLegendSetId: this.legend.value == G.conf.map_legendset_type_predefined ? this.cmp.mapLegendSet.getValue() : null,
				radiusLow: this.cmp.radiusLow.getValue(),
				radiusHigh: this.cmp.radiusHigh.getValue(),
                longitude: G.vars.map.getCenter().lon,
                latitude: G.vars.map.getCenter().lat,
                zoom: parseFloat(G.vars.map.getZoom())
			};
		},
        
        getLegendInfo: function() {
            return {
                name: this.valueType.isIndicator() ? this.cmp.indicator.getRawValue() : this.cmp.dataElement.getRawValue(),
                time: this.cmp.period.getRawValue(),
                map: this.organisationUnitSelection.level.name + ' / ' + this.organisationUnitSelection.parent.name
            };
        },
        
        getImageExportValues: function() {
			return {
				mapValueTypeValue: this.cmp.mapValueType.getValue() == G.conf.map_value_type_indicator ?
					this.cmp.indicator.getRawValue() : this.cmp.dataElement.getRawValue(),
				dateValue: this.cmp.period.getRawValue()
			};
		},
        
        clearForm: function(clearLayer) {
            this.cmp.mapview.clearValue();
            
            this.cmp.mapValueType.setValue(G.conf.map_value_type_indicator);
            this.valueType.setIndicator();
            this.prepareMapViewValueType();
            this.cmp.indicatorGroup.clearValue();
            this.cmp.indicator.clearValue();
            this.cmp.dataElementGroup.clearValue();
            this.cmp.dataElement.clearValue();
            
            this.prepareMapViewPeriod();
            this.cmp.periodType.clearValue();
            this.cmp.period.clearValue();
            
            this.cmp.level.clearValue();
            this.cmp.parent.reset();
            
            this.legend.reset();
            this.prepareMapViewLegend();
            this.cmp.method.setValue(this.legend.method);
            this.cmp.classes.setValue(this.legend.classes);
            this.cmp.bounds.reset();
            
            this.cmp.startColor.setValue('#FF0000');
            this.cmp.endColor.setValue('#FFFF00');
            
            this.cmp.radiusLow.reset();
            this.cmp.radiusHigh.reset();
            
            this.window.cmp.apply.disable();
            
            if (clearLayer) {            
                document.getElementById(this.legendDiv).innerHTML = '';                
                this.layer.destroyFeatures();
                this.layer.setVisibility(false);
            }
        }
	},
    
    loadGeoJson: function() {
        G.vars.mask.msg = G.i18n.loading;
        G.vars.mask.show();
        G.vars.activeWidget = this;
        this.updateValues = true;
        
        var url = G.conf.path_mapping + 'getGeoJson.action?' +
            'parentId=' + this.organisationUnitSelection.parent.id +
            '&level=' + this.organisationUnitSelection.level.level;
        this.setUrl(url);
    },

    classify: function(exception, lockPosition, loaded) {
        if (this.formValidation.validateForm.apply(this, [exception])) {
            if (!this.layer.features.length && !loaded) {
                this.loadGeoJson();
            }
            
            G.vars.mask.msg = G.i18n.loading;
            G.vars.mask.show();
            
            G.vars.lockPosition = lockPosition;
            
            if (this.mapView) {
                if (this.mapView.longitude && this.mapView.latitude && this.mapView.zoom) {
                    var point = G.util.getTransformedPointByXY(this.mapView.longitude, this.mapView.latitude);
                    G.vars.map.setCenter(new OpenLayers.LonLat(point.x, point.y), this.mapView.zoom);
                    G.vars.lockPosition = true;
                }
                this.mapView = false;
            }
            
            if (this.updateValues) {
                var dataUrl = this.valueType.isIndicator() ? 'getIndicatorMapValues' : 'getDataElementMapValues';
                var params = {
                    id: this.valueType.isIndicator() ? this.cmp.indicator.getValue() : this.cmp.dataElement.getValue(),
                    periodId: this.cmp.period.getValue(),
                    parentId: this.organisationUnitSelection.parent.id,
                    level: this.organisationUnitSelection.level.level
                };
                
                Ext.Ajax.request({
                    url: G.conf.path_mapping + dataUrl + G.conf.type,
                    params: params,
                    disableCaching: false,
                    scope: this,
                    success: function(r) {
                        var mapvalues = G.util.mapValueDecode(r);
                        this.layer.features = this.featureStorage.slice(0);
                        
                        if (mapvalues.length === 0) {
                            Ext.message.msg(false, G.i18n.current_selection_no_data);
                            G.vars.mask.hide();
                            return;
                        }
                        
                        for (var i = 0; i < this.layer.features.length; i++) {
                            this.layer.features[i].attributes.value = 0;
                            for (var j = 0; j < mapvalues.length; j++) {
                                if (this.layer.features[i].attributes.id == mapvalues[j].oi) {
                                    this.layer.features[i].attributes.value = parseFloat(mapvalues[j].v);
                                    break;
                                }
                            }
                        }
                        
                        this.updateValues = false;
                        this.applyValues();
                    }
                });
            }
            else {
                this.applyValues();
            }
        }
    },
    
    applyValues: function() {
        for (var i = 0; i < this.layer.features.length; i++) {
            var f = this.layer.features[i];
            if (!f.attributes.value) {
                this.layer.features.splice(i,1);
                i--;
            }
            else {
                f.attributes.labelString = f.attributes.name + ' (' + f.attributes.value + ')';
                f.attributes.fixedName = G.util.cutString(f.attributes.name, 30);
            }
        }
        if (!this.layer.features.length) {
            G.vars.mask.hide();
            Ext.message.msg(false, G.i18n.no_values_found);
            return;
        }
        
        this.button.menu.find('name','history')[0].addItem(this);
        
		var options = {
            indicator: 'value',
            method: this.cmp.method.getValue(),
            numClasses: this.cmp.classes.getValue(),
            colors: this.getColors(),
            minSize: parseInt(this.cmp.radiusLow.getValue()),
            maxSize: parseInt(this.cmp.radiusHigh.getValue())
        };
        
        G.vars.activeWidget = this;
        this.coreComp.applyClassification(options, this);
        this.classificationApplied = true;
        
        G.vars.mask.hide();
    },
    
    onRender: function(ct, position) {
        mapfish.widgets.geostat.Point.superclass.onRender.apply(this, arguments);

		var coreOptions = {
            'layer': this.layer,
            'format': this.format,
            'url': this.url,
            'requestSuccess': this.requestSuccess.createDelegate(this),
            'requestFailure': this.requestFailure.createDelegate(this),
            'featureSelection': this.featureSelection,
            'nameAttribute': this.nameAttribute,
            'legendDiv': this.legendDiv,
            'labelGenerator': this.labelGenerator
        };

        this.coreComp = new mapfish.GeoStat.Point(this.map, coreOptions);
    }
});

Ext.reg('point', mapfish.widgets.geostat.Point);
