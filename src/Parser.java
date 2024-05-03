import java.util.*;
import java.io.*;
public class Parser {
    File document = null;
    ArrayList<String> instructions = new ArrayList<String>();
    public Parser(boolean usingIDE) {
        try {
            //This check determines if the IDE is being used, or if code is run from cmd
            if (usingIDE) {
                document = new File("files/input.pars");
            } else {
                document = new File("../files/input.pars");
            }

            if (document.createNewFile()) {
                System.out.println("A file was created - " + document.getName());
            } else {
                System.out.println("Using the pre-existing file - " + document.getName());
            }
        } catch (Exception noDefaultFile) {
            System.err.println("ERRORS on file : " + document.getName());
            noDefaultFile.printStackTrace();
        }
    }

    //TODO: Modify Parser so it can deel with both .pars (human readable) and .poek (machine language opcodes)
    public Parser(String fileName, boolean usingIDE) {
        String filefull = fileName.concat(".pars");
        if (usingIDE) {
            filefull = "files/".concat(filefull);
        } else {
            filefull = "../files/".concat(filefull);
        }

        try {
            document = new File(filefull);
            if (document.createNewFile()) {
                System.out.println("A file was created - " + document.getName());
            } else {
                System.out.println("Using the pre-existing file - " + document.getName());
            }
        } catch (Exception noDefaultFile) {
            System.err.println("ERRORS on file : " + document.getName());
            noDefaultFile.printStackTrace();
            System.exit(-500);
        }
    }

    /**
     * Converts the instructions in the .pars file into Strings stored in the instructionsText list.
     * These instructions must be interpreted by the Processor.
     */
    public void parse() {
        try {
            Scanner scanner = new Scanner(document);
            while (scanner.hasNextLine()) {
                String save = scanner.nextLine();
                save = save.substring(0, save.indexOf(";"));
                if (!save.isEmpty()) {
                    instructions.add(save);
                }
            }
            scanner.close();
        } catch (FileNotFoundException invalidFile) {
            System.err.println("File read resulted in failure!");
            invalidFile.printStackTrace();
            System.exit(-404);
        }
    }
}
