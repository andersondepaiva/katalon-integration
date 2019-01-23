package br.com.andersondepaiva.katalonintegration.dto;

import br.com.andersondepaiva.katalonintegration.model.FileType;
import br.com.andersondepaiva.katalonintegration.model.SourceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectBaseDto extends FileDto {
	@Setter(AccessLevel.NONE)
	private final FileType fileType = FileType.PROJECT_PATH;
	private SourceType sourceType;
}
