package org.hisp.dhis.setting;

/*
 * Copyright (c) 2004-2011, University of Oslo
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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;

/**
 * @author Stian Strandli
 * @version $Id: SystemSettingManager.java 4910 2008-04-15 17:55:02Z larshelg $
 */
public interface SystemSettingManager
{
    final String ID = SystemSettingManager.class.getName();
    
    final String KEY_SYSTEM_IDENTIFIER = "keySystemIdentifier";
    final String KEY_APPLICATION_TITLE = "applicationTitle";
    final String KEY_FLAG = "keyFlag";
    final String KEY_FLAG_IMAGE = "keyFlagImage";
    final String KEY_START_MODULE = "startModule";
    final String KEY_FORUM_INTEGRATION = "forumIntegration";
    final String KEY_OMIT_INDICATORS_ZERO_NUMERATOR_DATAMART = "omitIndicatorsZeroNumeratorDataMart";
    final String KEY_REPORT_TEMPLATE_DIRECTORY = "reportTemplateDirectory";
    final String KEY_MAX_NUMBER_OF_ATTEMPTS = "maxAttempts";
    final String KEY_TIMEFRAME_MINUTES = "lockoutTimeframe";
    final String KEY_GOOGLE_MAPS_API_KEY = "googleMapsAPIKey";
    final String KEY_DISABLE_DATAENTRYFORM_WHEN_COMPLETED = "dataEntryFormCompleted";
    final String KEY_FACTOR_OF_DEVIATION = "factorDeviation";
    final String KEY_COMPLETENESS_OFFSET = "completenessOffset";
    final String KEY_PATIENT_EXCEL_TEMPLATE_FILE_NAME = "patientExcelTemplateFileName";
    final String KEY_DATAMART_TASK = "keyDataMartTask";
    final String KEY_DATASETCOMPLETENESS_TASK = "keyDataSetCompletenessTask";
    final String KEY_EMAIL_HOST_NAME = "keyEmailHostName";
    final String KEY_EMAIL_USERNAME = "keyEmailUsername";
    final String KEY_EMAIL_PASSWORD = "keyEmailPassword";
    final String KEY_SCHEDULED_PERIOD_TYPES = "keyScheduledPeriodTypes";
    final String KEY_SCHEDULED_TASKS = "keyScheduledTasks";
    final String KEY_ORGUNITGROUPSET_AGG_LEVEL = "orgUnitGroupSetAggregationLevel";
    final String KEY_SMS_CONFIG = "SMS_CONFIG";
    final String KEY_CACHE_STRATEGY = "keyCacheStrategy";

    final int DEFAULT_MAX_NUMBER_OF_ATTEMPTS = 20;
    final int DEFAULT_TIMEFRAME_MINUTES = 1;
    final double DEFAULT_FACTOR_OF_DEVIATION = 2.0;
    final int DEFAULT_ORGUNITGROUPSET_AGG_LEVEL = 3;    
    final String DEFAULT_GOOGLE_MAPS_API_KEY = "ABQIAAAAut6AhySExnYIXm5s2OFIkxRKNzJ-_9njnryRTbvC6CtrS4sRvRREWnxwlZUa630pLuPf3nD9i4fq9w";
    final String DEFAULT_START_MODULE = "dhis-web-dashboard-integration";
    
    final int DEFAULT_COMPLETENESS_OFFSET = 15;
    
    final HashSet<String> DEFAULT_SCHEDULED_PERIOD_TYPES = new HashSet<String>() { {
        add( MonthlyPeriodType.NAME ); 
        add( QuarterlyPeriodType.NAME );
        add( YearlyPeriodType.NAME );
    } };
    
    void saveSystemSetting( String name, Serializable value );

    Serializable getSystemSetting( String name );
    
    Serializable getSystemSetting( String name, Serializable defaultValue );

    Collection<SystemSetting> getAllSystemSettings();

    void deleteSystemSetting( String name );
    
    List<String> getFlags();
    
    String getSystemIdentifier();
    
    String getFlagImage();
    
    String getEmailHostName();
    
    String getEmailUsername();
    
    String getEmailPassword();
}
