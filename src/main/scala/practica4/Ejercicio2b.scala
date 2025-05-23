package ejercicios


object Ejercicio2b {

  def main(args:Array[String]) = {
    val h1 = periodico(1000)(log("soy hebra periódica 1"))
    val h2 = periodico(3000)(log("soy hebra periódica 2"))
  }
}
