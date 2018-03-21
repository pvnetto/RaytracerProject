package primitives;

import java.io.Serializable;
import java.util.Vector;

import lights.Light;
import materials.Material;
import renderer.Ray;
import renderer.Vector3;

public class Sphere implements IHittable, Serializable {
	
	private Vector3 center;
	private double radius;
	private Material material;
	
	public Sphere(Vector3 center, double radius, Material material) {
		super();
		this.center = center;
		this.radius = radius;
		this.material = material;
	}
	
	@Override
	public Vector3 getColor(Ray ray, HitRecord hitRecord, Vector<Light> lights, Vector<IHittable> objects) {
		return material.getColor(this, ray, hitRecord, lights, objects);
	}

	@Override
	public HitRecord Hit(Ray ray, double minT, double maxT) {
        Vector3 oc = ray.getOrigin().sub(center);

        double a = Vector3.Dot(ray.getDirection(), ray.getDirection());
        double b = 2.0f * Vector3.Dot(oc, ray.getDirection());
        double c = Vector3.Dot(oc, oc) - radius * radius;
        double delta = b * b - 4 * a * c;

        if(delta > 0){
            double solution1 = (-b - Math.sqrt(delta)) / (2.0 * a);
            HitRecord solutionHit = Solve(ray, solution1, maxT, minT);
            if(solutionHit != null) {
            	return solutionHit;
            }
            
            double solution2 = (-b + Math.sqrt(delta)) / (2.0 * a);
            solutionHit = Solve(ray, solution2, maxT, minT);
            if(solutionHit != null) {
            	return solutionHit;
            }
        }

        return null;
	}
	
	private HitRecord Solve(Ray ray, double solution, double maxT, double minT) {
        if(solution < maxT && solution > minT){
            double t = solution;
            Vector3 intersectionPoint = ray.PointAt(solution);
            Vector3 normal = intersectionPoint.sub(center).divide(radius);
            
            HitRecord hitRecord = new HitRecord(t, intersectionPoint, normal, this);

            //GetSphereUV((hitRecord.intersectionPoint - center) / radius, hitRecord.u, hitRecord.v);
            return hitRecord;
        }
        else {
        	return null;
        }
	}

}
