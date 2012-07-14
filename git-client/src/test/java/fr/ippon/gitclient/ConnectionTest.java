package fr.ippon.gitclient;

import java.io.IOException;

import javax.jms.JMSException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

public class ConnectionTest {

	@Test
	public void testConnection() throws JMSException, NoHeadException, JGitInternalException, IOException {
			Iterable<RevCommit> commits = new Git(Main.getSampleRepo()).log().call();
			new Notifier().notify(commits.iterator().next());
	}
}
