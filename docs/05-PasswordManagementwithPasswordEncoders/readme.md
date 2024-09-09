# 05 - Password Management with PasswordEncoders

## 001 How our passwords validated with out PasswordEncoders
```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
```
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

package org.springframework.security.crypto.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * Used for creating {@link PasswordEncoder} instances
 *
 * @author Rob Winch
 * @since 5.0
 */
public final class PasswordEncoderFactories {

	private PasswordEncoderFactories() {
	}

	/**
	 * Creates a {@link DelegatingPasswordEncoder} with default mappings. Additional
	 * mappings may be added and the encoding will be updated to conform with best
	 * practices. However, due to the nature of {@link DelegatingPasswordEncoder} the
	 * updates should not impact users. The mappings current are:
	 *
	 * <ul>
	 * <li>bcrypt - {@link BCryptPasswordEncoder} (Also used for encoding)</li>
	 * <li>ldap -
	 * {@link org.springframework.security.crypto.password.LdapShaPasswordEncoder}</li>
	 * <li>MD4 -
	 * {@link org.springframework.security.crypto.password.Md4PasswordEncoder}</li>
	 * <li>MD5 - {@code new MessageDigestPasswordEncoder("MD5")}</li>
	 * <li>noop -
	 * {@link org.springframework.security.crypto.password.NoOpPasswordEncoder}</li>
	 * <li>pbkdf2 - {@link Pbkdf2PasswordEncoder#defaultsForSpringSecurity_v5_5()}</li>
	 * <li>pbkdf2@SpringSecurity_v5_8 -
	 * {@link Pbkdf2PasswordEncoder#defaultsForSpringSecurity_v5_8()}</li>
	 * <li>scrypt - {@link SCryptPasswordEncoder#defaultsForSpringSecurity_v4_1()}</li>
	 * <li>scrypt@SpringSecurity_v5_8 -
	 * {@link SCryptPasswordEncoder#defaultsForSpringSecurity_v5_8()}</li>
	 * <li>SHA-1 - {@code new MessageDigestPasswordEncoder("SHA-1")}</li>
	 * <li>SHA-256 - {@code new MessageDigestPasswordEncoder("SHA-256")}</li>
	 * <li>sha256 -
	 * {@link org.springframework.security.crypto.password.StandardPasswordEncoder}</li>
	 * <li>argon2 - {@link Argon2PasswordEncoder#defaultsForSpringSecurity_v5_2()}</li>
	 * <li>argon2@SpringSecurity_v5_8 -
	 * {@link Argon2PasswordEncoder#defaultsForSpringSecurity_v5_8()}</li>
	 * </ul>
	 * @return the {@link PasswordEncoder} to use
	 */
	@SuppressWarnings("deprecation")
	public static PasswordEncoder createDelegatingPasswordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(encodingId, new BCryptPasswordEncoder());
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
		encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
		encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256",
				new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
		encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		return new DelegatingPasswordEncoder(encodingId, encoders);
	}

}

```
```java
/*
 * Copyright 2011-2016 the original author or authors.
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

package org.springframework.security.crypto.password;

/**
 * Service interface for encoding passwords.
 *
 * The preferred implementation is {@code BCryptPasswordEncoder}.
 *
 * @author Keith Donald
 */
public interface PasswordEncoder {

	/**
	 * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
	 * greater hash combined with an 8-byte or greater randomly generated salt.
	 */
	String encode(CharSequence rawPassword);

	/**
	 * Verify the encoded password obtained from storage matches the submitted raw
	 * password after it too is encoded. Returns true if the passwords match, false if
	 * they do not. The stored password itself is never decoded.
	 * @param rawPassword the raw password to encode and match
	 * @param encodedPassword the encoded password from storage to compare with
	 * @return true if the raw password, after encoding, matches the encoded password from
	 * storage
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);

	/**
	 * Returns true if the encoded password should be encoded again for better security,
	 * else false. The default implementation always returns false.
	 * @param encodedPassword the encoded password to check
	 * @return true if the encoded password should be encoded again for better security,
	 * else false.
	 */
	default boolean upgradeEncoding(String encodedPassword) {
		return false;
	}

}

