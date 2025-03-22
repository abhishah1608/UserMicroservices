package com.smart.inventory.user_service.application.service;

import org.springframework.stereotype.Service;
import com.smart.inventory.user_service.application.mapper.UserMapper;
import com.smart.inventory.user_service.domain.model.User;
import com.smart.inventory.user_service.presentation.dto.UserDto;
import com.smart.inventory.user_service.presentation.request.UserRequest;
import com.smart.inventory.user_service.domain.repository.UserRepository;

@Service
public class UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	public UserService(UserMapper userMapper, UserRepository userRepository) {
		this.userMapper = userMapper;
		this.userRepository = userRepository;
	}

	@SuppressWarnings("finally")
	public UserDto createUser(UserRequest request) throws Exception {
		User user = userMapper.toDomain(request);
		User savedUser = null;
		try {
			savedUser = userRepository.save(user);
		} catch (Exception ex) {
			throw ex;
		}
		return userMapper.toDTO(savedUser);
	}

}
