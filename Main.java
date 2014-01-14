package enigma;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;

/** Enigma simulator.
 *  @author Scott Lee
 */
public final class Main {
    /** Process a sequence of encryptions and decryptions, as
     *  specified in the input from the standard input.  Print the
     *  results on the standard output. Exits normally if there are
     *  no errors in the input; otherwise with code 1. */
    public static void main(String[] unused) {
        Machine M;
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

        M = null;

        try {
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    M = new Machine();
                    configure(M, line);
                } else {
                    if (M == null) {
                        System.out.println("Doesn't have config");
                        System.exit(1);
                    }
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    private static boolean isConfigurationLine(String line) {
        return line.trim().startsWith("*");
    }

    /** Possible labels for rotors 3-5. */
    static final String[] ROTOR_LABELS = {"I", "II", "III", "IV", "V",
                                          "VI", "VII", "VIII"};

    /** Configure M according to the specification given on CONFIG,
     *  which must have the format specified in the assignment. */
    private static void configure(Machine M, String config) {
        String[] stringah = config.split("\\s+");
        if (stringah.length != 7 || !stringah[0].equals("*")
                || !stringah[6].matches("[A-Za-z]{4}$")) {
            System.out.println(
                    "Must have all parts of input, start with *, "
                    + "and be letters.");
            System.exit(1);
        }
        if (!stringah[1].equals("B") && !stringah[1].equals("C")) {
            System.out.println("Must start with reflector");
            System.exit(1);
        }
        if (!stringah[2].equals("BETA") && !stringah[2].equals("GAMMA")) {
            System.out.println("Must start with fixed rotor");
            System.exit(1);
        }
        String[] rotor345 = {stringah[3], stringah[4], stringah[5]};
        for (String item: rotor345) {
            if (!Arrays.asList(ROTOR_LABELS).contains(item)) {
                System.out.println("Non-existent Rotor");
                System.exit(1);
            }
        }
        if (stringah[3].equals(stringah[4]) || stringah[4].equals(stringah[5])
                || stringah[3].equals(stringah[5])) {
            System.out.println("Can't be equal rotors");
            System.exit(1);
        }
        Rotor reflector = new Rotor(stringah[1]);
        Rotor fixedRotor = new Rotor(stringah[2]);
        Rotor rotor3 = new Rotor(stringah[3]);
        Rotor rotor4 = new Rotor(stringah[4]);
        Rotor rotor5 = new Rotor(stringah[5]);
        Rotor[] rotors = {reflector, fixedRotor, rotor3, rotor4, rotor5};
        M.replaceRotors(rotors);
        M.setRotors(stringah[6]);
    }

    /**Return true if String LINE contains only letters and spaces. */
    private static boolean isLetters(String line) {
        char[] characters = line.toCharArray();
        for (char x : characters) {
            if (!Character.isLetter(x) && x != ' ') {
                return false;
            }
        }
        return true;
    }
    /** Return the result of converting LINE to all upper case,
     *  removing all blanks and tabs.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    private static String standardize(String line) {
        if (!isLetters(line)) {
            System.out.println(
                    "Need to have letters blanks only.");
            System.exit(1);
        }
        return line.toUpperCase().replace(" ", "");
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private static void printMessageLine(String msg) {
        for (int x = 0; x < msg.length(); x += 5) {
            if (x > 0) {
                System.out.print(" ");
            }
            if (msg.length() >= x + 5) {
                System.out.print(msg.substring(x, x + 5));
            }
            if (msg.length() < x + 5) {
                System.out.print(msg.substring(x, msg.length()));
            }
        }
        System.out.println();
    }
}
