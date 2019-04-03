package com.stayconnected.utils.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

	@RequestMapping(method = RequestMethod.GET, produces = "text/plain")
	public String getBaggageDetails() {
		return "All is well version:";
	}

}
