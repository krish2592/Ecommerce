package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	public static final int USER_PAGE_SIZE=4;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		return (List<User>) userRepo.findAll();
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		
	boolean isUpdating=user.getId()!=0;  //Checking edit mode 
	
	if(isUpdating) {				// In edit mode 
		
	   User existingUser =userRepo.findById(user.getId()).get();  //Checking user for the id
	   
	   if(user.getPassword().isEmpty()) {
		   user.setPassword(existingUser.getPassword());
	   } else {
		   encodePassword(user);
	   }
	} else {
		encodePassword(user);   //New mode
	}
		return userRepo.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		if (userByEmail == null)
			return true; 					// Case 1: Email id not exist
											// create new users
		boolean isCreatingNew = (id == null);

		if (isCreatingNew) { 				// Case 2: Id is null but email id exist
			if (userByEmail != null)		// users already exist
				return false; 
		} else {
			if (userByEmail.getId() != id) { //case 3: New id is different from existing email users id
				return false;				 // users already exist
			} 
			else {							//Case 4: New id is same as existing email users id
				return true;				//edit mode
			}
		}
		return userByEmail == null;
	}

	
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find user for id:" + id);
		}
	}
	
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById=userRepo.countById(id);
		
		if(countById==null || countById==0) {
			throw new UserNotFoundException("Could not find user for id:" + id);
		}
		userRepo.deleteById(id);
	}
	 
    public void userUpdateEnableStatus(Integer id, boolean enabled) throws UserNotFoundException {
		userRepo.updateEnableStatus(id, enabled);
    }
    
    public Page<User> listByPage(int pageNumber,String sortField, String sortDirecn) {
    	Sort sort = Sort.by(sortField);
    	sort =sortDirecn.equals("asc") ? sort.ascending() : sort.descending();
    	
    	Pageable pageable=PageRequest.of(pageNumber-1, USER_PAGE_SIZE, sort);
    	return userRepo.findAll(pageable);
    }
}
