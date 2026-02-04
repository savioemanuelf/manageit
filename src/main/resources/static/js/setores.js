let setores = [];
let isEditing = false;

document.addEventListener('DOMContentLoaded', function() {
    loadSetores();
    loadUserInfo();

    document.getElementById('setorFormElement').addEventListener('submit', function(e) {
        e.preventDefault();
        saveSetor();
    });
});

function loadUserInfo() {
    const username = localStorage.getItem('username') || 'Usuário';
    document.getElementById('username').textContent = username;
}

async function loadSetores() {
    try {
        const response = await fetch('/setores');

        if (!response.ok) {
            const error = await response.text();
            throw new Error(`Erro ${response.status}: ${error}`);
        }

        setores = await response.json();
        renderSetores(setores);

    } catch (error) {
        showMessage('Erro ao carregar setores: ' + error.message, 'error');
        console.error('Erro detalhado:', error);
    }
}

function renderSetores(setoresToRender) {
    const tbody = document.getElementById('setoresTableBody');
    tbody.innerHTML = '';

    if (setoresToRender.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Nenhum setor cadastrado</td></tr>';
        return;
    }

    setoresToRender.forEach(setor => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td><strong>${setor.nome}</strong></td>
            <td>${setor.descricao || '-'}</td>
            <td class="coordenador-info">
                ${setor.coordenador ?
            `<strong>${setor.coordenador.nome}</strong>
                     <small>${setor.coordenador.email || ''}</small>`
            : 'Não definido'}
            </td>
            <td class="coordenador-info">
                ${setor.viceCoordenador ?
            `<strong>${setor.viceCoordenador.nome}</strong>
                     <small>${setor.viceCoordenador.email || ''}</small>`
            : 'Não definido'}
            </td>
            <td class="actions-cell">
                <button onclick="editSetor('${setor.id}')" class="btn-action btn-edit">Editar</button>
                <button onclick="deleteSetor('${setor.id}')" class="btn-action btn-delete">Excluir</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

function showCreateForm() {
    isEditing = false;
    document.getElementById('formTitle').textContent = 'Novo Setor';
    document.getElementById('setorFormElement').reset();
    document.getElementById('setorId').value = '';
    document.getElementById('setorForm').style.display = 'block';
}

function editSetor(id) {
    const setor = setores.find(s => s.id === id);
    if (!setor) return;

    isEditing = true;
    document.getElementById('formTitle').textContent = 'Editar Setor';
    document.getElementById('setorId').value = setor.id;
    document.getElementById('nome').value = setor.nome;
    document.getElementById('descricao').value = setor.descricao || '';
    document.getElementById('coordenadorId').value = setor.coordenador ? setor.coordenador.id : '';
    document.getElementById('viceCoordenadorId').value = setor.viceCoordenador ? setor.viceCoordenador.id : '';

    document.getElementById('setorForm').style.display = 'block';
}

async function saveSetor() {
    const id = document.getElementById('setorId').value;

    const setorData = {
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value
    };

    const coordenadorId = document.getElementById('coordenadorId').value;
    const viceCoordenadorId = document.getElementById('viceCoordenadorId').value;

    if (coordenadorId) {
        setorData.coordenador = { id: coordenadorId };
    }

    if (viceCoordenadorId) {
        setorData.viceCoordenador = { id: viceCoordenadorId };
    }

    try {
        let response;
        if (isEditing && id) {
            response = await fetch(`/setores/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(setorData)
            });
        } else {
            response = await fetch('/setores', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(setorData)
            });
        }

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao salvar setor');
        }

        showMessage(`Setor ${isEditing ? 'atualizado' : 'criado'} com sucesso!`, 'success');
        hideForm();
        loadSetores();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

async function deleteSetor(id) {
    if (!confirm('Tem certeza que deseja excluir este setor?')) return;

    try {
        const response = await fetch(`/setores/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Erro ao excluir setor');

        showMessage('Setor excluído com sucesso!', 'success');
        loadSetores();
    } catch (error) {
        showMessage('Erro ao excluir setor: ' + error.message, 'error');
    }
}

function searchSetores() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderSetores(setores);
        return;
    }

    const filtered = setores.filter(setor =>
        setor.nome.toLowerCase().includes(searchTerm) ||
        (setor.descricao && setor.descricao.toLowerCase().includes(searchTerm)) ||
        (setor.coordenador && setor.coordenador.nome.toLowerCase().includes(searchTerm)) ||
        (setor.viceCoordenador && setor.viceCoordenador.nome.toLowerCase().includes(searchTerm))
    );

    renderSetores(filtered);
}

function hideForm() {
    document.getElementById('setorForm').style.display = 'none';
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