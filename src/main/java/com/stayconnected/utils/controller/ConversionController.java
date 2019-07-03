package com.stayconnected.utils.controller;


import com.stayconnected.utils.Response;
import com.stayconnected.utils.service.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ConversionController {

	@Autowired
	CurrencyConverter converter;

	@GetMapping(value = "/v1/convert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Response> convertCurrency(@RequestParam(value = "from") String from,@RequestParam(value="to") String to) {


		Response response =new Response();
		ResponseEntity<Response> responseResponseEntity;
		try{
			Double rate = converter.getConversionRate(from,to);
			response.setFromCurrency(from);
			response.setToCurrency(to);
			response.setRate(rate);
			responseResponseEntity = new ResponseEntity<>(response, HttpStatus.OK);

		}catch (Exception e)
		{
			response.setError("Error while converting currency: "+e.getMessage());
			responseResponseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return responseResponseEntity;
	}

	@GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Response> convertCurrencyWithToken(@RequestParam(value = "from") String from,@RequestParam(value="to") String to) {


		Response response =new Response();
		ResponseEntity<Response> responseResponseEntity;
		response.setError("Invalid Token. Please send mail to 'service.stay.connected@gmail.com' to get free token");
		responseResponseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		return responseResponseEntity;
	}
}
