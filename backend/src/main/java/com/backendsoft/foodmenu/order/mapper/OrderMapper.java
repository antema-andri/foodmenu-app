package com.backendsoft.foodmenu.order.mapper;

import com.backendsoft.foodmenu.order.OrderItem;
import com.backendsoft.foodmenu.order.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "menu", ignore = true)
    OrderItemDto fromEntity(OrderItem orderItem);
    OrderItem fromDto(OrderItemDto orderItemDto);

}
