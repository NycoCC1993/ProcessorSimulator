import java.util.Scanner;

public class Executor {
    public static void main(String theory[]) {
        //When using cmd, you must append --cmd to the execution (or -c)
        //TODO: Make the system acknowledge that --cmd or -c have been used, and not just that there is something on the line
        boolean usingIDE = true;
        if (theory.length > 0) {
            usingIDE = false;
        }
        System.out.println("Nyco Computing Company (c) 1993\n\"Monotonoj System\"");
        Processor p1 = new Processor();
        //Processor p2 = new Processor((short)32, 32);

        //Parser parser1 = new Parser(usingIDE);
        String selection = "countToTen";

        //Allow user to input option if running on command line
        if (!usingIDE) {
            Scanner selector = new Scanner(System.in);
            System.out.print("Please type the name of the file you wish to use (exclude .pars extension) - ");
            selection = selector.nextLine();
        }

        Parser parser2 = new Parser(selection, usingIDE);
        parser2.parse();

        for (String each : parser2.instructions) {
            System.out.println(each);
        }

        System.out.println("-----");

        p1.execute(parser2);
    }
}
