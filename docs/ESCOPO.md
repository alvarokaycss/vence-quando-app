# Escopo do Projeto: Vence Quando?

## 1. Visão Geral
Aplicativo Android nativo (Kotlin/XML) para controle de validade de produtos domésticos, visando redução de desperdício. O sistema conta com uma API REST (Node.js) para persistência de dados.

## 2. Requisitos Funcionais (RF)
* **[RF001] Autenticação:** O usuário deve conseguir logar com email e senha pré-cadastrados no banco.
* **[RF002] Listagem de Itens:** O sistema deve listar os produtos ordenados pela data de validade (do mais próximo ao mais distante).
* **[RF003] Indicadores Visuais:** A listagem deve usar cores semáforicas para indicar urgência.
* **[RF004] Cadastro de Item:** O usuário deve poder adicionar um novo produto informando: Nome, Categoria e Data de Validade.
* **[RF005] Gestão de Item:** O usuário deve poder editar ou excluir um item existente.

## 3. Regras de Negócio (RN)
* **[RN001] Lógica do Semáforo:**
    * 🔴 **Vermelho:** Vence em até 3 dias (inclusive).
    * 🟡 **Amarelo:** Vence entre 4 e 7 dias.
    * 🟢 **Verde:** Vence em mais de 7 dias.

## 4. Stack Tecnológica
* **Mobile:** Android Nativo (Kotlin + XML Views).
* **Backend:** Node.js (Express).
* **Banco de Dados:** PostgreSQL.