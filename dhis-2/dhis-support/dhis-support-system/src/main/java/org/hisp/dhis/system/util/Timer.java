package org.hisp.dhis.system.util;

public class Timer
{
    private long startTime;
    
    private boolean printDisabled;
    
    public void start()
    {
        startTime = System.nanoTime();
    }
    
    public long getSplitTime()
    {
        return getSplitTime( "Split" );
    }
    
    public long getSplitTime( String msg )
    {
        long endTime = System.nanoTime();
        
        long time = ( endTime - startTime ) / 1000;
     
        if ( !printDisabled )
        {
            System.out.println( msg  + ": " + time + " micros" );
        }
        
        return time;
    }

    public long getMilliSec()
    {
        long endTime = System.nanoTime();
        long time = ( endTime - startTime ) / 1000000;
        return time;
    }
    
    public long getTime( String msg )
    {
        long time = getSplitTime( msg );
                
        start();
        
        return time;
    }
    
    public long getTime()
    {
        return getTime( "Time" );
    }
    
    public void disablePrint()
    {
        this.printDisabled = true;
    }
}
