package com.elhadjium.PMBackend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elhadjium.PMBackend.entity.InvitationToProject;

public interface InvitationToProjectDAO extends JpaRepository<InvitationToProject, Long>{
	@Query("select ip from InvitationToProject ip where ip.guest.id = :guestId and ip.project.id = :projectId")
	List<InvitationToProject> findByProjectIdAndGuestId(@Param("projectId") long projectId,
														@Param("guestId") long guestId);
}
