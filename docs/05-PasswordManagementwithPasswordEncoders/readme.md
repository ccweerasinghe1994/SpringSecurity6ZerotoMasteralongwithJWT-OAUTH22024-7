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

![alt text](image-1.png)

The diagram presents a comparison between **Encoding**, **Encryption**, and **Hashing**—three distinct methods of processing data, each serving a unique purpose in the context of data privacy and security. Below is a deep explanation of each method, along with real-world examples, highlighting when and why these processes are used.

### 1. **Encoding**

- **Definition**: Encoding is the process of converting data from one format to another for the purpose of interoperability, storage, or transmission. It is not designed for security purposes and does not involve cryptographic techniques.

- **Purpose**: Encoding ensures that data is transformed into a format that can be understood by different systems (computers, software, or devices). The key point is that encoding is **reversible**—anyone with the proper decoder can retrieve the original data.

- **How it Works**:
  - A set of rules (or algorithms) is applied to transform the original data into a specific format.
  - Since encoding is not a security measure, the encoded data can be easily converted back to its original form without any special key or secret.

- **Examples**:
  - **ASCII (American Standard Code for Information Interchange)**: Transforms characters into numeric codes. For example, the letter "A" is represented by the number 65.
  - **Base64 Encoding**: Commonly used to encode binary data, such as images, so they can be transmitted over text-based protocols like HTTP. For instance, an image file can be encoded as Base64 to be embedded in HTML or XML.
    - Example: The string `"hello"` becomes `"aGVsbG8="` in Base64 encoding.
  - **UNICODE**: A standardized system to encode text in different languages and symbols. It assigns a unique code point to every character in the world’s writing systems.

- **Reversibility**: Because encoding is meant for data transformation and not security, it is completely reversible. The original data can be retrieved as long as you have the correct decoding method.

- **Use Case**: Encoding is commonly used in data transmission or storage where data needs to be transformed into a format compatible with different systems. For example:
  - Encoding an image in Base64 for transmission over an HTTP request.
  - Encoding URL parameters to ensure they are safely transmitted in a URL (URL encoding).

### 2. **Encryption**

- **Definition**: Encryption is the process of transforming data (plaintext) into an unreadable form (ciphertext) to ensure **confidentiality**. It relies on cryptographic techniques and involves a **key**. The goal is to ensure that only authorized parties with the correct key can decrypt and read the original data.

- **Purpose**: Encryption guarantees that sensitive data remains confidential. Even if someone intercepts the encrypted data, they cannot understand it without the decryption key.

- **How it Works**:
  - Data (plaintext) is encrypted using an encryption algorithm (such as AES, RSA) and a **key** (a secret shared between the sender and receiver or known only to the data owner).
  - The encrypted data (ciphertext) can only be decrypted back into plaintext by someone who possesses the correct decryption key.

- **Examples**:
  - **AES (Advanced Encryption Standard)**: A symmetric encryption algorithm used for securing sensitive data. For example, encrypting the string `"hello"` with AES would produce a ciphertext such as `"8N3yZTxsXGhtDSYz="`.
  - **RSA Encryption**: An asymmetric encryption algorithm that uses a pair of keys (public and private). Data encrypted with a public key can only be decrypted by the corresponding private key.
    - Example: A website might encrypt sensitive data such as credit card numbers using RSA encryption before transmitting it over the internet.

- **Reversibility**: Encryption is **reversible** as long as the decryption key is available. The original data can be restored by decrypting the ciphertext with the appropriate key.

- **Use Case**: Encryption is widely used to protect sensitive data such as:
  - **Banking**: Encrypting transaction data so only authorized users can access it.
  - **Communication**: Encrypting messages sent through messaging apps (e.g., WhatsApp) to ensure only the intended recipient can read them.
  - **Storage**: Encrypting files on a hard drive to protect them in case the device is stolen.

- **Security Warning**: If the key is compromised, the encrypted data is no longer secure.

### 3. **Hashing**

- **Definition**: Hashing is a process that converts data into a fixed-size hash value (digest) using a hashing algorithm. Unlike encoding or encryption, **hashing is not reversible**—you cannot retrieve the original data from the hash value.

- **Purpose**: Hashing is used to ensure **data integrity**. It allows systems to verify that data has not been altered by comparing hash values, without needing to reveal the original data.

