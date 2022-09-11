package com.bastiansmn.vp.authorities;

import com.bastiansmn.vp.role.RoleDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthoritiesRepositoryTest {

    @Autowired
    private AuthoritiesRepository underTest;

    @Test
    void findByName() {
        // Given
        AuthoritiesDAO createAuth = AuthoritiesDAO.builder()
                .name("create")
                .build();

        AuthoritiesDAO readAuth = AuthoritiesDAO.builder()
                .name("read")
                .build();

        AuthoritiesDAO updateAuth = AuthoritiesDAO.builder()
                .name("update")
                .build();

        AuthoritiesDAO deleteAuth = AuthoritiesDAO.builder()
                .name("delete")
                .build();

        this.underTest.save(createAuth);
        this.underTest.save(readAuth);
        this.underTest.save(updateAuth);
        this.underTest.save(deleteAuth);

        // When
        AuthoritiesDAO expectedCreate = this.underTest.findByName("create");
        AuthoritiesDAO expectedRead = this.underTest.findByName("read");
        AuthoritiesDAO expectedUpdate = this.underTest.findByName("update");
        AuthoritiesDAO expectedDelete = this.underTest.findByName("delete");

        // Then
        assertThat(expectedCreate.getName()).isEqualTo(createAuth.getName());
        assertThat(expectedRead.getName()).isEqualTo(readAuth.getName());
        assertThat(expectedUpdate.getName()).isEqualTo(updateAuth.getName());
        assertThat(expectedDelete.getName()).isEqualTo(deleteAuth.getName());
    }
}