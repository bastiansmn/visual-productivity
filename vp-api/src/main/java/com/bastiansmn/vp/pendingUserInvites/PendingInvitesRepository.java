package com.bastiansmn.vp.pendingUserInvites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingInvitesRepository extends JpaRepository<PendingInvitesDAO, String> {

    List<PendingInvitesDAO> findAllByEmail(String email);

    void deleteAllByEmailAndProject(String email, String project);

}
