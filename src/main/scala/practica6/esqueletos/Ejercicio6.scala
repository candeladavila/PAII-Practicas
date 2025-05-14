package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

object mesa {
  // CS- fumador i: No puede fumar hasta que estén en la mesa los ingredientes que le faltan
  // CS-Agente: No pone un nuevo ingrediente hasta que el fumador no ha terminado de fumar
  /*
  DESCRIPCIÓN DEL PROBLEMA:
  Para poder fumarte un cigarrillo necesitas tres ingredientes. Hay tres fumadores y cada uno de ellos tiene un
  ingrediente distinto.
  Hay un agente que tiene de infinitas cantidades de los tres ingredientes. Escoge de forma aleatoria dos ingredientes
  y los pone encima de la mesa para que el fumador que tiene el ingrediente que falta se lo pueda fumar.

  VARIABLES:
    - ingr = ingredientes que no hay en la mesa
      Posibles valores:
        - -1 = no hay nada en la mesa
        - 0 = no tabaco
        - 1 = no papel
        - 2 = no cerillas
    - mutex = semáforo sobre la variable ingredientes para asegurar la exclusión mútua

  MÉTODOS:
    - quieroFumar
    - finFumar
    -nuevoIngr = añade dos ingredientes a la mesa y cambia la variable ingr al ingrediente que falte

  NOTAS:
  Mientras una persona está fumando nadie hace NADA más

   */
  private var ingr = -1 // el ingrediente que no está-- -1=mesa vacía, 0=no tabaco, 1=no papel, 2=no cerillas
  private val mutex = new Semaphore (1) //al principio todos pueden poner
  private val fumando = Array.fill(3)(new Semaphore(0)) //al principio no hay ingredientes y nadie puede fumar
  private val noHayElementos = new Semaphore (1) //inicialmente no hay elementos

  def quieroFumar(i: Int) = {
    // el fumador i quiere fumar
    fumando(i).acquire()
    log(s"Fumador $i fuma")
  }

  def finFumar(i: Int) = {
    // el fumador i termina de fumar
    log(s"Fumador $i termina de fumar")
    noHayElementos.release()
  }

  def nuevosIngr(ingrediente: Int) = {
    // el agente pone nuevos ingredientes (ingr es el ingrediente que no pone)
    noHayElementos.acquire() //podrá entrar cuando alguien haya cogido un elemento y terminado de fumar
    mutex.acquire()
    ingr = ingrediente //pone el nuevo ingrediente
    fumando(ingr).release() //libera al fumador que toque
    mutex.release()
  }
}

object Ejercicio6 {
  def main(args: Array[String]): Unit = {
    val fumador = new Array[Thread](3)
    for (i <- fumador.indices)
      fumador(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500))
          mesa.quieroFumar(i)
          Thread.sleep(Random.nextInt(200))
          mesa.finFumar(i)
      }
    val agente = thread {
      while (true)
        Thread.sleep(Random.nextInt(500))
        mesa.nuevosIngr(Random.nextInt(3))
    }
  }
}
