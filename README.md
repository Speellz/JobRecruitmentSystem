# JobRecruitmentSystem

This project is a Spring Boot based web application that provides a platform for job postings, applications and company management. It uses JSP views and SQL Server in production with H2 for tests.

## Requirements

- Java 21
- Maven (the project includes the Maven wrapper)
- SQL Server database (or update `application.properties` accordingly)

## Building

Use the Maven wrapper to compile the project:

```bash
./mvnw clean package
```

## Running

Run the application with Spring Boot:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

## Testing

Unit and integration tests are executed with:

```bash
./mvnw test
```

A H2 in-memory database is used when the `test` profile is active.

## Features

- User registration and authentication with roles: applicant, recruiter, company and admin.
- CRUD operations for job postings.
- Applicants can apply for jobs and upload CVs.
- Messaging between users and notifications.
- Recruiter analytics and feedback forms.

