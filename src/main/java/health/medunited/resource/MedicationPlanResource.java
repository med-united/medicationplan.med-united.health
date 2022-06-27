package health.medunited.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

import health.medunited.bmp.MedikationsPlan;
import health.medunited.service.MedicationPlanService;

@Path("/medicationPlanPdf")
public class MedicationPlanResource {

    @Inject
    MedicationPlanService medicationPlanService;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("application/pdf")
    public Response generatePdf(MedikationsPlan medicationPlan) throws IOException {

        ByteArrayOutputStream boas;
        try {
            boas = medicationPlanService.generatePdf(medicationPlan);
        } catch (FOPException | IOException | TransformerException | JAXBException e) {
            throw new WebApplicationException(e);
        }

        return Response.ok().entity(boas.toByteArray()).type("application/pdf").build();
    }

}