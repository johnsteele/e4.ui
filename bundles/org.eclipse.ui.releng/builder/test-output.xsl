<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text"/>
  
  <xsl:template match="/">
    <xsl:for-each select="testsuites/testsuite[@errors!='0' or @failures!='0']">
  	<xsl:value-of select="@package"/>
  	<xsl:text>.</xsl:text>
  	<xsl:value-of select="@name"/>
  	<xsl:text> errors: </xsl:text>
  	<xsl:value-of select="@errors"/>
  	<xsl:text> failures: </xsl:text>
  	<xsl:value-of select="@failures"/>
  	<xsl:text>&#10;</xsl:text>
  	</xsl:for-each>
  </xsl:template>

  <xsl:template match="node()|@*">
  </xsl:template>

</xsl:stylesheet>
