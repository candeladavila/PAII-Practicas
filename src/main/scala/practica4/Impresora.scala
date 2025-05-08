package practica4

import scala.util.Random
import java.util.concurrent.*

object Impresora {
  private var numImp = 2 //Número de impresoras
  private val espera = new Semaphore(1) //CS-Usuario
  private val mutex = new Semaphore(1) //mutex sobre numImp
  def quieroImpresora(id:Int) ={ //Id del usuario que la pide
    /*
    Para pedir una impresora:
    1. Espera fuera
       Asegurarnos de que hay impresoras -> Implementar la espera cuando no hay impresoras
       No puede ser un while (es una espera activa, no sirve (es del tema 4))
       Para esperar tenemos que hacer un acquire de espera
    2. Mutex.acquire -> igual que en devuelvoImpresora
    3. Decrementar el número de impresoras
    4. Espera.release()
    5.Mutex.release -> igual que en devuelvoImpresora
     */
    espera.acquire() //1
    mutex.acquire() //2
    numImp -= 1 //3
    if (numImp > 0) espera.release() //4
    log(s"Usuario $id coge una impresora. Hay: $numImp")
    mutex.release() //
  }

  def devuelvoImpresora(id:Int) ={ //Id del usuario que la devuelve
    //Suponemos que ya a un proceso se le ha dado la impresora y lo está devolviendo
    /*
    Para devolver una impresora:
    1. Mutex.acquire -> para proteger la variable (dos procesos no puedan acceder al semáforo a la vez)
    2. Aumenta en 1 el número de impresoras libres
    3. Si no hay impresoras y devuelvo la primera tengo que esperar a que esté abierto
    5. Mutex.release -> liberar el semáforo para que otro pueda acceder
    */
    mutex.acquire() //1
    numImp +=1 //2
    //4
    if (numImp ==1) espera.release() //3
    log(s"Usuario $id devuelve una impresora. Hay: $numImp")
    mutex.release() //5
  }

  def main(args: Array[String])= {
    //Estructura usuario (la suelen dar en los exámenes)
    val usuario = new Array[Thread](10) //10 usuarios para nuestro ejemplo
    for (i <- 0 until usuario.length) {
      usuario(i) = thread {
        for (j <- 0 until 3) {
          quieroImpresora(i)
          Thread.sleep(Random.nextInt(200))
          devuelvoImpresora(i)
        }
      }
    }
  }
//CS == Condición del usuario -> Usuario i espera hasta que haya impresoras libres

}