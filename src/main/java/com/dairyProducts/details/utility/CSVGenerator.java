package com.dairyProducts.details.utility;


import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CSVGenerator.class);

    public String generateCSV(Customer customer, List<Product> productList) {
        logger.info("Received data to generate csv file" + "  " + "for the customer CardNumber -> " + customer.getCardNumber());

        String fileName = customer.getCardNumber() + ".csv";
        String filePath = "D:\\Users\\vishal\\eclipse-workspace\\dairyProduct\\src\\main\\resources\\csv\\" + fileName;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writeRow(writer, "Customer Name", customer.getCustomerName().toUpperCase());
            writeRow(writer, "Card Number", String.valueOf(customer.getCardNumber()));
            writeRow(writer, "Mobile No.", String.valueOf(customer.getMobileNo()));
            writeRow(writer, "Pending Amount", String.valueOf(customer.getPendingAmount()));

            writeRow(writer, "Product ID", "Product Name", "Quantity", "Unit Price", "Total Price", "Payment Status", "PurchasedDate");

            for (Product product : productList) {
                writeRow(writer, String.valueOf(product.getId()), product.getProductName().toUpperCase(),
                        String.valueOf(product.getQuantity()), String.valueOf(product.getUnitPrice()),
                        String.valueOf(product.getQuantity() * product.getUnitPrice()), String.valueOf(product.getPaid()),
                        String.valueOf(product.getPurchasedDate()));
            }
            logger.info("CSV file generated successfully.");

            String csvFileLink = "/downloadCsv/" + fileName;
            return csvFileLink;

        } catch (IOException e) {
            logger.error("Error occurred while generating csv file");
            e.printStackTrace();
            return ("Error occurred while generating csv file");
        }

    }

    private void writeRow(CSVWriter writer, String... values) throws IOException {
        writer.writeNext(values);
    }
}

