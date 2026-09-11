package com.backendsoft.foodmenu.customer.lib;

import com.backendsoft.foodmenu.utils.InvalidEntityException;

public interface CustomerService {
    CustomerDto save(CustomerDto customerDto) throws InvalidEntityException;
    CustomerPage getCustomers(String keyword, int page, int size);
    CustomerDto updateCustomerName(String customerId, String newName) throws CustomerNotFoundException, CustomerUnchangedException;
    void deleteCustomer(String customerId) throws CustomerNotFoundException, UndeletableCustomerException;
}
