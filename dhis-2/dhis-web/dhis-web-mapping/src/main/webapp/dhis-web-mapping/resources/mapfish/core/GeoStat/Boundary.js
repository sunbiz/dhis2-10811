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

mapfish.GeoStat.Boundary = OpenLayers.Class(mapfish.GeoStat, {

    colors: [
        new mapfish.ColorRgb(120, 120, 0),
        new mapfish.ColorRgb(255, 0, 0)
    ],

    method: mapfish.GeoStat.Distribution.CLASSIFY_BY_QUANTILS,

    numClasses: 5,
	
	minSize: 3,
	
	maxSize: 20,
	
	minVal: null,
	
	maxVal: null,

    defaultSymbolizer: {'fillOpacity': 1},

    classification: null,

    colorInterpolation: null,
    
    widget: null,

    initialize: function(map, options) {
        mapfish.GeoStat.prototype.initialize.apply(this, arguments);
    },

    updateOptions: function(newOptions) {
        var oldOptions = OpenLayers.Util.extend({}, this.options);
        this.addOptions(newOptions);
        if (newOptions) {
            this.setClassification();
        }
    },
    
    createColorInterpolation: function() {
        var numColors = this.classification.bins.length;
		var mapLegendType = this.widget.cmp.mapLegendType.getValue();
        this.widget.imageLegend = [];
        
        this.colorInterpolation = mapLegendType == G.conf.map_legendset_type_automatic ?
            mapfish.ColorRgb.getColorsArrayByRgbInterpolation(this.colors[0], this.colors[1], numColors) : this.widget.colorInterpolation;
            
        for (var i = 0; i < this.classification.bins.length; i++) {
            this.widget.imageLegend.push({
                label: this.classification.bins[i].label.replace('&nbsp;&nbsp;', ' '),
                color: this.colorInterpolation[i].toHexString()
            });
        }
    },

    setClassification: function() {
        var values = [];
        for (var i = 0; i < this.layer.features.length; i++) {
            values.push(this.layer.features[i].attributes[this.indicator]);
        }
        
        var distOptions = {
            'labelGenerator': this.options.labelGenerator
        };
        var dist = new mapfish.GeoStat.Distribution(values, distOptions);

		this.minVal = dist.minVal;
        this.maxVal = dist.maxVal;

        this.classification = dist.classify(
            this.method,
            this.numClasses,
            null
        );

        this.createColorInterpolation();
    },

    applyClassification: function(options, widget) {
        this.widget = widget;
        this.updateOptions(options);
        
		var calculateRadius = OpenLayers.Function.bind(
			function(feature) {
				var value = feature.attributes[this.indicator];
                var size = (value - this.minVal) / (this.maxVal - this.minVal) *
					(this.maxSize - this.minSize) + this.minSize;
                return size || this.minSize;
            },	this
		);
		this.extendStyle(null, {'pointRadius': '${calculateRadius}'}, {'calculateRadius': calculateRadius});
    
        var boundsArray = this.classification.getBoundsArray();
        var rules = new Array(boundsArray.length-1);        
        for (var i = 0; i < boundsArray.length-1; i++) {
            var rule = new OpenLayers.Rule({
                symbolizer: {fillColor: this.colorInterpolation[i].toHexString()},
                filter: new OpenLayers.Filter.Comparison({
                    type: OpenLayers.Filter.Comparison.BETWEEN,
                    property: this.indicator,
                    lowerBoundary: boundsArray[i],
                    upperBoundary: boundsArray[i + 1]
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
        var element;
        this.legendDiv.update("");
        
        for (var p in info) {
            element = document.createElement("div");
            element.style.height = "14px";
            element.innerHTML = info[p];
            this.legendDiv.appendChild(element);
            
            element = document.createElement("div");
            element.style.clear = "left";
            this.legendDiv.appendChild(element);
        }
        
        element = document.createElement("div");
        element.style.width = "1px";
        element.style.height = "5px";
        this.legendDiv.appendChild(element);
        
        if (G.vars.activeWidget.legend.value == G.conf.map_legendset_type_automatic) {        
            for (var i = 0; i < this.classification.bins.length; i++) {
                var element = document.createElement("div");
                element.style.backgroundColor = this.colorInterpolation[i].toHexString();
                element.style.width = "30px";
                element.style.height = "15px";
                element.style.cssFloat = "left";
                element.style.marginRight = "8px";
                this.legendDiv.appendChild(element);

                element = document.createElement("div");
                element.innerHTML = this.classification.bins[i].label;
                this.legendDiv.appendChild(element);

                element = document.createElement("div");
                element.style.clear = "left";
                this.legendDiv.appendChild(element);
            }
        }
        else if (G.vars.activeWidget.legend.value == G.conf.map_legendset_type_predefined) {
            for (var i = 0; i < this.classification.bins.length; i++) {
                var element = document.createElement("div");
                element.style.backgroundColor = this.colorInterpolation[i].toHexString();
                element.style.width = "30px";
                element.style.height = this.widget.legendNames[i] ? "25px" : "20px";
                element.style.cssFloat = "left";
                element.style.marginRight = "8px";
                this.legendDiv.appendChild(element);

                element = document.createElement("div");
                element.style.lineHeight = this.widget.legendNames[i] ? "12px" : "7px";
                element.innerHTML = '<b style="color:#222">' + (this.widget.legendNames[i] || '') + '</b><br/>' + this.classification.bins[i].label;
                this.legendDiv.appendChild(element);

                element = document.createElement("div");
                element.style.clear = "left";
                this.legendDiv.appendChild(element);
            }
        }
    },

    CLASS_NAME: "mapfish.GeoStat.Boundary"
});
