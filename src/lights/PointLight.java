package lights;

import java.io.Serializable;

import primitives.IHittable.HitRecord;
import renderer.Vector3;

public class PointLight extends LocalLight {

	public PointLight(Vector3 intensity, Vector3 position) {
		super(intensity, position);
	}
	
	@Override
    public Vector3 getIntensity(HitRecord hitRecord) {
        double distance = getDirection(hitRecord).magnitude();

        /*attenuation is the reduction of light intensity over distance*/
        double attenuation = distance;
        Vector3 newIntensity = intensity.divide(attenuation);

        return newIntensity;
    }
	
}
