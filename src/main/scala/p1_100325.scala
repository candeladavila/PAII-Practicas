import scala.annotation.tailrec
import scala.collection.immutable.{AbstractSet, SortedSet}

object P1_100325 {
  def main(args: Array[String]): Unit = {
    println("Práctica 1: Temas 1 y 2 (Introducción a Scala)")

    //Comprobación Ejercicio 1
    println("Ejercicio 1: Divisores de un número")
    println(s" Ejemplo 1 (Divisores de 60):  ${primeFactors(60)}")
    println(s" Ejemplo 2 (Divisores de 97):  ${primeFactors(97)}")
    println(s" Ejemplo 3 (Divisores de 84):  ${primeFactors(84)}")

    //Comprobación Ejercicio 2
    println("Ejercicio 2: Búsqueda de un elemento en un array")
    val arr = Array(1,3,5,7,9,11)
    println(s" Array para ejemplos: ${arr.mkString(",")}")
    println(s" Ejemplo 1 (Búsqueda binaria 5):  ${binarySearch(arr, 5)}")
    println(s" Ejemplo 2 (Búsqueda binaria de 10):  ${binarySearch(arr, 10)}")

    //Comprobación Ejercicio 3
    println("Ejercicio 3: Unzip")
    val lista = List((10, 'a'), (20, 'b'), (30, 'c'))
    println(s" Lista para ejemplos: ${lista}")
    println(s" Ejemplo 1 (Unzip (int, char):  ${unzip(lista)}")

    //Comprobación Generalización Ejercicio 3
    println("Ejercicio Generalización Ejercicio 3: Unzip genérico")
    println(s" Ejemplo 1(Unzip:  ${unzipGen(lista)}")

    //Comprobación Ejercicio 4
    println("Ejercicio 4: Agrupación de dos listas")
    val l1 = List(10,20,30)
    val l2 = List('a','b','c')
    println(s" Listas para ejemplos: l1 = ${l1}, l2 = ${l2} ")
    println(s" Ejemplo 1(zip: ${zip(l1,l2)}")

    //Comprobación Ejercicio 5
    println("Ejercicio 5: Filtrar una lista")
    val lista2 = List(1,2,3,4,5)
    println(s" Lista para ejemplos: ${lista2}")
    println(s" Ejemplo 1(filtro pares): ${filter(lista2, _%2==0)}")

    //Comprobación Ejercicio 6
    println("Ejercicio 6: Mapa con condición")
    println(s" Lista para ejemplos: ${lista2}")
    println(s" Ejemplo 1(map pares): ${map(lista2, _*2)}")

    //Comprobación Ejercicio 7
    println("Ejercicio 7: Agrupación según una condición")
    println(s" Lista para ejemplos: ${lista2}")
    println(s" Ejemplo (agrupación pares): ${groupBy(lista2, _ % 2 == 0)}")

    //Comprobación Ejercicio 8
    println("Ejercicio 8: Reducir una lista con una operación")
    println(s" Lista para ejemplos: ${lista2}")
    println(s" Ejemplo (suma de elementos de un array): ${reduce(List(1,2,3,4,5), _ + _)}")

    //Comprobación Ejercicio 9
    println("Ejercicio 9: subgrupos de un Set")
    println(s" Ejemplo 1: ${subsets(Set())}")
    println(s" Ejemplo 2: ${subsets(Set(1))}")
    println(s" Ejemplo 3: ${subsets(Set(1,2))}")
    println(s" Ejemplo 4: ${subsets(Set(1, 2, 3))}")

    //Comprobación Ejercicio 10
    println("Ejercicio 10: combinaciones de paréntesis")
    println(s" Ejemplo: ${generateParentheses(3)}")
  }

  /*
  EJERCICIO 1
  Escribe una función recursiva de cola primeFactors(n: Int): List[Int] que devuelva una
  lista con los factores primos de un entero positivo dado n.
  */

  def primeFactors(n:Int) :List[Int] =
    def primeFactorsAux(n:Int, i:Int):List[Int] =
      if (i * i > n) List(n)
      else if (n% i == 0) i :: primeFactorsAux(n/i, i)
      else primeFactorsAux (n, i+1)
    primeFactorsAux(n, 2)

