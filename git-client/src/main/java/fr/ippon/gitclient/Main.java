package fr.ippon.gitclient;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Poller(getRepository("/home/jcdelmas/dev/test-git-repo/.git"), new Notifier()).run();
		} catch (IOException e) {
			throw new RuntimeException(e);
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
