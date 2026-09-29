# RideLink

Backend microservices-based ride-sharing platform developed using
Java and Spring Boot.

Microservices:

- Account Service
- Driver & Vehicle Service
- Ride Management Service
- Fare & Payment Service

Technology:

- Java
- Spring Boot
- Maven
- MongoDB for individual microservices where appropriate

Architecture:

Each microservice will maintain its own independent database/data store.
No single shared database will be used across the entire system.

The project is backend-only and will be demonstrated using
Swagger UI and Postman.

## Ride Management Service

**Purpose**: Handles the core lifecycle of a ride request, driver assignment, and ride state transitions.
- **Port**: `8083`
- **Database**: `ridelink_ride_db` (MongoDB)
- **Dependencies**: Depends on Driver & Vehicle Service (`http://localhost:8082`) for retrieving available drivers.

### API Endpoints
- `POST /api/rides`: Create a new ride request
- `GET /api/rides/{id}`: Retrieve ride details
- `GET /api/rides/available-drivers`: Find available drivers (via external service)
- `PATCH /api/rides/{id}/assign-driver`: Manually assign a driver
- `PATCH /api/rides/{id}/auto-assign-driver`: Automatically assign a driver
- `PATCH /api/rides/{id}/accept`: Accept the assigned ride
- `PATCH /api/rides/{id}/start`: Start the ride
- `PATCH /api/rides/{id}/complete`: Complete the ride
- `PATCH /api/rides/{id}/cancel`: Cancel the ride

### Ride Lifecycle
`REQUESTED` → `ASSIGNED` → `ACCEPTED` → `IN_PROGRESS` → `COMPLETED`
(Rides can be `CANCELLED` prior to `IN_PROGRESS`.)

### Security
Secured using JWT Bearer tokens issued by the Account Service. Ensure the `jwt.secret` (defaulting via `${JWT_SECRET}` in the environment) matches between services.

### Testing and Demonstration
- **Swagger UI**: Accessible at `http://localhost:8083/swagger-ui/index.html`
- **Postman**: The Postman collection and environment variables are located in `ride-management-service/postman/`.
- **Unit Tests**: Run tests from the `ride-management-service` directory using `mvn clean test`.
