package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	
//	  @GetMapping("/users") 
//	  public String listAll(Model model) { 
//		  List<User> listUsers = service.listAll(); 
//		  model.addAttribute("listUsers", listUsers);
//		  return "users"; 
//		  }
	 

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1,"firstName", "asc", model,null);
	}
	
	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable(name="pageNumber") int pageNumber,@Param(value="sortField") String sortField,@Param(value="sortDirecn") String sortDirecn,Model model, @Param(value="keyword") String keyword) {
		//List<User> listUsers = service.listAll();
		Page<User> page=service.listByPage(pageNumber,sortField,sortDirecn,keyword);
		List<User> listUsers=page.getContent();
//		System.out.println("PageNumber"+", "+page.getNumber());
//		System.out.println("ElementsPerPage"+", "+page.getNumberOfElements());
//		System.out.println("TotalElements"+", "+page.getTotalElements());
//		System.out.println("TotalPages"+", "+page.getTotalPages());
		int currentPage=page.getNumber()+1;
		int previousPage=currentPage-1;
		int nextPage=currentPage+1;
		int lastPage=page.getTotalPages();
		int totalPage=page.getTotalPages();
		int startCount=(pageNumber-1)*service.USER_PAGE_SIZE+1;
		int endCount=startCount+service.USER_PAGE_SIZE-1;
		if(startCount>page.getTotalElements()) {
			startCount=(int) page.getTotalElements();
		}
		if(endCount>page.getTotalElements()) {
			endCount=(int) page.getTotalElements();
		}
		int totalCount=(int) page.getTotalElements();
		String reverseDirection = sortDirecn.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", totalCount);
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("previousPage", previousPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortDirecn", sortDirecn);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseDirection", reverseDirection);
		model.addAttribute("keyword", keyword);
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		List<Role> listRoles = service.listRoles();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,@RequestParam("imagefile") MultipartFile multipartFile) throws IOException 
	{
		//System.out.println(user);
		//System.out.println(multipartFile.getOriginalFilename());
		if(!multipartFile.isEmpty()) {
			String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			//System.out.println(fileName);
			User savedUser= service.save(user);
			String uploadDir ="user-photos/"+savedUser.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(user.getPhotos().isEmpty()) 
				user.setPhotos(null);
				service.save(user);
		}
			
		redirectAttributes.addFlashAttribute("message", "The user has been saved sucessfully");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model,RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (Id:"+" "+id+")");
			model.addAttribute("listRoles", listRoles);
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "User assosciated with the id "+id+" has been deleted sucessfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
	 
	@GetMapping("/users/{id}/enabled/{status}")
	public String userUpdateEnableStatus(@PathVariable(name="id") Integer id,@PathVariable(name="status") boolean enabled, RedirectAttributes redirectAttributes) throws UserNotFoundException {
			service.userUpdateEnableStatus(id, enabled);
			String status = enabled? "enabled" : "disabled";
			String message="The user with status id "+ id+ " has been "+status+" sucessfully";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/users";
	}
	
	
	
}
