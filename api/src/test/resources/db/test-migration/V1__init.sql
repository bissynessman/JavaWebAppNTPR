DROP TABLE IF EXISTS grade CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS professor CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS user CASCADE;
CREATE TABLE course
(
	ects              INTEGER     NOT NULL,
	id                VARCHAR(36) NOT NULL,
	professor         VARCHAR(36),
	name              VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE grade
(
	grade             INTEGER     NOT NULL,
	course            VARCHAR(36),
	id                VARCHAR(36) NOT NULL,
	student           VARCHAR(36),
	PRIMARY KEY (id)
);
CREATE TABLE professor
(
	authorized        BOOLEAN DEFAULT FALSE NOT NULL,
	id                VARCHAR(36)           NOT NULL,
	first_name        VARCHAR(50)           NOT NULL,
	last_name         VARCHAR(50)           NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE student
(
	major             VARCHAR(32) NOT NULL,
	jmbag             VARCHAR(11) NOT NULL,
	id                VARCHAR(36) NOT NULL,
	first_name        VARCHAR(50) NOT NULL,
	last_name         VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE user
(
	role              VARCHAR(32)  NOT NULL,
	id                VARCHAR(36)  NOT NULL,
	user_uuid         VARCHAR(36),
	password          VARCHAR(255) NOT NULL,
	username          VARCHAR(32)  NOT NULL,
	PRIMARY KEY (id)
);
ALTER TABLE course ADD CONSTRAINT courseProfessorConstraint FOREIGN KEY (professor) REFERENCES professor(id);
ALTER TABLE grade ADD CONSTRAINT gradeCourseConstraint FOREIGN KEY (course) REFERENCES course(id);
ALTER TABLE grade ADD CONSTRAINT gradeStudentConstraint FOREIGN KEY (student) REFERENCES student(id);