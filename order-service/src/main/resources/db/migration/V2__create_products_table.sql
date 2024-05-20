CREATE TABLE IF NOT EXISTS product
(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	count INTEGER NOT NULL,
	cost DOUBLE PRECISION NOT NULL
);