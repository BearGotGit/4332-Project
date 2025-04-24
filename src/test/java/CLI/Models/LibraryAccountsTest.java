package CLI.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryAccountsTest {

    Purchasing purchasing;

    @BeforeEach
    void setUp() {
        purchasing = mock(Purchasing.class);
    }

//    /* deposit donation */

//    check balance after
    @Test
    void balanceAfterDonate() {
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Donate
        int donateAmount = 100;
        double before = accounts.getBalance();
        accounts.depositDonation(donateAmount);
        double after = accounts.getBalance();

//        Test donated
        assertEquals(donateAmount, after - before);
    }

    /* withdrawSalary */
//      salary good - check returned salary
    @Test
    void paySalary (){
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Pay salary
        int salary = 1_000;
        double before = accounts.getBalance();
        accounts.withdrawSalary(salary);
        double after = accounts.getBalance();

//        Test donated
        assertEquals(salary, before - after);
    }

    //      salary good - check balance after
    @Test
    void balanceAfterSalary (){
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Pay salary
        int salary = 1_000;
        double before = accounts.getBalance();
        accounts.withdrawSalary(salary);
        double after = accounts.getBalance();

//        Test donated
        assertEquals(salary, before - after);
    }

//      not enough balance - check returned salary
    @Test
    void paySalaryTightBalance() {
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Pay salary
        double salary = 1_000;
        double before = 100;
        accounts.setBalance(before);
        double paycheck = accounts.withdrawSalary(salary);

//        Test salary is meager
        assertEquals(before, paycheck);
    }

    //      not enough balance - check balance after
    @Test
    void tightBalanceAfterSalary() {
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Pay salary
        double salary = 1_000;
        double before = 100;
        accounts.setBalance(before);
        accounts.withdrawSalary(salary);
        double after = accounts.getBalance();

//        Test balance is 0
        assertEquals(0, after);
    }

    /*orderNewBook*/
//      price good - deduct price book from balance, bought book
    @Test
    void buyBook() {
//        Set up
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Buy book
        boolean ordered = accounts.orderNewBook("Everything I Never Told You", "Celeste Ng", 2014, "Fiction", 978014);

//        Test book ordered
        assertEquals(true, ordered);
    }

//      price good - deduct price book from balance, bought book
    @Test
    void balanceAfterBuyBook() {
//        Set up
        double bookCost = 10;
        when(purchasing.getBookPrice(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(bookCost);
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Buy book
        double before = accounts.getBalance();
        accounts.orderNewBook("Everything I Never Told You", "Celeste Ng", 2014, "Fiction", 978014);
        double after = accounts.getBalance();

//        Test deducted balance
        assertEquals(bookCost, before - after);
    }

//      price too high - refuse purchase, did not buy book
    @Test
    void buyBookTightBalance() {
//        Set up
        double bookCost = 30_800_000;
        when(purchasing.getBookPrice(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(bookCost);
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Buy book
        boolean ordered = accounts.orderNewBook("Codex Leicester", "Leonardo da Vinci", 1510, "Scientific Manuscript", 978000);

//        Test book ordered
        assertEquals(false, ordered);
    }

//      price good - deduct price book from balance, bought book
    @Test
    void tightBalanceAfterBuyBook() {
//        Set up
        double bookCost = 30_800_000;
        when(purchasing.getBookPrice(anyString(), anyString(), anyInt(), anyString(), anyInt())).thenReturn(bookCost);
        LibraryAccounts accounts = new LibraryAccounts(purchasing);

//        Buy book
        double before = accounts.getBalance();
        accounts.orderNewBook("Codex Leicester", "Leonardo da Vinci", 1510, "Scientific Manuscript", 978000);
        double after = accounts.getBalance();

//        Test deducted balance
        assertEquals(0, after - before);
    }


}
