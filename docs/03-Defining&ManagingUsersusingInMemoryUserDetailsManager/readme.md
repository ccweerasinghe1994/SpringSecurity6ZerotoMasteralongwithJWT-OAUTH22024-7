# 03 - Defining & Managing Users using InMemoryUserDetailsManager

## 001 Configuring users using InMemoryUserDetailsManager
let's remove username and password from application.properties and configure users using InMemoryUserDetailsManager

```properties
spring.application.name=${SPRING_APP_ID:bank}
logging.pattern.console=${LOG_PATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}
logging.level.org.springframework.security=${SPRING_LOG_LEVEL_SECURITY:TRACE}
```

```java
package com.wchamara.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);
//        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{noop}54321").authorities("admin").build();

        return new InMemoryUserDetailsManager(user, admin);

    }


}

```

The method:

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
    UserDetails admin = User.withUsername("admin").password("{noop}54321").authorities("admin").build();

    return new InMemoryUserDetailsManager(user, admin);
}
```

is responsible for defining an in-memory user store (called `InMemoryUserDetailsManager`) that Spring Security will use for authentication. Let’s break this down deeply with examples and explain the components involved.

---

### **1. What is `UserDetailsService`?**

- **`UserDetailsService`** is a core interface in Spring Security used to retrieve user information such as username, password, and authorities (roles or permissions).
- **`InMemoryUserDetailsManager`** is an implementation of `UserDetailsService` that stores user details **in memory** (i.e., it doesn’t rely on a database or external storage).

This method provides two in-memory users: `user` and `admin`, each with different credentials and authorities.

### **2. Defining UserDetails for `user` and `admin`**

```java
UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
UserDetails admin = User.withUsername("admin").password("{noop}54321").authorities("admin").build();
```

#### **UserDetails**:
- **`UserDetails`** is an interface that represents a user's credentials and authorities.
- The static **`User.withUsername()`** method is a convenient way to build `UserDetails` objects.

#### **User Creation**:
1. **User**:
   - Username: `"user"`
   - Password: `"password"` (with `{noop}` indicating that the password is stored without hashing).
   - Authorities: `"read"` (this user has a `read` authority, meaning they can perform operations that require this permission).

   ```java
   UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
   ```

   This creates a user object with the username `"user"`, a plain-text password `"password"`, and an authority `"read"`.

2. **Admin**:
   - Username: `"admin"`
   - Password: `"54321"` (again stored without encryption, indicated by `{noop}`).
   - Authorities: `"admin"` (the admin has an `admin` authority, typically used to differentiate user roles and permissions).

   ```java
   UserDetails admin = User.withUsername("admin").password("{noop}54321").authorities("admin").build();
   ```

---

### **3. Password Encoding with `{noop}`**

- **`{noop}`** is a password encoder ID that tells Spring Security to **not encode** the password. It means that the passwords `"password"` and `"54321"` are stored in plain text, which is usually **not recommended for production** but can be useful for testing or demo purposes.

- **Password Encoding**: Spring Security allows you to use different password encoders to store passwords securely. By using `{noop}`, you are bypassing any encoding, but you can easily change this to use encoding methods like BCrypt.

#### **Example** of using `BCryptPasswordEncoder` instead of `{noop}`:

```java
UserDetails user = User.withUsername("user").password(passwordEncoder().encode("password")).authorities("read").build();
```

Where `passwordEncoder()` would return a `BCryptPasswordEncoder`.

---

### **4. Authorities**

```java
.authorities("read")
```

- **Authorities** represent permissions or roles that a user has. In this case, the user `user` has the `"read"` authority, which could allow them to perform certain operations like reading data or accessing resources that require `read` permissions.
- Similarly, the `admin` user has the `"admin"` authority, giving them a different set of permissions. Typically, admin users have more privileges.

#### **Example**:
If your application defines endpoints like:
- `/viewData` that requires `read` authority
- `/adminPanel` that requires `admin` authority

Then:
- The user `user` will only be able to access `/viewData`.
- The user `admin` will be able to access both `/viewData` and `/adminPanel`.

### **5. InMemoryUserDetailsManager**

```java
return new InMemoryUserDetailsManager(user, admin);
```

- **`InMemoryUserDetailsManager`**: This is an implementation of `UserDetailsService` that stores user credentials in memory. It takes the `user` and `admin` objects created above and manages their credentials for authentication purposes.

- Spring Security will use this in-memory manager to look up the user’s credentials when they attempt to log in. If a user’s credentials match one of the `UserDetails` objects (`user` or `admin`), they will be authenticated and granted access according to their authorities.

---

### **How This Works in Practice**

1. **Authentication Flow**:
   - A user tries to access a protected resource.
   - Spring Security intercepts the request and prompts for credentials (either through a login form or basic authentication, depending on your setup).
   - The user submits a username and password (e.g., `user` with `password`).
   - **`InMemoryUserDetailsManager`** checks the submitted credentials against the stored `UserDetails` for `user` and `admin`.
   - If the credentials match one of the users (`user` or `admin`), the user is authenticated and granted the authorities associated with their account.

2. **Authorization**:
   - Once authenticated, Spring Security uses the **authorities** (e.g., `"read"` for `user` or `"admin"` for `admin`) to control which resources the user can access.
   - If a resource is protected by an authority that the user doesn’t have, access will be denied.

---

### **Example Scenario**

Let’s say you have two endpoints in your application:

- `/viewData` (only users with the `"read"` authority can access this endpoint)
- `/adminPanel` (only users with the `"admin"` authority can access this endpoint)

The configuration in `ProjectSecurityConfig` will manage access to these resources as follows:

1. **User logs in as `user`**:
   - Username: `user`
   - Password: `password`
   - Authorities: `read`

   If the user `user` tries to access `/viewData`, they will be allowed because they have the `"read"` authority. However, if they try to access `/adminPanel`, they will be denied because they do not have the `"admin"` authority.

2. **User logs in as `admin`**:
   - Username: `admin`
   - Password: `54321`
   - Authorities: `admin`

   If the `admin` user logs in, they will have access to both `/viewData` and `/adminPanel` because they have a higher privilege (`admin` authority).

---

### **Summary**

This code defines two users (`user` and `admin`) in memory using Spring Security's `InMemoryUserDetailsManager`. Each user has a specific set of authorities (permissions) and plain-text passwords (indicated by `{noop}`). Spring Security uses these details to authenticate and authorize users as they attempt to access different resources in the application.

- **`UserDetailsService`**: Manages user data.
- **`InMemoryUserDetailsManager`**: Stores user data in memory.
- **Authorities**: Control which resources each user can access based on their assigned roles or permissions.

This is a typical setup for simple applications or testing environments, where user credentials are stored in memory rather than in a database. In real-world applications, you'd likely use a database to store user credentials and a password encoder like `BCryptPasswordEncoder` to secure passwords.

![alt text](image.png)
## 002 Configuring PasswordEncoder using PasswordEncoderFactories
```java
package com.wchamara.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);
//        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$nwzTHZGwOOO7t9Eei0K00eBebzkhc4WHGh9UBgef5.F5PxtB7gnsK").authorities("admin").build();

        return new InMemoryUserDetailsManager(user, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}

