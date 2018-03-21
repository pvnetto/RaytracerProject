package commons;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import lights.Light;
import primitives.IHittable;
import renderer.Camera;
import renderer.ImageConfigs;

public interface IRender extends Remote{
	public String getRender(ImageConfigs imageConfigs, Vector<IHittable> objects, Vector<Light> lights, Camera camera) throws RemoteException;
}
