package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.User;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

	@Query("SELECT u FROM User u WHERE u.email= :email")
	public User getUserByEmail(@Param("email") String email);
	
	/* No need to implement by query. As spring framework already
	 * have implemented by countById method
	 */
	 //@Query("SELECT count(u.id) FROM User u") 
	 //public User countById(@Param("id")
	 //Integer id);

	public Long countById(Integer id);
	
	@Query("Update User u SET u.enabled =?2 where u.id= ?1")
	@Modifying
	public void updateEnableStatus(Integer id,boolean enabled);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
}
