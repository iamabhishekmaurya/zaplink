package io.zaplink.auth.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for Role entity.
 * Tests all JPA annotations, builder patterns, and validation constraints.
 */
@DisplayName("Role Entity Tests")
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("USER")
                .description("Regular user role")
                .build();
    }

    @Test @DisplayName("Default constructor should create empty role")
    void defaultConstructor_ShouldCreateEmptyRole() {
        // When
        Role emptyRole = new Role();
        
        // Then
        assertNotNull(emptyRole);
    }

    @Test @DisplayName("AllArgsConstructor should create role with all fields")
    void allArgsConstructor_ShouldCreateRoleWithAllFields() {
        // Given
        Long id = 2L;
        String name = "ADMIN";
        String description = "Administrator role";
        
        // When
        Role fullRole = new Role(id, name, description);
        
        // Then
        assertEquals(id, fullRole.getId());
        assertEquals(name, fullRole.getName());
        assertEquals(description, fullRole.getDescription());
    }

    @Test @DisplayName("Builder pattern should create role with specified fields")
    void builderPattern_ShouldCreateRoleWithSpecifiedFields() {
        // Given
        Long expectedId = 3L;
        String expectedName = "MANAGER";
        String expectedDescription = "Manager role";
        
        // When
        Role builtRole = Role.builder()
                .id(expectedId)
                .name(expectedName)
                .description(expectedDescription)
                .build();
        
        // Then
        assertEquals(expectedId, builtRole.getId());
        assertEquals(expectedName, builtRole.getName());
        assertEquals(expectedDescription, builtRole.getDescription());
    }

    @Test @DisplayName("Builder pattern with partial fields should work")
    void builderPattern_WithPartialFields_ShouldWork() {
        // When
        Role partialRole = Role.builder()
                .name("MODERATOR")
                .build();
        
        // Then
        assertEquals("MODERATOR", partialRole.getName());
        assertEquals(null, partialRole.getId());
        assertEquals(null, partialRole.getDescription());
    }

    @Test @DisplayName("Setters and getters should work correctly")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Role mutableRole = new Role();
        
        // When
        mutableRole.setId(5L);
        mutableRole.setName("SUPER_ADMIN");
        mutableRole.setDescription("Super administrator role");
        
        // Then
        assertEquals(5L, mutableRole.getId());
        assertEquals("SUPER_ADMIN", mutableRole.getName());
        assertEquals("Super administrator role", mutableRole.getDescription());
    }

    @Test @DisplayName("Equals and hashCode should work correctly")
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        Role role1 = Role.builder()
                .id(1L)
                .name("USER")
                .description("User role")
                .build();
        
        Role role2 = Role.builder()
                .id(1L)
                .name("USER")
                .description("User role")
                .build();
        
        Role role3 = Role.builder()
                .id(2L)
                .name("ADMIN")
                .description("Admin role")
                .build();
        
        // Then
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
        assertTrue(role1.equals(role2));
        assertTrue(!role1.equals(role3));
    }

    @Test @DisplayName("ToString should include all fields")
    void toString_ShouldIncludeAllFields() {
        // When
        String roleString = role.toString();
        
        // Then
        assertNotNull(roleString);
        assertTrue(roleString.contains("Role"));
        assertTrue(roleString.contains("id=1"));
        assertTrue(roleString.contains("name=USER"));
        assertTrue(roleString.contains("description=Regular user role"));
    }

    @Test @DisplayName("Entity should handle null values gracefully")
    void entity_ShouldHandleNullValuesGracefully() {
        // Given
        Role roleWithNulls = new Role();
        
        // When
        roleWithNulls.setId(null);
        roleWithNulls.setName(null);
        roleWithNulls.setDescription(null);
        
        // Then
        assertEquals(null, roleWithNulls.getId());
        assertEquals(null, roleWithNulls.getName());
        assertEquals(null, roleWithNulls.getDescription());
    }

    @Test @DisplayName("Builder should allow chaining")
    void builder_ShouldAllowChaining() {
        // When
        Role chainedRole = Role.builder()
                .id(10L)
                .name("TEST_ROLE")
                .description("Test role description")
                .build();
        
        // Then
        assertEquals(10L, chainedRole.getId());
        assertEquals("TEST_ROLE", chainedRole.getName());
        assertEquals("Test role description", chainedRole.getDescription());
    }
}