```
In the provided code, we have two key components to explain:

1. **UserDetailsService**: This manages user credentials and authorities in memory.
2. **PasswordEncoder**: This is responsible for encoding passwords and verifying password matches during authentication.

Let’s break down each part of the code deeply with examples.

---

### **1. UserDetailsService and User Definitions**

The **`UserDetailsService`** defines two users (`user` and `admin`) in memory, each with specific roles (authorities) and passwords.

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
    UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$nwzTHZGwOOO7t9Eei0K00eBebzkhc4WHGh9UBgef5.F5PxtB7gnsK").authorities("admin").build();

    return new InMemoryUserDetailsManager(user, admin);
}
```

#### **Explanation of the Users**:

1. **UserDetails for `user`**:
   - Username: `"user"`
   - Password: `"password"` (stored without encryption using `{noop}`)
   - Authority: `"read"`

   This user has minimal privileges and is only assigned the `"read"` authority. The password is stored in plaintext (due to the `{noop}` prefix). In real-world applications, this is not recommended, but it is useful for development or demo purposes.

   ```java
   UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
   ```

2. **UserDetails for `admin`**:
   - Username: `"admin"`
   - Password: A **bcrypt-encoded** password (`$2y$14$nwzTHZGwOOO7t9Eei0K00eBebzkhc4WHGh9UBgef5.F5PxtB7gnsK`)
   - Authority: `"admin"`

   This user has a more privileged role, assigned the `"admin"` authority. The password is **bcrypt-encoded**, meaning it has been hashed using the bcrypt algorithm, which is a secure, industry-standard algorithm for storing passwords. The password is more secure than the one for the `user` since it's encoded.

   ```java
   UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$nwzTHZGwOOO7t9Eei0K00eBebzkhc4WHGh9UBgef5.F5PxtB7gnsK").authorities("admin").build();
   ```

   - **`$2y$14$`** indicates the use of bcrypt with a cost factor of 14. This cost factor increases the computational complexity of encoding and verifying passwords, improving security by making brute-force attacks more difficult.
   - The rest of the string (`nwzTHZ...`) is the hashed version of the original password (let's assume the plaintext password was something like `54321` before hashing).

#### **Authorities**:

The users are assigned **authorities**, which represent permissions or roles in the system:

- `"read"`: This could be used to grant read-only access to specific resources or endpoints.
- `"admin"`: This could provide full administrative access to all resources.

**Example of How Authorities Are Used**:
- If your application has different routes for admin and general users, Spring Security will enforce these roles when users access various endpoints.
  - A user with the `"read"` role may only be able to access resources that involve viewing information.
  - An admin with the `"admin"` role will likely have broader access to all resources, including those requiring administrative control.

---

### **2. Password Encoding with PasswordEncoder**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

#### **What is Password Encoding?**

Password encoding is the process of transforming a plaintext password into a more secure, non-reversible format using cryptographic algorithms (like bcrypt, PBKDF2, etc.).

- **Why Password Encoding?** Passwords should never be stored in plaintext for security reasons. If a database is compromised, an attacker could retrieve the plaintext passwords. Encoding adds an extra layer of protection by storing a hashed version of the password, which is difficult to reverse-engineer.

#### **PasswordEncoderFactories.createDelegatingPasswordEncoder()**

This line creates a **delegating password encoder**. The delegating password encoder allows for different password encoding strategies, such as bcrypt, noop (no encoding), PBKDF2, etc.

- **Delegating**: It means that the encoder can handle multiple types of password encodings, and it will automatically delegate the encoding process to the appropriate encoder based on the prefix (like `{bcrypt}`, `{noop}`, etc.).
- **Prefixes**: Each password is stored with a prefix that indicates the encoding type, so Spring Security knows how to verify the password when a user tries to log in.

##### **Example Password Prefixes**:

- **`{bcrypt}`**: Indicates that the password is stored using bcrypt encoding.
- **`{noop}`**: No encoding is applied (i.e., plaintext password).
- **`{pbkdf2}`**: Indicates PBKDF2 encoding.
- **`{argon2}`**: Indicates Argon2 encoding.

**How Does This Work in Practice?**
- When Spring Security verifies a user’s password during login, it looks at the prefix to determine which password encoder to use.
  - If the password starts with `{noop}`, it compares the plaintext password directly.
  - If the password starts with `{bcrypt}`, it uses the bcrypt algorithm to verify the password.
  
```java
UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$nwzTHZGwOOO7t9Eei0K00eBebzkhc4WHGh9UBgef5.F5PxtB7gnsK").authorities("admin").build();
```

Here, Spring Security will use bcrypt to decode and verify the password for the `admin` user.

---

### **Example of How Password Encoding Works**

#### **User Authentication Flow**:

1. A user tries to log in with their credentials (username and password).
2. Spring Security retrieves the stored user details (username, password, authorities) from the `InMemoryUserDetailsManager`.
3. Spring Security checks the **password prefix** (e.g., `{noop}`, `{bcrypt}`):
   - If `{noop}`, it compares the raw (plaintext) password.
   - If `{bcrypt}`, it applies the bcrypt algorithm to the provided password and compares it to the stored hash.
4. If the passwords match, the user is authenticated and granted the appropriate authorities (e.g., `"read"` for a general user or `"admin"` for an admin).

#### **Flow Example**:

- **User login as `user`**:
   - Username: `user`
   - Password: `password`

   Since the password is stored with `{noop}` (i.e., no encoding), Spring Security directly compares the provided password `"password"` with the stored password.

```http
POST /login
{
  "username": "user",
  "password": "password"
}
```

If the credentials match, the user is authenticated and given access.

---

- **User login as `admin`**:
   - Username: `admin`
   - Password: (let’s assume it was `54321` before encoding with bcrypt)

   Spring Security uses the bcrypt encoder to hash the provided password (`54321`) and compares it with the stored hash `$2y$14$nwzTHZ...`. If they match, the user is authenticated.

```http
POST /login
{
  "username": "admin",
  "password": "54321"
}
```

---

### **Summary**

- **`UserDetailsService`**: Manages users (`user` and `admin`) stored in memory. The `user` has a plain-text password (using `{noop}`), while the `admin` has a bcrypt-encoded password (using `{bcrypt}`). The users are granted authorities based on their roles (`read` for `user`, `admin` for `admin`).
  
- **Password Encoding**: The `PasswordEncoder` ensures that passwords are stored securely using a delegating mechanism. It can handle different types of password encodings (like bcrypt, noop, etc.), and Spring Security uses the encoding prefix (e.g., `{noop}`, `{bcrypt}`) to determine how to verify passwords during login.

This configuration ensures that even though one user’s password is stored in plaintext (`noop`), the other user (`admin`) has a more secure bcrypt-encoded password. This demonstrates how Spring Security can handle different password encoding strategies while ensuring authentication security.

![alt text](image-1.png)

## 003 Demo of CompromisedPasswordChecker
![alt text](image-2.png)
![alt text](image-3.png)


```java
package com.wchamara.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

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
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);
//        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$OPivb1UNmmrSYTo5OQWDnuAZ78cS9DBCV5S9SsuWroQ10.wtm9JH6").authorities("admin").build();

        return new InMemoryUserDetailsManager(user, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}

```

The method:

```java
@Bean
public CompromisedPasswordChecker compromisedPasswordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
}
```

is introducing a mechanism to **check if a user's password has been compromised** by comparing it against a database of known compromised passwords. This is a common feature in security-conscious applications, helping to enhance the protection of user accounts by preventing users from using passwords that have already been exposed in data breaches.

### **1. What is a Compromised Password Checker?**

A **compromised password checker** is a service or utility that verifies if a password has been involved in known data breaches. The goal is to protect users from using insecure, compromised passwords that attackers could easily guess or exploit.

- **Why Check for Compromised Passwords?**  
  Over time, many passwords are leaked in data breaches and end up in lists circulated on the dark web. Attackers can use these lists for credential stuffing attacks, where they try commonly used or compromised passwords against multiple accounts. Checking for compromised passwords helps mitigate this risk by preventing users from using passwords that are already publicly known.

### **2. The `HaveIBeenPwnedRestApiPasswordChecker`**

In the code above, you’re using the `HaveIBeenPwnedRestApiPasswordChecker`. This class integrates with the **Have I Been Pwned (HIBP)** API to check if a password has appeared in known breaches.

- **Have I Been Pwned** is a popular online service that provides a large database of passwords that have been leaked in various breaches. It allows applications to query their API and determine whether a password has been compromised in any data breaches.

By using `HaveIBeenPwnedRestApiPasswordChecker`, your Spring Security application will check the passwords of users during certain operations (like registration or password change) and alert if the password has been involved in a breach.

### **3. How Does the Have I Been Pwned API Work?**

The HIBP API works in a **privacy-preserving** manner using a technique called **k-anonymity**. Here’s how it works:

1. **Password Hashing**: The password that needs to be checked is hashed using the SHA-1 algorithm (the hashing is done on the client-side).
2. **Prefix Matching**: The first five characters of the hash are sent to the HIBP API.
3. **Result Lookup**: The API returns a list of all hash suffixes that match the given prefix (along with how many times the hash has been seen in breaches).
4. **Comparison**: The client compares the full password hash with the returned list to determine if the password has been compromised.

### **4. Example Scenario**

Let’s say a user tries to register or change their password in your application. During that process, Spring Security checks if the password has been compromised.

1. **User Registration**:
   - A user enters a password during the registration process.
   - Before storing the password, Spring Security uses the `HaveIBeenPwnedRestApiPasswordChecker` to check if the password has appeared in a data breach.

2. **Compromised Password**:
   - If the password exists in the HIBP database, an error is returned, instructing the user to choose a different password.

3. **Safe Password**:
   - If the password has not been compromised, the registration process continues, and the password is safely stored (using bcrypt encoding, for instance).

#### Example Flow:

1. User tries to register with the password `123456`.
2. The password is hashed using SHA-1.
3. The first five characters of the hash are sent to the HIBP API.
4. The API returns a list of hashes that match the prefix.
5. The application checks the full hash against the returned results.
6. If a match is found (indicating that the password `123456` has been compromised), the registration is blocked with an error message like:
   ```plaintext
   "This password has been involved in a data breach. Please choose a different password."
   ```

### **5. How the Checker Fits into the Application**

By adding this method to the configuration:

```java
@Bean
public CompromisedPasswordChecker compromisedPasswordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
}
```

You are essentially plugging in a service that will:

- **Automatically validate passwords** when a user sets or changes a password.
- **Block compromised passwords** from being used in your application.

This increases the security of your application by ensuring that even if a user chooses a weak or commonly used password, they will be warned and required to choose a more secure password.

### **6. Customizing the Password Checker**

The default implementation checks against the HIBP service, but you could customize this to use a different API or a local database of known compromised passwords if needed. In more advanced configurations, you might:
- **Cache results** locally to reduce API calls.
- **Throttle API requests** to avoid hitting rate limits from HIBP.
- **Integrate with other password validation strategies** like length and complexity requirements.

### **Example of How it Works in Your Application**

Assuming your `ProjectSecurityConfig` class is configured like this:

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
    UserDetails admin = User.withUsername("admin").password("{bcrypt}$2y$14$OPivb1UNmmrSYTo5OQWDnuAZ78cS9DBCV5S9SsuWroQ10.wtm9JH6").authorities("admin").build();

    return new InMemoryUserDetailsManager(user, admin);
}
```

1. A new user tries to register with the password `"123456"`.
2. The **`HaveIBeenPwnedRestApiPasswordChecker`** checks if `"123456"` is in the HIBP database.
3. Since `"123456"` is a known compromised password (it’s one of the most common passwords ever), the API returns a match.
4. The user receives an error message asking them to pick a different password.
5. The user changes the password to a more secure one, like `"Passw0rd!123"`.
6. The API checks the new password, finds no match, and allows the user to proceed with registration.

---

### **7. Conclusion**

The `CompromisedPasswordChecker` using `HaveIBeenPwnedRestApiPasswordChecker` is a valuable addition to your security setup. It ensures that users do not choose passwords that have been exposed in data breaches, significantly reducing the risk of account compromise due to weak or common passwords.

By integrating this into your Spring Security application:
- You’re enhancing the security posture by checking passwords against a large dataset of known compromised passwords.
- You’re automatically preventing users from setting unsafe passwords.
- The implementation uses the **Have I Been Pwned API**, which is known for its reliability and security-focused approach to password validation.

This is an important step toward providing robust protection for user accounts in your application.

## 004 Deep Dive of UserDetailsService & UserDetailsManager Interfaces

![alt text](image-4.png)

The image provided outlines the key components and relationships involved in **User Management** within the **Spring Security** framework. Let’s break down the concepts deeply, with examples.

---

### **1. UserDetailsService (Interface)**

The **`UserDetailsService`** interface is a core component in Spring Security that is responsible for **loading user-specific data**. Typically, it's used during authentication to retrieve user details (like username, password, and roles) from a data source (like an in-memory store, database, or LDAP).

- **Method**:
  - `loadUserByUsername(String username)`: This method is used to fetch a user’s details by their username. It returns an implementation of the `UserDetails` interface that contains the user’s information.

#### Example:
If you have a user database or in-memory user store, you implement `UserDetailsService` to retrieve user information by username. Here’s a simple example of how this might look in code:

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Example: Fetch the user from a database
        UserDetails user = User.withUsername("user1")
                .password("{bcrypt}$2a$10$WwU.E/h43lB8KdYZIq1/Mu.tdKieLqFneU3OiZ/NE7EoC8sgOq5Wu")
                .roles("USER").build();
        
        return user;
    }
}
```

