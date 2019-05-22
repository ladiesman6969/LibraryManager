import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        mainMenu(true);
    }

    static user currentUser;

    static void mainMenu(boolean showIntro)
    {
        if(showIntro)
        {
            io.print("LIBRARY MANAGMENT SYSTEM\n" +
                    "By Debayan\n");
        }

        io.print("Select any one of the following\n" +
                "1. Login\n" +
                "2. Exit\n\n");

        String userResponse = io.readLine();
        io.println(userResponse);

        if(userResponse.equals("1"))
        {
            showLoginPrompt();
        }
        else if(userResponse.equals("2"))
        {
            System.exit(0);
        }
        else
        {
            io.println("Incorrect Option!");
            mainMenu(false);
        }
    }

    static void showLoginPrompt()
    {
        io.println("Username : ");
        String usernamePassed = io.readLine();
        io.println("Password : ");
        String passwordPassed = io.readLine();

        user loginUser = new user(usernamePassed);
        if(loginUser.doesExist)
        {
            if(loginUser.password.equals(passwordPassed))
            {
                io.println("Logged In!");
                currentUser = loginUser;
                if(loginUser.privilege == 1)
                {
                    userMenu(true);
                }
                else if(loginUser.privilege == 0)
                {
                    userMenu(true);
                }
            }
            else
            {
                io.println("Incorrect Password!");
                mainMenu(false);
            }
        }
        else
        {
            io.println("Incorrect Username/Password");
            mainMenu(false);
        }
    }

    static void userMenu(boolean showWelcome)
    {
        if(showWelcome)
        {
            io.println("---- Welcome "+currentUser.firstName+"! ----");
        }

        if(currentUser.privilege == 0)
        {
            io.println("Menu\n" +
                    "1. Return a book\n" +
                    "2. Issue a book\n" +
                    "3. All books issued till date\n" +
                    "4. Profile Details\n" +
                    "5. Logout");
        }
        else
        {
            io.println("Menu\n" +
                    "1. Get Profile Details (by username)\n" +
                    "2. List all Users\n" +
                    "3. Delete a user (by username)\n" +
                    "4. Register a new user!\n" +
                    "5. Get Book Details (by Book ID)\n" +
                    "6. List All Books of the library\n" +
                    "7. Register a new book\n" +
                    "8. Delete a book (by Book ID)\n" +
                    "9. Logout");
        }

        String choice = io.readLine();

        if(currentUser.privilege == 0)
        {
            if(choice.equals("1"))
            {
                while(true)
                {
                    ArrayList<String> booksToReturnTransactionID = currentUser.booksToReturn();
                    if(booksToReturnTransactionID.size() == 0)
                    {
                        io.println("No books to return!");
                        userMenu(false);
                        break;
                    }
                    else
                    {
                        io.println("Books To Be Returned : ");
                        int i = 0;
                        io.println("-1. Return");
                        ArrayList<String> booksToReturnTIDFiltered = new ArrayList<>();
                        for(String eachTransactionID: booksToReturnTransactionID)
                        {
                            book eachBook = new book(currentUser.bookTransaxStatus.get(eachTransactionID)[0]);
                            io.println(i+". "+eachBook.name+"     "+eachBook.author+"     "+eachBook.id+"     "+eachBook.isbn);
                            i++;
                            booksToReturnTIDFiltered.add(eachTransactionID);
                        }

                        io.println("Which one to return ?");
                        try
                        {
                            int userChoice = Integer.parseInt(io.readLine());
                            if(userChoice == -1)
                            {
                                userMenu(false);
                                break;
                            }
                            else
                            {
                                currentUser.returnBook(booksToReturnTIDFiltered.get(userChoice));
                                io.println("Returning to main menu ...");
                                userMenu(false);
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            io.println("Invalid Choice Entered");
                        }
                    }
                }
            }
            else if(choice.equals("2"))
            {
                while(true)
                {
                    io.println("Enter the book ID you want to issue \nEnter -1 to return");
                    String bookToBeIssuedIDEntered = io.readLine();
                    if(bookToBeIssuedIDEntered.equals("-1"))
                    {
                        userMenu(false);
                    }
                    else
                    {
                        currentUser.issueBook(bookToBeIssuedIDEntered);
                    }
                }
            }
            else if(choice.equals("3"))
            {
                currentUser.printAllBooksInfoTillDate();
                userMenu(false);
            }
            else if(choice.equals("4"))
            {
                currentUser.printProfileDetails();
                userMenu(false);
            }
            else if(choice.equals("5"))
            {
                mainMenu(false);
            }
            else
            {
                io.println("Invalid Option!");
                userMenu(false);
            }
        }
        else if(currentUser.privilege == 1)
        {
            if(choice.equals("1"))
            {
                while(true)
                {
                    io.println("Enter profile username\nEnter -1 to return\n");
                    String usernameEntered = io.readLine();
                    if(usernameEntered.equals("-1"))
                    {
                        userMenu(false);
                        break;
                    }
                    else
                    {
                        user requiredUser = new user(usernameEntered);
                        if(requiredUser.doesExist)
                        {
                            requiredUser.printProfileDetails();
                            if(requiredUser.privilege == 0)
                            {
                                requiredUser.printAllBooksInfoTillDate();
                            }
                        }
                        else
                        {
                            io.println("Username "+usernameEntered+" doesn't Exist!");
                        }
                    }
                }
            }
            else if(choice.equals("2"))
            {
                String[] usernames = new File("users/").list();
                int i = 0;
                for(String eachUsername : usernames)
                {
                    user eachUser = new user(eachUsername);
                    if(eachUser.doesExist)
                    {
                        io.println(i+". ID : "+eachUsername+" Full Name : "+eachUser.firstName+" "+eachUser.lastName);
                        i++;
                    }
                }
                userMenu(false);
            }
            else if(choice.equals("3"))
            {
                while(true)
                {
                    try
                    {
                        io.println("Enter username to delete\nEnter -1 to Return");
                        String usernameToBeDeleted = io.readLine();
                        if(usernameToBeDeleted.equals("-1"))
                        {
                            userMenu(false);
                            break;
                        }
                        else if(usernameToBeDeleted.equals(currentUser.username))
                        {
                            io.println("You Cannot delete yourself.");
                        }
                        else
                        {
                            io.println("Deleting user ...");
                            File usernameFile = new File("users/"+usernameToBeDeleted);
                            if(usernameFile.exists())
                            {
                                boolean result = usernameFile.delete();
                                if(result)
                                {
                                    io.println("... Successfully removed user "+usernameToBeDeleted+"!");
                                }
                                else
                                {
                                    io.println("... Unable to removed user "+usernameToBeDeleted+". Please check file permissions and Try Again!");
                                }
                            }
                            else
                            {
                                io.println("User "+usernameToBeDeleted+" doesn't exist!");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        io.println("ERROR OCCURRED!");
                        e.printStackTrace();
                    }
                }
            }
            else if(choice.equals("4"))
            {
                while(true)
                {
                    io.println("New User Registration Page\nEnter -1 to return");
                    String firstName, lastName, username, password="", privilege;

                    boolean quitLoop = false;
                    while(true)
                    {
                        io.println("First Name :");
                        firstName = io.readLine();
                        if(firstName.length() == 0)
                        {
                            io.println("Please enter a valid First Name and Try Again!");
                        }
                        else
                        {
                            if(firstName.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Last Name :");
                        lastName = io.readLine();
                        if(lastName.length() == 0)
                        {
                            io.println("Please enter a valid Last Name and Try Again!");
                        }
                        else
                        {
                            if(lastName.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Username :");
                        username = io.readLine();
                        File uFile = new File(username);
                        if(username.length() == 0)
                        {
                            io.println("Please enter a valid username and Try Again!");
                        }
                        else if (uFile.exists())
                        {
                            io.println("Username already taken! Please try a new username!");
                        }
                        else
                        {
                            if(username.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Password :");
                        String pass1 = io.readLine();
                        if(pass1.equals("-1"))
                        {
                            quitLoop = true;
                            break;
                        }
                        if(pass1.length() < 8)
                        {
                            io.println("Please enter a valid Password (Must Contain at least 8 letters) and Try Again!");
                        }
                        else
                        {
                            io.println("Confirm Password : ");
                            String pass2 = io.readLine();
                            if(pass1.equals(pass2))
                            {
                                password = pass2;
                                break;
                            }
                            else
                            {
                                if(pass2.equals("-1"))
                                {
                                    quitLoop = true;
                                    break;
                                }
                                io.println("Two passwords dont match with either. Please Try Again!");
                            }
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Privilege\n'0' : Normal User with Non-Admin rights\n'1' : Administrator Account\n");
                        privilege = io.readLine();
                        if(privilege.equals("-1"))
                        {
                            quitLoop = true;
                            break;
                        }
                        if(!privilege.equals("0") && !privilege.equals("1"))
                        {
                            io.println("Enter a valid privilege (0/1)");
                        }
                        else
                        {
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    try
                    {
                        io.println("New user profile details:\n" +
                                "First Name : "+firstName+"\n" +
                                "Last Name : "+lastName+"\n" +
                                "Username : "+username+"\n" +
                                "Privilege : "+privilege+"\n\n" +
                                "Register user [Y/N] ?");

                        String createUserQuestionChoice = io.readLine();
                        if(createUserQuestionChoice.toUpperCase().equals("Y"))
                        {
                            Date date = new Date();
                            String timeStampFormat = "ddMMyyyy";
                            DateFormat returnTimeStamp = new SimpleDateFormat(timeStampFormat);
                            String formattedTimeStamp= returnTimeStamp.format(date);

                            File newUser = new File("users/"+username);
                            boolean newUserFileCreationResult = newUser.createNewFile();
                            if(newUserFileCreationResult)
                            {
                                FileWriter fw = new FileWriter(newUser);
                                fw.write(username+";"+firstName+";"+lastName+";0;"+password+";"+privilege+";"+formattedTimeStamp+";");
                                fw.close();

                                io.println("User "+username+" successfully created!");
                            }
                            else
                            {
                                io.println("Unable to create new file for new user. Check file permissions!");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        io.println("Unable to create new user. Check file permissions!");
                        e.printStackTrace();
                    }
                }
            }
            else if(choice.equals("5"))
            {
                while(true)
                {
                    io.println("Enter the book ID, Enter -1 to return : ");
                    String uc = io.readLine();
                    if(uc.equals("-1"))
                    {
                        userMenu(false);
                        break;
                    }
                    else
                    {
                        book requiredBook = new book(uc);
                        if(requiredBook.doesExist)
                        {
                            io.println("\n\n\n----BOOK DETAILS----" +
                                    "\nID : "+requiredBook.id+
                                    "\nName : "+requiredBook.name+
                                    "\nIs available : "+requiredBook.isBookAvaialable+
                                    "\nCurrently Issued By (null if not taken) : "+requiredBook.bookTakenBy+
                                    "\nPrice : "+requiredBook.price+
                                    "\nPublisher : "+requiredBook.publisher+
                                    "\nISBN : "+requiredBook.isbn+"\n\n\n");
                        }
                        else
                        {
                            io.println("Book ID "+uc+" does not exist!");
                        }
                    }
                }
            }
            else if(choice.equals("6"))
            {
                String[] bookFiles = new File("books/").list();
                io.println("---All books currently present in the library---");
                int i = 1;
                for(String eachBookFile : bookFiles)
                {
                    book eachBook = new book(eachBookFile);
                    io.println(i+". ID : "+eachBook.id+
                            "\nName : "+eachBook.name+
                            "\nIs available : "+eachBook.isBookAvaialable+
                            "\nCurrently Issued By (null if not taken) : "+eachBook.bookTakenBy+
                            "\nPrice : "+eachBook.price+
                            "\nPublisher : "+eachBook.publisher+
                            "\nISBN : "+eachBook.isbn+"\n\n");
                    i++;
                }
                userMenu(false);
            }
            else if(choice.equals("7"))
            {
                while(true)
                {
                    io.println("New Book Registration Page\nEnter -1 to return");
                    String bookID, name, author, publisher, price, isbn;

                    boolean quitLoop = false;

                    while(true)
                    {
                        io.println("BOOK ID  :");
                        bookID = io.readLine();
                        book thisBook = new book(bookID);
                        if(bookID.length() == 0)
                        {
                            io.println("Please enter a ID and Try Again!");
                        }
                        else if (thisBook.doesExist)
                        {
                            io.println("A book with the same book ID already taken! Please try a new ID!");
                        }
                        else
                        {
                            if(bookID.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Book Name :");
                        name = io.readLine();
                        if(name.length() == 0)
                        {
                            io.println("Please enter a valid Book Name and Try Again!");
                        }
                        else
                        {
                            if(name.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Publisher :");
                        publisher = io.readLine();
                        if(publisher.length() == 0)
                        {
                            io.println("Please enter a valid Publisher and Try Again!");
                        }
                        else
                        {
                            if(publisher.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Author Full Name :");
                        author = io.readLine();
                        if(author.length() == 0)
                        {
                            io.println("Please enter a valid Author Name and Try Again!");
                        }
                        else
                        {
                            if(author.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }


                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("Price :");
                        price = io.readLine();
                        if(price.length() == 0)
                        {
                            io.println("Please enter a valid price and Try Again!");
                        }
                        else
                        {
                            if(price.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }

                    while(true)
                    {
                        io.println("ISBN :");
                        isbn = io.readLine();
                        if(isbn.length() == 0)
                        {
                            io.println("Please enter a valid ISBN and Try Again!");
                        }
                        else
                        {
                            if(isbn.equals("-1"))
                            {
                                quitLoop = true;
                            }
                            break;
                        }
                    }

                    if(quitLoop)
                    {
                        userMenu(false);
                        break;
                    }



                    try
                    {
                        io.println("New book details:\n" +
                                "ID : "+bookID+"\n" +
                                "Name : "+name+"\n" +
                                "Author : "+author+"\n" +
                                "Publisher : "+publisher+"\n" +
                                "Price : "+price+"\n" +
                                "ISBN : "+isbn+"\n" +
                                "Register Book [Y/N] ?");

                        String createBookQuestionChoice = io.readLine();
                        if(createBookQuestionChoice.toUpperCase().equals("Y"))
                        {
                            File newBook = new File("books/"+bookID);
                            boolean newBookFileCreationResult = newBook.createNewFile();
                            if(newBookFileCreationResult)
                            {
                                FileWriter fw = new FileWriter(newBook);
                                fw.write(name+";"+author+";"+publisher+";"+price+";"+isbn+";1;null;");
                                fw.close();

                                io.println("Book "+bookID+" successfully created!");
                            }
                            else
                            {
                                io.println("Unable to create new file for new Book. Check file permissions!");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        io.println("Unable to create new Book. Check file permissions!");
                        e.printStackTrace();
                    }
                }
            }
            else if(choice.equals("8"))
            {
                while(true)
                {
                    try
                    {
                        io.println("Enter book ID to delete\nEnter -1 to Return");
                        String bookIDToBeDeleted = io.readLine();
                        if(bookIDToBeDeleted.equals("-1"))
                        {
                            userMenu(false);
                            break;
                        }
                        else
                        {
                            io.println("Deleting book ...");
                            File bookFile = new File("books/"+bookIDToBeDeleted);
                            if(bookFile.exists())
                            {
                                boolean result = bookFile.delete();
                                if(result)
                                {
                                    io.println("... Successfully removed Book "+bookIDToBeDeleted+"!");
                                }
                                else
                                {
                                    io.println("... Unable to removed Book "+bookIDToBeDeleted+". Please check file permissions and Try Again!");
                                }
                            }
                            else
                            {
                                io.println("Book "+bookIDToBeDeleted+" doesn't exist!");
                                io.println("Book "+bookIDToBeDeleted+" doesn't exist!");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        io.println("ERROR OCCURRED!");
                        e.printStackTrace();
                    }
                }
            }
            else if(choice.equals("9"))
            {
                mainMenu(false);
            }
            else
            {
                io.println("Invalid Option!");
                userMenu(false);
            }
        }
    }
}
