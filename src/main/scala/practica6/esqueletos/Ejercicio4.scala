package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

class Coche(C: Int) extends Thread {
  // CS-pasajero1: si el coche está lleno, un pasajero no puede subir al coche hasta que haya terminado
  // el viaje y se hayan bajado los pasajeros de la vuelta actual
  // CS-pasajero2: un pasajero que está en el coche no puede bajarse hasta que haya terminado el viaje
  // CS-coche: el coche espera a que se hayan subido C pasajeros para dar una vuelta

  /*
  DESCRIPCIÓN DEL PROBLEMA:
  Tenemos una montaña rusa con capacidad C
  Si la montaña está llena (ya se han subido C pasajeros) entonces empieza el trayecto.
  Cuando termina el trayecto (tiempo T) entonces todos los pasajeros se bajan y dejan que entren nuevos pasajeros

  VARIABLES:
    - numPas = número de pasajeros
    - hayHueco = semáforo para permitir el acceso o no a nuevos pasajeros (se cierra cuando numPas == C)
    - viajeTerminado = semáforo que controla si hay o no un viaje iniciado o no
    - estaLleno = semáforo que controla si está lleno o no el coche de la montaña rusa

   MÉTODOS:
    - nuevoPaseo
      Para que haya un nuevo paseo el coche tiene que estar lleno -> desbloqueamos el semáforo lleno
      Para desbloquear el coche hace viajeTerminado.acquire (se libera en el método FinViaje)
    - esperaLleno -> se desbloquea cuando está lleno para imprimir el viaje
    - finViaje -> manda el mensaje y después desbloquea a un pasajero                                                                          

   */
  private var numPas = 0
  private val hayHueco = new Semaphore(1) // inicialmente está vacío el coche
  private val estaLleno = new Semaphore(0)
  private val empiezaViaje = new Semaphore(0)
  private val viajeTerminado = new Semaphore(0)

  def nuevoPaseo(id: Int) = {
    // el pasajero id quiere dar un paseo en la montaña rusa
    hayHueco.acquire()
    numPas += 1
    log(s"El pasajero $id se sube al coche. Hay $numPas pasajeros.")
    if (numPas < C)
      hayHueco.release() //no se ha llenado el coche
    if (numPas == C)
      estaLleno.release()

    //CUANDO TERMINA EL VIAJE
    viajeTerminado.acquire() //es liberado por el metodo finViaje
    numPas -=1
    log(s"El pasajero $id se baja del coche. Hay $numPas pasajeros.")
    if (numPas > 0) //quedan más pasajeros
      viajeTerminado.release() //para liberar a los demás pasajeros
    if (numPas == 0) //si no quedan pasajeros -> vuelve a liberar el coche para que puedan entrar nuevos pasajeros
      hayHueco.release()
  }

  def esperaLleno = {
    // el coche espera a que se llene para dar un paseo
    estaLleno.acquire()
    log(s"        Coche lleno!!! empieza el viaje....")
  }

  def finViaje = {
    // el coche indica que se ha terminado el viaje
    log(s"        Fin del viaje... :-(")
    viajeTerminado.release()
  }

  override def run = {
    while (true) {
      esperaLleno
      Thread.sleep(Random.nextInt(Random.nextInt(500))) // el coche da una vuelta
      finViaje
    }
  }
}

object Ejercicio4 {
  def main(args: Array[String]) =
    val coche = new Coche(5)
    val pasajero = new Array[Thread](12)
    coche.start()
    for (i <- 0 until pasajero.length)
      pasajero(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500)) // el pasajero se da una vuelta por el parque
          coche.nuevoPaseo(i)
      }
}
