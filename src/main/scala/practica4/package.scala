package object practica4 {
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run(): Unit = { body }
    }
    t.start()
    t
  }

  //EJERCICIO 3
  /* 
  Queremos que cada hebra se encargue de comprobar la mitad del array
   */
  def paralleel[A,B](a: =>A, b: => B):(A,B) =
    var va = null.asInstanceOf[A] //Iniciar una variable de un tipo abstracto a null
    var vb = null.asInstanceOf[B]
    val ha = thread{
      va = a
    }
    val hb = thread {
      vb = b
    }
    ha.join()
    hb.join()
    (va, vb)

  def log(str: String) = {
    println(s"${Thread.currentThread().getName}:$str")
  }

}

