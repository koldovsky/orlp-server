package com.softserve.academy.spaced.repetition.domain;

/**
 * Enum of available operations in the application with theirs masks.
 */
public enum Operations {
    CREATE(0b00001),
    READ(0b00010),
    UPDATE(0b00100),
    DELETE(0b01000),
    ADMINISTRATE(0b10000);

    /**
     * Integer value of operation. You can write 32(4*8) operations.
     */
    private int mask;

    /**
     * Constructor with mask of operation. For better understanding declared in binary code, other way is to declare it
     * like power of number 2 (each time increasing value of the power).
     *
     * @param mask code of operation
     */
    Operations(int mask) {
        this.mask = mask;
    }

    /**
     * Returns mask of operation
     *
     * @return mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * Creates allowing mask with given operations.
     *
     * @param operations allowed operations
     * @return mask with allowed operations
     */
    public static int setMask(Operations... operations) {
        int result = 0;
        for (Operations operation : operations) {
            result |= operation.getMask();
        }
        return result;
    }
}
