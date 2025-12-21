package api.service;

import api.entity.ContestEntity;
import api.models.request.ContestRequest;

public interface ContestService {
    ContestEntity createContest(Long userId, ContestRequest contestRequest);
}
