package camara;

import java.util.Properties;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.IdAssignmentPolicyValue;
// import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.ThreadPolicyValue;
/*MODIFICADO*/
import corba.camara.*;
/*FIN MODIFICADO*/


public class CameraServer {

	private static corba.camara.IPYPortD ipyport;
	
	public static void main(String[] args) {

		Properties props = System.getProperties();
		props.setProperty("org.omg.CORBA.ORBClass","com.sun.corba.se.internal.POA.POAORB"); 
		props.setProperty("org.omg.CORBA.ORBSingletonClass","com.sun.corba.se.internal.corba.ORBSingleton");
		
		// Solo si se cambia el host 
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		// Solo si se cambia el port 
		props.put("org.omg.CORBA.ORBInitialPort", "1050");
		
		
		/*MODIFICADO*/
		System.out.println("suscribiendose en el NameServive localhost:1111.");
		System.out.println("Para cambio canal difusion: args[3]-> ip args[4]-> port");
		if (args.length==4)
			ipyport = new IPYPortD( args[2], Integer.parseInt(args[3]) );
		else
			ipyport = new IPYPortD( "228.7.7.7", 7010);
		System.out.println("Difusion por canal. " + ipyport.ip + " / " + ipyport.port);
		/*FIN MODIFICADO*/


		try {
			// Initialize the ORB.
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);

			// get a reference to the root POA
			org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
			POA poaRoot = POAHelper.narrow(obj);

			// Create policies for our persistent POA
			org.omg.CORBA.Policy[] policies = {
					// poaRoot.create_lifespan_policy(LifespanPolicyValue.PERSISTENT),
					poaRoot.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID),
					poaRoot.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL) 
			};

			// Create myPOA with the right policies
			POA poa = poaRoot.create_POA("CamaraIntServerImpl_poa",	poaRoot.the_POAManager(), policies);

			// Create the servant
			CamaraIntServerImpl servant = new CamaraIntServerImpl(orb,poa,ipyport);

			// Activate the servant with the ID on myPOA
			byte[] objectId = "Camara".getBytes();
			poa.activate_object_with_id(objectId, servant);
			
			// Activate the POA manager
			poaRoot.the_POAManager().activate();

			// Get a reference to the servant and write it down.
			obj = poa.servant_to_reference(servant);

			 org.omg.CORBA.Object ncobj = orb.resolve_initial_references("NameService");
			 NamingContextExt nc = NamingContextExtHelper.narrow(ncobj);
			 nc.rebind(nc.to_name("Camara"), obj);

			servant.start();

			System.out.println("Camara Server ready...");

			// Wait for incoming requests
			orb.run();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
