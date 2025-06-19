# ClockApi_SpringRest-SpringSecurity

## 🚀 3-in-1 Project: REST API, REST Client & Authorization Server

### 🔗 Useful Endpoints
- 🔍 **Swagger Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
- 🔄 **REST Client Interface**: [http://localhost:8080/webapp](http://localhost:8080/webapp)  

---

### 🧩 What's Inside

This project combines three key modules into one:

- ✅ **REST API** – Build and expose endpoints  
- ✅ **REST Client** – Consume APIs using WebClient  
- ✅ **Authorization Server** – Secure APIs with OAuth 2.0 & JWT  

---

> ⚠️ **Important:**  
> Before running the application, make sure to configure your **Google OAuth 2.0 Client ID and Client Secret**  
> in your `application.properties` file:
>
> ```properties
> spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
> spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
> ```

---

### 📚 Topics Covered

#### 🌐 Spring Web
- `@RestController`, `@ResponseBody`, `@RequestBody`
- `ResponseEntity` return types
- Global exception handling with `@RestControllerAdvice`
- **Swagger/OpenAPI** documentation
- **CORS** configuration

#### 🔄 Spring Reactive Web
- **WebClient** (for asynchronous and synchronous HTTP requests)

#### 🔐 Spring Security
- `SecurityFilterChain` configuration
- **Basic Authentication**
- **OAuth 2.0 Authentication**
  - Google OAuth 2.0 Login
  - Custom OAuth 2.0 Authorization Server
- **JWT-based Authentication & Authorization**
- **Session Management**
- **Authorization Server + Resource Server** setup

---
