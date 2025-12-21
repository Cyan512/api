package api.service;

import api.entity.InvitationEntity;

public interface InvitationService {
    InvitationEntity inviteUser(Long contestId, Long userId);

    void acceptInvitation(String token);
}
