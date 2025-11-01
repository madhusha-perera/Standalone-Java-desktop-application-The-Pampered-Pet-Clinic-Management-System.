-- Create the database
CREATE DATABASE IF NOT EXISTS pamperedpetclinic;
USE pamperedpetclinic;

-- owner table
CREATE TABLE owner (
  owner_id     INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  owner_code   VARCHAR(7)  NOT NULL UNIQUE,
  fname        VARCHAR(45) NOT NULL,
  lname        VARCHAR(45) NOT NULL,
  email        VARCHAR(45) NOT NULL UNIQUE,
  address      VARCHAR(200),
  phone_number INT(10),
  username     VARCHAR(45) NOT NULL UNIQUE,
  password     VARCHAR(45) NOT NULL
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER owner_before_insert
  BEFORE INSERT ON owner
  FOR EACH ROW
BEGIN
  SET NEW.owner_code = CONCAT('OWN', LPAD(NEW.owner_id,4,'0'));
END$$
DELIMITER ;

-- veterinarian table
CREATE TABLE veterinarian (
  vet_id        INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  vet_code      VARCHAR(7)  NOT NULL UNIQUE,
  fname         VARCHAR(45) NOT NULL,
  lname         VARCHAR(45) NOT NULL,
  specialization VARCHAR(45),
  phone_number  INT(10),
  username      VARCHAR(45) NOT NULL UNIQUE,
  password      VARCHAR(45) NOT NULL,
  dob           DATE,
  address       VARCHAR(250)
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER veterinarian_before_insert
  BEFORE INSERT ON veterinarian
  FOR EACH ROW
BEGIN
  SET NEW.vet_code = CONCAT('VET', LPAD(NEW.vet_id,4,'0'));
END$$
DELIMITER ;

-- receptionist table
CREATE TABLE receptionist (
  receptionist_id   INT                             NOT NULL AUTO_INCREMENT PRIMARY KEY,
  receptionist_code VARCHAR(7)                      NOT NULL UNIQUE,
  username          VARCHAR(45)                     NOT NULL UNIQUE,
  password          VARCHAR(45)                     NOT NULL,
  fname             VARCHAR(45)                     NOT NULL,
  lname             VARCHAR(45)                     NOT NULL,
  role              ENUM('RECEPTIONIST','HEAD_RECEPTIONIST') NOT NULL DEFAULT 'RECEPTIONIST',
  phone_number      INT(10),
  address           VARCHAR(200),
  dob               DATE
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER receptionist_before_insert
  BEFORE INSERT ON receptionist
  FOR EACH ROW
BEGIN
  SET NEW.receptionist_code = CONCAT('REC', LPAD(NEW.receptionist_id,4,'0'));
END$$
DELIMITER ;

-- pet table
CREATE TABLE pet (
  pet_id     INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  pet_code   VARCHAR(7)  NOT NULL UNIQUE,
  name       VARCHAR(45) NOT NULL,
  species    VARCHAR(45),
  breed      VARCHAR(45),
  gender     CHAR(1),
  dob        DATE,
  owner_id   INT         NOT NULL,
  CONSTRAINT fk_pet_owner
    FOREIGN KEY (owner_id)
    REFERENCES owner(owner_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER pet_before_insert
  BEFORE INSERT ON pet
  FOR EACH ROW
BEGIN
  SET NEW.pet_code = CONCAT('PET', LPAD(NEW.pet_id,4,'0'));
END$$
DELIMITER ;

-- petmedicalrecord table
CREATE TABLE petmedicalrecord (
  record_id   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  record_code VARCHAR(7)  NOT NULL UNIQUE,
  pet_id      INT         NOT NULL,
  vet_id      INT         NOT NULL,
  visit_date  DATE,
  diagnosis   VARCHAR(200),
  treatment   VARCHAR(200),
  medication  VARCHAR(200),
  CONSTRAINT fk_rec_pet FOREIGN KEY (pet_id)
    REFERENCES pet(pet_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_rec_vet FOREIGN KEY (vet_id)
    REFERENCES veterinarian(vet_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER petmedicalrecord_before_insert
  BEFORE INSERT ON petmedicalrecord
  FOR EACH ROW
BEGIN
  SET NEW.record_code = CONCAT('PMR', LPAD(NEW.record_id,4,'0'));
END$$
DELIMITER ;

-- appointment table
CREATE TABLE appointment (
  appointment_id   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  appointment_code VARCHAR(7)  NOT NULL UNIQUE,
  datetime         DATETIME    NOT NULL,
  reason           VARCHAR(200),
  owner_id         INT         NOT NULL,
  pet_id           INT         NOT NULL,
  vet_id           INT         NOT NULL,
  receptionist_id  INT         NOT NULL,
  CONSTRAINT fk_app_owner FOREIGN KEY (owner_id)
    REFERENCES owner(owner_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_app_pet   FOREIGN KEY (pet_id)
    REFERENCES pet(pet_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_app_vet   FOREIGN KEY (vet_id)
    REFERENCES veterinarian(vet_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_app_recep FOREIGN KEY (receptionist_id)
    REFERENCES receptionist(receptionist_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;

DELIMITER $$
CREATE TRIGGER appointment_before_insert
  BEFORE INSERT ON appointment
  FOR EACH ROW
BEGIN
  SET NEW.appointment_code = CONCAT('APP', LPAD(NEW.appointment_id,4,'0'));
END$$
DELIMITER ;

ALTER TABLE appointment
  MODIFY vet_id         INT     NULL,
  MODIFY receptionist_id INT     NULL,
  ADD COLUMN status ENUM('REQUESTED','SCHEDULED','COMPLETED','CANCELLED')
    NOT NULL DEFAULT 'REQUESTED';


ALTER TABLE receptionist 
  MODIFY COLUMN dob DATE NOT NULL;
  
INSERT INTO receptionist
  (username, password, fname, lname, role, phone_number, address, dob)
VALUES
  ('headrec',
   'headrec123',
   'Saman',
   'Kumara',
   'HEAD_RECEPTIONIST',
   '0771234567',
   'Colombo', 
   '1985-07-19'  
  );