package com.dairyProducts.details.utility;

import com.dairyProducts.details.entity.Customer;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerCardNumberGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        String prefix = String.valueOf(Year.now().getValue());

        // Wrapper class to hold the generated ID
        class ResultHolder {
            Long generatedId;

        }

        ResultHolder resultHolder = new ResultHolder();

        // Use session.doWork to perform JDBC work within the context of the current transaction
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try (Statement statement = connection.createStatement()) {
                    ResultSet rs = statement.executeQuery("select count(card_number) as Id from customer");

                    if (rs.next()) {
                        int id = rs.getInt(1) + 1001;
                        resultHolder.generatedId = Long.parseLong(prefix + id);
                    }
                }
            }
        });

        return resultHolder.generatedId;
    }
}