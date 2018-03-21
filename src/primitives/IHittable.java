package primitives;


import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import renderer.Ray;
import renderer.Vector3;

public interface IHittable {
	public class HitRecord {
		public double t;
	    public Vector3 intersectionPoint;
	    public Vector3 normal;
	    public IHittable hitObject;
	    //public double u, v;
	    
		public HitRecord() {
			super();
		}
	    
		public HitRecord(double t, Vector3 intersectionPoint, Vector3 normal, IHittable hitObject) {
			SetHitRecord(t, intersectionPoint, normal, hitObject);
		}
		
		public HitRecord(HitRecord hitRecord) {
			SetHitRecord(hitRecord.t, hitRecord.intersectionPoint, hitRecord.normal, hitRecord.hitObject);
		}
		
		public void SetHitRecord(double t, Vector3 intersectionPoint, Vector3 normal, IHittable hitObject) {
			this.t = t;
			this.intersectionPoint = intersectionPoint;
			this.normal = normal;
			this.hitObject = hitObject;
		}
		
	}
	
	public HitRecord Hit(Ray ray, double minT, double maxT);
	
	public static HitRecord getFirstHit(Vector<IHittable> objects, Ray ray, double minT, double maxT) {
        boolean hasHit = false;
        
        double closestSoFar = maxT;
        HitRecord closestHit = new HitRecord();
        
        for(int i = 0; i < objects.size(); i++){
        	HitRecord currentHit = objects.get(i).Hit(ray, minT, maxT);
            if(currentHit != null){
                if(currentHit.t < closestSoFar){
                    closestHit = new HitRecord(currentHit);
                    closestSoFar = currentHit.t;
                    hasHit = true;
                }
            }
        }
        
        return hasHit ? closestHit : null;
	}
	
}
