package fr.ippon.gitclient;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.jgit.lib.Repository;

public class GitClientRouteConfig extends RouteBuilder  {

	private Repository repository;

	public GitClientRouteConfig(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void configure() throws Exception {
	
		from("timer:ticker?period=20s")
			.log("GitClientRouteConfig : tick n°${in.header.CamelTimerCounter}")
			.bean(new GitCommitTracker(repository),"getNewCommits")
			.split().body()
				.log("GitClientRouteConfig : received commit ${in.body.name} - sending it to ActiveMQ ...")
				.to("activemq:queue:commit")
				.end()
			.log("GitClientRouteConfig : END tick n°${in.header.CamelTimerCounter} ")
			;
	}

}
