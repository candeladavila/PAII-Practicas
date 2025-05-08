package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

class Olla(R: Int) {
  // CS-caníbal i: no coge una ración de la olla si está vacía
  // CS-cocinero: no cocina un nuevo explorador hasta que la olla está vacía
  /*
  DESCRIPCIÓN DEL PROBLEMA:
  Hay un grupo de caníbales que comen una ración cada uno.
  Cuando se agotan las raciones el cocinero repone R raciones

  VARIABLES:
    - olla = recurso compartido
    - hayComida = variable que representa si queda comida o no para que puedan entrar los caníbales a comer
      - se bloquea cuando no queda
      - se desbloquea cuando repone el cocinero
    - hayQueCocinar = variable que despierta al cocinero para que se ponga a cocinar
    - cocinero = variable que representa el acto de

   MÉTODOS:
    - racion
      1. Mira si hay comida (si no se bloquea)
      2. Coge comida
      3. Si era el último trozo (no queda nada) -> no hace release, no pueden entrar más caníbales
         Despierta al cocinero
      4. Si no era el último trozo libera la variable hayComida

   - dormir = el cocinero duerme hasta que le despiertan porque no hay comida y entonces empieza a cocinar
   - llenarOlla
      1. Reinicia el número de raciones de la olla
      2. Libera la variable hayComida para que puedan volver a comer los caníbales

   */
  private var olla = R // inicialmente llena -> R = numero de raciones
  private val hayComida = new Semaphore(1) //1 si hay comida, 0 si no hay comida
  private val hayQueCocinar = new Semaphore (0) //0 si no hay que cocinar, 1 si hay que cocinar
  private val cocinero = new Semaphore(0) //0 si está cocinando, 1 si está cocinando

  def racion(i: Int) = {
    // caníbal i coge una ración de la olla
    hayComida.acquire()
    olla -= 1 //come una ración
    log(s"Caníbal $i coge una ración de la olla. Quedan $olla raciones.")
    if (olla == 0)
      hayQueCocinar.release()
    else
      hayComida.release()
  }

  def dormir = {
    // cocinero espera a que la olla esté vacía
    hayQueCocinar.acquire()
    cocinero.release()
  }

  def llenarOlla = {
    cocinero.acquire()
    olla = 5 //se rellena la olla
    log(s"El cocinero llena la olla. Quedan $olla raciones.")
    hayComida.release() //vuelve a haber comida
  }
}

object Ejercicio8 {
  def main(args: Array[String]): Unit =
    val NCan = 20
    val olla = new Olla(5)
    val canibal = new Array[Thread](NCan)
    for (i <- canibal.indices)
      canibal(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500))
          olla.racion(i)
      }
    val cocinero = thread {
      while (true)
        olla.dormir
        Thread.sleep(500) // cocinando
        olla.llenarOlla
    }
}
