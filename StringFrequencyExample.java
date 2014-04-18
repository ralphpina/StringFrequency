import java.io.IOException;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;

public class StringFrequencyExample {
 
    public static void main(String[] args) {
   
        String string = null;

        try {
            string = readFile(args[0], StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("There was a problem reading the file!");
            e.printStackTrace();
        }
    
        if (string != null) {
            StringFrequency stringFrequency = new StringFrequency();
            List<String> list = stringFrequency.getSortedWordsByApperance(string, 200);

            // print out the list for testing
            //      for (int i = 0; i < list.size(); i++) {
            //        System.out.println(i + " " + list.get(i));
            //      }
        }
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}