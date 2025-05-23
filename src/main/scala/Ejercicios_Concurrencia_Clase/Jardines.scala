package Ejercicios_Concurrencia_Clase

import java.util.concurrent.Semaphore

object cont { //recurso compartido
  private var n = 0
  private var mutex = new Semaphore(1)
  def inc() = {
    mutex.acquire()
    n += 1
    mutex.release()
  }
  def valor = n
}

object Jardines {
  def main(args:Array[String]) =
    val N = 5
    val p = new Array[Thread](N)
    for (i <- 0 until N)
      p(i) = thread(
        for (i <- 0 until 10)
          cont.inc()
      )
      //si ponemos el siguiente for aquí entonces sería una ejecución secuencial y no se ejecutarían las hebras a la vez
    for (i <- 0 until N)
      p(i).join()

   /*
    val p1 = thread{
      for (i<- 0 until 10)
        cont.inc()
    }

    val p2 = thread {
      for (i <- 0 until 10)
        cont.inc()
    }

    p1.join
    p2.join
    */

    log(s"${cont.valor}")
}
