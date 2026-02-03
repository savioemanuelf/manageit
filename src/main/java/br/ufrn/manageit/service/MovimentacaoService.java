package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.dto.CadastrarMovimentacaoDTO;
import br.ufrn.manageit.domain.enumeration.StatusItem;
import br.ufrn.manageit.domain.enumeration.StatusMovimentacao;
import br.ufrn.manageit.domain.model.Item;
import br.ufrn.manageit.domain.model.Movimentacao;
import br.ufrn.manageit.domain.model.Setor;
import br.ufrn.manageit.domain.model.Usuario;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.infra.exception.RegraNegocioException;
import br.ufrn.manageit.repository.ItemRepository;
import br.ufrn.manageit.repository.MovimentacaoRepository;
import br.ufrn.manageit.repository.SetorRepository;
import br.ufrn.manageit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MovimentacaoService {
    private final MovimentacaoRepository movimentacaoRepository;
    private final SetorRepository setorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemRepository itemRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, SetorRepository setorRepository, UsuarioRepository usuarioRepository, ItemRepository itemRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.setorRepository = setorRepository;
        this.usuarioRepository = usuarioRepository;
        this.itemRepository = itemRepository;
    }

    private boolean itemDisponivel(Item item) {
        return item.getStatus().equals(StatusItem.DISPONIVEL);
    }

    @Transactional
    public Movimentacao cadastrarMovimentacao(CadastrarMovimentacaoDTO dto) {
        Setor setorDestino = setorRepository.findById(dto.setorDestinoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                   "Setor não encontrado"
                ));

        Usuario usuarioDestino = usuarioRepository.findById(dto.usuarioDestinoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado"
                ));

        Usuario usuarioPermitivo = usuarioRepository.findById(dto.usuarioPermitivoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário permissivo não encontrado"));

        List<Item> itens = itemRepository.findAllById(dto.itensId());

        if(itens.size() != dto.itensId().size()) {
            throw new RecursoNaoEncontradoException(
                    "Um ou mais itens não encontrados"
            );
        }

        for(Item item : itens) {
            if(!itemDisponivel(item)) {
                String nome = item.getNome();
                String patrimonioItem = item.getPatrimonio();
                StatusItem status = item.getStatus();
                throw new RegraNegocioException(
                  "O item " + nome + ", " + "de patrimônio " + patrimonioItem +
                          " não pode ser solicitado pois está com o status" + status
                );
            }
            item.setStatus(StatusItem.EMPRESTADO);
        }
        Set<Item> itensSet = new HashSet<>(itens);
        Movimentacao movimentacao = new Movimentacao(
                setorDestino,
                usuarioDestino,
                usuarioPermitivo,
                itensSet,
                dto.observacao()
        );
        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.findAll();
    }

    public Movimentacao buscarMovimentacaoPorId(UUID id) {
        return movimentacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                  "Movimentação não encontrada para o id: " + id
                )
                );
    }

    @Transactional
    public Movimentacao confirmarMovimentacao(UUID id) {
        Movimentacao movimentacao = buscarMovimentacaoPorId(id);

        if (movimentacao.getStatus() != StatusMovimentacao.PENDENTE) {
            throw new RegraNegocioException(
                    "Apenas movimentações PENDENTES podem ser confirmadas"
            );
        }

        movimentacao.setStatus(StatusMovimentacao.CONFIRMADA);
        return movimentacao;
    }

    @Transactional
    public Movimentacao cancelarMovimentacao(UUID id) {
        Movimentacao movimentacao = buscarMovimentacaoPorId(id);

        if (movimentacao.getStatus() != StatusMovimentacao.PENDENTE) {
            throw new RegraNegocioException(
                    "Apenas movimentações PENDENTES podem ser canceladas"
            );
        }

        movimentacao.setStatus(StatusMovimentacao.CANCELADA);

        for (Item item : movimentacao.getItens()) {
            item.setStatus(StatusItem.DISPONIVEL);
        }

        return movimentacao;
    }


    public List<Movimentacao> listarMovimentacoesPorStatus(StatusMovimentacao status) {
        List<Movimentacao> movimentacoes = movimentacaoRepository.findAllByStatus(status);

        if(movimentacoes.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há movimentações com o status: " + status);
        }

        return movimentacoes;
    }

    @Transactional
    public Movimentacao concluirMovimentacao(UUID id) {
        Movimentacao movimentacao = buscarMovimentacaoPorId(id);

        if (movimentacao.getStatus() != StatusMovimentacao.CONFIRMADA) {
            throw new RegraNegocioException(
                    "Apenas movimentações CONFIRMADAS podem ser concluídas"
            );
        }

        for(Item item : movimentacao.getItens()) {
            item.setStatus(StatusItem.DISPONIVEL);
        }
        movimentacao.setStatus(StatusMovimentacao.CONCLUIDA);

        return movimentacaoRepository.save(movimentacao);
    }

    public void deletarMovimentacao(UUID id) {
        Movimentacao movimentacao = buscarMovimentacaoPorId(id);
        movimentacaoRepository.deleteById(id);
    }
}
