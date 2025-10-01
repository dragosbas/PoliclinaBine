package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.BillingDiscountDto;
import com.example.policlicabine.entity.BillingDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillingDiscountMapper {

    @Mapping(target = "appliedByUserId", source = "appliedBy.userId")
    @Mapping(target = "appliedByUsername", source = "appliedBy.username")
    BillingDiscountDto toDto(BillingDiscount billingDiscount);
}
