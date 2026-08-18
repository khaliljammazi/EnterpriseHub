CREATE TABLE companies
(
    id           UUID         NOT NULL,
    name         VARCHAR(150) NOT NULL,
    company_type VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_companies PRIMARY KEY (id)
);