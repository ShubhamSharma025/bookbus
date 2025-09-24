# BookBus 🚌

A Spring Boot backend application for bus booking system that provides RESTful APIs for managing bus reservations, routes, and passenger information.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## 🎯 Overview

BookBus is a comprehensive backend solution for bus booking operations. It handles the core functionality needed for a bus reservation system including route management, seat allocation, booking operations, and passenger management. The application is built using Spring Boot framework and follows REST API standards.

## ✨ Features

- **Route Management**: Create, update, and manage bus routes
- **Bus Management**: Add and manage bus information with seating capacity
- **Booking System**: Handle seat reservations and booking confirmations
- **Passenger Management**: Store and manage passenger details
- **Seat Allocation**: Real-time seat availability and booking
- **Payment Integration**: Handle booking payments and confirmations
- **Search & Filter**: Search buses by route, date, and availability
- **Authentication & Authorization**: Secure user login and role-based access control
- **JWT Token Management**: Stateless authentication using JSON Web Tokens
- **RESTful APIs**: Clean and well-documented API endpoints
- **Data Validation**: Input validation and error handling
- **Database Integration**: Persistent data storage

## 🛠 Technologies Used

- **Framework**: Spring Boot 2.x/3.x
- **Language**: Java 8+
- **Database**: MySQL/PostgreSQL (configurable)
- **ORM**: Spring Data JPA/Hibernate
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **API Documentation**: Swagger/OpenAPI (optional)
- **Testing**: JUnit, Mockito
- **Validation**: Spring Boot Validation

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- Java 8 or higher
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 12+ (or any preferred database)
- Git
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/ShubhamSharma025/bookbus.git
cd bookbus
```

### 2. Database Setup

Create a database for the application:

**For MySQL:**
```sql
CREATE DATABASE bookbus_db;
```

**For PostgreSQL:**
```sql
CREATE DATABASE bookbus_db;
```

### 3. Configure Database

Update the `application.properties` or `application.yml` file:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bookbus_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 4. Build and Run

```bash
# Build the application
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## ⚙️ Configuration

### Application Properties

Key configuration properties you may need to modify:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bookbus_db
spring.datasource.username=root
spring.datasource.password=password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.com.bookbus=DEBUG
logging.level.org.springframework.web=INFO

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Security Configuration
spring.security.jwt.enabled=true
```

## 🔗 API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | User login | Public |
| POST | `/api/auth/logout` | User logout | Authenticated |
| POST | `/api/auth/refresh` | Refresh JWT token | Authenticated |
| GET | `/api/auth/profile` | Get user profile | Authenticated |

### User Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/users` | Get all users | Admin |
| GET | `/api/users/{id}` | Get user by ID | Admin/Owner |
| PUT | `/api/users/{id}` | Update user | Admin/Owner |
| DELETE | `/api/users/{id}` | Delete user | Admin |

### Bus Routes

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/routes` | Get all available routes | Public |
| GET | `/api/routes/{id}` | Get route by ID | Public |
| POST | `/api/routes` | Create a new route | Admin/Operator |
| PUT | `/api/routes/{id}` | Update existing route | Admin/Operator |
| DELETE | `/api/routes/{id}` | Delete a route | Admin |

### Bus Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/buses` | Get all buses | Public |
| GET | `/api/buses/{id}` | Get bus by ID | Public |
| POST | `/api/buses` | Add a new bus | Admin/Operator |
| PUT | `/api/buses/{id}` | Update bus information | Admin/Operator |
| DELETE | `/api/buses/{id}` | Remove a bus | Admin |

