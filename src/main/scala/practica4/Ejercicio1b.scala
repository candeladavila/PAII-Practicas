package ejercicios

var turno:Int = 0

class Hebra1b(c:Char,t:Int,miId:Int) extends Thread{
  override def run = {
    var n = 0
    for (i<-0 until t)
      while (turno!=miId) Thread.sleep(0)
      print(c)
      n +=1
      if (n==miId+1)
        n=0
        turno = (turno+1)%3

  }
}
object Ejercicio1b {

  def main(args:Array[String]) = {
    val hA = new Hebra1b('A',10,0)
    val hB = new Hebra1b('B',20,1)
    val hC = new Hebra1b('C',30,2)
    hA.start()
    hB.start()
    hC.start()
  }

}
