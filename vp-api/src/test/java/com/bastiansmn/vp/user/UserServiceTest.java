package com.bastiansmn.vp.user;

import com.bastiansmn.vp.authorities.AuthoritiesService;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import com.bastiansmn.vp.user.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private AuthoritiesService authoritiesService;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new UserServiceImpl(
                this.userRepository,
                this.roleService,
                this.authoritiesService,
                new BCryptPasswordEncoder()
        );
    }

    @Test
    void canCreateStudent() throws FunctionalException {
        // Given
        UserCreationDTO user = UserCreationDTO.builder()
                .email("john.doe@mail.com")
                .username("johndoe")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        // When
        UserDAO createdUser = this.underTest.create(user);

        // Then
        ArgumentCaptor<UserDAO> userDAOArgumentCaptor = ArgumentCaptor.forClass(UserDAO.class);

        verify(this.userRepository)
                .save(userDAOArgumentCaptor.capture());

        UserDAO capturedUser = userDAOArgumentCaptor.getValue();

        assertThat(capturedUser)
                .isEqualTo(createdUser);
    }

    @Test
    void willThrowEmailTaken() {
        // Given
        UserCreationDTO user = UserCreationDTO.builder()
                .email("john.doe@mail.com")
                .username("johndoe")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        // When

        // Then
        assertThatThrownBy(() -> this.underTest.create(user))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0001 - un utilisateur avec cet email existe déjà");
    }

    @Test
    void willThrowUsernameTaken() {
        // Given
        UserCreationDTO user = UserCreationDTO.builder()
                .email("doe.john@mail.com")
                .username("johndoe")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        // When

        // Then
        assertThatThrownBy(() -> this.underTest.create(user))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0002 - un utilisateur avec ce nom d'utilisateur existe déjà");
    }

    @Test
    void willThrowEmailNotValid() {
        // Given
        UserCreationDTO userTest1 = UserCreationDTO.builder()
                .email("doe.johnmail.com")
                .username("doejohn1")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        UserCreationDTO userTest2 = UserCreationDTO.builder()
                .email("doe.john@mail")
                .username("doejohn2")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        UserCreationDTO userTest3 = UserCreationDTO.builder()
                .email("doe.johnmail@mail.")
                .username("doejohn3")
                .name("Doe")
                .lastname("John")
                .password("notEncryptedPassword")
                .build();

        // When

        // Then
        assertThatThrownBy(() -> this.underTest.create(userTest1))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0003 - l'email n'est pas valide");

        assertThatThrownBy(() -> this.underTest.create(userTest2))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0003 - l'email n'est pas valide");

        assertThatThrownBy(() -> this.underTest.create(userTest3))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0003 - l'email n'est pas valide");
    }

    @Test
    void willThrowPasswordNotValid() {
        // Given
        UserCreationDTO user = UserCreationDTO.builder()
                .email("doe.john@mail.com")
                .username("doejohn1")
                .name("Doe")
                .lastname("John")
                .password("badpass")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> this.underTest.create(user))
                .isInstanceOf(FunctionalException.class)
                .hasMessage("USER_0004 - le mot de passe n'est pas valide");
    }

    @Test
    @Disabled
    void canFetchById() {

    }

    @Test
    @Disabled
    void canUpdate() {
    }

    @Test
    @Disabled
    void canDelete() {
    }

    @Test
    void canFetchAll() {
        // When
        this.underTest.fetchAll();

        verify(this.userRepository).findAll();
    }

    @Test
    @Disabled
    void doesEmailExists() {
    }

    @Test
    @Disabled
    void doesUsernameExists()     {
    }

}