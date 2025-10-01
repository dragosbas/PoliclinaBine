package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.SessionBillingDto;
import com.example.policlicabine.entity.SessionBilling;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BillingDiscountMapper.class})
public interface SessionBillingMapper {

    @Mapping(target = "sessionId", source = "session.sessionId")
    @Mapping(target = "subtotalAmount", expression = "java(sessionBilling.getSubtotalAmount())")
    @Mapping(target = "totalDiscountAmount", expression = "java(sessionBilling.getTotalDiscountAmount())")
    @Mapping(target = "finalAmount", expression = "java(sessionBilling.getFinalAmount())")
    SessionBillingDto toDto(SessionBilling sessionBilling);
}
