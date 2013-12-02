package camara;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import comm.Difusion;
import corba.camara.IPYPortD;
import corba.camara.ListaSuscripcionD;
import corba.camara.suscripcionD;
import corba.consola.ConsolaInt;
import corba.consola.ConsolaIntHelper;
import corba.instantanea.EstadoRobotD;
import corba.instantanea.EstadoRobotDHolder;
import corba.instantanea.InstantaneaD;
import corba.khepera.escenario.EscenarioD;
import corba.robot.RobotSeguidorInt;
import corba.robot.RobotSeguidorIntHelper;

/**
 * This class is the implemetation object for your IDL interface.
 * 
 * Let the Eclipse complete operations code by choosing 'Add unimplemented
 * methods'.
 */
public class CamaraIntServerImpl extends corba.camara.CamaraIntPOA {

	private org.omg.PortableServer.POA poa_;
	private org.omg.CORBA.ORB orb_;

	private CopyOnWriteArrayList<String> listaConsolas = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> listaRobots = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<EstadoRobotD> listaEstados = new CopyOnWriteArrayList<EstadoRobotD>();
	InstantaneaD instantanea;
	private int nrobots;
	private IPYPortD ipyport;

	private EscenarioD escenario;
	private boolean escenarioModificado;

	public CamaraIntServerImpl(org.omg.CORBA.ORB orb,
			org.omg.PortableServer.POA poa, IPYPortD iport) {
		orb_ = orb;
		poa_ = poa;
		ipyport = new IPYPortD(iport.ip, iport.port);

		nrobots = 0;

		this.escenarioModificado = false;
	}

	/**
	 * Constructor for CamaraIntServerImpl
	 */
	public CamaraIntServerImpl() {
	}

	@Override
	public suscripcionD SuscribirRobot(String IORrob) {

		int id = this.listaRobots.size();
		suscripcionD suscripcion = new suscripcionD(id, this.ipyport, null);

		this.listaRobots.add(IORrob);
		// TODO: Podemos estar quitando elementos

		nrobots++;

		return suscripcion;
	}

	@Override
	public suscripcionD SuscribirConsola(String IORcons) {

		int id = this.listaConsolas.size();
		suscripcionD suscripcion = new suscripcionD(id, this.ipyport, null);

		this.listaConsolas.add(IORcons);

		return suscripcion;
	}

	@Override
	public void BajaRobot(String IORrob) {
		// TODO Auto-generated method stub

	}

	@Override
	public void BajaConsola(String IORcons) {
		// TODO Auto-generated method stub

	}

	@Override
	public ListaSuscripcionD ObtenerLista() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPYPortD ObtenerIPYPortDifusion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstantaneaD ObtenerInstantanea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ModificarEscenario(EscenarioD esc) {

		EscenarioD oldEscenario = this.escenario;
		this.escenario = esc;
		this.escenarioModificado = true;

		// Avisar a todos los robots que se ha modificado el escenario (invocar
		// a todos los robots,el metodo "ModificarEscenario")
		// Avisar a la consola que se ha modificado el escenario (invocar a
		// todas las consolasel metodo "ModificarEscenario")
	}

	@Override
	public EscenarioD ObtenerEscenario() {
		return this.escenario;
	}

	public org.omg.PortableServer.POA _default_POA() {
		if (poa_ != null)
			return poa_;
		else
			return super._default_POA();
	}

	public void start() {
		new CamaraDifusion(ipyport).start();
	}

	// ------------------------------------------------------------------------------
	// La clase anidada CamaraDifusion
	// ------------------------------------------------------------------------------
	class CamaraDifusion extends Thread {
		private Difusion difusion;

		// ------------------------------------------------------------------------------
		public CamaraDifusion(IPYPortD iport) {
			difusion = new Difusion(iport);
		}

		private void avisarModificacionEscenario() {

			String ior = null;

			// avisar a todos los robots
			for (Iterator i = listaRobots.iterator(); i.hasNext();) {
				try {
					ior = i.next().toString();
					RobotSeguidorInt robot = RobotSeguidorIntHelper.narrow(orb_
							.string_to_object(ior));

					robot.ModificarEscenario(escenario);

				} catch (Exception e) {
					System.out.println("Detectado fallo Robot: " + ior);
				}
			}

			// avisar a todas las consolas
			for (Iterator i = listaConsolas.iterator(); i.hasNext();) {
				try {
					ior = i.next().toString();
					ConsolaInt consola = ConsolaIntHelper.narrow(orb_
							.string_to_object(ior));

					consola.ModificarEscenario(escenario);

				} catch (Exception e) {
					System.out.println("Detectado fallo Consola: " + ior);
				}
			}
		}

		private void obtenerEstadosRobots() {

			corba.instantanea.EstadoRobotDHolder st = new EstadoRobotDHolder();
			LinkedList listaFallos = new LinkedList();
			
			String ior = null;
			listaEstados.clear();
			for (Iterator i = listaRobots.iterator(); i.hasNext();) {
				try {
					ior = i.next().toString();
					RobotSeguidorInt robot = RobotSeguidorIntHelper.narrow(orb_
							.string_to_object(ior));

					robot.ObtenerEstado(st);
					listaEstados.add(st.value);

				} catch (Exception e) {
					System.out.println("Detectado fallo Robot: " + ior);
					// EJERCICIO: anyadir el robot caido a la lista de
					// fallos
					listaFallos.add(ior);
					// TODO: Hay que tener ciudado porque no podemos
					// modificar la lista (eliminar) ,porque podemos estar
					// suscribiendo a la vez
				}
			}

			// borrar los robots inactivos
			for (Iterator i = listaFallos.iterator(); i.hasNext();) {

				ior = i.next().toString();
				listaRobots.remove(ior);
			}
		}

		private void hacerDifusionEstado() {

			// si no tenemos robots ,no difundimos nada
			if (listaEstados.size() < 0) {
				return;
			}

			EstadoRobotD[] listRob = new EstadoRobotD[listaEstados.size()];
			listaEstados.toArray(listRob);
			instantanea = new InstantaneaD(listRob);

			difusion.sendObject(instantanea);
		}

		// ------------------------------------------------------------------------------
		public void run() {
			
			while (true) {

				this.obtenerEstadosRobots();

				if (escenarioModificado) {
					this.avisarModificacionEscenario();
				}

				this.hacerDifusionEstado();

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
