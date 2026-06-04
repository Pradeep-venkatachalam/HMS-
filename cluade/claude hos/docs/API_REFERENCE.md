# HMS API Reference

Base URL: `http://localhost:8080/api`

All protected endpoints require the header:
```
Authorization: Bearer <JWT_TOKEN>
```

## Authentication

### POST /auth/login
```json
Request:  { "username": "admin", "password": "admin123" }
Response: { "success": true, "data": { "token": "...", "role": "ADMIN", "fullName": "...", "userId": 1 } }
```

### POST /auth/register
```json
Request:  { "username": "john", "password": "pass123", "email": "john@example.com", "fullName": "John Doe", "role": "PATIENT" }
```

## Appointment Status Values
`SCHEDULED` | `CONFIRMED` | `COMPLETED` | `CANCELLED` | `NO_SHOW`

## Payment Status Values
`PENDING` | `PAID`

## Payment Methods
`CASH` | `CARD` | `UPI` | `INSURANCE` | `BANK_TRANSFER`
