package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public ArrayList<Data> readFile(String name) throws FileNotFoundException{
        ArrayList<Data> data = new ArrayList<>();
        File file = new File(name);
            String line;
            Scanner finput = new Scanner(file);
            while (finput.hasNext()) {
                line = finput.nextLine();
                String[] temp = line.split(";");
                Integer t =Integer.parseInt(temp[2]);
                data.add(new Data(temp[0],temp[1],t));
            }
            finput.close();
            return data;

    }
}
