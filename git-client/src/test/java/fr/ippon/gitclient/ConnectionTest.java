package fr.ippon.gitclient;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

public class ConnectionTest {

	@Test
	public void testConnection() throws JMSException, NoHeadException, JGitInternalException, IOException {
		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        try {
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("commit");
			
			MessageProducer producer = session.createProducer(queue);
			
			Iterable<RevCommit> commits = new Git(Main.getSampleRepo()).log().call();
			
			Message message = session.createObjectMessage(commits.iterator().next());
			producer.send(message);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
