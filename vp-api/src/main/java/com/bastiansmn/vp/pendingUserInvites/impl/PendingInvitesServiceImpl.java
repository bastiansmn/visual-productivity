package com.bastiansmn.vp.pendingUserInvites.impl;

import com.bastiansmn.vp.pendingUserInvites.PendingInvitesCreationDTO;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesDAO;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesRepository;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PendingInvitesServiceImpl implements PendingInvitesService {

    private final PendingInvitesRepository pendingInvitesRepository;

    @Override
    public PendingInvitesDAO create(PendingInvitesCreationDTO pendingInvites) {
        PendingInvitesDAO pendingInvitesDAO = PendingInvitesDAO.builder()
                .email(pendingInvites.getEmail())
                .project(pendingInvites.getProject())
                .build();

        return this.pendingInvitesRepository.save(pendingInvitesDAO);
    }

    @Override
    public List<PendingInvitesDAO> getPendingInvites(String email) {
        return this.pendingInvitesRepository.findAll();
    }

}
