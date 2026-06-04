# 🏥 Hospital Management System

A full-stack Hospital Management System built with **Spring Boot 3**, **MySQL**, and **Vanilla JS**.

---

## 🗂 Project Structure

```
hospital-management-system/
├── backend/               Spring Boot 3 application
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/              Vanilla HTML/CSS/JS
│   ├── index.html         Login page
│   ├── pages/             All dashboard pages
│   ├── css/style.css
│   └── js/
├── database/
│   └── schema.sql         MySQL schema
├── docs/
├── docker-compose.yml
└── README.md
```

---

## ⚙️ Prerequisites

| Tool       | Version     |
|------------|-------------|
| Java       | 17+         |
| Maven      | 3.8+        |
| MySQL      | 8.0+        |
| Node/npm   | optional    |
| Docker     | optional    |

---

## 🚀 Quick Start — Option 1: Docker Compose (Recommended)

```bash
# Clone / extract the project
cd hospital-management-system

# Start everything (MySQL + Backend + Frontend via nginx)
docker-compose up --build

# Access:
#   Frontend → http://localhost:3000
#   Backend  → http://localhost:8080
```

---

## 🚀 Quick Start — Option 2: Manual Setup

### 1. Database

```sql
-- Log into MySQL
mysql -u root -p

-- Run the schema
source database/schema.sql;
-- OR
mysql -u root -p < database/schema.sql
```

### 2. Backend

```bash
cd backend

# Edit DB credentials if needed:
# src/main/resources/application.properties
#   spring.datasource.username=root
#   spring.datasource.password=root

mvn clean install -DskipTests
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

### 3. Frontend

Open `frontend/index.html` in any browser **directly** (file://) or serve it:

```bash
# Simple Python server from the frontend folder:
cd frontend
python3 -m http.server 3000
# Open http://localhost:3000
```

---

## 🔐 Default Login Credentials

| Role         | Username       | Password    |
|--------------|----------------|-------------|
| Admin        | admin          | admin123    |
| Receptionist | receptionist   | recept123   |

These are auto-created on first startup by `DataInitializer.java`.

---

## 👥 Roles & Permissions

| Feature         | ADMIN | DOCTOR | RECEPTIONIST | PATIENT |
|-----------------|-------|--------|--------------|---------|
| Dashboard       | ✅    | ✅     | ✅           | ❌      |
| Manage Patients | ✅    | View   | ✅           | Own     |
| Manage Doctors  | ✅    | Own    | View         | ❌      |
| Appointments    | ✅    | Own    | ✅           | Own     |
| Prescriptions   | ✅    | ✅     | View         | View    |
| Billing         | ✅    | ❌     | ✅           | View    |

---

## 📡 API Endpoints

### Auth
```
POST /api/auth/login     { username, password }
POST /api/auth/register  { username, password, email, fullName, role }
```

### Patients
```
GET    /api/patients
GET    /api/patients/{id}
POST   /api/patients
PUT    /api/patients/{id}
DELETE /api/patients/{id}
GET    /api/patients/search?name=...
```

### Doctors
```
GET    /api/doctors
GET    /api/doctors/{id}
POST   /api/doctors
PUT    /api/doctors/{id}
DELETE /api/doctors/{id}
GET    /api/doctors/search?name=...
GET    /api/doctors/specialization/{spec}
```

### Appointments
```
GET    /api/appointments
GET    /api/appointments/{id}
POST   /api/appointments
PUT    /api/appointments/{id}
PATCH  /api/appointments/{id}/status?status=COMPLETED
DELETE /api/appointments/{id}
GET    /api/appointments/patient/{patientId}
GET    /api/appointments/doctor/{doctorId}
GET    /api/appointments/date?date=2024-01-15
```

### Prescriptions
```
GET    /api/prescriptions
GET    /api/prescriptions/{id}
GET    /api/prescriptions/appointment/{appointmentId}
POST   /api/prescriptions
PUT    /api/prescriptions/{id}
```

### Bills
```
GET    /api/bills
GET    /api/bills/{id}
GET    /api/bills/appointment/{appointmentId}
POST   /api/bills
PUT    /api/bills/{id}
PATCH  /api/bills/{id}/pay?paymentMethod=CASH
```

### Dashboard
```
GET    /api/dashboard/stats
```

---

## 🛠 Tech Stack

**Backend**
- Java 17
- Spring Boot 3.2
- Spring Security + JWT (JJWT 0.11.5)
- Spring Data JPA + Hibernate
- MySQL 8
- Lombok
- Maven

**Frontend**
- HTML5, CSS3, Vanilla JavaScript
- Fetch API for REST calls
- No frameworks, no build tools needed

---

## 🐳 Docker Notes

- The `docker-compose.yml` spins up MySQL, the Spring Boot backend, and an nginx frontend server.
- On first start, `database/schema.sql` is loaded automatically into MySQL.
- `DataInitializer.java` seeds the default admin and receptionist users.

---

## 🔧 Troubleshooting

**Port already in use**
```bash
# Change backend port in application.properties:
server.port=8081
# Change frontend port in docker-compose.yml: "3001:80"
```

**DB connection refused**
- Ensure MySQL is running: `sudo service mysql start`
- Verify credentials in `application.properties`

**CORS errors in browser**
- Make sure backend is running on port 8080
- The `SecurityConfig` allows all origins by default

**schema validate fails**
- Run `schema.sql` first, then start the backend
- Or change `spring.jpa.hibernate.ddl-auto=update` temporarily