This interface is a simple mechanism to fetch user details based on a username, which is typically used during authentication.

---

### **2. UserDetailsManager (Interface)**

**`UserDetailsManager`** is an extension of `UserDetailsService`, offering more functionality for **managing users**. It provides methods to create, update, delete, and verify the existence of a user, making it useful in cases where you need full user management capabilities (not just authentication).

- **Methods**:
  - `createUser(UserDetails user)`: Adds a new user to the system.
  - `updateUser(UserDetails user)`: Updates the details of an existing user.
  - `deleteUser(String username)`: Deletes a user by their username.
  - `changePassword(String oldPwd, String newPwd)`: Changes the password for a user.
  - `userExists(String username)`: Checks whether a user exists by their username.

#### Example:
In a typical application, you might want to allow administrators to manage users. You can use `UserDetailsManager` to provide functionality for adding, updating, and deleting users.

```java
@Service
public class CustomUserDetailsManager implements UserDetailsManager {

    private final Map<String, UserDetails> users = new HashMap<>();

    @Override
    public void createUser(UserDetails user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void updateUser(UserDetails user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // This can be customized based on how passwords are stored/verified
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
}
```

### **3. Implementations of `UserDetailsManager`**

Spring Security provides several built-in implementations of `UserDetailsManager`, each tailored to specific use cases or data sources.

