package com.backendsoft.foodmenu.customer;

import com.backendsoft.foodmenu.customer.lib.*;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/"})
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping({"customers"})
    public CustomerDto save(@RequestBody CustomerDto customerDto) throws InvalidEntityException {
        return customerService.save(customerDto);
    }

    @GetMapping({"customers"})
    public CustomerPage getCustomersByKeyword(
        @RequestParam(value = "keyword",defaultValue = "") String keyword,
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = "10") int size
    ) {
        return customerService.getCustomers(keyword,page,size);
    }

    @PutMapping("customers/{customerId}")
    public CustomerDto updateCustomer(
            @PathVariable String customerId,
            @RequestBody CustomerUpdateRequest customerUpdateRequest
    ) throws CustomerNotFoundException, CustomerUnchangedException {
        return customerService.updateCustomerName(customerId, customerUpdateRequest.getName());
    }

    @DeleteMapping("customers/{customerId}")
    public void deleteCustomer(@PathVariable String customerId) throws CustomerNotFoundException, UndeletableCustomerException {
        customerService.deleteCustomer(customerId);
    }
}
