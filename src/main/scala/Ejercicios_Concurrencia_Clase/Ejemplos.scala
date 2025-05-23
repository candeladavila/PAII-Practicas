package Ejercicios_Concurrencia_Clase

var turno = 0 //Variable compartida para saber a quién le va tocando
var iter = 0

class Hebra(id:Int, t:Int, c:Char) extends Thread{
  //Ejercicio 1
  override def run() =
    for (i <- 0 until t)
      //tenemos que añadir la espera para esperar al turno
      while (turno != id) Thread.sleep(0) //espera activa -> tiempo 0 para provocar un cambio de contexto

      print(c)
      iter += 1
      if (iter == id +1)
        turno = (turno +1)%3
        iter = 0
      /*
        turno = (turno + 1)%3 -> con esto pasamos el turno de uno a otro ABCABCABC...
       */
}

object Ejemplos {
  def main(args:Array[String])=
    // -----EJERCICIO 1-----
    //Apartado A
    val hebra0 = new Hebra(0, 10,'A')
    val hebra1 = new Hebra(1, 20, 'B')
    val hebra2 = new Hebra(2, 30, 'C')

    //Iniciamos las hebras
    hebra0.start()
    hebra1.start()
    hebra2.start()

    //Apartado B
    //Primero: añadimos un identificador para saber que hebra es
}
