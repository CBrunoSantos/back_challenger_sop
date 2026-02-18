CREATE TYPE orcamento_status AS ENUM ('ABERTO', 'FINALIZADO');
CREATE TYPE medicao_status AS ENUM ('ABERTA', 'VALIDADA');
CREATE TYPE orcamento_tipo AS ENUM ('OBRA_EDIFICACAO', 'OBRA_RODOVIAS', 'OUTROS');

CREATE TABLE orcamento(
	id BIGSERIAL PRIMARY KEY,
	num_protcl VARCHAR(40) NOT NULL UNIQUE,
	type_orc orcamento_tipo NOT NULL,
	total_value NUMERIC(15,2) NOT NULL CHECK (total_value >= 0),
	create_at DATE NOT NULL DEFAULT CURRENT_DATE,
	status orcamento_status NOT NULL DEFAULT 'ABERTO'
);

CREATE TABLE item (
	id BIGSERIAL PRIMARY KEY,
	description VARCHAR(255) NOT NULL,
	quantity NUMERIC(15,3) NOT NULL CHECK (quantity >= 0),
	unit_value NUMERIC(15,2) NOT NULL CHECK (unit_value >= 0),
	total_value NUMERIC(15,2) GENERATED ALWAYS AS (quantity * unit_value) STORED,
	quantity_amount NUMERIC(15,3) NOT NULL DEFAULT 0 CHECK (quantity_amount >= 0),
	orc_id BIGINT NOT NULL,
	CONSTRAINT fk_item_orcamento
		FOREIGN KEY (orc_id) REFERENCES orcamento(id) ON DELETE RESTRICT
);

CREATE TABLE medicao (
	id BIGSERIAL PRIMARY KEY,
	num_measure VARCHAR(40) NOT NULL UNIQUE,
	date_measure DATE NOT NULL DEFAULT CURRENT_DATE,
	value_measure NUMERIC(15,2) NOT NULL DEFAULT 0 CHECK (value_measure >= 0),
	status medicao_status NOT NULL DEFAULT 'ABERTA',
	observacao TEXT,
	orc_id BIGINT NOT NULL,
	CONSTRAINT fk_medicao_orcamento
		FOREIGN KEY (orc_id) REFERENCES orcamento(id) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX uq_medicao_aberta_por_orcamento ON medicao (orc_id) WHERE status = 'ABERTA';

CREATE TABLE item_medicao (
	id BIGSERIAL PRIMARY KEY,
	qtd_measure NUMERIC(15,3) NOT NULL CHECK (QTD_MEASURE > 0),
	unit_value_applied NUMERIC(15,2) NOT NULL CHECK (unit_value_applied >= 0),
	total_value_msr NUMERIC(15,2) GENERATED ALWAYS AS (qtd_measure * unit_value_applied) STORED,
	item_id BIGINT NOT NULL,
	measure_id BIGINT NOT NULL,
	CONSTRAINT fk_item_medicao_item
		FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE RESTRICT,
	CONSTRAINT fk_item_medicao_medicao
		FOREIGN KEY (measure_id) REFERENCES medicao(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uq_item_por_medicao ON item_medicao (measure_id, item_id);

SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

SELECT indexname, indexdef FROM pg_indexes WHERE tablename = 'medicao';

