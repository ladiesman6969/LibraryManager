import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class book {
    String id;
    String name;
    String author;
    String publisher;
    String price;
    String isbn;
    boolean doesExist;
    boolean isBookAvaialable;
    String bookTakenBy;

    public book(String idPassed)
    {
        try
        {
            File bookFile = new File("books/"+idPassed);
            if(bookFile.exists())
            {
                BufferedReader bf = new BufferedReader(new FileReader(bookFile));
                String content = bf.readLine();
                String part[] = content.split(";");
                bf.close();

                id = idPassed;
                name = part[0];
                author = part[1];
                publisher = part[2];
                price = part[3];
                isbn = part[4];
                bookTakenBy = part[6];

                String bookAvaStatus = part[5];
                if(bookAvaStatus.equals("0"))
                {
                    isBookAvaialable = false;
                }
                else if(bookAvaStatus.equals("1"))
                {
                    isBookAvaialable = true;
                }

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
}
