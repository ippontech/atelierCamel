package fr.ippon.scmconnector;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueNumberProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(IssueNumberProcessor.class);
	
	public void process(Message message) throws Exception {
		Commit commit = message.getBody(Commit.class);
		
		Long issueNumber = IssueNumberFinder.find(commit.getMessage());
		if (issueNumber != null) {
			log.info("Issue tag found [" + issueNumber + "] in commit: " + commit.getName());
			message.setHeader("issueNumber", issueNumber);
		} else {
			log.info("No issue tag found in commit: " + commit.getName());
		}
	}
}
