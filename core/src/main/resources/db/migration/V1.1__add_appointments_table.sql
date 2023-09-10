CREATE TABLE appointments (
    id uuid PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "date" TIMESTAMP NOT NULL DEFAULT now(),
    patient_id uuid,
    tenant_id uuid,
    CONSTRAINT fk_appointment_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_appointment_patient_id FOREIGN KEY (patient_id) REFERENCES patients(id)
);