package renderer;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class Vector3 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double x, y, z;
	public static final Vector3 zero = new Vector3();
	public static final Vector3 one = new Vector3(1, 1, 1);
	public static final Vector3 up = new Vector3(0, 1, 0);
	
	
	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double x() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double y() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double z() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public static double Dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static Vector3 Cross(Vector3 a, Vector3 b) {
        return new Vector3(a.y * b.z - a.z * b.y ,
                -( a.x * b.z - a.z * b.x ),
                a.x * b.y - a.y * b.x    );
	}
	
	public static Vector3 Interpolate(Vector3 start, Vector3 end, double t) {
	    //linearly interpolates the color, value = (1 - t) * startValue + t * endValue
		Vector3 result = start.scale(1 - t).add(end.scale(t));
		return result;
	}
	
    private static double Saturate(double value){
        return Math.min(Math.max(value, 0.0f), 255.0f);
    }
    
	public static Vector3 Saturate(Vector3 color) {
		return new Vector3(Saturate(color.x()), Saturate(color.y()), Saturate(color.z()));
	}
	
	/*Compares two vectors*/
	public boolean equals(Vector3 b) {
		double epsilon = 0.0001f;
		return ( Math.abs(x - b.x()) < epsilon &&
				Math.abs(y - b.y()) < epsilon && 
				Math.abs(z - b.z()) < epsilon);
	}
	
	public Vector3 add(Vector3 b) {
		Vector3 newVector = new Vector3(x + b.x, y + b.y, z + b.z);
		return newVector;
	}
	
	public Vector3 sub(Vector3 b) {
		Vector3 newVector = new Vector3(x - b.x, y - b.y, z - b.z);
		return newVector;
	}
	
	public Vector3 scale(double scalar) {
		Vector3 newVector = new Vector3(x * scalar, y * scalar, z * scalar);
		
		return newVector;
	}
	
	public Vector3 mult(Vector3 b) {
		Vector3 newVector = new Vector3(x * b.x, y * b.y, z * b.z);
		return newVector;
	}
	
	public Vector3 divide(double scalar) {
		Vector3 newVector = new Vector3(x / scalar, y / scalar, z / scalar);
		
		return newVector;
	}
	
	public Vector3 normalized() {
		return this.divide(magnitude());
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public double sqrMagnitude() {
		return x*x + y*y + z*z;
	}
	
	@Override
	public String toString() {
		return (int)x + " " + (int)y + " " + (int)z;
	}
	
}
