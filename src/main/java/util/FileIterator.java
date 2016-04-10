package util;

import java.io.*;

public class FileIterator {
    private BufferedReader fin;
    private String currentLine;

    public FileIterator(String filename) throws FileNotFoundException {
        fin = getReader(filename);
    }

    private BufferedReader getReader(String filename) throws FileNotFoundException {
        FileInputStream is = new FileInputStream(filename);
        InputStreamReader isReader = new InputStreamReader(is);
        return new BufferedReader(isReader);
    }

    public boolean hasNext() {
        try {
            currentLine = fin.readLine();
            if (currentLine == null) {
                fin.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return currentLine != null;
    }

    public String next() {
        return currentLine;
    }
}
