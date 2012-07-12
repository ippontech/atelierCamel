package fr.ippon.gitclient;

import java.io.File;
import java.io.IOException;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Repository repository = getSampleRepo();
		new Main().startCamel(repository);
	}


	private void startCamel(Repository repository) throws Exception {
        CamelContext context1 = new DefaultCamelContext();
		context1.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		CamelContext context = context1;
        
        context.addRoutes(new GitClientRouteConfig(repository));
        context.start();
        Thread.sleep(200000);
        context.stop();
	}	

	public static Repository getSampleRepo() {
		String gitDirPath = "../sampleGitRepo/.git";
		try {
			return getRepository(gitDirPath);
		} catch(IOException e) {
			e.printStackTrace();	
			throw new RuntimeException("Le repo d'exemple n'existe pas encore. Vous pouvez le créer via le script shell initSampleGitRepo.sh à la racine du projet");
		}
	}
	public static FileRepository getRepository(String gitDirPath)
			throws IOException {
		return new FileRepositoryBuilder()
			.setGitDir(new File(gitDirPath))
			.setMustExist(true)
			.build();
	}

}
