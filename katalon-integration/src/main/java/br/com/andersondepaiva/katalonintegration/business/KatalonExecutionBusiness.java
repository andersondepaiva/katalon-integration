package br.com.andersondepaiva.katalonintegration.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.dto.Comparasion;
import br.com.andersondepaiva.core.dto.ParamDto;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonExecutionBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonintegration.model.KatalonExecution;
import br.com.andersondepaiva.katalonintegration.model.Profile;
import br.com.andersondepaiva.katalonintegration.model.Project;
import br.com.andersondepaiva.katalonintegration.model.StatusExecution;
import br.com.andersondepaiva.katalonintegration.model.TestSuite;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IKatalonExecutionProducer;
import br.com.andersondepaiva.katalonintegration.repository.IKatalonExecutionRepository;
import br.com.andersondepaiva.katalonintegration.utils.io.FileUtil;

@Service
public class KatalonExecutionBusiness extends Business<KatalonExecution, String, KatalonExecutionDto>
		implements IKatalonExecutionBusiness {

	private final IKatalonExecutionProducer katalonProducer;
	private final FileUtil fileUtil;

	@Autowired
	public KatalonExecutionBusiness(IKatalonExecutionRepository baseRepository,
			IKatalonExecutionProducer katalonProducer, FileUtil fileUtil) {
		super(baseRepository);
		this.katalonProducer = katalonProducer;
		this.fileUtil = fileUtil;
	}

	public Page<KatalonExecutionDto> getAll(String projectId, String requestedById, Pageable pageable) {

		List<ParamDto> params = new ArrayList<ParamDto>();

		if (!Strings.isNullOrEmpty(projectId)) {
			params.add(ParamDto.builder().comparasion(Comparasion.EQUALS).field("project.id").value(projectId).build());
		}

		if (!Strings.isNullOrEmpty(requestedById)) {
			params.add(ParamDto.builder().comparasion(Comparasion.EQUALS).field("incluidoPor.id").value(requestedById)
					.build());
		}

		return super.filterAndPaginate(params, pageable, new Sort(Sort.Direction.DESC, "dataInclusao"));
	}

	@Override
	public void scheduleExecutionTest(KatalonExecutionDto dto) throws IOException, ReflectiveOperationException {
		KatalonExecution model = modelMapper.map(dto, KatalonExecution.class);
		isValid(model);
		model.setStatusExecution(StatusExecution.QUEUED);
		model = super.save(model);
		katalonProducer.sendMessage(katalonProducer.katalonExchange().getName(),
				katalonProducer.getRoutingKeyCmdExecute(), modelMapper.map(model, KatalonExecutionDto.class));
	}

	public Resource getLogFileAsResource(String idKatalonExecution) {
		Optional<KatalonExecution> optionalKatalonExecution = baseRepository.findById(idKatalonExecution);

		if (!optionalKatalonExecution.isPresent())
			throw new BusinessException("Execution not found");

		KatalonExecution katalonExecution = optionalKatalonExecution.get();

		if (Strings.isNullOrEmpty(katalonExecution.getPathLogFile()))
			throw new BusinessException("Log file not Available");

		return fileUtil.loadAsResource(katalonExecution.getPathLogFile());
	}

	public Resource getReportTestAsResource(String idKatalonExecution) {
		Optional<KatalonExecution> optionalKatalonExecution = baseRepository.findById(idKatalonExecution);

		if (!optionalKatalonExecution.isPresent())
			throw new BusinessException("Execution not found");

		KatalonExecution katalonExecution = optionalKatalonExecution.get();

		if (Strings.isNullOrEmpty(katalonExecution.getPathReportTest()))
			throw new BusinessException("Report Test not Available");

		return fileUtil.loadAsResource(katalonExecution.getPathReportTest());
	}

	@Override
	protected void sendEventFillUserChange(KatalonExecutionDto dto) {
		katalonProducer.sendMessage(katalonProducer.katalonExchange().getName(),
				katalonProducer.getRoutingKeyCmdFillUserChange(), dto);
	}

	private void isValid(KatalonExecution model) {

		if (model == null)
			throw new BusinessException("Invalid parameters");

		if (model.getProfile() == null || Strings.isNullOrEmpty(model.getProfile().getName()))
			throw new BusinessException("Profile is required");

		if (model.getTestSuite() == null || Strings.isNullOrEmpty(model.getTestSuite().getName()))
			throw new BusinessException("Test Suite is required");

		if (model.getProject() == null || Strings.isNullOrEmpty(model.getProject().getPath()))
			throw new BusinessException("Project is required");

		if (Strings.isNullOrEmpty(model.getBrowser()))
			throw new BusinessException("Browser is required");

		verifyExecutionNotCompleted(model);
	}

	private void verifyExecutionNotCompleted(KatalonExecution model) {
		IKatalonExecutionRepository katalonRepository = (IKatalonExecutionRepository) super.baseRepository;

		verifyExists(katalonRepository.findOne(buildModelCriteria(model, StatusExecution.QUEUED)));

		verifyExists(katalonRepository.findOne(buildModelCriteria(model, StatusExecution.EXECUTING)));
	}

	private Example<KatalonExecution> buildModelCriteria(KatalonExecution model, StatusExecution statusExecution) {
		return Example
				.of(KatalonExecution.builder().profile(Profile.builder().name(model.getProfile().getName()).build())
						.project(Project.builder().path(model.getProject().getPath()).build())
						.testSuite(TestSuite.builder().name(model.getTestSuite().getName()).build())
						.statusExecution(statusExecution).build());
	}

	private void verifyExists(Optional<KatalonExecution> optionalKatalon) {
		if (optionalKatalon.isPresent())
			throw new BusinessException("There is already an execution in progress with these parameters");
	}

}
