let processos = [];
let isEditing = false;

document.addEventListener('DOMContentLoaded', function() {
    loadProcessos();
    loadUserInfo();

    document.getElementById('processoFormElement').addEventListener('submit', function(e) {
        e.preventDefault();
        saveProcesso();
    });

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('dataInicio').max = today;
    document.getElementById('dataFim').max = today;
});

function loadUserInfo() {
    const username = localStorage.getItem('username') || 'Usuário';
    document.getElementById('username').textContent = username;
}

async function loadProcessos() {
    try {
        const response = await fetch('/processos-seletivos');

        if (!response.ok) {
            const error = await response.text();
            throw new Error(`Erro ${response.status}: ${error}`);
        }

        processos = await response.json();
        renderProcessos(processos);

    } catch (error) {
        showMessage('Erro ao carregar processos: ' + error.message, 'error');
        console.error('Erro detalhado:', error);
    }
}

function renderProcessos(processosToRender) {
    const tbody = document.getElementById('processosTableBody');
    tbody.innerHTML = '';

    if (processosToRender.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Nenhum processo cadastrado</td></tr>';
        return;
    }

    processosToRender.forEach(processo => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${processo.nomeProcesso}</td>
            <td>${processo.anoProcesso}</td>
            <td><span class="tipo">${getTipoText(processo.tipoProcesso)}</span></td>
            <td><span class="situacao situacao-${processo.situacao}">${getSituacaoText(processo.situacao)}</span></td>
            <td class="date-cell">${formatDate(processo.dataInicio)}</td>
            <td class="date-cell">${formatDate(processo.dataFim)}</td>
            <td class="actions-cell">
                <button onclick="editProcesso('${processo.id}')" class="btn-action btn-edit">Editar</button>
                <button onclick="deleteProcesso('${processo.id}')" class="btn-action btn-delete">Excluir</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

function getTipoText(tipo) {
    const tipoMap = {
        'CONCURSO': 'Concurso',
        'CURSO_TECNICO': 'Curso Técnico',
        'EAD': 'EAD',
        'PROFICIENCIA': 'Proficiência',
        'GRADUACAO': 'Graduação',
        'EXTRAVESTIBULAR': 'Extravestibular',
        'POS_GRADUACAO': 'Pós-Graduação',
        'IMD': 'IMD',
        'VESTIBULAR': 'Vestibular'
    };
    return tipoMap[tipo] || tipo;
}

function getSituacaoText(situacao) {
    const situacaoMap = {
        'EM_ANDAMENTO': 'Em Andamento',
        'CONCLUIDO': 'Concluído',
        'SUSPENSO': 'Suspenso'
    };
    return situacaoMap[situacao] || situacao;
}

function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

function showCreateForm() {
    isEditing = false;
    document.getElementById('formTitle').textContent = 'Novo Processo Seletivo';
    document.getElementById('processoFormElement').reset();
    document.getElementById('processoId').value = '';
    document.getElementById('processoForm').style.display = 'block';
}

function editProcesso(id) {
    const processo = processos.find(p => p.id === id);
    if (!processo) return;

    isEditing = true;
    document.getElementById('formTitle').textContent = 'Editar Processo Seletivo';
    document.getElementById('processoId').value = processo.id;
    document.getElementById('nomeProcesso').value = processo.nomeProcesso;
    document.getElementById('anoProcesso').value = processo.anoProcesso;
    document.getElementById('tipoProcesso').value = processo.tipoProcesso;
    document.getElementById('situacao').value = processo.situacao;
    document.getElementById('dataInicio').value = processo.dataInicio ? processo.dataInicio.split('T')[0] : '';
    document.getElementById('dataFim').value = processo.dataFim ? processo.dataFim.split('T')[0] : '';

    document.getElementById('processoForm').style.display = 'block';
}

async function saveProcesso() {
    const id = document.getElementById('processoId').value;
    const processoData = {
        nomeProcesso: document.getElementById('nomeProcesso').value,
        anoProcesso: parseInt(document.getElementById('anoProcesso').value),
        tipoProcesso: document.getElementById('tipoProcesso').value,
        situacao: document.getElementById('situacao').value,
        dataInicio: document.getElementById('dataInicio').value || null,
        dataFim: document.getElementById('dataFim').value || null
    };

    try {
        let response;
        if (isEditing && id) {
            response = await fetch(`/processos-seletivos/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(processoData)
            });
        } else {
            response = await fetch('/processos-seletivos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(processoData)
            });
        }

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro ao salvar processo');
        }

        showMessage(`Processo ${isEditing ? 'atualizado' : 'criado'} com sucesso!`, 'success');
        hideForm();
        loadProcessos();
    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
    }
}

async function deleteProcesso(id) {
    if (!confirm('Tem certeza que deseja excluir este processo?')) return;

    try {
        const response = await fetch(`/processos-seletivos/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Erro ao excluir processo');

        showMessage('Processo excluído com sucesso!', 'success');
        loadProcessos();
    } catch (error) {
        showMessage('Erro ao excluir processo: ' + error.message, 'error');
    }
}

function searchProcessos() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    if (!searchTerm) {
        renderProcessos(processos);
        return;
    }

    const filtered = processos.filter(processo =>
        processo.nomeProcesso.toLowerCase().includes(searchTerm) ||
        processo.anoProcesso.toString().includes(searchTerm)
    );

    renderProcessos(filtered);
}

function hideForm() {
    document.getElementById('processoForm').style.display = 'none';
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