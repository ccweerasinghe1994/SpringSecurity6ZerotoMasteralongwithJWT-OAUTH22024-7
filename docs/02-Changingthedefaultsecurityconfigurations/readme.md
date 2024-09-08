# 

- [](#)
  - [001 Understanding about UI part of the EazyBank application](#001-understanding-about-ui-part-of-the-eazybank-application)
  - [002 Creating backend services needed for the EazyBank application](#002-creating-backend-services-needed-for-the-eazybank-application)
  - [003 Checking the default configuration inside the spring security framework](#003-checking-the-default-configuration-inside-the-spring-security-framework)
    - [Overview](#overview)
    - [**Annotations**](#annotations)
    - [**SecurityFilterChain Bean**](#securityfilterchain-bean)
      - [Why is `SecurityFilterChain` used?](#why-is-securityfilterchain-used)
    - [**Authorizing HTTP Requests**](#authorizing-http-requests)
      - [**Example Scenario**:](#example-scenario)
    - [**Enabling Form-Based Login**](#enabling-form-based-login)
      - [**Example Scenario**:](#example-scenario-1)
      - [Example:](#example)
    - [**Enabling Basic HTTP Authentication**](#enabling-basic-http-authentication)
      - [**Example Scenario**:](#example-scenario-2)
      - [Example of Basic Auth in Request:](#example-of-basic-auth-in-request)
    - [**Returning the Security Filter Chain**](#returning-the-security-filter-chain)
    - [**Putting it All Together**](#putting-it-all-together)
      - [**Flow of the Security Process**:](#flow-of-the-security-process)
    - [**Example Use Cases**](#example-use-cases)
    - [**Summary**](#summary)
  - [004 Modifying the security config code as per our custom requirements](#004-modifying-the-security-config-code-as-per-our-custom-requirements)
  - [005 How to disable formLogin and httpBasic authentication](#005-how-to-disable-formlogin-and-httpbasic-authentication)
  - [006 httpBasic authentication testing using postman](#006-httpbasic-authentication-testing-using-postman)


## 001 Understanding about UI part of the EazyBank application

![alt text](image.png)

## 002 Creating backend services needed for the EazyBank application

![alt text](image-1.png)
![alt text](image-2.png)

```java
package com.wchamara.springsecurity.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/myAccount")
    public String accountDetails() {
        return "Here are your account details";
    }
}

```

other controller follows the same pattern



## 003 Checking the default configuration inside the spring security framework

```java
package com.wchamara.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }


}

```
This `ProjectSecurityConfig` class is a Spring Security configuration that defines the security rules for a Spring Boot web application. It uses **Java Configuration** to specify how HTTP requests are secured, and it integrates the form-based login and basic authentication mechanisms. Let's go through the class and its configuration step by step to explain each part deeply, along with practical examples.

---

### Overview

- **Spring Security**: A framework that provides authentication, authorization, and protection against common attacks like CSRF, XSS, and session fixation. This configuration sets up Spring Security's default behavior using a security filter chain.
- **`HttpSecurity`**: A class that allows you to configure web-based security for specific HTTP requests.

This class configures a security filter chain that:
1. Requires authentication for all HTTP requests.
2. Enables form-based login.
3. Enables basic HTTP authentication.

---

### **Annotations**

```java
@Configuration
@EnableWebSecurity
```

- **`@Configuration`**: This annotation indicates that this class provides Spring configuration. It declares that the class is a source of bean definitions that Spring will process.
  
- **`@EnableWebSecurity`**: This annotation enables Spring Security’s web security support and integrates the configuration class into the Spring Security filter chain.

### **SecurityFilterChain Bean**

```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
```

- **`SecurityFilterChain`**: This is the core component of Spring Security that defines which requests should be intercepted and what security rules apply. 
- This method returns a **SecurityFilterChain** bean, which customizes how Spring Security filters and processes HTTP requests in the application.
  
#### Why is `SecurityFilterChain` used?

- **SecurityFilterChain** determines the security behavior of the web application. It handles the security filters that are applied to incoming requests and helps configure the authentication and authorization strategies.

---

### **Authorizing HTTP Requests**

```java
http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
```

- **`authorizeHttpRequests()`**: This method allows you to define rules for how different HTTP requests are handled.
  
  - **`anyRequest().authenticated()`**: This rule specifies that **every HTTP request** requires authentication. No public access is allowed to any URL unless the user is authenticated.
  
    This means that any request to a URL (e.g., `/home`, `/admin`, `/api`, etc.) will trigger Spring Security’s authentication mechanism. If the user is not logged in, they will be redirected to the login page.

#### **Example Scenario**:

If you attempt to visit a URL such as `http://example.com/dashboard`, Spring Security will intercept the request and check if the user is authenticated. Since **`anyRequest().authenticated()`** is applied, the request will not proceed unless the user logs in.

```http
GET /dashboard
Response: 302 Redirect to /login
```

---

### **Enabling Form-Based Login**

```java
http.formLogin(withDefaults());
```

- **`formLogin()`**: This method enables form-based login. This means that users will be presented with a login page (generated by Spring Security) where they must enter their credentials (username and password).
  
  - **`withDefaults()`**: This configures the form-based login with default settings, such as a default login page and default endpoints for login (`/login`) and logout (`/logout`).

#### **Example Scenario**:

When the user tries to access a protected resource, such as `/dashboard`, and they are not authenticated, they will be redirected to a default login page provided by Spring Security:

- **Login Page URL**: `http://example.com/login`
  
The user enters their credentials, and if they are correct, the user is authenticated and redirected to the originally requested page (`/dashboard` in this case).

#### Example:

```html
<form action="/login" method="POST">
    <input type="text" name="username" placeholder="Username"/>
    <input type="password" name="password" placeholder="Password"/>
    <button type="submit">Login</button>
</form>
```

---

### **Enabling Basic HTTP Authentication**

```java
http.httpBasic(withDefaults());
```

- **`httpBasic()`**: This method enables **HTTP Basic Authentication**, where the client (such as a web browser or API client) provides credentials in the HTTP request header (using the `Authorization` header) rather than through a form. The credentials are typically encoded using Base64.

  - **`withDefaults()`**: Again, this configures basic authentication with default settings.

#### **Example Scenario**:

This mechanism is typically used in REST APIs where clients can send a request with an `Authorization` header containing their credentials. The format for the `Authorization` header is:

```http
Authorization: Basic base64(username:password)
```

For example, if a user’s **username** is `user` and **password** is `password`, the header might look like this:

```http
Authorization: Basic dXNlcjpwYXNzd29yZA==
```

- When this header is included in an HTTP request, Spring Security will automatically extract the credentials, decode them, and authenticate the user.

#### Example of Basic Auth in Request:

```http
GET /api/data
Authorization: Basic dXNlcjpwYXNzd29yZA==
Response: 200 OK
```

---

### **Returning the Security Filter Chain**

```java
return http.build();
```

This line builds and returns the configured `SecurityFilterChain` bean to be used in the Spring Security context.

---

### **Putting it All Together**

The **ProjectSecurityConfig** class sets up a basic security structure that requires users to authenticate themselves for any request they make to the application. The configuration uses:
- **Form-based login** for web-based login pages.
- **HTTP Basic Authentication** for API or programmatic access (such as through REST clients).

#### **Flow of the Security Process**:
1. A user tries to access a protected resource (like `/dashboard`).
2. Since **`anyRequest().authenticated()`** is configured, Spring Security checks if the user is authenticated.
   - If not, the user is redirected to the login page (if it’s a browser request) or an HTTP 401 Unauthorized error is returned (for API requests).
3. The user provides credentials:
   - Through the form on the login page for a web browser.
   - Through the `Authorization` header for API access.
4. If the credentials are valid, the user is authenticated, and Spring Security allows access to the protected resource.

---

### **Example Use Cases**

1. **Web Application**:
   - If you are building a Spring Boot web application that has both a UI and an API, this setup can provide both a form login for the UI and HTTP Basic Authentication for API clients.
   - For example, users can log in via the login form to access the dashboard, and API clients can use Basic Auth to interact with API endpoints.

2. **REST API**:
   - If you are exposing a REST API and want to protect it with Basic Authentication, this configuration will allow clients to send requests with an `Authorization` header containing their Base64-encoded credentials.

---

### **Summary**

The `ProjectSecurityConfig` class is a basic but powerful security setup:
- **All requests require authentication** (`anyRequest().authenticated()`).
- **Form login** is enabled to handle user logins through a web form.
- **HTTP Basic Authentication** is enabled for API access or programmatic authentication.

This provides a simple, flexible way to secure your application with both UI and API endpoints, using Spring Security’s default mechanisms.

## 004 Modifying the security config code as per our custom requirements


## 005 How to disable formLogin and httpBasic authentication


## 006 httpBasic authentication testing using postman
