package com.stayconnected.utils.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class GoogleCurrencyConverter implements CurrencyConverter {

	private Logger logger = LoggerFactory.getLogger(GoogleCurrencyConverter.class);

	@Autowired
	MailService mailService;

	private double getRateFromGoogle(String currentCurrency, String requiredCurrency) throws Exception {
		double conversionRate = 0.0;
		String currUrl = "https://www.google.com/search?q=10000" + currentCurrency + "+to+" + requiredCurrency;

		//converting 10000 and then diving by same amount to get the rate with more precision
		Document doc = Jsoup.connect(currUrl).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; XFire Client +http://xfire.codehaus.org)").get();
		List<Element> resultElement = doc.getElementsByClass("J7UKTe");
		String result = resultElement.get(0).html();
		// sample result = "10000 Indian Rupee = 142.75 United States Dollar"
		int from = result.indexOf("=") + 2;
		int to = result.indexOf(" ", from);
		String rate = result.substring(from, to).replaceAll(",", "");
		conversionRate = Double.parseDouble(rate) / 10000;
		return conversionRate;
	}


	@Override
	public double getConversionRate(String fromCurrency, String toCurrency) throws Exception {
		double rate = 0.0;
		try {
			if (StringUtils.isEmpty(fromCurrency) || StringUtils.isEmpty(toCurrency) || fromCurrency.equals(toCurrency)) {
				rate = 1.0;
			} else {
				rate = getRateFromGoogle(fromCurrency, toCurrency);
				logger.debug("successfully got currency conversion rate data from directly hitting google for  as: " + rate);
			}
		} catch (Exception e) {
			String errorMessage = "Could not get conversion rate from google for : " + fromCurrency + "_to_" + toCurrency;
			logger.error(errorMessage, e);
			mailService.sendMail(errorMessage + e.getMessage());
			throw new Exception(errorMessage, e);
		}
		return rate;

	}

	@Scheduled(fixedDelay = 86400000)
	public void testConversion()
	{
		try{
			getConversionRate("INR","USD");
		}
		catch (Exception e)
		{

		}
	}
}
