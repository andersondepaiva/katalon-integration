package br.com.andersondepaiva.katalonintegration.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.business.interfaces.ITestSuiteBusiness;
import br.com.andersondepaiva.katalonintegration.dto.TestSuiteDto;
import br.com.andersondepaiva.katalonintegration.model.CustomFile;
import br.com.andersondepaiva.katalonintegration.model.TestSuite;
import br.com.andersondepaiva.katalonintegration.repository.ITestSuiteRepository;
import br.com.andersondepaiva.katalonintegration.utils.io.FileUtil;

@Service
public class TestSuiteBusiness extends Business<TestSuite, String, TestSuiteDto> implements ITestSuiteBusiness {

	private final FileUtil fileUtil;
	private final String extension = ".groovy";
	private final String basePathTestSuites = "Test Suites";

	@Autowired
	public TestSuiteBusiness(ITestSuiteRepository baseRepository, FileUtil fileUtil) {
		super(baseRepository);
		this.fileUtil = fileUtil;
	}

	public List<TestSuiteDto> getAll(String path) {
		List<CustomFile> folders = fileUtil
				.listFilesAndSubDirectories(Joiner.on(File.separator).join(path, basePathTestSuites), extension);

		if (folders == null || folders.size() == 0)
			throw new BusinessException("Diretório vazio");

		List<TestSuiteDto> retorno = BuildTestSuite(folders);

		return retorno;
	}

	private List<TestSuiteDto> BuildTestSuite(List<CustomFile> folders) {
		List<TestSuiteDto> retorno = new ArrayList<TestSuiteDto>();
		folders.forEach(folder -> {
			TestSuiteDto dto = modelMapper.map(folder, TestSuiteDto.class);

			List<String> paths = Arrays.asList(dto.getPath().split(Pattern.quote(File.separator)));

			int indexTestSuite = paths.indexOf(basePathTestSuites);
			StringBuilder name = new StringBuilder();
			for (int i = indexTestSuite; i < paths.size(); i++) {
				name.append(paths.get(i));
				if (i + 1 != paths.size())
					name.append(File.separator);
			}

			dto.setName(name.toString().replace(extension, ""));
			retorno.add(dto);
		});

		return retorno;
	}
}
