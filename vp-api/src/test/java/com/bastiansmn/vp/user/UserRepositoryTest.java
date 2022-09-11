package com.bastiansmn.vp.user;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByEmail() {
        // Given
        UserDAO user = UserDAO.builder()
                .email("myTestEmail@mail.com")
                .username("username")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(List.of())
                .authorities(List.of())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        this.underTest.save(user);

        // When
        Optional<UserDAO> testedUser = this.underTest.findByEmail("myTestEmail@mail.com");

        // Then
        assertThat(testedUser)
                .isNotEmpty()
                .is(new Condition<>((u) -> {
                    if (u.isEmpty()) return false;
                    return testedUser.get().getEmail().equals(user.getEmail());
                }, "Test if the method find the user we actually saved before"));
    }

    @Test
    void itShouldFindByUsername() {
        // Given
        UserDAO user = UserDAO.builder()
                .email("myTestEmail@mail.com")
                .username("username")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(List.of())
                .authorities(List.of())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        this.underTest.save(user);

        // When
        Optional<UserDAO> testedUser = this.underTest.findByUsername("username");

        // Then
        assertThat(testedUser)
                .isNotEmpty()
                .is(new Condition<>((u) -> {
                    if (u.isEmpty()) return false;
                    return testedUser.get().getUsername().equals(user.getUsername());
                }, "Test if the method find the user we actually saved before"));
    }

    @Test
    void itShouldExistsByEmail() {
        // Given
        UserDAO user = UserDAO.builder()
                .username("username")
                .email("myTestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(List.of())
                .authorities(List.of())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        this.underTest.save(user);

        // When
        boolean expected = this.underTest.existsByEmail("myTestEmail@mail.com");

        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldExistsByUsername() {
        // Given
        UserDAO user = UserDAO.builder()
                .username("username")
                .email("myTestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(List.of())
                .authorities(List.of())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        this.underTest.save(user);

        // When
        boolean expected = this.underTest.existsByUsername("username");

        // Then
        assertThat(expected).isTrue();
    }
}