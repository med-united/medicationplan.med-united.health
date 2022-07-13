package health.medunited.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import health.medunited.bmp.MedikationsPlan;
import health.medunited.resource.reader.MessageBodyReaderXmlMedicationPlan;

class MedicationPlanServiceTest {
    @Test
    void testCreateMedicationPlan() throws IOException, SAXException, WebApplicationException, TransformerException, JAXBException {
        MedicationPlanService medicationPlanService = new MedicationPlanService();

        ByteArrayOutputStream boas = medicationPlanService.generatePdf(
            new MessageBodyReaderXmlMedicationPlan().readFrom(MedikationsPlan.class, null, null, null, null, getClass().getResourceAsStream("/MedicationPlan.xml")));

        Files.write(Paths.get("target/MedicationPlan.pdf"), boas.toByteArray());
        
    }

    @Test
    void testCreateMedicationPlanMEDU218() throws IOException, SAXException, WebApplicationException, TransformerException, JAXBException {
        MedicationPlanService medicationPlanService = new MedicationPlanService();

        ByteArrayOutputStream boas = medicationPlanService.generatePdf(
            new MessageBodyReaderXmlMedicationPlan().readFrom(MedikationsPlan.class, null, null, null, null, getClass().getResourceAsStream("/MedicationPlan-MEDU-218.xml")));

        Files.write(Paths.get("target/MedicationPlan-MEDU-218.pdf"), boas.toByteArray());
        
    }
}
