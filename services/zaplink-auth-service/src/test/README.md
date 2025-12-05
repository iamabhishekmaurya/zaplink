# Auth Service Test Suite

This directory contains comprehensive test cases for the Zaplink Auth Service using JUnit 5 and Mockito.

## Test Structure

### Service Layer Tests
- **AuthServiceImplTest**: Tests authentication, token management, and password reset functionality
- **RegistrationServiceImplTest**: Tests user registration, email verification, and account management
- **UserServiceImplTest**: Tests user CRUD operations and validation

### Security Layer Tests
- **CustomUserDetailsServiceTest**: Tests Spring Security user details loading
- **AuthControllerTest**: Tests REST API endpoints with validation
- **UserHelperTest**: Tests utility functions for user operations

### Test Configuration
- **TestConfig**: Provides test-specific bean configurations
- **Test Suites**: Groups related tests for batch execution

## Running Tests

### Individual Test Classes
```bash
# Run specific test class
./gradlew test --tests "io.zaplink.auth.service.impl.AuthServiceImplTest"

# Run all tests in a package
./gradlew test --tests "io.zaplink.auth.service.impl.*"
```

### Test Suites
```bash
# Run all service tests
./gradlew test --tests "io.zaplink.auth.service.AuthServiceTestSuite"

# Run all security tests
./gradlew test --tests "io.zaplink.auth.security.SecurityTestSuite"
```

### All Tests
```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport
```

## Test Coverage

### Authentication Service
- ✅ User login with valid/invalid credentials
- ✅ Token refresh and rotation
- ✅ Logout functionality
- ✅ Password reset request and completion
- ✅ Error handling for various scenarios

### Registration Service
- ✅ User registration with validation
- ✅ Email verification
- ✅ Resend verification email
- ✅ Duplicate user handling
- ✅ Account status management

### User Service
- ✅ User CRUD operations
- ✅ Email/username uniqueness checks
- ✅ User activation/deactivation
- ✅ Role assignment
- ✅ Password encoding

### Security Components
- ✅ User details loading
- ✅ Account status validation
- ✅ Authority assignment
- ✅ JWT token generation

### REST Controllers
- ✅ Endpoint validation
- ✅ Request/response handling
- ✅ Error response formatting
- ✅ HTTP status codes

### Utility Classes
- ✅ User lookup operations
- ✅ User updates
- ✅ Validation helpers
- ✅ Exception handling

## Test Best Practices

1. **Mocking**: Use Mockito to mock external dependencies
2. **Assertions**: Use JUnit 5 assertions for clear test validation
3. **Test Naming**: Use descriptive test method names
4. **Setup/Teardown**: Use @BeforeEach for test setup
5. **Exception Testing**: Test both happy path and error scenarios
6. **Edge Cases**: Test null values, empty strings, and boundary conditions

## Test Data

Test data is created in @BeforeEach methods to ensure test isolation:
- `testUser`: Standard user entity for testing
- `registrationRequest`: User registration request object
- `loginRequest`: Login credentials object
- `passwordResetRequest`: Password reset request object

## Mocking Strategy

- **Repository Layer**: Mocked to avoid database dependencies
- **External Services**: Mocked to isolate unit under test
- **Utility Classes**: Mocked when they contain complex logic
- **Configuration**: Test-specific beans provided via TestConfig

## Continuous Integration

These tests are designed to run in CI/CD pipelines:
- Fast execution with minimal external dependencies
- Clear failure messages for debugging
- Comprehensive coverage of business logic
- Isolated test execution
