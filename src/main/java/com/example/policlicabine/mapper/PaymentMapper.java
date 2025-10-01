package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.PaymentDto;
import com.example.policlicabine.entity.Invoice;
import com.example.policlicabine.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "invoiceIds", source = "invoices")
    @Mapping(target = "generatedByUserId", source = "generatedBy.userId")
    @Mapping(target = "generatedByUsername", source = "generatedBy.username")
    PaymentDto toDto(Payment payment);

    /**
     * Custom mapping to extract invoice IDs from Invoice collection.
     */
    default List<UUID> mapInvoicesToIds(List<Invoice> invoices) {
        if (invoices == null) {
            return null;
        }
        return invoices.stream()
            .map(Invoice::getInvoiceId)
            .collect(Collectors.toList());
    }
}
