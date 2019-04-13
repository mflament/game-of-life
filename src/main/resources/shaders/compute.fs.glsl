#version 330 core

uniform sampler2D universe;

//layout(origin_upper_left) in vec4 gl_FragCoord;

in vec2 pTexCoordinate;

out vec4 outColor;

${rule}

float neighbor(vec2 c, ivec2 offset) {
	return textureOffset(universe, c, offset).r;
}

int neighbors(vec2 c) {
	float n = neighbor(c, ivec2(-1,-1));
	n += neighbor(c, ivec2(0,-1));
	n += neighbor(c, ivec2(1,-1));
	n += neighbor(c, ivec2(-1,0));
	n += neighbor(c, ivec2(1,0));
	n += neighbor(c, ivec2(-1,1));
	n += neighbor(c, ivec2(0,1));
	n += neighbor(c, ivec2(1,1));
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
