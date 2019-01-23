package br.com.andersondepaiva.katalonexecutor.business;

import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IGitBusiness;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IProjectBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.ProjectDto;
import br.com.andersondepaiva.katalonexecutor.model.Project;
import br.com.andersondepaiva.katalonexecutor.model.SourceType;
import br.com.andersondepaiva.katalonexecutor.repository.IProjectRepository;

@Service
public class ProjectBusiness extends Business<Project, String, ProjectDto> implements IProjectBusiness {

	private final IGitBusiness gitBusiness;

	@Autowired
	public ProjectBusiness(IProjectRepository baseRepository, IGitBusiness gitBusiness) {
		super(baseRepository);
		this.gitBusiness = gitBusiness;
	}

	@Override
	public void syncProject(Project project) {
		if (project.getSourceType() == SourceType.GIT) {
			Optional<Project> optionalProject = baseRepository.findById(project.getId());
			if (optionalProject.isPresent()) {
				Project projectEntity = optionalProject.get();
				gitBusiness.clonePull(projectEntity.getGitAuthentication().getUri(), project.getPath(),
						projectEntity.getGitAuthentication().getUser(),
						new String(Base64.decodeBase64(projectEntity.getGitAuthentication().getPassword().getBytes())),
						projectEntity.getGitAuthentication().getBranch());
			}
		}

	}
}
