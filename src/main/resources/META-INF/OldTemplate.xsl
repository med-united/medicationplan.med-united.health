<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"
         font-family="Courier, Liberation Sans" font-size="12pt" text-align="left"
         line-height="normal" font-selection-strategy="character-by-character"
         line-height-shift-adjustment="disregard-shifts" writing-mode="lr-tb"
         language="DE">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="DIN-A5" column-count="2"
                               page-width="210mm" page-height="148mm"
                               margin-top="5mm" margin-bottom="5mm"
                               margin-left="8mm" margin-right="5mm">
            <fo:region-body region-name="body"
                            margin-top="60mm" margin-bottom="0mm"
                            margin-left="2mm" margin-right="5mm"/>
            <fo:region-before region-name="header" extent="55mm"/>
            <fo:region-after region-name="footer" extent="50mm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    <fo:declarations>
        <x:xmpmeta xmlns:x="adobe:ns:meta/">
            <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
                <rdf:Description xmlns:dc="http://purl.org/dc/elements/1.1/"
                                 rdf:about="">
                    <dc:title>E-Rezept</dc:title>
                    <dc:description></dc:description>
                </rdf:Description>
                <rdf:Description xmlns:pdf="http://ns.adobe.com/pdf/1.3/"
                                 rdf:about=""/>
                <rdf:Description xmlns:xmp="http://ns.adobe.com/xap/1.0/"
                                 rdf:about="">
                    <xmp:CreatorTool>ere.health</xmp:CreatorTool>
                </rdf:Description>
            </rdf:RDF>
        </x:xmpmeta>
        <pdf:embedded-file filename="Bundles.xml" description="Embedded Bundles XML">
            <xsl:attribute name="src">
                url(<xsl:value-of select="$bundleFileUrl"/>)
            </xsl:attribute>
        </pdf:embedded-file>
    </fo:declarations>
    <fo:page-sequence master-reference="DIN-A5" initial-page-number="1">
        <fo:static-content flow-name="header">
            <xsl:call-template name="header"/>
        </fo:static-content>
        <fo:static-content flow-name="footer">
            <xsl:call-template name="footer"/>
        </fo:static-content>
        <fo:flow flow-name="body">
            <xsl:call-template name="body"/>
        </fo:flow>
    </fo:page-sequence>
</fo:root>