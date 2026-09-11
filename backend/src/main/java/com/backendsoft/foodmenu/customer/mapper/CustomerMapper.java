package com.backendsoft.foodmenu.customer.mapper;

import com.backendsoft.foodmenu.customer.lib.Customer;
import com.backendsoft.foodmenu.customer.lib.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto fromEntity(Customer customer);
    Customer fromDto(CustomerDto customerDto);
}
