<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0"
    xmlns:amts="http://ws.gematik.de/fa/amtss/AMTS_Document/v1.6"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:pdf="http://xmlgraphics.apache.org/fop/extensions/pdf"
    xmlns:barcode="http://barcode4j.krysalis.org/ns"
    xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
    xmlns:fn="http://www.w3.org/2005/xpath-functions" xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd http://www.w3.org/1999/XSL/Format https://svn.apache.org/repos/asf/xmlgraphics/fop/trunk/fop/src/foschema/fop.xsd">

    <xsl:decimal-format name="de" decimal-separator=',' grouping-separator='.'/>

    <xsl:param name="bundleFileUrl"/>

    <xsl:template match="amts:MP">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" font-family="Courier, Liberation Sans" font-size="12pt" text-align="left" line-height="normal" font-selection-strategy="character-by-character" line-height-shift-adjustment="disregard-shifts" writing-mode="lr-tb" language="DE">

            <fo:layout-master-set>
                <fo:simple-page-master master-name="DIN-A4" column-count="1" page-width="297mm" page-height="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
                    <fo:region-body region-name="body" margin-top="60mm" margin-bottom="0mm" margin-left="0mm" margin-right="0mm"/>
                    <fo:region-before region-name="header" extent="55mm"/>
                    <fo:region-after region-name="footer" extent="30mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            
            <fo:page-sequence master-reference="DIN-A4" initial-page-number="1">
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
    </xsl:template>

    <xsl:template name="formatDate">
        <xsl:param name="date"/>
        <xsl:variable name="year" select="substring-before($date, '-')"/>
        <xsl:variable name="month" select="substring-before(substring-after($date, '-'), '-')"/>
        <xsl:variable name="day" select="substring-after(substring-after($date, '-'), '-')"/>
        <xsl:value-of select="concat($day, '.', $month, '.', $year)"/>
    </xsl:template>

    <xsl:template name="footer">
        <fo:block text-align="end">
            <fo:external-graphic content-height="41mm" content-width="scale-to-fit" src="classpath:/fop/img/erezept-app-note.svg"/>
        </fo:block>
    </xsl:template>

    <xsl:template name="header">
        <fo:table border-style="solid">
            <fo:table-column column-number="1" column-width="15%"/>
            <fo:table-column column-number="2" column-width="75%"/>
            <fo:table-column column-number="3" column-width="10%"/>
            <fo:table-body>
                <fo:table-cell display-align="after">
                    <fo:block-container reference-orientation="0" margin-left="1mm">
                        <fo:block font-size="14pt" font-family="Liberation Sans" font-weight="bold" wrap-option="no-wrap">
                            Medikationsplan
                            <fo:block font-size="12pt">
                            Seite <fo:page-number/>
 von <fo:page-number-citation ref-id="TheVeryLastPage"/>
                        </fo:block>
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell display-align="after">
                <fo:block-container reference-orientation="0" margin-left="1mm">
                    <fo:block font-size="12pt" font-family="Liberation Sans" font-weight="bold" wrap-option="no-wrap">
                            für: <xsl:value-of select="amts:P/@g" />
                    <xsl:value-of select="amts:P/@f" />
 geb. am:  <xsl:value-of select="amts:P/@b" />
                </fo:block>
                <fo:block font-size="10pt" font-family="Liberation Sans" font-weight="bold" wrap-option="no-wrap">
                            ausgedruckt von:
                    <xsl:value-of select="amts:A/@n" />
                    <xsl:value-of select="amts:A/@s" />
                    <xsl:value-of select="amts:A/@z" />
                    <xsl:value-of select="amts:A/@c" />
 
                            Tel: <xsl:value-of select="amts:A/@p" />
                            E-Mail: <xsl:value-of select="amts:A/@e" />
                            ausgedruckt am: <xsl:value-of select="amts:A/@t" />
                </fo:block>
            </fo:block-container>
        </fo:table-cell>
        <fo:table-cell display-align="after">
            <fo:block-container reference-orientation="0" margin-left="1mm">
                <fo:block>
                    <fo:instream-foreign-object>
                        <barcode:barcode>
                                            TODO: Hallo Welt!
                            <barcode:datamatrix>
                                <barcode:module-width>0.52mm</barcode:module-width>
                                <barcode:min-symbol-size>90</barcode:min-symbol-size>
                                <barcode:max-symbol-size>100</barcode:max-symbol-size>
                            </barcode:datamatrix>
                        </barcode:barcode>
                    </fo:instream-foreign-object>
                </fo:block>
            </fo:block-container>
        </fo:table-cell>
        </fo:table-body>
        </fo:table>
        </xsl:template>

        <xsl:template name="body">
        <fo:table table-layout="fixed" width="190mm">
        <fo:table-column column-number="1" column-width="15%"/>
        <fo:table-column column-number="2" column-width="15%"/>
        <fo:table-column column-number="3" column-width="5%"/>
        <fo:table-column column-number="4" column-width="5%"/>
        <fo:table-column column-number="5" column-width="2.5%"/>
        <fo:table-column column-number="6" column-width="2.5%"/>
        <fo:table-column column-number="7" column-width="2.5%"/>
        <fo:table-column column-number="8" column-width="2.5%"/>
        <fo:table-column column-number="9" column-width="5%"/>
        <fo:table-column column-number="10" column-width="25%"/>
        <fo:table-column column-number="11" column-width="20%"/>
        <fo:table-header background-color="#F38222" border-width="1pt" border-style="solid">
            <fo:table-row>
                <fo:table-cell>
                    <fo:block>
                            Wirkstoff
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Handelsname
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Stärke
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Form
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="center">
                    <fo:block>
                            morgens
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="center">
                    <fo:block>
                            mittags
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="center">
                    <fo:block>
                            abends
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell text-align="center">
                    <fo:block>
                            zur Nacht
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Einheit
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Hinweis
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block>
                            Grund
                    </fo:block>
                </fo:table-cell>
            </fo:table-row>
        </fo:table-header>
        <fo:table-body>
            <xsl:for-each select="//amts:M">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <xsl:value-of select="@p" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                TODO
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                <xsl:value-of select="@i" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block>
                                <xsl:value-of select="@r" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </xsl:for-each>
        </fo:table-body>
        </fo:table>
    <fo:block id="TheVeryLastPage"></fo:block>
    </xsl:template>
</xsl:stylesheet>
