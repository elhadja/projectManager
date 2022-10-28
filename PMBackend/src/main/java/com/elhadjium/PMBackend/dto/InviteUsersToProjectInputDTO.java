package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;

public class InviteUsersToProjectInputDTO implements DTOValidator {
	private long guestId;
	private long authorId;
	
	public InviteUsersToProjectInputDTO() {
		
	}
	
	public InviteUsersToProjectInputDTO(long guestId, long authorId) {
		super();
		this.guestId = guestId;
		this.authorId = authorId;
	}

	public long getGuestId() {
		return guestId;
	}

	public void setGuestId(long guestId) {
		this.guestId = guestId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	@Override
	public void validate() {
		if (guestId == authorId) {
			throw new PMInvalidInputDTO(null, "guestId and Author Id must be different");
		}
		
	}
}
