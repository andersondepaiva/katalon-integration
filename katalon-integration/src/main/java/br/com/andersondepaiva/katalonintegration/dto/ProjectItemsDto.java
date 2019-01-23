package br.com.andersondepaiva.katalonintegration.dto;

import java.util.List;

import br.com.andersondepaiva.core.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectItemsDto extends BaseDto {
	private List<TestSuiteDto> testSuites;
	private List<ProfileDto> profiles;
	private List<String> browsers;
}
