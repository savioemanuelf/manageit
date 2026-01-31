package br.ufrn.manageit.domain.dto;

import br.ufrn.manageit.domain.enumeration.StatusItem;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusItemDTO(
    @NotNull
    StatusItem status
){}
