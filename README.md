# Order Book Service

The Order Book Service is a RESTful API that allows users to create and manage orders. It provides endpoints for
creating orders, retrieving orders by ID, and getting summaries of orders based on ticker, order side, and date.

## Technologies Used

- Java 17
- Docker Compose
- Spring Boot
- Spring Data JPA: Simplifies the interaction with the database and provides repository support.
- Spring Web: Enables building RESTful APIs using annotations and HTTP request mapping.
- H2 Database: A lightweight, in-memory database used here soley for testing purposes.
- Lombok: Reduces boilerplate code with automatic generation of getters, constructors, builders, etc.
- Swagger: Generates API documentation and provides an interactive UI for testing the endpoints.

## Assumptions

- The application assumes a single user interacting with the API. Authentication and authorization mechanisms are not
  implemented in this version.

## Running the Application with Docker Compose

To run the Order Book Service using Docker Compose, follow these steps:

1. Make sure Docker and Docker Compose are installed on your system.
2. Clone the repository: `git clone https://github.com/annsofip/order-book-service.git`
3. Navigate to the project directory: `cd order-book-service`
4. Start the containers using Docker Compose: `docker-compose up`
5. The application will be available at `http://localhost:8080`.

## API Documentation

The API documentation is automatically generated using Swagger/OpenAPI annotations. It provides detailed information
about the available endpoints, request/response dtos.

To access the Swagger, follow these steps:

1. Start the application using Docker Compose.
2. Open a web browser and navigate to `http://localhost:8080/swagger-ui.html`.
3. The Swagger UI will display the available endpoints and their documentation.
4. Use the provided forms and documentation to interact with the API and test different requests.

