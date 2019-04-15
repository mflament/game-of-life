#version 330 core

#define TOP_LEFT ivec2(-1, -1)
#define TOP ivec2(0, -1)
#define TOP_RIGHT ivec2(1, -1)
#define LEFT ivec2(-1, 0)
#define RIGHT ivec2(1, 0)
#define BOTTOM_LEFT ivec2(-1, 1)
#define BOTTOM ivec2(0, 1)
#define BOTTOM_RIGHT ivec2(1, 1)

uniform sampler2D universe;

//layout(origin_upper_left) in vec4 gl_FragCoord;

in vec2 pTexCoordinate;

out vec4 outColor;

${rule}

int neighbors(vec2 c) {
	float n = textureOffset(universe, c, TOP_LEFT).r;
	n += textureOffset(universe, c, TOP).r;
	n += textureOffset(universe, c, TOP_RIGHT).r;
	n += textureOffset(universe, c, LEFT).r;
	n += textureOffset(universe, c, RIGHT).r;
	n += textureOffset(universe, c, BOTTOM_LEFT).r;
	n += textureOffset(universe, c, BOTTOM).r;
	n += textureOffset(universe, c, BOTTOM_RIGHT).r;
	return int(n);
}

void main()
{
	vec2 c = vec2(pTexCoordinate.x, 1.0 - pTexCoordinate.y);
	int state = int(texture(universe, c).r);
	int n = neighbors(c);

	float nextState = rule[state * 9 + n];
	outColor = vec4(vec3(nextState), 1.0);
}
