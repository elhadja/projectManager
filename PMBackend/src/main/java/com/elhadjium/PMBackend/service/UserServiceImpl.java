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
import com.elhadjium.PMBackend.dao.InvitationToProjectDAO;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.UserAccount;
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
	
	@Autowired
	private InvitationToProjectDAO invitationToProjectDAO;
	
	private BCryptPasswordEncoder passwordEncodere = new BCryptPasswordEncoder();

	@Override
	public Long signup(UserAccount user) {
		if (userDAO.findByPseudo(user.getPseudo()) != null || userDAO.findByEmail(user.getEmail()) != null) {
			// TODO use message manager
			throw new PMEntityExistsException(messageSource.getMessage("msgErrorUserAlreadExists", null, LocaleContextHolder.getLocale()));
		}

		user.setPassword(passwordEncodere.encode(user.getPassword()));
		return userDAO.save(user).getId();
	}

	@Override
	public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
		try {
			UserAccount u = userDAO.findByEmail(userIdentifier);
			if (u == null) {
				u = userDAO.findByPseudo(userIdentifier);
			}
			return new CustomUserDetailsImpl(userIdentifier, u.getPassword(), u.getId(), (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>());
		} catch (Exception e) {
			throw new UsernameNotFoundException("user not found");
		}
	}
	
	@Transactional
	public Long CreateUserProject(Long userId, Project project) {
		if (projectDAO.findByName(project.getName()) != null) {
			throw new PMEntityExistsException(messageSource.getMessage("msgErrorProjectAlreadyExists", null, LocaleContextHolder.getLocale()));
		}
		
		try {
			UserAccount user = userDAO.findById(userId).get();
			project.addManager(user);
			Backlog backlog = new Backlog();
			backlog.setProject(project);
			project.setBacklog(backlog);
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
	
	public List<InvitationToProject> getUserInvitationToProject(long guestId) {
		return invitationToProjectDAO.findByGuestId(guestId);
	}
	
	@Transactional
	public void acceptInvitationToProjects(String[] projectIds, Long userId) {
		for (String invitationId: projectIds) {
			InvitationToProject invitation =  invitationToProjectDAO.findById(Long.valueOf(invitationId)).get();
			invitation.getGuest().addProject(invitation.getProject());
			invitation.getGuest().removeInvitationToProject(invitation);
			invitation.getProject().removeInvitation(invitation);
		}
	}
	
	public static void throwUserNotFoundException(Long userId, MessageSource messageSource) {
			throw new PMEntityNotExistsException(messageSource.getMessage("msgErrorEntityNotFound", 
																			new Object[] {messageSource.getMessage("user", null, LocaleContextHolder.getLocale()), userId} ,
																			LocaleContextHolder.getLocale()));
	}

	@Override
	public List<UserAccount> getUsersByCriteria(GetUsersByCriteriaInputDTO input) {
		return userDAO.getUsersByCriteria(input.getPseudo(), input.getFirstname(), input.getLastname());
	}

	@Override
	@Transactional
	public void cancelInvitationToProjects(String[] invitationsIds, Long valueOf) {
		for (String invitationId: invitationsIds) {
			InvitationToProject invitation =  invitationToProjectDAO.findById(Long.valueOf(invitationId)).get();
			invitation.getGuest().removeInvitationToProject(invitation);
			invitation.getProject().removeInvitation(invitation);
		}
	}
}
