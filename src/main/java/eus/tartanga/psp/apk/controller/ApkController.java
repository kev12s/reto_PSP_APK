package eus.tartanga.psp.apk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.tartanga.psp.apk.model.Apk;
import eus.tartanga.psp.apk.service.ApkService;

@RestController
@RequestMapping("/apk")
public class ApkController {

	private final ApkService apkService;

	public ApkController(ApkService apkService) {
		this.apkService = apkService;
	}
	
	@GetMapping
	public ResponseEntity<List<Apk>> getAllApk (){
		apkService.
	}
	
	
	
	
}
