package health.medunited.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import health.medunited.bmp.MedikationsPlan;
import health.medunited.resource.reader.MessageBodyReaderXmlMedicationPlan;

@ApplicationScoped
public class MedicationPlanService {

    private static final Logger log = Logger.getLogger(MedicationPlanService.class.getName());

    private FopFactory fopFactory;

    public MedicationPlanService() {
        try {
            URL url = MedicationPlanService.class.getResource("/fop/fop.xconf");
            URI uri = url.toURI();
            fopFactory = FopFactory.newInstance(uri);
        } catch (URISyntaxException e) {
            log.log(Level.SEVERE, "Could not create FopFactory", e);
        }
    }

    public ByteArrayOutputStream generatePdf(MedikationsPlan medicationPlan) throws IOException, FOPException, TransformerException, JAXBException {

        File xml = createTemporaryXmlFileFromBundles(medicationPlan);
        return generatePdfInOutputStream(xml);
    }

    private File createTemporaryXmlFileFromBundles(MedikationsPlan medicationPlans) throws IOException, JAXBException {
        
        File tmpFile = Files.createTempFile("medication-plan-", ".xml").toFile();
        MessageBodyReaderXmlMedicationPlan.jaxbContext.createMarshaller().marshal(medicationPlans, tmpFile);
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
        String xslPath = "/META-INF/MedicationPlan.xsl";

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
        transformer.setParameter("medicationPlanFileUrl", xml.toURI().toURL().toString());
        transformer.setParameter("medicationPlanFileContent", Files.readString(xml.toPath()));
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
