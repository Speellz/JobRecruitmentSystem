# JobRecruitmentPrivate

This project uses Spring Boot and requires database credentials to run. The datasource URL, username and password are read from environment variables. Before starting the application ensure the following variables are set:

```
export SPRING_DATASOURCE_URL=<jdbc url>
export SPRING_DATASOURCE_USERNAME=<database user>
export SPRING_DATASOURCE_PASSWORD=<database password>
```

Replace the placeholders with the values for your SQL Server instance. These variables can be placed in your shell profile or provided when running the application.

