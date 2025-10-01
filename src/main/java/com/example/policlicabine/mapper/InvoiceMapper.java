package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.InvoiceDto;
import com.example.policlicabine.entity.Invoice;
import com.example.policlicabine.entity.SessionBilling;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(source = "generatedBy.userId", target = "generatedByUserId")
    @Mapping(source = "generatedBy.username", target = "generatedByUsername")
    @Mapping(source = "sessionBillings", target = "sessionBillingIds")
    InvoiceDto toDto(Invoice invoice);

    /**
     * Custom mapping to extract IDs from SessionBilling collection.
     */
    default List<UUID> mapSessionBillingsToIds(List<SessionBilling> sessionBillings) {
        if (sessionBillings == null) {
            return null;
        }
        return sessionBillings.stream()
            .map(SessionBilling::getBillingId)
            .collect(Collectors.toList());
    }
}
