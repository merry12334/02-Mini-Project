package in.ashokit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.ashokit.binding.DashBoardResponse;
import in.ashokit.binding.EnquiryForm;
import in.ashokit.binding.EnquirySearchCriteria;
import in.ashokit.entity.StudentEnqEntity;
import in.ashokit.service.EnquiryService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private EnquiryService endService;
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "index";
	}
	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		
		Integer userId=(Integer) session.getAttribute("userId");
		
		DashBoardResponse response = endService.getDashBoardData(userId);
		
		model.addAttribute("response", response);
		
		return "dashboard";
	}
	
	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {
		
		//get course from drop down
		List<String> courses = endService.getCourseName();
		
		//get enq status from dropdown
		List<String> status = endService.getEnqStatus();
		
		//create binding class object
		EnquiryForm formObj= new EnquiryForm();
		
		//set data in model object
		model.addAttribute("courses", courses);
		model.addAttribute("status", status);
		model.addAttribute("formObj",formObj);
		
		return "add-enquiry";  
	}
	
	@PostMapping("/enquiry")
	public String handleAddEnq(@ModelAttribute("formObj") EnquiryForm form,Model model) {
		boolean status = endService.upsertEnquiry(form);
		if(status) {
			model.addAttribute("succMsg","Enquiry added");
			
		}else {
			model.addAttribute("errMsg","Problem Occured");
		}
	return "add-enquiry";
	}
	
	private void initForm(Model model) {
		//get course from dropdown
		List<String> courses = endService.getCourseName();
		
		//get status from dropdown
		List<String> enqStatus = endService.getEnqStatus();
		EnquiryForm formObj= new EnquiryForm();
		
		model.addAttribute("courses",courses );
		model.addAttribute("enqStatus",enqStatus );
		model.addAttribute("formObj", formObj);
		
	}
	
	@GetMapping("/enquires")
	public String viewEnquiriesPage(Model model) {
	
		initForm(model);
		List<StudentEnqEntity> enquiries = endService.getEnquiries();
		model.addAttribute("enquiries", enquiries);

		return "view-enquiries";
		}


}
