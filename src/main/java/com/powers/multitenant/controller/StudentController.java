package com.powers.multitenant.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.powers.multitenant.model.Classroom;
import com.powers.multitenant.model.Student;
import com.powers.multitenant.model.StudentLogin;
import com.powers.multitenant.service.ClassroomService;
import com.powers.multitenant.service.StudentService;


@EnableTransactionManagement
@Transactional
@Controller
@SessionAttributes("student")
public class StudentController {
	

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ClassroomService classService;
		
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
	@ResponseBody
	@RequestMapping(value="/updateTenant", method=RequestMethod.GET)
	public String updateTenant(@RequestParam("tenantId") String tenantId,HttpSession httpSession,Model model) {			
		httpSession.setAttribute("hibernate.tenant_identifier_resolver", tenantId);	
		return "{success : \"true\"}";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/getAllStudents", method=RequestMethod.POST)
	public String getAllStudents() {			
		List<Classroom> classList = classService.getAllClassrooms();
		JSONArray jsonArray = new JSONArray();
		Set<String>uniqueSet = new HashSet<String>();
		for(Classroom c:classList)
		{			
			List<Student>students = c.getStudents();
			for(Student s:students){
				uniqueSet.add(s.getUserName());
				JSONObject json = new JSONObject();
				json.put("className", c.getName());
				json.put("classId", c.getClassId());
				json.put("username", s.getUserName());
				json.put("firstname", s.getFirstName());
				json.put("lastname", s.getLastName());
				json.put("email", s.getEmailAddress());
				jsonArray.add(json);
			}
		}
		List<Student> studentList = studentService.getAllStudents();
		for(Student s:studentList){
			if(uniqueSet.add(s.getUserName())){
				JSONObject json = new JSONObject();
				json.put("className", "N/A");
				json.put("classId", "N/A");
				json.put("username", s.getUserName());
				json.put("firstname", s.getFirstName());
				json.put("lastname", s.getLastName());
				json.put("email", s.getEmailAddress());
				jsonArray.add(json);
			}			
		}	
		JSONObject classroomJson = new JSONObject();
		classroomJson.put("classArray", jsonArray);
		System.out.println(jsonArray.toString());
		return classroomJson.toString() ;
	}
}
