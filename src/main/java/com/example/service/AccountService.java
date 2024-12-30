package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidCredentialsException;
import com.example.repository.AccountRepository;

@Service
@Transactional(rollbackFor = {InvalidAccountException.class, InvalidCredentialsException.class})
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;


    public Account findAccountByUsername(String username) throws InvalidAccountException{
        return accountRepository.findByUsername(username);
    }

    public Account saveAccount(Account account) throws InvalidAccountException{
        // Check whether there was any error with the specific account data
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            throw new InvalidAccountException("Account information cannot be null");
        }
        // Check if account already exists
        Account existingAccount = findAccountByUsername(account.getUsername());
        if(existingAccount != null){
            throw new InvalidAccountException("Account already exists");
        }

        // Save newly created account to database
        return accountRepository.save(account);
    }

    public Account validateAccount(Account account) throws InvalidCredentialsException { 
        // Retrieve Account username and validate credentials with account in database
        Account accountInDb = accountRepository.findByUsername(account.getUsername());
        if(accountInDb != null && accountInDb.getPassword().equals(account.getPassword())){
            return accountInDb;
        }
        else{
            throw new InvalidCredentialsException("User Credentials failed");
        }
    }

    public Account findAccountById(Integer id){
        Optional<Account> accountInDb = accountRepository.findById(id);
        return accountInDb.orElse(null);
    }
}
