package br.com.andersondepaiva.katalonintegration.business.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProjectBaseDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectItemsDto;
import br.com.andersondepaiva.katalonintegration.model.Project;

public interface IProjectBusiness extends IBusiness<Project, String, ProjectDto> {
	List<ProjectDto> getAll();

	ProjectItemsDto getItemsProject(String id);

	Page<ProjectBaseDto> getAllWithoutGitAuthentication(ProjectDto filter, Pageable pageable);

	ProjectBaseDto customSave(ProjectDto dto) throws ReflectiveOperationException;
}
