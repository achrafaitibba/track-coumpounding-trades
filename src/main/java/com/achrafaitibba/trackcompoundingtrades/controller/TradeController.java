package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.dto.request.TradeRequest;
import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import com.achrafaitibba.trackcompoundingtrades.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/add")
    public ResponseEntity<Trade> createTrade(@RequestBody TradeRequest request){
        return ResponseEntity.ok().body(tradeService.createTrade(request));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTradeBYId(@PathVariable Long id){
         tradeService.deleteById(id);
    }




    //todo
    /**
     * Delete trade
     * update trade
     * get all trades by user
     */
}
