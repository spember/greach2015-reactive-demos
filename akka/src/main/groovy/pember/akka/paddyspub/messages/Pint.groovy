package pember.akka.paddyspub.messages

import groovy.transform.Immutable

/**
 *
 * @author Steve Pember
 */
class Pint {

    private final int number

    Pint(int n) {
        number = n
    }

    int getNumber() {
        number
    }

    String toString() {
        "Pint #$number"
    }
}

class FullPint extends Pint {
    FullPint(int n) {
        super(n)
    }
}

class EmptyPint extends Pint {
    EmptyPint(int n) {
        super(n)
    }
}
