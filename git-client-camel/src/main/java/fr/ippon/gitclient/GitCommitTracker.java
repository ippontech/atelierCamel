package fr.ippon.gitclient;

import java.util.LinkedList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitCommitTracker {

	public GitCommitTracker(Repository repository) {
		this.repository = repository;
	}

	private final Repository repository;
	private String lastRecordedCommitName = null;

	public LinkedList<RevCommit> getNewCommits() throws NoHeadException, JGitInternalException {
		LinkedList<RevCommit> newCommits = new LinkedList<RevCommit>();

		Iterable<RevCommit> commits = git().log().call();
		if (containsRecordedCommit()) {
			RevCommit lastCommit = null;
			for (RevCommit commit : commits) {
				if (lastCommit == null) {
					lastCommit = commit;
				}
				if (isLastRecordedCommit(commit)) {
					break;
				}
				newCommits.addFirst(commit);
			}
			markAsLastCommit(lastCommit);
		} else {
			markAsLastCommit(commits.iterator().next());
		}
		return newCommits;
	}

	private void markAsLastCommit(RevCommit commit) {
		if (commit != null) {
			lastRecordedCommitName = commit.getName();
		}
	}

	private boolean isLastRecordedCommit(RevCommit commit) {
		return commit.getName().equals(lastRecordedCommitName);
	}

	private boolean containsRecordedCommit() {
		return lastRecordedCommitName != null;
	}

	private Git git() {
		return new Git(repository);
	}

	public Repository getRepository() {
		return repository;
	}
	
}
