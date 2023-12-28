package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.configuration.token.JwtService;
import com.achrafaitibba.trackcompoundingtrades.dto.request.TradeRequest;
import com.achrafaitibba.trackcompoundingtrades.model.Account;
import com.achrafaitibba.trackcompoundingtrades.model.Coin;
import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import com.achrafaitibba.trackcompoundingtrades.repository.CoinRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.TradeRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TradeService {
    private final TargetService targetService;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;
    private final CoinRepository coinRepository;

    public Trade createTrade(TradeRequest request) {
        String header = httpServletRequest.getHeader("Authorization");
        String jwt = header.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        System.out.println(claims.getSubject());
        Account account = userRepository.findByUsername(
                claims.getSubject()
        ).get().getAccount();
        Double targetByInvestedCapital = targetService.calculateTarget(
                "win",
                request.investedCap(),
                account.getCompoundPercentage()/100,
                account.getEstimatedFeesByTradePercentage()/100
        );
        Trade trade = Trade.builder()
                .date(request.tradeDate())
                .investedCap(request.investedCap())
                .closedAt(request.closedAt())
                .PNL(request.closedAt() - request.investedCap())
                .targetByInvestedCap(
                        targetByInvestedCapital
                )
                .diffProfitTarget( request.closedAt() - targetByInvestedCapital )
                .tradingPair(request.baseCoin()+"-"+request.quoteCoin())
                .account(account)
                .build();
        coinRepository.save(Coin.builder().coinName(request.baseCoin()).build());
        coinRepository.save(Coin.builder().coinName(request.quoteCoin()).build());
        tradeRepository.save(trade);
        return trade;
    }



}
