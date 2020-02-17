package org.gitstats.api.schedulingtasks;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.gitstats.api.git.GitUtility;
import org.gitstats.api.models.Project;
import org.gitstats.api.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Component
public class ProjectRefresh {
    @Autowired
    private ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ProjectRefresh.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelay = 5000)
    public void refresh() {
        log.info("Refreshing projects....");
        Iterable<Project> projects = projectRepository.findAll();
        int projectsCount = 0;

        if (projects instanceof Collection) {
            projectsCount = ((Collection<?>) projects).size();
        }
        log.info(String.format("Validating last commit for %s", projectsCount));
        for (Project project : projects) {
            refreshProject(project);
        }

        log.info("Done.");
    }

    public void refreshProject(Project project) {
        log.info(String.format("Validating project %s", project.getName()));
        try {
            Git git = GitUtility.getGit(project);
            git.fetch().call();

            Date latestCommitDate = GitUtility.getLatestCommitDate(git);

            if (project.getLatestCommitDate() == null || latestCommitDate.after(project.getLatestCommitDate())) {
                PullResult pullResult = git.pull().call();
                if (!pullResult.isSuccessful()) {
                    log.error(String.format("Error encountered pulling latest for project %s", project.getName()));
                } else {
                    project.setLatestCommitDate(latestCommitDate);
                    projectRepository.save(project);
                }
            }

        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
