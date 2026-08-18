ALTER TABLE companies
    ADD company_type VARCHAR(255);

ALTER TABLE companies
    ALTER COLUMN company_type SET NOT NULL;