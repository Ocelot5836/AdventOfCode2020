#version 430 core

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;

layout(std430, binding = 0) buffer inBuffer {
    float[] inValue;
};
layout(std430, binding = 1) buffer outBuffer {
    float outValue;
};

void main() {
    for(int i = 0; i < inValue.length(); i++) {
        outValue += inValue[i];
    }
}
