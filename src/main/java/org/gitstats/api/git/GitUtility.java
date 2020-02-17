package org.gitstats.api.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitstats.api.models.Project;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class GitUtility {
    public static Git getGit(Project project) throws IOException {
        return new Git(getRepository(project));
    }

    public static Repository getRepository(Project project) throws IOException {
        return new RepositoryBuilder().setGitDir(new File(project.getLocalPath())).build();
    }

    public static void fetch(Project project) throws IOException, GitAPIException {
        Git git = new Git(getRepository(project));
        git.fetch().call();
    }

    public static Git clone(Project project) throws GitAPIException {
        UUID uuid = UUID.randomUUID();
        String localPath = String.format("/repos/%s_", uuid.toString());
        project.setLocalPath(localPath);
        Git git = Git.cloneRepository()
            .setURI(project.getRemoteUrl())
            .setDirectory(new File(localPath))
            .call();
        project.setLocalPath(String.format("%s/.git",localPath));
        return git;
    }

    public static Date getLatestCommitDate(Git git) throws IOException, GitAPIException {
        Date latestCommitDate = new Date(0);
        Iterable<RevCommit> commits = git.log().all().call();
        for (RevCommit commit : commits) {
            Date commitDate = new Date(commit.getCommitTime() * 1000L);
            if (latestCommitDate.before(commitDate)) {
                latestCommitDate = commitDate;
            }
        }
        return latestCommitDate;
    }

    public static boolean deleteRepoPath(Project project) {
        File file = new File(project.getLocalPath());
        boolean result = FileSystemUtils.deleteRecursively(file);
        return result;
    }
}