package org.springboot.jpa.controller;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.springboot.jpa.exception.ColleageDataException;
import org.springboot.jpa.model.Department;
import org.springboot.jpa.model.Student;
import org.springboot.jpa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class DepartmentsController {

	@Autowired
	DepartmentService departmentService;
	
	@GetMapping("/departments")
	public ResponseEntity<List<Department>> getAllDepartments() throws ColleageDataException{
		
		List<Department> departments = departmentService.getAllDepartments();
		
		if(departments==null||departments.size()<=0) {
			throw new ColleageDataException("departments data not available.Please contact the admin");
		}
		
		return new ResponseEntity<List<Department>>(departments,HttpStatus.OK);
	}
	
	@GetMapping("/department/{departmentId}")
	public ResponseEntity<Department>getDepartmentForId(@PathVariable Long departmentId) throws ColleageDataException{
		
		if(departmentId==null|| departmentId==0) {
				throw new ColleageDataException("departmentId must not be null");
			}
			
			Department department = departmentService.findDepartmentById(departmentId);
			
			if(department==null||department.getDepartmentId()<=0) {
				throw new ColleageDataException("Department data not available for deparmentId"+departmentId);
			}
			
			return new ResponseEntity<Department>(department,HttpStatus.OK);
	}
	
	@GetMapping("/department/students/{departmentId}")
	public ResponseEntity<Set<Student>> getStudentsForDepartment(@PathVariable Long departmentId) throws ColleageDataException{
		
		if(departmentId==null|| departmentId==0) {
			throw new ColleageDataException("departmentId must not be null");
		}
		
		Set<Student> students = departmentService.getAllStudentsForDepartment(departmentId);
		
		if(students==null||students.size()<=0) {
			throw new ColleageDataException("students data not available for the departmentId.Please contact the admin");
		}
		
		return new ResponseEntity<Set<Student>>(students,HttpStatus.OK);
	}
	
	@PostMapping("/colleage/departments/{colleageId}")
	public ResponseEntity<?> addDepartmentToColleage(@RequestBody Department department,@PathVariable Long colleageId) throws ColleageDataException{
		
		if(department == null || colleageId==null) {
			throw new ColleageDataException("No Department data or colleageId is available in the requestbody.Please check your request");
		}
		
		Department savedDepartment = departmentService.saveDepartmentForColleage(department, colleageId);
		
		if(savedDepartment==null||savedDepartment.getDepartmentId()==null) {
			throw new ColleageDataException("Department data not saved for the colleageId"+colleageId);
		}
	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(colleageId).toUri();
		
		return ResponseEntity.created(uri).build();
	}
}
