package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.enumeration.StatusItem;
import br.ufrn.manageit.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findByPatrimonio(String patrimonio);
    Page<Item> findByStatus(StatusItem status, Pageable pageable);
    Page<Item> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
