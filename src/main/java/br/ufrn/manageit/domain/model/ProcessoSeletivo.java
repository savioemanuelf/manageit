package br.ufrn.manageit.domain.model;

import br.ufrn.manageit.domain.enumeration.SituacaoProcessoSeletivo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
public class ProcessoSeletivo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nomeProcesso;
    private Integer anoProcesso;
    private String tipoProcesso;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private SituacaoProcessoSeletivo situacao;

    public ProcessoSeletivo(SituacaoProcessoSeletivo situacao,
                            LocalDate dataFim, LocalDate dataInicio,
                            String tipoProcesso, Integer anoProcesso,
                            String nomeProcesso, UUID id) {
        this.situacao = situacao;
        this.dataFim = dataFim;
        this.dataInicio = dataInicio;
        this.tipoProcesso = tipoProcesso;
        this.anoProcesso = anoProcesso;
        this.nomeProcesso = nomeProcesso;
        this.id = id;
    }

    public ProcessoSeletivo() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

    public Integer getAnoProcesso() {
        return anoProcesso;
    }

    public void setAnoProcesso(Integer anoProcesso) {
        this.anoProcesso = anoProcesso;
    }

    public String getTipoProcesso() {
        return tipoProcesso;
    }

    public void setTipoProcesso(String tipoProcesso) {
        this.tipoProcesso = tipoProcesso;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public SituacaoProcessoSeletivo getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoProcessoSeletivo situacao) {
        this.situacao = situacao;
    }
}
