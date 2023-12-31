package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.dto.AccountStats;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountAuthenticateRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountRegisterRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountResetRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountUpdatePassword;
import com.achrafaitibba.trackcompoundingtrades.dto.response.AccountAuthenticateResponse;
import com.achrafaitibba.trackcompoundingtrades.dto.response.AccountPreferencesResponse;
import com.achrafaitibba.trackcompoundingtrades.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountAuthenticateResponse> register(@RequestBody AccountRegisterRequest request) {
        accountService.usernameChecker(request);
        return ResponseEntity.ok(accountService.accountRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AccountAuthenticateResponse> authenticate(@RequestBody AccountAuthenticateRequest request) {

        return ResponseEntity.ok().body(accountService.authenticate(request));
    }

    @PostMapping("/update-password")
    public ResponseEntity<AccountAuthenticateResponse> updatePassword(@RequestBody AccountUpdatePassword request){
        return ResponseEntity.ok().body(accountService.updatePassword(request));
    }

    @PostMapping("/reset")
    public ResponseEntity<AccountAuthenticateResponse> resetAccountData(@RequestBody AccountResetRequest request) {
        return ResponseEntity.ok().body(accountService.resetAllData(request));
    }

    @GetMapping()
    public ResponseEntity<AccountPreferencesResponse> getAccountPreferences() {
        return ResponseEntity.ok().body(accountService.getAccountPreferences());
    }

    @GetMapping("/stats")
    public ResponseEntity<AccountStats> refreshAccountStats() {
        return ResponseEntity.ok().body(accountService.refresfAccountStats());
    }

}
