public class Executor {
    public static void main(String theory[]) {
        System.out.println("Nyco Computing Company (c) 1993\n\"Monotonoj System\"");
        Processor p1 = new Processor();
        Processor p2 = new Processor((short)32, 32);

        Parser parser1 = new Parser();
        Parser parse2 = new Parser("jetbrains");
        parser1.parse();

        for (String each : parser1.instructions) {
            System.out.println(each);
        }

        System.out.println("-----");

        p1.execute(parser1);
    }
}
