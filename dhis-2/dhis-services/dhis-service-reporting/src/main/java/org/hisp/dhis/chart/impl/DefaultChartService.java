package org.hisp.dhis.chart.impl;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static org.hisp.dhis.chart.Chart.TYPE_AREA;
import static org.hisp.dhis.chart.Chart.TYPE_BAR;
import static org.hisp.dhis.chart.Chart.TYPE_COLUMN;
import static org.hisp.dhis.chart.Chart.TYPE_LINE;
import static org.hisp.dhis.chart.Chart.TYPE_PIE;
import static org.hisp.dhis.chart.Chart.TYPE_STACKED_BAR;
import static org.hisp.dhis.chart.Chart.TYPE_STACKED_COLUMN;
import static org.hisp.dhis.reporttable.ReportTable.getIdentifier;
import static org.hisp.dhis.system.util.ConversionUtils.getArray;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.SplineInterpolator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealInterpolator;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.chart.ChartService;
import org.hisp.dhis.chart.ChartStore;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.reporttable.jdbc.ReportTableManager;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.system.util.MathUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.TableOrder;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
@Transactional
public class DefaultChartService
    implements ChartService
{
    private static final Font titleFont = new Font( "Tahoma", Font.BOLD, 15 );

    private static final Font subTitleFont = new Font( "Tahoma", Font.PLAIN, 12 );

    private static final Font labelFont = new Font( "Tahoma", Font.PLAIN, 10 );

    private static final String TREND_PREFIX = "Trend - ";

    private static final Color[] colors = {Color.decode( "#88be3b" ), Color.decode( "#3b6286" ),
        Color.decode( "#b7404c" ), Color.decode( "#ff9f3a" ), Color.decode( "#968f8f" ), Color.decode( "#b7409f" ),
        Color.decode( "#ffda64" ), Color.decode( "#4fbdae" ), Color.decode( "#b78040" ), Color.decode( "#676767" ),
        Color.decode( "#6a33cf" ), Color.decode( "#4a7833" )};

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ChartStore chartStore;

    public void setChartStore( ChartStore chartStore )
    {
        this.chartStore = chartStore;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private MinMaxDataElementService minMaxDataElementService;

    public void setMinMaxDataElementService( MinMaxDataElementService minMaxDataElementService )
    {
        this.minMaxDataElementService = minMaxDataElementService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private ReportTableManager reportTableManager;

    public void setReportTableManager( ReportTableManager reportTableManager )
    {
        this.reportTableManager = reportTableManager;
    }

    // -------------------------------------------------------------------------
    // ChartService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public JFreeChart getJFreeChart( int id, I18nFormat format )
    {
        Chart chart = getChart( id );

        return chart != null ? getJFreeChart( chart, format ) : null;
    }

    public JFreeChart getJFreeChart( Chart chart, I18nFormat format )
    {
        return getJFreeChart( chart, null, format );
    }
    
    public JFreeChart getJFreeChart( Chart chart, Date date, I18nFormat format )
    {
        if ( chart.getRelatives() != null )
        {
            chart.setRelativePeriods( periodService.reloadPeriods( chart.getRelatives().getRelativePeriods( 
                date, format, true ) ) );
        }

        User user = currentUserService.getCurrentUser();

        if ( user != null && user.getOrganisationUnit() != null )
        {
            OrganisationUnit unit = user.getOrganisationUnit();
            
            if ( chart.isUserOrganisationUnit() )
            {
                chart.getRelativeOrganisationUnits().add( unit );
            }
            else if ( chart.isUserOrganisationUnitChildren() )
            {
                chart.getRelativeOrganisationUnits().addAll( unit.hasChild() ? unit.getSortedChildren() : Arrays.asList( unit ) );
            }
        }
        
        chart.setFormat( format );

        return getJFreeChart( chart, !chart.isHideSubtitle() );
    }

    public JFreeChart getJFreePeriodChart( Indicator indicator, OrganisationUnit unit, boolean title, I18nFormat format )
    {
        List<Period> periods = periodService.reloadPeriods( 
            new RelativePeriods().setLast12Months( true ).getRelativePeriods( format, true ) );

        Chart chart = new Chart();

        if ( title )
        {
            chart.setName( indicator.getName() );
        }

        chart.setType( TYPE_LINE );
        chart.setDimensions( Chart.DIMENSION_DATA, Chart.DIMENSION_PERIOD, Chart.DIMENSION_ORGANISATIONUNIT );
        chart.setHideLegend( true );
        chart.getIndicators().add( indicator );
        chart.setRelativePeriods( periods );
        chart.getOrganisationUnits().add( unit );
        chart.setFormat( format );

        return getJFreeChart( chart, title );
    }

    public JFreeChart getJFreeOrganisationUnitChart( Indicator indicator, OrganisationUnit parent, boolean title,
                                                     I18nFormat format )
    {
        List<Period> periods = periodService.reloadPeriods( 
            new RelativePeriods().setThisYear( true ).getRelativePeriods( format, true ) );

        Chart chart = new Chart();

        if ( title )
        {
            chart.setName( indicator.getName() );
        }

        chart.setType( TYPE_COLUMN );
        chart.setDimensions( Chart.DIMENSION_DATA, Chart.DIMENSION_ORGANISATIONUNIT, Chart.DIMENSION_PERIOD );
        chart.setHideLegend( true );
        chart.getIndicators().add( indicator );
        chart.setRelativePeriods( periods );
        chart.setOrganisationUnits( parent.getSortedChildren() );
        chart.setFormat( format );

        return getJFreeChart( chart, title );
    }

    public JFreeChart getJFreeChart( List<Indicator> indicators, List<DataElement> dataElements,
                                     List<Period> periods, List<OrganisationUnit> organisationUnits, 
                                     String series, String category, String filter,
                                     boolean regression, I18nFormat format )
    {
        Chart chart = new Chart();

        chart.setType( TYPE_COLUMN );
        chart.setDimensions( series, category, filter );
        chart.setHideLegend( false );
        chart.setRegression( regression );
        chart.setIndicators( indicators );
        chart.setDataElements( dataElements );
        chart.setRelativePeriods( periods );
        chart.setOrganisationUnits( organisationUnits );
        chart.setFormat( format );
        chart.setName( chart.getTitle() );

        return getJFreeChart( chart, false );
    }

    public JFreeChart getJFreeChart( String name, PlotOrientation orientation, CategoryLabelPositions labelPositions,
                                     Map<String, Double> categoryValues )
    {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        for ( Entry<String, Double> entry : categoryValues.entrySet() )
        {
            dataSet.addValue( entry.getValue(), name, entry.getKey() );
        }

        CategoryPlot plot = getCategoryPlot( dataSet, getBarRenderer(), orientation, labelPositions );

        JFreeChart jFreeChart = getBasicJFreeChart( plot );
        jFreeChart.setTitle( name );

        return jFreeChart;
    }

    public JFreeChart getJFreeChartHistory( DataElement dataElement,
                                            DataElementCategoryOptionCombo categoryOptionCombo, Period lastPeriod, OrganisationUnit organisationUnit,
                                            int historyLength, I18nFormat format )
    {
        lastPeriod = periodService.reloadPeriod( lastPeriod );

        List<Period> periods = periodService.getPeriods( lastPeriod, historyLength );

        MinMaxDataElement minMax = minMaxDataElementService.getMinMaxDataElement( organisationUnit, dataElement,
            categoryOptionCombo );

        UnivariateRealInterpolator interpolator = new SplineInterpolator();

        Integer periodCount = 0;
        List<Double> x = new ArrayList<Double>();
        List<Double> y = new ArrayList<Double>();

        // ---------------------------------------------------------------------
        // DataValue, MinValue and MaxValue DataSets
        // ---------------------------------------------------------------------

        DefaultCategoryDataset dataValueDataSet = new DefaultCategoryDataset();
        DefaultCategoryDataset metaDataSet = new DefaultCategoryDataset();

        for ( Period period : periods )
        {
            ++periodCount;

            period.setName( format.formatPeriod( period ) );

            DataValue dataValue = dataValueService.getDataValue( organisationUnit, dataElement, period,
                categoryOptionCombo );

            double value = 0;

            if ( dataValue != null && dataValue.getValue() != null && MathUtils.isNumeric( dataValue.getValue() ) )
            {
                value = Double.parseDouble( dataValue.getValue() );

                x.add( periodCount.doubleValue() );
                y.add( value );
            }

            dataValueDataSet.addValue( value, dataElement.getShortName(), period.getName() );

            if ( minMax != null )
            {
                metaDataSet.addValue( minMax.getMin(), "Min value", period.getName() );
                metaDataSet.addValue( minMax.getMax(), "Max value", period.getName() );
            }
        }

        // ---------------------------------------------------------------------
        // Interpolation DataSet
        // ---------------------------------------------------------------------

        if ( x.size() >= 3 ) // minimum 3 points required for interpolation
        {
            periodCount = 0;

            double[] xa = getArray( x );

            int min = MathUtils.getMin( xa ).intValue();
            int max = MathUtils.getMax( xa ).intValue();

            try
            {
                UnivariateRealFunction function = interpolator.interpolate( xa, getArray( y ) );

                for ( Period period : periods )
                {
                    if ( ++periodCount >= min && periodCount <= max )
                    {
                        metaDataSet.addValue( function.value( periodCount ), "Regression value", period.getName() );
                    }
                }
            }
            catch ( MathException ex )
            {
                throw new RuntimeException( "Failed to interpolate", ex );
            }
        }

        // ---------------------------------------------------------------------
        // Plots
        // ---------------------------------------------------------------------

        CategoryPlot plot = getCategoryPlot( dataValueDataSet, getBarRenderer(), PlotOrientation.VERTICAL,
            CategoryLabelPositions.UP_45 );

        plot.setDataset( 1, metaDataSet );
        plot.setRenderer( 1, getLineRenderer() );

        JFreeChart jFreeChart = getBasicJFreeChart( plot );

        return jFreeChart;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns a basic JFreeChart.
     */
    private JFreeChart getBasicJFreeChart( CategoryPlot plot )
    {
        JFreeChart jFreeChart = new JFreeChart( null, titleFont, plot, false );

        jFreeChart.setBackgroundPaint( Color.WHITE );
        jFreeChart.setAntiAlias( true );

        return jFreeChart;
    }

    /**
     * Returns a CategoryPlot.
     */
    private CategoryPlot getCategoryPlot( CategoryDataset dataSet, CategoryItemRenderer renderer,
                                          PlotOrientation orientation, CategoryLabelPositions labelPositions )
    {
        CategoryPlot plot = new CategoryPlot( dataSet, new CategoryAxis(), new NumberAxis(), renderer );

        plot.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );
        plot.setOrientation( orientation );

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions( labelPositions );

        return plot;
    }

    /**
     * Returns a bar renderer.
     */
    private BarRenderer getBarRenderer()
    {
        BarRenderer renderer = new BarRenderer();

        renderer.setMaximumBarWidth( 0.07 );

        for ( int i = 0; i < colors.length; i++ )
        {
            renderer.setSeriesPaint( i, colors[i] );
            renderer.setShadowVisible( false );
        }

        return renderer;
    }

    /**
     * Returns a line and shape renderer.
     */
    private LineAndShapeRenderer getLineRenderer()
    {
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        for ( int i = 0; i < colors.length; i++ )
        {
            renderer.setSeriesPaint( i, colors[i] );
        }

        return renderer;
    }

    /**
     * Returns a horizontal line marker for the given x value and label.
     */
    private Marker getMarker( Double value, String label )
    {
        Marker marker = new ValueMarker( value );
        marker.setPaint( Color.BLACK );
        marker.setStroke( new BasicStroke( 1.1f ) );
        marker.setLabel( label );
        marker.setLabelOffset( new RectangleInsets( -10, 50, 0, 0 ) );
        marker.setLabelFont( subTitleFont );
        
        return marker;
    }

    /**
     * Returns a JFreeChart of type defined in the chart argument.
     */
    private JFreeChart getJFreeChart( Chart chart, boolean subTitle )
    {
        final BarRenderer barRenderer = getBarRenderer();
        final LineAndShapeRenderer lineRenderer = getLineRenderer();

        // ---------------------------------------------------------------------
        // Plot
        // ---------------------------------------------------------------------

        CategoryPlot plot = null;

        CategoryDataset[] dataSets = getCategoryDataSet( chart );

        if ( chart.isType( TYPE_LINE ) || chart.isType( TYPE_AREA ) )
        {
            plot = new CategoryPlot( dataSets[0], new CategoryAxis(), new NumberAxis(), lineRenderer );
            plot.setOrientation( PlotOrientation.VERTICAL );
        }
        else if ( chart.isType( TYPE_COLUMN ) )
        {
            plot = new CategoryPlot( dataSets[0], new CategoryAxis(), new NumberAxis(), barRenderer );
            plot.setOrientation( PlotOrientation.VERTICAL );
        }
        else if ( chart.isType( TYPE_BAR ) )
        {
            plot = new CategoryPlot( dataSets[0], new CategoryAxis(), new NumberAxis(), barRenderer );
            plot.setOrientation( PlotOrientation.HORIZONTAL );
        }
        else if ( chart.isType( TYPE_PIE ) )
        {
            return getMultiplePieChart( chart, dataSets );
        }
        else if ( chart.isType( TYPE_STACKED_COLUMN ) )
        {
            return getStackedBarChart( chart, dataSets[0], false );
        }
        else if ( chart.isType( TYPE_STACKED_BAR ) )
        {
            return getStackedBarChart( chart, dataSets[0], true );
        }

        if ( chart.isRegression() )
        {
            plot.setDataset( 1, dataSets[1] );
            plot.setRenderer( 1, lineRenderer );
        }

        JFreeChart jFreeChart = new JFreeChart( chart.getName(), titleFont, plot, !chart.isHideLegend() );

        if ( chart.isTargetLine() )
        {
            plot.addRangeMarker( getMarker( chart.getTargetLineValue(), chart.getTargetLineLabel() ) );
        }
        
        if ( chart.isBaseLine() )
        {
            plot.addRangeMarker( getMarker( chart.getBaseLineValue(), chart.getBaseLineLabel() ) );
        }

        if ( subTitle )
        {
            jFreeChart.addSubtitle( getSubTitle( chart ) );
        }

        plot.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );

        // ---------------------------------------------------------------------
        // Category label positions
        // ---------------------------------------------------------------------

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.UP_45 );
        domainAxis.setLabel( chart.getDomainAxisLabel() );

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabel( chart.getRangeAxisLabel() );

        // ---------------------------------------------------------------------
        // Color & antialias
        // ---------------------------------------------------------------------

        jFreeChart.setBackgroundPaint( Color.WHITE );
        jFreeChart.setAntiAlias( true );

        return jFreeChart;
    }
    
    private JFreeChart getStackedBarChart( Chart chart, CategoryDataset dataSet, boolean horizontal )
    {
        JFreeChart stackedBarChart = null;

        if ( chart.isType( TYPE_STACKED_BAR ) )
        {
            stackedBarChart = ChartFactory.createStackedBarChart( chart.getName(), chart.getDomainAxisLabel(),
                chart.getRangeAxisLabel(), dataSet, PlotOrientation.VERTICAL, true, false, false );
        }
        else
        {
            stackedBarChart = ChartFactory.createStackedBarChart( chart.getName(), chart.getDomainAxisLabel(),
                chart.getRangeAxisLabel(), dataSet, PlotOrientation.VERTICAL, true, false, false );
        }

        CategoryPlot plot = (CategoryPlot) stackedBarChart.getPlot();
        plot.setBackgroundPaint( Color.WHITE );
        plot.setOutlinePaint( Color.WHITE );
        plot.setOrientation( horizontal ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL );

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions( CategoryLabelPositions.UP_45 );

        stackedBarChart.getTitle().setFont( titleFont );
        stackedBarChart.addSubtitle( getSubTitle( chart ) );
        stackedBarChart.setAntiAlias( true );

        return stackedBarChart;
    }

    private JFreeChart getMultiplePieChart( Chart chart, CategoryDataset[] dataSets )
    {
        JFreeChart multiplePieChart = null;

        if ( chart.isType( TYPE_PIE ) )
        {
            multiplePieChart = ChartFactory.createMultiplePieChart( chart.getName(), dataSets[0], TableOrder.BY_ROW,
                !chart.isHideLegend(), false, false );
        }
        else
        {
            multiplePieChart = ChartFactory.createMultiplePieChart3D( chart.getName(), dataSets[0], TableOrder.BY_ROW,
                !chart.isHideLegend(), false, false );
        }

        multiplePieChart.getTitle().setFont( titleFont );
        multiplePieChart.addSubtitle( getSubTitle( chart ) );
        multiplePieChart.getLegend().setItemFont( subTitleFont );
        multiplePieChart.setBackgroundPaint( Color.WHITE );
        multiplePieChart.setAntiAlias( true );

        MultiplePiePlot multiplePiePlot = (MultiplePiePlot) multiplePieChart.getPlot();
        JFreeChart pieChart = multiplePiePlot.getPieChart();
        pieChart.getTitle().setFont( subTitleFont );

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint( Color.WHITE );
        piePlot.setLabelFont( labelFont );
        piePlot.setLabelGenerator( new StandardPieSectionLabelGenerator( "{2}" ) );
        piePlot.setSimpleLabels( true );
        piePlot.setIgnoreZeroValues( true );
        piePlot.setIgnoreNullValues( true );

        for ( int i = 0; i < dataSets[0].getColumnCount(); i++ )
        {
            piePlot.setSectionPaint( dataSets[0].getColumnKey( i ), colors[(i % colors.length)] );
        }

        return multiplePieChart;
    }

    private CategoryDataset[] getCategoryDataSet( Chart chart )
    {
        Map<String, Double> valueMap = reportTableManager.getAggregatedValueMap( chart );
                
        DefaultCategoryDataset regularDataSet = new DefaultCategoryDataset();
        DefaultCategoryDataset regressionDataSet = new DefaultCategoryDataset();
        
        SimpleRegression regression = new SimpleRegression();
        
        for ( NameableObject series : chart.series() )
        {
            double categoryIndex = 0;
            
            for ( NameableObject category : chart.category() )
            {   
                categoryIndex++;
                
                String key = getIdentifier( Arrays.asList( series, category, chart.filter() ) );
                
                Double value = valueMap.get( key );
                
                regularDataSet.addValue( value, series.getShortName(), category.getShortName() );
                
                if ( chart.isRegression() && value != null && !MathUtils.isEqual( value, MathUtils.ZERO ) )
                {
                    regression.addData( categoryIndex, value );
                }
            }
            
            if ( chart.isRegression() ) // Period must be category
            {
                categoryIndex = 0;
                
                for ( NameableObject category : chart.category() )
                {
                    final double value = regression.predict( categoryIndex++ );

                    // Enough values must exist for regression
                    
                    if ( !Double.isNaN( value ) )
                    {
                        regressionDataSet.addValue( value, TREND_PREFIX + series.getShortName(), category.getShortName() );
                    }
                }
            }
        }
        
        return new CategoryDataset[] { regularDataSet, regressionDataSet };
    }
    
    private TextTitle getSubTitle( Chart chart )
    {
        TextTitle title = new TextTitle();
        
        title.setFont( subTitleFont );
        title.setText( chart.getTitle() );
        
        return title;
    }
    
    // -------------------------------------------------------------------------
    // CRUD operations
    // -------------------------------------------------------------------------

    public int saveChart( Chart chart )
    {
        return chartStore.save( chart );
    }

    public void saveOrUpdate( Chart chart )
    {
        chartStore.saveOrUpdate( chart );
    }

    public Chart getChart( int id )
    {
        return chartStore.get( id );
    }

    public Chart getChart( String uid )
    {
        return chartStore.getByUid( uid );
    }

    public void deleteChart( Chart chart )
    {
        chartStore.delete( chart );
    }

    public Collection<Chart> getAllCharts()
    {
        return chartStore.getAll();
    }
    
    public Collection<Chart> getSystemAndUserCharts()
    {
        return chartStore.getSystemAndUserCharts( currentUserService.getCurrentUser() );
    }

    public Chart getChartByName( String name )
    {
        return chartStore.getByName( name );
    }

    public Collection<Chart> getCharts( final Collection<Integer> identifiers )
    {
        Collection<Chart> charts = getAllCharts();

        return identifiers == null ? charts : FilterUtils.filter( charts, new Filter<Chart>()
        {
            public boolean retain( Chart object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }

    public int getChartCount()
    {
        return chartStore.getCount();
    }

    public int getChartCountByName( String name )
    {
        return chartStore.getCountByName( name );
    }

    public Collection<Chart> getChartsBetween( int first, int max )
    {
        return chartStore.getBetween( first, max );
    }

    public Collection<Chart> getChartsBetweenByName( String name, int first, int max )
    {
        return chartStore.getBetweenByName( name, first, max );
    }
    
    public Collection<Chart> getChartsByUser( User user )
    {
        return chartStore.getByUser( user );
    }
}
