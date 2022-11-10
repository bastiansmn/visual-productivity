package com.bastiansmn.vp.mail;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.mail.dto.MailConfirmDTO;
import com.bastiansmn.vp.user.UserDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@Slf4j
public class MailConfirmController {

    private final MailConfirmService mailConfirmService;

    @Operation(summary = "Confirme le mail d'un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mail confirmé"),
        @ApiResponse(responseCode = "404", description = "Confirmation de mail non trouvée"),
        @ApiResponse(responseCode = "404", description = "Mauvaise confirmation de mail"),
        @ApiResponse(responseCode = "410", description = "Confirmation de mail expirée"),
    })
    @PutMapping("/confirm")
    public boolean confirm(@RequestBody MailConfirmDTO mailConfirmDTO) throws TechnicalException, FunctionalException {
        return this.mailConfirmService.verifyConfirmation(
                mailConfirmDTO.getUser(),
                mailConfirmDTO.getConfirmationCode()
        );
    }

    @Operation(summary = "Renvoie une nouvelle confirmation de mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mail envoyé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Mail déjà confirmé"),
            @ApiResponse(responseCode = "503", description = "Erreur technique (service mail down)")
    })
    @PostMapping("/revalidate")
    public void resendConfirmation(@RequestBody UserDAO user) throws TechnicalException, FunctionalException {
        this.mailConfirmService.createConfirmation(user);
    }

}
