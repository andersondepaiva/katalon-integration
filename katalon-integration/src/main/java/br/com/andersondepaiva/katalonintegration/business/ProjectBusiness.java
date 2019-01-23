package br.com.andersondepaiva.katalonintegration.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.dto.Comparasion;
import br.com.andersondepaiva.core.dto.ParamDto;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IGitBusiness;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IProfileBusiness;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IProjectBusiness;
import br.com.andersondepaiva.katalonintegration.business.interfaces.ITestSuiteBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProjectBaseDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectItemsDto;
import br.com.andersondepaiva.katalonintegration.model.CustomFile;
import br.com.andersondepaiva.katalonintegration.model.Project;
import br.com.andersondepaiva.katalonintegration.model.SourceType;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IProjectProducer;
import br.com.andersondepaiva.katalonintegration.repository.IProjectRepository;
import br.com.andersondepaiva.katalonintegration.utils.io.FileUtil;

@Service
public class ProjectBusiness extends Business<Project, String, ProjectDto> implements IProjectBusiness {

	private final FileUtil fileUtil;
	private final IGitBusiness gitBusiness;
	private final IProfileBusiness profileBusiness;
	private final ITestSuiteBusiness testSuiteBusiness;
	private final List<String> browsers = Arrays.asList("Chrome (headless)", "Edge", "Firefox (headless)", "IE",
			"Safari");
	private final IKatalonStudioBusiness katalonStudioBusiness;
	private final IProjectProducer projectProducer;

	@Autowired
	public ProjectBusiness(IProjectRepository baseRepository, FileUtil fileUtil, IGitBusiness gitBusiness,
			IProfileBusiness profileBusiness, ITestSuiteBusiness testSuiteBusiness,
			IKatalonStudioBusiness katalonStudioBusiness, IProjectProducer projectProducer) {
		super(baseRepository);
		this.fileUtil = fileUtil;
		this.gitBusiness = gitBusiness;
		this.profileBusiness = profileBusiness;
		this.testSuiteBusiness = testSuiteBusiness;
		this.katalonStudioBusiness = katalonStudioBusiness;
		this.projectProducer = projectProducer;
	}

	public List<ProjectDto> getAll() {
		List<CustomFile> folders = fileUtil.listFolders(katalonStudioBusiness.getWorkPath());
		List<ProjectDto> retorno = new ArrayList<ProjectDto>();
		folders.forEach(folder -> {
			retorno.add(modelMapper.map(folder, ProjectDto.class));
		});

		return retorno;
	}

	public Page<ProjectBaseDto> getAllWithoutGitAuthentication(ProjectDto filter, Pageable pageable) {

		Page<Project> dataSet = super.filterAndPaginateModel(buildParameters(filter), pageable, null);

		if (dataSet.hasContent()) {
			List<ProjectBaseDto> dtos = new ArrayList<ProjectBaseDto>();
			dataSet.getContent().stream().forEach(model -> {
				dtos.add((ProjectBaseDto) modelMapper.map(model, ProjectBaseDto.class));
			});

			return new PageImpl<ProjectBaseDto>(dtos, pageable, dataSet.getTotalElements());
		}

		return new PageImpl<ProjectBaseDto>(new ArrayList<ProjectBaseDto>());
	}

	public ProjectBaseDto customSave(ProjectDto dto) throws ReflectiveOperationException {
		Project model = modelMapper.map(dto, Project.class);
		isValid(model);
		boolean isInsert = Strings.isNullOrEmpty(model.getId());
		try {
			model = super.save(model);

			if (model.getSourceType() == SourceType.GIT) {
				model.setPath(gitBusiness.clonePull(model.getGitAuthentication().getUri(),
						Joiner.on(File.separator).join(katalonStudioBusiness.getWorkPath(), model.getId()),
						model.getGitAuthentication().getUser(),
						new String(Base64.decodeBase64(model.getGitAuthentication().getPassword().getBytes())),
						model.getGitAuthentication().getBranch()));

				model = super.save(model);
			}
		} catch (Exception ex) {
			if (isInsert && !Strings.isNullOrEmpty(model.getId()))
				baseRepository.deleteById(model.getId());

			throw ex;
		}

		return modelMapper.map(model, ProjectBaseDto.class);
	}

	public ProjectItemsDto getItemsProject(String id) {
		Optional<Project> optionalProject = baseRepository.findById(id);

		if (!optionalProject.isPresent())
			throw new BusinessException("Project not found");

		Project project = optionalProject.get();

		return ProjectItemsDto.builder().profiles(profileBusiness.getAll(project.getPath()))
				.testSuites(testSuiteBusiness.getAll(project.getPath())).browsers(this.browsers).build();

	}

	private List<ParamDto> buildParameters(ProjectDto filters) {
		List<ParamDto> params = new ArrayList<ParamDto>();

		params.add(ParamDto.builder().comparasion(Comparasion.EQUALS).field("excluido").value(false).build());

		if (filters == null) {
			return params;
		}

		if (!Strings.isNullOrEmpty(filters.getName())) {
			params.add(ParamDto.builder().comparasion(Comparasion.LIKE).field("name").value(filters.getName()).build());
		}

		return params;
	}

	private void isValid(Project project) {
		if (project == null)
			throw new BusinessException("Invalid parameters");

		if (Strings.isNullOrEmpty(project.getName()))
			throw new BusinessException("Name is required");

		if (project.getSourceType() == null)
			throw new BusinessException("Source Type is required");

		if (project.getSourceType() == SourceType.FILE_SYSTEM && Strings.isNullOrEmpty(project.getPath()))
			throw new BusinessException("Path is required for Source Type FILE SYSTEM");

		IProjectRepository projectRepository = (IProjectRepository) super.baseRepository;

		if (Strings.isNullOrEmpty(project.getName())
				&& projectRepository.countByNameIgnoreCaseAndExcluidoFalse(project.getName()) > 0)
			throw new BusinessException("Project's name already exists");

		if (project.getSourceType() == SourceType.GIT) {
			if (project.getGitAuthentication() == null
					|| Strings.isNullOrEmpty(project.getGitAuthentication().getPassword())
					|| Strings.isNullOrEmpty(project.getGitAuthentication().getUser())
					|| Strings.isNullOrEmpty(project.getGitAuthentication().getUri()))
				throw new BusinessException("Authentication data is required for Source Type GIT");
		}
	}

	@Override
	protected void sendEventFillUserChange(ProjectDto dto) {
		projectProducer.sendMessage(projectProducer.projectExchange().getName(),
				projectProducer.getRoutingKeyCmdFillUserChange(), dto);

	}

}
