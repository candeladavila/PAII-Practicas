package esqueletosLaboratorio7

import scala.util.Random
/*
  Descripción el problema:
    Hay dos tipos de estudiantes: Iphone y Android
    La barca tiene una capacidad 4 (4 asientos) y siempre se tiene que garantizar que el número de cada tipo tiene que ser balanceado
    Configuraciones posibles:
      - 4 Android
      - 4 iPhone
      - 2 Android y 2 iPhone

  VARIABLES:
    - var nIphone
    - var nAnd
    - var puertaSalidaAbierta => con una es suficiente porque todos tienen que bajar
    - var puertaEntradaAndroid => para controlar si puede subir o no un android (CS-And)
    - var puertaEntradaIphone => para controlar si puede subir o no un iPhone (CS-Iph)

  MÉTODOS:
    - paseoIphone
    - paseoAndroid

  NOTAS:
    - Condiciones de sincronización:
        - CS-And = espero si está lleno o si la configuración no es segura (se provoca)
        - CS-iPh = espero si está lleno o si la configuración no es segura (se provoca)
        - CS-Todos = espero hasta el final del paseo (Muy parecida a la del coche -> ponemos una puerta de salida)
*/
object Barca{
  //Contadores de estudiantes de cada tipo
  private var nIPhone = 0
  private var nAndroid = 0
  private var puertaSalidaAbierta = false
  private var puertaEntradaAndroid = true
  private var puertaEntradaIphone = true

  def paseoIphone(id: Int) = synchronized {
    while (!puertaEntradaIphone) wait() //Si no está abierta su puerta no entra
    //Cuando la hebra está aquí es porque el iphone ya está dentro
    nIPhone += 1
    log(s"Estudiante IPhone $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    //Ahora tenemos que mirar cuando cerrar qué puertas (analizar el número de cada tipo)
    /*
    Si cuando entro la suma de los dos tipos da 3 entonces es crítico y ya tenemos que analizar que puerta abrir
     */
    if (nIPhone + nAndroid == 3) { //he sido el tercero en entrar y tengo que analizar la situación actual
      if (nIPhone == 3 || nIPhone == 1) { //no puede entrar ningún android
        puertaEntradaAndroid = false
      } else { //hay un android tengo que cerrar la puerta a los iphone
        puertaEntradaIphone = false
      }
    }
    if (nIPhone + nAndroid < 4) { //si no soy el último en subirme entonces me espero
      while (!puertaSalidaAbierta) wait()
    } else { //soy el último en entrar
      //Cierro las dos puertas de entrada
      puertaEntradaAndroid = false
      puertaEntradaIphone = false
      log(s"Empieza el viaje....")
      Thread.sleep(Random.nextInt(200))
      log(s"fin del viaje....")
      //Abro la puerta de salida y notifico a todas las hebras
      puertaSalidaAbierta = true
      notifyAll()
    }
    //Cuando la hebra está aquí es porque le toca bajarse
    nIPhone -= 1
    log(s"Estudiante IPhone $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    if (nIPhone + nAndroid == 0) { //si soy el último en bajarme
      puertaSalidaAbierta = false //cierro la puerta de salida
      //Abro las dos puertas de entrada
      puertaEntradaIphone = true
      puertaEntradaAndroid = true
      log (s"**********************************************")
      notifyAll() //despierto a todas las hebras
    }
  }

  def paseoAndroid(id:Int) =  synchronized {
    while(!puertaEntradaAndroid) wait()
    nAndroid += 1
    log(s"Estudiante Android $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    /*
    Si cuando entro la suma de los dos tipos da 3 entonces es crítico y ya tenemos que analizar que puerta abrir
     */
    if (nIPhone + nAndroid == 3){
      if (nAndroid == 3 || nAndroid== 1){
        puertaEntradaIphone = false
      } else {
        puertaEntradaAndroid = false
      }
    }
    if (nIPhone + nAndroid < 4){ //si no soy el último en subirme entonces me espero
      while (!puertaSalidaAbierta) wait()
    } else{ //soy el último en entrar
      //Cierro las dos puertas de entrada
      puertaEntradaAndroid = false
      puertaEntradaIphone = false
      log(s"Empieza el viaje....")
      Thread.sleep(Random.nextInt(200))
      log(s"fin del viaje....")
      //Abro la puerta de salida y notifico a todas las hebras
      puertaSalidaAbierta = true
      notifyAll()
    }
    //Cuando la hebra está aquí es porque le toca bajarse
    nAndroid -= 1
    log(s"Estudiante Android $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    if (nIPhone + nAndroid == 0){ //si soy el último en bajarme
      puertaSalidaAbierta = false //cierro la puerta de salida
      //Abro las dos puertas de entrada
      puertaEntradaIphone = true
      puertaEntradaAndroid = true
      log (s"**********************************************")
      notifyAll() //despierto a todas las hebras
    }
  }
}
object Ejercicio5 {

  def main(args:Array[String]) = {
    val NPhones = 10
    val NAndroid = 10
    val iphone = new Array[Thread](NPhones)
    val android = new Array[Thread](NAndroid)
    for (i<-iphone.indices)
      iphone(i) = thread{
     //   while (true){
          Thread.sleep(Random.nextInt(400))
          Barca.paseoIphone(i)
        //    }
      }
    for (i <- android.indices)
      android(i) = thread {
     //   while (true) {
          Thread.sleep(Random.nextInt(400))
          Barca.paseoAndroid(i)
     //   }
      }
  }
}
