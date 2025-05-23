package ejercicios

import scala.util.Random


def mezclar(l1: List[Int], l2: List[Int]): List[Int] = (l1, l2) match
  case (Nil, _) => l2
  case (_, Nil) => l1
  case (a1 :: r1, a2 :: r2) =>
    if (a1 <= a2) a1 :: mezclar(r1, l2)
    else a2 :: mezclar(l1, r2)

def ordenar(l: List[Int]): List[Int] =
  //log(s"${l.mkString("[", ",", "]")}")
  if (l.size <= 1) l
  else {
    val (l1, l2) = l.splitAt(l.size / 2)
    val (l1o, l2o) = parallel[List[Int], List[Int]](ordenar(l1), ordenar(l2))
    mezclar(l1o, l2o)
  }

object Ejercicio5 {
  def main(args:Array[String]) = {
    val lista = List.fill(Random.nextInt(50))(Random.nextInt(100))
    log(s"${lista.mkString("[", ",", "]")}")
    val listaOrd = ordenar(lista)
    log(s"${listaOrd.mkString("[", ",", "]")}")
  }
}
