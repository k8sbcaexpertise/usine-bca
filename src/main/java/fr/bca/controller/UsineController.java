package fr.bca.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.bca.model.ServerManagement;

 

@Controller
public class UsineController {

	private static final String versionFile = "version/version.properties";
	
	@RequestMapping(value = "/usine", method = RequestMethod.GET)
	public String rebootFrom(Model model) {
		model.addAttribute("serverManagement", new ServerManagement());		
		return "usine";
	}

	/* Avec formulaire */
	@RequestMapping(value = "/reboot", method = RequestMethod.POST)
	public String rebootSubmit(@ModelAttribute ServerManagement serverManagement, Model model) {
		model.addAttribute("serverManagement", serverManagement);
		System.out.println("GET " + serverManagement.getInstanceName());
		System.out.println("GET " + serverManagement.getOperation());
		return "usine";
	}
	
	@ResponseBody
	@RequestMapping(value = "/usine/version", method = RequestMethod.GET, headers = "Accept=text/plain")
	public String getVersion(){			
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		Properties props = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream(versionFile)) {
		    props.load(resourceStream);
		} catch (IOException e1) {
			System.out.println("Problème d'affichage de la version de l'usine " + e1);
		}		
		return props.getProperty("application.version");
		
	}
	
}