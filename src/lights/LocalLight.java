package lights;

import primitives.IHittable.HitRecord;
import renderer.Vector3;

public abstract class LocalLight extends Light {
	
	protected Vector3 position;
	
	public LocalLight(Vector3 intensity, Vector3 position) {
		super(intensity);
		this.position = position;
	}
	
	@Override
	public Vector3 getDirection(HitRecord hitRecord) {
		return position.sub(hitRecord.intersectionPoint);
	}

}
