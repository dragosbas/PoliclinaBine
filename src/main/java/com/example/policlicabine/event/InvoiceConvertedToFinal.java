package com.example.policlicabine.event;

import java.util.UUID;

public record InvoiceConvertedToFinal(
    UUID invoiceId,
    String oldInvoiceNumber,
    String newInvoiceNumber
) {}
