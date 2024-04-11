import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Zebra {
    private final static int size = 5;
    private final static HashMap<String, Integer> propsToInt = new HashMap<String, Integer>() {{
        put("red", 0);
        put("green", 1);
        put("ivory", 2);
        put("yellow", 3);
        put("blue", 4);

        put("Englishman", 5);
        put("Spaniard", 6);
        put("Ukrainian", 7);
        put("Norwegian", 8);
        put("Japanese", 9);

        put("coffee", 10);
        put("tea", 11);
        put("milk", 12);
        put("orange juice", 13);
        put("water", 14);

        put("Old Gold", 15);
        put("Kools", 16);
        put("Chesterfields", 17);
        put("Lucky Strike", 18);
        put("Parliaments", 19);

        put("dog", 20);
        put("snails", 21);
        put("fox", 22);
        put("horse", 23);
        put("zebra", 24);
    }};
    private final static ArrayList<String> props = new ArrayList<String>() {{
        add("red");
        add("green");
        add("ivory");
        add("yellow");
        add("blue");

        add("Englishman");
        add("Spaniard");
        add("Ukrainian");
        add("Norwegian");
        add("Japanese");

        add("coffee");
        add("tea");
        add("milk");
        add("orange juice");
        add("water");

        add("Old Gold");
        add("Kools");
        add("Chesterfields");
        add("Lucky Strike");
        add("Parliaments");

        add("dog");
        add("snails");
        add("fox");
        add("horse");
        add("zebra");
    }};
    private static int encode(int propriety, int house) {
        return propriety * size + house + 1;
    }
    private static int[] decode(int code) {
        return new int[] {(code - 1) / size, (code - 1) % size};
    }
    private static void distinctHouse(FileWriter writer) {
        try {
            // each house has at least one from each propriety
            for (int propc = 0; propc < props.size(); propc+=5) {
                for (int house = 0; house < size; house++) {
                    for (int prop = propc; prop < propc + size; prop++) {
                        writer.write(encode(prop, house) + " ");
                    }
                    writer.write("0\n");
                }
            }

            // each house has at most one from each propriety
            for (int propc = 0; propc < props.size(); propc+=5) {
                for (int house = 0; house < size; house++) {
                    for (int prop = propc; prop < propc + size; prop++) {
                        for (int prop2 = prop + 1; prop2 < propc + size; prop2++) {
                            writer.write("-" + encode(prop, house) + " -" + encode(prop2, house) + " 0\n");
                        }
                    }
                }
            }

            // each house has different proprieties
            for (int propc = 0; propc < props.size(); propc+=5) {
                for (int house = 0; house < size; house++) {
                    for (int prop = propc; prop < propc + size; prop++) {
                        for (int house2 = house + 1; house2 < size; house2++) {
                            writer.write("-" + encode(prop, house) + " -" + encode(prop, house2) + " 0\n");
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    // IF prop1 THEN prop2
    private static void directEquivalence(FileWriter writer, int prop1, int prop2, int start, int end, int step) {
        try {
            for (int house = start; house < end; house++) {
                writer.write("-" + encode(prop1, house) + " " + encode(prop2, house + step) + " 0\n");
                writer.write(encode(prop1, house) + " -" + encode(prop2, house + step) + " 0\n");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    // IF prop1 THEN prop2 OR prop3
    private static void inDirectEquivalence(FileWriter writer, int prop1, int prop2, int start, int end, int step) {
        try {
            for (int house = start; house < end; house++) {
                writer.write("-" + encode(prop1, house) + " " + encode(prop2, house + step) + " " + encode(prop2, house - step) + " 0\n");
                writer.write(encode(prop1, house) + " -" + encode(prop2, house + step) + " -" + encode(prop2, house - step) + " 0\n");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("encoded.cnf");

            // 1. There are five different houses.
            distinctHouse(writer);

            // 2. The Englishman lives in the red house.
            directEquivalence(writer, propsToInt.get("Englishman"), propsToInt.get("red"), 0, size, 0);

            // 3. The Spaniard owns the dog.
            directEquivalence(writer, propsToInt.get("Spaniard"), propsToInt.get("dog"), 0, size, 0);

            // 4. Coffee is drunk in the green house.
            directEquivalence(writer, propsToInt.get("coffee"), propsToInt.get("green"), 0, size, 0);

            // 5. The Ukrainian drinks tea.
            directEquivalence(writer, propsToInt.get("Ukrainian"), propsToInt.get("tea"), 0, size, 0);

            // 6. The green house is immediately to the right of the ivory house.
            directEquivalence(writer, propsToInt.get("ivory"), propsToInt.get("green"), 0, size - 1, 1);
            writer.write("-" + encode(propsToInt.get("green"), 0) + " 0\n");
            writer.write("-" + encode(propsToInt.get("ivory"), size - 1) + " 0\n");

            // 7. The Old Gold smoker owns snails.
            directEquivalence(writer, propsToInt.get("Old Gold"), propsToInt.get("snails"), 0, size, 0);

            // 8. Kools are smoked in the yellow house.
            directEquivalence(writer, propsToInt.get("Kools"), propsToInt.get("yellow"), 0, size, 0);

            // 9. Milk is drunk in the middle house.
            writer.write(encode(propsToInt.get("milk"), size / 2) + " 0\n");

            // 10. The Norwegian lives in the first house.
            writer.write(encode(propsToInt.get("Norwegian"), 0) + " 0\n");

            // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
            writer.write("-" + encode(propsToInt.get("Chesterfields"), 0) + " " + encode(propsToInt.get("fox"), 1) + " 0\n");
            writer.write(encode(propsToInt.get("Chesterfields"), 0) + " -" + encode(propsToInt.get("fox"), 1) + " 0\n");

            inDirectEquivalence(writer, propsToInt.get("Chesterfields"), propsToInt.get("fox"), 1, size - 1, 1);

            writer.write("-" + encode(propsToInt.get("Chesterfields"), size - 1) + " " + encode(propsToInt.get("fox"), size - 2) + " 0\n");
            writer.write(encode(propsToInt.get("Chesterfields"), size - 1) + " -" + encode(propsToInt.get("fox"), size - 2) + " 0\n");

            // 12. Kools are smoked in the house next to the house where the horse is kept.
            writer.write("-" + encode(propsToInt.get("Kools"), 0) + " " + encode(propsToInt.get("horse"), 1) + " 0\n");
            writer.write(encode(propsToInt.get("Kools"), 0) + " -" + encode(propsToInt.get("horse"), 1) + " 0\n");

            inDirectEquivalence(writer, propsToInt.get("Kools"), propsToInt.get("horse"), 1, size - 1, 1);

            writer.write("-" + encode(propsToInt.get("Kools"), size - 1) + " " + encode(propsToInt.get("horse"), size - 2) + " 0\n");
            writer.write(encode(propsToInt.get("Kools"), size - 1) + " -" + encode(propsToInt.get("horse"), size - 2) + " 0\n");

            // 13. The Lucky Strike smoker drinks orange juice.
            directEquivalence(writer, propsToInt.get("Lucky Strike"), propsToInt.get("orange juice"), 0, size, 0);

            // 14. The Japanese smokes Parliaments.
            directEquivalence(writer, propsToInt.get("Japanese"), propsToInt.get("Parliaments"), 0, size, 0);

            // 15. The Norwegian lives next to the blue house.
            directEquivalence(writer, propsToInt.get("Norwegian"), propsToInt.get("blue"), 0, size - 1, 1);

            writer.close();

            // run minisat
            Process p = Runtime.getRuntime().exec("minisat encoded.cnf solution.txt");
            p.waitFor();

            // Read the solution
            BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("solution.txt"));
            String sat = reader.readLine();

            // Analyze the solution
            if (sat.equals("SAT")) {
                String[] solution = reader.readLine().split(" ");
                // Initialize the houses table
                ArrayList<ArrayList<String>> houses = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    houses.add(new ArrayList<>());
                }
                // Decode the solution
                int code;
                for(int i = 0; i < solution.length; i++) {
                    code = Integer.parseInt(solution[i]);
                    if(code > 0) {
                        int[] decoded = decode(code);
                        houses.get(decoded[1]).add(props.get(decoded[0]));
                    }
                }
                // Print the solution
                for (int i = 0; i < size; i++) {
                    System.out.println("House " + (i + 1) + ": " + houses.get(i));
                }
            }
            else {
                System.out.println("Unsatisfiable");
            }

        }
        catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
