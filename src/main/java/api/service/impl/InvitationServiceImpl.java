package api.service.impl;

import api.entity.ContestEntity;
import api.entity.ContestParticipantEntity;
import api.entity.InvitationEntity;
import api.entity.UserEntity;
import api.repository.ContestParticipantRepository;
import api.repository.ContestRepository;
import api.repository.InvitationRepository;
import api.repository.UserRepository;
import api.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final ContestParticipantRepository participantRepository;

    @Override
    public InvitationEntity inviteUser(Long contestId, Long userId) {
        ContestEntity contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Concurso no existe"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));
        InvitationEntity invitation = new InvitationEntity();
        invitation.setContest(contest);
        invitation.setInvitedUser(user);
        invitation.setInviteToken(UUID.randomUUID().toString());

        return invitationRepository.save(invitation);
    }

    @Override
    public void acceptInvitation(String token) {
        InvitationEntity invitation = invitationRepository.findByInviteToken(token)
                .orElseThrow(() -> new RuntimeException("Invitación inválida"));

        invitation.setStatus("ACCEPTED");

        ContestParticipantEntity participant = new ContestParticipantEntity();
        participant.setContest(invitation.getContest());
        participant.setUser(invitation.getInvitedUser());

        participantRepository.save(participant);
        invitationRepository.save(invitation);
    }


}
