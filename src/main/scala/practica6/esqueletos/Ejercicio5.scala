package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

object gestorAgua {
  // CS-Hid1: El hidrógeno que quiere formar una molécula espera si ya hay dos hidrógenos
  // CS-Hid2: Un hidrógeno debe esperar a los otros dos átomos para formar la molécula
  // CS-Ox1: El oxígeno que quiere formar una molécula espera si ya hay un oxígeno
  // CS-Ox2: El oxígeno debe esperar a los otros dos átomos para formar la molécula
  private var nOxigeno = 0
  private var nHidrogeno =0
  private val mutex = new Semaphore(1)
  private val puertaHid = new Semaphore(1)
  private val puertaOx = new Semaphore(1)
  private val molecula = new Semaphore(0)

  def oxigeno(id: Int) = {
    puertaOx.acquire()
    mutex.acquire()
    log(s"Oxígeno $id quiere formar una molécula")
    nOxigeno += 1
    if (nHidrogeno + nOxigeno < 3) {
      mutex.release()
      molecula.acquire() //se bloquea hasta que se haga la molecula
      mutex.acquire()
    } else {
      log(s"      Molécula formada!!!")
    }
    nOxigeno -= 1
    if (nHidrogeno + nOxigeno > 0) molecula.release() //siguen quedando elementos bloqueados -> libero en cascada
    else { //abrimos las puertas para que puedan volver a entrar
      puertaOx.release()
      puertaHid.release()
    }
    mutex.release()
  }

  def hidrogeno(id: Int) = {
    // el hidrógeno id quiere formar una molécula
    puertaHid.acquire()
    mutex.acquire()
    log(s"Hidrógeno $id quiere formar una molécula")
    nHidrogeno +=1
    if (nHidrogeno ==1)
      puertaHid.release() //si todavía no hay 2 hidrógenos puede entrar 1 más, vuelvo a abrir la puerta
    if (nHidrogeno+nOxigeno < 3) {
      mutex.release()
      molecula.acquire()
      mutex.acquire()
    } else{
      log(s"      Molécula formada!!!")
    }
    nHidrogeno -=1
    if (nHidrogeno + nOxigeno > 0) molecula.release()
    else{
      puertaHid.release()
      puertaOx.release()
    }
    mutex.release()
  }
}
object Ejercicio5 {

  def main(args:Array[String]) =
    val N = 5
    val hidrogeno = new Array[Thread](2*N)
    for (i<-0 until hidrogeno.length)
      hidrogeno(i) = thread{
        Thread.sleep(Random.nextInt(500))
        gestorAgua.hidrogeno(i)
      }
    val oxigeno = new Array[Thread](N)
    for(i <- 0 until oxigeno.length)
      oxigeno(i) = thread {
        Thread.sleep(Random.nextInt(500))
        gestorAgua.oxigeno(i)
      }
    hidrogeno.foreach(_.join())
    oxigeno.foreach(_.join())
    log("Fin del programa")
}
