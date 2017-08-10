<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="node()|@*">
        <xsl:value-of select="count(//relation[infon[@key='relation']/text()='PPIm'])"/>
    </xsl:template>


</xsl:stylesheet>