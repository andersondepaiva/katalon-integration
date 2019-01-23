package br.com.andersondepaiva.katalonexecutor.business.interfaces;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.ProjectDto;
import br.com.andersondepaiva.katalonexecutor.model.Project;

public interface IProjectBusiness extends IBusiness<Project, String, ProjectDto> {
	void syncProject(Project project);
}
