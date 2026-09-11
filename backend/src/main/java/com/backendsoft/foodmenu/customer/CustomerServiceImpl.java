package com.backendsoft.foodmenu.customer;

import com.backendsoft.foodmenu.customer.lib.*;
import com.backendsoft.foodmenu.customer.mapper.CustomerMapper;
import com.backendsoft.foodmenu.order.OrderItem;
import com.backendsoft.foodmenu.order.OrderItemRepository;
import com.backendsoft.foodmenu.utils.ErrorCodes;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import com.backendsoft.foodmenu.utils.RegNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, OrderItemRepository orderItemRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) throws InvalidEntityException {
        List<String> errors = CustomerValidator.validate(customerDto);
        String prefixId = "CST";
        int lastCustomerCount = (int) customerRepository.count();

        if (!errors.isEmpty()) {
            log.error("Customer is not valid: {}", customerDto);
            throw new InvalidEntityException("Customer is not valid", ErrorCodes.CUSTOMER_NOT_VALID, errors);
        }

        Customer customer = customerMapper.fromDto(customerDto);
        customer.setId(RegNumberUtil.generate(prefixId, lastCustomerCount + 1));

        return customerMapper.fromEntity(customerRepository.save(customer));
    }

    @Override
    public CustomerPage getCustomers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        CustomerPage customerPageDto = new CustomerPage();
        Page<Customer> customerPage = this.customerRepository.findCustomersByKeyword(keyword, pageable);

        List<CustomerDto> customerDtos = customerPage.getContent().stream()
                .map(customerMapper::fromEntity)
                .collect(Collectors.toList());

        customerPageDto.setCustomers(customerDtos);
        customerPageDto.setCurrentPage(customerPage.getNumber());
        customerPageDto.setSize(customerPage.getSize());
        customerPageDto.setTotalPage(customerPage.getTotalPages());
        return customerPageDto;
    }

    @Override
    public CustomerDto updateCustomerName(String customerId, String updatedName)
            throws CustomerNotFoundException, CustomerUnchangedException {

        if (!StringUtils.hasText(updatedName)) {
            throw new IllegalArgumentException("The new customer name must not be empty");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with id %s does not exist", customerId))
                );

        if (customer.getFullname().equals(updatedName)) {
            throw new CustomerUnchangedException("No modification to make for the customer name");
        }

        customer.setFullname(updatedName);

        return customerMapper.fromEntity(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(String customerId) throws CustomerNotFoundException, UndeletableCustomerException {

        if (!StringUtils.hasLength(customerId)) {
            throw new IllegalArgumentException("The customer id must not be empty");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with id %s does not exist", customerId))
                );

        List<OrderItem> customerOrders = orderItemRepository.findByCustomerId(customerId);

        if (!customerOrders.isEmpty()) {
            throw new UndeletableCustomerException(
                    String.format("Customer with ID: %s having at least one order cannot be deleted", customerId)
            );
        }

        customerRepository.deleteById(customerId);
    }
}
