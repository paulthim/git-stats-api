package org.gitstats.api.repository;

import org.gitstats.api.models.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Iterable<Project> findByOwner_IdOrderByCreatedAtAsc(Long ownerId);
    Optional<Project> findOneByIdAndOwner_Id(Long id, Long ownerId);
}
