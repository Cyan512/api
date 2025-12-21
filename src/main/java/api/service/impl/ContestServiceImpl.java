package api.service.impl;

import api.entity.ContestEntity;
import api.entity.UserEntity;
import api.models.request.ContestRequest;
import api.repository.ContestRepository;
import api.repository.UserRepository;
import api.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;

    @Override
    public ContestEntity createContest(Long userId, ContestRequest contestRequest) {
        UserEntity creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ContestEntity contest = new ContestEntity();
        contest.setTitle(contestRequest.getTitle());
        contest.setDescription(contestRequest.getDescription());
        contest.setStartDate(contestRequest.getStartDate());
        contest.setEndDate(contestRequest.getEndDate());
        contest.setCreatedBy(creator);
        return contestRepository.save(contest);
    }
}
