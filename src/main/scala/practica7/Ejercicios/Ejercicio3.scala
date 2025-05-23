package esqueletosLaboratorio7

import scala.util.Random

object Parejas{
  

  def llegaHombre(id:Int) = {

    log(s"Hombre $id quiere formar pareja")

  }

  def llegaMujer(id: Int) =  {
   
    log(s"Mujer $id quiere formar pareja")
  }
}
object Ejercicio3 {

  def main(args:Array[String]):Unit = {
    val NP = 10
    val mujer = new Array[Thread](NP)
    val hombre = new Array[Thread](NP)
    for (i<-mujer.indices)
      mujer(i) = thread{
        Parejas.llegaMujer(i)
      }
    for (i <- hombre.indices)
      hombre(i) = thread {
        Parejas.llegaHombre(i)
      }
  }

}
