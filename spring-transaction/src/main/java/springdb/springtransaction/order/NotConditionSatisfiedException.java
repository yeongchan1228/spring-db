package springdb.springtransaction.order;

public class NotConditionSatisfiedException extends Exception {

    public NotConditionSatisfiedException() {
    }

    public NotConditionSatisfiedException(String message) {
        super(message);
    }

    public NotConditionSatisfiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
