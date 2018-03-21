package renderer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import lights.DirectionalLight;
import lights.Light;
import lights.PointLight;
import lights.Spotlight;
import materials.BlinnPhongMaterial;
import materials.Color;
import primitives.IHittable;
import primitives.Sphere;
import primitives.IHittable.HitRecord;
import renderer.ImageConfigs.Codification;

public class Render {

	public static Vector3 getColor(Ray ray, Vector<IHittable> objects, Vector<Light> lights) {
	    Vector3 bottomColor = Vector3.one;
	    Vector3 topColor = new Vector3(0.5, 0.7, 1);
	    Vector3 colorResult = new Vector3();

	    HitRecord hit = IHittable.getFirstHit(objects, ray, 0.0001f, Double.MAX_VALUE);

	    if(hit != null){
	    	Sphere hitSphere = (Sphere)hit.hitObject;
	    	return hitSphere.getMaterial().getColor(hitSphere, ray, hit, lights, objects);
	    }

	    Vector3 unitRay = ray.getDirection().normalized();

	    //The Y axis is used because the gradient is top-bottom
	    double unitRayY = unitRay.y();

	    //Scaling unitRayY from [-1.0, 1.0] to [0, 1.0]
	    double t = 0.5 * (unitRayY + 1);

	    colorResult = Vector3.Interpolate(bottomColor, topColor, t);

	    return colorResult;
	}
	
	public static void Trace(ImageConfigs ic, final Vector<IHittable> objects, final Vector<Light> lights) {
		Vector3 cameraLookFrom = new Vector3(9, 3.5d, 15);
		Vector3 cameraLookAt = new Vector3(0, 0, -1);
		double cameraFOV = 30;
		double aspectRatio = 2 / 1;
		
		Camera camera = new Camera(cameraLookFrom, cameraLookAt, Vector3.up, cameraFOV, aspectRatio);
		
		System.out.println("Starting tracing");
		
		try {
			PrintWriter printWriter = new PrintWriter("image.ppm", "UTF-8");
			printWriter.println(ic.getConfig());
			for(int i = ic.getROWS() - 1; i >= 0; i--) {
				for(int j = 0; j < ic.getCOLS(); j++) {
					Vector3 pixelColor = new Vector3();
					for(int k = 0; k < ic.getAntialiasingSamples(); k++) {
		                double randomU, randomV;
		                
		                randomU = Math.random() * (0.9999d);
		                randomV = Math.random() * (0.9999d);

		                //horizontal %
		                double u = (j + randomU) / ic.getCOLS();
		                //vertical %
		                double v = (i + randomV) / ic.getROWS();

		                Ray ray = camera.RayCast(u, v);

		                Vector3 currentColor = getColor(ray, objects, lights);

		                pixelColor = pixelColor.add(currentColor);
					}
					
					pixelColor = pixelColor.divide(ic.getAntialiasingSamples());
					
					//TODO Gamma Correction
					
					pixelColor = pixelColor.scale(255.99d);
					
					
					pixelColor = Vector3.Saturate(pixelColor);
					printWriter.println(pixelColor + "\n");
				}
			}
			printWriter.close();
		}
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ImageConfigs imageConfigs = new ImageConfigs("image", 1920, 960, 4, Codification.ASCII);
		Vector3 ambientColor = Color.blue.divide(8);
		
		Vector3 groundSpherePos = new Vector3(0, -1000.5d, -1);
		
		BlinnPhongMaterial blinnPhongMaterial1 = new BlinnPhongMaterial(ambientColor, new Vector3(0.7d, 0.2d, 0.1d), Color.white, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial2 = new BlinnPhongMaterial(ambientColor, new Vector3(0.8d, 0, 0.8d), Color.yellow, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial3 = new BlinnPhongMaterial(ambientColor, new Vector3(0.5d, 0.5d, 0), Color.white, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial4 = new BlinnPhongMaterial(ambientColor, new Vector3(0.2d, 0.2d, 0.0d), Color.white, 256.0d);
		
		
		DirectionalLight directionalLight = new DirectionalLight(Color.white.divide(2), new Vector3(3, 2, 0));
		PointLight pointLight = new PointLight(Color.red.divide(1.2d), new Vector3(0, 1, 0));
		Spotlight spotlight = new Spotlight(new Vector3(0.3d, 0.9d, 0.3d), new Vector3(0, 0, -2), new Vector3(0, 1, 2), 15);
		Spotlight spotlight2 = new Spotlight(new Vector3(0.7d, 0.2d, 0.2d), new Vector3(-4, 0, -2), new Vector3(0, 4, -2), 18);
		Spotlight spotlight3 = new Spotlight(new Vector3(0.2d, 0.2d, 0.7d), new Vector3(4, 0, -2), new Vector3(0, 4, -2), 18);
		
		
		Vector<Light> lights = new Vector<Light>();
		lights.add(directionalLight);
		//lights.add(pointLight);
		lights.add(spotlight);
		lights.add(spotlight2);
		lights.add(spotlight3);

		
		Sphere sphere = new Sphere(new Vector3(0, 0, -2), 0.5d, blinnPhongMaterial2);
		Sphere sphere2 = new Sphere(new Vector3(-4, 0, -2), 0.5d, blinnPhongMaterial1);
		Sphere sphere3 = new Sphere(new Vector3(4, 0, -2), 0.5d, blinnPhongMaterial3);
		Sphere groundSphere = new Sphere(groundSpherePos, 1000, blinnPhongMaterial4);
		
		Vector<IHittable> objects = new Vector<IHittable>();
		objects.add(sphere);
		objects.add(sphere2);
		objects.add(sphere3);
		objects.add(groundSphere);
		
		Trace(imageConfigs, objects, lights);
		
		System.out.println("Finished!");
	}
}
