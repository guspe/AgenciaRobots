package camara;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import khepera.escenario.Escenario;
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

	private List<String> listaConsolas = Collections
			.synchronizedList(new ArrayList<String>());
	private List<String> listaRobots = Collections
			.synchronizedList(new ArrayList<String>());
	private List<EstadoRobotD> listaEstados = Collections
			.synchronizedList(new ArrayList<EstadoRobotD>());

	private List<String> listaRobotsNuevos = Collections
			.synchronizedList(new ArrayList<String>());
	private List<String> listaRobotEliminados = Collections
			.synchronizedList(new ArrayList<String>());

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
		Escenario esc;
		try {
			// esc = new
			// Escenario("C:/Users/bogdan/Documents/University/6/DYA/BigPro/prj-camara/lib/laberinto");
			esc = new Escenario(
					"C:/Users/bogdan/Documents/University/6/DYA/BigPro/prj-camara/lib/dibujo");
			this.escenario = esc.toEscenarioD();

		} catch (FileNotFoundException e) {
			System.out
					.println("No se puede abrir el fichero del escenario por defecto!!");
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for CamaraIntServerImpl
	 */
	public CamaraIntServerImpl() {
	}

	@Override
	public suscripcionD SuscribirRobot(String IORrob) {

		int id = nrobots;
		suscripcionD suscripcion = new suscripcionD(id, this.ipyport,
				this.escenario);

		// Podemos estar quitando elementos
		this.listaRobotsNuevos.add(IORrob);

		nrobots++;

		return suscripcion;
	}

	@Override
	public suscripcionD SuscribirConsola(String IORcons) {

		int id = this.listaConsolas.size();
		suscripcionD suscripcion = new suscripcionD(id, this.ipyport,
				this.escenario);

		// Podemos estar quitando elementos
		this.listaConsolas.add(IORcons);

		return suscripcion;
	}

	@Override
	public void BajaRobot(String IORrob) {
		this.listaRobotEliminados.add(IORrob);
	}

	@Override
	public void BajaConsola(String IORcons) {
		this.listaConsolas.remove(IORcons);
	}

	@Override
	public ListaSuscripcionD ObtenerLista() {
		ListaSuscripcionD lista = new ListaSuscripcionD(
				(String[]) this.listaRobots.toArray(),
				(String[]) this.listaConsolas.toArray());
		return lista;
	}

	@Override
	public IPYPortD ObtenerIPYPortDifusion() {
		return this.ipyport;
	}

	//Me he creado un metodo para obtener la lista de robots
	public ArrayList<String> obtenerListaRobots()
	{
		ArrayList<String> robots = new ArrayList<String>(this.listaRobots);
		return robots;
	}
	
	@Override
	public InstantaneaD ObtenerInstantanea() {
		return this.instantanea;
	}

	@Override
	public void ModificarEscenario(EscenarioD esc) {

		// EscenarioD oldEscenario = this.escenario;
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
			for (Iterator<String> i = listaRobots.iterator(); i.hasNext();) {
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
			synchronized (listaConsolas) {
				for (Iterator<String> i = listaConsolas.iterator(); i.hasNext();) {
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

			escenarioModificado = false;
		}

		private void obtenerEstadosRobots() {

			corba.instantanea.EstadoRobotDHolder st = new EstadoRobotDHolder();
			List<String> listaFallos = new ArrayList<String>();

			String ior = null;
			listaEstados.clear();

			// pasar los nuevos robots a la lista de robots
			synchronized (listaRobotsNuevos) {
				for (Iterator<String> i = listaRobotsNuevos.iterator(); i.hasNext();) {
					ior = i.next().toString();
					listaRobots.add(ior);
				}
				listaRobotsNuevos.clear();
			}

			// eliminar los robots dados de baja
			synchronized (listaRobotEliminados) {
				for (Iterator<String> i = listaRobotEliminados.iterator(); i.hasNext();) {
					ior = i.next().toString();
					listaRobots.remove(ior);
				}
				listaRobotEliminados.clear();
			}

			//no necesitamos hacer el synchronized porque listaRobots no se modifica en ningun otro lado
			for (Iterator<String> i = listaRobots.iterator(); i.hasNext();) {
				try {
					ior = i.next().toString();
					RobotSeguidorInt robot = RobotSeguidorIntHelper.narrow(orb_
							.string_to_object(ior));

					robot.ObtenerEstado(st);
					listaEstados.add(st.value);

				} catch (Exception e) {
					System.out.println("Detectado fallo Robot: " + ior);
					listaFallos.add(ior);
				}
			}

			// borrar los robots inactivos
			for (Iterator<String> i = listaFallos.iterator(); i.hasNext();) {

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
