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
    - [**Understanding the Components**](#understanding-the-components)
    - [**What Does It Do?**](#what-does-it-do)
      - [**Example Scenario**](#example-scenario-3)
    - [**When to Use `permitAll()`**](#when-to-use-permitall)
      - [**Common Use Cases for `permitAll()`**:](#common-use-cases-for-permitall)
    - [**Security Implications**](#security-implications)
      - [**Security Risks**:](#security-risks)
    - [**Conclusion**](#conclusion)
    - [**1. `http.authorizeHttpRequests()`**](#1-httpauthorizehttprequests)
    - [**2. `(requests) -> requests.anyRequest()`**](#2-requests---requestsanyrequest)
    - [**3. `.denyAll()`**](#3-denyall)
    - [**Practical Outcome:**](#practical-outcome)
    - [**Example Scenario:**](#example-scenario-4)
    - [**Use Case for `.denyAll()`**:](#use-case-for-denyall)
    - [**Form Login and HTTP Basic Authentication in this Context**](#form-login-and-http-basic-authentication-in-this-context)
    - [**Example Flow**:](#example-flow)
    - [**Summary**:](#summary-1)
    - [**Deep Dive Explanation of the Code**](#deep-dive-explanation-of-the-code)
    - [**1. Security Filter Chain Configuration**](#1-security-filter-chain-configuration)
    - [**2. Securing Specific Endpoints**](#2-securing-specific-endpoints)
      - [**Example Scenario**:](#example-scenario-5)
      - [**Flow**:](#flow)
    - [**3. Publicly Accessible Endpoints**](#3-publicly-accessible-endpoints)
      - [**Example Scenario**:](#example-scenario-6)
    - [**4. Form-Based Login**](#4-form-based-login)
      - [**Example Flow of Form Login**:](#example-flow-of-form-login)
    - [**5. HTTP Basic Authentication**](#5-http-basic-authentication)
      - [**Example of Basic Authentication**:](#example-of-basic-authentication)
    - [**Flow of the Security Process**](#flow-of-the-security-process-1)
    - [**Conclusion**](#conclusion-1)
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
        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }


}

```


The line `http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());` in the code configures Spring Security to allow unrestricted access to all HTTP requests made to the application. Let’s break this down and explain it step by step.

### **Understanding the Components**

1. **`http.authorizeHttpRequests()`**:
   - This method is used to define authorization rules for different HTTP requests. It allows you to specify which requests require authentication and which do not.
   - It's a part of Spring Security's **HttpSecurity** object that handles web-based security configuration.

2. **`(requests) -> requests.anyRequest().permitAll()`**:
   - This is a **lambda expression** that configures the behavior for how all incoming HTTP requests should be handled.
   - **`anyRequest()`** refers to **any HTTP request** made to the application (e.g., `GET /home`, `POST /login`, `PUT /api/data`).
   - **`permitAll()`** means that all requests are allowed **without authentication**. In other words, no security checks are applied, and any user (authenticated or not) can access any URL or resource in the application.

### **What Does It Do?**

- The configuration allows **every request** to pass through the security filter chain without requiring the user to authenticate. This effectively **disables authentication** for all endpoints, making the entire application publicly accessible.

#### **Example Scenario**

Let’s say your application has the following endpoints:
- `/home`: Homepage for the application.
- `/admin`: Admin dashboard.
- `/api/data`: API endpoint for retrieving data.

Normally, without specific configuration, Spring Security would require the user to be authenticated to access protected resources. However, with this configuration:

```java
http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
```

- **No request** will require authentication.
- Anyone can access any of the endpoints, regardless of whether they are logged in or not.
  
For example:
- **`/home`**: The homepage can be accessed by anyone.
- **`/admin`**: Normally, this page might be restricted to admins only, but with `permitAll()`, even unauthorized users can access it.
- **`/api/data`**: Even sensitive API data could be exposed because no authentication or authorization is enforced.

---

### **When to Use `permitAll()`**

While allowing unrestricted access to every part of your application may be useful during **development** or for **public-facing applications**, it should be used cautiously in production environments where sensitive data or administrative functions need to be protected.

#### **Common Use Cases for `permitAll()`**:
1. **Publicly Accessible Pages**:
   - For pages like the home page (`/home`), login page (`/login`), or a public-facing FAQ or contact page, you would typically use `permitAll()` to allow users to access these resources without needing to log in.

   Example:
   ```java
   http.authorizeHttpRequests((requests) -> 
       requests.antMatchers("/home", "/login", "/contact").permitAll()
   );
   ```

2. **Open API Endpoints**:
   - If you are building an API that doesn’t require authentication (e.g., public APIs), you might configure those endpoints to allow unauthenticated access.

   Example:
   ```java
   http.authorizeHttpRequests((requests) -> 
       requests.antMatchers("/api/public/**").permitAll()
   );
   ```

3. **Development and Testing**:
   - During development, you might want to disable authentication to test the application without being interrupted by login pages. In such cases, you might temporarily use `permitAll()`.

---

### **Security Implications**

Using `permitAll()` on every request (`anyRequest()`) makes the application **completely unprotected**. No user will be required to authenticate or authorize, and anyone can access any resource.

#### **Security Risks**:
1. **Unrestricted Access**: All endpoints, including sensitive ones, are publicly accessible. This could lead to unauthorized access to administrative features, sensitive data, or internal APIs.
2. **No Authentication**: With this setting, the application does not check whether a user is logged in, making it impossible to restrict access to specific users or roles.
3. **No Authorization Checks**: You won’t be able to enforce role-based access control (e.g., only allowing admins to access certain features).

---

### **Conclusion**

The line `http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());` is a configuration that tells Spring Security to **allow all incoming HTTP requests** without requiring any authentication. This makes the application fully accessible to anyone, which can be useful for certain scenarios but poses significant security risks in production environments. 

For a more secure application, you would want to replace `permitAll()` with specific access rules, such as requiring authentication for certain paths or protecting certain resources by roles.

![alt text](image.png)

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
        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }


}

```

In the line:

```java
http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
```

You are configuring **Spring Security** to deny all incoming HTTP requests, regardless of the URL or resource being accessed. Let's break it down step by step:

---

### **1. `http.authorizeHttpRequests()`**

This method is used to define rules for **authorizing HTTP requests**. It allows you to specify which requests should be allowed, and which should be denied based on the request’s URL, method, or user authentication.

---

### **2. `(requests) -> requests.anyRequest()`**

- **`anyRequest()`**: This means **"any HTTP request"** that is made to the server, without exception. It doesn't matter if the request is for a public page, a secure resource, or an API endpoint; it applies to all HTTP requests.

---

### **3. `.denyAll()`**

- **`denyAll()`**: This is the critical part of the line. It specifies that **all requests should be denied**, meaning no user will be able to access any part of the application—whether they are authenticated or not. It blocks all access to the application.

### **Practical Outcome:**

- If **`denyAll()`** is used, **every request** to your Spring Boot application will be blocked. Even if a user tries to visit the login page, access an API, or visit any other page, Spring Security will deny access.

### **Example Scenario:**

If a user tries to access any URL, such as `/login` or `/home`, they will see a **403 Forbidden** error, meaning they are not authorized to access any resources:

- **Request**: `GET /home`
  
- **Response**:
```http
HTTP/1.1 403 Forbidden
```

### **Use Case for `.denyAll()`**:

1. **Maintenance Mode**: 
   - You might use `.denyAll()` in special cases where you want to block all access to your site for a period (e.g., for maintenance purposes), where even the login or public pages should not be accessible.
   
2. **Application Lockdown**:
   - For temporary lockdown of the entire application during certain events (e.g., critical security incidents), denying access to all requests can be part of a security measure.

3. **Testing or Debugging**:
   - You could also use `.denyAll()` during testing if you want to ensure that certain parts of your security configuration are being applied correctly by denying all traffic and checking for any leaks.

---

### **Form Login and HTTP Basic Authentication in this Context**

Even though **form login** and **HTTP Basic Authentication** are enabled in your configuration (`http.formLogin()` and `http.httpBasic()`), they won’t be functional if `denyAll()` is used.

- The form login page (`/login`) won’t be accessible because **any request** (including `/login`) is denied.
- Basic authentication for API endpoints would also be blocked because **all requests** will receive a **403 Forbidden** response.

---

### **Example Flow**:

1. **User Requests Login Page**:
   - **Request**: `GET /login`
   - **Response**: `403 Forbidden`

2. **User Requests API Endpoint**:
   - **Request**: `GET /api/data` (with or without credentials)
   - **Response**: `403 Forbidden`

---

### **Summary**:

The line `http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());` is a strict rule that denies access to **all incoming requests**. Even though you have enabled form login and basic authentication, they will not be usable because all requests, including those to the login page and API, will be rejected.

![alt text](image-3.png)

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
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("myAccount", "myBalance", "myCards", "myLoans").authenticated()
                .requestMatchers("notices", "welcome", "contact", "error").permitAll()
        );
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }


}

```
This Spring Security configuration defines a custom security setup for a Spring Boot application, applying **authentication** and **authorization rules** for specific HTTP requests. It provides different levels of access to different endpoints based on whether they should be secured (authenticated) or publicly accessible (permitted).

### **Deep Dive Explanation of the Code**

---

### **1. Security Filter Chain Configuration**

```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("myAccount", "myBalance", "myCards", "myLoans").authenticated()
            .requestMatchers("notices", "welcome", "contact", "error").permitAll()
    );
    http.formLogin(withDefaults());
    http.httpBasic(withDefaults());
    return http.build();
}
```

- **SecurityFilterChain**: The core component that defines how incoming HTTP requests are secured in the application. Spring Security uses this filter chain to enforce security rules on each request.
  
- **authorizeHttpRequests()**: This method defines which HTTP requests need to be authenticated and which should be allowed without authentication.

---

### **2. Securing Specific Endpoints**

```java
.requestMatchers("myAccount", "myBalance", "myCards", "myLoans").authenticated()
```

- **`requestMatchers("myAccount", "myBalance", "myCards", "myLoans")`**: This defines a list of URL patterns (e.g., `myAccount`, `myBalance`, `myCards`, `myLoans`) that should require the user to be **authenticated** before accessing them.

- **`.authenticated()`**: This means that users must be logged in and authenticated to access these URLs. If they are not authenticated, they will be redirected to the login page (in the case of form-based login) or prompted for credentials (in the case of basic authentication).

#### **Example Scenario**:

Suppose your application has the following endpoints:
- `/myAccount` to view account details
- `/myBalance` to view the user's balance
- `/myCards` to manage payment cards
- `/myLoans` to manage loans

These pages should be protected so that only authenticated users can access them.

**What Happens?**
- If a user tries to access `http://example.com/myAccount` without being authenticated, they will be redirected to the login page.

```http
GET /myAccount
Response: 302 Redirect to /login
```

- After logging in, the user will be redirected back to `/myAccount` and can view their account details.

#### **Flow**:
1. User accesses `/myAccount` without being authenticated.
2. Spring Security intercepts the request and checks if the user is logged in.
3. Since the user is not authenticated, Spring Security redirects them to the login page.
4. Once the user logs in successfully, they are redirected back to `/myAccount` to view the protected resource.

---

### **3. Publicly Accessible Endpoints**

```java
.requestMatchers("notices", "welcome", "contact", "error").permitAll()
```

- **`requestMatchers("notices", "welcome", "contact", "error")`**: This defines a list of URL patterns that should be publicly accessible. These pages do not require the user to be authenticated to access them.

- **`.permitAll()`**: This means that these endpoints are open to everyone, whether they are authenticated or not. Any user can access them without being prompted to log in.

#### **Example Scenario**:

Suppose your application has the following public endpoints:
- `/notices` for displaying general notices
- `/welcome` for a welcome page
- `/contact` for users to contact customer support
- `/error` for handling error pages

These are pages that anyone should be able to view without logging in.

**What Happens?**
- If a user visits `http://example.com/welcome`, they will be able to view the page directly without authentication.

```http
GET /welcome
Response: 200 OK
```

- Even if the user is not logged in, Spring Security will allow access to this endpoint because of the `.permitAll()` configuration.

---

### **4. Form-Based Login**

```java
http.formLogin(withDefaults());
```

- **formLogin()**: This enables form-based login, where users are required to enter their credentials (username and password) via an HTML form.
  
- **withDefaults()**: This applies the default configuration for form-based login, which includes:
  - A default login page at `/login`
  - A default login processing endpoint at `/login`
  - A default logout page at `/logout`

#### **Example Flow of Form Login**:

1. A user tries to access a protected resource like `/myAccount`.
2. Since the user is not authenticated, they are redirected to the login page (`/login`).
3. The user submits their credentials via the login form.
4. If authentication is successful, the user is redirected back to the originally requested page (e.g., `/myAccount`).

---

### **5. HTTP Basic Authentication**

```java
http.httpBasic(withDefaults());
```

- **httpBasic()**: This enables **HTTP Basic Authentication**, where the client provides credentials in the request's `Authorization` header. Basic authentication is often used in REST APIs where clients (such as Postman or other programs) can send the `username` and `password` directly with each request.

- **withDefaults()**: This applies the default behavior for basic authentication. It expects the `Authorization` header to contain the credentials in the format:

```http
Authorization: Basic base64(username:password)
```

#### **Example of Basic Authentication**:

If a client tries to access a protected API like `/myBalance` using HTTP Basic Authentication, they would send a request like this:

```http
GET /myBalance
Authorization: Basic dXNlcjpwYXNzd29yZA==
```

- If the credentials are correct, the user will be authenticated, and the resource will be returned.
- If the credentials are incorrect, the server will return a **401 Unauthorized** response.

---

### **Flow of the Security Process**

1. **User Accesses Public Resource**:
   - If a user accesses a public resource like `/welcome`, Spring Security allows access without requiring authentication (`permitAll()`).

2. **User Tries to Access Protected Resource**:
   - If a user tries to access a protected resource like `/myAccount` without being logged in, Spring Security will intercept the request and check the user's authentication status.
   - If the user is not authenticated, they are either:
     - Redirected to the login page for form-based login.
     - Prompted for credentials using basic authentication if it is an API request.

3. **User Logs In**:
   - If the user successfully logs in (via form login or basic auth), they are authenticated and allowed to access the protected resources.

---

### **Conclusion**

This security configuration achieves the following:

- **Publicly accessible pages**: Some pages (`notices`, `welcome`, `contact`, `error`) are accessible to anyone without needing to log in.
- **Secured pages**: Certain resources (`myAccount`, `myBalance`, `myCards`, `myLoans`) are protected and can only be accessed by authenticated users.
- **Form Login**: Form-based login is enabled for users to log in via a web form.
- **Basic Authentication**: HTTP Basic authentication is enabled, useful for APIs or programmatic access.

With this setup, you can secure specific areas of your web application while leaving others open for public access, providing flexibility and security.
## 005 How to disable formLogin and httpBasic authentication

```java
package com.wchamara.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("myAccount", "myBalance", "myCards", "myLoans").authenticated()
                .requestMatchers("notices", "welcome", "contact", "error").permitAll()
        );
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }


}

```

![alt text](image-4.png)

## 006 httpBasic authentication testing using postman
![alt text](image-5.png)
![alt text](image-6.png)