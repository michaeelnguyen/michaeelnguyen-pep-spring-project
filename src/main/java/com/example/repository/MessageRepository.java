package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.messageId = :messageId")
    Integer deleteMessageById(@Param("messageId") Integer messageId);

    @Query("SELECT m FROM Message m WHERE m.postedBy = :postedBy")
    List<Message> findAllByAccountId(@Param("postedBy") Integer postedBy);
}
