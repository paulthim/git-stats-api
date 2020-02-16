package org.gitstats.api.repository;

import org.gitstats.api.models.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
