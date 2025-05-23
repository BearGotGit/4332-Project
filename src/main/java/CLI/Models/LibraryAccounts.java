package CLI.Models;

public class LibraryAccounts {
    private Purchasing purchasing;
    private double balance = 39_000.00;

    public LibraryAccounts(Purchasing purchasing) {
        this.purchasing = purchasing;
    }

    public void depositDonation(double donation) {
        balance += donation;
    }

//    Increases observability
    public double getBalance() {
        return balance;
    }
//    Increases controllability
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Returns the salary withdrawn, might be less than input if the balance is less than the salary
    public double withdrawSalary(double salary) {
        double bal;
        if (salary >= balance) {
            bal = balance;
            balance = 0;
        }
        else {
            bal = salary;
            balance -= salary;
        }
        System.out.printf("Salary of $%.2f was withdrawn.%n", bal);
        return bal;
    }

    // Tries to order a new book, fails if the cost is greater than the library account balance
    public Boolean orderNewBook(String bookName, String author, int year, String genre, int ISBN) {
        double price = purchasing.getBookPrice(bookName, author, year, genre, ISBN);
        if (balance >= price) {
            balance -= price;
            System.out.printf("Order for \"%s\" was placed for $%.2f!%n", bookName, price);
            return true;
        }
        System.out.printf("Order could NOT be placed for book \"%s\" with price $%.2f.%n", bookName, price);
        return false;
    }
}
