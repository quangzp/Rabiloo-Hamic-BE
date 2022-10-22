package com.project.service.impl;

import com.project.dto.UserDto;
import com.project.entity.RoleEntity;
import com.project.entity.UserEntity;
import com.project.repository.UserRepository;
import com.project.request.UserRequest;
import com.project.response.UserResponse;
import com.project.security.CustomUserDetail;
import com.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserResponse findAll() {;
		List<UserEntity> entities = repository.findAll();

		UserResponse response = new UserResponse();
		if(entities.isEmpty()){
			response.setMessage("answers is null");
			response.setStatusCode(HttpStatus.NOT_FOUND);
		}else {
			List<UserDto> dtos = new ArrayList<>();
			entities.forEach(e -> dtos.add(mapper.map(e, UserDto.class)));

			response.setDtos(dtos);
			response.setMessage("OK");
			response.setStatusCode(HttpStatus.OK);
		}


		return response;
	}

	@Override
	public UserResponse findOne(Long id) {
		Optional<UserEntity> questionEntityOptional = repository.findById(id);

		UserResponse response = new UserResponse();

		if(!questionEntityOptional.isPresent()){
			response.setMessage("answer not found");
			response.setStatusCode(HttpStatus.NOT_FOUND);
		}else {
			UserDto dto = mapper.map(questionEntityOptional.get(),UserDto.class);
			response.setDto(dto);
			response.setMessage("ok");
			response.setStatusCode(HttpStatus.OK);
		}

		return response;
	}

	@Override
	public UserResponse create(UserRequest req) {
		UserEntity user;

		user = repository.findByUserName(req.getUserName());

		UserResponse response = new UserResponse();
		if(user != null){
			response.setMessage("User name is exits");
			response.setStatusCode(HttpStatus.BAD_REQUEST);
		}

		user = mapper.map(req,UserEntity.class);
		RoleEntity urole = new RoleEntity("ROLE_USER");
		user.addRole(urole);
		user.setPassword(passwordEncoder.encode(req.getPassword()));

		UserDto dto = mapper.map(repository.save(user),UserDto.class);

		response.setDto(dto);
		response.setMessage("ok");
		response.setStatusCode(HttpStatus.OK);

		return response;
	}

	@Override
	public UserResponse update(UserRequest req) {
		/*UserEntity user;
		user = repository.findByUserName(req.getUserName());
		if (user == null){
			return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
		}
		user = mapper.map(req,UserEntity.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		UserDto dto = mapper.map(repository.save(user),UserDto.class);
		dto.setPassword(req.getPassword());
		return new ResponseEntity<>(dto,HttpStatus.OK);*/
		return null;
	}


	@Override
	public UserResponse delete(Long id) {
		Optional<UserEntity> optional = repository.findById(id);

		UserResponse response = new UserResponse();
		if (!optional.isPresent()) {
			response.setMessage("Answer is not found");
			response.setStatusCode(HttpStatus.NOT_FOUND);
		} else {
			UserEntity entity = optional.get();
			entity.setDeleted(true);
			entity.setActive(0);
			UserDto dto = mapper.map(repository.save(entity), UserDto.class);

			response.setMessage("OK");
			response.setStatusCode(HttpStatus.OK);
		}

		return response;
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public UserEntity findByUserName(String userName) {
		return repository.findByUserName(userName);
	}

	@Override
	public UserResponse updateUserInfo(UserRequest request) {

		UserResponse response = new UserResponse();
		if(request.getId() == null) {
			response.setMessage("User is not found");
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return response;
		}

		Optional<UserEntity> userOptional = repository.findById(request.getId());

		if(!userOptional.isPresent()) {
			response.setMessage("User is not found");
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return response;
		}

		UserEntity user = userOptional.get();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setGender(request.getGender());
		user.setBirthDay(new Date(request.getBirthDay()));
		user.setModifiedDate(new Date());
		user.setModifiedBy(user.getId());

		response.setDto(mapper.map(repository.save(user),UserDto.class));
		response.setMessage("OK");
		response.setStatusCode(HttpStatus.OK);
		return response;
	}

	@Override
	public UserResponse getCurrent() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserResponse response = new UserResponse();
		if (auth != null && auth.isAuthenticated()) {
			String userName = ((CustomUserDetail) auth.getPrincipal()).getUsername();
			UserEntity user = repository.findByUserName(userName);
			UserDto dto = mapper.map(repository.findByUserName(userName), UserDto.class);
			 response.setDto(mapper.map(repository.findByUserName(userName), UserDto.class));
			 response.setStatusCode(HttpStatus.OK);
			 response.setMessage("OK");
		}else {
			response.setMessage("Not authentication");
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
		}
		return response;
	}
}
