let items = [];
let isEditing = false;

document.addEventListener('DOMContentLoaded', function() {
    loadItems();
    loadUserInfo();

    document.getElementById('itemFormElement').addEventListener('submit', function(e) {
        e.preventDefault();
        saveItem();
    });
});

function loadUserInfo() {
    const username = localStorage.getItem('username') || 'Usuário';
    document.getElementById('username').textContent = username;
}

async function loadItems() {
    try {
        const response = await fetch('/itens');
        if (!response.ok) {
            const error = await response.json();
            throw new Error(`Erro ${response.status}: ${error.message}`);
        }
        items = await response.json();

        renderItems(items);
    } catch (error) {
        showMessage('Erro ao carregar itens: ' + error.message, 'error');
    }
}

function renderItems(itemsToRender) {
    const tbody = document.getElementById('itemsTableBody');
    tbody.innerHTML = '';

    if (itemsToRender.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Nenhum item cadastrado</td></tr>';
        return;
    }

    itemsToRender.forEach(item => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${item.patrimonio}</td>
            <td>${item.nome}</td>
            <td>${item.descricao || '-'}</td>
            <td><span class="status status-${item.status}">${getStatusText(item.status)}</span></td>
            <td class="actions-cell">
                <button onclick="editItem('${item.id}')" class="btn-action btn-edit">Editar</button>
                <button onclick="deleteItem('${item.id}')" class="btn-action btn-delete">Excluir</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

function getStatusText(status) {
    const statusMap = {
        'DISPONIVEL': 'Disponível',
        'EMPRESTADO': 'Emprestado',
        'MANUTENCAO': 'Manutenção'
    };
    return statusMap[status] || status;
}

function showCreateForm() {
    isEditing = false;
    document.getElementById('formTitle').textContent = 'Novo Item';
    document.getElementById('itemFormElement').reset();
    document.getElementById('itemId').value = '';
    document.getElementById('itemForm').style.display = 'block';
}

function editItem(id) {
    const item = items.find(i => i.id === id);
    if (!item) return;

    isEditing = true;
    document.getElementById('formTitle').textContent = 'Editar Item';
    document.getElementById('itemId').value = item.id;
    document.getElementById('patrimonio').value = item.patrimonio;
    document.getElementById('nome').value = item.nome;
    document.getElementById('descricao').value = item.descricao || '';
    document.getElementById('status').value = item.status;

    document.getElementById('itemForm').style.display = 'block';
}

async function saveItem() {
    const id = document.getElementById('itemId').value;
    const itemData = {
        patrimonio: document.getElementById('patrimonio').value,
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        status: document.getElementById('status').value
    };

    try {
        let response;
        if (isEditing && id) {
            response = await fetch(`/itens/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(itemData)
            });
        } else {
            response = await fetch('/itens', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(itemData)
            });
        }

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao salvar item');
        }

        showMessage(`Item ${isEditing ? 'atualizado' : 'criado'} com sucesso!`, 'success');
        hideForm();
        loadItems();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

async function deleteItem(id) {
    if (!confirm('Tem certeza que deseja excluir este item?')) return;

    try {
        const response = await fetch(`/itens/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Erro ao excluir item');

        showMessage('Item excluído com sucesso!', 'success');
        loadItems();
    } catch (error) {
        showMessage('Erro ao excluir item: ' + error.message, 'error');
    }
}

function searchItems() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderItems(items);
        return;
    }

    const filtered = items.filter(item =>
        item.patrimonio.toLowerCase().includes(searchTerm) ||
        item.nome.toLowerCase().includes(searchTerm) ||
        (item.descricao && item.descricao.toLowerCase().includes(searchTerm))
    );

    renderItems(filtered);
}

function hideForm() {
    document.getElementById('itemForm').style.display = 'none';
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