package br.com.andersondepaiva.katalonexecutor.dto;

import br.com.andersondepaiva.katalonexecutor.model.FileType;
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
public class ProfileDto extends FileDto {
	@Setter(AccessLevel.NONE)
	private final FileType fileType = FileType.PROFILE;
}
