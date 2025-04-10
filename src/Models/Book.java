package Models;

public class Book {
    String Name;
    String Author;
    int Year;
    String Genre;
    Boolean IsAvailable;
    int ISBN;
    String BookID;

    public Book(String Name, String Author, int Year, String Genre, int ISBN, String BookID) {
        this.Name = Name;
        this.Author = Author;
        this.Year = Year;
        this.BookID = BookID;
        this.IsAvailable = true;
        this.Genre = Genre;
        this.ISBN = ISBN;
    }

    // Check if book is available
    public Boolean checkAvailability(){
        return this.IsAvailable;
    }

    // Get info about a book
    public String getBookInfo() {
        return String.format(
            "Name: %s, Author: %s, Year: %d, Genre: %s, ISBN: %d, BookID: %s, Available: %s",
            this.Name,
            this.Author,
            this.Year,
            this.Genre,
            this.ISBN,
            this.BookID,
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
