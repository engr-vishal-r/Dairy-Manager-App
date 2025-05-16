# 🐄 Dairy Manager App

The Dairy Manager App is a backend application built with Java and Spring Boot, designed to streamline the daily operations of a dairy farm. It provides RESTful APIs to manage customers, entries, and automated billing processes.

---

## 🚀 Features

- **Customer Management**: Create, read, update, and delete customer information.
- **Entry Tracking**: Record daily deliveries with details such as quantity and delivery time.
- **Automated Billing**: Generate daily and monthly bills based on entries.
- **Layered Architecture**: Follows a clean separation of concerns with Controller, Service, and Repository layers.
- **Testing**: Comprehensive unit and service layer tests using JUnit 5 and Mockito.
- **Database Integration**: Utilizes MySQL for development and H2 for testing environments.

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, MockMvc
- **Database**: MySQL (development), H2 (testing)

---

## 📁 Project Structure
Dairy-Manager-App/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── dairy/
│ │ │ ├── controller/
│ │ │ ├── service/
│ │ │ ├── repository/
│ │ │ └── model/
│ │ └── resources/
│ │ ├── application.properties
│ │ └── ...
│ └── test/
│ └── java/
│ └── com/
│ └── dairy/
│ └── ...
├── pom.xml
└── README.md


---

## ⚙️ Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL

## 📚 API Documentation

The project uses Swagger (OpenAPI) for documenting REST APIs.

- Swagger UI: [http://localhost:8085/swagger-ui.html](http://localhost:8080/swagger-ui.html)


| Method | Endpoint             | Description                     |
| ------ | -------------------- | ------------------------------- |
| GET    | /api/customers       | Retrieve all customers          |
| POST   | /api/customers       | Add a new customer              |
| PUT    | /api/customers/{id}  | Update customer information     |
| DELETE | /api/customers/{id}  | Delete a customer               |
| GET    | /api/milk-entries    | Retrieve all milk entries       |
| POST   | /api/milk-entries    | Add a new milk entry            |
| GET    | /api/billing/daily   | Generate daily billing report   |
| GET    | /api/billing/monthly | Generate monthly billing report |
