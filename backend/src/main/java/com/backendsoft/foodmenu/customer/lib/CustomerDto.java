package com.backendsoft.foodmenu.customer.lib;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Customer")
public class CustomerDto {
    private String id;
    private String fullname;
    private String phone; // optionnel
}
