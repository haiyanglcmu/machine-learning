package util;

import decisiontree.Attribute;
import decisiontree.Dataset;

import java.io.FileNotFoundException;

public class DataLoader {
    public static void main(String[] args) throws FileNotFoundException {
        load(args[0]);
    }

    public static Dataset load(String filename) throws FileNotFoundException {
        Dataset ds = new Dataset();

        FileIterator fin = new FileIterator(filename);
        while (fin.hasNext()) {
            String line = fin.next().trim();
            if (line.equals("") || line.startsWith("@relation") || line.startsWith("@data")) {
                continue;
            }

            if (line.startsWith("@attribute")) {
                String[] splits = line.split(" ");
                String attrName = splits[1];
                String attrValues = splits[2];
                if (attrValues.equals("real")) {
                    ds.addAttribute(attrName, Attribute.AttributeType.NUMERIC);
                } else {
                    ds.addAttribute(attrName, Attribute.AttributeType.NOMINAL);
                }
            } else {
                ds.addInstance(line);
            }
        }

        // System.out.println(ds);

        return ds;
    }
}
