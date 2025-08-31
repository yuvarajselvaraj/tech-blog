## Tech Blog (Spring Boot)

Simple Spring Boot app that fetches and displays curated Laravel News posts. It uses Thymeleaf for server-side rendering, Spring Security for auth, and a scheduler to pull RSS items into a repository.

### Requirements
- **Java**: 17+
- **Maven Wrapper**: included (`mvnw` / `mvnw.cmd`)

### Quick start
```bash
# From the project root
./mvnw spring-boot:run
# App will start on http://localhost:8080
```

### Build
```bash
./mvnw clean package
```
Produces a runnable jar under `target/`.

### Run the packaged jar
```bash
java -jar target/*.jar
```

### Authentication
- Custom login page at `/login`
- Landing page at `/` (unauthenticated users see a landing, authenticated users are redirected to `/blogs`)
- Logout via POST `/logout` (button available on `/blogs`) redirects to `/`
- In-memory users:
  - `user` / `password` (ROLE_USER)
  - `admin` / `admin123` (ROLE_ADMIN)

### Endpoints
- `GET /` – Landing page
- `GET /login` – Sign-in page
- `POST /login` – Form login submit
- `POST /logout` – Logout
- `GET /blogs` – Authenticated HTML view of posts (paginated)
- `GET /blogs?size=50` – JSON API of latest posts (via `RestController`)

### UI & Layout
- Thymeleaf base layout at `templates/fragments/layout.html` with a `base(pageTitle, extraHead, content)` fragment
- Pages extend the layout using:
  - `th:replace="~{fragments/layout :: base('Title', ~{::extraHead}, ~{::content})}"`
- Templates:
  - `templates/index.html` – landing
  - `templates/login.html` – login
  - `templates/blogs.html` – posts list with logout button

### Configuration
- Main application properties: `src/main/resources/application.properties`
- Security configuration: `src/main/java/com/techblog/config/SecurityConfig.java`

### Project structure (simplified)
```
src/
  main/
    java/
      com/techblog/
        BlogApplication.java
        config/SecurityConfig.java
        auth/AuthController.java
        blog/
          controller/WebController.java   # HTML view
          controller/BlogController.java  # JSON API
          service/LaravelNewsFeedService.java
          repository/BlogPostRepository.java
          model/BlogPost.java
          scheduler/FeedScheduler.java
    resources/
      templates/
        fragments/layout.html
        index.html
        login.html
        blogs.html
```

### Notes
- Uses Thymeleaf with a shared layout fragment to reduce repetition
- Logout is CSRF-protected; all forms include the CSRF token
