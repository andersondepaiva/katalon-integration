package br.com.andersondepaiva.katalonexecutor.business.interfaces;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonexecutor.model.KatalonStudio;

public interface IKatalonStudioBusiness extends IBusiness<KatalonStudio, String, KatalonStudioDto> {
	String getPathKatalonStudio();

	String getWorkPath();
}
