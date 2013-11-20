package robot;

import corba.instantanea.EstadoRobotDHolder;
import corba.khepera.escenario.EscenarioD;
import corba.khepera.robot.PosicionD;

/**
 * This class is the implemetation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class RobotSeguidorIntServerImpl extends corba.robot.RobotSeguidorIntPOA {
	/**
	 * Constructor for RobotSeguidorIntServerImpl 
	 */
	public RobotSeguidorIntServerImpl() {
	}

	@Override
	public void ObtenerEstado(EstadoRobotDHolder est) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ModificarEscenario(EscenarioD esc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ModificarObjetivo(PosicionD NuevoObj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ModificarPosicion(PosicionD npos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ModificarLider(int idLider) {
		// TODO Auto-generated method stub
		
	}
}
