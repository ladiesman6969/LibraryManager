import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class user
{
    String username;
    String firstName;
    String lastName;
    String password;
    int privilege;
    boolean doesExist;
    String firstLine;
    int booksTillDate;
    String dateOfJoin;

    HashMap<String,String[]> bookTransaxStatus = new HashMap<>();
    ArrayList<String> booksTransactionID = new ArrayList<>();

    String generateTransactionID()
    {
        int random = (int)(Math.random() * 32000 + 1);
        return random+"";
    }

    public user(String fileName)
    {
        try
        {
            File userFile = new File("users/"+fileName);
            if(userFile.exists())
            {
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                firstLine = reader.readLine();

                String part[] = firstLine.split(";");
                username = part[0];
                firstName = part[1];
                lastName = part[2];
                booksTillDate = Integer.parseInt(part[3]);
                password = part[4];
                privilege = Integer.parseInt(part[5]);
                dateOfJoin = part[6];

                for(int i =0;i<booksTillDate;i++)
                {
                    String line = reader.readLine();
                    String part2[] = line.split(";");
                    String config[] = new String[4];
                    config[0] = part2[0];
                    config[1] = part2[1];
                    config[2] = part2[2];
                    config[3] = part2[3];

                    booksTransactionID.add(config[0]);
                    bookTransaxStatus.put(config[0], new String[]{config[1],config[2],config[3]});
                }

                reader.close();
                doesExist = true;
            }
            else
            {
                doesExist = false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            doesExist = false;
        }
    }

    ArrayList<String> booksToReturn()
    {
        ArrayList<String> booksToReturn = new ArrayList<>();
        for(String eachTransactionID : booksTransactionID)
        {
            book eachBook = new book(bookTransaxStatus.get(eachTransactionID)[0]);
            String[] issueReturnDates = bookTransaxStatus.get(eachTransactionID);
            if(issueReturnDates[2].equals("NOT_RETURNED"))
            {
                booksToReturn.add(eachTransactionID);
            }
        }
        return booksToReturn;
    }

    void returnBook(String transactionID)
    {
        //TODO : SimpleDateFormat Given Incorrect!
        try
        {
            String[] bookData = bookTransaxStatus.get(transactionID);
            String bookID = bookData[0];
            io.println("Returning book "+bookID+"...");
            Date date = new Date();
            String timeStampFormat = "ddmmyyyy";
            DateFormat returnTimeStamp = new SimpleDateFormat(timeStampFormat);
            String formattedTimeStamp= returnTimeStamp.format(date);

            String[] dates = new String[3];
            dates[0] = bookID;
            dates[1] = bookTransaxStatus.get(transactionID)[1];
            dates[2] = formattedTimeStamp;


            bookTransaxStatus.put(transactionID,dates);

            reloadTransax();

            io.println("Returned!");
        }
        catch (Exception e)
        {
            io.println("... Unable to Return Book!");
            e.printStackTrace();
        }
    }

    void issueBook(String bookID)
    {
        book bookToBeIssued = new book(bookID);
        if(bookToBeIssued.doesExist)
        {
            boolean isAlreadyIssuedAndNotReturned = false;
            for(String eachTransactionID : booksTransactionID)
            {
                String[] bookData = bookTransaxStatus.get(eachTransactionID);
                if(bookData[0].equals(bookID) && bookData[2].equals("NOT_RETURNED"))
                {
                    isAlreadyIssuedAndNotReturned = true;
                }
            }
            if(isAlreadyIssuedAndNotReturned)
            {
                io.println("You already issued this book an didnt return it!");
            }
            else
            {
                io.println("Book Details :\n" +
                        "Name : "+bookToBeIssued.name+"\n" +
                        "Author : "+bookToBeIssued.author+"\n" +
                        "ISBN : "+bookToBeIssued.isbn+"\n" +
                        "Publisher : "+bookToBeIssued.publisher+"\n" +
                        "Price : "+bookToBeIssued.price+"\n\n" +
                        "Are you sure that you want to issue this book [Y/N]");
                String userFinalIssueChoice = io.readLine();
                if(userFinalIssueChoice.toUpperCase().equals("Y"))
                {
                    try
                    {
                        io.println("Issuing book "+bookID+"...");
                        Date date = new Date();
                        String timeStampFormat = "ddmmyyyy";
                        DateFormat returnTimeStamp = new SimpleDateFormat(timeStampFormat);
                        String formattedTimeStamp= returnTimeStamp.format(date);

                        String newTransacID = generateTransactionID();
                        String[] config = new String[3];
                        config[0] = bookID;
                        config[1] = formattedTimeStamp;
                        config[2] = "NOT_RETURNED";
                        bookTransaxStatus.put(newTransacID,config);
                        booksTransactionID.add(newTransacID);

                        booksTillDate++;
                        reloadTransax();

                        io.println("...Successfully Issued!");
                    }
                    catch (Exception e)
                    {
                        io.println("... Unable to issue book! Contact admin!");
                        e.printStackTrace();
                    }
                }
                else
                {
                    io.println("Cancelled.");
                }
            }
        }
        else
        {
            io.println("Sorry that book could not be found. Please contact Administrator for more info.");
        }
    }

    void reloadTransax() throws Exception
    {

        FileWriter fw = new FileWriter("users/"+username);
        fw.write(username+";"+firstName+";"+lastName+";"+booksTillDate+";"+password+";"+privilege+";"+dateOfJoin+";\n");
        for(int i =0;i<booksTransactionID.size();i++)
        {
            String[] config = bookTransaxStatus.get(booksTransactionID.get(i));
            fw.write(booksTransactionID.get(i)+";"+config[0]+";"+config[1]+";"+config[2]+";\n");
        }
        fw.close();
    }

    void printAllBooksInfoTillDate()
    {
        io.println("All books issued till date :-");
        int i = 1;
        for(String eachTransactionID : booksTransactionID)
        {
            String[] eachConfig = bookTransaxStatus.get(eachTransactionID);
            book eachBook = new book(eachConfig[0]);
            if(eachBook.doesExist)
            {
                io.println(i+". "+eachBook.id+" -- "+eachBook.name+" -- "+eachBook.author+" -- "+eachBook.isbn+" --  "+eachBook.price);
            }
            else
            {
                io.println(i+". "+eachBook.id+" -- Book unavailable!");
            }
            i++;
        }
    }

    void printProfileDetails()
    {
        io.println("Profile Details :-\n" +
                "Full Name : "+firstName+" "+lastName+"\n" +
                "Username : "+username+"\n" +
                "Account Privilege : "+privilege+"\n" +
                "No. of Books Issued till date : "+booksTransactionID.size()+"\n" +
                "Date Joined (DD/MM/YYYY) : "+dateOfJoin);
    }
}