package com.ridemanagement.accountservice.controller;

import com.ridemanagement.accountservice.dto.request.LoginRequest;
import com.ridemanagement.accountservice.dto.request.RegisterRequest;
import com.ridemanagement.accountservice.dto.request.UpdateAccountRequest;
import com.ridemanagement.accountservice.dto.request.UpdateAccountStatusRequest;
import com.ridemanagement.accountservice.dto.response.AccountResponse;
import com.ridemanagement.accountservice.dto.response.LoginResponse;
import com.ridemanagement.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
        public ResponseEntity<AccountResponse> registerAccount(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(accountService.registerAccount(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.login(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountSecurity.isOwnerOrAdmin(#id, authentication)")
        public ResponseEntity<AccountResponse> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AccountResponse> getAccountByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountService.getAccountByEmail(email));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountSecurity.isOwnerOrAdmin(#id, authentication)")
        public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String id,
            @Valid @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@accountSecurity.isOwnerOrAdmin(#id, authentication)")
        public ResponseEntity<Void> deactivateAccount(@PathVariable String id) {
        accountService.deactivateAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateAccountStatusRequest request) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, request));
    }
}
