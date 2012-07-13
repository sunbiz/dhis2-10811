package org.hisp.dhis.coldchain.reports;

import java.util.List;
import java.util.Map;

public interface CCEMReportManager
{
    String getOrgunitIdsByComma( List<Integer> selOrgUnitList, List<Integer> orgunitGroupList );
    
    Map<String,String> getCCEMSettings();
    
    List<CCEMReportDesign> getCCEMReportDesign( String designXMLFile );
    
    CCEMReport getCCEMReportByReportId( String selReportId );
    
    Map<String, Integer> getCatalogTypeAttributeValue( String orgUnitIdsByComma, Integer inventoryTypeId, Integer catalogTypeAttributeId );
    
    Map<String, Integer> getCatalogTypeAttributeValueByAge( String orgUnitIdsByComma, Integer inventoryTypeId, Integer catalogTypeAttributeId, Integer yearInvTypeAttId, Integer ageStart, Integer ageEnd );
    
    List<String> getDistinctDataElementValue( Integer dataelementID, Integer optComboId, Integer periodId );
    
    List<Integer> getOrgunitIds( List<Integer> selOrgUnitList, Integer orgUnitGroupId );
    
    Map<String, Integer> getDataValueCountforDataElements( String dataElementIdsByComma, String optComboIdsByComma, Integer periodId, String orgUnitIdsBycomma );
    
    Integer getPeriodId( String startDate, String periodType );
    
    Map<String, Integer> getFacilityWiseEquipmentRoutineData( String orgUnitIdsByComma, String periodIdsByComma, String dataElementIdsByComma, String optComboIdsByComma );
    
    Map<Integer, Double> getCatalogDataSumByEquipmentData( String orgUnitIdsByComma, Integer inventoryTypeId, Integer catalogTypeAttributeId, Integer inventoryTypeAttributeId, String equipmentValue );
    
    Map<Integer, Double> getSumOfEquipmentDatabyInventoryType( String orgUnitIdsByComma, Integer inventoryTypeId, Integer inventoryTypeAttributeId, Double factor );
    
    Map<String, String> getOrgUnitGroupAttribDataForRequirement( String orgUnitGroupIdsByComma, String orgUnitGroupAttribIds );
    
    Map<String, String> getDataElementDataForCatalogOptionsForRequirement( String orgUnitIdsByComma, String catalogOption_DataelementIds, Integer periodId );
    
    Map<String, String> getCatalogDataForRequirement( Integer vsReqCatalogTypeId, Integer vsReqStorageTempId, String vsReqStorageTemp, Integer vsReqNationalSupplyId, String vsReqNationalSupply, String vsReqCatalogAttribIds );
    
    List<Integer> getCatalogIdsForRequirement( Integer vsReqCatalogTypeId, Integer vsReqStorageTempId, String vsReqStorageTemp, Integer vsReqNationalSupplyId, String vsReqNationalSupply );
    
    Map<Integer, String> getOrgunitAndOrgUnitGroupMap( String orgUnitGroupIdsByComma, String orgUnitIdsByComma );
}
