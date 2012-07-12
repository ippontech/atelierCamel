package fr.ippon.scmconnector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteConfig extends RouteBuilder {
	
	private static final Logger log = LoggerFactory.getLogger(RouteConfig.class);

	@Override
	public void configure() throws Exception {
		
		onException(HttpOperationFailedException.class)
			.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
					System.out.println("Error: " + exception.getStatusCode() + " " + exception.getStatusText());
					System.out.println("Reason: " + exception.getResponseBody());
				}
			});
		
        from("activemq:queue:commit").setExchangePattern(ExchangePattern.InOnly)
        	.filter(new Predicate() {
				public boolean matches(Exchange exchange) {
					Object message = exchange.getIn().getBody();
					if (message instanceof RevCommit) {
						return true;
					}
					log.info("Unexpected commit type: " + message.getClass().getName());
					return false;
				}
        	})
        	// TODO : utiliser un bean
        	.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					RevCommit commit = (RevCommit) exchange.getIn().getBody();
					System.out.println();
					System.out.println("commit " + commit.getName());
					PersonIdent author = commit.getAuthorIdent();
					System.out.println("Author: " + author.getName() + " <" + author.getEmailAddress() + ">");
					System.out.println();
					System.out.println(commit.getFullMessage());
				}
        	})
        	// TODO : utiliser un bean
        	.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					Message message = exchange.getIn();
					RevCommit gitCommit = message.getBody(RevCommit.class);
					Commit commit = new Commit();
					commit.setName(gitCommit.getName());
					commit.setAuthorEmail(gitCommit.getAuthorIdent().getEmailAddress());
					commit.setMessage(gitCommit.getFullMessage());
					message.setBody(commit);
				}
        	})
        	.process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					Message message = exchange.getIn();
					Commit commit = message.getBody(Commit.class);
					
					Matcher matcher = Pattern.compile("#([0-9]+)").matcher(commit.getMessage());
					if (matcher.find()) {
						Long issueNumber = Long.valueOf(matcher.group(1));
						log.info("Issue tag found [" + issueNumber + "] in commit: " + commit.getName());
						message.setHeader("issueNumber", issueNumber);
					} else {
						log.info("No issue tag found in commit: " + commit.getName());
					}
				}
        	})
        	.filter(simple("${header.issueNumber} != null"))
        	.marshal().json(JsonLibrary.Jackson)
        	.setHeader(Exchange.HTTP_URI, simple("http://localhost:9000/json/issues/${header.issueNumber}/comments"))
        	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
        	.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        	.to("http://dummyhost");
	}

}