### Booking Operations

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/bookings` | Get user's bookings | Authenticated |
| GET | `/api/bookings/all` | Get all bookings | Admin/Operator |
| GET | `/api/bookings/{id}` | Get booking by ID | Owner/Admin |
| POST | `/api/bookings` | Create a new booking | Authenticated |
| PUT | `/api/bookings/{id}` | Update booking | Owner/Admin |
| DELETE | `/api/bookings/{id}` | Cancel booking | Owner/Admin |

### Search & Availability

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/search` | Search buses by criteria | Public |
| GET | `/api/availability/{busId}` | Check seat availability | Public |
| GET | `/api/routes/{from}/{to}` | Get routes between cities | Public |

## 🗃️ Database Schema

### Key Entities

#### User
```sql
- id (Primary Key)
- username
- email
- password (encrypted)
- first_name
- last_name
- phone
- role (USER, ADMIN, OPERATOR)
- created_at
- updated_at
- is_active
```

#### Route
```sql
- id (Primary Key)
- from_city
- to_city
- distance
- duration
- created_at
- updated_at
```

#### Bus
```sql
- id (Primary Key)
- bus_number
- bus_type
- total_seats
- route_id (Foreign Key)
- departure_time
- arrival_time
- fare
```

#### Booking
```sql
- id (Primary Key)
- user_id (Foreign Key)
- passenger_name
- passenger_email
- passenger_phone
- bus_id (Foreign Key)
- seat_numbers
- booking_date
- journey_date
- total_amount
- booking_status
```

#### Passenger
```sql
- id (Primary Key)
- name
- email
- phone
- age
- gender
```

## 💻 Usage

### Example API Calls

#### User Registration
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "9876543210"
  }'
```

#### User Login
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "securePassword123"
  }'
```

#### Search for Buses
```bash
curl -X GET "http://localhost:8080/api/search?from=Delhi&to=Mumbai&date=2024-01-15"
```

#### Create a Booking (Authenticated)
```bash
curl -X POST "http://localhost:8080/api/bookings" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "passengerName": "John Doe",
    "passengerEmail": "john@example.com",
    "passengerPhone": "9876543210",
    "busId": 1,
    "seatNumbers": ["A1", "A2"],
    "journeyDate": "2024-01-15"
  }'
```

#### Check Seat Availability
```bash
curl -X GET "http://localhost:8080/api/availability/1?date=2024-01-15"
```

### Authentication Response Format

Login successful response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": 1,
      "username": "johndoe",
      "email": "john@example.com",
      "role": "USER"
    }
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error Response Format

All API error responses follow this format:

```json
{
  "success": false,
  "message": "Error description",
  "errors": [
    {
      "field": "fieldName",
      "message": "Field specific error"
    }
  ],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Standard Response Format

All successful API responses follow a consistent format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Response data
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## 🧪 Testing

Run the test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn -Dtest=BookingServiceTest test

# Generate test coverage report
mvn jacoco:report
```

## 📁 Project Structure

```
bookbus/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bookbus/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── config/
│   │   │       ├── security/
│   │   │       ├── exception/
│   │   │       └── BookBusApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
├── pom.xml
├── README.md
└── .gitignore
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding standards
- Write unit tests for new features
- Update documentation as needed
- Use meaningful commit messages
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

**Shubham Sharma**
- GitHub: [@ShubhamSharma025](https://github.com/ShubhamSharma025)
- Email: [Contact via GitHub](https://github.com/ShubhamSharma025)

## 🙏 Acknowledgments

- Spring Boot Documentation
- Java Community
- Open Source Contributors

---

⭐ If you found this project helpful, please give it a star!

## 🚀 Future Enhancements

- [x] ~~Add user authentication and authorization~~ ✅ **Completed**
- [ ] Implement payment gateway integration
- [ ] Add email/SMS notifications
- [ ] Create admin dashboard
- [ ] Add real-time seat booking updates
- [ ] Implement caching for better performance
- [ ] Add API rate limiting
- [ ] Create mobile app APIs
- [ ] Add bus tracking features
- [ ] Implement booking analytics
- [ ] Add two-factor authentication (2FA)
- [ ] Implement refresh token rotation
- [ ] Add password reset functionality
