package eu.genome.fpx;

/**
 * List of signature modes.
 */
public enum SignatureMode {
    /**
     * Signature for callbacks.
     */
    CALLBACK,

    /**
     * Amount only signature.
     */
    MODE_A,

    /**
     * Amount with timestamp nonce.
     */
    MODE_A_TS;
}
