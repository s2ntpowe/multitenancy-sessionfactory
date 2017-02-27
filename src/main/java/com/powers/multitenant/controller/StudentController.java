package com.powers.multitenant.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.powers.multitenant.model.Student;
import com.powers.multitenant.model.StudentLogin;
import com.powers.multitenant.service.StudentService;


@EnableTransactionManagement
@Transactional
@Controller
@SessionAttributes("student")
public class StudentController {
	

	@Autowired
	private StudentService studentService;
		
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signup(Model model) {
		Student student = new Student();
		model.addAttribute("student", student);		
		return "signup";
	}	
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid @ModelAttribute("student") Student student, BindingResult result, Model model,HttpSession httpSession) {	
		if(student!=null && student.getUserName().contains("-1")){
			httpSession.setAttribute("hibernate.tenant_identifier_resolver", "tenant1");
		}
		else {
			httpSession.setAttribute("hibernate.tenant_identifier_resolver", "tenant2");
		}
		if(result.hasErrors()) {
			return "signup";
		} else if(studentService.findByUserName(student.getUserName())) {
			model.addAttribute("message", "User Name exists. Try another user name");
			return "signup";
		} else {
			studentService.save(student);
			model.addAttribute("message", "Saved student details");
			return "redirect:login.html";
		}
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(Model model) {			
		StudentLogin studentLogin = new StudentLogin();		
		model.addAttribute("studentLogin", studentLogin);
		return "login";
	}

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(@Valid @ModelAttribute("studentLogin") StudentLogin studentLogin,BindingResult result,Map<String, Object> model, HttpSession httpSession) {
		if (result.hasErrors()) {
			return "login";
		} else {			
			if(studentLogin.getUserName().contains("-1")){
				httpSession.setAttribute("hibernate.tenant_identifier_resolver", "tenant1");
			}
			else {
				httpSession.setAttribute("hibernate.tenant_identifier_resolver", "tenant2");
			}
			boolean found = studentService.findByLogin(studentLogin.getUserName(), studentLogin.getPassword());
			if (found) {				
				return "success";
			} else {				
				return "failure";
			}
		}
		
	}

	@RequestMapping(value="/getAllStudents", method=RequestMethod.GET)
	public String getAllStudents(@RequestParam("tenantId") String tenantId,HttpSession httpSession) {			
		System.out.println("In controller...");
		System.out.println("TENANT ID ====> " +  tenantId);
		httpSession.setAttribute("hibernate.tenant_identifier_resolver", tenantId);
		List<Student> students = studentService.getAllStudents();
		JSONArray jsonArray = new JSONArray();
		for(Student s:students){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", s.getUserName());
			jsonObject.put("firstname", s.getFirstName());
			jsonObject.put("lastname", s.getLastName());
			jsonObject.put("email", s.getEmailAddress());
			jsonArray.add(jsonObject);
			System.out.println("STUDENT: " + s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName());
		}
		
		return jsonArray.toString();
	}
}
