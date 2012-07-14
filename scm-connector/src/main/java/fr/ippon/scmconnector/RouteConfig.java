package fr.ippon.scmconnector;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.jgit.revwalk.RevCommit;

public class RouteConfig extends RouteBuilder {
	
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
        	.filter().body(RevCommit.class)
        	.bean(new CommitLogger())
        	.bean(new CommitConverter())
        	.bean(new IssueNumberProcessor())
        	.filter(simple("${header.issueNumber} != null"))
        	.marshal().json(JsonLibrary.Jackson)
        	.setHeader(Exchange.HTTP_URI, simple("http://localhost:9000/json/issues/${header.issueNumber}/comments"))
        	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
        	.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        	.to("http://dummyhost");
	}

}
