package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import com.achrafaitibba.trackcompoundingtrades.model.Coin;
import com.achrafaitibba.trackcompoundingtrades.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enums")
@RequiredArgsConstructor
public class EnumController {

    private final CoinRepository coinRepository;

    @GetMapping("/timeframes")
    public ResponseEntity<List<TimeFrame>> getAllTimeFrames(){
        return ResponseEntity.ok().body(Arrays.asList(TimeFrame.values()));
    }


    @GetMapping("/coins")
    public ResponseEntity<List<Coin>> getAllCoins(){
        return ResponseEntity.ok().body(coinRepository.findAll());
    }
}
