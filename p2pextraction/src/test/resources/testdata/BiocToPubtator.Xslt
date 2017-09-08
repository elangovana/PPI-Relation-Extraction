<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output omit-xml-declaration="yes"/>
    <xsl:variable name="newline" select="'&#xa;'"/>
    <xsl:variable name="tab" select="'&#x9;'"/>


    <xsl:template match="/">

        <xsl:for-each select="collection/document">
            <xsl:variable name="docID" select="id/text()"></xsl:variable>

            <xsl:value-of
                    select="concat($docID, '|t|',  passage[infon[@key='type']/text() ='title']/text/text(),$newline)"></xsl:value-of>
            <xsl:value-of
                    select="concat($docID, '|a|',  passage[infon[@key='type']/text() ='abstract']/text/text(),$newline)"></xsl:value-of>
           <!-- Loop through ann -->

                <!--<xsl:for-each select="passage[infon[@key='type']/text() ='title']/annotation">-->
                    <!--<xsl:value-of-->
                            <!--select="concat($docID, $tab,  location/@offset, $tab, location/@offset +location/@length, $tab,  text/text(), $tab, infon[@key='type']/text(), $tab,infon[@key='NCBI GENE']/text(),$newline )"></xsl:value-of>-->
                <!--</xsl:for-each>-->

            <!--<xsl:for-each select=" passage[infon[@key='type']/text() ='abstract']/annotation">-->
                <!--<xsl:variable name="titlelength" select="string-length(passage[infon[@key='type']/text() ='title']/text())"></xsl:variable>-->
                <!--<xsl:value-of-->
                        <!--select="concat($docID, $tab,  $titlelength+ location/@offset, $tab, $titlelength+location/@offset +location/@length, $tab,  text/text(), $tab, infon[@key='type']/text(), $tab,infon[@key='NCBI GENE']/text(),$newline )"></xsl:value-of>-->
            <!--</xsl:for-each>-->


            <xsl:value-of select="$newline"/>

        </xsl:for-each>
    </xsl:template>


</xsl:stylesheet>