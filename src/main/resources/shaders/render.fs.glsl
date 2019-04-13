#version 330 core

uniform sampler2D universe;

in vec2 pTexCoordinate;

out vec4 outColor;

void main()
{
    //outColor = vec4(universeCoordinate, 0.0,  1.0);
    outColor = texture(universe, pTexCoordinate);

    //outColor = vec4(1.0);
}
