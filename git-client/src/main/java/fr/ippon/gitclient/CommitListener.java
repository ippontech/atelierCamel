package fr.ippon.gitclient;

import org.eclipse.jgit.revwalk.RevCommit;

public interface CommitListener {

	public void notify(RevCommit commit);
}
