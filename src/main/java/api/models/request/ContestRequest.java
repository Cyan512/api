package api.models.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContestRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
