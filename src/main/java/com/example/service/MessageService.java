package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional(rollbackFor = {InvalidAccountException.class, InvalidMessageException.class})
public class MessageService {
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message createMessage(Message message) throws InvalidAccountException, InvalidMessageException {
        // Check if account exists before creating message
        accountRepository.findById(message.getPostedBy()).orElseThrow(() -> new InvalidAccountException("Account not found"));

        // Check if message is blank or over the 254 characters for message requirements
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new InvalidMessageException("Message text cannot be blank");
        }
        if (message.getMessageText().length() > 254) {
            throw new InvalidMessageException("Message text exceeds 254 characters");
        }

        // Save the message posted by the user to the database
        return messageRepository.save(message);
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> msgInDb = messageRepository.findById(messageId);
        return msgInDb.orElse(null);
    }

    public Integer deleteByMessageId(Integer messageId) {
        return messageRepository.deleteMessageById(messageId);
    }

    public Integer updatedMessageById(Integer messageId, Message message) throws InvalidMessageException {
        Message msgInDb = getMessageById(messageId);
        // Check if message does not exists in the database before updating
        if(msgInDb == null){
            throw new InvalidMessageException("Message does not exist");
        }
        // Check if message passes the message requirements
        if(message.getMessageText().trim().isEmpty() || message.getMessageText().trim().length() > 254){
            throw new InvalidMessageException("Message must not be be blank or over 254 characters");
        }

        // Update and save the message to the database
        msgInDb.setMessageText(message.getMessageText());
        messageRepository.save(message);

        return 1;
    }

    public List<Message> getAllMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllByAccountId(accountId);
    }

}
