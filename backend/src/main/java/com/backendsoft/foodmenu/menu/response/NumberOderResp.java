package com.backendsoft.foodmenu.menu.response;

import lombok.Data;

@Data
public class NumberOderResp {
    private Integer totalCustomersOrdered;
    private Integer totalEligibleCustomers;
}
