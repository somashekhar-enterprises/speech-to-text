CREATE TABLE tenants (
    id uuid PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE patients (
    id uuid PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    attributes TEXT,
    tenant_id uuid,
    created TIMESTAMP NOT NULL DEFAULT now(),
    last_modified TIMESTAMP,
    CONSTRAINT fk_patient_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);