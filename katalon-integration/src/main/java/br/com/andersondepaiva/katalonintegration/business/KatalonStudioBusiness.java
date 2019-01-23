package br.com.andersondepaiva.katalonintegration.business;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonintegration.model.KatalonStudio;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IKatalonStudioProducer;
import br.com.andersondepaiva.katalonintegration.properties.AppProperties;
import br.com.andersondepaiva.katalonintegration.repository.IKatalonStudioRepository;

@Service
public class KatalonStudioBusiness extends Business<KatalonStudio, String, KatalonStudioDto>
		implements IKatalonStudioBusiness {

	private final AppProperties appProperties;
	private final IKatalonStudioProducer katalonStudioProducer;

	@Autowired
	public KatalonStudioBusiness(IKatalonStudioRepository baseRepository, AppProperties appProperties, IKatalonStudioProducer katalonStudioProducer) {
		super(baseRepository);
		this.appProperties = appProperties;
		this.katalonStudioProducer = katalonStudioProducer;
	}

	@Override
	public KatalonStudio save(KatalonStudio objModel) {

		if (!new File(objModel.getPath()).exists())
			throw new BusinessException("Katalon Studio Path not exists");

		if (!new File(objModel.getWorkPath()).exists())
			throw new BusinessException("Work Path not exists");

		return super.save(objModel);
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
	
	@Override
	protected void sendEventFillUserChange(KatalonStudioDto dto) {
		katalonStudioProducer.sendMessage(katalonStudioProducer.katalonStudioExchange().getName(),
				katalonStudioProducer.getRoutingKeyCmdFillUserChange(), dto);

	}
}
