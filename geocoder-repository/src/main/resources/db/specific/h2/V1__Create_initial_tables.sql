CREATE SEQUENCE geocoder_problem_pk_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE;

CREATE TABLE geocoder_problem
(
	id         BIGINT DEFAULT NEXT VALUE FOR geocoder_problem_pk_seq PRIMARY KEY,
	name       VARCHAR(100) NOT NULL,
	points     JSON         NOT NULL,
	created_at TIMESTAMP    NOT NULL,
	updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE geocoder_solver_request
(
	request_key UUID PRIMARY KEY,
	problem_id  BIGINT      NOT NULL,
	solver      VARCHAR(50) NOT NULL,
	status      VARCHAR(20) NOT NULL,
	created_at  TIMESTAMP   NOT NULL,
	updated_at  TIMESTAMP   NOT NULL,
	FOREIGN KEY (problem_id) REFERENCES geocoder_problem (id)
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
	id          BIGINT DEFAULT NEXT VALUE FOR geocoder_solver_solution_pk_seq PRIMARY KEY,
	problem_id  BIGINT      NOT NULL,
	request_key UUID,
	status      VARCHAR(20) NOT NULL,
	result      JSON        NOT NULL,
	created_at  TIMESTAMP   NOT NULL,
	updated_at  TIMESTAMP   NOT NULL,
	FOREIGN KEY (problem_id) REFERENCES geocoder_problem (id) ON DELETE CASCADE,
	FOREIGN KEY (request_key) REFERENCES geocoder_solver_request (request_key)
);

CREATE TABLE geocoder_solution
(
	geocoder_problem_id BIGINT PRIMARY KEY,
	suggestedCoordinate JSON      NOT NULL,
	created_at          TIMESTAMP NOT NULL,
	updated_at          TIMESTAMP NOT NULL,
	FOREIGN KEY (geocoder_problem_id) REFERENCES geocoder_problem (id) ON DELETE CASCADE
);
