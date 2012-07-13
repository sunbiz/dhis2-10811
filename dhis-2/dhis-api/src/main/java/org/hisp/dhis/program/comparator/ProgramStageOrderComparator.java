package org.hisp.dhis.program.comparator;

import java.util.Comparator;

import org.hisp.dhis.program.ProgramStage;

public class ProgramStageOrderComparator implements Comparator<ProgramStage>
{
    public int compare( ProgramStage programStage1, ProgramStage programStage2 )
    {
        return programStage1.getStageInProgram() - programStage2.getStageInProgram();
    }
}
