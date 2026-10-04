# Fare Payment Service


## Technology Stack
- Java 17+
- Spring Boot
- Maven
- MongoDB
- Swagger / OpenAPI
- Postman

**IMPORTANT COMPLIANCE STATEMENT:**
This service does not use MERN, Node.js, or Express.js. It is a purely Java/Spring Boot microservice as required by the assignment guidelines.

## Purpose of the Service
The Fare Payment Service manages the lifecycle of ride payments, handles server-side fare calculations, enforces payment rules (e.g., duplicate payment prevention per ride), and processes refunds safely.

## Main Features
- Server-side fare calculation based on distance and duration.
- Duplicate-payment protection at both application and database levels.
- Comprehensive payment lifecycle management (creation, status updates, refunds).
- Automated audit timestamps (`createdAt`, `updatedAt`, `refundedAt`).

### Supported Payment Methods
- `CASH`
- `CARD`
- `WALLET`

### Supported Payment Statuses
- `PENDING`
- `COMPLETED`
- `FAILED`
- `REFUNDED`

## Configuration
- **MongoDB Database:** `fare_payment_db`
- **Server Port:** `8083`
- **Swagger UI URL:** `http://localhost:8083/swagger-ui/index.html`

---

## Group Service Port Plan
- **Fare Payment Service:** `8083`
*(Do not invent ports for other services if they are not already documented)*

---

## How to Run the Project

### Prerequisites
1. **Java 17** or above installed.
2. **Maven** installed.
3. **MongoDB** running locally on default port `27017` (URI: `mongodb://localhost:27017/fare_payment_db`).

### Running the Application
To build and run the application from the root directory:

```bash
# Clean and package the application
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

The application will start on `http://localhost:8083`.

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/payments` | Create payment |
| `GET` | `/api/payments/{paymentId}` | Get payment by ID |
| `GET` | `/api/payments/ride/{rideId}` | Get payment by ride ID |
| `GET` | `/api/payments/passenger/{passengerId}` | Get passenger payment history |
| `GET` | `/api/payments/driver/{driverId}` | Get driver payment records |
| `GET` | `/api/payments/status/{status}` | Get payments by status |
| `PATCH` | `/api/payments/{paymentId}/status` | Update payment status |
| `POST` | `/api/payments/{paymentId}/refund` | Refund payment |
| `POST` | `/api/payments/calculate-fare` | Preview fare calculation |

---

## Sample Request JSONs

### Create Payment (`POST /api/payments`)
```json
{
  "rideId": "RIDE-001",
  "passengerId": "PASSENGER-001",
  "driverId": "DRIVER-001",
  "distanceKm": 10.0,
  "durationMinutes": 20.0,
  "paymentMethod": "CARD"
}
```

### Update Payment Status (`PATCH /api/payments/{paymentId}/status`)
```json
{
  "paymentStatus": "COMPLETED"
}
```

### Refund Payment (`POST /api/payments/{paymentId}/refund`)
```json
{
  "refundAmount": 500.00
}
```

---

## Fare Calculation Formula

Fares are calculated server-side based on the following formula:

- **Base fare:** `200.00`
- **Distance fare:** `distanceKm × 80.00`
- **Time fare:** `durationMinutes × 5.00`
- **Total fare:** `baseFare + distanceFare + timeFare`

> Note: All monetary calculations use Java's `BigDecimal` to ensure absolute precision, rounded to 2 decimal places. Floating-point primitives (`double`, `float`) are not used.

---

## Payment Flow

1. **Create payment:** The client provides ride IDs and distance/duration. The service calculates the fare and creates a record.
2. **Payment starts as `PENDING`:** The initial status upon creation.
3. **Update payment to `COMPLETED`:** Usually triggered after a successful gateway callback or cash confirmation.
4. **Refund Processing:** A refund may only be processed on a `COMPLETED` payment. It cannot exceed the `totalAmount`.
5. **Refunded payment becomes `REFUNDED`:** Status updates to reflect the refund, and timestamps/amounts are logged.

*Note: Only one payment record is allowed per `rideId`. Any attempt to create another payment for an existing ride will result in an HTTP 409 Conflict.*

---

## Microservice Integration Contract

Document what other services need to send when creating a payment.

- **rideId:**
Unique ID provided by Ride Management Service.

- **passengerId:**
Passenger/account identifier.

- **driverId:**
Driver identifier.

- **distanceKm:**
Completed ride distance used for fare calculation.

- **durationMinutes:**
Ride duration used for fare calculation.

- **paymentMethod:**
CASH, CARD, or WALLET.

---

## Payment Creation Flow

1. Ride Management Service completes a ride.
2. Relevant ride information is supplied to Fare Payment Service.
3. Fare Payment Service calculates the fare.
4. Fare Payment Service creates a payment in PENDING status.
5. Payment is updated to COMPLETED or FAILED.
6. A completed payment may later be refunded.

---

## Microservice Database Isolation

The Fare Payment Service owns and exclusively manages:
`fare_payment_db`

It does **not** directly query databases belonging to other services (such as Ride, Driver, or Passenger databases).

External entities are referenced only by their respective unique IDs:
- `rideId`
- `passengerId`
- `driverId`

This strict database-per-service pattern is required by the assignment's microservice architecture to ensure high cohesion and loose coupling.
