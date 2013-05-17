<?xml version="1.0" encoding="UTF-8"?>
<!-- See http://www.w3.org/TR/xslt -->


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="xml" indent="yes" />

  <!-- Graph DB Resource adaptor 
  <xsl:include href="neo4j.xsl"/>
  -->
  <!-- Datasource configuration -->
  <xsl:include href="datasource.xsl"/>
	<!-- Copy everything else. -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
