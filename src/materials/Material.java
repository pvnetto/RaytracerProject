package materials;

import java.util.Vector;

import lights.Light;
import primitives.IHittable;
import primitives.IHittable.HitRecord;
import renderer.Ray;
import renderer.Vector3;

public interface Material {
	public Vector3 getColor(IHittable hitObj, Ray ray, HitRecord hitRecord, Vector<Light> lights, Vector<IHittable> objects);
}
