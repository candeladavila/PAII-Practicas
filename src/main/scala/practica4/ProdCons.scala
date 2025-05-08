package practica4
import scala.util.Random
import java.util.concurrent._

class Buffer(N:Int) {
  private val buffer = new Array[Int](N) //N = tamaño del buffer
  var b = new Array[Int](N) //lo ponemos de enteros porque el tipo de entrada de datos es irrelevante
  private var i = 0 //Indice producto
  private var j = 0 //Indice consumidor
  private val mutex = new Semaphore(1)


  private val hayEspacio = new Semaphore(N) //CS-Productor -> Inicialmente hay N huecos libres (todos)
  private val hayDatos = new Semaphore(0) //CS-Consumidor -> Inicialmente no hay datos entonces es 0
  /*
  La suma de hayDatos y hayEspacio tiene que ser SIEMPRE igual a N
   */

  def nuevoDato(dato:Int) = {}
    //Para el productor -> dejar un nuevo elemento en el buffer
    //hayDatos.acquire() //Inicialmente no lo va a poder hacer porque no hay datos
    hayEspacio.acquire()
    mutex.acquire()
    buffer(i) = dato
    i = (i+1)%N
    mutex.release()
    hayDatos.release()

  def extraerDato() = 0
    //Para el consumidor -> cuando quiera sacar un elemento del buffer
    hayDatos.acquire()
    mutex.acquire()
    val dato = buffer(j)
    j = (j+1)%N
    mutex.release()
    hayEspacio.release()
    dato
}

/*
CONDICIONES DE SINCRONIZACIÓN
conProductor = el productor espera si el buffer está leno
conConsumidor = el consumidor espera si el buffer está vacío

Lo que vamos a hacer es considerar el buffer como un círculo -> van dando vueltas el consumidor
y productor
 */
object ProdCons{
  def main(array: Array[String]) = {

    val buf = new Buffer(5)
    val prod = thread {
      for (i <- 0 until 50)
        Thread.sleep(Random.nextInt(200)) //tiempo que le lleva al productor construir el dato
        buf.nuevoDato(i) //acceso al buffer para almacenar el dato
    }

    val cons = thread {
      for (i <- 0 until 50)
        val dato = buf.extraerDato()
        Thread.sleep(Random.nextInt(5))
    }
  }
}