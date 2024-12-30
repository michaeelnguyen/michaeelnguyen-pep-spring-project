package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.InvalidMessageException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    /**
     * Endpoint on POST localhost:8080/register to create/register an account
     * Response status code: 200 OK if registration passes account requirements validation
     * If account username exists, 409 (Conflict). Otherwise, 400 (Client error)
     * A request to POST localhost:8080/register will respond with the newly created/registered account
     * @param account The account and associated data used to register an user account
     * @throws InvalidAccountException 
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) throws InvalidAccountException{
        try {
            Account acc = accountService.saveAccount(account);
            return new ResponseEntity<>(acc, HttpStatus.OK);
        } catch (InvalidAccountException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint on POST localhost:8080/login to login to an account
     * Response status code: 200 OK if login credentials are valid and passes authorization
     * Otherwise, 401 (Unauthorized)
     * A request to POST localhost:8080/register will respond with the newly created/registered account.
     * @param account The account and associated data containing the login username and password credentials
     * @throws InvalidCredentialsException 
     */
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) throws InvalidCredentialsException{
        try {
            Account existingAccount = accountService.validateAccount(account);
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        } catch (InvalidCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }

    /**
     * Endpoint on POST localhost:8080/messages to create a message on an user account
     * Response status code: 200 OK if message passes message requirements validation
     * Otherwise, 400 (Client error)
     * A request to POST localhost:8080/register will respond with the newly created/registered account.
     * @param message The message that was created and posted by the user account
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        try {
            Message newMessage = messageService.createMessage(message);
            return new ResponseEntity<>(newMessage, HttpStatus.OK);
        } catch (InvalidMessageException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (InvalidAccountException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint on GET localhost:8080/messages to retrieve all messages in database
     * Response status code: 200 OK, always by default.
     * A request to GET localhost:8080/messages will respond with returning a list of messages,
     * whether or not the list is empty from the database
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * Endpoint on GET localhost:8080/messages/{messageId} to retrieve a message by its messageId
     * Response status code: 200 OK, always by default.
     * A request to GET localhost:8080/messages/{messageId} will respond with returning the message
     * If there is no such message, then response body is expected to be empty.
     * @param messageId The message id that corresponds to a specific message by a user account
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        Message message = messageService.getMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Endpoint on DELETE localhost:8080/messages/{messageId} to delete a message by its messageId
     * Response status code: 200 OK, always by default.
     * A request to DELETE localhost:8080/messages/{messageId} should have return  aresponse body containinh the number of rows updated (1)
     * If there is no such message, then response body is expected to be empty.
     * @param messageId The message id that corresponds to a specific message by a user account that is selected for deletion
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        int rowsDeleted = messageService.deleteByMessageId(messageId);
        if(rowsDeleted > 0){
            return new ResponseEntity<>(rowsDeleted, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Endpoint on PATCH localhost:8080/messages/{messageId} to update a message by its messageId
     * Response status code: 200 OK if the updated message passes message requirements validation
     * Otherwise, 400 (Client error)
     * A request to PATCH localhost:8080/messages/{messageId} will respond with returning the newly updated message
     * @param messageId The message id that corresponds to a specific message by a user account that is selected for update
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message message) throws InvalidMessageException{
        try {
            int rowsUpdated = messageService.updatedMessageById(messageId, message);
            return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);
        } catch (InvalidMessageException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint on GET localhost:8080/accounts/{accountId}/messages to retrieve all messages
     * from a specific account by its accountId
     * Response status code: 200 OK, always by default.
     * A request to GET localhost:8080/accounts/{accountId}/messages will respond with returning the messages
     * If there is no such message, then response body is expected to be empty.
     * @param accountId The account id that is associated with the messages posted by the user account
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable Integer accountId){
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }



}
