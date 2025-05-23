package object ejercicios {

  def thread(body: =>Unit):Thread={
    val t = new Thread{
      override def run = body
    }
    t.start
    t
  }

  def log(msg:String) =
    println(s"${Thread.currentThread().getName}: $msg")

  //Ejercicio 2a
  def periodico(t:Long)(b: =>Unit):Thread =
    thread{
      while (true)
        b
        Thread.sleep(t)
    }
  //Ejercicio 3a
  def parallel[A,B](a: =>A,b: =>B):(A,B) = {
    var va:A=null.asInstanceOf[A]
    var vb:B=null.asInstanceOf[B]
    val hA = thread{va = a}
    val hB = thread{vb = b}
    hA.join()
    hB.join()
    (va,vb)
  }


}
