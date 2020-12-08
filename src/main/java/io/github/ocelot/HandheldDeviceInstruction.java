package io.github.ocelot;

/**
 * <p>Static enum values for handheld device instructions.</p>
 *
 * @author Ocelot
 */
public enum HandheldDeviceInstruction
{
    ACCUMULATOR("acc"),
    JUMP("jmp"),
    NO_OPERATION("nop");

    private final String instruction;

    HandheldDeviceInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    /**
     * @return The input string for this instruction
     */
    public String getInstruction()
    {
        return instruction;
    }

    /**
     * Retrieves a device instruction by the input string.
     *
     * @param instruction The instruction to get
     * @return An instruction by that name
     * @throws UnsupportedOperationException If the requested instruction is not implemented
     */
    public static HandheldDeviceInstruction byInstruction(String instruction)
    {
        for (HandheldDeviceInstruction value : values())
            if (value.instruction.equalsIgnoreCase(instruction))
                return value;
        throw new UnsupportedOperationException("Unknown instruction '" + instruction + "'");
    }
}
