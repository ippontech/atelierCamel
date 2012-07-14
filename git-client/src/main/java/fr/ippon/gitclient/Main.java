package fr.ippon.gitclient;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Main {

	private static final String SAMPLE_REPO_PATH = "../sampleGitRepo/.git";

	public static void main(String[] args) throws Exception {
		Repository repository = getSampleRepo();
		new Poller(repository, new Notifier()).run();
	}

	public static Repository getSampleRepo() throws IOException {
		try {
			return getRepository(SAMPLE_REPO_PATH);
		} catch(RepositoryNotFoundException e) {
			throw new RuntimeException(
					"Le repo d'exemple n'existe pas encore. " +
					"Vous pouvez le créer via le script shell initSampleGitRepo.sh " +
					"à la racine du projet", e);
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
