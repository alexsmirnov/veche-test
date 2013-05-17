<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ds="urn:jboss:domain:datasources:1.1" version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="ds.jdbc.driver" select="'h2'"/>
    <xsl:param name="ds.jdbc.url" select="'jdbc:h2:mem:test;DB_CLOSE_DELAY=-1'"/>
    <xsl:param name="ds.jdbc.user" select="'sa'"/>
    <xsl:param name="ds.jdbc.pass" select="'sa'"/>

    <xsl:variable name="newDatasourceDefinition">
      <ds:datasources>
        <ds:datasource jndi-name="java:/jdbc/netoprise" pool-name="Netoprise" enabled="true" jta="true"
                       use-java-context="true">
            <ds:connection-url><xsl:value-of select="$ds.jdbc.url"/></ds:connection-url>
            <ds:driver><xsl:value-of select="$ds.jdbc.driver"/></ds:driver>
            <ds:security>
                <ds:user-name><xsl:value-of select="$ds.jdbc.user"/></ds:user-name>
                <ds:password><xsl:value-of select="$ds.jdbc.pass"/></ds:password>
            </ds:security>
        </ds:datasource>
                <ds:drivers>
                    <ds:driver name="h2" module="com.h2database.h2">
                        <ds:xa-datasource-class>org.h2.jdbcx.JdbcDataSource</ds:xa-datasource-class>
                    </ds:driver>
                </ds:drivers>
      </ds:datasources>
    </xsl:variable>

    <!-- replace the old definition with the new -->
    <xsl:template match="//ds:subsystem/ds:datasources">
        <!-- http://docs.jboss.org/ironjacamar/userguide/1.0/en-US/html/deployment.html#deployingds_descriptor -->
        <xsl:copy-of select="$newDatasourceDefinition"/>
    </xsl:template>


</xsl:stylesheet>