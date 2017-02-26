package com.powers.multitenant.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.powers.multitenant.model.Student;
import com.powers.multitenant.model.StudentLogin;
import com.powers.multitenant.service.StudentService;


@Controller
@SessionAttributes("student")
public class StudentController {
	

	@Autowired
	private StudentService studentService;
		
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signup(Model model) {
		Student student = new Student();
		System.out.println("signup**************");
		model.addAttribute("student", student);		
		return "signup";
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid @ModelAttribute("student") Student student, BindingResult result, Model model) {		
		System.out.println("signup**************");
		if(result.hasErrors()) {
			return "signup";
		} else if(studentService.findByUserName(student.getUserName())) {
			model.addAttribute("message", "User Name exists. Try another user name");
			return "signup";
		} else {
			System.out.println("Saving students details...........");
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
			
			if(studentLogin.getUserName().contains("nga")){
				System.out.println("LOGIN: " + studentLogin.getUserName());
				httpSession.setAttribute("hibernate.tenant_identifier_resolver", "nga");
			}
			else {
				httpSession.setAttribute("hibernate.tenant_identifier_resolver", "nsa");
			}
			boolean found = studentService.findByLogin(studentLogin.getUserName(), studentLogin.getPassword());
			List<Student> students = studentService.getAllStudents();	
			model.put("studentList",students);
			if (found) {				
				return "success";
			} else {				
				return "failure";
			}
		}
		
	}


	//@RequestMapping(value="success.jsp", method=RequestMethod.GET)
	public ModelAndView home(RedirectAttributes redir) {
		List<Student> students = studentService.getAllStudents();		
		System.out.println("Setting ModelAndView ere at success...");
		for(Student s:students){
			System.out.println("MAV ---- STUDENT: " + s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName());
		}
		ModelAndView modelAndView = new ModelAndView("success.jsp");
		
		modelAndView.setViewName("redirect:welcome");
	    redir.addFlashAttribute("studentList",students);
	    return modelAndView;

	}
	
	@RequestMapping(value="/testdatabase", method=RequestMethod.GET)
	public String testDatabase() {			
		System.out.println("In controller...");
		List<Student> students = studentService.getAllStudents();		
		for(Student s:students){
			System.out.println("STUDENT: " + s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName());
		}
		
		return "thisstuff";
	}
}
