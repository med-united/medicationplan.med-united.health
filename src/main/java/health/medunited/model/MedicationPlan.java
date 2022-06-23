package health.medunited.model;

import org.hl7.fhir.r4.model.Bundle;

public class MedicationPlan {

    private String bundle;

    public MedicationPlan() {
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
}
