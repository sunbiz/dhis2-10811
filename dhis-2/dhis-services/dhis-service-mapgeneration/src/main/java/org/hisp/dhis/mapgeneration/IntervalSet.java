package org.hisp.dhis.mapgeneration;

/*
 * Copyright (c) 2011, University of Oslo
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

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * An interval set is a collection of map objects that are distributed into
 * intervals according to their value.
 * 
 * The core functionality of this class is encapsulated into its method
 * applyIntervalSetToMapLayer, which takes a map layer as input, creates an
 * interval set for it, and distributes its map objects into intervals according
 * to the given distribution strategy.
 * 
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public class IntervalSet
{
    // The intervals in this set
    private List<Interval> intervals;

    // The map object in this interval set with the lowest and highest values
    private InternalMapObject objectLow, objectHigh;

    // The interval distrubution strategies
    public enum DistributionStrategy
    {
        STRATEGY_EQUAL_RANGE, STRATEGY_EQUAL_SIZE
    }

    /**
     * Creates and applies a fixed length interval set to the given map layer.
     * 
     * How map objects are distributed among intervals depends on the
     * distribution strategy that is used, which may be either 'equal range' or
     * 'equal size'.
     * 
     * The 'equal range' strategy is defined by passing
     * DistributionStrategy.STRATEGY_EQUAL_RANGE to this method. It creates and
     * applies to the given map layer a fixed length interval set distributing
     * map objects into intervals that has the same range.
     * 
     * The 'equal size' strategy is defined by passing
     * DistributionStrategy.STRATEGY_EQUAL_SIZE to this method. It creates and
     * applies to the given map layer a fixed length interval set distributing
     * map objects into intervals that has (optimally) the same amount of map
     * objects.
     * 
     * For example, given the map object collection of a map layer
     * [a:3,b:2,c:5,d:18,e:0,f:50,g:22], where the objects with the lowest and
     * highest values are e:0 and f:50, this collection of map objects will
     * distribute differently into intervals depending on the distribution
     * strategy chosen.
     * 
     * Strategy 'equal range' with length 5: interval [e:0,b:2,a:3,c:5] range
     * 0-10 size 4 interval [d:18] range 11-20 size 1 interval [g:22] range
     * 21-30 size 1 interval [] range 31-40 size 0 interval [f:50] range 41-50
     * size 1
     * 
     * Strategy 'equal size' with length 5: interval [e:0,b:2] range 0-2 size 2
     * interval [a:3,c:5] range 3-5 size 2 interval [d:18] range 5-18 size 1
     * interval [g:22] range 18-22 size 1 interval [f:50] range 22-50 size 1
     * 
     * @param strategy the desired distribution strategy
     * @param mapLayer the map layer whose map objects to distribute
     * @param length the number of intervals in the set
     * @return the created interval set that was applied to this map layer
     */
    public static IntervalSet applyIntervalSetToMapLayer( DistributionStrategy strategy, InternalMapLayer mapLayer,
        int length )
    {
        if ( DistributionStrategy.STRATEGY_EQUAL_RANGE == strategy )
        {
            return applyEqualRangeIntervalSetToMapLayer( mapLayer, length );
        }
        else if ( DistributionStrategy.STRATEGY_EQUAL_SIZE == strategy )
        {
            return applyEqualSizeIntervalSetToMapLayer( mapLayer, length );
        }
        else
        {
            throw new RuntimeException( "The interval distribution strategy " + strategy + " is not implemented (yet)!" );
        }
    }

    /**
     * Creates and applies to the given map layer a fixed length interval set
     * distributing map objects into intervals that has the same range.
     * 
     * @param mapLayer the map layer whose map objects to distribute
     * @param length the number of equal sized intervals
     * @return the created interval set that was applied to this map layer
     */
    private static IntervalSet applyEqualRangeIntervalSetToMapLayer( InternalMapLayer mapLayer, int length )
    {
        Assert.isTrue( mapLayer != null );
        Assert.isTrue( length > 0 );
        Assert.isTrue( mapLayer.getAllMapObjects() != null );
        Assert.isTrue( mapLayer.getAllMapObjects().size() > 0 );

        IntervalSet set = new IntervalSet();
        set.intervals = new LinkedList<Interval>();

        set.objectLow = null;
        set.objectHigh = null;

        // Determine the objects with the min and max values
        for ( InternalMapObject mapObject : mapLayer.getAllMapObjects() )
        {
            if ( set.objectLow == null || mapObject.getValue() < set.objectLow.getValue() )
            {
                set.objectLow = mapObject;
            }
            
            if ( set.objectHigh == null || mapObject.getValue() > set.objectHigh.getValue() )
            {
                set.objectHigh = mapObject;
            }
        }

        // Determine and set the color for each of the intervals according to
        // the highest and lowest values
        for ( int i = 0; i < length; i++ )
        {
            // Determine the boundaries the interval covers
            double low = MapUtils.lerp( set.objectLow.getValue(), set.objectHigh.getValue(), (i + 0.0) / length );
            double high = MapUtils.lerp( set.objectLow.getValue(), set.objectHigh.getValue(), (i + 1.0) / length );

            // Determine the color of the interval
            Color color = MapUtils.lerp( mapLayer.getColorLow(), mapLayer.getColorHigh(), (i + 0.5) / length );

            // Create and setup a new interval
            Interval in = new Interval( low, high );
            in.setColor( color );

            // Add it to the set
            set.intervals.add( in );
        }

        // Distribute this map layer's objects among the intervals in the set
        distributeAndUpdateMapObjectsForMapLayer( mapLayer, set );

        // Set this interval set for the map layer
        mapLayer.setIntervalSet( set );

        return set;
    }

    /**
     * Creates and applies to the given map layer a fixed length interval set
     * distributing map objects into intervals that has (optimally) the same
     * amount of map objects. TODO Implement this method
     * 
     * @param mapLayer the map layer whose map objects to distribute
     * @param length the number of equal sized intervals
     * @return the created interval set that was applied to this map layer
     */
    private static IntervalSet applyEqualSizeIntervalSetToMapLayer( InternalMapLayer mapLayer, int length )
    {
        Assert.isTrue( mapLayer != null );
        Assert.isTrue( length > 0 );
        Assert.isTrue( mapLayer.getAllMapObjects() != null );
        Assert.isTrue( mapLayer.getAllMapObjects().size() > 0 );

        throw new RuntimeException( "This distribution strategy is not implemented yet!" );
    }

    /**
     * Distribute a map layer's map objects into the given interval set and
     * update each map object with its interval.
     * 
     * @param mapLayer the map layer whose objects to distribute
     * @param set the interval set
     */
    private static void distributeAndUpdateMapObjectsForMapLayer( InternalMapLayer mapLayer, IntervalSet set )
    {

        // For each map object, determine in which interval it belongs
        for ( InternalMapObject obj : mapLayer.getAllMapObjects() )
        {
            for ( Interval in : set.intervals )
            {

                // If the map object's value is within this interval's
                // boundaries, add it to this interval
                if ( obj.getValue() >= in.getValueLow() && obj.getValue() <= in.getValueHigh() )
                {

                    // Add map object to interval and set interval for map
                    // object
                    in.addMember( obj );
                    obj.setInterval( in );

                    // Do not add to more than one interval
                    break;
                }
            }

            Assert.isTrue( obj.getInterval() != null );
        }
    }

    /**
     * Returns all the intervals in this interval set.
     * 
     * @return the list of intervals
     */
    public List<Interval> getAllIntervals()
    {
        return this.intervals;
    }

    /**
     * Gets the map object with the lowest value in this interval set.
     * 
     * @return the object with the lowest value
     */
    public InternalMapObject getObjectLow()
    {
        return this.objectLow;
    }

    /**
     * Gets the map object with the highest value in this interval set
     * 
     * @return the object with the highest value
     */
    public InternalMapObject getObjectHigh()
    {
        return this.objectHigh;
    }
}
