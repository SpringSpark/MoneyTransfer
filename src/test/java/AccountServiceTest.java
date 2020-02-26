import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransactionDao;
import com.karpovich.homework.database.service.AccountService;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private static final int INVALID_ID = -1;
    private static final int INVALID_AMOUNT = -100;
    private static final int VALID_AMOUNT = 100;
    private static final int VALID_ID = 1;
    private AccountDao accountDao = Mockito.mock(AccountDao.class);
    private TransactionDao transactionDao = Mockito.mock(TransactionDao.class);

    private AccountService accountService = new AccountService(accountDao, transactionDao);

    @Test
    public void createNewAccountTest() {
        Account newAccount = new Account(VALID_ID, VALID_AMOUNT);
        accountService.createNewAccount(newAccount);
        verify(accountDao, Mockito.times(1)).get(VALID_ID);
        verify(accountDao, Mockito.times(1)).save(newAccount);
    }

    @Test
    public void createNewAccountNegativeIdTest() {
        Account badAccount = new Account(INVALID_ID, VALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            accountService.createNewAccount(badAccount);
        });
    }

    @Test
    public void createNewAccountNegativeAmountTest() {
        Account badAccount = new Account(VALID_ID, INVALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            accountService.createNewAccount(badAccount);
        });
    }

    @Test
    public void getBalanceNegativeIdTest() {

        assertThrows(DataValidationException.class, () -> {
            accountService.getAccountBalance(INVALID_ID);
        });
    }

    @Test
    public void getBalanceNoSuchAccountTest() {
        when(accountDao.get(VALID_ID)).thenReturn(null);

        assertThrows(DataValidationException.class, () -> {
            accountService.getAccountBalance(VALID_ID);
        });
    }
}
