package com.bridgelabz.fundoonotes.controller;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.UpdatePasswordDTO;
import com.bridgelabz.fundoonotes.dto.UserDTO;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.exception.FundooException;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.IUserService;
import com.bridgelabz.fundoonotes.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	
//	@Autowired
//	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@Valid @RequestBody UserDTO userDTO,BindingResult result){
		if(result.hasErrors())
		{
			throw new FundooException(HttpStatus.UNPROCESSABLE_ENTITY.value(),result.getAllErrors().get(0).getDefaultMessage());
		}
		User user=userService.register(userDTO);
		return new ResponseEntity<Response>(new Response(HttpStatus.CREATED.value(),"User Registered Successfully",user), HttpStatus.CREATED);
		
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDTO loginDTO){
		String token=userService.login(loginDTO);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"User login Successfully",token), HttpStatus.OK );
		
	}
	
	@GetMapping("/verify-email/{token}")
	public ResponseEntity<Response> verifyEmail(@PathVariable String token)
	{
		userService.verifyEmail(token);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"User Email verified Sucessfully",token),HttpStatus.OK);
	}
	
	@GetMapping("/forgot-password/{email}")
	public ResponseEntity<Response> forgotPassword(@PathVariable String email){
		userService.forgotPassword(email);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"Reset link sent Sucessfully",""),HttpStatus.OK);
	}
	
	
	@PutMapping("/reset-password")
	public ResponseEntity<Response> resetPassword(@RequestHeader String token,@RequestParam String email){
		userService.forgotPassword(email);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"Password reseted Sucessfully",""),HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<Response> getAllUsers(){
		List<User> users=userService.getAllUsers();
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"User retrieved Sucessfully",users),HttpStatus.OK);
	}
	
	@PutMapping("/update-password")
	public ResponseEntity<Response> updatePassword(@RequestHeader String token,@RequestBody UpdatePasswordDTO updatePasswordDTO){
		userService.updatePassword(token, updatePasswordDTO);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"Password updated Sucessfully",""),HttpStatus.OK);
	}
	
	@PostMapping("/profile-pic")
	public ResponseEntity<Response> profilePic(@RequestHeader String token,@RequestParam MultipartFile file){
		String url=userService.uploadProfilePic(token, file);
		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"Profile pic updated Sucessfully",url),HttpStatus.OK);
	}
	
	
}