  /*
  EJERCICIO 2
  Escribe una función recursiva de cola binarySearch(arr: Array[Int], elt: Int):
  Option[Int] que devuelva el índice de elt (Some(i)) en un array ordenado utilizando el
  algoritmo de búsqueda binaria, o None en caso de que el elemento no se esté.
   */

  def binarySearch(arr: Array[Int], elt: Int) : Option[Int] =
    def binarySearchAux(arr: Array[Int], elt:Int, low: Int, high: Int) : Option[Int] =
      if (low > high) None
      else
        val mid = low + (high-low)/2
        arr(mid) match {
          case x if x == elt => Some(mid)
          case x if x < elt => binarySearchAux(arr, elt, mid + 1, high)
          case _ => binarySearchAux(arr, elt, low, mid - 1)
        }
    binarySearchAux(arr, elt, 0, arr.length)


  /*
  EJERCICIO 3
  Define una función recursiva genérica unzip que tome una lista de tuplas con dos componentes y
  que devuelva una tupla con dos listas: una con las primeras componentes y otra con las segundas.
  */
  def unzip(lst: List[(Int, Char)]): (List[Int], List[Char]) =
    def bucle(lista: List[(Int, Char)], c1: List[Int], c2: List[Char]) : (List[Int], List[Char]) =
      lista.match{
        case Nil => (c1.reverse, c2.reverse) //ponemos reverse para invertir las listas (porque añadimos por head)
        //cogemos la primera tupla de la lista -> el resto lo guardamos en tail y lo usamos para la llamada recursiva
        case(a,b)::tail => bucle(tail, a::c1, b::c2)
      }
    bucle(lst, Nil, Nil)


  //GENERALIZACIÓN DE TIPOS
  def unzipGen[A, B](lst: List[(A, B)]): (List[A], List[B]) =
    def bucle[A,B](lista: List[(A, B)], c1: List[A], c2: List[B]): (List[A], List[B]) =
      lista.match {
        case Nil => (c1.reverse, c2.reverse)
        //cogemos la primera tupla de la lista -> el resto lo guardamos en tail y lo usamos para la llamada recursiva
        case (a, b) :: tail => bucle(tail, a :: c1, b :: c2)
      }
    bucle(lst, Nil, Nil)

  /*
  SOLUCIÓN DE CLASE
  def unzipGen[A, B](lst: List[(A, B)]): (List[A], List[B]) =
  def unzipAux[A,B](lst:List[(A,B)], acc1: List[A], acc2: List[B]): (List[A],List[B]) = {
    lst match{
      case Nil => (acc1.reverse, acc2, reverse)
      case (a,b)::tail => unzip(tail, a::acc1, b::acc2)
    }
   */

  /*
  EJERCICIO 4
  Define una función recursiva genérica zip que tome dos listas y devuelva una lista de tuplas, donde
  las primeras componentes se tomen de la primera lista y las segundas componentes de la segunda
  lista.
  */
  def zip[A, B](l1 : List[A], l2: List[B]) : List[(A,B)] =
    def zipAux[A,B](lst: List[(A,B)], lA: List[A], lB: List[B]): List[(A,B)] =
      (lA, lB) match{
        case (Nil,_) | (_,Nil) => lst.reverse
        case (headA :: tailA, headB :: tailB) => zipAux((headA, headB) :: lst, tailA, tailB)
      }
    zipAux(List(), l1, l2)

  /*
  EJERCICIO 5
  Implementa una operación filter(l, f) que tome una lista l de elementos de tipo A y una función f: A
  => Boolean y que devuelva una lista con los elementos e de l que satisfacen f(e).
  */

  //HACER RECURSIVO DE COLA -> REVISAR
  def filter[A](l: List[A], f: A => Boolean): List[A] =
    l match {
      case Nil => Nil // Caso base: lista vacía
      case head :: tail if f(head) => head :: filter(tail, f) // Incluye head si cumple f
      case _ :: tail => filter(tail, f) // Omite head si no cumple f

      /*
      Otra opción es unir los dos últimos cases de la forma:
      case head::tail => if (f(head)) head::filter(tail, f) else filter(tail, f)
       */
    }

