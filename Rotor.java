package enigma;
/** Class that represents a rotor in the enigma machine.
 *  @author Scott Lee
 */
class Rotor {
    /** Size of alphabet used for plaintext and ciphertext. */
    static final int ALPHABET_SIZE = 26;
    /** Total number of rotors - 1. */
    static final int TOTAL_NUMBER = 12;
    /** Public class Rotor takes in String ROTORNAME and creates Rotors. */
    public Rotor(String rotorname) {
        _setting = 0;
        for (int i = 0; i < TOTAL_NUMBER; i += 1) {
            if (PermutationData.ROTOR_SPECS[i][0].equals(rotorname)) {
                forwardperm = PermutationData.ROTOR_SPECS[i][1];
                if (PermutationData.ROTOR_SPECS[i].length >= 3) {
                    backperm = PermutationData.ROTOR_SPECS[i][2];
                    if (PermutationData.ROTOR_SPECS[i].length == 4) {
                        notches = PermutationData.ROTOR_SPECS[i][3];
                    }
                }
            }
        }
    }

    /** Returns X mod 26. */
    static int mod(int x) {
        assert x >= -ALPHABET_SIZE;
        return (x + ALPHABET_SIZE) % ALPHABET_SIZE;
    }

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        assert p >= 0 && p < ALPHABET_SIZE;
        return (char) ('A' + p);
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        assert c >= 'A' && 'Z' >= c;
        return c - 'A';
    }

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return true;
    }

    /** Returns true iff this rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return true;
    }

    /** Return my current rotational setting as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */
    int getSetting() {
        return _setting;
    }

    /** Set getSetting() to POSN.  */
    void set(int posn) {
        _setting = mod(posn);
    }

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {
        char a = forwardperm.charAt(mod(p + _setting));
        return mod(toIndex(a) - _setting);
    }

    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        char b = backperm.charAt(mod(e + _setting));
        return mod(toIndex(b) - _setting);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return notches.contains(Character.toString(toLetter(
                mod(_setting))));
    }
    /** Advance me one position. */
    void advance() {
        _setting = mod(_setting + 1);
    }

    /** My current setting (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int _setting;
    /** Forwards permutation. */
    private String forwardperm;
    /** Notch settings. */
    private String notches;
    /** Backwards permutation. */
    private String backperm;
}
