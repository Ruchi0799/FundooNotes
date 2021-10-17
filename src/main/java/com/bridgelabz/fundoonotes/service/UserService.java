package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.configuration.ApplicationConfig;
import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.UpdatePasswordDTO;
import com.bridgelabz.fundoonotes.dto.UserDTO;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.exception.FundooException;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.utils.EmailService;
import com.bridgelabz.fundoonotes.utils.S3service;
import com.bridgelabz.fundoonotes.utils.TokenService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private S3service s3service;
	
	
	
	@Override
	public User register(UserDTO userDTO) {
		
		//PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		Optional<User>isUserPresent=getUser(userDTO.getEmail());
		
		if(isUserPresent.isPresent()) {
			throw new FundooException(HttpStatus.CONFLICT.value(),"User email already present");
		}
		User user=new User();
		BeanUtils.copyProperties(userDTO, user);
		user.setIsEmailVerified(false);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		User  savedUser=userRepository.save(user);
		String token =tokenService.createToken(savedUser.getId(),new Date(System.currentTimeMillis()+(180*1000)));
		System.out.println("token is "+token);
		boolean isEmailSent=emailService.sendMail(savedUser.getEmail(),"pracman07@gmail.com", "Email verifiaction link", "http://localhost:8080/user/verify-email/"+token);
		if(!isEmailSent) {
			throw new FundooException(HttpStatus.BAD_REQUEST.value(),"User is registered, but error while sending mail");
		}
		return savedUser;
	}
	
	public Optional<User> getUser(String email){
		return userRepository.findByEmail(email);
	}

	@Override
	public String login(LoginDTO loginDTO) {
		// TODO Auto-generated method stub
		Optional<User> isUserPresent=getUser(loginDTO.getEmail());
		if(!isUserPresent.isPresent()) {
			throw new FundooException(HttpStatus.NOT_FOUND.value(),"Ëmailid is not registered");
		}
		if(!(isUserPresent.get().getEmail().equals(loginDTO.getEmail()) && passwordEncoder.matches(loginDTO.getPassword(),isUserPresent.get().getPassword())))
		{
			throw new FundooException(HttpStatus.BAD_REQUEST.value(),"Email or Password is wrong");
		}
		return tokenService.createToken(isUserPresent.get().getId());
	}

	@Override
	public void verifyEmail(String token) {
		Long userId=tokenService.decodeToken(token);
		User user=userRepository.findById(userId).orElseThrow(()-> new FundooException(HttpStatus.NOT_FOUND.value(),"User not Found"));
		// TODO Auto-generated method stub
		user.setIsEmailVerified(true);
		userRepository.save(user);
	}

	@Override
	public void forgotPassword(String email) {
		// TODO Auto-generated method stub
		User user=getUser(email).orElseThrow(()-> new FundooException(HttpStatus.NOT_FOUND.value(),"User not Found"));
		String token =tokenService.createToken(user.getId(),new Date(System.currentTimeMillis()+(180*1000)));
		boolean isEmailSent=emailService.sendMail(user.getEmail(),"pracman07@gmail.com", "Forgot password link", "http://localhost:8080/user/reset-password/"+token);
		if(!isEmailSent) {
			throw new FundooException(HttpStatus.BAD_REQUEST.value()," error while sending mail");
		}
	}

	@Override
	public void resetPassword(String token, String password) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(tokenService.decodeToken(token)).orElseThrow(()-> new FundooException(HttpStatus.NOT_FOUND.value(),"User not Found"));
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return  userRepository.findAll();
		
	}

	@Override
	public void updatePassword(String token, UpdatePasswordDTO updatePasswordDTO) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(tokenService.decodeToken(token)).orElseThrow(()-> new FundooException(HttpStatus.NOT_FOUND.value(),"User not Found"));
		 if(!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword()))
		 {
			 throw new FundooException(HttpStatus.UNAUTHORIZED.value(),"Old and new password is not matching");
		 }
		 user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
		 userRepository.save(user);
	}

	@Override
	public String uploadProfilePic(String token, MultipartFile file) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(tokenService.decodeToken(token)).orElseThrow(()-> new FundooException(HttpStatus.NOT_FOUND.value(),"User not Found"));
		String url=s3service.fileUpload(file, "profile pictures",String.valueOf(user.getId()));
		user.setProfileURL(url);
		userRepository.save(user);
		return url;
	}
}
