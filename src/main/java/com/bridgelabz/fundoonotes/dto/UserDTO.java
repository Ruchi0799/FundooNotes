package com.bridgelabz.fundoonotes.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import com.bridgelabz.fundoonotes.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	@NotBlank(message="FirstName cannot be blank")	
private String firstName;
	
	@NotBlank(message="Lastname cannot be blank")
	private String lastName;
	
	
	@NotBlank(message="Mobile Number cannot be blank")
	private String mobileNumber;
	
	@NotBlank(message="Email cannot be blank")
	private String email;
	
	@NotBlank(message="Password cannot be blank")
	private String password;
	
}