#### a. **InMemoryUserDetailsManager**
- This class is used to manage users in-memory (i.e., stored in application memory rather than a persistent data store).
- **Example**: This is commonly used in small applications, testing, or development environments where you don't need a persistent database to store user credentials.

```java
@Bean
public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    UserDetails user = User.withUsername("user")
            .password("{noop}password")
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(user);
}
```

#### b. **JdbcUserDetailsManager**
- This implementation works with a relational database. It allows you to manage users and store their credentials in a database using **JDBC**.
- **Example**: When you want to store user information in a database, you can use this implementation. Spring provides default SQL queries to fetch users and roles, but you can customize them if needed.

```java
@Bean
public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
}
```

#### c. **LdapUserDetailsManager**
- This implementation works with an **LDAP** directory, which is often used in corporate environments for user authentication and authorization.
- **Example**: If your application needs to authenticate users against an LDAP server, this is the implementation to use.

```java
@Bean
public LdapUserDetailsManager ldapUserDetailsManager(LdapContextSource contextSource) {
    return new LdapUserDetailsManager(contextSource);
}
```

### **4. UserDetails (Interface)**

The **`UserDetails`** interface represents the **core user information** required by Spring Security. This is what `UserDetailsService` returns when loading a user by username. The `UserDetails` object contains:
- Username
- Password (usually hashed)
- Authorities/Roles
- Account status (whether the account is locked, expired, etc.)

