package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.SignUpForm;
import in.ashokit.binding.UnlockForm;
import in.ashokit.service.UserService;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	@Autowired 
	private UserService userService;
	

	@GetMapping("/signup")
	public  String signupPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String handleSignUp(@Valid @ModelAttribute("user")  SignUpForm form,BindingResult result,Model model) {
		
		if(result.hasErrors()) {
			return "signup";
		}
		boolean status = userService.signup(form);
		if(status) {
			model.addAttribute("succMsg", " Account Created Check Your Email");
		}else {
			model.addAttribute("errMsg","Choose Unique Email");
		}
		return "signup";
	}
	
	@GetMapping("/unlock")
	public  String unlockPage(@RequestParam String email, Model model) {
		
		UnlockForm form= new UnlockForm();
		form.setEmail(email);
		model.addAttribute("unlock",form);
		
		return "unlock";
	}
	
	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm form ,Model model) {
		   if (!form.getNewPwd().equals(form.getConfPwd())) {
		        model.addAttribute("error", "New Password and Confirm Password do not match.");
		        return "unlock";
		    }

		    String status = userService.unlockAccount(form);
		    if (status.equals("Account Unlocked")) {
		        model.addAttribute("msg", "Your account has been unlocked. Please login.");
		        return "login"; // redirect to login page
		    } else {
		        model.addAttribute("error", status); // show error returned from service
		        return "unlock";
		    }
	}
	
	@GetMapping("/login")
	public  String loginPage(Model model) {
		
		model.addAttribute("login", new LoginForm());
		
		return "login";
	}
	
	@PostMapping("/login")
	public String handleLogin(@Valid @ModelAttribute("login") LoginForm form,BindingResult result,Model model) {
		 if(result.hasErrors()) {
			 return "login";
		 }
		 String status = userService.login(form);
		 if("success".equals(status)) {
			 return "redirect:/dashboard";
		 }
		 model.addAttribute("errMsg", status);
		return "login";
		
	}
	

	@GetMapping("/forgot")
	public  String forgotPage() {
		return "forgotPwd";
	}
	
	@PostMapping("/forgot")
	public String handleForgotPwd(@RequestParam("email")String email,Model model) {
		String status = userService.forgotPwd(email);
		model.addAttribute("msg", status);
		return "forgotPwd";
	}

}
