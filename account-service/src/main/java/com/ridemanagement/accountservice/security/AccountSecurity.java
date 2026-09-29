package com.ridemanagement.accountservice.security;

import com.ridemanagement.accountservice.model.Account;
import com.ridemanagement.accountservice.model.UserRole;
import com.ridemanagement.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("accountSecurity")
@RequiredArgsConstructor
public class AccountSecurity {

    private final AccountRepository accountRepository;

    public boolean isOwnerOrAdmin(String accountId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();
        
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            return false;
        }

        Account authenticatedAccount = accountOpt.get();

        if (authenticatedAccount.getRole() == UserRole.ADMIN) {
            return true;
        }

        return authenticatedAccount.getId().equals(accountId);
    }
}