#### Example:
When you create a user in Spring Security, you're essentially creating a `UserDetails` object, like so:

```java
UserDetails user = User.withUsername("user")
        .password("{bcrypt}$2y$12$...")
        .authorities("ROLE_USER")
        .build();
```

- The password is typically encoded (e.g., using bcrypt).
- The authorities (`ROLE_USER`) determine the permissions this user has.

### **5. How All These Interfaces Work Together**

1. **Authentication**: 
   - When a user tries to log in, Spring Security will call the `loadUserByUsername` method from the `UserDetailsService` or `UserDetailsManager` implementation to fetch the user’s details.

2. **User Management**:
   - For applications that require user management, the `UserDetailsManager` interface allows creating, updating, and deleting users. For example, an admin might use an interface to add new users via `createUser(UserDetails user)`.

3. **Integration with Various Data Sources**:
   - You can choose between `InMemoryUserDetailsManager`, `JdbcUserDetailsManager`, and `LdapUserDetailsManager` based on where you store user information (in-memory, database, or LDAP).

4. **Customization**:
   - These classes and interfaces provide extensibility. If your application requires custom user management (e.g., fetching users from a NoSQL database), you can implement `UserDetailsService` or `UserDetailsManager` to suit your specific needs.

---

### **Example Scenario: Adding a New User**

