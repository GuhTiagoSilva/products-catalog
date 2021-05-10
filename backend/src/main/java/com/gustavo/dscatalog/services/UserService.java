package com.gustavo.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.dscatalog.dto.RoleDTO;
import com.gustavo.dscatalog.dto.UserDTO;
import com.gustavo.dscatalog.dto.UserInsertDTO;
import com.gustavo.dscatalog.dto.UserUpdateDTO;
import com.gustavo.dscatalog.entities.Role;
import com.gustavo.dscatalog.entities.User;
import com.gustavo.dscatalog.repositories.RoleRepository;
import com.gustavo.dscatalog.repositories.UserRepository;
import com.gustavo.dscatalog.services.exceptions.DatabaseException;
import com.gustavo.dscatalog.services.exceptions.ResourceNotFoundException;


@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
		Page<User> list = repository.findAll(pageRequest);
		return list.map(user -> new UserDTO(user));
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User user = new User();
		copyDtoToEntity(dto, user);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user = repository.save(user);
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User user = repository.getOne(id);
			copyDtoToEntity(dto, user);
			user = repository.save(user);
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity Not Found");
		}
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> user = repository.findById(id);
		User entity = user.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
		return new UserDTO(entity);
	}

	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database Exception");
		}
	}

	private void copyDtoToEntity(UserDTO dto, User user) {

		user.setEmail(dto.getEmail());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());

		user.getRoles().clear();

		for (RoleDTO roleDTO : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDTO.getId());
			user.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("User Not Found: " + username);
			throw new UsernameNotFoundException("Email not found");
		}

		logger.info("User Found: " + username);

		return user;
	}
}
