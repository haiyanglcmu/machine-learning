package util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static void main(String[] args) throws FileNotFoundException {
        load(args[0]);
    }

    public static Dataset load(String filename) throws FileNotFoundException {
        Dataset ds = new Dataset();

        FileIterator fin = new FileIterator(filename);
        List<String> attrLines = new ArrayList<String>();
        while (fin.hasNext()) {
            String line = fin.next().trim();
            if (line.equals("") || line.startsWith("@relation")) {
                continue;
            }

            if (line.startsWith("@attribute")) {
                attrLines.add(line);
            } else if (line.startsWith("@data")){
                attrLines.remove(attrLines.size() - 1);
                for (String attrLine : attrLines) {
                    String[] splits = attrLine.split(" ");
                    String attrName = splits[1];
                    String attrValues = splits[2];
                    if (attrValues.equals("real")) {
                        ds.addAttribute(attrName, Attribute.AttributeType.NUMERIC);
                    } else {
                        ds.addAttribute(attrName, Attribute.AttributeType.NOMINAL);
                    }
                }
            } else {
                ds.addInstance(line);
            }
        }

        return ds;
    }
}
