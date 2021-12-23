package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	/**
	 * Unit test function for CRUD operation for user entity
	 */
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user1 = new User("Krish@gmail.com", "bc@2021", "Krish", "Kumar");
		user1.addRoles(roleAdmin);

		User savedUser = repo.save(user1);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userA = new User("Ap@gmail.com", "Ap@2021", "Ap", "Kumar");
		Role roleSalesperson = new Role(2);
		Role roleEditor = new Role(3);
		userA.addRoles(roleSalesperson);
		userA.addRoles(roleEditor);

		User savedUser = repo.save(userA);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User userName = repo.findById(2).get();
		System.out.println(userName);
		assertThat(userName).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userName = repo.findById(2).get();
		userName.setEnabled(true);
		userName.setEmail("xyz@gmail.com");
		System.out.println(userName);
		repo.save(userName);
	}

	
	
	  @Test public void testUpdateUserRoles() { User userRavi =
	  repo.findById(2).get(); Role roleEditor = new Role(3); Role roleSalesperson =
	  new Role(2); userRavi.getRoles().remove(roleEditor);
	  userRavi.addRoles(roleSalesperson);
	  
	  repo.save(userRavi); }
	  
	  
	  
	  @Test public void testDeleteUser() { Integer userId=33;
	  repo.deleteById(userId); }
	 
	  @Test
	  public void testGetUserByEmail() {
		  String email="Kristopher01@gmail.com";
		 User user=repo.getUserByEmail(email);
		 System.out.println(user);
		 assertThat(user).isNotNull();
		 
	  }
	  
	  @Test
	  public void testCountById() {
		  Integer id=100;
		  Long user=repo.countById(id);
		  System.out.println(user);
		  assertThat(user).isNotNull().isGreaterThan(0);
	  }
	  
	  @Test
	  public void testUpdateEnabledUser() {
		  Integer id=92;
		  repo.updateEnableStatus(id, true);
	  }
	
	 @Test
	 public void testCreateFirstPage() {
		 int pageNumber=1;
		 int pageSize=4;
		 
		 Pageable pageable=PageRequest.of(pageNumber, pageSize);
		 Page<User> page=repo.findAll(pageable);
		 List<User> listUsers=page.getContent();
		 listUsers.forEach(user->System.out.println(user));
		 assertThat(listUsers.size()).isEqualTo(pageSize);
	 }
	 
	 @Test
	 public void searchByKeyword() {
		 int pageNumber=0;
		 int pageSize=4;
		 String keyword="Bruce";
		 
		 	 Pageable pageable=PageRequest.of(pageNumber, pageSize);
			 Page<User> page=repo.findAll(keyword,pageable);
			 List<User> listUsers=page.getContent();
			 listUsers.forEach(user->System.out.println(user));
			 assertThat(listUsers.size()).isGreaterThan(0);
	 }
}