- **How it Works**:
  - A hashing algorithm (such as SHA-256, MD5) takes an input (of any length) and produces a fixed-length output (hash).
  - The same input will always produce the same hash, but even a small change in the input will result in a completely different hash.

- **Examples**:
  - **SHA-256 (Secure Hash Algorithm 256-bit)**: Often used in blockchain and digital certificates. It converts any input into a 256-bit hash value.
    - Example: The string `"hello"` might be hashed into something like `"2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"`.
  - **MD5 (Message Digest Algorithm)**: Though it’s now considered insecure, it was commonly used for checksums to verify file integrity.
    - Example: An MD5 hash of a file can be compared before and after transmission to ensure it hasn’t been tampered with.

- **Reversibility**: Hashing is a **one-way** process—once data is hashed, it cannot be reversed to its original form. This makes it ideal for storing sensitive data like passwords. Instead of storing the password, you store the hash, and during login, you hash the entered password and compare it with the stored hash.

- **Use Case**:
  - **Password Storage**: Instead of storing plain-text passwords, systems store hashed passwords. When a user logs in, the entered password is hashed and compared with the stored hash.
    - Example: A user’s password `"password123"` might be hashed and stored as `"ef92b779c0001fc10f2de6058e4340a7"`. When the user logs in, the system hashes the entered password and compares it to this value.
  - **File Integrity Verification**: When downloading large files, the publisher might provide a hash value (checksum). After downloading, you can hash the file and compare it with the provided hash to ensure that the file hasn’t been corrupted.

### Key Differences

- **Reversibility**:
  - **Encoding** is reversible (e.g., decoding a Base64-encoded image).
  - **Encryption** is reversible (if you have the key).
  - **Hashing** is not reversible.

- **Use Case**:
  - **Encoding** is for interoperability, not security. Example: encoding data to be safely transmitted over HTTP.
  - **Encryption** is for confidentiality. Example: encrypting credit card data during online transactions.
  - **Hashing** is for data integrity. Example: ensuring that a downloaded file has not been tampered with by comparing hashes.

### Practical Example: Password Management in Applications

When managing user passwords, systems typically follow this workflow:

1. **Hashing (Security Best Practice)**: Instead of storing a password as plain text, the system hashes the password (e.g., using bcrypt or SHA-256) and stores the hash. When a user logs in, the system hashes the input and compares it with the stored hash.
   - If the hashes match, authentication succeeds.
   
2. **Encryption (Sensitive Data)**: If the application needs to store sensitive data (like user credit card numbers), it encrypts the data using a symmetric or asymmetric algorithm. Only authorized entities can decrypt and access the data using the encryption key.

3. **Encoding (Data Transmission)**: If the data needs to be transmitted over networks, it might be encoded (e.g., Base64 encoding) to ensure it is in a format that can be safely transmitted over protocols like HTTP.

### Conclusion

- **Encoding** is about data transformation for compatibility and transmission.
- **Encryption** ensures confidentiality by converting plaintext into ciphertext using a key.
- **Hashing** ensures integrity by creating a non-reversible fixed-length digest of the original data.

Each process has its own purpose in securing data, and understanding when to use each is critical for maintaining privacy and security in software systems.

![alt text](image-2.png)

The diagram illustrates the process of **Encoding** and **Decoding**, showing how data is transformed from **plain text** into an **encoded format** and then back into **plain text** through decoding. Encoding is used to ensure data is in a suitable format for transmission or storage. Let’s explore this concept in-depth with examples.

### 1. **What is Encoding?**

**Encoding** is the process of converting data from one form to another. It is not designed to be secure but is used for purposes like compatibility, transmission, or storage. The key characteristic of encoding is that it is **reversible**—you can always decode the data back to its original form.

- **Key Characteristics**:
  - **Reversible**: The original data can be retrieved with the appropriate decoding algorithm.
  - **Purpose**: Typically used to ensure that data is safely transmitted or stored without being altered. 
  - **Not for Security**: Encoding is not meant for encryption or securing sensitive data.
  
- **Examples of Encoding**:
  - **ASCII (American Standard Code for Information Interchange)**: Translates characters into numeric codes. For example, the letter "A" is represented by the number `65`.
  - **Base64**: Commonly used to encode binary data, such as images, into a text-based format for transmission over text-based protocols like HTTP. For example, the string `"hello"` would be encoded in Base64 as `"aGVsbG8="`.
  - **URL Encoding**: Used to encode URLs so they can be safely transmitted over the internet. Special characters (such as spaces or punctuation) are replaced by a percent sign followed by two hexadecimal digits. For example, a space is encoded as `%20`.

