package com.bastiansmn.vp.mail;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.user.UserDAO;

public interface MailConfirmService {

    void createConfirmation(UserDAO user) throws FunctionalException, TechnicalException;

    boolean verifyConfirmation(UserDAO user, String confirmationCode) throws TechnicalException, FunctionalException;

    void removeExpiredConfirmations();

}
