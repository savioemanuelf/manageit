package br.ufrn.manageit.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CadastrarMovimentacaoDTO(
        @NotNull
        UUID setorDestinoId,

        @NotNull
        UUID usuarioDestinoId,


        @NotEmpty
        Set<UUID> itensId,

        String observacao
) {
}
