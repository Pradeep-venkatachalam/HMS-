-- ============================================================
--  Hospital Management System — Database Schema
--  Database: hms
-- ============================================================

CREATE DATABASE IF NOT EXISTS hms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hms;

-- Users
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    full_name   VARCHAR(100) NOT NULL,
    role        ENUM('ADMIN','DOCTOR','RECEPTIONIST','PATIENT') NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Patients
CREATE TABLE IF NOT EXISTS patients (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name        VARCHAR(50)  NOT NULL,
    last_name         VARCHAR(50)  NOT NULL,
    email             VARCHAR(100) UNIQUE,
    phone             VARCHAR(20),
    date_of_birth     DATE,
    gender            VARCHAR(10),
    address           TEXT,
    blood_group       VARCHAR(5),
    emergency_contact VARCHAR(100),
    medical_history   TEXT,
    user_id           BIGINT,
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Doctors
CREATE TABLE IF NOT EXISTS doctors (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name           VARCHAR(50)  NOT NULL,
    last_name            VARCHAR(50)  NOT NULL,
    email                VARCHAR(100) UNIQUE,
    phone                VARCHAR(20),
    specialization       VARCHAR(100) NOT NULL,
    qualification        VARCHAR(200),
    license_number       VARCHAR(50)  UNIQUE,
    consultation_fee     DECIMAL(10,2),
    available_days       VARCHAR(100),
    available_time_start VARCHAR(10),
    available_time_end   VARCHAR(10),
    active               BOOLEAN NOT NULL DEFAULT TRUE,
    user_id              BIGINT,
    created_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Appointments
CREATE TABLE IF NOT EXISTS appointments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id       BIGINT NOT NULL,
    doctor_id        BIGINT NOT NULL,
    appointment_date DATE   NOT NULL,
    appointment_time TIME   NOT NULL,
    status           ENUM('SCHEDULED','CONFIRMED','COMPLETED','CANCELLED','NO_SHOW') NOT NULL DEFAULT 'SCHEDULED',
    reason           TEXT,
    notes            TEXT,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES doctors(id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Prescriptions
CREATE TABLE IF NOT EXISTS prescriptions (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    medications    TEXT   NOT NULL,
    diagnosis      TEXT,
    instructions   TEXT,
    follow_up_date VARCHAR(20),
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Bills
CREATE TABLE IF NOT EXISTS bills (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id   BIGINT         NOT NULL UNIQUE,
    consultation_fee DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    medicine_charge  DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    test_charge      DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    other_charge     DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    total_amount     DECIMAL(10,2)  NOT NULL,
    paid_amount      DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    payment_status   VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    payment_method   VARCHAR(30),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    paid_at          DATETIME,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes
CREATE INDEX idx_apt_date     ON appointments(appointment_date);
CREATE INDEX idx_apt_patient  ON appointments(patient_id);
CREATE INDEX idx_apt_doctor   ON appointments(doctor_id);
CREATE INDEX idx_apt_status   ON appointments(status);
CREATE INDEX idx_bill_status  ON bills(payment_status);
CREATE INDEX idx_doc_spec     ON doctors(specialization);
