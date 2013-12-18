package robot;

import khepera.control.Braitenberg;
import khepera.control.Destino;
import khepera.control.Trayectoria;
import khepera.escenario.Escenario;
import khepera.robot.IzqDer;
import khepera.robot.Polares;
import khepera.robot.RobotKhepera;
import comm.Difusion;
import corba.camara.CamaraInt;
import corba.camara.suscripcionD;
import corba.instantanea.EstadoRobotD;
import corba.instantanea.EstadoRobotDHolder;
import corba.instantanea.InstantaneaD;
import corba.khepera.escenario.EscenarioD;
import corba.khepera.robot.PosicionD;
import corba.robot.RobotSeguidorIntHelper;

/**
 * This class is the implemetation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class RobotSeguidorIntServerImpl extends corba.robot.RobotSeguidorIntPOA {
	
	org.omg.CORBA.ORB orb;
    CamaraInt camara;

    String minombre;
    int miid;
    String miIOR;
    
    public corba.robot.RobotSeguidorInt refrob = null; //referencia al robot 
    public corba.instantanea.PuntosRobotD puntrob = null; //puntos donde se encuentra el robot
    public corba.khepera.robot.PosicionD posObj = null; //posicion del objetivo del robot
    private int idLider = -1; // robot lider
    
    private InstantaneaD instantanea;
	private EscenarioD escenario; 
	
	private RobotKhepera r; 
	private Trayectoria tra; 
	private Destino dst = new Destino(); 
	private Braitenberg bra = new Braitenberg();
	
	/**
	 * Constructor for RobotSeguidorIntServerImpl 
	 */
	public RobotSeguidorIntServerImpl() {
	}
	
	@Override
	public void ObtenerEstado(EstadoRobotDHolder est) {
				
		corba.instantanea.EstadoRobotD _r = new corba.instantanea.EstadoRobotD();
		_r.nombre = this.minombre;
		_r.id = this.miid;
		_r.IORrob = this.miIOR;
		_r.refrob = this.refrob;
		_r.puntrob = this.puntrob;
		_r.posObj = this.posObj;
		_r.idLider = this.idLider;
		
	    est.value = _r;	
	}

	@Override
	public void ModificarEscenario(EscenarioD esc) {
		this.escenario = esc;
		this.resetRobot();
	}

	@Override
	public void ModificarObjetivo(PosicionD NuevoObj) {
		this..idLider = -1; //reset del lider
		this.posObj= NuevoObj;
	}

	@Override
	public void ModificarPosicion(PosicionD npos) {
		//this.posObj = npos;
		this.r.fijarPosicion(npos);
	}

	@Override
	public void ModificarLider(int idLider) {
		this.idLider = idLider;
	}
	
	//TODO: Esta llamada tiene que estar sincronizada porque es posible llamarla en mitad de la ejecucion del movimiento 
	private void resetRobot(){
		this.posObj = new PosicionD(10, 10);
		this.r = new RobotKhepera(new PosicionD(10, 10), new Escenario(this.escenario), 0);
		this.puntrob = this.r.posicionRobot();
	}
	
	public void start(){
        new RobotDifusion().start();
    }
	
	//------------------------------------------------------------------------------
    // La clase anidada RobotDifusion
    //------------------------------------------------------------------------------

    class RobotDifusion extends Thread{

      private Difusion difusion;
      private EstadoRobotD sr;
      private suscripcionD sus;
      
      private void movementControl(){
    	  
    	  r.avanzar();
    	  Polares mipos = r.posicionPolares(); 
    	  puntrob = r.posicionRobot();
    	  
    	  tra = new Trayectoria(mipos, posObj); 
    	  float[] ls=r.leerSensores(); 
    	  IzqDer nv = dst.calcularVelocidad((Object)tra); 
    	  IzqDer nv2 = bra.calcularVelocidad((Object) ls); 
    	  nv.izq += nv2.izq/90;
    	  nv.der += nv2.der/90; 
    	  r.fijarVelocidad(nv.izq,nv.der);
      }

      public void run(){
    	  sus = camara.SuscribirRobot(miIOR);

    	  escenario = sus.esc;
          miid=sus.id;
          difusion = new Difusion(sus.iport);
          
          resetRobot();
          
        while(true){
           //EJERCICIO: recibir instantanea
        	instantanea = (InstantaneaD) difusion.receiveObject();
        	
        	for (int i = 0 ; i < instantanea.estadorobs.length; i++ ){
        		
        		sr = instantanea.estadorobs[i]; 
        		if(idLider >= 0 && sr.id == idLider){
        			posObj = sr.puntrob.centro;
        			//System.out.println("Robot "+ miid + ",ha empezado a seguir al" + i + "Lider (" + sr.nombre + ")");
        			break;
        		}
	            
        	}
        	
        	this.movementControl();
	          
          try{
            Thread.sleep(400);
          }catch(InterruptedException e){
            e.printStackTrace();
          }
        }
      }
    }
}
