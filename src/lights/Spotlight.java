package lights;

import primitives.IHittable.HitRecord;
import renderer.Vector3;

public class Spotlight extends LocalLight {
	
	private double cutOffAngle;
	private Vector3 lookAt;
	
	public Spotlight(Vector3 intensity, Vector3 lookAt, Vector3 position, double cutOffAngle) {
		super(intensity, position);
		this.lookAt = lookAt.sub(position).normalized();
		this.cutOffAngle = cutOffAngle;
	}
	
	private boolean isInsideCone(final HitRecord hitRecord) {
        Vector3 v = hitRecord.intersectionPoint.sub(position).normalized();
        double VdotD = Vector3.Dot(v, lookAt);
        double cosAngle = Math.cos(Math.toRadians(cutOffAngle));

        return VdotD > cosAngle;
	}
	
	@Override
	public double getDiffuse(HitRecord hitRecord) {
		if(isInsideCone(hitRecord)) {
			return super.getDiffuse(hitRecord);
		}
		return 0;
	}

}
