package esqueletosLaboratorio7

import scala.collection.mutable.ListBuffer
import scala.util.Random
/*
  Ejemplo:
  imaginamos que tenemos un total de 5 recursos
    - p0 = pide 4 recursos (se los doy y me queda 1)
    - p1 = pide 3 recursos (se espera a que haya recursos libres)
    - p2 = pide 1 recursos (aunque hay recursos para él tiene que esperar la cola)

  La idea es que hay asignar los recursos en el mismo orden en el que se han pedido (First Come First Served)

  VARIABLES:
    -
  MÉTODOS:
    - pidoRecursos =>
    - libRecursos =>
  NOTAS:
    - La repartición de recursos se hace en orden de solicitud
    - CS-1 = Un proceso se espera cuando alguien ha pedido recursos antes que él y no ha sido atendido (no se llegan a mirar siquiera si hay recursos)
    - CS-2 = Un proceso se tiene que esperar en su turno si no hay suficientes recursos en el sistema para él
*/

class Recursos(rec:Int) {

  
  private var numRec = rec
  
  def pidoRecursos(id:Int,num:Int) =  {
    //proceso id solicita num recursos
      
      log(s"Proceso $id pide $num recursos.")
      
    
      log(s"Proceso $id coge $num recursos. Quedan $numRec")
      
  }

  def libRecursos(id:Int,num:Int) =  {
    //proceso id devuelve num recursos
   
    log(s"Proceso $id devuelve $num recursos. Quedan $numRec")
    
  }
}
object Ejercicio2 {

  def main(args:Array[String]):Unit = {
    val rec = 5
    val numProc = 10
    val recursos = new Recursos(rec)
    val proceso = new Array[Thread](numProc)
    for (i<-proceso.indices)
      proceso(i) = thread{
      //  while (true){
          val r = Random.nextInt(rec)+1
          recursos.pidoRecursos(i,r)
          Thread.sleep(Random.nextInt(300))
          recursos.libRecursos(i,r)
     //   }
      }
  }
}
