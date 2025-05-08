package practica4

import scala.util.Random

def tTrue(l:List[Boolean]):Boolean =
  l match
    case Nil => true
    case true::r => tTrue(r)
    case _ => false

object todosTrue {
  def main(str:Array[String]) =
    val lista = List.fill(5)(Random.nextBoolean())
    val (l1,l2) = lista.splitAt(lista.size/2)
    val (b1, b2) = paralleel(tTrue(l1), //Comprobar la primera parte de la lista sea True
                             tTrue(l2)) //Comprobar la segunda parte de la lista sea True

    println(lista)
    println(s"${b1 && b2}")
}