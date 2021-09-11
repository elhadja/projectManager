package com.elhadjium.PMBackend.service;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;
import com.elhadjium.PMBackend.exception.PMEntityNotExistsException;
import org.springframework.context.i18n.LocaleContextHolder;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private MessageSource messageSource;
	
	private BCryptPasswordEncoder passwordEncodere = new BCryptPasswordEncoder();

	@Override
	public Long signup(User user) {
		if (userDAO.findByPseudo(user.getPseudo()) != null || userDAO.findByEmail(user.getEmail()) != null) {
			throw new PMEntityExistsException(user.getEmail() + " or " + user.getPseudo() + "is already used");
		}

		user.setPassword(passwordEncodere.encode(user.getPassword()));
		return userDAO.save(user).getId();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User u = userDAO.findByEmail(username);
			return new CustomUserDetailsImpl(u.getEmail(), u.getPassword(), u.getId(), (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>());
		} catch (Exception e) {
			throw new UsernameNotFoundException("user not found");
		}
	}
	
	@Transactional
	public Long CreateUserProject(Long userId, Project project) {
		
		try {
			User user = userDAO.findById(userId).get();
			project.addManager(user);
			project = projectDAO.save(project);
			user.addProject(project);
		} catch (NoSuchElementException e) {
			throwUserNotFoundException(userId, messageSource);
		}
		
		return project.getId();
	}
	
	public List<Project> getUserProjects(Long userId) {
		List<Project> projects = new ArrayList<Project>();
		try {
			List<UserProject> userProjects = userDAO.findById(userId).get().getProjects();
			for (UserProject userProject: userProjects) {
				projects.add(userProject.getProject());
			}
		} catch (NoSuchElementException e) {
			throwUserNotFoundException(userId, messageSource);
		}

		return projects;
	}
	
	public static void throwUserNotFoundException(Long userId, MessageSource messageSource) {
			throw new PMEntityNotExistsException(messageSource.getMessage("msgErrorEntityNotFound", 
																			new Object[] {messageSource.getMessage("user", null, LocaleContextHolder.getLocale()), userId} ,
																			LocaleContextHolder.getLocale()));
	}
}
