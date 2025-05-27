package dev.keii.goldenage.betaprotect.models;

public enum TransactionAction {
    Add(0),
    Remove(1);

    private final int value;

    TransactionAction(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static TransactionAction fromInt(int value) {
        for (TransactionAction action : TransactionAction.values()) {
            if (action.value == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid int value for TransactionAction: " + value);
    }
}
