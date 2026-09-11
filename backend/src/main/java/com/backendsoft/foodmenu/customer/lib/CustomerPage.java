package com.backendsoft.foodmenu.customer.lib;

import com.backendsoft.foodmenu.utils.Page;
import lombok.Data;

import java.util.List;

@Data
public class CustomerPage extends Page {
    private List<CustomerDto> customers;
}
