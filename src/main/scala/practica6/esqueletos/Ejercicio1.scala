package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

/*
  Necesitamos un entero para indicar cuantas mediciones se han hecho
    - mediciones = 0 //inicialmente
    - el valor mediciones se modifica en cada uno de los sensores sumando 1
  Los sensores:
    - bloquean cuando hacen una medición
    - desbloquean cuando el controlador detecte que el valor de mediciones = 3 y se reinicia el contador mediciones desbloqueando a los tres sensores
*/
/*
  Se puede plantear el problema sin semáforos binarios (más facil) con un semáfoto que puedan modificar los tres sensores
*/
object mediciones {
  // CS-Sensor-i: sensor i no puede volver a medir hasta que el trabajador no ha
  // terminado de procesar las medidas anteriores
  // CS-Trabajador: no puede realizar su tarea hasta que no están las
  // tres mediciones
  private val mutex = new Semaphore(1)
  private var mediciones = 0 //variable de contadores
  private val esperaSensor = new Array[Semaphore](3) //CS-Sensor-i
  private val esperaTrab = new Semaphore(0)
  for (i <- 0 until esperaSensor.length)
      esperaSensor(i) = new Semaphore(1)

  def nuevaMedicion(id: Int) = {
    mutex.acquire()
    mediciones +=1 //incrementa en 1 el número de mediciones
    log(s"Sensor $id almacena su medición" )
    if (mediciones == 3) esperaTrab.release() //si han terminado todos los sensores -> bloqueo
    mutex.release()
    esperaSensor(id).acquire()
  }

  def leerMediciones() = {
    /*
    Tiene que hacer un release por cada semáforo asociado a cada sensor
     */
    /*
    El semáforo del trabajador está bloqueado hasta que se realice la tercera medición -> release del semáforo asociado al controlador
    El controlador desbloqueará a los sensores de nuevo para realizar una nueva medición
     */
    esperaTrab.acquire()
    mutex.acquire()
    mediciones = 0 //reestablece las mediciones a 0
    log(s"El trabajador recoge las mediciones")
    mutex.release()
  }

  def finTarea() = {
    log(s"El trabajador ha terminado sus tareas")
    esperaSensor.foreach(_.release()) //libera a todos los sensores
  }
}

object Ejercicio1 {
  def main(args: Array[String]) =
    val sensor = new Array[Thread](3)

    for (i <- 0 until sensor.length)
      sensor(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(100)) // midiendo
          mediciones.nuevaMedicion(i)
      }

    val trabajador = thread {
      while (true)
        mediciones.leerMediciones()
        Thread.sleep(Random.nextInt(100)) // realizando la tarea
        mediciones.finTarea()
    }
}
