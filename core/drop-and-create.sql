DROP TABLE IF EXISTS grade CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS professor CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS user CASCADE;
CREATE TABLE course
(
	ects              INTEGER     NOT NULL,
	version           INTEGER,
	creation_date     TIMESTAMP(6),
	modification_date TIMESTAMP(6),
	id                VARCHAR(36) NOT NULL,
	professor_id      VARCHAR(36),
	name              VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE grade
(
	grade             INTEGER     NOT NULL,
	version           INTEGER,
	creation_date     TIMESTAMP(6),
	modification_date TIMESTAMP(6),
	course_id         VARCHAR(36),
	id                VARCHAR(36) NOT NULL,
	student_id        VARCHAR(36),
	PRIMARY KEY (id)
);
CREATE TABLE professor
(
	authorization     BOOLEAN DEFAULT FALSE NOT NULL,
	version           INTEGER,
	creation_date     TIMESTAMP(6),
	modification_date TIMESTAMP(6),
	id                VARCHAR(36)           NOT NULL,
	first_name        VARCHAR(50)           NOT NULL,
	last_name         VARCHAR(50)           NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE student
(
	enrolled_ects     NUMERIC(38, 2),
	major             TINYINT CHECK (major BETWEEN 0 AND 3),
	total_ects        NUMERIC(38, 2),
	version           INTEGER,
	creation_date     TIMESTAMP(6),
	modification_date TIMESTAMP(6),
	jmbag             VARCHAR(11) NOT NULL,
	id                VARCHAR(36) NOT NULL,
	first_name        VARCHAR(50) NOT NULL,
	last_name         VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE user
(
	role              TINYINT      NOT NULL CHECK (role BETWEEN 0 AND 2),
	version           INTEGER,
	creation_date     TIMESTAMP(6),
	modification_date TIMESTAMP(6),
	id                VARCHAR(36)  NOT NULL,
	user_uuid         VARCHAR(36),
	password          VARCHAR(255) NOT NULL,
	username          VARCHAR(32)  NOT NULL,
	PRIMARY KEY (id)
);
ALTER TABLE course ADD CONSTRAINT FKqctak3o6xmul2nu2561al3pb5 FOREIGN KEY (professor_id) REFERENCES professor(id);
ALTER TABLE grade ADD CONSTRAINT FK7e8ca7hfmrpruicqhocskjlf2 FOREIGN KEY (course_id) REFERENCES course(id);
ALTER TABLE grade ADD CONSTRAINT FK5secqnjjwgh9wxk4h1xwgj1n0 FOREIGN KEY (student_id) REFERENCES student(id);
