package renderer;

import java.io.Serializable;

public class Ray implements Serializable {
	private Vector3 origin;
	private Vector3 direction;
	
	public Ray(Vector3 origin, Vector3 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Vector3 getOrigin() {
		return origin;
	}

	public Vector3 getDirection() {
		return direction;
	}
	
	/*Returns the ray's location at point t in the direction axis*/
	public Vector3 PointAt(double t) {
		Vector3 point = origin.add(direction.scale(t));
		return point;
	}
	
}
