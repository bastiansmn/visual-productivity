package com.bastiansmn.vp.role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    @Test
    void findByName() {
        // Given
        RoleDAO roleUser = RoleDAO.builder()
                .name("ROLE_USER")
                .build();

        RoleDAO roleAdmin = RoleDAO.builder()
                .name("ROLE_ADMIN")
                .build();

        this.underTest.save(roleUser);
        this.underTest.save(roleAdmin);

        // When
        RoleDAO userExpected = this.underTest.findByName("ROLE_USER");
        RoleDAO adminExpected = this.underTest.findByName("ROLE_ADMIN");

        // Then
        assertThat(userExpected.getName()).isEqualTo(roleUser.getName());
        assertThat(adminExpected.getName()).isEqualTo(roleAdmin.getName());
    }
}