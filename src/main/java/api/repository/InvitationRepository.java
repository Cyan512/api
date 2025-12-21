package api.repository;

import api.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {
    Optional<InvitationEntity> findByInviteToken(String token);
}
