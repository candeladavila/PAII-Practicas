package ejercicios


def fibonacci(n:Int):(Int,Int) = {
  require(n>0)
  if (n==1)
    log(s"fib($n) = 1")
    (1,0)
  else
    var p = (0,0)
    val h = thread{
      p = fibonacci(n-1)
    }
    h.join()
    log(s"fib($n) = ${p._2+p._1}")
    (p._2+p._1,p._1)
}

object Ejercicio4 {

  def main(args:Array[String]) =
    val n=7
    log("fib(0) = 0")
    val p = fibonacci(n)
    log("Fin del programa")

}
