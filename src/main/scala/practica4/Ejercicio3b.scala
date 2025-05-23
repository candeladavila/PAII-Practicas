package ejercicios

import scala.util.Random


object Ejercicio3b {
  /*
  def todosTrue(l:List[Boolean]):Boolean = l match
    case Nil => true
    case a::r =>
      if (!a) false
      else todosTrue(r)
   */

  def todosTrue(l:List[Boolean]):Boolean = l.filter(!_).isEmpty

  def main(args:Array[String]) = {
    val lista = List.fill(Random.nextInt(10))(Random.nextBoolean())
    log(s"${lista.mkString("[", ",", "]")}")
    val (l1, l2) = lista.splitAt(lista.size / 2)
    val (r1, r2) = parallel(todosTrue(l1), todosTrue(l2))
    log(s"¿Son todos true?: ${if (r1 && r2) "sí" else "no"}")
  }

}
