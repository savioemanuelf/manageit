# ManageIT – Gerenciamento de Patrimônio de TI

> Branch mais atualizada: `feature/auth-implementation`

## Descrição

O **ManageIT** é um sistema web para gerenciamento de patrimônio de Tecnologia da Informação de um núcleo de concursos, permitindo o controle de itens, setores, usuários e movimentações de empréstimo e devolução de equipamentos.

Este projeto corresponde à implementação final com **autenticação e segurança**, conforme exigido na Atividade Unidade 03.

---

## Funcionalidades

- CRUD de:
    - Usuários
    - Setores
    - Itens de patrimônio
    - Movimentações
    - Processos Seletivos
- Controle de status dos itens
- Filtros nas listagens
- Autenticação de usuários
- Endpoints REST para todas as operações
- Interface web com mensagens de sucesso e erro
- Documentação da API com Swagger

---

## Regras de Negócio (Movimentações)

- Apenas itens com status **DISPONÍVEL** podem ser movimentados
- Usuário autenticado pode solicitar movimentação para um outro
    usuário cadastrado
- Ao cadastrar uma movimentação:
    - Os itens passam para **EMPRESTADO**
- Apenas movimentações **PENDENTES** podem ser:
    - Confirmadas
    - Canceladas
- Ao cancelar:
    - Os itens retornam para **DISPONÍVEL**
- Apenas movimentações **CONFIRMADAS** podem ser concluídas
- Ao concluir:
    - Os itens retornam para **DISPONÍVEL**


As regras são aplicadas na camada de serviço, garantindo integridade e consistência dos dados.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
    - Spring Web
    - Spring Data JPA
    - Spring Security
- **PostgreSQL**
- **Swagger / OpenAPI**
- **Maven**

---

## Segurança

- Autenticação via formulário com login e senha
- Controle de acesso com Spring Security
- Rotas mapeadas (públicas ou protegidas)

---

##  Como Executar

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL

### Execução
```bash
git clone https://github.com/savioemanuelf/manageit.git
cd manageit
git checkout feature/auth-implementation
mvn spring-boot:run
```
A aplicação estará disponível em:
```http://localhost:8080```
- Há um super usuário cadastrado diretamente pela classe AdminInitializer,
sendo usuário admin e senha admin para acesso total.

Para ver a documentação dos endpoints da API, acesse a rota:
```/swagger-ui.html```
