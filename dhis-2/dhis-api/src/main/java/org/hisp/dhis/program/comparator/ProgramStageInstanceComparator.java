package org.hisp.dhis.program.comparator;

import java.util.Comparator;

import org.hisp.dhis.program.ProgramStageInstance;

public class ProgramStageInstanceComparator implements Comparator<ProgramStageInstance>
{
    public int compare( ProgramStageInstance programStageInstance1, ProgramStageInstance programStageInstance2 )
    {
        return programStageInstance1.getProgramStage().getStageInProgram() - programStageInstance2.getProgramStage().getStageInProgram();
    }

}
