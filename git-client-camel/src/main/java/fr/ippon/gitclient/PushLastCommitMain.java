package fr.ippon.gitclient;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Ignore;
import org.junit.Test;

public class PushLastCommitMain {

	public static void main(String[] args) throws Exception  {
			
		CamelContext context = new DefaultCamelContext();
		context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		
		CamelContext camelContext = context;
		camelContext.start();
		
		Iterable<RevCommit> commits = new Git(Main.getSampleRepo()).log().setMaxCount(1).call();
		RevCommit lastCommit = commits.iterator().next();
		
		ProducerTemplate template = camelContext.createProducerTemplate();
		template.sendBody("activemq:queue:commit", lastCommit);
		
		camelContext.stop();
	}
	
}
