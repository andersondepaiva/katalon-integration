package br.com.andersondepaiva.katalonexecutor.business;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IGitBusiness;

@Service
public class GitBusiness implements IGitBusiness {

	public String clonePull(String uri, String dir, String username, String password, String branch) {
		File dirFile = new File(dir);
		try {
			if (dirFile.exists()) {
				Git git = mountGitFromFolder(dir);
				git.pull().setRemoteBranchName(branch)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
			} else {
				Git.cloneRepository().setURI(uri).setBranch(branch)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
						.setDirectory(dirFile).call();
			}

			return dir;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

	}

	public Git mountGitFromFolder(String absolutePath) throws IOException {
		Repository localRepo = new FileRepository(Joiner.on(File.separator).join(absolutePath, ".git"));
		return new Git(localRepo);
	}

}
