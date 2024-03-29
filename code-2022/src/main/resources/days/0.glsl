#version 430 core

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

uniform int in_Part;

layout(std430, binding = 0) buffer errorBuffer {
    int[] out_Error;
};
layout(std430, binding = 1) buffer inBuffer {
    float[] inV_alue;
};
layout(std430, binding = 2) buffer outBuffer {
    float out_Value;
};

void main() {
    for(int i = 0; i < inValue.length(); i++) {
        outValue += inValue[i];
    }
}
