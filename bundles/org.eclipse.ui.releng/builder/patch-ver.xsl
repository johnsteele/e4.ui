<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="@range[../@name='org.eclipse.platform.feature.group']">
    <xsl:attribute name="range">[3.5.0,4.0.0)</xsl:attribute>
  </xsl:template>
  
  <xsl:template match="@range[../@name='org.eclipse.rcp.feature.group']">
    <xsl:attribute name="range">[3.5.0,4.0.0)</xsl:attribute>
  </xsl:template>
  
  <xsl:template match="unit[@id='org.eclipse.ui.test.master.feature.categoryIU']">
  </xsl:template>
  
  <!-- Whenever you match any node or any attribute -->
  <xsl:template match="node()|@*">
    <!-- Copy the current node -->
    <xsl:copy>
      <!-- Including any attributes it has and any child nodes -->
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
