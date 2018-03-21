package lights;

import primitives.IHittable.HitRecord;
import renderer.Vector3;

public class DirectionalLight extends Light {

	public DirectionalLight(Vector3 intensity, Vector3 direction) {
		super(intensity);
		this.direction = direction;
	}

}
