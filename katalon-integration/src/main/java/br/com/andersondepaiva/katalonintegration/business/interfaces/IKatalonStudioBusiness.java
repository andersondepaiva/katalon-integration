package br.com.andersondepaiva.katalonintegration.business.interfaces;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonintegration.model.KatalonStudio;

public interface IKatalonStudioBusiness extends IBusiness<KatalonStudio, String, KatalonStudioDto> {
	String getPathKatalonStudio();

	String getWorkPath();
}
