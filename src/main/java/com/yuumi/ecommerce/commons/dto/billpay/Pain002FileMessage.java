package com.yuumi.ecommerce.commons.dto.billpay;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.yuumi.ecommerce.commons.events.billpay.Pain002Message;

public record Pain002FileMessage(
        UUID batchId,
        List<Pain002Message> items,
        OffsetDateTime generatedAt
) {}