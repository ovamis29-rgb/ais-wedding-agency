CREATE SCHEMA ais_wedding_agency;

CREATE OR REPLACE FUNCTION ais_wedding_agency.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE ais_wedding_agency.client_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.client (
	id BIGINT DEFAULT nextval('ais_wedding_agency.client_sq') PRIMARY KEY,
	phone_number VARCHAR(20) NOT NULL UNIQUE,
	email VARCHAR(50) UNIQUE,
	name VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	gender VARCHAR(1) NOT NULL CHECK(gender IN('М','Ж')),
	date_of_registration DATE NOT NULL,
	place_of_birth VARCHAR(100) NOT NULL,
	series VARCHAR(4) NOT NULL,
	number VARCHAR(6) NOT NULL,
	issued_by VARCHAR(50) NOT NULL,
	when_issued DATE NOT NULL,
	registration_adress VARCHAR(100) NOT NULL,
	CONSTRAINT fio_client UNIQUE(name,lastname),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);


CREATE SEQUENCE ais_wedding_agency.project_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.project (
	id BIGINT DEFAULT nextval('ais_wedding_agency.project_sq') PRIMARY KEY,
	wedding_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'Europe/Moscow' + INTERVAL '1 day'),
	status VARCHAR DEFAULT 'ожидает выполнения' 
		CHECK(status IN('ожидает выполнения','в процессе','завершено','отменено')),
	wishes_for_wedding VARCHAR(1000),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);


CREATE SEQUENCE ais_wedding_agency.project_client_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.project_client(
	id BIGINT DEFAULT nextval('ais_wedding_agency.project_client_sq') PRIMARY KEY,
	project_id BIGINT NOT NULL REFERENCES ais_wedding_agency.project(id) ON DELETE CASCADE,
	client_id BIGINT NOT NULL REFERENCES ais_wedding_agency.client(id) ON DELETE CASCADE,
	role VARCHAR(7) NOT NULL 
		CHECK(role IN ('жених','невеста'))
);

CREATE SEQUENCE ais_wedding_agency.guest_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.guest (
	id BIGINT DEFAULT nextval('ais_wedding_agency.guest_sq') PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	CONSTRAINT fio_guest UNIQUE(name,lastname),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);



