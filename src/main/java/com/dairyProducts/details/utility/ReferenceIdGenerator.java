package com.dairyProducts.details.utility;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.Work;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

public class ReferenceIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        YearMonth currentYearMonth = YearMonth.now();
        String yearMonthSuffix = currentYearMonth.getMonth().toString().substring(0, 3);

        // Wrapper class to hold the generated ID
        class ResultHolder {
            String generatedId;
        }

        ResultHolder resultHolder = new ResultHolder();

        // Use session.doWork to perform JDBC work within the context of the current transaction
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try (Statement statement = connection.createStatement()) {
                    ResultSet rs = statement.executeQuery("select count(milk_id) as Id from product");

                    if (rs.next()) {
                        int id = rs.getInt(1) + 10000;  // Incremented ID
                        resultHolder.generatedId = Year.now().getValue() + String.format("%05d", id) + yearMonthSuffix;
                    }
                }
            }
        });

        return resultHolder.generatedId;
    }
}