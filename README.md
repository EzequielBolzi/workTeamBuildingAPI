# workTeamBuildingAPI

##TeamBuildingAPI

This project is an API developed using Java 8+, Spring Boot, Hibernate/JPA, and Lombok, designed to manage movie and series proposals for team-building events within an organization. The API allows for registering, updating, deleting, and querying information about movies, series, employees, and their votes.

## Main Features

- **Movie and Series Registration**: Allows registration of details such as year, director, genre, seasons, duration, proposed employee, average rating, implementing employee, and registration date and time.
- **Employee Management**: Storage of basic employee information such as name, email, role (frontend/backend), age.
- **Voting System**: Recording of scores given by employees to the proposed movies and series.
- **Query Endpoints**: Ability to list all employees, movies, series, and perform filtered queries by genre, score, year.
- **Best Proposal**: Identification of the employee and movie/series with the highest score.

## Technologies Used

- **Java 8+**
- **Spring Framework (Spring Boot, Spring Security)**
- **Hibernate/JPA** for managing the PostgreSQL database.
- **Lombok** to reduce code verbosity.
- **JUnit** for unit testing.
- **Swagger** for API documentation.
- **GitFlow** for development workflow.
