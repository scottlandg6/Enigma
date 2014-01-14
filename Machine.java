package enigma;
import static enigma.Rotor.toLetter;
import static enigma.Rotor.toIndex;

/** Class that represents a complete enigma machine.
 *  @author Scott Lee
 */
class Machine {
    /** Initialize Rotors. */
    private final Rotor[] _rotor = new Rotor[5];
    /** Set my rotors to (from left to right) ROTORS.  Initially, the rotor
     *  settings are all 'A'. */
    void replaceRotors(Rotor[] rotors) {
        for (int x = 0; x < rotors.length; x += 1) {
            _rotor[x] = rotors[x];
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting.  */
    void setRotors(String setting) {
        assert setting.matches("[A-Z]{4}");
        _rotor[1].set(Rotor.toIndex(setting.charAt(0)));
        _rotor[2].set(Rotor.toIndex(setting.charAt(1)));
        _rotor[3].set(Rotor.toIndex(setting.charAt(2)));
        _rotor[4].set(Rotor.toIndex(setting.charAt(3)));
    }
    /** Boolean advance and advance1. */
    private boolean advance, advance1;
    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.length(); i += 1) {
            advance = _rotor[4].atNotch();
            advance1 = _rotor[3].atNotch();
            _rotor[4].set(_rotor[4].getSetting() + 1);
            if (advance) {
                _rotor[3].set(_rotor[3].getSetting() + 1);
            }
            if (advance1) {
                _rotor[2].set(_rotor[2].getSetting() + 1);
                if (!advance) {
                    _rotor[3].set(_rotor[3].getSetting() + 1);
                }
            }
            int a = toIndex(msg.charAt(i));
            for (int x = 4; x >= 0; x -= 1) {
                a = _rotor[x].convertForward(a);
            }
            for (int x = 1; x < 5; x += 1) {
                a = _rotor[x].convertBackward(a);
            }
            result.append(toLetter(a));
        }
        return result.toString();
    }
}
