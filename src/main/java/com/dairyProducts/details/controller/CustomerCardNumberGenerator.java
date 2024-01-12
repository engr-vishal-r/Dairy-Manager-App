package com.dairyProducts.details.controller;

import com.dairyProducts.details.entity.Customer;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;
public class CustomerCardNumberGenerator implements IdentifierGenerator {

    private static final AtomicLong sequence = new AtomicLong(1000);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        int currentYear = Year.now().getValue();
        long generatedValue = 1000 + sequence.getAndIncrement();
        String formattedValue = String.valueOf(currentYear)+ generatedValue;
        return Long.parseLong(formattedValue);
    }

}