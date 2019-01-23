package br.com.andersondepaiva.katalonintegration.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IProfileBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProfileDto;
import br.com.andersondepaiva.katalonintegration.model.CustomFile;
import br.com.andersondepaiva.katalonintegration.model.Profile;
import br.com.andersondepaiva.katalonintegration.repository.IProfileRepository;
import br.com.andersondepaiva.katalonintegration.utils.io.FileUtil;

@Service
public class ProfileBusiness extends Business<Profile, String, ProfileDto> implements IProfileBusiness {

	private final FileUtil fileUtil;

	@Autowired
	public ProfileBusiness(IProfileRepository baseRepository, FileUtil fileUtil) {
		super(baseRepository);
		this.fileUtil = fileUtil;
	}

	public List<ProfileDto> getAll(String path) {
		List<CustomFile> folders = fileUtil.listFiles(Joiner.on(File.separator).join(path, "Profiles"), ".glbl");
		List<ProfileDto> retorno = new ArrayList<ProfileDto>();
		folders.forEach(folder -> {
			retorno.add(modelMapper.map(folder, ProfileDto.class));
		});

		return retorno;
	}
}
