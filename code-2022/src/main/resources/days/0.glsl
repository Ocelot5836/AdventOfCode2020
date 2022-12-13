#version 430 core
#include errors.glsl

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer inBuffer {
    int in_Part;
    float[] inValue;
};
layout(std430, binding = 1) buffer outBuffer {
    int out_Error;
    float outValue;
};

void main() {
    for(int i = 0; i < inValue.length(); i++) {
        outValue += inValue[i];
    }
}
