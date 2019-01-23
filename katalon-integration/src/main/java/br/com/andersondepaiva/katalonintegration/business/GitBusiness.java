package br.com.andersondepaiva.katalonintegration.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IGitBusiness;

@Service
public class GitBusiness implements IGitBusiness {

	public String clonePull(String uri, String dir, String username, String password, String branch) {
		File dirFile = new File(dir);
		try {
			clonePull(uri, username, password, dirFile, branch);
			return dir;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private Git clonePull(String uri, String username, String password, File dirFile, String branch)
			throws GitAPIException, WrongRepositoryStateException, InvalidConfigurationException,
			InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException,
			TransportException, IOException {
		if (Strings.isNullOrEmpty(branch)) {
			branch = "master";
		}

		if (dirFile.exists()) {
			Git git = mountGitFromFolder(dirFile.getAbsolutePath());
			git.pull().setRemoteBranchName(branch)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
			return git;
		} else {
			return Git.cloneRepository().setURI(uri).setBranch(branch)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
					.setDirectory(dirFile).call();
		}
	}

	public List<String> getAllBranches(String uri, String dir, String username, String password) {
		File dirFile = new File(dir);
		try {
			Git git = clonePull(uri, username, password, dirFile, null);
			List<Ref> refBranches = git.branchList().setListMode(ListMode.REMOTE).call();
			List<String> branches = buildBranchesName(refBranches);
			return branches;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<String> getAllBranches(String uri, String username, String password) {
		try {
			Collection<Ref> refBranches = Git.lsRemoteRepository().setHeads(true).setRemote(uri)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,
							new String(Base64.decodeBase64(password.getBytes()))))
					.call();

			if (refBranches == null) {
				return new ArrayList<String>();
			}

			return buildBranchesName(new ArrayList<Ref>(refBranches));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private List<String> buildBranchesName(List<Ref> refBranches) {
		List<String> branches = new ArrayList<String>();

		if (refBranches == null) {
			return branches;
		}

		refBranches.forEach(ref -> {
			String branchName = ref.getName();
			if (ref.getName().contains("/")) {
				List<String> names = Arrays.asList(ref.getName().split("/"));
				branchName = names.get(names.size() - 1);
			}

			branches.add(branchName);
		});
		return branches;
	}

	private Git mountGitFromFolder(String absolutePath) throws IOException {
		Repository localRepo = new FileRepository(Joiner.on(File.separator).join(absolutePath, ".git"));
		return new Git(localRepo);
	}

}
