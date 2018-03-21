package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import commons.IRender;
import lights.Light;
import primitives.IHittable;
import renderer.Camera;
import renderer.ImageConfigs;
import renderer.Raytracer;

public class RenderServer extends UnicastRemoteObject implements IRender {
	
	protected RenderServer() throws RemoteException {
		super();
		System.out.println("> Starting the render server...");
	}

	@Override
	public String getRender(ImageConfigs imageConfigs, Vector<IHittable> objects, Vector<Light> lights, Camera camera) throws RemoteException {
		Raytracer raytracer = new Raytracer(imageConfigs, objects, lights, camera);
		String imageString = raytracer.Trace();
		return imageString;
	}
	
	public static void main(String args[]) {		
		try {
			RenderServer renderServer = new RenderServer();
			Registry registry = LocateRegistry.createRegistry(2000);
			registry.rebind("Server", renderServer);
			System.out.println("> Render server started!");
		} catch (RemoteException ex) {
			System.out.println("> Failed to start the server...");
			System.out.println("> Error: " + ex.getMessage());
		}
	}

}
