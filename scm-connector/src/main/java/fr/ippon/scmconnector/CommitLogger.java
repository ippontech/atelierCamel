package fr.ippon.scmconnector;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

public class CommitLogger {

	public void log(RevCommit commit) {
		System.out.println();
		System.out.println("commit " + commit.getName());
		PersonIdent author = commit.getAuthorIdent();
		System.out.println("Author: " + author.getName() + " <" + author.getEmailAddress() + ">");
		System.out.println();
		System.out.println(commit.getFullMessage());
	}
}
