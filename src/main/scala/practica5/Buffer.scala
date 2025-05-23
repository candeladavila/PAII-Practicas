package Laboratorio5
import scala.util.Random
object Buffer {
  private val N = 5
  private val buffer = new Array[Int](N)
  private var i = 0 // indice productor
  private var j = 0 // indice consumidor
  private var numElems = 0

  @volatile private var fp = false
  @volatile private var fc = false
  @volatile private var turno = 0 //productor 0, consumidor 1

  def almacena(dato:Int) =
    while (numElems == N) Thread.sleep(0)
    fp = true
    turno = 1
    while (fc && turno==1) Thread.sleep(0)
    buffer(i) = dato
    i = (i+1) % N
    numElems += 1
    fp = false

  def extrae() : Int =
    while (numElems == 0) Thread.sleep(0)
    fc = true
    turno = 0
    while (fp && turno == 0) Thread.sleep(0)
    val dato = buffer(j)
    j = (j + 1) % N
    numElems -= 1
    fc = false
    dato
}

object Principal2{
  def main(args:Array[String]) =
    val prod = thread{
      for (i<-0 until 100)
        Thread.sleep(Random.nextInt(500))
        Buffer.almacena(i)
    }

    val cons = thread{
      for (i <- 0 until 100)
        val dato = Buffer.extrae()
        log(s"Consumidor lee $dato")
        Thread.sleep(Random.nextInt(500))
    }
}