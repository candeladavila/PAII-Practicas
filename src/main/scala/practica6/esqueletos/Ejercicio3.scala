package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random
object aseo{
  // CS-Cliente: Esperan si está el Equipo de Limpieza en el aseo
  // CS-EquipoLimpieza: Espera si hay clientes en el aseo

  // ...
  var numClientes = 0

  def entraCliente(id:Int)={
    // ...
    log(s"Entra cliente $id. Hay $numClientes clientes.")
    // ...
  }
  def saleCliente(id:Int)={
    // ...
    log(s"Sale cliente $id. Hay $numClientes clientes.")
    // ...
  }
  def entraEquipoLimpieza ={
    // ...
    log(s"        Entra el equipo de limpieza.")
    // ...
  }
  def saleEquipoLimpieza = {
    // ...
    log(s"        Sale el equipo de limpieza.")
    // ...
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
