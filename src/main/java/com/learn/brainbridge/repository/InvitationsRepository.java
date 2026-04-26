package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationsRepository extends JpaRepository<Invitations, Integer> {

    List<Invitations> findByProjectId(Integer projectId);

    List<Invitations> findByInvitedUserId(Integer invitedUserId);

    List<Invitations> findByInvitedUserIdAndStatus(Integer invitedUserId, String status);

    List<Invitations> findByProjectIdAndStatus(Integer projectId, String status);

    Optional<Invitations> findByProjectIdAndInvitedUserId(Integer projectId, Integer invitedUserId);

    List<Invitations> findByInvitedByUserId(Integer invitedByUserId);

    void deleteByProjectIdAndInvitedUserId(Integer projectId, Integer invitedUserId);
}
