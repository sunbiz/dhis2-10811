<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:d="http://dhis2.org/schema/dxf/2.0"
  version="1.0">
  
  <xsl:output method="xml" indent="yes" />

  <xsl:template match="/">
      <d:dxf>
        <d:dataValueSets>
        <xsl:for-each select="//*[local-name()='Group']">
          <xsl:variable name="dataset"
            select="substring-before(substring-after(substring-after(namespace-uri(),'='),':'),':')"/>
          <xsl:variable name="period" select="@*[local-name()='TIME_PERIOD']"/>
          <d:dataValueSet period='{$period}' idScheme='CODE' dataset='{$dataset}'>

            <xsl:for-each select="*[local-name()='Section']">
              <xsl:for-each select="*[local-name()='OBS_VALUE']">
                <xsl:variable name="orgUnit" select="@*[local-name()='FACILITY']"/>
                <xsl:variable name="dataElement" select="@*[local-name()='DATAELEMENT']"/>
                <xsl:variable name="value" select="@*[local-name()='value']"/>
                <d:dataValue dataElement='{$dataElement}' orgUnit='{$orgUnit}' value='{$value}' />
              </xsl:for-each>
            </xsl:for-each>
          </d:dataValueSet>
        </xsl:for-each>
      </d:dataValueSets>
    </d:dxf>
  </xsl:template>

</xsl:stylesheet>
