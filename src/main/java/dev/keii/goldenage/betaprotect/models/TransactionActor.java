package dev.keii.goldenage.betaprotect.models;

public enum TransactionActor {
    Player(0),
    Fire(1),
    PrimedTNT(2),
    Creeper(3),
    Fireball(4),
    Unknown(5);
    private final int value;

    TransactionActor(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static TransactionActor fromInt(int value) {
        for (TransactionActor action : TransactionActor.values()) {
            if (action.value == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid int value for TransactionActor: " + value);
    }
}
