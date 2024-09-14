package com.reddot.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Welcome to API page!";
	}

	@GetMapping("/hello")
	public String hello() {
		return "Secret: Hello, World!";
	}

}