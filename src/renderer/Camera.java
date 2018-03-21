package renderer;

import java.io.Serializable;

public class Camera implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vector3 lowerLeftCorner;
	private Vector3 horizontal;
	private Vector3 vertical;
	private Vector3 origin;
	
	public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 vectorUp, double fieldOfView, double aspectRatio) {
		Vector3 u, v, w;
		double theta = fieldOfView * Math.PI / 180;
		
		double halfHeight = Math.tan(theta / 2);
		
		double halfWidth = aspectRatio * halfHeight;
		
		origin = lookFrom;
		
		w = lookFrom.sub(lookAt).normalized();
		
		u = Vector3.Cross(vectorUp, w).normalized();
		
		v =  Vector3.Cross(w, u);
		
		lowerLeftCorner = new Vector3(-halfWidth, -halfHeight, -1);
		
		Vector3 uHalfWidth = u.scale(halfWidth);
		
		Vector3 vHalfHeight = v.scale(halfHeight);
		
		lowerLeftCorner = origin.sub(uHalfWidth).sub(vHalfHeight).sub(w);
		
		horizontal = uHalfWidth.scale(2);
		vertical = vHalfHeight.scale(2);
	}
	
	public Ray RayCast(double u, double v) {
		Vector3 uHorizontal = horizontal.scale(u);
		Vector3 vVertical = vertical.scale(v);
		
		Vector3 rayEnd = lowerLeftCorner.add(uHorizontal).add(vVertical);
		Vector3 rayDir = rayEnd.sub(origin);
		
		Ray newRay = new Ray(origin, rayDir);
		return newRay;
	}
}
