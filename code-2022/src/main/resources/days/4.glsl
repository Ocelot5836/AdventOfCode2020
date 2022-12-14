#version 430 core
#include errors.glsl

uniform int in_Part;

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer errorBuffer {
    int[] out_Error;
};
layout(std430, binding = 1) buffer inBuffer {
    float[] in_Value;
};
layout(std430, binding = 2) buffer outBuffer {
    float out_Value;
};

void main() {
}
