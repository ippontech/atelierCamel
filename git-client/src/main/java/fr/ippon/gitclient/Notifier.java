package fr.ippon.gitclient;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.jgit.revwalk.RevCommit;

public class Notifier implements CommitListener {

	public void notify(RevCommit commit) {
		try {
			sendCommit(commit);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void sendCommit(RevCommit commit) throws JMSException {
		
		System.out.println("Send commit " + commit.getName() + " to ActiveMQ");
		
		ConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory("tcp://localhost:61616");
		
        Connection connection = connectionFactory.createConnection();
        try {
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("commit");
			
			MessageProducer producer = session.createProducer(queue);
			
			Message message = session.createObjectMessage(commit);
//			Message message = session.createTextMessage(commit.getName());
			
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
