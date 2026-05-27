import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LoanManagerTest {

    @Before
    public void resetLegacyDatabase() {
        LegacyDatabase.getBooks().clear();
        LegacyDatabase.getUsers().clear();
        LegacyDatabase.getLoans().clear();
        LegacyDatabase.getLogs().clear();
        LegacyDatabase.BOOK_SEQ = 1;
        LegacyDatabase.USER_SEQ = 1;
        LegacyDatabase.LOAN_SEQ = 1;
        LegacyDatabase.seedInitialData();
    }

    @Test
    public void deveCalcularMultaPadraoQuandoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-01", "2026-05-02", 0, "teste", "helper", 1, 2);

        assertEquals(2.0, fine, 0.0001);
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-10", "2026-05-10", 0, "teste", "helper", 1, 2);

        assertEquals(0.0, fine, 0.0001);
    }

    // ==========================================================
    // Bug #2 - returnBook() deve SOMAR multa na divida do usuario
    // ==========================================================

    @Test
    public void deveSomarMultaNaDividaAoDevolver() {
        LoanManager loanManager = new LoanManager();

        // Emprestar livro 1 (Clean Code) para usuario 1 (Ana)
        int loanId = loanManager.borrowBook(1, 1, "2026-05-01", "2026-05-10", "email", 14, "test", 0);

        // Devolver com atraso (returnedDate > dueDate) e forceFlag=0 para gerar multa
        loanManager.returnBook(loanId, "2026-05-15", "email", 0, "test", "handler");

        // A divida do usuario deve ter AUMENTADO (somou a multa), nao diminuido
        Map<String, Object> user = LegacyDatabase.getUserById(1);
        double debt = ((Double) user.get("debt")).doubleValue();
        assertTrue("A divida do usuario deve ser positiva apos multa por atraso", debt > 0);
    }

    @Test
    public void deveManterDividaZeroQuandoDevolverSemAtraso() {
        LoanManager loanManager = new LoanManager();

        // Emprestar livro 1 para usuario 1
        int loanId = loanManager.borrowBook(1, 1, "2026-05-01", "2026-05-20", "email", 14, "test", 0);

        // Devolver antes da data limite (sem atraso)
        loanManager.returnBook(loanId, "2026-05-10", "email", 0, "test", "handler");

        // A divida deve continuar zero
        Map<String, Object> user = LegacyDatabase.getUserById(1);
        double debt = ((Double) user.get("debt")).doubleValue();
        assertEquals("Divida deve permanecer zero sem atraso", 0.0, debt, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoQuandoLoanIdInvalido() {
        LoanManager loanManager = new LoanManager();
        loanManager.returnBook(-1, "2026-05-15", "email", 0, "test", "handler");
    }

    // ==========================================================
    // Bug #3 - countOpenLoansByBook() deve filtrar por bookId
    // ==========================================================

    @Test
    public void deveContarEmprestimosAbertosDoLivroCorreto() {
        LoanManager loanManager = new LoanManager();

        // Emprestar livro 1 para usuario 1
        loanManager.borrowBook(1, 1, "2026-05-01", "2026-05-20", "email", 14, "test", 0);

        // Contar emprestimos abertos do livro 1 deve ser 1
        int countBook1 = LegacyDatabase.countOpenLoansByBook(1);
        assertEquals("Livro 1 deve ter 1 emprestimo aberto", 1, countBook1);

        // Contar emprestimos abertos do livro 2 deve ser 0 (nenhum emprestimo feito)
        int countBook2 = LegacyDatabase.countOpenLoansByBook(2);
        assertEquals("Livro 2 nao deve ter emprestimos abertos", 0, countBook2);
    }

    @Test
    public void deveRetornarZeroParaLivroSemEmprestimo() {
        // Nenhum emprestimo foi feito
        int count = LegacyDatabase.countOpenLoansByBook(3);
        assertEquals("Livro sem emprestimos deve retornar zero", 0, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoQuandoBookIdInvalidoEmContagem() {
        LegacyDatabase.countOpenLoansByBook(-1);
    }
}