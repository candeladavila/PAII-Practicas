package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

//BICHITOS

class Nido(B: Int) {
  // CS-bebé i: no puede coger un bichito del plato si está vacío
  // CS-papá/mamá: no puede dejar un bichito en el plato si está lleno

  //en el constructor tenemos el parámetro B que indica la capacidad del plato (máximo número de bichitos)
  private var plato = 0
  private val mutex = new Semaphore(1) //sobre plato
  private val hayBichitos = new Semaphore(0) //CS-papa/mama
    //Se inicializa a 0 para que si llega un bebé primero se bloquée al no haber bichitos
  private val hayEspacio = new Semaphore(1) //CS-bebe //se inicializa a 1 porque suponemos que el plato está vacio, 
  // es decir, hay espacio

  def cojoBichito(i: Int) = {
    // el bebé i coge un bichito del plato
    /*
    Los bebes no pueden comer si no hay en el plato, primero bloquean (acquire)
    Después cogemos el semáforo
    Pensamos cuando desbloqueamos los hilos o hebras
     */
    hayBichitos.acquire() //aquí están dormidos
    mutex.acquire()
    plato -= 1
    log(s"Bebé $i coge un bichito. Quedan $plato bichitos")
    if (plato > 0) hayBichitos.release()
    if (plato == B-1) hayEspacio.release() //avisa a los padres de que ha liberado espacio del plato que estaba lleno (los desbloquea)
    mutex.release()
  }

  def pongoBichito(i: Int) = {
    // el papá/la mamá pone un bichito en el plato (0=papá, 1=mamá)
    hayEspacio.acquire()
    mutex.acquire()
    plato +=1
    if (i == 0) {
      log(s"Papá pone un bichito. Quedan $plato bichitos")
    } else{
      log(s"Mamá pone un bichito. Quedan $plato bichitos")
    }
    if (plato < B) hayEspacio.release()
    if (plato == 1) hayBichitos.release() //solo cuando pongo el primer bichito en el plato vacío desbloqueo a un bebe
    mutex.release()
  }
}

object Ejercicio7 {
  def main(args: Array[String]): Unit =
    val N = 10
    val nido = new Nido(5)
    val bebe = new Array[Thread](N)
    for (i <- bebe.indices)
      bebe(i) = thread {
        while (true)
          nido.cojoBichito(i)
          Thread.sleep(Random.nextInt(600))
      }
    val papa = new Array[Thread](2)
    for (i <- papa.indices)
      papa(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(100))
          nido.pongoBichito(i)
      }
}
