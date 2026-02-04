let movimentacoes = [];
let allMovimentacoes = [];
let itensDisponiveis = [];
let itensSelecionados = [];

document.addEventListener('DOMContentLoaded', function() {
    loadMovimentacoes();
    loadUserInfo();

    document.getElementById('movimentacaoFormElement').addEventListener('submit', function(e) {
        e.preventDefault();
        saveMovimentacao();
    });
});

async function loadSetoresParaSelect() {
    try {
        const response = await fetch('/setores');
        if (!response.ok) throw new Error('Erro ao carregar setores');

        const setores = await response.json();
        const select = document.getElementById('setorDestinoId');

        select.innerHTML = '<option value="">Selecione um setor...</option>';

        setores.forEach(setor => {
            const option = document.createElement('option');
            option.value = setor.id;
            option.textContent = setor.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar setores:', error);
    }
}
async function loadUsuariosParaSelect() {
    try {
        const response = await fetch('/usuarios/nomes');
        if (!response.ok) throw new Error('Erro ao carregar usuários');

        const usuarios = await response.json();
        const select = document.getElementById('usuarioDestinoId');

        select.innerHTML = '<option value="">Selecione um usuário...</option>';

        for (const [id, nome] of Object.entries(usuarios)) {
            const option = document.createElement('option');
            option.value = id;
            option.textContent = nome;
            select.appendChild(option);
        }
    } catch (error) {
        console.error('Erro ao carregar usuários:', error);
        showMessage('Erro ao carregar lista de usuários', 'error');
    }
}

function loadUserInfo() {
    const username = localStorage.getItem('username') || 'Usuário';
    document.getElementById('username').textContent = username;
}

async function loadMovimentacoes() {
    try {
        const response = await fetch('/movimentacoes');
        if (!response.ok) {
            const error = await response.json();
            throw new Error(`Erro ${response.status}: ${error.message}`);
        }

        allMovimentacoes = await response.json();
        movimentacoes = [...allMovimentacoes];

        renderMovimentacoes(movimentacoes);

    } catch (error) {
        showMessage('Erro ao carregar movimentações: ' + error.message, 'error');
    }
}

async function loadItensDisponiveis() {
    try {
        const response = await fetch('/itens/disponiveis');
        if (!response.ok) {
            throw new Error('Erro ao carregar itens disponíveis');
        }

        itensDisponiveis = await response.json();
        renderItensDisponiveis();

    } catch (error) {
        showMessage('Erro ao carregar itens: ' + error.message, 'error');
        document.getElementById('itensDisponiveisContainer').innerHTML =
            '<div class="error">Erro ao carregar itens disponíveis</div>';
    }
}

function renderItensDisponiveis() {
    const container = document.getElementById('itensDisponiveisContainer');

    if (itensDisponiveis.length === 0) {
        container.innerHTML = '<div class="no-items">Não há itens disponíveis no momento</div>';
        return;
    }

    let html = '<div class="itens-grid">';

    itensDisponiveis.forEach(item => {
        const isSelected = itensSelecionados.some(selected => selected.id === item.id);

        html += `
            <div class="item-card-selectable ${isSelected ? 'selected' : ''}" 
                 onclick="toggleItemSelection('${item.id}')">
                <div class="item-info">
                    <div class="item-name">${item.nome}</div>
                    <div class="item-patrimonio">${item.patrimonio}</div>
                    <div class="item-status">${getStatusText(item.status)}</div>
                </div>
                <input type="checkbox" 
                       class="checkbox" 
                       ${isSelected ? 'checked' : ''}
                       onclick="toggleItemSelection('${item.id}', event)">
            </div>
        `;
    });

    html += '</div>';
    container.innerHTML = html;
}

function toggleItemSelection(itemId, event) {
    if (event) {
        event.stopPropagation(); // Evita duplo clique
    }

    const item = itensDisponiveis.find(i => i.id === itemId);
    if (!item) return;

    const index = itensSelecionados.findIndex(i => i.id === itemId);

    if (index === -1) {
        // Adicionar item
        itensSelecionados.push({
            id: item.id,
            nome: item.nome,
            patrimonio: item.patrimonio
        });
    } else {
        // Remover item
        itensSelecionados.splice(index, 1);
    }

    // Atualizar visualização
    renderItensDisponiveis();
    updateSelectedItemsList();
}

function updateSelectedItemsList() {
    const container = document.getElementById('selectedItemsList');

    if (itensSelecionados.length === 0) {
        container.innerHTML = '<div class="no-items">Nenhum item selecionado</div>';
        return;
    }

    let html = '';
    itensSelecionados.forEach(item => {
        html += `
            <div class="selected-item-tag">
                ${item.nome} (${item.patrimonio})
                <button class="remove-btn" onclick="removeSelectedItem('${item.id}')">×</button>
            </div>
        `;
    });

    container.innerHTML = html;
}

function removeSelectedItem(itemId) {
    itensSelecionados = itensSelecionados.filter(item => item.id !== itemId);
    renderItensDisponiveis();
    updateSelectedItemsList();
}

function showCreateForm() {
    isEditing = false;
    itensSelecionados = [];

    document.getElementById('formTitle').textContent = 'Nova Movimentação';
    document.getElementById('movimentacaoFormElement').reset();
    document.getElementById('movimentacaoId').value = '';
    document.getElementById('movimentacaoForm').style.display = 'block';

    loadSetoresParaSelect();
    loadUsuariosParaSelect();
    loadItensDisponiveis();
    updateSelectedItemsList();
}

async function saveMovimentacao() {
    if (itensSelecionados.length === 0) {
        showMessage('Selecione pelo menos um item para a movimentação', 'error');
        return;
    }

    const movimentacaoData = {
        setorDestinoId: document.getElementById('setorDestinoId').value.trim(),
        usuarioDestinoId: document.getElementById('usuarioDestinoId').value.trim(),
        itensId: itensSelecionados.map(item => item.id),
        observacao: document.getElementById('observacao').value
    };

    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

    if (!uuidRegex.test(movimentacaoData.setorDestinoId)) {
        showMessage('UUID do setor destino inválido', 'error');
        return;
    }

    if (!uuidRegex.test(movimentacaoData.usuarioDestinoId)) {
        showMessage('UUID do usuário destino inválido', 'error');
        return;
    }


    try {
        const response = await fetch('/movimentacoes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movimentacaoData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao criar movimentação');
        }

        showMessage('Movimentação criada com sucesso!', 'success');
        hideForm();
        loadMovimentacoes();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

function renderMovimentacoes(movimentacoesToRender) {
    const tbody = document.getElementById('movimentacoesTableBody');
    tbody.innerHTML = '';

    if (movimentacoesToRender.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align: center; padding: 40px;">
                    Nenhuma movimentação encontrada
                </td>
            </tr>
        `;
        return;
    }

    movimentacoesToRender.forEach(movimentacao => {
        const tr = document.createElement('tr');

        const dataCriacao = new Date(movimentacao.dataCriacao || movimentacao.createdAt || Date.now());
        const dataFormatada = dataCriacao.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        const setorNome = movimentacao.setorDestino?.nome || 'Setor não informado';
        const usuarioDestinoNome = movimentacao.usuarioDestino?.nome || 'Usuário não informado';
        const qtdItens = movimentacao.itens?.length || 0;

        tr.innerHTML = `
            <td>${setorNome}</td>
            <td>${usuarioDestinoNome}</td>
            <td>${qtdItens} item(ns)</td>
            <td><span class="status status-${movimentacao.status}">${getStatusText(movimentacao.status)}</span></td>
            <td>${dataFormatada}</td>
            <td class="actions-cell">
                <button onclick="showDetails('${movimentacao.id}')" class="btn-action btn-details">Detalhes</button>
                ${getActionButtons(movimentacao)}
            </td>
        `;

        tbody.appendChild(tr);
    });
}

function getStatusText(status) {
    const statusMap = {
        'PENDENTE': 'Pendente',
        'CONFIRMADA': 'Confirmada',
        'CONCLUIDA': 'Concluída',
        'CANCELADA': 'Cancelada',
        'DISPONIVEL': 'Disponível',
        'EMPRESTADO': 'Emprestado',
        'MANUTENCAO': 'Manutenção'
    };
    return statusMap[status] || status;
}

function getActionButtons(movimentacao) {
    let buttons = '';

    switch(movimentacao.status) {
        case 'PENDENTE':
            buttons += `
                <button onclick="confirmarMovimentacao('${movimentacao.id}')" class="btn-action btn-confirm">Confirmar</button>
                <button onclick="cancelarMovimentacao('${movimentacao.id}')" class="btn-action btn-cancel">Cancelar</button>
            `;
            break;
        case 'CONFIRMADA':
            buttons += `
                <button onclick="concluirMovimentacao('${movimentacao.id}')" class="btn-action btn-complete">Concluir</button>
            `;
            break;
    }

    return buttons;
}

async function showDetails(id) {
    try {
        const response = await fetch(`/movimentacoes/${id}`);
        if (!response.ok) throw new Error('Erro ao carregar detalhes');

        const movimentacao = await response.json();

        document.getElementById('detailStatus').textContent = getStatusText(movimentacao.status);
        document.getElementById('detailStatus').className = `status status-${movimentacao.status}`;

        document.getElementById('detailSetor').textContent = movimentacao.setorDestino?.nome || 'Não informado';
        document.getElementById('detailUsuarioDestino').textContent =
            `${movimentacao.usuarioDestino?.nome || 'Não informado'} (${movimentacao.usuarioDestino?.email || ''})`;

        const dataCriacao = new Date(movimentacao.dataCriacao || movimentacao.createdAt);

        document.getElementById('detailObservacao').textContent =
            movimentacao.observacao || 'Nenhuma observação';

        const itemsContainer = document.getElementById('detailItems');
        itemsContainer.innerHTML = '';

        if (movimentacao.itens && movimentacao.itens.length > 0) {
            movimentacao.itens.forEach(item => {
                const itemDiv = document.createElement('div');
                itemDiv.className = 'item-card';
                itemDiv.innerHTML = `
                    <div class="item-name">${item.nome}</div>
                    <div class="item-patrimonio">Patrimônio: ${item.patrimonio}</div>
                    <div class="item-status">Status: ${getStatusText(item.status)}</div>
                `;
                itemsContainer.appendChild(itemDiv);
            });
        } else {
            itemsContainer.innerHTML = '<p>Nenhum item encontrado</p>';
        }

        const modalActions = document.getElementById('modalActions');
        modalActions.innerHTML = getModalActions(movimentacao);

        document.getElementById('detailsModal').style.display = 'block';

    } catch (error) {
        showMessage('Erro ao carregar detalhes: ' + error.message, 'error');
    }
}

function getModalActions(movimentacao) {
    let actions = `
        <button onclick="closeModal()" class="btn btn-secondary">Fechar</button>
    `;

    if (movimentacao.status === 'PENDENTE') {
        actions += `
            <button onclick="confirmarMovimentacao('${movimentacao.id}')" class="btn btn-success">Confirmar</button>
            <button onclick="cancelarMovimentacao('${movimentacao.id}')" class="btn btn-danger">Cancelar</button>
        `;
    } else if (movimentacao.status === 'CONFIRMADA') {
        actions += `
            <button onclick="concluirMovimentacao('${movimentacao.id}')" class="btn btn-primary">Concluir Movimentação</button>
        `;
    }

    return actions;
}

async function confirmarMovimentacao(id) {
    if (!confirm('Tem certeza que deseja confirmar esta movimentação?')) return;

    try {
        const response = await fetch(`/movimentacoes/${id}/confirmar`, {
            method: 'PATCH'
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao confirmar movimentação');
        }

        showMessage('Movimentação confirmada com sucesso!', 'success');
        closeModal();
        loadMovimentacoes();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

async function cancelarMovimentacao(id) {
    if (!confirm('Tem certeza que deseja cancelar esta movimentação?')) return;

    try {
        const response = await fetch(`/movimentacoes/${id}/cancelar`, {
            method: 'PATCH'
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao cancelar movimentação');
        }

        showMessage('Movimentação cancelada com sucesso!', 'success');
        closeModal();
        loadMovimentacoes();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

async function concluirMovimentacao(id) {
    if (!confirm('Tem certeza que deseja concluir esta movimentação (devolver itens)?')) return;

    try {
        const response = await fetch(`/movimentacoes/${id}/devolucao`, {
            method: 'PATCH'
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao concluir movimentação');
        }

        showMessage('Movimentação concluída com sucesso!', 'success');
        closeModal();
        loadMovimentacoes();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

function filterByStatus() {
    const status = document.getElementById('statusFilter').value;

    if (!status) {
        movimentacoes = [...allMovimentacoes];
    } else {
        movimentacoes = allMovimentacoes.filter(m => m.status === status);
    }

    renderMovimentacoes(movimentacoes);
}

function searchMovimentacoes() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderMovimentacoes(movimentacoes);
        return;
    }

    const filtered = movimentacoes.filter(movimentacao =>
        (movimentacao.setorDestino?.nome?.toLowerCase() || '').includes(searchTerm) ||
        (movimentacao.usuarioDestino?.nome?.toLowerCase() || '').includes(searchTerm) ||
        (movimentacao.observacao?.toLowerCase() || '').includes(searchTerm) ||
        (movimentacao.id?.toLowerCase() || '').includes(searchTerm)
    );

    renderMovimentacoes(filtered);
}

function hideForm() {
    document.getElementById('movimentacaoForm').style.display = 'none';
}

function closeModal() {
    document.getElementById('detailsModal').style.display = 'none';
}

function showMessage(text, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = text;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';

    setTimeout(() => {
        messageDiv.style.display = 'none';
    }, 5000);
}

function logout() {
    localStorage.removeItem('username');
    window.location.href = '/login.html';
}

window.onclick = function(event) {
    const modal = document.getElementById('detailsModal');
    if (event.target === modal) {
        closeModal();
    }
}

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        closeModal();
    }
});