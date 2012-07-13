<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://www.w3.org/1999/xhtml" xmlns:d="http://dhis2.org/schema/dxf/2.0">

  <xsl:template match="*" mode="row">
    <tr>
      <td> <xsl:value-of select="@name"/> </td>
      <td> <a href="{@link}">html</a> </td>
      <td> <a href="{@link}.xml">xml</a> </td>
      <td> <a href="{@link}.json">json</a> </td>
      <td> <a href="{@link}.jsonp">jsonp</a> </td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
