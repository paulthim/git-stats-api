package org.gitstats.api.schedulingtasks;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gitstats.api.models.Project;
import org.gitstats.api.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;

@Component
public class ProjectRefresh {
    @Autowired
    private ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ProjectRefresh.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelay = 300000)
    public void refresh() {
        log.info("Refreshing projects....");
        Iterable<Project> projects = projectRepository.findAll();
        int projectsCount = 0;

        if (projects instanceof Collection) {
            projectsCount = ((Collection<?>) projects).size();
        }
        for (Project project : projects) {
            refreshProject(project);
        }

        log.info(String.format("Validating last commit for %s", projectsCount));
        log.info("Done.");
    }

    public void refreshProject(Project project) {
        log.info(String.format("Validating project %s", project.getName()));
        try {
            File repoDirectory = new File(String.format("/repos/%s", project.getName()));
            FileRepositoryBuilder builder = new FileRepositoryBuilder();

            Repository repository = builder.setGitDir(repoDirectory).readEnvironment().findGitDir().build();
            RevWalk revWalk = new RevWalk(repository);

            for (RevCommit commit : revWalk) {
                log.info(String.format("Latest commit at %s", commit.getCommitTime()));
            }
            repository.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
