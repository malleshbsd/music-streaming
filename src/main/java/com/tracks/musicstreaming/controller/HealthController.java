package com.tracks.musicstreaming.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

	private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

	@GetMapping("/")
	public ResponseEntity<String> index() {
		logger.info("Health Index");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
}
