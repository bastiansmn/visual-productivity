package com.bastiansmn.vp.mail;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.mail.dto.MailConfirmDTO;
import com.bastiansmn.vp.user.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/mail")
@RequiredArgsConstructor
@Slf4j
public class MailConfirmController {

    private final MailConfirmService mailConfirmService;

    @PutMapping("/confirm")
    public boolean confirm(@RequestBody MailConfirmDTO mailConfirmDTO) throws TechnicalException, FunctionalException {
        return this.mailConfirmService.verifyConfirmation(
                mailConfirmDTO.getUser(),
                mailConfirmDTO.getConfirmationCode()
        );
    }

    @PostMapping("/revalidate")
    public void resendConfirmation(@RequestBody UserDAO user) throws TechnicalException, FunctionalException {
        this.mailConfirmService.createConfirmation(user);
    }

}
