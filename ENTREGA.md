# Entregas - Manutenção de Software ESOFT5S 2BIM

**Disciplina:** Manutenção de Software - ESOFT5S 2BIM

## Integrantes

| Nome | GitHub |
|------|--------|
| Luis Gustavo Boratto | [@Tox1469](https://github.com/Tox1469) |
| Igor Pallisser | - |

## Links

- **Repositório:** https://github.com/Tox1469/unicesumar-manutencao
- **Project Board:** https://github.com/users/Tox1469/projects/1
- **Issues:** https://github.com/Tox1469/unicesumar-manutencao/issues

---

## Atividade 2 - Triagem, Relatório Profissional e Ciclo de Vida do Bug

### Bugs Documentados

| Issue | Título | Tipo | Severidade | Prioridade | Commit |
|-------|--------|------|------------|------------|--------|
| [#1](https://github.com/Tox1469/unicesumar-manutencao/issues/1) | listBooksSimple() crash com lista vazia | Bug | Catastrophic | Alta | 977be0a |
| [#2](https://github.com/Tox1469/unicesumar-manutencao/issues/2) | returnBook() subtrai multa ao invés de somar | Bug | Serious | Alta | 64f18c1 |
| [#3](https://github.com/Tox1469/unicesumar-manutencao/issues/3) | countOpenLoansByBook() filtra por campo errado | Defect | Serious | Média | 63aac37 |
| [#4](https://github.com/Tox1469/unicesumar-manutencao/issues/4) | ReportGenerator contagem errada de closed loans | Bug | Normal | Média | 6fd73a1 |
| [#5](https://github.com/Tox1469/unicesumar-manutencao/issues/5) | calculateFineLegacy() condição inalcançável | Defect | Normal | Baixa | 596fca1 |

---

## Atividade 3 - Debugging e Observabilidade

### Bugs Corrigidos com Teste, Contrato e Log4j

| Issue | Classe | Teste JUnit | Programação por Contrato | Log4j |
|-------|--------|-------------|--------------------------|-------|
| [#2](https://github.com/Tox1469/unicesumar-manutencao/issues/2) | LoanManager.returnBook() | deveSomarMultaNaDividaAoDevolver | IllegalArgumentException se loanId <= 0 | INFO/ERROR |
| [#3](https://github.com/Tox1469/unicesumar-manutencao/issues/3) | LegacyDatabase.countOpenLoansByBook() | deveContarEmprestimosAbertosDoLivroCorreto | IllegalArgumentException se bookId <= 0 | INFO |
| [#5](https://github.com/Tox1469/unicesumar-manutencao/issues/5) | LoanManager.calculateFineLegacy() | deveDispararAlertaNivel3QuandoMultaAcimaDe100 | assert dueDate/returnedDate != null | INFO |

### Testes (13 testes, todos passando)

- `LoanManagerTest.java` - 13 testes cobrindo bugs #2, #3, #5 e refatoração

---

## Atividade 4 - Inventário de Dívida Técnica e Refatoração

### Dívidas Técnicas Identificadas

| Issue | Code Smell | Arquivo | Risco |
|-------|-----------|---------|-------|
| [#6](https://github.com/Tox1469/unicesumar-manutencao/issues/6) | God Class | LibrarySystem.java | Alto - viola SRP, 250+ linhas |
| [#7](https://github.com/Tox1469/unicesumar-manutencao/issues/7) | Arrow Anti-Pattern | LoanManager.borrowBook() | Alto - 8 níveis de if aninhado |
| [#8](https://github.com/Tox1469/unicesumar-manutencao/issues/8) | Feature Envy | ReportGenerator.generateSimpleReport() | Médio - acessa internals de LegacyDatabase |
| [#9](https://github.com/Tox1469/unicesumar-manutencao/issues/9) | Global Mutable State | LegacyDatabase.java | Alto - estado global sem encapsulamento |
| [#10](https://github.com/Tox1469/unicesumar-manutencao/issues/10) | Long Parameter List | BookManager.registerBook() | Médio - 8 parâmetros |

### Refatoração Aplicada

- **Issue #7 - Arrow Anti-Pattern:** substituídos 8 níveis de if aninhado por guard clauses no `borrowBook()`, com testes e logging
- **Commit:** `refactor(loan-manager): remove arrow anti-pattern do borrowBook com guard clauses`
