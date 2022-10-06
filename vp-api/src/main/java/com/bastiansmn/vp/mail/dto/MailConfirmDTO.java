package com.bastiansmn.vp.mail.dto;

import com.bastiansmn.vp.user.UserDAO;
import lombok.Getter;

@Getter
public class MailConfirmDTO {

    private UserDAO user;

    private String confirmationCode;

}
