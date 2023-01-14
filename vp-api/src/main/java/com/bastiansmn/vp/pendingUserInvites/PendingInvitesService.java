package com.bastiansmn.vp.pendingUserInvites;

import com.bastiansmn.vp.project.ProjectDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PendingInvitesService {

    PendingInvitesDAO create(PendingInvitesCreationDTO pendingInvites);

    List<PendingInvitesDAO> getPendingInvites(String email);

}
