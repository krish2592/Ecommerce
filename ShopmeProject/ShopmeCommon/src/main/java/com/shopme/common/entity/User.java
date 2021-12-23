package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	//Here we are using password encoder and length of password encoder character is 64
	@Column(length=64, nullable=false)
	private String password;
	
	@Column(name="email", length=128, nullable=false, unique=true)
	private String email;
	
	@Column(name="first_name", length=45, nullable=false)
	private String firstName;
	
	@Column(name="last_name", length=45, nullable=false)
	private String lastName;
	
	@Column(name="photos",length=64)
	private String photos;
	
	@Column(name="enabled")
	private boolean enabled;

	@ManyToMany
	@JoinTable(
			name="users_roles",
			joinColumns= @JoinColumn(name="user_id"), 
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Role> roles=new HashSet<>();
	
	public User() {
		
	}
	
	public User(String email,String password,  String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRoles(Role roles) {
		this.roles.add(roles);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}
	
	//As this method is not mapped with any database or entity we used transient annotation
	@Transient
	public String getUserPhotosPath() {
		if(id==0 || photos==null) return "/images/userThumbnail.png";
		return "/user-photos/"+this.id+"/"+this.photos;
	}
}
