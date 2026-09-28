package com.ridemanagement.accountservice.service.impl;

import com.ridemanagement.accountservice.dto.request.RegisterRequest;
import com.ridemanagement.accountservice.dto.request.UpdateAccountRequest;
import com.ridemanagement.accountservice.dto.response.AccountResponse;
import com.ridemanagement.accountservice.exception.EmailAlreadyExistsException;
import com.ridemanagement.accountservice.exception.PhoneNumberAlreadyExistsException;
import com.ridemanagement.accountservice.exception.ResourceNotFoundException;
import com.ridemanagement.accountservice.model.Account;
import com.ridemanagement.accountservice.model.AccountStatus;
import com.ridemanagement.accountservice.repository.AccountRepository;
import com.ridemanagement.accountservice.service.AccountService;
import com.ridemanagement.accountservice.util.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of AccountService providing core business logic for account management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse registerAccount(RegisterRequest request) {
        log.info("Attempting to register account with email: {}", request.getEmail());

        // Validate unique email
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Account with email '" + request.getEmail() + "' already exists");
        }

        // Validate unique phone number
        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Account with phone number '" + request.getPhoneNumber() + "' already exists");
        }

        // Map request to entity, set status to ACTIVE and timestamps
        Account account = AccountMapper.toAccount(request);
        account.setStatus(AccountStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        account.setCreatedAt(now);
        account.setUpdatedAt(now);

        Account savedAccount = accountRepository.save(account);
        log.info("Account successfully registered with id: {}", savedAccount.getId());

        return AccountMapper.toAccountResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(String id) {
        log.info("Fetching account by id: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        return AccountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse getAccountByEmail(String email) {
        log.info("Fetching account by email: {}", email);

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with email: " + email));

        return AccountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse updateAccount(String id, UpdateAccountRequest request) {
        log.info("Updating account with id: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        // If phone number is being changed, check if the new phone number is already registered by another account
        if (!account.getPhoneNumber().equals(request.getPhoneNumber())) {
            if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new PhoneNumberAlreadyExistsException("Phone number '" + request.getPhoneNumber() + "' is already in use by another account");
            }
            account.setPhoneNumber(request.getPhoneNumber());
        }

        // Update allowed profile fields only (id, email, password, role, status, createdAt remain unchanged)
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setUpdatedAt(LocalDateTime.now());

        Account updatedAccount = accountRepository.save(account);
        log.info("Account successfully updated for id: {}", updatedAccount.getId());

        return AccountMapper.toAccountResponse(updatedAccount);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        log.info("Fetching all accounts");

        return accountRepository.findAll().stream()
                .map(AccountMapper::toAccountResponse)
                .toList();
    }

    @Override
    public void deactivateAccount(String id) {
        log.info("Deactivating account with id: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setStatus(AccountStatus.INACTIVE);
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
        log.info("Account successfully deactivated for id: {}", id);
    }
}
