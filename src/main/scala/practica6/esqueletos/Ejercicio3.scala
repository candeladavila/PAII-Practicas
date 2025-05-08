package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random
object aseo{
  // CS-Cliente: Esperan si está el Equipo de Limpieza en el aseo
  // CS-EquipoLimpieza: Espera si hay clientes en el aseo

/*
  DESCRIPCIÓN DEL PROBLEMA:
  Tenemos un baño con capacidad infinita en el que:
    - Si hay gente en el baño siempre puede entrar más gente
    - Si en el baño no hay nadie puede entrar el servicio de limpieza
      - Si entra el servicio de limpieza no puede entrar nadie más hasta que salgan
  VARIABLES:
    - numClientes = número de personas que hay en el baño
    - mutex = semáforo para controlar el acceso a la variable numClientes
    - hayLimpieza = semáforo para controlar que no entre más limpieza simultáneamente
    - puerta = semáforo para controlar cuando pueden o no entrar clientes/limpieza

  MÉTODOS:
    - entraCliente
      Cuando entra un cliente:
        - bloqueamos a la limpieza
        - bloqueamos el acceso a la variable
        - aumentamos el número de clientes
          - si es el primero en entrar
            - bloquea la puerta
    - sale cliente
      Cuando sale un cliente:
        - bloqueamos el acceso a la puerta
        - bloqueamos el acceso a la variable
        - reducimos el número de clientes
          - si es el último en salir -> liberamos la puerta
    - entra limpieza
      Cuando entra la limpieza:
        - bloqueamos a la limpieza
        - bloqueamos la puerta
        - bloqueamos mutex
    - sale limpieza
      Cuando sale la limpieza:
        - liberamos la limpieza
        - liberamos la puerta
        - liberamos el mutex
*/

  var numClientes = 0 //Recurso compartido
  val mutex = new Semaphore(1)
  val puerta = new Semaphore(1) //Semáforo para controlar el acceso de los clientes al baño
  val limpieza = new Semaphore(1) //Semáforo para evitar que entren dos equipos de limpieza a la vez

  def entraCliente(id:Int)={
    limpieza.acquire()
    mutex.acquire()
    numClientes += 1
    log(s"Entra cliente $id. Hay $numClientes clientes.")
    if (numClientes == 1) //primer cliente en entrar
      puerta.acquire()
    limpieza.release()
    mutex.release()
  }
  def saleCliente(id:Int)={
    limpieza.acquire()
    mutex.acquire()
    numClientes -= 1
    log(s"Sale cliente $id. Hay $numClientes clientes.")
    if (numClientes == 0) //último cliente en salir
      puerta.release()
    limpieza.release()
    mutex.release()
  }
  def entraEquipoLimpieza ={
    puerta.acquire()
    limpieza.acquire()
    mutex.acquire()
    log(s"        Entra el equipo de limpieza.")
    mutex.release()
  }
  def saleEquipoLimpieza = {
    mutex.acquire()
    log(s"        Sale el equipo de limpieza.")
    puerta.release()
    limpieza.release()
    mutex.release()
  }
}

object Ejercicio3 {
  def main(args: Array[String]) = {
    val cliente = new Array[Thread](10)
    for (i <- 0 until cliente.length)
      cliente(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500))
          aseo.entraCliente(i)
          Thread.sleep(Random.nextInt(50))
          aseo.saleCliente(i)
      }
    val equipoLimpieza = thread {
      while (true)
        Thread.sleep(Random.nextInt(500))
        aseo.entraEquipoLimpieza
        Thread.sleep(Random.nextInt(100))
        aseo.saleEquipoLimpieza
    }
  }
}
