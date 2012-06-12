package fr.ippon.gitclient;

import java.util.LinkedList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class Poller implements Runnable {
	
	private static final int WAIT_DURATION = 10;
	
	private final Repository repository;
	private final CommitListener listener;
	
	private String lastRecordedCommitName = null;

	public Poller(Repository repository, CommitListener listener) {
		this.repository = repository;
		this.listener = listener;
	}

	public void run() {
		System.out.println("Start polling repository: " + repository);
		while (true) {
			try {
				checkForNewCommits();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try { 
				Thread.sleep(WAIT_DURATION * 1000);
			} catch (InterruptedException e) {
			}
		}
	}

	private void checkForNewCommits() throws NoHeadException, JGitInternalException {
		for (RevCommit commit : getNewCommits()) {
			listener.notify(commit);
		}
	}

	private LinkedList<RevCommit> getNewCommits() throws NoHeadException, JGitInternalException {
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
}
