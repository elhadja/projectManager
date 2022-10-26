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
import com.elhadjium.PMBackend.common.MessageManager;
import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.dao.InvitationToProjectDAO;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.dto.UserDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;
import com.elhadjium.PMBackend.exception.PMEntityNotExistsException;
import com.elhadjium.PMBackend.util.JavaUtil;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private InvitationToProjectDAO invitationToProjectDAO;
	
	@Autowired
	private MessageManager messageManager;
	
	private BCryptPasswordEncoder passwordEncodere = new BCryptPasswordEncoder();
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public Long signup(UserAccount user) {
		if (userDAO.findByPseudo(user.getPseudo()) != null || userDAO.findByEmail(user.getEmail()) != null) {
			// TODO use message manager
			throw new PMEntityExistsException(messageManager.getTranslation(MessageManager.ENTITY_ALREADY_EXISTS));
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
			// TODO use a specific message
			throw new UsernameNotFoundException(messageManager.getTranslation(MessageManager.ENTITY_NOT_FOUND_ERROR));
		}
	}
	
	@Transactional
	public Long CreateUserProject(Long userId, Project project) {
		if (projectDAO.findByName(project.getName()) != null) {
			throw new PMEntityExistsException(messageManager.getTranslation(MessageManager.PROJECT_ALREADY_EXISTS));
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
			throw new PMEntityNotExistsException(null, MessageManager.getEntityNotFoundDetails(userId));
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
			throw new PMEntityNotExistsException(null, MessageManager.getEntityNotFoundDetails(userId));
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
	
	@Transactional
	public void updateUserPassword(String userEmail, String newUserPassword) {
		UserAccount user = userDAO.findByEmail(userEmail);
		if (user != null) {
			user.setPassword(this.passwordEncodere.encode(newUserPassword));
		}
	}
	
	@Override
	public void sendSimpleEmail(String targetEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("elhadja007@gmail.com");
        message.setTo(targetEmail); 
        message.setSubject(subject); 
        message.setText(content);
        javaMailSender.send(message);
	}

	@Override
	public UserAccount getUserById(Long userId) {
		return userDAO.findById(userId).get();
	}

	@Override
	@Transactional
	public UserAccount updateUser(UserAccount user) {
		UserAccount userToUpdate = userDAO.getById(user.getId());
		if (!JavaUtil.isNullOrEmpty(user.getFirstName())) {
			userToUpdate.setFirstName(user.getFirstName());
		}
		if (!JavaUtil.isNullOrEmpty(user.getLastName())) {
			userToUpdate.setLastName(user.getLastName());
		}
		if (!JavaUtil.isNullOrEmpty(user.getPseudo())) {
			userToUpdate.setPseudo(user.getPseudo());
		}

		userDAO.save(userToUpdate);
		return userToUpdate;
	}

	@Override
	@Transactional
	public void updateUserPassword(Long id, String newUserPassword) {
		UserAccount user = userDAO.findById(id).get();
		if (user != null) {
			user.setPassword(this.passwordEncodere.encode(newUserPassword));
		}
	}

	@Override
	@Transactional
	public void UpdateUserEmail(Long userId, String newEmail) {
		UserAccount user = userDAO.findById(userId).get();
		if (user != null) {
			user.setEmail(newEmail);
		}
	}
}
