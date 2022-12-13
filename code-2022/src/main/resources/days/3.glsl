#version 430 core
#include errors.glsl

#define NL 10
#define A 65
#define a 97
#define Z 90
#define z 122

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer inBuffer {
    int in_Part;
    int[] in_Value;
};
layout(std430, binding = 1) buffer outBuffer {
    int out_Error;
    int out_Score;
};

bool isLowerCase(int character) {
    return character >= a && character <= z;
}

bool isUpperCase(int character) {
    return character >= A && character <= Z;
}

void main() {
    int startIndex = 0;
    int pointer = 0;
    int sum = 0;

    // Start at 1 because the length is the first char
    for (int endIndex = 1; endIndex < in_Value.length(); endIndex++) {
        if (in_Value[endIndex] != NL) {
            endIndex++;
            continue;
        }

        int leftLower = 0;
        int leftUpper = 0;
        int rightLower = 0;
        int rightUpper = 0;
        int size = (endIndex - 1 - startIndex) / 2;

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

        int resultLower = leftLower & rightLower;
        int resultUpper = leftUpper & rightUpper;



        for (int i = 0; i < 26; i++) {
            if ((resultLower >> i & 1) > 0) {
                sum += i + 1;
            } else if ((resultUpper >> i & 1) > 0) {
                sum += i + 27;
            }
        }

//        out_Score = (endIndex - 1 - startIndex) / 2;

        // selection is startIndex -> endIndex - 1

        endIndex++;
        startIndex = endIndex;
        pointer++;
    }

    out_Score = sum;
}
