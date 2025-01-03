package com.bastiansmn.vp.mail.impl;

import com.bastiansmn.vp.config.MailConstants;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.exception.TechnicalRule;
import com.bastiansmn.vp.mail.MailConfirmDAO;
import com.bastiansmn.vp.mail.MailConfirmService;
import com.bastiansmn.vp.mail.MailRepository;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MailConfirmConfirmServiceImpl implements MailConfirmService {
    private final MailRepository mailRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void createConfirmation(UserDAO user) throws TechnicalException, FunctionalException {
        // Remove expired confirmations (if user ask for confirmation but never confirm it for example)
        this.removeExpiredConfirmations();
        if (!this.userRepository.existsById(user.getUser_id()))
            throw new FunctionalException(
                    FunctionalRule.USER_0004,
                    HttpStatus.NOT_FOUND
            );

        if (this.userRepository.isEnabledByEmail(user.getEmail()))
            throw new FunctionalException(
                    FunctionalRule.MAIL_0001,
                    HttpStatus.BAD_REQUEST
            );

        // Generate 8 digit code (digits between 1 and 9)
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < MailConstants.CONFIRM_CODE_LENGTH; i++)
            res.append(new Random().nextInt(9) + 1);

        MailConfirmDAO mailConfirm = MailConfirmDAO.builder()
                .concernedUser(user)
                .attempts(0)
                .expirationDate(
                    new Date(System.currentTimeMillis() + MailConstants.EXPIRATION_TIME)
                )
                .confirmationCode(String.valueOf(res))
                .build();

        this.mailRepository.save(mailConfirm);

        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(profile.equals("dev") ? "bastian.somon@gmail.com" : user.getEmail());
            helper.setSubject("Visual Productivity - Activation");

            Context context = new Context();
            // Get formatted date (hh:mm:ss) from expiration date
            String formattedDate = mailConfirm.getExpirationDate().toInstant()
                    .atZone(ZoneId.of("Europe/Paris"))
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            context.setVariables(
                Map.of(
                    "firstName", user.getName(),
                    "code", mailConfirm.getConfirmationCode(),
                    "expirationDate", formattedDate
                )
            );
            helper.setText(templateEngine.process("emailConfirmation", context), true);
            this.mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Impossible d'envoyer le mail d'activation pour email: {}", user.getEmail());
            throw new TechnicalException(
                    TechnicalRule.MAIL_0001,
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    @Override
    public boolean verifyConfirmation(UserDAO user, String confirmationCode)
            throws TechnicalException, FunctionalException {
        Optional<MailConfirmDAO> mailConfirm = this.mailRepository.findFirstByConcernedUserOrderByExpirationDateDesc(user);

        if (mailConfirm.isEmpty())
            throw new TechnicalException(
                    TechnicalRule.MAIL_0002,
                    HttpStatus.NOT_FOUND
            );

        if (!mailConfirm.get().getConfirmationCode().equals(confirmationCode))
            throw new FunctionalException(
                    FunctionalRule.MAIL_0002,
                    HttpStatus.BAD_REQUEST
            );

        if (mailConfirm.get().getExpirationDate().before(new Date())) {
            this.mailRepository.delete(mailConfirm.get());
            throw new FunctionalException(
                    FunctionalRule.MAIL_0003,
                    HttpStatus.GONE
            );
        }

        this.mailRepository.deleteAllByConcernedUser(user);
        Optional<UserDAO> updatedUser = this.userRepository.findById(user.getUser_id());
        if (updatedUser.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0004,
                    HttpStatus.NOT_FOUND
            );
        updatedUser.get().setEnabled(Boolean.TRUE);
        this.userRepository.save(updatedUser.get());

        return true;
    }

    @Override
    public void removeExpiredConfirmations() {
        this.mailRepository.deleteAllByExpirationDateBefore(new Date());
    }
}
