package eu.genome.domain;

public enum TransactionStatus {
    INITIALIZED,
    SUCCESS,
    DECLINE,
    FRAUD,
    ERROR,
    ABORTED,
    TIMED_OUT,
    PROCESSING,
    WAIT_FIRST_SIGN,
    CANCELED;
}
