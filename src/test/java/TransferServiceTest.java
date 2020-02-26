import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransactionDao;
import com.karpovich.homework.database.DAO.TransferDao;
import com.karpovich.homework.database.service.TransferService;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.model.Account;
import com.karpovich.homework.model.Transfer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferServiceTest {
    private static final int VALID_ACCOUNT = 1;
    private static final int INVALID_ACCOUNT = -1;
    private static final int VALID_AMOUNT = 100;
    private static final int INSUFFICIENT_AMOUNT = 99;
    private static final int VALID_RECEIVER_ACCOUNT = 2;
    private static final int INVALID_AMOUNT = -100;
    private TransferDao transferDao = Mockito.mock(TransferDao.class);
    private AccountDao accountDao = Mockito.mock(AccountDao.class);
    private TransactionDao transactionDao = Mockito.mock(TransactionDao.class);

    private TransferService transferService = new TransferService(transferDao, accountDao, transactionDao);

    @Test
    public void createNewTransferTest() {
        Account senderAccount = new Account(VALID_ACCOUNT, VALID_AMOUNT);
        Account receiverAccount = new Account(VALID_RECEIVER_ACCOUNT, 0);

        when(accountDao.get(VALID_ACCOUNT)).thenReturn(senderAccount);
        when(accountDao.get(VALID_RECEIVER_ACCOUNT)).thenReturn(receiverAccount);

        Transfer newTransfer = new Transfer(VALID_ACCOUNT, VALID_RECEIVER_ACCOUNT, VALID_AMOUNT);
        transferService.createNewTransfer(newTransfer);

        verify(accountDao, Mockito.times(1)).get(VALID_ACCOUNT);
        verify(accountDao, Mockito.times(1)).get(VALID_RECEIVER_ACCOUNT);
        verify(transferDao, Mockito.times(1)).save(newTransfer);

        senderAccount.setAmount(0);
        receiverAccount.setAmount(VALID_AMOUNT);

        verify(accountDao, Mockito.times(1)).save(senderAccount);
        verify(accountDao, Mockito.times(1)).save(receiverAccount);
    }

    @Test
    public void createNewTransferInsufficientFundsTest() {
        Account senderAccount = new Account(VALID_ACCOUNT, INSUFFICIENT_AMOUNT);
        Account receiverAccount = new Account(VALID_RECEIVER_ACCOUNT, 0);

        when(accountDao.get(VALID_ACCOUNT)).thenReturn(senderAccount);
        when(accountDao.get(VALID_RECEIVER_ACCOUNT)).thenReturn(receiverAccount);

        Transfer newTransfer = new Transfer(VALID_ACCOUNT, VALID_RECEIVER_ACCOUNT, VALID_AMOUNT);
        assertThrows(DataValidationException.class, () -> {
            transferService.createNewTransfer(newTransfer);
        });

        verify(accountDao, Mockito.times(1)).get(VALID_ACCOUNT);
        verify(accountDao, Mockito.times(1)).get(VALID_RECEIVER_ACCOUNT);
        verify(transferDao, Mockito.times(0)).save(newTransfer);
    }

    @Test
    public void createNewTransferNegativeSenderAccountTest() {
        Transfer transfer = new Transfer(INVALID_ACCOUNT, VALID_ACCOUNT, VALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            transferService.createNewTransfer(transfer);
        });
    }

    @Test
    public void createNewTransferNegativeReceiverAccountTest() {
        Transfer transfer = new Transfer(VALID_ACCOUNT, INVALID_ACCOUNT, VALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            transferService.createNewTransfer(transfer);
        });
    }

    @Test
    public void createNewTransferNegativeAmountTest() {
        Transfer transfer = new Transfer(VALID_ACCOUNT, VALID_RECEIVER_ACCOUNT, INVALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            transferService.createNewTransfer(transfer);
        });
    }

    @Test
    public void createNewTransferSenderAccountEqualsReceiverTest() {
        Transfer transfer = new Transfer(VALID_ACCOUNT, VALID_ACCOUNT, VALID_AMOUNT);

        assertThrows(DataValidationException.class, () -> {
            transferService.createNewTransfer(transfer);
        });
    }

    @Test
    public void getBalanceBadIdTest() {

        assertThrows(DataValidationException.class, () -> {
            transferService.getTransfer(INVALID_ACCOUNT);
        });
    }

    @Test
    public void getTransferNoSuchIdTest() {
        when(transferDao.get(VALID_ACCOUNT)).thenReturn(null);

        assertThrows(DataValidationException.class, () -> {
            transferService.getTransfer(VALID_ACCOUNT);
        });
    }
}
