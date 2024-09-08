# 

## 001 Course Introduction

![alt text](image.png)
![alt text](image-1.png)
![alt text](image-2.png)
![alt text](image-3.png)
![alt text](image-4.png)

## 003 Creating a simple Spring Boot app with out security - Part 1
![alt text](image-5.png)
![alt text](image-6.png)
![alt text](image-7.png)
![alt text](image-8.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.wchamara</groupId>
    <artifactId>springsecurity</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springsecurity</name>
    <description>springsecurity</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>21</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```
The file you've provided is a **Maven Project Object Model (POM)** file, which is used to define the configuration of a **Maven project**. In this case, it defines a **Spring Boot application** that uses **Java 21**. The POM includes project metadata, dependencies, and build configuration.

Let’s break down the key sections and explain each part in detail with examples.

---

### 1. **Basic Project Information**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
```

- **POM Version (`modelVersion`)**: Specifies the version of the POM format. For Maven 3.x, this should always be `4.0.0`.
- **XML Namespace and Schema**: This defines the XML schema for the POM file, ensuring that the XML adheres to the proper format.

---

### 2. **Parent Project: Spring Boot Starter Parent**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.3</version>
    <relativePath/>
</parent>
```

- **`spring-boot-starter-parent`**: This specifies that the project uses **Spring Boot** as its base. The parent POM provides common configuration and dependency management for Spring Boot applications.
- **Version `3.3.3`**: This is the version of **Spring Boot** used in your project.

**What the Parent Provides**:
1. **Dependency Management**: Spring Boot handles dependency versions, so you don’t have to manually specify versions for Spring-related dependencies.
2. **Plugin Management**: Common plugins like the Spring Boot Maven Plugin are pre-configured.
3. **Default Build Settings**: It provides default Maven settings such as encoding, source compatibility, and testing tools.

**Example**:
Instead of specifying each version for dependencies like `spring-boot-starter-web`, the parent POM will automatically manage the appropriate versions. This reduces the need to manage many dependencies manually.

---

### 3. **Project Coordinates and Metadata**

```xml
<groupId>com.wchamara</groupId>
<artifactId>springsecurity</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>springsecurity</name>
<description>springsecurity</description>
```

- **`groupId`**: The group identifier for your project, typically representing the organization or team. In this case, it’s `"com.wchamara"`.
- **`artifactId`**: The name of your project or artifact. This is `"springsecurity"` in this example, indicating it’s a Spring Security-related project.
- **`version`**: The current version of the project. In this case, it’s `"0.0.1-SNAPSHOT"`, which is a development version. `SNAPSHOT` indicates that the version is not a final release and is subject to change.
- **`name` and `description`**: Metadata describing the project.

**Example**:
If this project is built as a JAR or WAR file, it will have the following coordinates:
- `com.wchamara:springsecurity:0.0.1-SNAPSHOT`

---

### 4. **Java Version**

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

- **`java.version`**: Specifies the Java version for the project. In this case, it’s **Java 21**, which is a newer version of the Java programming language.

**Purpose**:
This property ensures that the project is compiled and run with Java 21. Maven will use this property in the build lifecycle to ensure compatibility with this version.

---

### 5. **Dependencies**

The `<dependencies>` section defines the libraries (dependencies) the project needs to compile and run. Maven will download these libraries from a central repository and include them in the project.

#### 5.1 **Spring Boot Starter Web**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- **`spring-boot-starter-web`**: This is a Spring Boot starter for building **web applications**, including REST APIs. It includes several important modules:
  - **Spring MVC**: For building web and RESTful services.
  - **Embedded Tomcat**: An embedded servlet container for running the application.
  - **Jackson**: For JSON serialization and deserialization.

**Example Use Case**:
If you are building a REST API or a web-based application, this dependency gives you everything you need. For example, you can create a REST controller like this:

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
```

By using `spring-boot-starter-web`, you can quickly set up endpoints like this without needing to configure many web-related dependencies manually.

#### 5.2 **Spring Boot DevTools**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

- **`spring-boot-devtools`**: This dependency provides tools to help with the development of Spring Boot applications. Its key feature is **hot reloading**, which automatically reloads the application when code changes are detected, allowing faster development.

- **`scope="runtime"`**: The dependency is only included at runtime and not part of the compile-time classpath.
- **`optional="true"`**: Marks the dependency as optional, meaning that other projects depending on this project won’t automatically inherit this dependency.

**Example**:
When developing a Spring Boot application, any changes you make to the code will trigger a restart of the application automatically. This improves productivity by reducing the time required to manually restart the application.

#### 5.3 **Spring Boot Starter Test**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

- **`spring-boot-starter-test`**: This is a starter dependency that provides everything you need for testing Spring Boot applications, including:
  - **JUnit 5**: For writing unit tests.
  - **Mockito**: For mocking dependencies in tests.
  - **Spring Test**: Tools to test Spring components, like controllers and services.
  - **Hamcrest** and **AssertJ**: For more expressive assertions.

- **`scope="test"`**: This dependency is only required for testing, so it will only be included in the classpath during the testing phase.

**Example**:
You can write unit tests for your Spring Boot components like this:

```java
@SpringBootTest
public class HelloControllerTest {
  
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSayHello() throws Exception {
        mockMvc.perform(get("/hello"))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello, World!"));
    }
}
```

---

### 6. **Build Section**

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

- **`spring-boot-maven-plugin`**: This plugin is necessary to package your Spring Boot application as a **JAR** or **WAR** file. It allows you to:
  - **Package** the application with `mvn package`.
  - **Run** the application with `mvn spring-boot:run`.
  - **Build executable JARs**: Spring Boot applications can be packaged as self-contained, executable JARs with an embedded Tomcat server.

**Example Use Case**:
When you run `mvn package`, this plugin will package your application into a single executable JAR file that can be run with:
```bash
java -jar target/springsecurity-0.0.1-SNAPSHOT.jar
```

This JAR will contain everything needed to run the application, including all dependencies, configuration, and an embedded web server.

---

### Example Use Case: A Simple Spring Boot Application

1. **Create a REST API**:
   With the dependencies for **`spring-boot-starter-web`**, you can create a basic REST controller like this:
   
   ```java
   @RestController
   public class GreetingController {
   
       @GetMapping("/greeting")
       public String greet() {
           return "Hello, World!";
       }
   }
   ```
   Running the application (`mvn spring-boot:run`) will expose a REST endpoint at `/greeting` that returns a simple message.

2. **Testing**:
   With **`spring-boot-starter-test`**, you can write unit tests for the controller:
   
   ```java
   @SpringBootTest
   public class GreetingControllerTest {
   
       @Autowired
       private MockMvc mockMvc;
   
       @Test
       public void testGreeting() throws Exception

 
           mockMvc.perform(get("/greeting"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("Hello, World!"));
       }
   }
   
   ```

   This test verifies that the `/greeting` endpoint behaves as expected.

---

### Conclusion

This POM file defines a basic Spring Boot project with dependencies for web development and testing. It uses Spring Boot 3.3.3 and is configured to support Java 21. The key features include support for building REST APIs (`spring-boot-starter-web`), automatic hot-reloading during development (`spring-boot-devtools`), and comprehensive testing tools (`spring-boot-starter-test`). The **build** section ensures that you can package the application into a runnable JAR file with the **Spring Boot Maven Plugin**.

## 004 Creating a simple Spring Boot app with out security - Part 2

```properties
spring.application.name=${SPRING_APP_ID:bank}
logging.pattern.console=${LOG_PATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}
```

```java
package com.wchamara.springsecurity.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Bank";
    }
}

```

This Java class defines a simple REST API using Spring Boot. The **WelcomeController** contains a single endpoint (`/welcome`) that returns a message when accessed. Let’s break down the structure and purpose of this controller, its components, and how it works in a Spring Boot application, including example usage.

---

### 1. **Controller Class**

```java
@RestController
public class WelcomeController {
```

- **`@RestController`**: This annotation is a **Spring stereotype** that marks the class as a RESTful controller. It indicates that the class will handle incoming HTTP requests and return data (typically as JSON or plain text) to the client.
  - Combines two annotations:
    - **`@Controller`**: Marks the class as a Spring MVC controller that can handle HTTP requests.
    - **`@ResponseBody`**: Automatically serializes the return value of methods into the HTTP response body. In this case, it will return a plain text response.

- **Purpose**: The `WelcomeController` class defines the behavior for an endpoint that clients can access via HTTP requests.

---

### 2. **`@GetMapping("/welcome")`**

```java
@GetMapping("/welcome")
public String welcome() {
    return "Welcome to the Bank";
}
```

- **`@GetMapping("/welcome")`**: This annotation maps **HTTP GET requests** to the `welcome()` method. It defines an endpoint accessible via the `/welcome` URL path.
  - **`/welcome`**: When a client sends a GET request to `/welcome`, this method is executed.
  - **`@GetMapping`**: This is a specialized version of the **`@RequestMapping`** annotation for handling **GET** requests specifically. GET requests are typically used to retrieve data from the server.

**Example**:
- **URL**: `http://localhost:8080/welcome`
- **HTTP Method**: `GET`
- **Response**: `"Welcome to the Bank"`

**Real-World Use Case**:
This could be the entry point to a banking application, where users are greeted with this message when they visit the main page.

---

### 3. **Method: `welcome()`**

```java
public String welcome() {
    return "Welcome to the Bank";
}
```

- **Return Type**: `String`
  - The method returns a **plain text string** (`"Welcome to the Bank"`), which will be sent back to the client as the response body.
  
- **Response**: The string `"Welcome to the Bank"` is the HTTP response that the client receives when they access the `/welcome` endpoint.

---

### 4. **How It Works in a Spring Boot Application**

1. **Spring Boot Setup**:
   - In a typical Spring Boot application, this controller is part of the web layer.
   - When the application starts, Spring Boot automatically detects this controller because of the `@RestController` annotation and registers it as a request handler for the `/welcome` endpoint.

2. **Client Requests**:
   - When a client (such as a web browser or a REST client like Postman) sends an HTTP GET request to `/welcome`, Spring Boot forwards the request to the `welcome()` method.
   - The `welcome()` method processes the request and returns the plain text response `"Welcome to the Bank"`.

---

### 5. **Example Scenarios**

#### Example 1: Accessing the Endpoint via Web Browser

When you run the Spring Boot application, the server starts at a default port (e.g., `8080`). If you open a browser and navigate to the following URL:

```plaintext
http://localhost:8080/welcome
```

The browser will display the message:

```plaintext
Welcome to the Bank
```

This means that the browser made an HTTP GET request to the `/welcome` endpoint, and the `WelcomeController` responded with the plain text message.

#### Example 2: Accessing the Endpoint via cURL (Command Line)

You can also access the endpoint via **cURL**, a command-line tool for making HTTP requests. Running the following command:

```bash
curl http://localhost:8080/welcome
```

Will output:

```plaintext
Welcome to the Bank
```

This shows that the `WelcomeController` correctly handled the GET request and returned the appropriate response.

#### Example 3: Using Postman to Access the Endpoint

You can use **Postman** (or any other REST client) to test this endpoint as follows:

1. Open Postman.
2. Set the request type to `GET`.
3. Enter the URL `http://localhost:8080/welcome`.
4. Click **Send**.

The response from the server will be:

```plaintext
Welcome to the Bank
```

---

### 6. **How Spring Boot Handles the Request**

1. **Request Handling**:
   - When the client sends an HTTP GET request to `/welcome`, Spring Boot's **DispatcherServlet** receives the request and maps it to the appropriate controller method, in this case, `welcome()`.

2. **Method Execution**:
   - The `welcome()` method is executed, and the string `"Welcome to the Bank"` is returned.

3. **Response**:
   - Spring Boot automatically sends the returned string as the HTTP response body, with a `200 OK` status code.
   - Because the `@RestController` annotation includes `@ResponseBody`, Spring Boot automatically writes the string to the HTTP response.

---

### 7. **Expanding the Example: JSON Response**

While the current controller returns a **plain text** response, Spring Boot also allows controllers to return **JSON**. Here’s how you could modify the controller to return a **JSON** object instead of a plain string.

```java
@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Bank");
        return response;
    }
}
```

- **Return Type**: The method now returns a **Map** (a simple key-value structure).
- **Response**: The response will now be in JSON format:

```json
{
  "message": "Welcome to the Bank"
}
```

This is useful when building REST APIs, where responses are typically formatted in JSON for easy parsing by clients.

---

### 8. **Securing the Endpoint**

In a real-world banking application, you’d likely want to protect this endpoint from unauthorized access. You can integrate **Spring Security** to require authentication before accessing this endpoint. Here’s a brief example:

1. Add Spring Security dependency in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   ```

2. Secure the `/welcome` endpoint:
   ```java
   @GetMapping("/welcome")
   @PreAuthorize("hasRole('USER')")
   public String welcome() {
       return "Welcome to the Bank";
   }
   ```

   This requires that the client making the request is authenticated and has the role `USER` before they can access the `/welcome` endpoint.

---

### Conclusion

The `WelcomeController` is a simple example of a **REST controller** in Spring Boot that handles HTTP GET requests. It demonstrates the basic setup for exposing an API endpoint using `@RestController` and `@GetMapping`. This example is extendable to more complex use cases, such as returning JSON data, securing endpoints with authentication, or integrating with other services to perform business logic. 

By using Spring Boot’s simplified annotations and defaults, you can quickly create powerful web services without extensive configuration.

## 005 Securing Spring Boot basic app using Spring Security & static credentials
## 006 Funny memes on Security
## 007 What is Security & Why it is important
## 008 Quick introduction to Servlets & Filters
## 009 Introduction to Spring Security Internal flow - Theory
## 010 Demo of Spring Security internal flow - Part 1
## 011 Demo of Spring Security internal flow - Part 2
## 012 Sequence flow of the Spring Security default behaviour
## 013 Understanding on how multiple requests work with out credentials
