package Laboratorio5

object Lago {
  private var nivel = 0
  @ volatile private var fr = false //rio = 0
  @ volatile private var fp = false //presa = 1
  @ volatile private var turnopr = 0

  @volatile private var fp0 = false //rio = 0
  @volatile private var fp1 = false //presa = 1
  @volatile private var turnop = 0

  def inc() =
    fr = true
    turnopr = 1
    while (fp && turnopr == 1) Thread.sleep(0)
    nivel += 1
    log(s"Nivel = $nivel")
    fr = false

  def dec0() =

    fp0 = true
    turnop = 1
    while (fp1 && turnop==1) Thread.sleep(0)

    while (nivel == 0) Thread.sleep(0)
    fp = true
    turnopr = 0
    while (fr && turnopr==0) Thread.sleep(0)
    nivel -= 1
    log(s"Nivel = $nivel")
    fp = false
    fp0 = false

  def dec1() =
    fp1 = true
    turnop = 0
    while (fp0 && turnop == 0) Thread.sleep(0)

    while (nivel == 0) Thread.sleep(0)
    fp = true
    turnopr = 0
    while (fr && turnopr == 0) Thread.sleep(0)
    nivel -= 1
    log(s"Nivel = $nivel")
    fp = false
    fp1 = false

  def nivelLago = nivel
}

object Principal1{
  def main(args:Array[String]) =
    val rio = thread{
      for (i<-0 until 2000)
        Lago.inc()
    }
    val presa0 = thread{
      for (i <- 0 until 1000)
        Lago.dec0()
    }
    val presa1 = thread {
      for (i <- 0 until 1000)
        Lago.dec1()
    }
    rio.join()
    presa0.join()
    presa1.join()
    log(s"Nivel del lago = ${Lago.nivelLago}")
}