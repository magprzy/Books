package pl.jstk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {

	@GetMapping (value = "/403")
	public String accessDenied(){
		
		return "403";
	}
	
	
	
	
	
	
	/*@ResponseStatus(value = HttpStatus.FORBIDDEN) // 403
	@ExceptionHandler()
	public void conflict() {
		// Nothing to do
	}*/
}
