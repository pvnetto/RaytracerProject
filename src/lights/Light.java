package lights;

import primitives.IHittable.HitRecord;
import renderer.Vector3;

public abstract class Light {
	protected Vector3 intensity;
	protected Vector3 direction;
	
	public Light(Vector3 intensity) {
		super();
		this.intensity = intensity;
	}
	
	public Vector3 getIntensity(HitRecord hitRecord) {
		return intensity;
	}
	
	public Vector3 getDirection(HitRecord hitRecord) {
		return direction;
	}
	
	public double getDiffuse(HitRecord hitRecord) {
		double dirDotNormal = Vector3.Dot(getDirection(hitRecord).normalized(), hitRecord.normal);
		return Math.max(dirDotNormal, 0);
	}


}
