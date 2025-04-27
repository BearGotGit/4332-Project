# Library Management CLI Project
Built by: Alex Brodsky, Berend Grandt, Shawn Russell, & Bruce Brown

### Run the CLI
Mac/Linux or GitBash:
```sh
cd 4332-Project
./run.sh # or `make run`
```

Windows without GitBash:
```sh
cd 4332-Project
make run
```

Or just open the root folder (4332-Project) in **IntelliJ** and hit the green arrow on Main.java.

##### How to use the CLI
 * Simply follow the instructions in the CLI, such as:
   * "Enter the number of the option you want to select: "
   * "Press any key to continue: "
   * "Enter your librarian username: "
 * It is a simple interface with the options clearly listed.

### Run the Tests in IntelliJ:
 * Open the project at the root 4332-Project in IntelliJ
 * Right-click on the `test` folder
   * Click `Run 'All Tests'`
 * You can also go in each test file and run them with coverage

### Features
 * 14 total options:
   * Order Book
   * Remove Book
   * Check Book Availability
   * Checkout Book
   * Return Book
   * View All Books
   * Add Member
   * Revoke Membership
   * View All Members
   * Hire Part-Time Librarian
   * Withdraw Salary
   * Donate to Library
   * Log In
   * Log Out
 * Members can:
   * Check Book Availability
   * Checkout Book
   * Return Book
   * View All Books
   * View All Members
 * Librarians are also supported
   * Once logged in, the librarian stays logged in until logging out.
   * Full-time librarians have an Auth Code
     * There are 3 full-time librarians created at the start.
       * They can be found in the Main.java, along with their auth code and salary.
     * They can:
       * Order Book
       * Remove Book
       * Check Book Availability
       * View All Books
       * Add Member
       * Revoke Membership
       * View All Members
       * Hire Part-Time Librarian
       * Withdraw Salary
       * Donate to Library
       * Log In
       * Log Out
       * They can also check out and return books, but they need a Member's ID.
   * Part-time librarians can be hired by full-time librarians
     * They can:
       * Remove Book
       * Check Book Availability
       * View All Books
       * View All Members
       * Log In
       * Log Out 
       * They can also check out and return books, but they need a Member's ID.
         * If they try to check out a book that doesn't exist, they can have a full-time librarian sign-in to approve ordering the book.
 * When a librarian is logged in, their username and employment level is clearly shown at the top of the list of options.
