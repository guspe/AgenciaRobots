package corba.instantanea;


/**
* corba/instantanea/PuntosRobotD.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/Gustavo/Documents/AgenciaRobots/prj-corba/robot.idl
* mi?rcoles 20 de noviembre de 2013 17H35' CET
*/

public final class PuntosRobotD implements org.omg.CORBA.portable.IDLEntity
{
  public corba.khepera.robot.PosicionD centro = null;
  public corba.khepera.robot.PosicionD sens[] = null;
  public corba.khepera.robot.PosicionD finsens[] = null;
  public corba.khepera.robot.PosicionD inter[] = null;

  public PuntosRobotD ()
  {
  } // ctor

  public PuntosRobotD (corba.khepera.robot.PosicionD _centro, corba.khepera.robot.PosicionD[] _sens, corba.khepera.robot.PosicionD[] _finsens, corba.khepera.robot.PosicionD[] _inter)
  {
    centro = _centro;
    sens = _sens;
    finsens = _finsens;
    inter = _inter;
  } // ctor

} // class PuntosRobotD
