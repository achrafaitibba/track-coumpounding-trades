package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.configuration.token.JwtService;
import com.achrafaitibba.trackcompoundingtrades.dto.request.TradeRequest;
import com.achrafaitibba.trackcompoundingtrades.exception.CustomErrorMessage;
import com.achrafaitibba.trackcompoundingtrades.exception.RequestException;
import com.achrafaitibba.trackcompoundingtrades.model.Account;
import com.achrafaitibba.trackcompoundingtrades.model.Coin;
import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import com.achrafaitibba.trackcompoundingtrades.repository.CoinRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.TradeRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        Account account = userRepository.findByUsername(
                claims.getSubject()
        ).get().getAccount();

        if (request.tradeDate().isBefore(account.getOfficialStartDate())) {
            throw new RequestException(CustomErrorMessage.TRADE_DATE_SHOULD_BE_AFTER_START_DATE.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.investedCap() <= 0) {
            throw new RequestException(CustomErrorMessage.NEGATIVE_INVESTED_CAP.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.investedCap() > account.getCurrentBalance()) {
            throw new RequestException(CustomErrorMessage.INVESTED_CAP_HIGHER_THAN_BASE_CAP.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.closedAt() <= 0) {
            throw new RequestException(CustomErrorMessage.CLOSED_AT_ZERO_VALUE.getMessage(), HttpStatus.CONFLICT);
        }
        Double targetByInvestedCapital = targetService.calculateTarget(
                "win",
                request.investedCap(),
                account.getCompoundPercentage() / 100,
                account.getEstimatedFeesByTradePercentage() / 100
        );
        Trade trade = Trade.builder()
                .date(request.tradeDate())
                .investedCap(request.investedCap())
                .closedAt(request.closedAt())
                .PNL(request.closedAt() - request.investedCap())
                .targetByInvestedCap(
                        targetByInvestedCapital
                )
                .diffProfitTarget(request.closedAt() - targetByInvestedCapital)
                .tradingPair(request.baseCoin().toUpperCase() + "-" + request.quoteCoin().toUpperCase())
                .account(account)
                .build();
        account.setCurrentBalance(account.getCurrentBalance() + trade.getPNL());
        coinRepository.save(Coin.builder().coinName(request.baseCoin()).build());
        coinRepository.save(Coin.builder().coinName(request.quoteCoin()).build());
        tradeRepository.save(trade);
        return trade;
    }


    public void deleteById(Long id) {
        Optional<Trade> trade = tradeRepository.findById(id);
        tradeRepository.deleteById(id);
        String header = httpServletRequest.getHeader("Authorization");
        String jwt = header.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        Account account = userRepository.findByUsername(
                claims.getSubject()
        ).get().getAccount();
        account.setCurrentBalance(account.getCurrentBalance() - trade.get().getPNL());
        tradeRepository.deleteById(id);
    }

    public Trade updateTrade(Long id, TradeRequest request) {

        Optional<Trade> trade = tradeRepository.findById(id);
        String header = httpServletRequest.getHeader("Authorization");
        String jwt = header.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        Account account = userRepository.findByUsername(
                claims.getSubject()
        ).get().getAccount();
        if (request.tradeDate().isBefore(account.getOfficialStartDate())) {
            throw new RequestException(CustomErrorMessage.TRADE_DATE_SHOULD_BE_AFTER_START_DATE.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.investedCap() <= 0) {
            throw new RequestException(CustomErrorMessage.NEGATIVE_INVESTED_CAP.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.investedCap() > account.getCurrentBalance()) {
            throw new RequestException(CustomErrorMessage.INVESTED_CAP_HIGHER_THAN_BASE_CAP.getMessage(), HttpStatus.CONFLICT);
        }
        if (request.closedAt() <= 0) {
            throw new RequestException(CustomErrorMessage.CLOSED_AT_ZERO_VALUE.getMessage(), HttpStatus.CONFLICT);
        }
        account.setCurrentBalance(account.getCurrentBalance() - trade.get().getPNL());
        Double targetByInvestedCapital = targetService.calculateTarget(
                "win",
                request.investedCap(),
                account.getCompoundPercentage() / 100,
                account.getEstimatedFeesByTradePercentage() / 100
        );
        double oldPNL = trade.get().getPNL();
        trade.get().setDate(request.tradeDate());
        trade.get().setInvestedCap(request.investedCap());
        trade.get().setClosedAt(request.closedAt());
        trade.get().setPNL(request.closedAt() - request.investedCap());
        trade.get().setTargetByInvestedCap(targetByInvestedCapital);
        trade.get().setDiffProfitTarget(request.closedAt() - targetByInvestedCapital);
        trade.get().setTradingPair(request.baseCoin().toUpperCase()+"-"+request.quoteCoin().toUpperCase());
        Trade  saved = tradeRepository.save(trade.get());
        account.setCurrentBalance(account.getCurrentBalance() - oldPNL + saved.getPNL());
        return saved;
    }

    public Page<Trade> getAll(String sort, String direction, Integer page, Integer size) {
        String header = httpServletRequest.getHeader("Authorization");
        String jwt = header.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        Account account = userRepository.findByUsername(
                claims.getSubject()
        ).get().getAccount();
        Sort sorting = Sort.by(Sort.Direction.valueOf(direction),sort);
        Pageable pageable = PageRequest.of(page, size, sorting);
        return tradeRepository.findByAccount_AccountId(account.getAccountId(), pageable);

    }
}
