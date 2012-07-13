package org.hisp.dhis.i18nresourceeditor.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

/**
 * @author Saptarshi Purkayastha
 */
public class UnicodeTransferHandler extends TransferHandler {

    JTextArea output;

    public void TransferHandler() {
    }

    @Override
    public boolean canImport(JComponent dest, DataFlavor[] flavors) {
        // you bet we can!
        return true;
    }

    @Override
    public boolean importData(JComponent src, Transferable transferable) {
        // Ok, here's the tricky part...
        println("Receiving data from " + src);
        println("Transferable object is: " + transferable);
        println("Valid data flavors: ");
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        DataFlavor listFlavor = null;
        DataFlavor objectFlavor = null;
        DataFlavor readerFlavor = null;
        int lastFlavor = flavors.length - 1;

        // Check the flavors and see if we find one we like.
        // If we do, save it.
        for (int f = 0; f <= lastFlavor; f++) {
            println("  " + flavors[f]);
            if (flavors[f].isFlavorJavaFileListType()) {
                listFlavor = flavors[f];
            }
            if (flavors[f].isFlavorSerializedObjectType()) {
                objectFlavor = flavors[f];
            }
            if (flavors[f].isRepresentationClassReader()) {
                readerFlavor = flavors[f];
            }
        }

        // Ok, now try to display the content of the drop.
        try {
            DataFlavor bestTextFlavor = DataFlavor.selectBestTextFlavor(flavors);
            BufferedReader br = null;
            String line = null;
            if (bestTextFlavor != null) {
                println("Best text flavor: " + bestTextFlavor.getMimeType());
                println("Content:");
                Reader r = bestTextFlavor.getReaderForText(transferable);
                br = new BufferedReader(r);
                line = br.readLine();
                while (line != null) {
                    println(line);
                    line = br.readLine();
                }
                br.close();
            } else if (listFlavor != null) {
                List<?> list = (List<?>) transferable.getTransferData(listFlavor);
                println(list);
            } else if (objectFlavor != null) {
                println("Data is a java object:\n" + transferable.getTransferData(objectFlavor));
            } else if (readerFlavor != null) {
                println("Data is an InputStream:");
                br = new BufferedReader((Reader) transferable.getTransferData(readerFlavor));
                line = br.readLine();
                while (line != null) {
                    println(line);
                }
                br.close();
            } else {
                // Don't know this flavor type yet...
                println("No text representation to show.");
            }
            println("\n\n");
        } catch (Exception e) {
            println("Caught exception decoding transfer:");
            println(e);
            return false;
        }
        return true;
    }

    @Override
    public void exportDone(JComponent source, Transferable data, int action) {
        // Just let us know when it occurs...
        System.err.println("Export Done.");
    }

    public void setOutput(JTextArea jta) {
        output = jta;
    }

    protected void print(Object o) {
        print(o.toString());
    }

    protected void print(String s) {
        if (output != null) {
            output.append(s);
        } else {
            System.out.println(s);
        }
    }

    protected void println(Object o) {
        println(o.toString());
    }

    protected void println(String s) {
        if (output != null) {
            output.append(s);
            output.append("\n");
        } else {
            System.out.println(s);
        }
    }

    protected void println() {
        println("");
    }
}