#version 330 core

in vec2 position;
in vec2 vTexCoordinate;

out vec2 pTexCoordinate;

void main()
{
    gl_Position = vec4(position, 0.0, 1.0);
    pTexCoordinate = vTexCoordinate;
}
