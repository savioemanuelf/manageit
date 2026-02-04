document.getElementById('cadastroForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;

    if (senha !== confirmarSenha) {
        showMessage('As senhas não coincidem!', 'error');
        return;
    }

    const pessoaData = {
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value,
        email: document.getElementById('email').value,
        telefone: document.getElementById('telefone').value || null
    };

    try {
        const pessoaResponse = await fetch('/pessoas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pessoaData)
        });

        if (!pessoaResponse.ok) {
            const error = await pessoaResponse.text();
            throw new Error(error || 'Erro ao criar pessoa');
        }

        const pessoa = await pessoaResponse.json();

        const usuarioData = {
            login: document.getElementById('login').value,
            senha: senha,
            pessoaId: pessoa.id
        };

        const usuarioResponse = await fetch('/usuarios/cadastrar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuarioData)
        });

        if (!usuarioResponse.ok) {
            const error = await usuarioResponse.text();
            throw new Error(error || 'Erro ao criar usuário');
        }

        showMessage('Conta criada com sucesso! Redirecionando para login...', 'success');

        setTimeout(() => {
            window.location.href = '/login.html';
        }, 2000);

    } catch (error) {
        showMessage('Erro: ' + error.message, 'error');
        console.error('Erro no cadastro:', error);
    }
});

function showMessage(text, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = text;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';

    if (type === 'success') {
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);
    }
}

document.getElementById('cpf').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length > 3 && value.length <= 6) {
        value = value.replace(/(\d{3})(\d{1,3})/, '$1.$2');
    } else if (value.length > 6 && value.length <= 9) {
        value = value.replace(/(\d{3})(\d{3})(\d{1,3})/, '$1.$2.$3');
    } else if (value.length > 9) {
        value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
    }
    e.target.value = value;
});

document.getElementById('telefone').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length > 0) {
        value = '(' + value;
    }
    if (value.length > 3) {
        value = value.substring(0, 3) + ') ' + value.substring(3);
    }
    if (value.length > 10) {
        value = value.substring(0, 10) + '-' + value.substring(10, 14);
    }
    e.target.value = value;
});