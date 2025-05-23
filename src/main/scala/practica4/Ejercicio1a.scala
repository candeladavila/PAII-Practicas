package ejercicios
class Hebra(c:Char,t:Int) extends Thread{
  override def run = {
    for (i<-0 until t)
      print(c)
  }
}
object Ejercicio1a {

  def main(args:Array[String]) = {
      val hA = new Hebra('A',10)
      val hB = new Hebra('B',20)
      val hC = new Hebra('C',30)
      hA.start()
      hB.start()
      hC.start()
  }

}
