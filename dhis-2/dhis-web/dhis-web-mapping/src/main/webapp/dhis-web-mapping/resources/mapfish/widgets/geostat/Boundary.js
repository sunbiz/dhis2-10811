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
 * @requires core/GeoStat/Boundary.js
 * @requires core/Color.js
 */

Ext.namespace('mapfish.widgets', 'mapfish.widgets.geostat');

mapfish.widgets.geostat.Boundary = Ext.extend(Ext.Panel, {

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
    
    initComponent: function() {
    
        this.initProperties();
        
        this.createItems();
        
        this.addItems();
        
		mapfish.widgets.geostat.Boundary.superclass.initComponent.apply(this);
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
    },
    
    addItems: function() {
        
        this.items = [
			{
				xtype: 'panel',
				width: 270,
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
        ];
    },
    
    formValidation: {
        validateForm: function() {
            if (!this.cmp.parent.selectedNode || !this.cmp.level.getValue()) {
                this.window.cmp.apply.disable();
                return false;
            }
            
            if (this.cmp.parent.selectedNode.attributes.level > this.cmp.level.getValue()) {
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
                parentOrganisationUnitId: this.organisationUnitSelection.parent.id,
                parentOrganisationUnitLevel: this.organisationUnitSelection.parent.level,
                parentOrganisationUnitName: this.organisationUnitSelection.parent.name,
                organisationUnitLevel: this.organisationUnitSelection.level.level,
                organisationUnitLevelName: this.organisationUnitSelection.level.name,
                longitude: G.vars.map.getCenter().lon,
                latitude: G.vars.map.getCenter().lat,
                zoom: parseFloat(G.vars.map.getZoom())
			};
		},
        
        clearForm: function(clearLayer) {            
            this.cmp.level.clearValue();
            this.cmp.parent.reset();
            
            this.window.cmp.apply.disable();
            
            if (clearLayer) {
                this.layer.destroyFeatures();
                this.layer.setVisibility(false);
            }
        }
	},
    
    loadGeoJson: function() {
        G.vars.mask.msg = G.i18n.loading;
        G.vars.mask.show();
        G.vars.activeWidget = this;
        this.updateValues = false;
        
        var url = G.conf.path_mapping + 'getGeoJson.action?' + 
            '&parentId=' + this.organisationUnitSelection.parent.id +
            '&level=' + this.organisationUnitSelection.level.level;
        this.setUrl(url);
    },

    classify: function(exception, lockPosition, loaded) {
        if (this.formValidation.validateForm.apply(this, [exception])) {
            if (!this.layer.features.length && !loaded) {
                this.loadGeoJson();
            }
            
            this.applyValues();			
        }
    },
    
    applyValues: function() {            
		for (var i = 0; i < this.layer.features.length; i++) {
			var f = this.layer.features[i];
			f.attributes.labelString = f.attributes.name;
			f.attributes.fixedName = G.util.cutString(f.attributes.name, 30);
		}
		
		this.layer.renderer.clear();
		this.layer.redraw();
		this.layer.setVisibility(true);
		
		G.util.zoomToVisibleExtent();
		G.vars.mask.hide();
	},
    
    onRender: function(ct, position) {
        mapfish.widgets.geostat.Boundary.superclass.onRender.apply(this, arguments);

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

        this.coreComp = new mapfish.GeoStat.Boundary(this.map, coreOptions);
    }
});

Ext.reg('boundary', mapfish.widgets.geostat.Boundary);
