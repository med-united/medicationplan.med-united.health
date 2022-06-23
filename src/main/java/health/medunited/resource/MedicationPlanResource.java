package health.medunited.resource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import health.medunited.model.MedicationPlan;
import health.medunited.service.MedicationPlanService;
import org.apache.fop.apps.FOPException;
import org.hl7.fhir.r4.model.Bundle;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/medicationPlanPdf")
public class MedicationPlanResource {

    @Inject
    MedicationPlanService medicationPlanService;

    IParser jsonParser = FhirContext.forR4().newJsonParser();
    IParser xmlParser = FhirContext.forR4().newXmlParser();

    @POST
    @Produces("application/pdf")
    public Response generatePdf(String bundlesString) throws IOException {

        System.out.println(bundlesString);

        JsonArray jsonArray = Json.createReader(new StringReader(bundlesString)).readArray();

        List<MedicationPlan> bundles = jsonArray.stream().map(this::convert).collect(Collectors.toList());

        ByteArrayOutputStream boas;
        try {
            boas = medicationPlanService.generatePdf(bundles);
        } catch (FOPException | IOException | TransformerException e) {
            throw new WebApplicationException(e);
        }

        return Response.ok().entity(boas.toByteArray()).type("application/pdf").build();
    }

    private MedicationPlan convert(JsonValue jv) {
        MedicationPlan bt = new MedicationPlan();
        if(jv instanceof JsonObject) {
            JsonObject jo = (JsonObject) jv;
            String mimeType = jo.getString("mimeType", "application/json");
            if("application/xml".equals(mimeType)) {
                bt.setBundle(jo.getJsonString("bundle").getString());
            } else {
                bt.setBundle(jo.getJsonObject("bundle").toString());
            }
        }
        return bt;
    }

}