package esqueletosLaboratorio7

import scala.util.Random

object Guarderia{
  private var nBebe  = 0
  private var nAdulto = 0
  //nBebe<=3*Adulto (3bebes,1adulto),(4bebes,2adulto)
  

  def entraBebe(id:Int) =  {
   
    log(s"Ha llegado un bebé. Bebés=$nBebe, Adultos=$nAdulto")
  }
  def saleBebe(id:Int) =  {
    
    log(s"Ha salido un bebé. Bebés=$nBebe, Adultos=$nAdulto")
  
  }
  def entraAdulto(id:Int) =  {
    
    log(s"Ha llegado un adulto. Bebés=$nBebe, Adultos=$nAdulto")
    
  }
  def saleAdulto(id:Int) =  {
    
    log(s"Ha salido un adulto. Bebés=$nBebe, Adultos=$nAdulto")
  }
}
object Ejercicio7 {
  def main(args:Array[String]):Unit={
    val NB = 15
    val NA = 5
    val bebe = new Array[Thread](NB)
    val adulto = new Array[Thread](NA)
    for (i <- bebe.indices)
      bebe(i) = thread {
        while (true) {
          Thread.sleep(Random.nextInt(700))
          Guarderia.entraBebe(i)
          Thread.sleep(Random.nextInt(500))
          Guarderia.saleBebe(i)
        }
      }
    for (i <- adulto.indices)
      adulto(i) = thread {
        while (true) {
          Thread.sleep(Random.nextInt(700))
          Guarderia.entraAdulto(i)
          Thread.sleep(Random.nextInt(500))
          Guarderia.saleAdulto(i)
        }
      }
  }

}