```

![alt text](image.png)

This diagram explains how password validation works using Spring Security's `NoOpPasswordEncoder`, which does not apply any encryption or hashing to passwords, meaning the passwords are stored and processed in plain text. Let's go through each part of the diagram and explain it in detail with practical examples.

### 1. **Overview of NoOpPasswordEncoder**

`NoOpPasswordEncoder` is a class in Spring Security that performs no encoding or encryption of passwords. It is primarily used in development or testing environments when you want to avoid the complexity of password hashing. However, the use of `NoOpPasswordEncoder` is **not recommended for production** environments because it leaves passwords exposed in plain text, which compromises both **integrity** and **confidentiality**.

#### **Key Risk:**
When passwords are stored in plain text in a database, anyone with access to the database (e.g., an attacker who compromises the system) can see and use these passwords, which can lead to unauthorized access to user accounts.

### 2. **User Credentials Submission**

In the diagram, the user submits the following credentials through a login form:

- **Username**: `admin`
- **Password**: `12345`

These credentials are sent to the server as plain text (assuming no encryption like HTTPS is used, which introduces further security risks).

### 3. **Password Validation Process**

When a user submits their credentials, the following sequence of events occurs:

1. **Credentials Submission**: The user inputs their username and password (`admin` and `12345`), and clicks the "LOGIN" button.
   
2. **Loading User Details from Database**: The application receives the submitted credentials and calls the `loadUserByUsername()` method from the `UserDetailsService` implementation. This method retrieves the user details from the database. The query fetches the following data for the user with the username `admin`:
   - **Username**: `admin`
   - **Password**: `12345` (stored in plain text in the database)
   
3. **Password Comparison (Matching Process)**:
   - After retrieving the user details, Spring Security compares the password entered by the user (`12345`) with the password stored in the database (`12345`).
   - Since `NoOpPasswordEncoder` is being used, no hashing or encryption is performed on either the stored password or the submitted password, meaning the comparison is done directly in plain text.

4. **Decision Point: Does the Password Match?**
   - **Match**: If the submitted password matches the password stored in the database (in this case, `12345` matches `12345`), the user is successfully authenticated and granted access to the system (login success).
   - **No Match**: If the password does not match (for example, if the user entered `123456` instead of `12345`), authentication fails, and the user is denied access (login failure).

### 4. **NoOpPasswordEncoder is Not Recommended for Production**

At the bottom of the diagram, it clearly states that using `NoOpPasswordEncoder` is **not recommended for production**. Here’s why:

#### a) **Confidentiality Issues**:
- **Plain-text Passwords**: If passwords are stored in plain text in the database, they can be easily read by anyone with access to the database. This includes database administrators, attackers who gain unauthorized access, or even backups where passwords are not properly encrypted.
- **No Encryption**: Unlike more secure password encoders (e.g., `BCryptPasswordEncoder`, `PBKDF2PasswordEncoder`), `NoOpPasswordEncoder` does not perform any encryption or hashing. This means that even during transmission, the password is exposed unless the connection is encrypted (e.g., via HTTPS).

#### b) **Integrity Issues**:
- **Database Compromise**: If an attacker gains access to the database, they can easily read and use all passwords because they are stored in an easily accessible format (plain text).
- **Reused Passwords**: Many users tend to reuse passwords across different platforms. If your database is compromised, attackers can use the exposed plain-text passwords to gain access to other services where users have reused the same password.

### 5. **Secure Alternatives to NoOpPasswordEncoder**

In a production environment, it’s crucial to use password encoding to protect sensitive user data. Instead of `NoOpPasswordEncoder`, secure alternatives should be used, such as:

#### a) **BCryptPasswordEncoder**
This is one of the most commonly used password encoders in Spring Security. It hashes the password using the BCrypt hashing function, which includes a built-in salt and is resistant to rainbow table attacks.

**Example**:
- When the user registers with the password `12345`, `BCryptPasswordEncoder` hashes the password into a string like this:
  ```
  $2a$10$0VVyXaMOX7FdMhFVyxLfEu.Vex8pXjGlwQmZzmU9uSeOE5XfXoUdS
  ```
- The hash is stored in the database. When the user logs in, the submitted password is hashed again, and the hashes are compared.

**Code Example**:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

#### b) **PBKDF2PasswordEncoder**
This is another secure password encoder that uses the PBKDF2 (Password-Based Key Derivation Function 2) algorithm, which applies a hash function multiple times to create a secure password hash.

**Code Example**:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
}
```

### 6. **How it Works with a Secure Encoder (Example)**

Let’s see how the process differs when a secure password encoder (e.g., `BCryptPasswordEncoder`) is used:

#### a) **User Registration (Secure Password Storage)**
- When the user registers with the password `12345`, Spring Security will use `BCryptPasswordEncoder` to hash the password before storing it in the database.
- The stored password will look something like this: `$2a$10$0VVyXaMOX7FdMhFVyxLfEu.Vex8pXjGlwQmZzmU9uSeOE5XfXoUdS`.

#### b) **Login Process (Secure Password Validation)**
1. **Credentials Submission**: The user submits their credentials (`admin`, `12345`).
2. **Password Retrieval**: The system retrieves the hashed password from the database.
3. **Password Comparison**: Instead of comparing plain-text passwords, Spring Security hashes the submitted password (`12345`) using the same algorithm (`BCryptPasswordEncoder`) and compares it with the stored hash.
4. **Result**:
   - If the hashes match, the user is authenticated.
   - If the hashes do not match, the user is denied access.

### 7. **Conclusion: NoOpPasswordEncoder Use Case**

`NoOpPasswordEncoder` is typically used in **development or testing environments** where:
- Password security is not the primary concern.
- You want to avoid the complexity of hashing while quickly testing functionality.
  
However, in a **production environment**, using a secure password encoding mechanism like `BCryptPasswordEncoder` or `PBKDF2PasswordEncoder` is essential to ensure the integrity and confidentiality of user passwords.

By using `NoOpPasswordEncoder`, you expose sensitive data (passwords) to a variety of security risks, and this practice is explicitly discouraged in real-world applications.

## 002 What is Encoding, Decoding & why it is not suitable for passwords management
## 003 What is Encryption, Decryption & why it is not suitable for passwords management
## 004 Demo of Encryption, Decryption
## 005 Introduction to Hashing
## 006 Drawbacks of Hashing & what are Brute force attacks, Dictionary or Rainbow tab
## 007 How to overcome Hashing drawbacks, Brute force and Dictionary table attacks
## 008 Introduction to PasswordEncoders in Spring Security
## 009 Deep dive of PasswordEncoder implementation classes
## 010 Demo of registration and login with Bcrypt password encoder
