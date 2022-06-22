package health.medunited.service;

import health.medunited.model.MedicationPlan;
import org.apache.fop.apps.*;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class MedicationPlanService {

    private static final Logger log = Logger.getLogger(MedicationPlanService.class.getName());

    private FopFactory fopFactory = FopFactory.newInstance(new File("src/main/resources/fop/fop.xconf"));;

    public MedicationPlanService() throws IOException, SAXException {
    }

    public ByteArrayOutputStream generatePdf(List<MedicationPlan> medicationPlans) throws IOException, FOPException, TransformerException {

        File xml = createTemporaryXmlFileFromBundles(medicationPlans);
        return generatePdfInOutputStream(xml);
    }

    private File createTemporaryXmlFileFromBundles(List<MedicationPlan> medicationPlans) throws IOException {
        String serialized = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns=\"http://hl7.org/fhir\">\n";

        File tmpFile = Files.createTempFile("bundle-", ".xml").toFile();
        Files.write(tmpFile.toPath(), serialized.getBytes(StandardCharsets.UTF_8));
        return tmpFile;
    }

    private ByteArrayOutputStream generatePdfInOutputStream(File xml) throws FOPException, TransformerException,
            IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Step 3: Construct fop with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

        // Step 4: Setup JAXP using identity transformer
        TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
//
//        // with XSLT:
        String xslPath = "/META-INF/ERezeptTemplate.xsl";

        InputStream inputStream = getClass().getResourceAsStream(xslPath);
        String systemId = this.getClass().getResource(xslPath).toExternalForm();
        StreamSource xslt = new StreamSource(inputStream, systemId);
        xslt.setPublicId(systemId);
        factory.setErrorListener(new ErrorListener() {
            private static final String MSG = "Error in XSLT:";

            @Override
            public void warning(TransformerException exception) {
                log.warning(MSG + exception);

            }

            @Override
            public void fatalError(TransformerException exception) {
                log.severe(MSG + exception);

            }

            @Override
            public void error(TransformerException exception) {
                log.severe(MSG + exception);
            }
        });

        Transformer transformer = factory.newTransformer(xslt);
        transformer.setParameter("bundleFileUrl", xml.toURI().toURL().toString());
//
//        // Step 5: Setup input and output for XSLT transformation
//        // Setup input stream
        Source src = new StreamSource(xml);
//
//        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = new SAXResult(fop.getDefaultHandler());
//
//        // Step 6: Start XSLT transformation and FOP processing
        transformer.transform(src, res);

        return out;

    }

}
