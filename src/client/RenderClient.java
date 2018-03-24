package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import commons.IRender;
import lights.DirectionalLight;
import lights.Light;
import lights.PointLight;
import lights.Spotlight;
import materials.BlinnPhongMaterial;
import materials.Color;
import primitives.IHittable;
import primitives.Sphere;
import renderer.Camera;
import renderer.ImageConfigs;
import renderer.Vector3;
import renderer.ImageConfigs.Codification;
import renderer.PPMWriter;

public class RenderClient extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton renderButton;
	private JTextField fileNameTxt;
	private JTextArea messageTxtArea;
	private IRender renderServer;
	
	public RenderClient() {
		super("Render client");
		ConnectToServer();
		SetupJFrame();
	}
	
	private void ConnectToServer() {
		try {
			Registry registry = LocateRegistry.getRegistry(2000);
			renderServer = (IRender) registry.lookup("//177.89.187.106/Server");
			
		} catch (RemoteException ex) {
			System.out.println("> Couldn't connect to the server. RemoteException thrown.");
			System.out.println("> Error: " + ex.getMessage());
		} catch (NotBoundException ex) {
			System.out.println("> Couldn't connect to the server. NotBoundException thrown.");
			System.out.println("> Error: " + ex.getMessage());
		}
	}
	
	private void SetupJFrame() {
		Container container = getContentPane();
		
		JPanel topPanel = new JPanel(new GridLayout(2, 2));
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel();
		
		JLabel imageNameLabel = new JLabel("File name: ");
		fileNameTxt = new JTextField(7);
		topPanel.add(imageNameLabel);
		topPanel.add(fileNameTxt);
		
		messageTxtArea = new JTextArea(10, 20);
		messageTxtArea.setEditable(false);
		centerPanel.add(messageTxtArea);
		
		renderButton = new JButton("Render");
		renderButton.addActionListener(new ButtonHandler());
		btnPanel.add(renderButton);
		
		container.add(topPanel, BorderLayout.NORTH);
		container.add(centerPanel);
		container.add(btnPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocation(15, 15);
		show();
	}
	
	private void SetEnableRenderOptions(boolean val) {
		renderButton.setEnabled(val);
		fileNameTxt.setEnabled(val);
	}
	
	private class TraceThread implements Runnable {

		@Override
		public void run() {
			messageTxtArea.setText("> Tracing...");
			
			ImageConfigs imageConfigs = new ImageConfigs(fileNameTxt.getText(), 1920, 960, 8, Codification.ASCII);
			
			Vector3 cameraLookFrom = new Vector3(9, 3.5d, 15);
			Vector3 cameraLookAt = new Vector3(0, 0, -1);
			double cameraFOV = 30;
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
			
			try {
				String result = renderServer.getRender(imageConfigs, objects, lights, camera);
				messageTxtArea.setText(messageTxtArea.getText() + "\n" + "> Writing...");
				
				PPMWriter ppmWriter = new PPMWriter(imageConfigs.getFileName());
				ppmWriter.Write(result);
				messageTxtArea.setText(messageTxtArea.getText() + "\n" + "> Finished rendering " + imageConfigs.getFileName() + ".ppm");
				
				SetEnableRenderOptions(true);
			}
			catch (RemoteException ex) {
				messageTxtArea.setText("> Couldn't request.\nError: " + ex.getMessage());
			}
		}
		
	}
	
	private class ButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent ae) {
			SetEnableRenderOptions(false);
			TraceThread traceThread = new TraceThread();
			Thread thread = new Thread(traceThread);
			thread.start();
		}
	}
	
	public static void main(String args[]) {
		new RenderClient();
	}
	
}
