package materials;

import renderer.Vector3;

public class Color {
	public static final Vector3 red = new Vector3(1, 0, 0);
	public static final Vector3 green = new Vector3(0, 1, 0);
	public static final Vector3 blue = new Vector3(0, 0, 1);
	public static final Vector3 white = Vector3.one;
	public static final Vector3 black = Vector3.zero;
	public static final Vector3 yellow = Color.red.add(Color.green);
}