CREATE SEQUENCE ais_wedding_agency.contractor_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.contractor (
	id BIGINT DEFAULT nextval('ais_wedding_agency.contractor_sq') PRIMARY KEY,
	phone_number VARCHAR(20) NOT NULL UNIQUE,
	email VARCHAR(50) UNIQUE,
	name VARCHAR(150) NOT NULL,
	lastname VARCHAR(30),
	CONSTRAINT fio_contractor UNIQUE(name,lastname),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE ais_wedding_agency.portfolio_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.portfolio (
	id BIGINT DEFAULT nextval('ais_wedding_agency.portfolio_sq') PRIMARY KEY,
	contractor_id BIGINT NOT NULL REFERENCES ais_wedding_agency.contractor(id) ON DELETE CASCADE,
	description VARCHAR(100) NOT NULL,
	image_url VARCHAR(600) NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE ais_wedding_agency.contractor_service_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.contractor_service (
	id BIGINT DEFAULT nextval('ais_wedding_agency.contractor_service_sq') PRIMARY KEY,
	contractor_id BIGINT NOT NULL REFERENCES ais_wedding_agency.contractor(id) ON DELETE CASCADE,
	service VARCHAR(50) NOT NULL,
	price DECIMAL(10,2) NOT NULL,
	prepayment DECIMAL(10,2),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);


CREATE SEQUENCE ais_wedding_agency.used_contractor_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.used_contractor (
	id BIGINT DEFAULT nextval('ais_wedding_agency.used_contractor_sq') PRIMARY KEY,
	contractor_id BIGINT NOT NULL REFERENCES ais_wedding_agency.contractor(id) ON DELETE CASCADE,
	project_id BIGINT NOT NULL REFERENCES ais_wedding_agency.project(id) ON DELETE CASCADE
);


CREATE SEQUENCE ais_wedding_agency.report_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.report (
	id BIGINT DEFAULT nextval('ais_wedding_agency.report_sq') PRIMARY KEY,
	project_id BIGINT NOT NULL REFERENCES ais_wedding_agency.project(id) ON DELETE CASCADE,
	total_budget DECIMAL(15,4) NOT NULL,
	amount_of_guests BIGINT,
	completed_tasks_count BIGINT NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE ais_wedding_agency.task_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.task (
    id BIGINT DEFAULT nextval('ais_wedding_agency.task_sq') PRIMARY KEY,
    contractor_service_id BIGINT NOT NULL 
        REFERENCES ais_wedding_agency.contractor_service(id) ON DELETE CASCADE,
    project_id BIGINT NOT NULL 
        REFERENCES ais_wedding_agency.project(id) ON DELETE CASCADE,
    status VARCHAR DEFAULT 'ожидает выполнения' 
        CHECK(status IN ('ожидает выполнения','в процессе','завершено','отменено')),
    price DECIMAL(15,4) NOT NULL,
    deadline TIMESTAMP WITH TIME ZONE NOT NULL,
    priority INTEGER DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE ais_wedding_agency.project_guest_sq
MINVALUE 1
MAXVALUE 9999
START WITH 1
INCREMENT BY 1
NO CYCLE;

CREATE TABLE ais_wedding_agency.project_guest (
	id BIGINT DEFAULT nextval('ais_wedding_agency.project_guest_sq') PRIMARY KEY,
	guest_id BIGINT NOT NULL REFERENCES ais_wedding_agency.guest(id) ON DELETE CASCADE,
	project_id BIGINT NOT NULL REFERENCES ais_wedding_agency.project(id) ON DELETE CASCADE,
	CONSTRAINT unique_project_guest UNIQUE(project_id,guest_id)
);

CREATE TRIGGER trigger_client_updated_at
    BEFORE UPDATE ON ais_wedding_agency.client
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_project_updated_at
    BEFORE UPDATE ON ais_wedding_agency.project
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_guest_updated_at
    BEFORE UPDATE ON ais_wedding_agency.guest
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_contractor_updated_at
    BEFORE UPDATE ON ais_wedding_agency.contractor
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_portfolio_updated_at
    BEFORE UPDATE ON ais_wedding_agency.portfolio
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();


CREATE TRIGGER trigger_contractor_service_updated_at
    BEFORE UPDATE ON ais_wedding_agency.contractor_service
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_report_updated_at
    BEFORE UPDATE ON ais_wedding_agency.report
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();

CREATE TRIGGER trigger_task_updated_at
    BEFORE UPDATE ON ais_wedding_agency.task
    FOR EACH ROW
    EXECUTE FUNCTION ais_wedding_agency.update_updated_at_column();


CREATE OR REPLACE FUNCTION ais_wedding_agency.get_report_stats(pr_id BIGINT)
RETURNS TABLE(
	completed_tasks_count BIGINT,
    total_budget DECIMAL(15,4),
    amount_of_guests BIGINT
)
AS $$
BEGIN
	SELECT COUNT(*) INTO completed_tasks_count FROM ais_wedding_agency.task 
		WHERE project_id = pr_id AND status = 'завершено';
	SELECT COALESCE(SUM(price),0) INTO total_budget FROM ais_wedding_agency.task 
		WHERE project_id = pr_id AND status = 'завершено';
	SELECT COUNT(*) INTO amount_of_guests FROM ais_wedding_agency.project_guest 
		WHERE project_id = pr_id;
	RETURN NEXT;	 
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION ais_wedding_agency.is_it_project_guest(pr_id BIGINT,gst_id BIGINT)
RETURNS BOOLEAN
AS $$
BEGIN
	RETURN EXISTS(SELECT 1 FROM ais_wedding_agency.project_guest WHERE project_id = pr_id AND guest_id = gst_id);	 
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ais_wedding_agency.contrHaveTasksInProject(pr_id BIGINT,contr_id BIGINT)
RETURNS BOOLEAN
AS $$
BEGIN
	RETURN EXISTS(SELECT 1 FROM ais_wedding_agency.task t
        JOIN ais_wedding_agency.contractor_service cs ON t.contractor_service_id = cs.id
        WHERE t.project_id = pr_id AND cs.contractor_id = contr_id);	 
END;
$$ LANGUAGE plpgsql;

SELECT ais_wedding_agency.contrHaveTasksInProject(3, 5);
CREATE OR REPLACE FUNCTION ais_wedding_agency.service_search(descr VARCHAR,pr DECIMAL(10,2),prepay DECIMAL(10,2),cont_id BIGINT)
RETURNS SETOF  ais_wedding_agency.contractor_service
AS $$
	SELECT * FROM ais_wedding_agency.contractor_service WHERE
	(descr IS NULL OR service ILIKE '%' || descr || '%') AND
	(pr IS NULL OR price = pr) AND (prepay IS NULL OR prepayment = prepay)
	AND contractor_id = cont_id;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION ais_wedding_agency.task_search(descr VARCHAR,st VARCHAR,pr_id BIGINT)
RETURNS SETOF  ais_wedding_agency.task
AS $$
	SELECT t.* FROM ais_wedding_agency.task t 
	JOIN ais_wedding_agency.contractor_service cs ON t.contractor_service_id = cs.id
	WHERE project_id = pr_id AND
	(descr IS NULL OR cs.service ILIKE '%' || descr || '%') AND
	(st IS NULL OR t.status ILIKE '%' || st || '%');
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION ais_wedding_agency.service_exists(p_contractor_id BIGINT,p_service_name VARCHAR)
RETURNS BOOLEAN
AS $$
BEGIN
    RETURN EXISTS(
        SELECT 1 
        FROM ais_wedding_agency.contractor_service 
        WHERE contractor_id = p_contractor_id 
          AND service ILIKE p_service_name);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ais_wedding_agency.portfolio_exists(p_contractor_id BIGINT,p_image_url VARCHAR)
RETURNS BOOLEAN
AS $$
BEGIN
    RETURN EXISTS(
        SELECT 1 
        FROM ais_wedding_agency.portfolio 
        WHERE contractor_id = p_contractor_id AND image_url = p_image_url);
END;
$$ LANGUAGE plpgsql;

CREATE INDEX idx_client_name_lastname ON ais_wedding_agency.client(name, lastname);
CREATE INDEX idx_client_phone ON ais_wedding_agency.client(phone_number);
CREATE INDEX idx_client_registration_date ON ais_wedding_agency.client(date_of_registration);

CREATE INDEX idx_guest_name_lastname ON ais_wedding_agency.guest(name, lastname);

CREATE INDEX idx_contractor_name ON ais_wedding_agency.contractor(name);
CREATE INDEX idx_contractor_phone ON ais_wedding_agency.contractor(phone_number);

CREATE INDEX idx_service_name ON ais_wedding_agency.contractor_service(service);

CREATE INDEX idx_project_wedding_date ON ais_wedding_agency.project(wedding_date);
CREATE INDEX idx_project_created_at ON ais_wedding_agency.project(created_at);

CREATE INDEX idx_project_guest_project ON ais_wedding_agency.project_guest(project_id);
CREATE INDEX idx_project_guest_guest ON ais_wedding_agency.project_guest(guest_id);

CREATE INDEX idx_project_client_project ON ais_wedding_agency.project_client(project_id);
CREATE INDEX idx_project_client_client ON ais_wedding_agency.project_client(client_id);

CREATE INDEX idx_task_project ON ais_wedding_agency.task(project_id);
CREATE INDEX idx_task_deadline ON ais_wedding_agency.task(deadline);

CREATE INDEX idx_report_project ON ais_wedding_agency.report(project_id);

CREATE INDEX idx_used_contractor_project ON ais_wedding_agency.used_contractor(project_id);
CREATE INDEX idx_used_contractor_contractor ON ais_wedding_agency.used_contractor(contractor_id);

CREATE INDEX idx_portfolio_contractor ON ais_wedding_agency.portfolio(contractor_id);

CREATE USER manager WITH PASSWORD 'admin';
GRANT CONNECT ON DATABASE postgres TO manager;
GRANT USAGE ON SCHEMA ais_wedding_agency TO manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA ais_wedding_agency
TO manager;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA ais_wedding_agency TO manager;
