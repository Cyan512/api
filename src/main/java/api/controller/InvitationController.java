package api.controller;

import api.entity.InvitationEntity;
import api.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/send")
    public InvitationEntity invite(
            @RequestParam Long contestId,
            @RequestParam Long userId
    ) {
        return invitationService.inviteUser(contestId, userId);
    }

    @PostMapping("/accept")
    public void accept(@RequestParam String token) {
        invitationService.acceptInvitation(token);
    }
}
