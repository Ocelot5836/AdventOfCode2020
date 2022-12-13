#version 430 core
#include errors.glsl

#define ROCK 0
#define PAPER 1
#define SCISSORS 2

#define LOSS 0
#define TIE 1
#define WIN 2

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer inBuffer {
    int in_Part;
    int[] in_Value;
};
layout(std430, binding = 1) buffer outBuffer {
    int out_Error;
    int out_Score;
};

int getResponse(int opponent, int inputValue) {
    if (in_Part == 0) {
        return inputValue;
    }

    int response = 0;
    switch (inputValue) {
        case LOSS:
        {
            response = opponent - 1;
            break;
        }
        case TIE:
        {
            response = opponent;
            break;
        }
        case WIN:
        {
            response = opponent + 1;
            break;
        }
    }

    if (response < 0) {
        response = 2;
    }
    if (response > 2) {
        response = 0;
    }

    return response;
}

void main() {
    int totalScore = 0;
    for (int i = 0; i < in_Value.length(); i+= 2) {
        int opponent = in_Value[i];
        int response = getResponse(opponent, in_Value[i + 1]);

        // 0 > 2
        // 1 > 0
        // 2 > 1

        bool win = opponent == SCISSORS && response == ROCK;
        bool loss = response == SCISSORS && opponent == ROCK;

        if (response == opponent) {
            // tie
            totalScore += 3;
        } else if (!loss && (win || response > opponent)) {
            // win
            totalScore += 6;
        }
        totalScore += response + 1;
    }

    out_Score = totalScore;
}
