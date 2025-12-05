package io.zaplink.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for ZaplinkAuthServiceApplication.
 * Tests the main application entry point and Spring Boot startup.
 */
@DisplayName("ZaplinkAuthServiceApplication Tests")
class ZaplinkAuthServiceApplicationTest {

    @Test @DisplayName("Application class should be instantiable")
    void applicationClass_ShouldBeInstantiable() {
        // When
        ZaplinkAuthServiceApplication application = new ZaplinkAuthServiceApplication();
        
        // Then
        assertNotNull(application);
    }

    @Test @DisplayName("SpringBootApplication annotation should be present")
    void springBootApplicationAnnotation_ShouldBePresent() {
        // When
        Class<?> clazz = ZaplinkAuthServiceApplication.class;
        
        // Then
        assertNotNull(clazz.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test @DisplayName("Application should have correct package structure")
    void application_ShouldHaveCorrectPackageStructure() {
        // When
        String packageName = ZaplinkAuthServiceApplication.class.getPackage().getName();
        
        // Then
        assertEquals("io.zaplink.auth", packageName);
    }
}
