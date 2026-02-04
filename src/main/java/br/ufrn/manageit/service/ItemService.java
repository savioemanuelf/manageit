package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.dto.AtualizarStatusItemDTO;
import br.ufrn.manageit.domain.enumeration.StatusItem;
import br.ufrn.manageit.domain.model.Item;
import br.ufrn.manageit.infra.exception.BuscaInvalidaException;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.infra.exception.RegraNegocioException;
import br.ufrn.manageit.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item salvar(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> listar() {
        return itemRepository.findAll();
    }

    public Item listarPorId(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Não há item registrado com o id " + id
                )
            );
    }

    public Item buscarPorPatrimonio(String patrimonio) {
        if(patrimonio == null || patrimonio.isEmpty()) {
            throw new BuscaInvalidaException(
                    "É necessário informar o patrimônio"
            );
        }
        return itemRepository.findByPatrimonio(patrimonio)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Não há item registrado com o patrimônio " + patrimonio
                )
            );
    }

    public Item atualizarItem(UUID id, Item item) {
        Item itemExistente = itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                   "Item não encontrado para o id " + id
                ));

        if(!itemExistente.getPatrimonio().equals(item.getPatrimonio())) {
            throw new RegraNegocioException(
                    "Não é possível alterar o patrimônio de um item"
            );
        }
        itemExistente.setDescricao(item.getDescricao());
        itemExistente.setNome(item.getNome());
        itemExistente.setStatus(item.getStatus());

        return itemRepository.save(itemExistente);
    }

    public Item atualizarStatusItem(UUID id, AtualizarStatusItemDTO status) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Item não encontrado para o id " + id
                ));
        item.setStatus(status.status());
        return itemRepository.save(item);
    }

    public void remover(UUID id) {
        if (!itemRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Item não encontrado para o id " + id
            );
        }
        itemRepository.deleteById(id);
    }

    public Page<Item> listarPaginado(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> listarPorStatusPaginado(StatusItem status, Pageable pageable) {
        return itemRepository.findByStatus(status, pageable);
    }

    public Page<Item> buscarPorNomePaginado(String nome, Pageable pageable) {
        return itemRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public List<Item> listarPorStatus(StatusItem status) {
        return itemRepository.findByStatus(status);
    }
}
