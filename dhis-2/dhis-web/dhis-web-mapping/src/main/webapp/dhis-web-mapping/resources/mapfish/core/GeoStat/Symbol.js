/*
 * Copyright (C) 2007  Camptocamp
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
 * @requires core/GeoStat.js
 */

mapfish.GeoStat.Symbol = OpenLayers.Class(mapfish.GeoStat, {

    classification: null,

    initialize: function(map, options) {
        mapfish.GeoStat.prototype.initialize.apply(this, arguments);
    },

    updateOptions: function(newOptions) {
        this.addOptions(newOptions);
    },

    applyClassification: function(form, widget) {
        this.widget = widget;
        
        var panel = widget.cmp.group;
        G.stores.groupsByGroupSet.img = [];
        for (var i = 0, items = panel.items.items; i < items.length; i++) {
            G.stores.groupsByGroupSet.img.push(items[i].getRawValue());
        }
        
        var boundsArray = G.stores.groupsByGroupSet.data.items;
        var rules = new Array(boundsArray.length);
        for (var i = 0; i < boundsArray.length; i++) {
            var rule = new OpenLayers.Rule({                
                symbolizer: {
                    'pointRadius': 8,
                    'externalGraphic': '../resources/ext-ux/iconcombo/' + G.stores.groupsByGroupSet.img[i] + '.png'
                },                
                filter: new OpenLayers.Filter.Comparison({
                    type: OpenLayers.Filter.Comparison.EQUAL_TO,
                    property: this.indicator,
                    value: G.stores.groupsByGroupSet.data.items[i].data.name
                })
            });
            rules[i] = rule;
        }
        
        this.extendStyle(rules);
        mapfish.GeoStat.prototype.applyClassification.apply(this, arguments);
    },

    updateLegend: function() {
        if (!this.legendDiv) {
            return;
        }
        
        var info = this.widget.formValues.getLegendInfo.call(this.widget);
        this.legendDiv.update("");
        
        var element = document.createElement("div");
        element.style.height = "14px";
        element.innerHTML = info.map;
        this.legendDiv.appendChild(element);
        
        element = document.createElement("div");
        element.style.clear = "left";
        this.legendDiv.appendChild(element);
        
        element = document.createElement("div");
        element.style.width = "1px";
        element.style.height = "5px";
        this.legendDiv.appendChild(element);

        for (var i = 0; i < G.stores.groupsByGroupSet.data.items.length; i++) {
            var element = document.createElement("div");
            element.style.backgroundImage = 'url(../resources/ext-ux/iconcombo/' + G.stores.groupsByGroupSet.img[i] + '.png)';
            element.style.backgroundRepeat = 'no-repeat';
            element.style.width = "25px";
            element.style.height = "18px";
            element.style.cssFloat = "left";
            element.style.marginLeft = "3px";
            this.legendDiv.appendChild(element);

            element = document.createElement("div");
            element.innerHTML = G.stores.groupsByGroupSet.data.items[i].data.name;
            this.legendDiv.appendChild(element);

            element = document.createElement("div");
            element.style.clear = "left";
            this.legendDiv.appendChild(element);
        }
    },

    CLASS_NAME: "mapfish.GeoStat.Symbol"
});
