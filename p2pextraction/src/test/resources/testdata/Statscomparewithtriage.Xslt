<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:variable name="relation-file" select="'PMtask_Relations_TrainingSet.xml'"/>

    <xsl:variable name="relation-doc" select="document($relation-file)"/>
    <xsl:variable name="triagRelavantDocs"
                  select="//document[infon[@key='relevant']/text()='yes']"></xsl:variable>
    <xsl:variable name="triagIrrelavantDocs"
                  select="//document[infon[@key='relevant']/text()='no']"></xsl:variable>


    <xsl:template match="node() | @*">

        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>

    <xsl:template match="collection">
        <table>
            <tr>
                <td>Document count (Relation task vs Triage Task)</td>
                <td>
                    <xsl:value-of select="count(($relation-doc//document))"/>
                </td>

                <td>
                    <xsl:value-of select="count(//document)"/>
                </td>
            </tr>
            <tr>
                <td>Document of docs in triage (drill down : yes vs no)	</td>

                <td>
                    <xsl:value-of select="count(//document[infon[@key='relevant']/text()='yes'])"/>
                </td>

                <td>
                    <xsl:value-of select="count(//document[infon[@key='relevant']/text()='no'])"/>
                </td>


            </tr>

            <tr>
                <td>Relations count total in relation task
                    <xsl:value-of select="count($relation-doc//document/relation)"/>
                    (drilldown: PPIm vs non PPIm relationship)
                </td>

                <td>
                    <xsl:value-of
                            select="count($relation-doc//document/relation[infon[@key='relation']/text()='PPIm'])"/>
                </td>
                <td>
                    <xsl:value-of
                            select="count($relation-doc//document/relation[infon[@key='relation']/text()!='PPIm'])"/>
                </td>

            </tr>

            <tr>
                <td>Total documents that contain PPIm vs non PPIm relationship</td>
                <td>
                    <xsl:value-of
                            select="count($relation-doc//document[relation[infon[@key='relation']/text()='PPIm']])"/>
                </td>
                <td>
                    <xsl:value-of
                            select="count($relation-doc//document[relation[infon[@key='relation']/text()!='PPIm']])"/>
                </td>
            </tr>


            <tr>
                <td>Total number of documents in relation task, that exists in the triage task training set</td>
                <td>
                    <xsl:value-of select="count($relation-doc//document[id/text()= //document/id/text()])"/>
                </td>
            </tr>

            <tr>
                <td>Number of document in relation task that are marked relevant in triage task vs
                    non-relevant
                    ones
                </td>


                <td>
                    <xsl:value-of select="count($relation-doc//document[id/text()= $triagRelavantDocs/id/text()])"/>
                </td>
                <td>
                    <xsl:value-of select="count($relation-doc//document[id/text()= $triagIrrelavantDocs/id/text()])"/>
                </td>
            </tr>
        </table>
        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>

    <!--<xsl:template match="annotation[infon[@key='type']/text()='Gene']">-->

    <!--<xsl:variable name="geneName" select="text/text()"/>-->
    <!--<xsl:variable name="docId" select="../../id"/>-->
    <!--<xsl:value-of select="'*********'"/>-->
    <!--&lt;!&ndash;<xsl:value-of select="$docId"/>&ndash;&gt;-->
    <!--&lt;!&ndash;<xsl:value-of select="$geneName"/>&ndash;&gt;-->
    <!--<br/>-->
    <!--<xsl:value-of select="$training-doc//document[id=$docId]//annotation[infon[@key='type']/text()='Gene'][text/text()=$geneName]/text/text()"/>-->
    <!--&lt;!&ndash;<xsl:value-of select="$training-doc//document[id=$docId]//annotation[infon[infon[@key='type']/text()='Gene']/text/text()=$geneName]"/>&ndash;&gt;-->


    <!--</xsl:template>-->

</xsl:stylesheet>