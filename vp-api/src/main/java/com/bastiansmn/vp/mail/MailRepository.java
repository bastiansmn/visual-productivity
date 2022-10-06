package com.bastiansmn.vp.mail;

import com.bastiansmn.vp.user.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<MailConfirmDAO, Long> {

    Optional<MailConfirmDAO> findFirstByConcernedUserOrderByExpirationDateDesc(UserDAO user);

    void deleteAllByExpirationDateBefore(Date date);
}