### 2. **The Encoding Process**

The diagram depicts the following flow:

1. **Plain Text (Original Data)**: 
   - This is the original form of the data before encoding. It could be a string of characters, binary data, or any form of raw data.
   
2. **Encoding**: 
   - This process converts the plain text into an encoded format (or encoded text). The type of encoding algorithm (e.g., Base64, ASCII) determines how the data is transformed. The purpose is to make the data suitable for specific purposes, such as transmission over text-based protocols or ensuring it is stored in a standardized format.

   **Example**:
   - If you have a binary image that needs to be transmitted as part of an HTTP message, encoding it in Base64 makes it readable in text form, like this:
     - Original Image Data: `Binary data`
     - Base64 Encoded: `iVBORw0KGgoAAAANSUhEUgAAAOEAA...`
     
3. **Encoded Text**: 
   - This is the result of the encoding process. It is a transformed representation of the original data but remains in a format that is reversible through decoding.
   
   **Example**: 
   - Base64 encoding of the word `hello` would give you the encoded output: `"aGVsbG8="`.

### 3. **What is Decoding?**

**Decoding** is the reverse process of encoding. It converts the encoded data back into its original form. As long as the same encoding algorithm is used, decoding will perfectly retrieve the original data.

- **Key Characteristics**:
  - **Reversible**: The data is returned to its original form after decoding.
  - **Requirements**: The same algorithm that was used for encoding must be used for decoding.

### 4. **The Decoding Process**

1. **Encoded Text**: 
   - This is the data in its encoded form. It cannot be interpreted in its original meaning until it is decoded back to plain text.

2. **Decoding**:
   - Decoding takes the encoded data and applies the reverse algorithm to return it to its original form. 

   **Example**:
   - The Base64 string `"aGVsbG8="` can be decoded back into the plain text `"hello"`.
   
3. **Plain Text (Original Data)**:
   - This is the output after decoding, which matches the original input before encoding.

### 5. **Real-World Examples**

#### a) **Base64 Encoding in Emails**

When sending attachments in emails, Base64 encoding is used to encode binary files (such as images) into text format. Email protocols like SMTP are text-based, so converting binary files into Base64 ensures that they can be safely transmitted.

- **Original Data**: A binary image file.
- **Encoding**: The image file is encoded into Base64 format.
- **Decoding**: The recipient's email client decodes the Base64 back into the original image so that it can be displayed.

#### b) **URL Encoding**

Web browsers use URL encoding to convert characters in a URL that may otherwise be misinterpreted by web servers. For example, spaces in URLs are encoded as `%20`.

- **Original URL**: `https://example.com/my file.html`
- **Encoded URL**: `https://example.com/my%20file.html`
- **Decoded URL**: A web server decodes the `%20` back to a space to process the URL correctly.

#### c) **Encoding Data for Transmission**

Data that includes special or binary characters may need to be encoded to ensure it can be transmitted safely over networks. For example, JSON Web Tokens (JWT) are encoded in Base64 to ensure they are safely transmitted over HTTP.

- **Original JWT**: `{"alg": "HS256", "typ": "JWT"}`
- **Encoded JWT**: `eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9`
- **Decoded JWT**: The encoded JWT is decoded back into its original form when it's received on the server for validation.

### 6. **Conclusion**

The diagram summarizes the process of **encoding** and **decoding** by showing how plain text (or any raw data) is transformed into an encoded format and then back into its original form. While encoding ensures that data can be transmitted or stored in a suitable format, it does not provide security or confidentiality, unlike encryption or hashing. For secure data storage or transmission, encryption should be used instead of encoding. 

Encoding is purely for compatibility and data integrity, where the goal is to ensure that the data can be safely transmitted, displayed, or interpreted by various systems and devices.
## 003 What is Encryption, Decryption & why it is not suitable for passwords management
## 004 Demo of Encryption, Decryption
## 005 Introduction to Hashing
## 006 Drawbacks of Hashing & what are Brute force attacks, Dictionary or Rainbow tab
## 007 How to overcome Hashing drawbacks, Brute force and Dictionary table attacks
## 008 Introduction to PasswordEncoders in Spring Security
## 009 Deep dive of PasswordEncoder implementation classes
## 010 Demo of registration and login with Bcrypt password encoder
