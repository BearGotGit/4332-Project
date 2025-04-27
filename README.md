# Library Management CLI Project
Built by: Alex Brodsky, Berend Grandt, Shawn Russell, & Bruce Brown

## Overview
This project is a Library Management system for managing a library's books, members, librarians, and bank account with authentication displayed through an interactive and intuitive command-line interface.

## How to run the CLI
* Open the root folder `4332-Project` in `IntelliJ` and hit the green arrow in Main.java.
* Or, in the command line:
  ```sh
  cd 4332-Project
  make run
  ```

#### How to use the CLI
 * Simply follow the instructions in the CLI, such as:
   * "Enter the number of the option you want to select: "
   * "Press any key to continue: "
   * "Enter your librarian username: "
 * It is a simple interface with the options clearly displayed.

## How to run the Tests in IntelliJ:
  * Open the project at the root folder `4332-Project` in `IntelliJ`
  * Right-click on the `src/test` folder
  * Click `Run 'All Tests'`
  * You can also hover over `More Run/Debug`
    * Click `Run 'All Tests' with Coverage`

## Features
  * <details>
    <summary>14 total options:</summary>

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
    </details>
   
  * <details>
      <summary>Members can:</summary>

      * Check Book Availability
      * Checkout Book
      * Return Book
      * View All Books
      * View All Members
    </details>

  * <details>
      <summary>Librarians are also supported</summary>
      
      * Once logged in, the librarian stays logged in until logging out.
      * When a librarian is logged in, their username and employment level is clearly shown at the top of the list of options.
      * Full-time librarians have a 6-digit authentication code.
        * There are 3 full-time librarians created at the start.
          * They can be found in the Main.java, along with their auth code and salary.
          * <details>
              <summary>Full-time librarians can:</summary>
              
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
            </details>
          
      * Part-time librarians can be hired by full-time librarians
          * <details>
              <summary>Part-time librarians can:</summary>
              
              * Remove Book
              * Check Book Availability
              * View All Books
              * View All Members
              * Log In
              * Log Out 
              * They can also check out and return books, but they need a Member's ID.
                * If they try to check out a book that doesn't exist, they can have a full-time librarian sign-in to approve ordering the book.
            </details>
    </details>
