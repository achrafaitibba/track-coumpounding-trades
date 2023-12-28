package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
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

    @GetMapping("/timeframes")
    public ResponseEntity<List<TimeFrame>> getAll(){
        return ResponseEntity.ok().body(Arrays.asList(TimeFrame.values()));
    }
}
