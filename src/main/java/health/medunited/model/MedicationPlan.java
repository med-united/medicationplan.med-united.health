package health.medunited.model;

import org.hl7.fhir.r4.model.Bundle;

public class MedicationPlan {

    private Bundle bundle;

    public MedicationPlan() {
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