  /*
  EJERCICIO 6
  Implementa una operación map(l, f) que tome como argumentos una lista l de elementos de tipo
  A y una función f: A => B y que devuelva una lista de elementos de tipo B con los elementos
  resultantes de aplicar f a cada uno de los elementos de l.
  */
  def map[A,B](l:List[A], f:A => B) : List[B] =
    l match{
      case Nil => Nil //lista vacía
      case head :: tail => f(head) :: map(tail, f) //llamada recursiva al método
    }

  /*
  EJERCICIO 7
  Implementa una operación groupBy(l, f) que tome como argumentos una lista l de elementos de
  tipo A y una función f: A => B y que devuelva un objeto de tipo Map[B, List[A]] que asocie una lista
  con los elementos e de l con el mismo f(e).
  */
  def groupBy[A, B](l: List[A], f: A => B): Map[B, List[A]] =
    def bucle(remaining: List[A], res: Map[B, List[A]]): Map[B, List[A]] =
      remaining match {
      case Nil => res // lista vacía -> devolvemos map
      case head :: tail =>
        val clave = f(head) // f(elemento)
        // Actualizamos el mapa con la nueva clave y el elemento correspondiente
        val newMap = res + (clave -> (head :: res.getOrElse(clave, Nil)))
        bucle(tail, newMap) // Llamada recursiva con el resto de la lista y el mapa actualizado
      }
    bucle(l, Map()) // Llamamos a la función auxiliar con la lista inicial y un mapa vacío

  /*
  EJERCICIO 8
  Implementa una operación reduce(l, f) que toma como argumentos una lista "l" de elementos de tipo
  A y una función "f" de tipo (A, A) => A y que devuelva el resultado de combinar todos los elementos
  de "l" utilizando la función f.
  */
  def reduce[A](l:List[A], f:(A,A) => A) : A =
    l match{
      case Nil => throw new UnsupportedOperationException("La lista está vacía")
      case head::tail => tail.foldLeft(head)(f)
    }

  /*
  EJERCICIO 9
  Implementa una función recursiva para generar todos los subconjuntos de un conjunto
  determinado. Conviértela en recursiva de cola.
  */
  def subsets[A](s: Set[A]) : Set[Set[A]] =
    def bucle[A](remaining: Set[A], acc: Set[Set[A]]): Set[Set[A]] =
      if (remaining.isEmpty) acc
      else
        val elem = remaining.head
        bucle(remaining.tail, acc ++ acc.map(_ + elem))
        //acc.map(_+elem) = coge todos los elementos de acc y crea nuevos subconjuntos con elem
        //acc ++ acc.map(_+elem) = une los subconjuntos nuevos con los que ya estaban en acc
    bucle(s, Set(Set()))

  /*
  EJERCICIO 10
  Escribe una función recursiva de cola generateParentheses(n: Int): List[String] que
  genere todas las combinaciones válidas de n pares de paréntesis.

  Consejos:
  • Utiliza un acumulador para almacenar secuencias válidas.
  • Haz un seguimiento del número de paréntesis de apertura (open) y cierre (closed) utilizados.
  • Caso base: Cuando open == closed == n, agrega la secuencia al resultado.
  */
  def generateParentheses(n: Int): List[String] =
    def bucle(open: Int, close: Int, current: String, acc: List[String]): List[String] =
      if (open == 0 && close == 0) current :: acc //si hay igual nu¡úmero de "(" que ")" entonces es una combinación válida
      else
        val addOpen = if (open > 0) bucle(open - 1, close, current + "(", acc) else acc
        //añadimos una apertura si siguen quedando aperturas por poner (hasta n)
        val addClose = if (close > open) bucle(open, close - 1, current + ")", addOpen) else addOpen
        //añadimos un cierre en caso de que haya más que apertura (el resto de casos "(")
        addClose
    bucle(n, n, "", List())

}