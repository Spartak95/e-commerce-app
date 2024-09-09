package com.xcoder.ecommerce.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String houseNumber;
    private String zipCode;
}
