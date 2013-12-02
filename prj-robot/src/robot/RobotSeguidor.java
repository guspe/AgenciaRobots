package robot;

import java.util.Properties;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.ThreadPolicyValue;

import corba.camara.*;

public class RobotSeguidor {

	  static CamaraInt camara;
	  static int ok=0;
	  
	public static void main(String[] args) {

		Properties props = System.getProperties();
		props.setProperty("org.omg.CORBA.ORBClass","com.sun.corba.se.internal.POA.POAORB"); 
		props.setProperty("org.omg.CORBA.ORBSingletonClass","com.sun.corba.se.internal.corba.ORBSingleton");
		
		// Solo si se cambia el host 
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		// Solo si se cambia el port 
		props.put("org.omg.CORBA.ORBInitialPort", "1050");
		
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
			POA poa = poaRoot.create_POA("RobotSeguidorIntServerImpl_poa",	poaRoot.the_POAManager(), policies);

			// Create the servant
			RobotSeguidorIntServerImpl servant = new RobotSeguidorIntServerImpl();

			// Activate the servant with the ID on myPOA
			byte[] objectId = "Robot".getBytes();
			poa.activate_object_with_id(objectId, servant);
			
			// Activate the POA manager
			poaRoot.the_POAManager().activate();

			// Get a reference to the servant and write it down.
			obj = poa.servant_to_reference(servant);


		    do{
		        try{
		        	//EJERCICIO:Conectar con el servidor de nombre y obtener una referencia 
		        	//a la **camara**
		        	org.omg.CORBA.Object ncobj = orb.resolve_initial_references("NameService");
					NamingContextExt nc = NamingContextExtHelper.narrow(ncobj);
					camara = CamaraIntHelper.narrow( nc.resolve(nc.to_name("Camara")));
					
					System.out.println("Identificador: " + servant);
			         //EJERCICIO: convertir la referencia al robot en un IOR en formato String 
					servant.miIOR = orb.object_to_string(obj); 

					servant.orb = orb;
		          	servant.camara = camara;
		          	if (args.length>0) servant.minombre = args[0]; else servant.minombre="Robot";
		          	ok=1;
		        }  catch (NotFound e1) {
		        	System.out.println("Camara no se encuentra en NS");
		        }  catch (InvalidName e1) {
		        	System.out.println("Camara es un nombre valido");
		        }  catch(Exception ex) {
		        	  ex.printStackTrace();
			          System.out.println("El robot no se registro bien en la camara. Reintentando...");
			    }
		      } while(ok==0);

		      servant.start();
	
			
			System.out.println("CORBA Server ready...");

			// Wait for incoming requests
			orb.run();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

