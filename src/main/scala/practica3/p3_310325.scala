package practica3

import scala.annotation.tailrec

object P3_310325 {
  def main(args: Array[String]): Unit = {
    /*
    FUNCIONES FOLDRIGHT y FOLDLEFT (l = lista) (por defecto)

    FOLDRIGHT (Copia en el mismo orden, empieza por el final de la lista)
    def foldRight[B] (acumulador: B)(f: (A, B) => B): B =
      l match
      case Nil => acc
      case a :: r => f(a, r.foldRight(acumulador)(f))

    FOLDLEFT (Copia en orden inverso, empieza por el principio de la lista)
    def foldLeft[B](acumulador: B)(f: (B, A) => B): B =
      l match
      case Nil => acc
      case a :: r => r.foldLeft(f(acumulador, a))(f)
    */

    //EJERCICIO 1
    def sum(l: List[Int]): Int =
      l.foldLeft(0)(_ + _) //empezamos en 0 la suma

    def product(l: List[Int]): Int =
      l.foldLeft(1)(_ * _) //empezamos en 1 el producto

    def length[A](l: List[A]): Int =
      l.foldRight(0)((_, acc) => acc + 1)

    //EJERCICIO 2
    def reverse[A](l: List[A]): List[A] =
      l.foldLeft(List.empty[A])((acc, elem) => elem :: acc)

    def append[A](l1: List[A], l2: List[A]): List[A] = //Concatenar listas
      l1.foldRight(l2)(_ :: _)

    //EJERCICIO 3
    def existe[A](l:List[A],f:A=>Boolean):Boolean =
      l.foldLeft(false)((acc, elem) => acc || f(elem))
      /*
      Empezamos inicialmente con false
       */

    //EJERCICIO 4
    //Versión 1: función recursiva de cola con pattern matching
    def f_ver1(l:List[Int]):List[Int] =
      @tailrec
      def bucle(lista: List[Int], acc: List[Int]): List[Int]=
        lista match
          case Nil => acc
          case head::tail => if (head < 0) bucle(tail, (head*(-1)::acc)) else bucle(tail, acc)
      bucle(l, Nil).reverse

    //Versión 2: usando funciones unicamente de orden superior
    def f_ver2(l:List[Int]):List[Int] =
      l.filter(_<0).map(math.abs)

    //EJERCICIO 5
    //Empezamos construyendo una función que unzipee dos tuplas
    def f[A,B](x:(A,B), y:(A,B))=
      (List(x._1, y._1), List(x._2,y._2))
    val l = List((1,'a'),(2,'b'),(3,'c'))
    f(l(0),l(1))

    def unzip1[A,B](l:List[(A,B)]):(List[A],List[B])=
      l.foldRight[(List[A], List[B])]((List(),List()))
        ((x,y)=>(x._1::y._1,x._2::y._2))

    //EJERCICIO 6 (usando FoldRight)
    def compose[A](lf: List[A => A], v: A): A =
      lf.foldRight(v)((elem, acc) => elem(acc))

    //EJERCICIO EXTRA 1: Dada una lista hacer una copia de la lista solo con los elementos pares
    def pares(lista:List[Int]):List[Int]=
      lista.foldRight[List[Int]](List())((x,y)=> if (x%2==0) x::y else y)

    //EJERCICIO 7
    /*
    Información:
      - Acumulador: Lista vacía
      - Función: combinamos elementos cone l acumulador para modificar la lista
          Mi función lo que hará es comparar el elemento que vamos a insertar con la cabeza del acumulador
     */
    def remdups[A](l:List[A]):List[A] =
      l.foldRight[List[A]](Nil)(
        (elem,acc) => if (acc.isEmpty || acc.head != elem)elem::acc else acc)

    //EJERCICIO 8 (usando foldRight)
    def fibonnaci(n:Int):Int =
      (0 until n).toList.foldRight((1, 0)) { case (_, (a, b)) => (b, a + b)}._2

    //EJERCICIO 9
    def inits[A](l: List[A]): List[List[A]] =
      l.foldRight(List(List.empty[A])) { (elem, acc) =>List.empty[A] :: acc.map(elem :: _)}
    /*
    1. Parámetro inicial = Lista vacía de Listas de elementos tipo A
    2. Por cada elemento de la lista:
        1. Concatenamos todas las listas que tenemos en el acumulado con el nuevo elemento al principio
        2. Concatenamos todas las listas que hemos generado con la vacía al principio
     NOTA: Usamos List.empty porque queremos especificar que sea de tipo A y si no Scala pondrá el tipo por contexto
     */

    //EJERCICIO 10
    //Versión 1: Recursiva de cola y pattern matching
    def halfEven_ver1(l1:List[Int],l2:List[Int]):List[Int] =
      @tailrec
      def bucle(lista1:List[Int], lista2:List[Int], acc:List[Int]):List[Int] =
        (lista1,lista2) match
          case (Nil,_)|(_, Nil) => acc.reverse
          case (head1::tail1, head2::tail2) =>
            val sum= head1+head2
            if (sum%2 == 0) bucle(tail1, tail2, (sum/2)::acc)
            else bucle(tail1, tail2, acc)
      bucle(l1, l2, Nil)

    //Versión 2: Funciones de orden superior
    def halfEven_ver2(l1:List[Int],l2:List[Int]):List[Int] =
      l1.zip(l2).collect{
        case (a,b)
          if (a+b)%2 == 0 => (a+b)/2
      }

    //EJERCICIO 11
    val logs = List(
      "ERROR: Null pointer exception",
      "INFO: User logged in",
      "ERROR: Out of memory",
      "WARNING: Disk space low",
      "INFO: File uploaded",
      "ERROR: Database connection failed"
    )
    //Devolver una lista con parejas: (tipo de dato, nº)
    val logCounts = logs.map(_.split(":")(0)).groupBy(identity).map{case (k -> v) => (k -> v.size)}
    /*
    .map = Extract log type
    identity = es lo mismo que hacer x -> x
    .groupBy -> genera un mapa donde las claves son los distintos elementos de la lista (unicos) y el valor va a ser una lista de
      elementos que coinciden con la clave
    .map{case(k -> v) => (k -> v.size)} -> lo que hacemos es separar clave valor para cambiar los tipos de datos del mapa
      (cadena, lista) a (cadena, numero), donde número es el resultado de obtener la longitud de la lista que estaba asociada antes
    */
    //Devolver una lista con los mensajes de error
    val errormessages = logs.filter(_.startsWith("ERROR"))

    //GENERALIZACIÓN DEL EJERCICIO CON UNA FUNCIÓN
    def logs_list(list:List[String]): Map[String,Int]=
      list.map(_.split(":")(0)).groupBy(identity).map{case (k -> v) => (k -> v.size)}

    //EJERCICIO 12
    val sales = List(
      ("Laptop", 2, 1000.0),
      ("Mouse", 10, 15.0),
      ("Keyboard", 5, 50.0),
      ("Monitor", 3, 200.0),
      ("USB Drive", 20, 5.0)
    )

    def total_sales[A,B,C](transacciones:List[(String,Int,Double)]):Double =
      transacciones.map {case (_, quantity, price) => (quantity * price)}.sum

    def highValueTransactions(transactions: List[(String, Int, Double)]): List[(String, Double)] =
      transactions.map{case (name, quantity, price) => (name, quantity*price)}.filter {
        case(name, total) => total >= 100}.sortBy{case (name, total) => -total}

    //EJERCICIO 13
    val sentences = Set(
      "Scala is a functional language",
      "The power of functional programming is great",
      "Functional programming is elegant"
    )
    val stopWords = Set("a", "the", "is", "of") //Palabras insignificantes que no tenemos en cuenta

    def unique_words(palabras:Set[String], stopWords:Set[String]):Set[String] =
      palabras.flatMap(_.toLowerCase.split("\\W+")).diff(stopWords).filter(_.nonEmpty)

    //EJERCICIO 14
    val words = List("scala", "is", "awesome", "scala", "functional", "scala", "is", "great")
    def word_frequency(words:List[String]):Map[String,Int] =
      words.groupBy(identity).mapValues(_.size).toMap

    //EJERCICIO 15
    val warehouse1 = Map("laptop" -> 5, "mouse" -> 20, "keyboard" -> 10)
    val warehouse2 = Map("laptop" -> 3, "mouse" -> 15, "monitor" -> 8)

    def comb_listas(wh1: Map[String,Int], wh2: Map[String,Int]):Map[String,Int] =
      wh1.foldLeft(wh2){case (acc, (product, quantity)) => acc.updated(product, acc.getOrElse(product,0)+quantity)}
      /*
      Podemos actualizar el acumulado cuando se trata de map usando .update, en nuestro caso como la estructura es
      (product, quantity) el parámetro a actualizar sería la cantidad por lo que en el segundo parámetro primero
      comprobamos si ya estaba antes: getOrElse y después actulizamos la cantidad sumando la actual (+quantity)
       */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //COMPROBACIONES DE EJERCICIOS
    //Comprobación Ejercicio 1
    println("Ejercicio 1: Operaciones con listas")
    val lista1_1 = List(1, 2, 3)
    val lista1_2 = List(1, 3, 5)
    val lista1_3 = List("Hola", " ", "Mundo")
    println(s" Suma lista ${lista1_1}:  ${sum(lista1_1)}")
    println(s" Producto lista ${lista1_2}:  ${product(lista1_2)}")
    println(s" Longitud lista ${lista1_3}:  ${length(lista1_3)}")

    //Comprobación Ejercicio 2
    println("Ejercicio 2: Inversión y concatenación de listas")
    println(s" Inversión lista ${lista1_1}: ${reverse(lista1_1)}")
    println(s" Concatenación lista 1 = ${lista1_1} y lista 2 = ${List(1, 2)}: ${append(lista1_1, List(1, 2))}")

    //Comprobación Ejercicio 3
    println("Ejercicio 3: Elementos de una lista que satisfacen una función f")
    val lista3_1 = List(1,2,3)
    val lista3_2 = List("Hola","Mundo")
    println(s" Ejemplo 1: ${existe(lista3_1,_>2)}") //devuelve true
    println(s" Ejemplo 2: ${existe(lista3_2,_.length>=5)}") //devuelve true
    println(s" Ejemplo 3: ${existe(lista3_2,_.length<3)}") //devuelve false

    //Comprobación Ejercicio 4
    println("Ejercicio 4: Lista con los elementos negativos de una lista")
    val lista4 = List(1,-2,3,-4,-5,6)
    println(s"La lista que vamos a usar va a ser: ${lista4}")
    println(s" Versión 1 (Recursiva de cola):  ${f_ver1(lista4)}")
    println(s" Versión 2 (Funciones de orden superior): ${f_ver2(lista4)}")

    //Comprobación Ejercicio 5
    println("Ejercicio 5: Unzip")
    val lista5_1= List((1,'a'),(2,'b'),(3,'c'))
    println(s"Usamos la lista: ${lista5_1}")
    println(s" Unzip elementos de la lista: ${unzip1(lista5_1)}")

    //Comprobación Ejercicio 6
    println("Ejercicio 6: Concatenación de operaciones")
    val lista6 = List[Int => Int](Math.pow(_, 2).toInt, _ + 2)
    println(s" Las funciones se concatenan bien: ${compose(lista6, 5) == (49)}")

    //Comprobación Ejercicio Extra 1
    println("Ejercicio extra 1: Copiar los elementos pares de una lista")
    val lista_extra = List(1, 2, 3, 4)
    println(s" Elementos lista ${lista_extra}: ${pares(lista_extra)}")

    //Comprobación Ejercicio 7
    println("Ejercicio 7: Copiar una lista sin elementos repetidos seguidos")
    val lista7 = List(1, 1, 3, 3, 3, 2, 1, 2, 2, 1, 2)
    println(assert(remdups(List(1,1,3,3,3,2,1,2,2,1,2)) == List(1, 3, 2, 1, 2, 1, 2), "No funciona"))
    println(s" Lista ${lista7}: ${remdups(lista7)}")

    //Comprobación Ejercicio 8
    println("Ejercicio 8: Fibonnaci usando FoldRight")
    println(s"Fibonnaci de 5: ${fibonnaci(5)}")
    println(s"Fibonnaci de 10: ${fibonnaci(10)}")

    //Comprobación Ejercicio 9
    println("Ejercicio 9: pendiente")
    val lista9_1 = List(1,2,3)
    val lista9_2 = List(3)
    val lista9_3 = List()
    println(s" Ejemplo 1 (lista 1 = ${lista9_1}): ${inits(lista9_1)}")
    println(s" Ejemplo 2 (lista 2 = ${lista9_2}): ${inits(lista9_2)}")
    println(s" Ejemplo 3 (lista 3 = ${lista9_3}): ${inits(lista9_3)}")

    //Comprobación Ejercicio 10
    println("Ejercicio 10: Suma de dos listas (elementos pares)")
    val lista10_1 = List(1,2,3,4)
    val lista10_2 = List(3,2,4)
    println(s" Version 1 (recursiva de cola): ${halfEven_ver1(lista10_1,lista10_2)}")
    println(s" Version 2 (funciones de orden superior): ${halfEven_ver2(lista10_1,lista10_2)}")

    //Comprobación Ejercicio 11
    println("Ejercicio 11: Agrupar datos")
    println(logs)
    println("Apartado 1: agrupar los datos por tipo y devolver el número total")
    println(logCounts)
    println("Apartado 2: extraer los mensajes de error y guardarlos en una lista")
    println(errormessages)

    //Comprobación Ejercicio 12
    println("Ejercicio 12: Transacciones de ventas")
    println(s"Ventas: ${sales}")
    println(s" Ventas totales: ${total_sales(sales)}")
    println(s" Ventas de alto valor: ${highValueTransactions(sales)}")

    //Comprobación Ejercicio 13
    println("Ejercicio 13: Extraer las palabras únicas de unas sentencias (excluyendo palabras no significativas)")
    println(s" Extracción de palabras únicas: ${unique_words(sentences, stopWords)}")

    //Comprobación Ejercicio 14
    println("Ejercicio 14: Contador apariciones de palabras en una lista")
    println(s" Words = ${words}")
    println(s" Solución: ${word_frequency(words)}")

    //Comprobación Ejercicio 15
    println("Ejercicio 15: Combinación de dos listas")
    println(s" Lista 1: ${warehouse1}")
    println(s" Lista 2: ${warehouse2}")
    println(s" Combinación: ${comb_listas(warehouse1, warehouse2)}")
  }
}