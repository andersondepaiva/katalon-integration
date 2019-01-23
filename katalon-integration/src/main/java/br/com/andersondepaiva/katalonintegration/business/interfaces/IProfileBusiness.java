package br.com.andersondepaiva.katalonintegration.business.interfaces;

import java.util.List;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProfileDto;
import br.com.andersondepaiva.katalonintegration.model.Profile;

public interface IProfileBusiness extends IBusiness<Profile, String, ProfileDto> {
	List<ProfileDto> getAll(String path);
}
