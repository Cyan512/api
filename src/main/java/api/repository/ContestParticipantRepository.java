package api.repository;

import api.entity.ContestParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestParticipantRepository extends JpaRepository<ContestParticipantEntity, Long> {
}
