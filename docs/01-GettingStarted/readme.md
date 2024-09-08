# 01 - Getting Started 


- [01 - Getting Started](#01---getting-started)
  - [001 Course Introduction](#001-course-introduction)
  - [003 Creating a simple Spring Boot app with out security - Part 1](#003-creating-a-simple-spring-boot-app-with-out-security---part-1)
    - [1. **Basic Project Information**](#1-basic-project-information)
    - [2. **Parent Project: Spring Boot Starter Parent**](#2-parent-project-spring-boot-starter-parent)
    - [3. **Project Coordinates and Metadata**](#3-project-coordinates-and-metadata)
    - [4. **Java Version**](#4-java-version)
    - [5. **Dependencies**](#5-dependencies)
      - [5.1 **Spring Boot Starter Web**](#51-spring-boot-starter-web)
      - [5.2 **Spring Boot DevTools**](#52-spring-boot-devtools)
      - [5.3 **Spring Boot Starter Test**](#53-spring-boot-starter-test)
    - [6. **Build Section**](#6-build-section)
    - [Example Use Case: A Simple Spring Boot Application](#example-use-case-a-simple-spring-boot-application)
    - [Conclusion](#conclusion)
  - [004 Creating a simple Spring Boot app with out security - Part 2](#004-creating-a-simple-spring-boot-app-with-out-security---part-2)
    - [1. **`spring.application.name=${SPRING_APP_ID:bank}`**](#1-springapplicationnamespring_app_idbank)
      - [Example:](#example)
    - [2. **`logging.pattern.console=${LOG_PATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}`**](#2-loggingpatternconsolelog_pattern_consolegreendhhmmsssss-blue-5level-redthread-yellowlogger15---msgn)
      - [Detailed Breakdown of the Log Pattern:](#detailed-breakdown-of-the-log-pattern)
      - [Example of Console Output:](#example-of-console-output)
    - [**Why Customize the Log Pattern?**](#why-customize-the-log-pattern)
      - [Example Use Case:](#example-use-case)
    - [**Summary**](#summary)
    - [1. **Controller Class**](#1-controller-class)
    - [2. **`@GetMapping("/welcome")`**](#2-getmappingwelcome)
    - [3. **Method: `welcome()`**](#3-method-welcome)
    - [4. **How It Works in a Spring Boot Application**](#4-how-it-works-in-a-spring-boot-application)
    - [5. **Example Scenarios**](#5-example-scenarios)
      - [Example 1: Accessing the Endpoint via Web Browser](#example-1-accessing-the-endpoint-via-web-browser)
      - [Example 2: Accessing the Endpoint via cURL (Command Line)](#example-2-accessing-the-endpoint-via-curl-command-line)
      - [Example 3: Using Postman to Access the Endpoint](#example-3-using-postman-to-access-the-endpoint)
    - [6. **How Spring Boot Handles the Request**](#6-how-spring-boot-handles-the-request)
    - [7. **Expanding the Example: JSON Response**](#7-expanding-the-example-json-response)
    - [8. **Securing the Endpoint**](#8-securing-the-endpoint)
    - [Conclusion](#conclusion-1)
  - [005 Securing Spring Boot basic app using Spring Security \& static credentials](#005-securing-spring-boot-basic-app-using-spring-security--static-credentials)
  - [006 Funny memes on Security](#006-funny-memes-on-security)
  - [007 What is Security \& Why it is important](#007-what-is-security--why-it-is-important)
  - [008 Quick introduction to Servlets \& Filters](#008-quick-introduction-to-servlets--filters)
  - [009 Introduction to Spring Security Internal flow - Theory](#009-introduction-to-spring-security-internal-flow---theory)
  - [010 Demo of Spring Security internal flow - Part 1](#010-demo-of-spring-security-internal-flow---part-1)
  - [011 Demo of Spring Security internal flow - Part 2](#011-demo-of-spring-security-internal-flow---part-2)
  - [012 Sequence flow of the Spring Security default behaviour](#012-sequence-flow-of-the-spring-security-default-behaviour)
  - [013 Understanding on how multiple requests work with out credentials](#013-understanding-on-how-multiple-requests-work-with-out-credentials)


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

This configuration snippet is part of a **Spring Boot** application's configuration file, likely from `application.properties` or `application.yml`. These two properties define the application's name and configure the console log pattern.

Let’s break down each part to understand what they do and how they are used with examples:

---

### 1. **`spring.application.name=${SPRING_APP_ID:bank}`**

This property sets the name of the Spring Boot application, which is important for logging, monitoring, and management purposes. It uses a **placeholder** with an environment variable fallback.

- **`spring.application.name`**: This property defines the name of the Spring Boot application. It’s a key property used for identifying the application in different Spring features such as **Spring Cloud**, **Spring Boot Admin**, and **logging**.

- **`${SPRING_APP_ID:bank}`**: This part indicates that the value of `spring.application.name` will be set in the following manner:
  - **`${SPRING_APP_ID}`**: This refers to an **environment variable** named `SPRING_APP_ID`. If this environment variable is set in the operating system or the application's deployment environment, the application name will be set to the value of `SPRING_APP_ID`.
  - **`:bank`**: If the `SPRING_APP_ID` environment variable is **not set**, the application name will default to `"bank"`. This is a fallback value.

#### Example:

- **Scenario 1: `SPRING_APP_ID` is set in the environment**:
  If you have an environment variable like this:
  ```bash
  export SPRING_APP_ID=my-custom-app
  ```

  The application name will be set to `my-custom-app` in this case. This can be used for differentiating instances in a cloud environment, for example:
  ```yaml
  spring:
    application:
      name: ${SPRING_APP_ID:bank}
  ```

  When you start your Spring Boot application, the following logs will include `my-custom-app`:
  ```bash
  2024-09-05 12:45:12.123 INFO [my-custom-app] - Application started
  ```

- **Scenario 2: `SPRING_APP_ID` is not set**:
  If the `SPRING_APP_ID` environment variable is **not set**, the application name defaults to `bank`:
  ```bash
  2024-09-05 12:45:12.123 INFO [bank] - Application started
  ```

**Use Case**:
This feature is useful when you need to set different names for the application based on the environment. For example, in **production**, you might use a name like `"bank-production"`, while in **development**, it could be `"bank-development"`. This helps in monitoring and distinguishing between different instances of the application.

---

### 2. **`logging.pattern.console=${LOG_PATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}`**

This property defines the **log pattern** that Spring Boot uses when writing logs to the console. It uses placeholders and ANSI color codes to customize the log output.

- **`logging.pattern.console`**: This property sets the **format of log messages** in the console. You can customize the format using **placeholders** for various log elements, such as the timestamp, log level, thread name, logger name, and the actual log message.

- **`${LOG_PATTERN_CONSOLE:%green(...)}%blue(...)...`**: This uses a placeholder with a default value. Let’s break it down:
  - **`${LOG_PATTERN_CONSOLE}`**: This references an external environment variable or system property. If it’s defined, its value will be used to set the log pattern.
  - **`:%green(... %msg%n)`**: If `LOG_PATTERN_CONSOLE` is **not set**, this default value is used, which specifies a **colored log pattern**. This pattern uses **ANSI colors** to make the logs easier to read in the console.

#### Detailed Breakdown of the Log Pattern:

```java
%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n
```

- **`%green(...)`**: This makes the enclosed content **green** in the console using ANSI color codes. You can use different colors for different parts of the log to highlight important information.
  
- **`%d{HH:mm:ss.SSS}`**: This represents the **timestamp** of the log entry in the format **hours:minutes:seconds.milliseconds**.
  - Example: `12:45:12.123`.

- **`%blue(%-5level)`**: This prints the **log level** (e.g., INFO, DEBUG, ERROR) in **blue**. The `%-5level` ensures that the log level is left-aligned and takes up 5 characters, providing consistent alignment.
  - Example: `INFO`, `ERROR`.

- **`%red([%thread])`**: This prints the **name of the thread** in **red**, enclosed in square brackets.
  - Example: `[main]`.

- **`%yellow(%logger{15})`**: This prints the **logger name** (the class or package generating the log message) in **yellow**. The `{15}` means that the logger name is truncated to a maximum of 15 characters.
  - Example: `com.example.Controller`.

- **`- %msg`**: This prints the actual **log message**. The log message can be anything the application logs, such as error messages or information about the application's behavior.
  - Example: `Application started`.

- **`%n`**: This inserts a **newline character** at the end of the log message.

#### Example of Console Output:

With the log pattern configured as above, a typical log message might look like this:

```bash
12:45:12.123 INFO  [main]  com.example.Service - Application started
```

- **Green** timestamp: `12:45:12.123`
- **Blue** log level: `INFO`
- **Red** thread: `[main]`
- **Yellow** logger name: `com.example.Service`
- Log message: `Application started`

---

### **Why Customize the Log Pattern?**

Customizing the log pattern is useful for making logs more readable, especially when troubleshooting or debugging. For example:
1. **Coloring**: By color-coding different parts of the log, you can easily distinguish between timestamps, log levels, threads, and messages.
2. **Alignment**: The `%5level` ensures that log levels are aligned, which can make reading large log files easier.
3. **Truncated Logger Names**: Limiting the length of logger names prevents long class or package names from making logs harder to read, especially in complex applications.

#### Example Use Case:

In a **microservice architecture**, different services log information concurrently. By using a custom log pattern, such as coloring by log level or thread name, you can easily differentiate logs for different threads or services. For example, `ERROR` messages might stand out in **red**, while `INFO` messages could be in **blue**, helping you quickly identify issues.

---

### **Summary**

1. **`spring.application.name=${SPRING_APP_ID:bank}`**:
   - Sets the Spring Boot application's name.
   - If the `SPRING_APP_ID` environment variable is defined, its value is used; otherwise, the name defaults to `"bank"`.
   - Helps in identifying the application in logs, monitoring systems, and microservices environments.

2. **`logging.pattern.console=${LOG_PATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}`**:
   - Customizes the console log output format.
   - Uses color coding and formatting to improve readability.
   - Defines elements like the timestamp, log level, thread name, logger, and log message.
   - Makes it easier to distinguish important log entries, such as errors, in a multi-threaded or multi-service environment.

By customizing these configurations, you make your Spring Boot application more manageable and its logs easier to analyze, especially in production environments where quick troubleshooting and monitoring are critical.


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
