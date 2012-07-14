/*
 * Copyright (c) 2004-2009, University of Oslo
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
package org.hisp.dhis.program;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.GenericStore;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientreport.TabularReportColumn;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.sms.outbound.OutboundSms;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public interface ProgramStageInstanceStore
    extends GenericStore<ProgramStageInstance>
{
    String ID = ProgramStageInstanceStore.class.getName();

    ProgramStageInstance get( ProgramInstance programInstance, ProgramStage programStage );

    Collection<ProgramStageInstance> get( ProgramStage programStage );

    Collection<ProgramStageInstance> get( Date dueDate );

    Collection<ProgramStageInstance> get( Date dueDate, Boolean completed );

    Collection<ProgramStageInstance> get( Date startDate, Date endDate );

    Collection<ProgramStageInstance> get( Date startDate, Date endDate, Boolean completed );

    Collection<ProgramStageInstance> get( Collection<ProgramInstance> programInstances );

    /**
     * Get all {@link ProgramStageInstance program stage instances} for unit.
     * 
     * @param unit - the unit to get instances for.
     * @param after - optional date the instance should be on or after.
     * @param before - optional date the instance should be on or before.
     * @param completed - optional flag to only get completed (<code>true</code>
     *        ) or uncompleted (<code>false</code>) instances.
     * @return
     */
    public List<ProgramStageInstance> get( OrganisationUnit unit, Date after, Date before, Boolean completed );

    List<ProgramStageInstance> get( Patient patient, Boolean completed );

    List<ProgramStageInstance> get( ProgramStage programStage, OrganisationUnit orgunit, Date startDate, Date endDate,
        int min, int max );

    Grid getTabularReport( ProgramStage programStage, Map<Integer, OrganisationUnitLevel> orgUnitLevelMap,
        Collection<Integer> orgUnits, List<TabularReportColumn> columns, int level, int maxLevel, Date startDate,
        Date endDate, boolean descOrder, Boolean completed, Integer min, Integer max, I18n i18n );

    int getTabularReportCount( ProgramStage programStage, List<TabularReportColumn> columns,
        Collection<Integer> organisationUnits, int level, int maxLevel, Date startDate, Date endDate, Boolean completed );

    void removeEmptyEvents( ProgramStage programStage, OrganisationUnit organisationUnit );

    void update( Collection<Integer> programStageInstanceIds, OutboundSms outboundSms );

    Collection<SchedulingProgramObject> getSendMesssageEvents();

    int getStatisticalProgramStageReport( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, int status );

    List<ProgramStageInstance> getStatisticalProgramStageDetailsReport( ProgramStage programStage,
        Collection<Integer> orgunitIds, Date startDate, Date endDate, int status, Integer min, Integer max );

    Grid getAggregateReport( int position, ProgramStage programStage, Collection<Integer> orgunitIds,
        String facilityLB, Integer deGroupBy, Integer deSum, Map<Integer, Collection<String>> deFilters, List<Period> periods,
        String aggregateType, Integer limit, Boolean useCompletedEvents, I18nFormat format, I18n i18n );

}