Let's say you want to allow an admin to add a new user:

```java
public void addUser(String username, String password, String role) {
    UserDetails user = User.withUsername(username)
            .password(passwordEncoder.encode(password))
            .authorities(role)
            .build();
    
    userDetailsManager.createUser(user);
}
```

In this example, you're creating a `UserDetails` object for a new user, encoding their password using the `PasswordEncoder`, and saving the user using the `UserDetailsManager`.

---

### **Summary**

- **`UserDetailsService`** is used for retrieving user details during authentication.
- **`UserDetailsManager`** extends `UserDetailsService` and provides methods for user management (like creating, updating, and deleting users).
- Spring Security provides several implementations of these interfaces, such as `InMemoryUserDetailsManager`, `JdbcUserDetailsManager`, and `LdapUserDetailsManager`.
- `UserDetails` represents the user’s core information (username, password, authorities, etc.).
  
This design ensures that Spring Security can easily adapt to different data sources (in-memory, database, LDAP) and provides flexibility for managing user accounts.

![alt text](image-5.png)

```java
/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.core.userdetails;

/**
 * Core interface which loads user-specific data.
 * <p>
 * It is used throughout the framework as a user DAO and is the strategy used by the
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider
 * DaoAuthenticationProvider}.
 *
 * <p>
 * The interface requires only one read-only method, which simplifies support for new
 * data-access strategies.
 *
 * @author Ben Alex
 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
 * @see UserDetails
 */
public interface UserDetailsService {

	/**
	 * Locates the user based on the username. In the actual implementation, the search
	 * may possibly be case sensitive, or case insensitive depending on how the
	 * implementation instance is configured. In this case, the <code>UserDetails</code>
	 * object that comes back may have a username that is of a different case than what
	 * was actually requested..
	 * @param username the username identifying the user whose data is required.
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws UsernameNotFoundException if the user could not be found or the user has no
	 * GrantedAuthority
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}

```

![alt text](image-7.png)

```java
/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.provisioning;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * An extension of the {@link UserDetailsService} which provides the ability to create new
 * users and update existing ones.
 *
 * @author Luke Taylor
 * @since 2.0
 */
public interface UserDetailsManager extends UserDetailsService {

	/**
	 * Create a new user with the supplied details.
	 */
	void createUser(UserDetails user);

	/**
	 * Update the specified user.
	 */
	void updateUser(UserDetails user);

	/**
	 * Remove the user with the given login name from the system.
	 */
	void deleteUser(String username);

	/**
	 * Modify the current user's password. This should change the user's password in the
	 * persistent user repository (database, LDAP etc).
	 * @param oldPassword current password (for re-authentication if required)
	 * @param newPassword the password to change to
	 */
	void changePassword(String oldPassword, String newPassword);

	/**
	 * Check if a user with the supplied login name exists in the system.
	 */
	boolean userExists(String username);

}

```


