package fr.ippon.scmconnector;

import org.eclipse.jgit.revwalk.RevCommit;

public class CommitConverter {

	public Commit convert(RevCommit gitCommit) {
		Commit commit = new Commit();
		commit.setName(gitCommit.getName());
		commit.setAuthorEmail(gitCommit.getAuthorIdent().getEmailAddress());
		commit.setMessage(gitCommit.getFullMessage());
		return commit;
	}
}
