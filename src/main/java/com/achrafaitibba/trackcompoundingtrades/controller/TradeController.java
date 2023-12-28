package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.dto.request.TradeRequest;
import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import com.achrafaitibba.trackcompoundingtrades.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/add")
    public ResponseEntity<Trade> createTrade(@RequestBody TradeRequest request){
        return ResponseEntity.ok().body(tradeService.createTrade(request));
    }
}
