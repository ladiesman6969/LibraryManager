import java.io.BufferedReader;
import java.io.InputStreamReader;

public class io {
    public static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine()
    {
        try
        {
            return bf.readLine();
        }
        catch (Exception e)
        {
            println("ERROR OCCURED!\nStackTrace:\n\n");
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public static void print(String a)
    {
        System.out.print(a);
    }

    public static void println(String a)
    {
        System.out.println(a);
    }
}
