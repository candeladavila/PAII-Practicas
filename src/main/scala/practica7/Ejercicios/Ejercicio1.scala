package esqueletosLaboratorio7

import scala.util.Random

class Buffer(ncons:Int,tam:Int){
  //ncons-número de consumidores
  //tam-tamaño del buffer
  private val buffer = new Array[Int](tam)
  
  def nuevoDato(dato:Int) = {
    //el productor pone un nuevo dato
    
   
    log(s"Productor almacena $dato: buffer=${buffer.mkString("[",",","]")}}")
    
  }

  def extraerDato(id:Int):Int =  {
    
    log(s"Consumidor $id lee : buffer=${buffer.mkString("[",",","]")}")
    0
  }
}
object Ejercicio1 {

  def main(args:Array[String]):Unit = {
    val ncons = 4
    val tam = 3
    val nIter = 10
    val buffer  = new Buffer(ncons,tam)
    val consumidor = new Array[Thread](ncons)
    for (i<-consumidor.indices)
      consumidor(i) = thread{
        for (j<-0 until nIter)
          val dato = buffer.extraerDato(i)
          Thread.sleep(Random.nextInt(200))
      }
    val productor = thread{
      for (i<-0 until nIter)
        Thread.sleep(Random.nextInt(50))
        buffer.nuevoDato(i+1)
    }
  }

}
