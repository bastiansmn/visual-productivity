package com.bastiansmn.vp.user;

import com.bastiansmn.vp.VpApplication;
import com.bastiansmn.vp.socialAuth.UserProvider;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = VpApplication.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindByEmail() {
        // Given
        UserDAO user = UserDAO.builder()
                .email("myTestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(Set.of())
                .provider(UserProvider.LOCAL)
                .createdDate(new Date())
                .isNotLocked(true)
                .isEnabled(false)
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
    void itShouldExistsByEmail() {
        // Given
        UserDAO user = UserDAO.builder()
                .email("anotherTestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(Set.of())
                .provider(UserProvider.LOCAL)
                .createdDate(new Date())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        this.underTest.save(user);

        // When
        boolean expected = this.underTest.existsByEmail("anotherTestEmail@mail.com");

        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldSayItsEnabledByEmail() {
        // Given
        UserDAO user = UserDAO.builder()
                .email("myNewTestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(Set.of())
                .provider(UserProvider.LOCAL)
                .createdDate(new Date())
                .isNotLocked(true)
                .isEnabled(true)
                .build();
        UserDAO user2 = UserDAO.builder()
                .email("myNew2TestEmail@mail.com")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .roles(Set.of())
                .provider(UserProvider.LOCAL)
                .createdDate(new Date())
                .isNotLocked(true)
                .isEnabled(false)
                .build();
        this.underTest.save(user);
        this.underTest.save(user2);

        // When
        boolean expected = this.underTest.isEnabledByEmail("myNewTestEmail@mail.com");
        boolean expected2 = this.underTest.isEnabledByEmail("myNew2TestEmail@mail.com");

        // Then
        assertThat(expected).isTrue();
        assertThat(expected2).isFalse();
    }

}