package com.stayconnected.utils.service;

public interface CurrencyConverter {
	
	public double getConversionRate(String fromCurrency, String toCurrency) throws Exception;
	
}
