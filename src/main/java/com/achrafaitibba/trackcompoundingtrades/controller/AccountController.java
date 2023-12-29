package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountAuthenticateRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountRegisterRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountResetRequest;
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
    public ResponseEntity<AccountAuthenticateResponse> register(@RequestBody AccountRegisterRequest request){
        accountService.usernameChecker(request);
        return ResponseEntity.ok(accountService.accountRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AccountAuthenticateResponse> authenticate(@RequestBody AccountAuthenticateRequest request){

        return ResponseEntity.ok().body(accountService.authenticate(request));
    }

    @PostMapping("/reset")
    public ResponseEntity<AccountAuthenticateResponse> resetAccountData(@RequestBody AccountResetRequest request){
        return ResponseEntity.ok().body(accountService.resetAllData(request));
    }

    @GetMapping()
    public ResponseEntity<AccountPreferencesResponse> getAccountPreferences(){
        return ResponseEntity.ok().body(accountService.getAccountPreferences());
    }
    //todo
    /**
     * show user each time connected:
     *  >balance (baseCapital + sum(PNL)"
     *  >PNL (sum(PNL))
     *  >EST ATP (final target -baseCapital)
     *  > dif aka status ( current balance - estimated final target)
     *  > total trades = count(trades)
     *
     *
     *
     *  Also show user target, extract them from targets by date:
     *  >Today
     *  >Week
     *  >Month
     *
     */

}
