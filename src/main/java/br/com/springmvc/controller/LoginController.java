package br.com.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Realiza o login no sistema.
 */
@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}