package br.com.andersondepaiva.katalonintegration.business.interfaces;

import java.util.List;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonintegration.dto.TestSuiteDto;
import br.com.andersondepaiva.katalonintegration.model.TestSuite;

public interface ITestSuiteBusiness extends IBusiness<TestSuite, String, TestSuiteDto> {
	List<TestSuiteDto> getAll(String path);
}
