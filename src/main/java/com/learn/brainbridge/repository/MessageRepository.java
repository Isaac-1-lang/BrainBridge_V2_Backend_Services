package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.Message;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverOrderByCreatedAtDesc(User receiver);

    List<Message> findByProjectOrderByCreatedAtAsc(Projects project);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Message m WHERE m.sender = :user OR m.receiver = :user ORDER BY m.createdAt DESC")
    List<Message> findUserMessages(@org.springframework.data.repository.query.Param("user") User user);
}
