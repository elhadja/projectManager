package com.elhadjium.PMBackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.dto.ApplicationInfosOutputDTO;

@RestController
@RequestMapping(PMConstants.PMBaseUri + "/generic")
public class ProjectManagementController {
	@Value("${spring.application.name}")
	private String name;

	@Value("${spring.application.version}")
	private String version;

	@Value("${spring.application.environnement}")
	private String environnement;
	
	@GetMapping
	public ApplicationInfosOutputDTO sendEmail() {
		return new ApplicationInfosOutputDTO(environnement, version, name);
	}
}
