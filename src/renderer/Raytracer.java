package renderer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import lights.Light;
import primitives.IHittable;
import primitives.IHittable.HitRecord;

public class Raytracer {
	
	private ImageConfigs imageConfigs;
	private Vector<IHittable> objects;
	private Vector<Light> lights;
	private Camera camera;
	
	public Raytracer(ImageConfigs imageConfigs, Vector<IHittable> objects, Vector<Light> lights, Camera camera) {
		this.imageConfigs = imageConfigs;
		this.objects = objects;
		this.lights = lights;
		this.camera = camera;
	}
	
	public String Trace() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(imageConfigs.getConfig());
		
		for(int i = imageConfigs.getROWS() - 1; i >= 0; i--) {
			for(int j = 0; j < imageConfigs.getCOLS(); j++) {
				Vector3 pixelColor = new Vector3();
				for(int k = 0; k < imageConfigs.getAntialiasingSamples(); k++) {
	                double randomU, randomV;
	                
	                randomU = Math.random() * (0.9999d);
	                randomV = Math.random() * (0.9999d);

	                //horizontal %
	                double u = (j + randomU) / imageConfigs.getCOLS();
	                //vertical %
	                double v = (i + randomV) / imageConfigs.getROWS();

	                Ray ray = camera.RayCast(u, v);

	                Vector3 currentColor = getColor(ray, objects, lights);

	                pixelColor = pixelColor.add(currentColor);
				}
				
				pixelColor = pixelColor.divide(imageConfigs.getAntialiasingSamples());
				//TODO Gamma Correction
				pixelColor = pixelColor.scale(255.99d);
				pixelColor = Vector3.Saturate(pixelColor);
				
				sb.append(pixelColor + "\n");
			}
		}
		
		return sb.toString();
	}
	
	private Vector3 getColor(Ray ray, Vector<IHittable> objects, Vector<Light> lights) {
	    Vector3 bottomColor = Vector3.one;
	    Vector3 topColor = new Vector3(0.5, 0.7, 1);
	    Vector3 colorResult = new Vector3();

	    HitRecord hit = IHittable.getFirstHit(objects, ray, 0.0001f, Double.MAX_VALUE);

	    if(hit != null){
	    	return hit.hitObject.getColor(ray, hit, lights, objects);
	    }

	    Vector3 unitRayDir = ray.getDirection().normalized();

	    //The Y axis is used because the gradient is top-bottom
	    double unitRayY = unitRayDir.y();

	    //Scaling unitRayY from [-1.0, 1.0] to [0, 1.0]
	    double t = 0.5 * (unitRayY + 1);

	    colorResult = Vector3.Interpolate(bottomColor, topColor, t);

	    return colorResult;
	}
	
	
}
