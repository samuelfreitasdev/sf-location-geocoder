CREATE SEQUENCE geocoder_problem_pk_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE;

CREATE TABLE geocoder_problem
(
	id         BIGINT       NOT NULL DEFAULT nextval('geocoder_problem_pk_seq'),
	name       VARCHAR(100) NOT NULL,
	points     JSON         NOT NULL,
	created_at TIMESTAMP    NOT NULL,
	updated_at TIMESTAMP    NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE geocoder_solver_request
(
	request_key UUID        NOT NULL,
	problem_id  BIGINT      NOT NULL,
	solver      VARCHAR(50) NOT NULL,
	status      VARCHAR(20) NOT NULL,
	created_at  TIMESTAMP   NOT NULL,
	updated_at  TIMESTAMP   NOT NULL,
	PRIMARY KEY (request_key),
	CONSTRAINT fk_problem_id FOREIGN KEY (problem_id) REFERENCES geocoder_problem (id)
);

CREATE SEQUENCE geocoder_solver_solution_pk_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE;

CREATE TABLE geocoder_solution_request
(
	id          BIGINT      NOT NULL DEFAULT nextval('geocoder_solver_solution_pk_seq'),
	problem_id  BIGINT      NOT NULL,
	request_key UUID,
	status      VARCHAR(20) NOT NULL,
	result      JSON        NOT NULL,
	created_at  TIMESTAMP   NOT NULL,
	updated_at  TIMESTAMP   NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_problem_id FOREIGN KEY (problem_id) REFERENCES geocoder_problem (id) ON DELETE CASCADE,
	CONSTRAINT fk_solver_request_key FOREIGN KEY (request_key) REFERENCES geocoder_solver_request (request_key)
);

CREATE TABLE geocoder_solution
(
	geocoder_problem_id BIGINT    NOT NULL,
	suggestedCoordinate JSON      NOT NULL,
	created_at          TIMESTAMP NOT NULL,
	updated_at          TIMESTAMP NOT NULL,
	PRIMARY KEY (geocoder_problem_id),
	CONSTRAINT fk_geocoder_problem_id FOREIGN KEY (geocoder_problem_id) REFERENCES geocoder_problem (id) ON DELETE CASCADE
);
