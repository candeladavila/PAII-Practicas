package Lab61.soluciones

import java.util.concurrent.*
import scala.util.Random

class Cadena(n: Int) {
  /*
  Descripción del problema:

  Vamos a representar los paquetes de cada tipo en una única lista: (nº tipo 0, nº tipo º, nº tipo 2)

  Empaquetadores especializados
  - tipo 0
  - tipo 1
  - tipo 2
  Están constantemente consultando si hay paquetes de su tipo (valor mayor que 0 de la casilla de la lista), si los hay los empaqueta e imprime un mensaje

  El recurso compartido es la lista con los paquetes y, por tanto, el acceso a la lista se tiene que hacer con un protocolo de excluisión mútua (mutex)

  El colocador se bloquea cuando no hay hueco en el buffer -> acquire de semáforo hayEspacio
    - inicializado a 1 (para que el colocador pueda colocar)
    - desbloqueamos cuando un empaquetador (cualquiera) coja un paquete

  El semáforo de los empaquedores:
    - inicalizados a 1 (bloqueados porque está vacío el almacén)
    - desbloqueados cuando haya elementos de su tipo en la lista
   */
  //n = número máximo de paquetes
  // CS-empaquetador-i: espera hasta que hay productos de tipo i
  // CS-colocador: espera si hay n productos en la cadena
  private val tipo = Array.fill(3)(0) // el buffer de tipos de paquetes
  private var cuentaTotal = 0
  private val mutex = new Semaphore(1) //Sobre todos los enteros
  private val esperaCol = Semaphore(1) // CS- Colocador
  private val esperaEmp = new Array[Semaphore](3) //Empaquetadores
    for (i <- 0 until esperaEmp.length)
      esperaEmp(i) = new Semaphore(0) //cremos un semáforo por cada empaquetador

  //Si es 0 y hago acquiere se bloquea

  def retirarProducto(p: Int) = { //p es el tipo
    /*
      El empaquetador coge un paquete
     */
    esperaEmp(p).acquire()
    mutex.acquire()
    tipo(p) -=1
    log(s"Empaquetador $p retira un producto. Quedan ${tipo.mkString("[",",","]")}")
    if (tipo(p) > 0) esperaEmp(p).release() //Cuando el colocador coloca un producto si sigue habiendo productos de ese tipo hay que hacer un release
      //me aviso a mí mismo de que puedo seguir cogiendo paquetes, quedan de ese tipo
    if (tipo.sum == n-1) esperaCol.release() //desbloquea al colocador porque queda un sitio
    mutex.release()
  }
  def nuevoProducto(p:Int) = { //p es el tipo
    /*
    El colocador coloca un nuevo paquete
     */
    esperaCol.acquire()
    mutex.acquire()
    tipo(p)+=1
    cuentaTotal +=1
    log(s"Colocador pone un producto $p. Quedan ${tipo.mkString("[",",","]")}")
    log(s"Total de productos empaquetados $cuentaTotal")
    if (tipo(p) == 1) esperaEmp(p).release() //añado un elemento de tipo p cuando antes había 0 -> desbloqueo al empaquetador de ese tipo
    if (tipo.sum < n) esperaCol.release() //queda espacio libre en el contador
    mutex.release()
  }
}

object Ejercicio2 {
  def main(args:Array[String]) = {
    val cadena = new Cadena(6)
    val empaquetador = new Array[Thread](3)
    for (i <- 0 until empaquetador.length)
      empaquetador(i) = thread {
        while (true)
          cadena.retirarProducto(i)
          Thread.sleep(Random.nextInt(500)) // empaquetando
      }

    val colocador = thread {
      while (true)
        Thread.sleep(Random.nextInt(100)) // recogiendo el producto
        cadena.nuevoProducto(Random.nextInt(3))
    }
  }
}
