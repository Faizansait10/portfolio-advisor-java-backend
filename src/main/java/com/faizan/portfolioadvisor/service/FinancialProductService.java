// src/main/java/com/faizan/portfolioadvisor/service/FinancialProductService.java
package com.faizan.portfolioadvisor.service;

import com.faizan.portfolioadvisor.dao.FinancialProductDAO;
import com.faizan.portfolioadvisor.model.FinancialProduct;
import java.util.List;

public class FinancialProductService {
    private final FinancialProductDAO financialProductDAO;

    public FinancialProductService(FinancialProductDAO financialProductDAO) {
        this.financialProductDAO = financialProductDAO;
    }

    public void addProduct(FinancialProduct product) {
        // Add any business logic/validation before saving to DB
        financialProductDAO.addFinancialProduct(product);
    }

    public FinancialProduct getProductById(int id) {
        return financialProductDAO.getFinancialProductById(id);
    }

    public List<FinancialProduct> getAllProducts() {
        return financialProductDAO.getAllFinancialProducts();
    }

    public List<FinancialProduct> getProductsByRiskLevel(String riskLevel) {
        // You would need to add a new method in FinancialProductDAO for this,
        // or filter the result of getAllProducts() here.
        List<FinancialProduct> allProducts = financialProductDAO.getAllFinancialProducts();
        // A quick filter using Java Streams
        return allProducts.stream()
                .filter(p -> p.getRiskLevel().equalsIgnoreCase(riskLevel))
                .collect(java.util.stream.Collectors.toList());
    }
}