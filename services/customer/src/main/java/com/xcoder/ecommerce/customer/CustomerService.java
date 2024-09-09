package com.xcoder.ecommerce.customer;

import java.util.List;

import com.xcoder.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public String createCustomer(CustomerRequest request) {
        Customer customer = customerRepository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        Customer customer = customerRepository.findById(request.id())
            .orElseThrow(() -> new CustomerNotFoundException(
                String.format("Cannot update customer: Customer not found with the provided ID: %s", request.id())));
        mergeCustomer(customer, request);
        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
            .map(customerMapper::fromCustomer)
            .toList();
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(
                String.format("Cannot update customer: Customer not found with the provided ID: %s", customerId)));
        return new CustomerResponse(customer.getId(), customer.getFirstname(), customer.getLastname(),
                                    customer.getEmail(), customer.getAddress());
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}
