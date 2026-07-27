package com.yuumi.ecommerce.commons.dto.account;
import java.math.BigDecimal;
import java.util.UUID;


public record AccountResponse(
        UUID id,
        String customerId,
        String accountNumber,
        AccountType accountType,
        AccountSubType accountSubType,
        AccountStatus status,
        String currency,
        String nickname,
        String displayName,
        BigDecimal balance,
        String maskedAccountNumber,
        Integer version
) {}