![alt text](image-6.png)

```java
/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.provisioning;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.util.Assert;

/**
 * Non-persistent implementation of {@code UserDetailsManager} which is backed by an
 * in-memory map.
 * <p>
 * Mainly intended for testing and demonstration purposes, where a full blown persistent
 * system isn't required.
 *
 * @author Luke Taylor
 * @since 3.1
 */
public class InMemoryUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

	protected final Log logger = LogFactory.getLog(getClass());

	private final Map<String, MutableUserDetails> users = new HashMap<>();

	private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
		.getContextHolderStrategy();

	private AuthenticationManager authenticationManager;

	public InMemoryUserDetailsManager() {
	}

	public InMemoryUserDetailsManager(Collection<UserDetails> users) {
		for (UserDetails user : users) {
			createUser(user);
		}
	}

	public InMemoryUserDetailsManager(UserDetails... users) {
		for (UserDetails user : users) {
			createUser(user);
		}
	}

	public InMemoryUserDetailsManager(Properties users) {
		Enumeration<?> names = users.propertyNames();
		UserAttributeEditor editor = new UserAttributeEditor();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			editor.setAsText(users.getProperty(name));
			UserAttribute attr = (UserAttribute) editor.getValue();
			Assert.notNull(attr,
					() -> "The entry with username '" + name + "' could not be converted to an UserDetails");
			createUser(createUserDetails(name, attr));
		}
	}

	private User createUserDetails(String name, UserAttribute attr) {
		return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
	}

	@Override
	public void createUser(UserDetails user) {
		Assert.isTrue(!userExists(user.getUsername()), "user should not exist");
		this.users.put(user.getUsername().toLowerCase(), new MutableUser(user));
	}

	@Override
	public void deleteUser(String username) {
		this.users.remove(username.toLowerCase());
	}

	@Override
	public void updateUser(UserDetails user) {
		Assert.isTrue(userExists(user.getUsername()), "user should exist");
		this.users.put(user.getUsername().toLowerCase(), new MutableUser(user));
	}

	@Override
	public boolean userExists(String username) {
		return this.users.containsKey(username.toLowerCase());
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
		if (currentUser == null) {
			// This would indicate bad coding somewhere
			throw new AccessDeniedException(
					"Can't change password as no Authentication object found in context " + "for current user.");
		}
		String username = currentUser.getName();
		this.logger.debug(LogMessage.format("Changing password for user '%s'", username));
		// If an authentication manager has been set, re-authenticate the user with the
		// supplied password.
		if (this.authenticationManager != null) {
			this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
			this.authenticationManager
				.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
		}
		else {
			this.logger.debug("No authentication manager set. Password won't be re-checked.");
		}
		MutableUserDetails user = this.users.get(username);
		Assert.state(user != null, "Current user doesn't exist in database.");
		user.setPassword(newPassword);
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		String username = user.getUsername();
		MutableUserDetails mutableUser = this.users.get(username.toLowerCase());
		mutableUser.setPassword(newPassword);
		return mutableUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this.users.get(username.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
	}

	/**
	 * Sets the {@link SecurityContextHolderStrategy} to use. The default action is to use
	 * the {@link SecurityContextHolderStrategy} stored in {@link SecurityContextHolder}.
	 *
	 * @since 5.8
	 */
	public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
		Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
		this.securityContextHolderStrategy = securityContextHolderStrategy;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}

```




## 005 Deep Dive of UserDetails & Authentication interfaces

![alt text](image-8.png)

The image you provided explains the relationship between **UserDetails** and **Authentication** in **Spring Security**, highlighting two main components that store and manage user information. Let's break down the components, the relationship between them, and why they both exist.

---

### **1. UserDetails & User**

- **`UserDetails` (Interface)**: This interface is the central place where user information (like username, password, and roles) is stored in Spring Security.
  
  - **getPassword()**: Returns the password of the user (usually hashed).
  - **getUsername()**: Returns the username of the user (used for login).
  - **getAuthorities()**: Retrieves the roles or authorities (permissions) assigned to the user.
  - **isAccountNonExpired()**, **isAccountNonLocked()**, **isCredentialsNonExpired()**, **isEnabled()**: These methods return the account status flags, checking whether the account is expired, locked, disabled, or if the credentials have expired.

- **`User` (Class)**: This is the default implementation of `UserDetails`. It provides a simple way to create a user object with a username, password, and authorities. Spring offers this class as a utility for quick setup, but in a real-world scenario, you might create your own `UserDetails` implementation that fetches data from a database or another storage system.

