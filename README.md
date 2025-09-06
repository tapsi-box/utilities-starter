# Tapsi Utilities Starter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.23-blue.svg)](https://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/box.tapsi.libs/utilities-starter)](https://search.maven.org/artifact/box.tapsi.libs/utilities-starter)

Common Infrastructure Utilities for Spring Boot Applications

## Overview

Tapsi Utilities Starter is a comprehensive Kotlin library that provides essential utilities and extensions for Spring Boot applications. It offers a collection of commonly used functions, security services, reactive programming utilities, and formatting tools to accelerate development and maintain consistency across projects.

## Features

### 🔧 Common Extensions
- **Type casting utilities** with safe casting operations
- **Collection extensions** for enhanced data manipulation
- **String utilities** including Persian/English number conversion
- **Method reflection utilities** for reactive type detection

### 🔐 Security Utilities
- **Encryption/Decryption services** with salt support
- **Hash services** for secure password handling
- **JWT token utilities** for authentication
- **Security properties** for configuration management

### ⚡ Reactive Programming
- **Reactor extensions** for Mono and Flux operations
- **Context utilities** for reactive context management
- **CompletableFuture integration** with reactive streams
- **Tuple extensions** for reactive data handling

### ⏰ Time Management
- **Time operations** for date/time manipulation
- **Time value objects** for type-safe time handling
- **Time extensions** for common time operations

### 🎨 Formatting & Generation
- **Random code generators** for unique identifiers
- **Format utilities** for data presentation
- **Custom formatters** for specialized formatting needs

### 🚀 Spring Boot Integration
- **Auto-configuration** for seamless integration
- **Spring context utilities** for dependency injection
- **Bean management** utilities

## Installation

### Maven

```xml
<dependency>
    <groupId>box.tapsi.libs</groupId>
    <artifactId>utilities-starter</artifactId>
    <version>0.9.1</version>
</dependency>
```

## Quick Start

### 1. Add Dependency

Include the library in your Spring Boot project's dependencies.

### 2. Auto-Configuration

The library automatically configures necessary beans when included in your classpath.

### 3. Use Utilities

```kotlin
import box.tapsi.libs.utilities.*

// Common extensions
val castedValue: String = anyObject.castOrThrow<String>()
val persianNumbers = "12345".toPersianNumber() // "۱۲۳۴۵"

// Security services
@Autowired
lateinit var encryptionService: EncryptionService

val encrypted = encryptionService.encrypt("sensitive data", "salt")
val decrypted = encryptionService.decrypt(encrypted, "salt")

// Random code generation
@Autowired
lateinit var codeGenerator: CodeGenerator

val uniqueCode = codeGenerator.generate(8, "ORDER") // "ORDER-12345678"

// Time operations
@Autowired
lateinit var timeOperator: TimeOperator

val now = timeOperator.now()
val formattedDate = timeOperator.format(now, "yyyy-MM-dd")
```

## Usage Examples

### Common Extensions

```kotlin
// Safe type casting
val result: String = someObject.castOrThrow<String>()

// Collection utilities
val items = listOf("a", "b", "a", "c")
val distinctLast = items.distinctLastBy { it } // ["b", "a", "c"]

// Number conversion
val persian = "123".toPersianNumber() // "۱۲۳"
val english = "۱۲۳".toEnglishNumber() // "123"

// Method reflection
val method = MyClass::class.java.getMethod("getData")
val isReactive = method.isReturningPublisher() // true if returns Mono/Flux
```

### Security Services

```kotlin
@Service
class UserService(
    private val encryptionService: EncryptionService,
    private val hashService: HashService
) {
    
    fun createUser(password: String): User {
        val salt = encryptionService.generateSalt()
        val hashedPassword = hashService.hash(password, salt)
        
        return User(
            passwordHash = hashedPassword,
            salt = salt
        )
    }
    
    fun verifyPassword(password: String, user: User): Boolean {
        return hashService.verify(password, user.passwordHash, user.salt)
    }
}
```

### Reactive Extensions

```kotlin
// Mono extensions
Mono.just("data")
    .withContext("key", "value")
    .subscribe()

// Context utilities
Mono.deferContextual { ctx ->
    val userId = ctx.get<String>("userId")
    getUserById(userId)
}
```

### Time Operations

```kotlin
@Service
class SchedulingService(
    private val timeOperator: TimeOperator
) {
    
    fun scheduleTask() {
        val now = timeOperator.now()
        val tomorrow = timeOperator.addDays(now, 1)
        val formatted = timeOperator.format(tomorrow, "yyyy-MM-dd HH:mm:ss")
        
        // Schedule logic here
    }
}
```

## Configuration

### Security Properties

```yaml
tapsi:
  security:
    crypto:
      algorithm: AES
      key-size: 256
    hash:
      algorithm: SHA-256
      iterations: 10000
```

### Auto-Configuration

The library provides Spring Boot auto-configuration. You can customize behavior by:

1. **Excluding auto-configuration classes** if needed
2. **Overriding bean definitions** in your configuration
3. **Using conditional beans** for specific scenarios

## Testing

The library includes comprehensive test coverage and provides test utilities:

```kotlin
@SpringBootTest
class MyServiceTest {
    
    @Test
    fun `should encrypt and decrypt data`() {
        val encryptionService = EncryptionServiceImpl()
        val original = "test data"
        val salt = encryptionService.generateSalt()
        
        val encrypted = encryptionService.encrypt(original, salt)
        val decrypted = encryptionService.decrypt(encrypted, salt)
        
        assertEquals(original, decrypted)
    }
}
```

## Contributing

We welcome contributions! Please see our contributing guidelines:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

### Development Setup

```bash
# Clone the repository
git clone https://github.com/tapsi-box/utilities-starter.git

# Navigate to project directory
cd utilities-starter

# Build the project
./gradlew build

# Run tests
./gradlew test

# Run code quality checks
./gradlew detekt
./gradlew spotlessCheck
```

## Code Quality

This project maintains high code quality standards:

- **Kotlin** with strict compiler options
- **Detekt** for static code analysis
- **Spotless** for code formatting
- **Comprehensive testing** with JUnit 5
- **Spring Boot Test** integration

## Dependencies

### Core Dependencies
- **Spring Framework 6.2.10** - Core Spring functionality
- **Spring Boot 3.5.5** - Auto-configuration support
- **Reactor Core 3.7.9** - Reactive programming support
- **Spring Security Crypto 6.5.3** - Security utilities
- **JJWT 0.12.5** - JWT handling
- **Jackson 2.17.0** - JSON processing
- **Micrometer 1.15.3** - Metrics support

### Test Dependencies
- **JUnit 5** - Testing framework
- **Spring Boot Test** - Integration testing
- **Reactor Test** - Reactive testing utilities
- **Mockito Kotlin** - Mocking framework

## Version Compatibility

| Library Version | Spring Boot | Kotlin | Java |
|----------------|-------------|---------|------|
| 0.0.9          | 3.5.x       | 1.9.23  | 17+  |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/tapsi-box/utilities-starter/issues)
- **Discussions**: [GitHub Discussions](https://github.com/tapsi-box/utilities-starter/discussions)
- **Documentation**: [Project Wiki](https://github.com/tapsi-box/utilities-starter/wiki)

## Authors

- **Mahdi Bohloul** - [@mahdibohloul](https://github.com/mahdibohloul/)

## Acknowledgments

- Spring Boot team for the excellent framework
- Kotlin team for the amazing language
- Reactor team for reactive programming support
- All contributors and users of this library

---

**Made with ❤️ by the Tapsi team**
