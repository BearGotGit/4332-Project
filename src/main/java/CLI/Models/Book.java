package CLI.Models;

public class Book {
    public String Name;
    public String Author;
    public int Year;
    public String Genre;
    public int ISBN;
    public String BookID;
    public Boolean IsAvailable;

    public Book(String Name, String Author, int Year, String Genre, int ISBN, String BookID) {
        this.Name = Name;
        this.Author = Author;
        this.Year = Year;
        this.Genre = Genre;
        this.ISBN = ISBN;
        this.BookID = BookID;
        this.IsAvailable = true;
    }

    // Check if book is available
    public Boolean checkAvailability(){
        return this.IsAvailable;
    }

    // Get info about a book
    public String getBookInfo() {
        return String.format(
            "Name: %s, Author: %s, Year: %d, Genre: %s, ISBN: %d, Available: %s",
            this.Name,
            this.Author,
            this.Year,
            this.Genre,
            this.ISBN,
            this.IsAvailable ? "Yes" : "No"
        );
    }

    // Update the info of a book
    public void updateBookInfo(String Name, String Author, int Year, String Genre, int ISBN, String BookID, boolean IsAvailable){
        this.Name = Name;
        this.Author = Author;
        this.Year = Year;
        this.Genre = Genre;
        this.ISBN = ISBN;
        this.BookID = BookID;
        this.IsAvailable = IsAvailable;
    }
}
