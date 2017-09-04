<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:variable name="training-file" select="'PMtask_Relations_TrainingSet.xml'"/>

    <xsl:variable name="training-doc" select="document($training-file)"/>

<xsl:template match="node() | @*">

    <xsl:apply-templates select="@*|node()"/>
</xsl:template>

    <xsl:template match="collection">
        <tr>
        <td>Gene name count</td>
        <td> <xsl:value-of select="count($training-doc//annotation[infon[@key='type']/text()='Gene'])"/>
        </td>

        <td> <xsl:value-of select="count(//annotation[infon[@key='type']/text()='Gene'])"/>
        </td>



    </tr>
        <tr>
            <td>Gene name count</td>
            <td> <xsl:value-of select="count(distinct-values( $training-doc//annotation[infon[@key='type']/text()='Gene']))"/>
            </td>

            <td> <xsl:value-of select="count(distinct-values(//annotation[infon[@key='type']/text()='Gene']))"/>
            </td>



        </tr>

        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>

    <xsl:template match="annotation[infon[@key='type']/text()='Gene']">

                <xsl:variable name="geneName" select="text/text()"/>
                <xsl:variable name="docId" select="../../id"/>
        <xsl:value-of select="'*********'"/>
        <!--<xsl:value-of select="$docId"/>-->
        <!--<xsl:value-of select="$geneName"/>-->
        <br/>
        <xsl:value-of select="$training-doc//document[id=$docId]//annotation[infon[@key='type']/text()='Gene'][text/text()=$geneName]/text/text()"/>
                <!--<xsl:value-of select="$training-doc//document[id=$docId]//annotation[infon[infon[@key='type']/text()='Gene']/text/text()=$geneName]"/>-->


    </xsl:template>

</xsl:stylesheet>