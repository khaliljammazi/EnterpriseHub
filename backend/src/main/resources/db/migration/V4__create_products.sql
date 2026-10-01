CREATE TABLE products (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    created_by_user_id UUID NOT NULL REFERENCES users(id),
    sku VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    price NUMERIC(19, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INTEGER NOT NULL CHECK (stock_quantity >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT products_company_sku_unique UNIQUE (company_id, sku)
);

CREATE INDEX products_company_id_index ON products (company_id);
CREATE INDEX products_created_by_user_id_index ON products (created_by_user_id);
