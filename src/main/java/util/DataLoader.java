package util;

import decisiontree.Dataset;

import java.io.FileNotFoundException;

public class DataLoader {
    public static void main(String[] args) throws FileNotFoundException {
        Dataset ds = new Dataset();

        String filename = args[0];
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
                    ds.addNumericAttribute(attrName);
                } else {
                    ds.addNominalAttributes(attrName, attrValues);
                }
            } else {
                ds.addInstance(line);
            }
        }
    }
}
