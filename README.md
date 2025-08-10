## News (Spring Boot)

Minimal Spring Boot application scaffolded with Maven. Uses Java 17 and the Spring Boot Maven plugin.

### Requirements
- **Java**: 17+
- **Maven Wrapper**: included (`mvnw` / `mvnw.cmd`)

### Quick start
```bash
# From the project root
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080` by default.

### Build
```bash
./mvnw clean package
```
This produces a runnable jar under `target/` (e.g., `target/news-0.0.1-SNAPSHOT.jar`).

### Run the packaged jar
```bash
java -jar target/news-0.0.1-SNAPSHOT.jar
```

### Test
```bash
./mvnw test
```

### Dev experience
- **Spring Boot DevTools** is included for live reload during development.

### Configuration
- App properties live in `src/main/resources/application.properties`.

### Project layout
```
news/
  ├─ src/
  │  ├─ main/
  │  │  ├─ java/com/laranews/news/NewsApplication.java
  │  │  └─ resources/
  │  │     ├─ application.properties
  │  │     ├─ static/
  │  │     └─ templates/
  │  └─ test/
  └─ pom.xml
```

### Notes
- Java version is configured via Maven property: `java.version=17`.
- Managed by Spring Boot parent: `org.springframework.boot:spring-boot-starter-parent:3.5.4`.

