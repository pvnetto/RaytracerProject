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
	
	public static void main(String[] args) {
		ImageConfigs imageConfigs = new ImageConfigs("newImage", 1920, 960, 2, Codification.ASCII);
		
		Vector3 cameraLookFrom = new Vector3(9, 3.5d, 15);
		Vector3 cameraLookAt = new Vector3(0, 0, -1);
		double cameraFOV = 60;
		double aspectRatio = 2 / 1;
		Camera camera = new Camera(cameraLookFrom, cameraLookAt, Vector3.up, cameraFOV, aspectRatio);
		
		Vector3 ambientColor = Color.blue.divide(8);
		
		Vector3 groundSpherePos = new Vector3(0, -1000.5d, -1);
		
		BlinnPhongMaterial blinnPhongMaterial1 = new BlinnPhongMaterial(ambientColor, new Vector3(0.7d, 0.2d, 0.1d), Color.white, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial2 = new BlinnPhongMaterial(ambientColor, new Vector3(0.8d, 0, 0.8d), Color.yellow, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial3 = new BlinnPhongMaterial(ambientColor, new Vector3(0.5d, 0.5d, 0), Color.white, 256.0d);
		BlinnPhongMaterial blinnPhongMaterial4 = new BlinnPhongMaterial(ambientColor, Color.yellow.divide(1.5d), Color.white, 256.0d);
		
		
		DirectionalLight directionalLight = new DirectionalLight(Color.white.divide(2), new Vector3(3, 2, 0));
		PointLight pointLight = new PointLight(Color.red.divide(1.2d), new Vector3(0, 1, 0));
		Spotlight spotlight = new Spotlight(new Vector3(0.3d, 0.9d, 0.3d), new Vector3(0, 0, -2), new Vector3(0, 1, 2), 15);
		Spotlight spotlight2 = new Spotlight(new Vector3(0.7d, 0.2d, 0.2d), new Vector3(-4, 0, -2), new Vector3(0, 4, -2), 18);
		Spotlight spotlight3 = new Spotlight(new Vector3(0.2d, 0.2d, 0.7d), new Vector3(4, 0, -2), new Vector3(0, 4, -2), 18);
		
		
		Vector<Light> lights = new Vector<Light>();
		//lights.add(directionalLight);
		lights.add(pointLight);
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
		
		System.out.println("> Tracing...");
		Raytracer raytracer = new Raytracer(imageConfigs, objects, lights, camera);
		String imageString = raytracer.Trace();
		
		System.out.println("> Writing...");
		PPMWriter ppmWriter = new PPMWriter(imageConfigs.getFileName());
		ppmWriter.Write(imageString);
		
		System.out.println("> Finished!");
	}
}
