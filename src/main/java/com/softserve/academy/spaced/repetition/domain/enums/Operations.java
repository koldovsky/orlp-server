package com.softserve.academy.spaced.repetition.domain.enums;

/**
 * Enum of available operations in the application with theirs masks.
 */
public enum Operations {
    CREATE(0b0001),
    READ(0b0010),
    UPDATE(0b0100),
    DELETE(0b1000);

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
    public static int createMask(Operations... operations) {
        int result = 0;
        for (Operations operation : operations) {
            result |= operation.getMask();
        }
        return result;
    }
}
