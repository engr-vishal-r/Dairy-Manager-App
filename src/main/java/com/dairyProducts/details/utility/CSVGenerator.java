package com.dairyProducts.details.utility;

import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.dairyProducts.details.entity.ProductStock;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class CSVGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CSVGenerator.class);

    public String generateCSV(Customer customer, List<Product> productList) {
        logger.info("Received data to generate csv file" + "  " + "for the customer CardNumber -> " + customer.getCardNumber());

        String fileName = customer.getCardNumber()+"_"+ LocalDate.now() + ".csv";
        String filePath = "D:\\Users\\vishal\\eclipse-workspace\\dairyProduct\\src\\main\\resources\\csv\\" + fileName;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writeRow(writer, "", "","", "LVS EXPORTS", " ", "");
            writeRow(writer, "", "","", "CHENNAI", " ", "");
            writeRow(writer, " ", "","", "PURCHASE LIST", " ", "");
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

    public String generateProductStockCSV(List<ProductStock> productStockList) {
        logger.info("Received request to generate product stock CSV report -> ");

        String fileName = "ProductStock_" + LocalDate.now() + ".csv";
        String filePath = "D:\\Users\\vishal\\eclipse-workspace\\dairyProduct\\src\\main\\resources\\csv\\" + fileName;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writeRow(writer, "", "","", "LVS EXPORTS", " ", "");
            writeRow(writer, "", "","", "CHENNAI", " ", "");
            writeRow(writer, "", "","", "STOCK LIST", " ", "");
            writeRow(writer, "Product ID", "Emp ID", "Product Name", "Loaded Date", "Loaded Qty", "Balance Qty");
            for (ProductStock productStock : productStockList) {
                writeRow(writer, String.valueOf(productStock.getId()), String.valueOf(productStock.getEmployeeId()),
                        productStock.getProductName().toUpperCase(), String.valueOf(productStock.getLoadedDate()),
                        String.valueOf(productStock.getLoadedQuantity()), String.valueOf(productStock.getBalanceQuantity()));
            }
            logger.info("Product stock CSV file generated successfully");

            String csvFileLink = "/downloadCsv/" + fileName;
            return csvFileLink;

        } catch (IOException e) {
            logger.error("Error occurred while generating product stock CSV file");
            e.printStackTrace();
            return null;
        }
    }

    private void writeRow(CSVWriter writer, String... values) throws IOException {
        writer.writeNext(values);
    }
}

