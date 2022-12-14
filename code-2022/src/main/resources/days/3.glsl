#version 430 core
#include errors.glsl

#define NL 10
#define A 65
#define a 97
#define Z 90
#define z 122

uniform int in_Part;

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer errorBuffer {
    int[] out_Error;
};
layout(std430, binding = 1) buffer inBuffer {
    int[] in_Value;
};
layout(std430, binding = 2) buffer outBuffer {
    int out_Result;
};

bool isLowerCase(int character) {
    return character >= a && character <= z;
}

bool isUpperCase(int character) {
    return character >= A && character <= Z;
}

void main() {
    int startIndex = 0;
    int sum = 0;
    int lineCount = 0;

    // Ignore last char because it will always be a null
    for (int endIndex = 0; endIndex < in_Value.length() - 1; endIndex++) {
        if (in_Value[endIndex] == NL) {
            int resultLower = 0;
            int resultUpper = 0;
            bool incrementSum = false;

            if (in_Part == 0) {
                int leftLower = 0;
                int leftUpper = 0;
                int rightLower = 0;
                int rightUpper = 0;

                int size = (endIndex - 1 - startIndex) / 2 + 1;
                for (int j = 0; j < size; j++) {
                    int leftChar = in_Value[startIndex + j];
                    int rightChar = in_Value[startIndex + size + j];

                    if (isLowerCase(leftChar)) {
                        int shift = leftChar - a;
                        leftLower |= 1 << shift;
                    } else {
                        int shift = leftChar - A;
                        leftUpper |= 1 << shift;
                    }

                    if (isLowerCase(rightChar)) {
                        int shift = rightChar - a;
                        rightLower |= 1 << shift;
                    } else {
                        int shift = rightChar - A;
                        rightUpper |= 1 << shift;
                    }
                }

                resultLower = leftLower & rightLower;
                resultUpper = leftUpper & rightUpper;
                incrementSum = true;
            } else {
                lineCount++;
                if (lineCount > 2) {
                    lineCount = 0;

                    int[] lower = int[](0, 0, 0);
                    int[] upper = int[](0, 0, 0);
                    int line = 0;
                    for (int j = startIndex; j < endIndex; j++) {
                        int character = in_Value[j];
                        if (character == NL) {
                            line++;
                        } else {
                            if (isLowerCase(character)) {
                                int shift = character - a;
                                lower[line] |= 1 << shift;
                            } else {
                                int shift = character - A;
                                upper[line] |= 1 << shift;
                            }
                        }
                    }

                    resultLower = lower[0] & lower[1] & lower[2];
                    resultUpper = upper[0] & upper[1] & upper[2];
                    incrementSum = true;
                }
            }

            if (incrementSum) {
                if (resultLower > 0) {
                    sum += int(log2(resultLower)) + 1;
                } else if (resultUpper > 0) {
                    sum += int(log2(resultUpper)) + 27;
                } else {
                    print("Got no result from ");
                    printChar(in_Value[startIndex]);
                    print(" to ");
                    printChar(in_Value[endIndex]);
//                    int[] error = "Got no result from ";
//                    int[] error2 = " to ";
//                    for (int i = 0; i < error.length(); i++) {
//                        out_Error[i] = error[i];
//                    }
//                    out_Error[error.length() - 1] = in_Value[startIndex];
//                    for (int i = 0; i < error2.length(); i++) {
//                        out_Error[error.length() + i] = error2[i];
//                    }
//                    out_Error[error.length() + error2.length() - 1] = in_Value[endIndex];
                    return;
                }

                endIndex++;
                startIndex = endIndex;
            }
        }
    }

    out_Result = sum;
}
