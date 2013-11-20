package camara;

import corba.camara.IPYPortD;
import corba.camara.ListaSuscripcionD;
import corba.camara.suscripcionD;
import corba.instantanea.InstantaneaD;
import corba.khepera.escenario.EscenarioD;

/**
 * This class is the implemetation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class CamaraIntServerImpl extends corba.camara.CamaraIntPOA {
	/**
	 * Constructor for CamaraIntServerImpl 
	 */
	public CamaraIntServerImpl() {
	}

	@Override
	public suscripcionD SuscribirRobot(String IORrob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public suscripcionD SuscribirConsola(String IORcons) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public EscenarioD ObtenerEscenario() {
		// TODO Auto-generated method stub
		return null;
	}
}
