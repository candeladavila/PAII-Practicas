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
    - enEspera = new ListBuffer[Int]() => lista para que almacene los ids de los procesos que están esperando
    - procEsperando = 0 => variable para controlar el numero de hebras en espera
    - sigProceso = -1 => variable que tiene el id del siguiente proceso al que le toca

  MÉTODOS:
    - pidoRecursos => primero tiene que mirar si le toca -> tiene que mirar si hay más hebras o no esperando => coge recursos
    - libRecursos => libra los recursos y despierta al proceso que estaba esperando porque no tenía suficientes recursos disponibles
  NOTAS:
    - La repartición de recursos se hace en orden de solicitud
    - CS-1 = Un proceso se espera cuando alguien ha pedido recursos antes que él y no ha sido atendido (no se llegan a mirar siquiera si hay recursos)
    - CS-2 = Un proceso se tiene que esperar en su turno si no hay suficientes recursos en el sistema para él
    - Como notify() despierta una hebra aleatoria entonces tenemos que usar una estructura auxiliar para saber a quién tenemos que despertar en cada momento
      - En el caso de condiciones de sincronización es distinto -> se van bloqueando en orden de llegada (se hará más tarde)
    - El proceso que esté esperando en la cabeza de la cola tiene una condición de sincronización distinta, espero a que tenga los recursos que necesita
      - Para despertar a una en concreto hay que despertarla y después que espere en otro sitio diferente a los recursos
        - Ponemos dos puntos en el código de sincronización diferente para controlarlo
*/

class Recursos(rec:Int) {

  
  private var numRec = rec
  private var enEspera = new ListBuffer[Int]() //inicialmente está vacía porque no hay nadie esperando
  private var procEsperando = 0
  private var sigProc = -1  //para almacenar el id del siguiente (se puede omitir si en el if ponemos >= 1 para encolarlos a todos y comprobar la cabeza de la lista
  
  def pidoRecursos(id:Int,num:Int) =  synchronized {
    //proceso id solicita num recursos
    procEsperando += 1 //me sumo a los procesos que esperan
    log(s"Proceso $id pide $num recursos.")
    if (procEsperando > 1){ //si hay más gente esperando tengo que esperar a que me toque
      enEspera.append(id)
      while(sigProc !=  id) wait() //CS-1 -> que se ponga a true SOLO si te toca
    }
    //Cuando la hebra está aquí es o bien porque ha llegado la primera o no era la primera pero le tocaba
    while (num > numRec) wait() //CS-2 -> se espera hasta que no tenga los recursos que necesita
    //Cuando la hebra está aquí entonces ya tiene los recursos para hacer su proceso
    numRec -= num //coge los recursos que necesita
    procEsperando -= 1 //ya queda uno menos esperando
    log(s"Proceso $id coge $num recursos. Quedan $numRec")
    /*
    Mira si hay más procesos esperando
      - si no hay más => FIN
      - si hay más esperando => DESPIERTA AL SIGUIENTE
     */
    if (procEsperando != 0){
      //tenemos que poner como siguiente proceso al siguiente que haya en la lista de espera
      if (procEsperando != 0) {
        sigProc = enEspera.remove(0) //saco el id del siguiente proceso de la estructura y lo guardo en la variable del siguiente proceso
        notifyAll() //porque no podemos elegir a quien despertamos, después se bloquea
        //Despertamos a todos, pero como el booleano de while(sigProc!= id) solo lo va a ver cierto uno entonces el resto se van a bloquear
      } else{
        //En caso de que no quede nadie esperando tenemos que poner sigProc que esté esperando a -1 => reiniciar la variable para que no se cuele nadie
        sigProc -= 1
      }
    }

      
  }

  def libRecursos(id:Int,num:Int) =  synchronized {
    //proceso id devuelve num recursos
    numRec += num //aumentamos los recursos disponibles al liberarlo
    log(s"Proceso $id devuelve $num recursos. Quedan $numRec")
    //La intención es despertar al que estaba esperando porque no tenía suficientes recursos libres
    notifyAll() //despertamos a todos porque no podemos elegir a quien despertar
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
