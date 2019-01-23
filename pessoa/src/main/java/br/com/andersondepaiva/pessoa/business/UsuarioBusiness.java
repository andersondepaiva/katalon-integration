package br.com.andersondepaiva.pessoa.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.dto.Comparasion;
import br.com.andersondepaiva.core.dto.ParamDto;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.core.infra.security.AuthService;
import br.com.andersondepaiva.pessoa.business.interfaces.IUsuarioBusiness;
import br.com.andersondepaiva.pessoa.dto.UsuarioDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioFiltroDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioPassDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioWithAuthDto;
import br.com.andersondepaiva.pessoa.model.Usuario;
import br.com.andersondepaiva.pessoa.repository.IUsuarioRepository;

@Service
public class UsuarioBusiness extends Business<Usuario, String, UsuarioWithAuthDto> implements IUsuarioBusiness {

	@Autowired
	private AuthService authService;

	@Autowired
	public UsuarioBusiness(IUsuarioRepository baseRepository) {
		super(baseRepository);
	}

	@Override
	public UsuarioDto saveUser(UsuarioWithAuthDto dto) throws ReflectiveOperationException, NoSuchAlgorithmException {
		Usuario userModel = modelMapper.map(dto, Usuario.class);

		isValid(userModel);

		if (Strings.isNullOrEmpty(userModel.getId())) {
			userModel.setSenha(authService.encode(new String(Base64.decodeBase64(userModel.getSenha().getBytes()))));
		}

		return modelMapper.map(super.save(userModel), UsuarioDto.class);
	}

	public UsuarioDto updateProfile(UsuarioDto dto) {
		IUsuarioRepository repository = (IUsuarioRepository) baseRepository;
		Optional<Usuario> optionalUsuario = repository.findById(dto.getId());

		if (!optionalUsuario.isPresent()) {
			throw new BusinessException("User not found");
		}

		Update updateUser = new Update();
		updateUser.set("pessoa.nome", dto.getPessoa().getNome());
		String idUserRequest = super.getUserIdRequest();
		if (!Strings.isNullOrEmpty(idUserRequest)) {
			updateUser.set("alteradoPor.id", idUserRequest);
			updateUser.set("alteradoPor.nome", null);
		}

		executeAtomicUpdate(updateUser, dto.getId());

		if (!Strings.isNullOrEmpty(idUserRequest)) {
			sendEventFillUserChange(modelMapper.map(dto, UsuarioWithAuthDto.class));
		}

		return dto;
	}

	public Page<UsuarioDto> getAllWithoutAuthentication(UsuarioFiltroDto filtros, Pageable pageable) {
		Page<Usuario> dataSet = super.filterAndPaginateModel(buildParameters(filtros), pageable, null);

		if (dataSet.hasContent()) {
			List<UsuarioDto> dtos = new ArrayList<UsuarioDto>();
			dataSet.getContent().stream().forEach(model -> {
				dtos.add(modelMapper.map(model, UsuarioDto.class));
			});

			return new PageImpl<UsuarioDto>(dtos, pageable, dataSet.getTotalElements());
		}

		return new PageImpl<UsuarioDto>(new ArrayList<UsuarioDto>());
	}

	public void updatePassword(UsuarioPassDto dto) {
		IUsuarioRepository repository = (IUsuarioRepository) baseRepository;
		Optional<Usuario> optionalUsuario = repository.findById(dto.getId());

		if (!optionalUsuario.isPresent()) {
			throw new BusinessException("User not found");
		}

		Usuario user = optionalUsuario.get();
		String newPass = new String(Base64.decodeBase64(dto.getSenha().getBytes()));

		if (!authService.matches(new String(Base64.decodeBase64(dto.getOldSenha().getBytes())), user.getSenha())) {
			throw new BusinessException("Current Password is wrong");
		}

		user.setSenha(authService.encode(newPass));

		super.save(user);
	}

	protected void sendEventFillUserChange(UsuarioWithAuthDto dto) {
		super.fillUserChange(dto.getId());
	}

	protected br.com.andersondepaiva.core.dto.UsuarioDto findUserChange(String id) {
		Optional<Usuario> optional = baseRepository.findById(id);

		if (optional.isPresent()) {
			return modelMapper.map(optional.get(), br.com.andersondepaiva.core.dto.UsuarioDto.class);
		}

		return null;
	}

	private List<ParamDto> buildParameters(UsuarioFiltroDto filters) {
		List<ParamDto> params = new ArrayList<ParamDto>();

		params.add(ParamDto.builder().comparasion(Comparasion.EQUALS).field("excluido").value(false).build());

		if (filters == null) {
			return params;
		}

		if (!Strings.isNullOrEmpty(filters.getNome())) {
			params.add(ParamDto.builder().comparasion(Comparasion.LIKE).field("pessoa.nome").value(filters.getNome())
					.build());
		}

		return params;
	}

	private boolean isValid(Usuario user) throws BusinessException {
		if (Strings.isNullOrEmpty(user.getId())) {
			List<Usuario> usuarios = ((IUsuarioRepository) baseRepository).findByLoginAndExcluidoFalse(user.getLogin());

			if (usuarios != null && !usuarios.isEmpty()) {
				throw new BusinessException("Email already exists");
			}
		}

		return true;
	}
}