#### Example:
```java
UserDetails user = User.withUsername("user")
        .password("{bcrypt}$2a$10...")
        .authorities("ROLE_USER")
        .build();
```

In this example, we create a user with a username, password, and authorities (roles). This user object is often stored in an **in-memory store**, database, or fetched from another source like LDAP.

- **When is UserDetails used?**  
  `UserDetails` is primarily used when loading user information from a storage system. For example, when Spring Security wants to authenticate a user, it calls the `loadUserByUsername()` method in `UserDetailsService`, which returns a `UserDetails` object. This object contains all the necessary user data for authentication and authorization.

---

### **2. Principal & Authentication**

- **`Principal` (Interface)**: The **Principal** is a higher-level interface representing the user’s identity. It contains methods like `getName()` to retrieve the username. The `Principal` is part of the `Authentication` object.

- **`Authentication` (Interface)**: This interface represents the user’s authentication information. It contains the following key methods:
  - **getPrincipal()**: Returns the user object (the `UserDetails` object).
  - **getAuthorities()**: Returns the authorities (roles/permissions) granted to the user.
  - **getCredentials()**: Returns the credentials (such as the password) that were used to authenticate the user.
  - **isAuthenticated()**: Returns `true` or `false` depending on whether the user has been authenticated.
  - **setAuthenticated()**: Allows you to set whether the user is authenticated.
  - **eraseCredentials()**: Allows the credentials to be removed from the `Authentication` object, for security purposes (to avoid storing sensitive information like the password).

- **UsernamePasswordAuthenticationToken (Class)**: This is a commonly used class that implements `Authentication` and is specifically used when authenticating users based on their username and password. It contains all the information (username, password, roles) for the authentication process.

#### Example:
After the user logs in, Spring Security creates an `Authentication` object (often `UsernamePasswordAuthenticationToken`) to store the user’s credentials, authorities, and principal. This object is then stored in the **Security Context** and used to verify if the user is authenticated in subsequent requests.

```java
UsernamePasswordAuthenticationToken authentication = 
    new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
```

- **When is Authentication used?**  
  The `Authentication` object is used to determine whether the user has been authenticated and to store the user’s credentials and authorities. This object is returned by the **AuthenticationProvider** after successfully verifying the user’s identity and is later placed in the **Security Context** to persist the authentication status.

---

### **3. Relationship Between UserDetails and Authentication**

- **UserDetails** is used for **loading** and **storing** user data from storage systems (e.g., databases, LDAP). It is returned by `UserDetailsService` or `UserDetailsManager` when Spring Security needs to authenticate a user or load user details.

- **Authentication** is used for **processing** authentication. It contains information about the user, credentials, and authorities after authentication has occurred. Once a user is authenticated, Spring Security generates an `Authentication` object that holds the **Principal** (which is often a `UserDetails` instance) and the user’s credentials and authorities.

- **Why do we have two separate ways to store login user details?**
  1. **UserDetails** focuses on **loading** user information from storage (like a database or LDAP), whereas **Authentication** deals with **authentication** (whether a user is authenticated, and if so, what authorities they have).
  2. **UserDetails** exists independently of the authentication process. It's simply a container for user data.
  3. **Authentication** is generated dynamically during the authentication process and holds runtime information (like whether the user is authenticated).

---

### **Authentication Flow Overview**

1. **User submits credentials** (e.g., username and password).
2. Spring Security invokes the **`UserDetailsService`** to load the user information from storage (e.g., a database).
3. Spring Security creates a **`UsernamePasswordAuthenticationToken`** with the user’s credentials and roles.
4. The **AuthenticationProvider** verifies the credentials and generates an **`Authentication`** object.
5. This **`Authentication`** object is stored in the **Security Context** to persist the authentication status for future requests.

---

### **Example Scenario**

1. A user submits a form to log in with a username and password.
2. Spring Security calls `UserDetailsService.loadUserByUsername("john")`, which returns a `UserDetails` object containing the user's username, password, and roles.
3. Spring Security creates a `UsernamePasswordAuthenticationToken` with the `UserDetails` object.
4. The `AuthenticationManager` checks if the credentials are valid. If valid, it marks the `Authentication` object as authenticated (`isAuthenticated() = true`).
5. This `Authentication` object is placed in the **Security Context**.
6. Future requests check the **Security Context** to determine whether the user is authenticated and what roles/permissions they have.

---

### **Summary**

- **UserDetails** is responsible for loading user information (e.g., from a database or LDAP).
- **Authentication** represents the user's current authentication status and stores the authenticated user’s data, credentials, and roles.
- Both are essential for managing security in a Spring Security application, with `UserDetails` focused on retrieving user data and `Authentication` focused on verifying and storing the user's authentication state.