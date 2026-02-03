package br.ufrn.manageit.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarUsuarioDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull UUID pessoaId
) {}
