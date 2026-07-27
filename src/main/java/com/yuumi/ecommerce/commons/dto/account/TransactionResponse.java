package com.yuumi.ecommerce.commons.dto.account;


import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String status,
        BigDecimal amount,
        String reason,
        BigDecimal balanceAfter

) {}
