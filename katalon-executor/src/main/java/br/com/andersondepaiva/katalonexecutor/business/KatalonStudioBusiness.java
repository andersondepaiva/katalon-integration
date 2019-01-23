package br.com.andersondepaiva.katalonexecutor.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonexecutor.model.KatalonStudio;
import br.com.andersondepaiva.katalonexecutor.properties.AppProperties;
import br.com.andersondepaiva.katalonexecutor.repository.IKatalonStudioRepository;

@Service
public class KatalonStudioBusiness extends Business<KatalonStudio, String, KatalonStudioDto>
		implements IKatalonStudioBusiness {

	private final AppProperties appProperties;

	@Autowired
	public KatalonStudioBusiness(IKatalonStudioRepository baseRepository, AppProperties appProperties) {
		super(baseRepository);
		this.appProperties = appProperties;
	}

	public String getPathKatalonStudio() {
		Optional<KatalonStudio> optionalKatalonStudio = baseRepository.findAllByExcluidoFalse().stream().findFirst();

		if (!optionalKatalonStudio.isPresent())
			return appProperties.getKatalonBasePath();

		return optionalKatalonStudio.get().getPath();
	}

	public String getWorkPath() {
		Optional<KatalonStudio> optionalKatalonStudio = baseRepository.findAllByExcluidoFalse().stream().findFirst();

		if (!optionalKatalonStudio.isPresent())
			return appProperties.getWorkPath();

		return optionalKatalonStudio.get().getWorkPath();
	}
}
