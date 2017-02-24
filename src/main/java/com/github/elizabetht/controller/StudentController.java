package com.github.elizabetht.controller;

import java.util.List;

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

import com.github.elizabetht.model.Student;
import com.github.elizabetht.model.StudentLogin;
import com.github.elizabetht.service.StudentService;

@Controller
@SessionAttributes("student")
@Transactional
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
	@Transactional
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(@Valid @ModelAttribute("studentLogin") StudentLogin studentLogin, BindingResult result, HttpSession httpSession) {
		if (result.hasErrors()) {
			return "login";
		} else {
			boolean found = studentService.findByLogin(studentLogin.getUserName(), studentLogin.getPassword());
			if(studentLogin.getUserName().contains("nga")){
				System.out.println("LOGIN: " + studentLogin.getUserName());
				httpSession.setAttribute("agency", "nga");
			}
			else {
				httpSession.setAttribute("agency", "nsa");
			}
			List<Student> students = studentService.getAllStudents();		
			for(Student s:students){
				System.out.println("STUDENT: " + s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName());
			}
			if (found) {				
				return "success";
			} else {				
				return "failure";
			}
		}
		
	}
	
	@RequestMapping(value="/testDatabase", method=RequestMethod.GET)
	public String testDatabase() {			
		String str = "";
		System.out.println("In controller...");
		List<Student> students = studentService.getAllStudents();		
		for(Student s:students){
			System.out.println("STUDENT: " + s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName());
			str += s.getUserName() + " "  + s.getFirstName() + " " + s.getLastName();
		}
		
		return "TEST";
	}
}
