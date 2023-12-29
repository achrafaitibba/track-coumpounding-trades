package com.achrafaitibba.trackcompoundingtrades.controller;

import com.achrafaitibba.trackcompoundingtrades.model.Target;
import com.achrafaitibba.trackcompoundingtrades.service.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/target")
@RequiredArgsConstructor
public class TargetController {
    private final TargetService targetService;

    @GetMapping("/{timeframe}/{page}/{size}")
    public ResponseEntity<Page<Target>> getAllTargets(
            @PathVariable String timeframe,
            @PathVariable Integer page,
            @PathVariable Integer size
    ) {
        return ResponseEntity.ok().body(targetService.getAll(timeframe, page - 1, size));
    }


    @GetMapping("actual")
    public ResponseEntity<Double> refreshActualTarget(){
        return ResponseEntity.ok().body(targetService.refreshActualTarget());
    }

}
