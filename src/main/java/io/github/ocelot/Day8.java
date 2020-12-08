package io.github.ocelot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Ocelot
 */
public class Day8
{
    public static void main(String[] args) throws IOException
    {
        TimeTracker tracker = new TimeTracker();

        // Parse input data
        tracker.startSection("parse");
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Day8.class.getResourceAsStream("/8/input.txt"))))
        {
            while (reader.ready())
                data.append(reader.readLine().trim()).append(",");
        }
        String[] input = data.toString().split(",");

        tracker.endStartSection("execute");
        int acc = execute(tracker, input);
        tracker.endSection();

        // Print result
        System.out.println("Program terminated with accumulator value of " + acc);
        System.out.println(tracker);
    }

    private static String flipInstruction(String input)
    {
        // Parse instruction
        String[] parts = input.split(" ");
        HandheldDeviceInstruction instruction = HandheldDeviceInstruction.byInstruction(parts[0]);

        // Swap no-op or jump
        switch (instruction)
        {
            case JUMP -> {
                return "nop " + parts[1];
            }
            case NO_OPERATION -> {
                return "jmp " + parts[1];
            }
            default -> throw new IllegalStateException(instruction + " cannot be corrupted");
        }
    }

    private static int execute(TimeTracker tracker, String[] instructions)
    {
        // global variables
        byte[] memory = new byte[4096]; // memory[0] is 3 flags: terminate, errored, and first error. Instruction markers are placed at 1024-4096
        byte[] corrupted = new byte[4096]; // copy of memory after first error flag is set
        char pc = 0; // program counter
        int acc = 0; // global accumulator variable

        // Execute until termination flag is set
        while ((memory[0] & 1) == 0)
        {
            boolean errored = (memory[0] & 2) > 0;
            // If pc exceeds instructions, this configuration is successful
            if (pc >= instructions.length)
            {
                System.out.println("Successfully repaired program");
                memory[0] |= 1; // terminate program
                continue;
            }

            // Read instruction
            String s = instructions[pc];
            if (errored && !s.startsWith(HandheldDeviceInstruction.ACCUMULATOR.getInstruction())) // If attempting to repair instruction
            {
                if (((corrupted[1024 + pc / Byte.SIZE] >> (pc % Byte.SIZE)) & 1) == 1) // Check if this line was already executed
                {
                    // If successful, mark line as processed and flip instruction
                    corrupted[1024 + pc / Byte.SIZE] &= ~(1 << (pc % Byte.SIZE));

                    // System.out.println("Flipping " + s + " at " + (int) pc);
                    s = flipInstruction(s);

                    // Remove error flag to continue execution as normal
                    memory[0] &= ~2;
                }
            }
            String[] parts = s.split(" ");
            if (parts.length != 2) // This should never be thrown
                throw new IllegalArgumentException("Invalid instruction '" + instructions[pc] + "'");

            // Process instruction
            HandheldDeviceInstruction instruction = HandheldDeviceInstruction.byInstruction(parts[0]);
            int parameter = Integer.parseInt(parts[1]);

            // If execution is marked twice, it is assumed to be a loop and boot repair operations commence
            if (((memory[1024 + pc / Byte.SIZE] >> (pc % Byte.SIZE)) & 1) == 1)
            {
                if ((memory[0] & 4) == 0) // If the first error flag is not set this must be the first error, begin repair phase
                {
                    // Print accumulator
                    System.out.println("Accumulator is " + acc);
                    System.out.println("Attempting to repair boot");
                    tracker.endStartSection("repair");
                    System.arraycopy(memory, 0, corrupted, 0, memory.length);
                }

                // Reset global variables
                pc = 0;
                acc = 0;
                memory[0] |= 2;

                // Set first error flag if not set
                memory[0] |= 4;
                // Clear memory
                for (int i = 1024; i < memory.length; i++)
                    memory[i] = 0;
                continue;
            }
            // Mark this line as processed
            memory[1024 + pc / Byte.SIZE] |= 1 << pc % Byte.SIZE;

            // Execute instructions. acc adds parameter to accumulator, jmp adds to program counter, and nop is an empty no-op instruction
            switch (instruction)
            {
                case ACCUMULATOR -> {
                    acc += parameter;
                    pc++;
                }
                case JUMP -> pc += parameter;
                case NO_OPERATION -> pc++;
                default -> throw new UnsupportedOperationException("Unknown instruction '" + instruction + "'");
            }
        }
        return acc;
    }
}
