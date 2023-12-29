package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.dto.request.TradeRequest;
import com.achrafaitibba.trackcompoundingtrades.enumeration.TradeSortingOption;
import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import com.achrafaitibba.trackcompoundingtrades.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/add")
    public ResponseEntity<Trade> createTrade(@RequestBody TradeRequest request) {
        return ResponseEntity.ok().body(tradeService.createTrade(request));
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<Trade> updateTrade(@PathVariable Long id,
                                             @RequestBody TradeRequest request) {
        return ResponseEntity.ok().body(tradeService.updateTrade(id, request));
    }


    @DeleteMapping("/delete/{id}")
    public void deleteTradeBYId(@PathVariable Long id) {
        tradeService.deleteById(id);
    }


    @GetMapping("/{sort}/{direction}/{page}/{size}")
    public ResponseEntity<Page<Trade>> getAllTrades(
            @PathVariable String sort,
            @PathVariable String direction,
            @PathVariable Integer page,
            @PathVariable Integer size
    ) {
        return ResponseEntity.ok().body(tradeService.getAll(TradeSortingOption.valueOf(sort).getOption(), direction, page - 1, size));

    }

    //todo> endpoint of the sorting options > date, investedCap, closedAt, PNL, targetByInvestedCap, diffProfitTarget, tradingPair

}
