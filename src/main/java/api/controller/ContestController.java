package api.controller;

import api.entity.ContestEntity;
import api.models.request.ContestRequest;
import api.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contests")
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;

    @PostMapping("/{userId}")
    public ContestEntity createContest(@PathVariable Long userId, @RequestBody ContestRequest request) {
        return contestService.createContest(userId, request);
    }
}
