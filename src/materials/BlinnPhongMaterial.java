package materials;

import java.util.Vector;

import lights.DirectionalLight;
import lights.Light;
import primitives.IHittable;
import primitives.IHittable.HitRecord;
import renderer.Ray;
import renderer.Vector3;

public class BlinnPhongMaterial implements Material{
	private Vector3 ambientColor;
	private Vector3 diffuseColor;
	private Vector3 specular;
	private double shininess;
	
	public BlinnPhongMaterial(Vector3 ambientColor, Vector3 diffuseColor, Vector3 specular, double shininess) {
		super();
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specular = specular;
		this.shininess = shininess;
	}

	@Override
	public Vector3 getColor(IHittable hitObj, Ray ray, HitRecord hitRecord, Vector<Light> lights, Vector<IHittable> objects) {
        /*Adding the ambient component to the final color*/
		Vector3 finalColor = ambientColor;

        double diffuseContribution;
        double specularContribution;
        Vector3 intensity = new Vector3();

        Vector3 viewDir = ray.getOrigin().sub(hitRecord.intersectionPoint).normalized();
        
        for (int i = 0; i < lights.size(); i++) {
        	Light light = lights.get(i);
        	
			Vector3 lightDir = light.getDirection(hitRecord).normalized();
			
            Ray invertedRay = new Ray(hitRecord.intersectionPoint, lightDir);
            HitRecord shadowHit = IHittable.getFirstHit(objects, invertedRay, 0.0001f, Double.MAX_VALUE);
            if(shadowHit != null){
            	if(shadowHit.hitObject != hitObj) {
            		continue;
            	}
            }
			
            diffuseContribution = light.getDiffuse(hitRecord);
            intensity = light.getIntensity(hitRecord);
            /*Adding diffuse component to the final color.*/
            finalColor = finalColor.add( diffuseColor.scale(diffuseContribution).mult(intensity) );

            if(diffuseContribution > 0.0f){
                Vector3 halfDir = lightDir.add(viewDir).normalized();
                double specAngle = Math.max(Vector3.Dot(halfDir, hitRecord.normal), 0);
                specularContribution = Math.pow(specAngle, shininess);
                /*Adding specular component to the final color.*/
                finalColor = finalColor.add( specular.scale(specularContribution).mult(intensity) );
            }
		}

        /*Ambient + diffuse + specular*/
        return finalColor;
	}
	
}
