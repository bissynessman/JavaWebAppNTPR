DROP DATABASE IF EXISTS ntprdb;

DROP USER IF EXISTS `admin`@`%`;

CREATE DATABASE IF NOT EXISTS ntprdb
  CHARACTER SET utf8mb4
	    COLLATE utf8mb4_unicode_ci;

    CREATE USER IF NOT EXISTS `admin`@`%`
IDENTIFIED WITH mysql_native_password
	    BY 'password';

GRANT SELECT,
	  INSERT,
	  UPDATE,
	  DELETE,
	  CREATE,
      DROP,
	  REFERENCES,
	  INDEX,
	  ALTER,
	  EXECUTE,
	  CREATE VIEW,
	  SHOW VIEW,
	  CREATE ROUTINE,
	  ALTER ROUTINE,
	  EVENT,
	  TRIGGER
   ON `ntprdb`.*
   TO `admin`@`%`;

FLUSH PRIVILEGES;