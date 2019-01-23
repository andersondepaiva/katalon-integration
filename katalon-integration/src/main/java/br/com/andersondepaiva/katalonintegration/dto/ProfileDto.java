package br.com.andersondepaiva.katalonintegration.dto;

import br.com.andersondepaiva.katalonintegration.model.FileType;
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
