package com.transaction.util;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
	
	private static final Map<String, Double> exchangeRates = new HashMap<>();
	 
    static {
        exchangeRates.put("USD_TO_INR", 86.02);
        exchangeRates.put("EUR_TO_INR", 88.61);
        exchangeRates.put("EUR_TO_GBP", 0.84);
        exchangeRates.put("GBP_TO_INR", 105.85);
        exchangeRates.put("USD_TO_EUR", 0.9);
        exchangeRates.put("USD_TO_GBP", 0.75);
        exchangeRates.put("EUR_TO_USD", 1.03);
        exchangeRates.put("GBP_TO_USD", 1.23); 
        exchangeRates.put("GBP_TO_EUR", 1.19);
        exchangeRates.put("INR_TO_USD", 0.012); 
        exchangeRates.put("INR_TO_EUR", 0.011); 
        exchangeRates.put("INR_TO_GBP", 0.0094);
        exchangeRates.put("INR_TO_INR", 1.0);
        exchangeRates.put("USD_TO_USD", 1.0);
        exchangeRates.put("GBP_TO_GBP", 1.0);
        exchangeRates.put("EUR_TO_EUR", 1.0);
    }
 
    public double convert(String fromCurrency, String toCurrency, double amount) {
        String key = fromCurrency + "_TO_" + toCurrency;
        if (exchangeRates.containsKey(key)) {
            return amount * exchangeRates.get(key);
        }
        throw new IllegalArgumentException("Exchange rate for " + key + " not found.");
    }
}
