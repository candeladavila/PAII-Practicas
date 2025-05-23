package esqueletosLaboratorio7

import scala.util.Random
/*
VARIABLES:
  - numPas = lleva la cuenta del numero de pasajeros
  - booleano puertaEntradaAbierto (CS-Pasajero1) => puerta abierta = puede entrar gente y se puede llenar ; puerta cerrrada = está lleno, viaje o se están bajando
  - booleano puertaSalidaAbierta  (CS-Pasajero1)
  - booleano lleno => para indicar cuando se da el paso o no a los pasajeros
MÉTODOS:
  - nuevoPaseo(id:Int) => todos los pasajeros que se tienen que esperar a que se termine el viaje para poder bajar
      - el ultimo pasajero en subirse tiene que avisar al coche para que empiece el viaje
      - en cualquier caso todo el mundo tiene que esperarse a que termine el paseo
  - esperaLleno => el coche espera a que lleno == true para empezar al viaje
  - finViaje => abre la puerta de salida a los pasajeros

NOTAS:
  - En esta implementación el recurso que es el coche es a la vez una hebra que tiene un método run que es donde está ejecutandose el
    código de lleno. El main lo unico que hace es crear los pasajero e inicializarlos
  - asociamos a cada condición un booleano que indica si puede pasar o si no puede pasar
  - en el caso de los monitores solo se cambia los booleanos cuando quiero cerrar la puerta, en el caso de los semáforos teniamos que hacer el release
    para dejar entrar a otra persona, si no se hace nada entonces la condición sigue estando en el mismo estado que estaba
  - notify() = despierta solo a una hebra
  - notifyAll() = despierta a  todas las hebras de su wait (se vueleven a analizar todas las condiciones de while)
*/

class Coche(C:Int) extends Thread{
  //CS-pasajero1: si el coche está lleno, un pasajero no puede subir al coche hasta que haya terminado
  //el viaje y se hayan bajado los pasajeros de la vuelta actual
  //CS-pasajero2: un pasajero que está en el coche no se puede bajarse hasta que haya terminado el viaje
  //CS-coche: el coche espera a que se hayan subido C pasajeros para dar una vuelta
  private var numPas = 0

  //VARIABLES
  private var puertaEntradaAbierta = true
  private var puertaSalidaAbierta = false
  private var lleno = false
  


  def nuevoPaseo(id:Int)= synchronized { //hay que poner "syncronized" en todo para garantizar exclusión mutua (en el run no)
    //el pasajero id  quiere dar un paseo en la montaña rusa
    while (!puertaEntradaAbierta) wait () //me espero si la puerta de entrda no está abierta
    //Aquí ya está el pasajero dentro del coche
    numPas += 1
    log(s"El pasajero $id se sube al coche. Hay $numPas pasajeros.")
    if (numPas == C){ //llena el coche y cierra la puerta de entrada
      puertaEntradaAbierta = false
      lleno = true
      notifyAll()
      /*
      hay que poner notifyAll cuando quieres despertar a todas las hebras y si quieres despertar a una hebra específica pero hay más de una esperando
      porque no podemos seleccionar la hebra en concreto que queremos despertar -> despertamos a todas
      Hacemos notifyAll para poder despertar a la hebra del coche que es la que nos interesa en concreto
       */
    }
    while (!puertaSalidaAbierta) wait() //me espero hasta que se abra la puerta de salida
    numPas -= 1
    log(s"El pasajero $id se baja del coche. Hay $numPas pasajeros.")
    /*
    Tenemos que volver a cambiar el valor de la variable puertaSalidaAbierta para despertarlas poco a poco pero primero tengo que despertar a todo el mundo
    La unica que tiene que cambiar la variable para bloquear la puerta de salida de nuevo tiene que ser la última hebra
    Además tiene que volver a abrir la puerta de entrada para empezar un nuevo viaje
     */
    if (numPas == 0) { //si soy el ultimo pasajero en bajarme
        puertaSalidaAbierta = false //reseteo de la variable de la puertaSalidaAbierta
        puertaEntradaAbierta = true //abro la puerta
        log(s"****************************************************")
        notifyAll() //para despertar a todas las hebras bloqueadas
    }

  }

  def esperaLleno =  synchronized {
    //el coche espera a que se llene para dar un paseo
    while (!lleno) wait() //espera a que el último pasajero le avise que está lleno
    //aquí el coche ya ha sido avisado de que está lleno -> ahora tengo que volver a ponerlo a falso para poder bloquearme otra vez
    lleno = false
    log(s"        Coche lleno!!! empieza el viaje....")
  }

  def finViaje =  synchronized {
    //el coche indica que se ha terminado el viaje
    log(s"        Fin del viaje... :-(")
    puertaSalidaAbierta = true //abre la puerta de entrada a los pasajeros y despierta a las hebras que están bloqueadas en la salida
    notifyAll() //para despertar a todas las hebras
  }

  override def run = { //Aquí no hay que poner "synchronized"
    while (true){
      esperaLleno
      Thread.sleep(Random.nextInt(Random.nextInt(500))) //el coche da una vuelta
      finViaje
    }
  }
}
object Ejercicio4 {
  def main(args:Array[String])=
    val coche = new Coche(5)
    val pasajero = new Array[Thread](20)
    coche.start()
    for (i<-0 until pasajero.length)
      pasajero(i) = thread{
          while (true)
            Thread.sleep(Random.nextInt(500))
            coche.nuevoPaseo(i)
      }
      
}

/*
Para hacer que el sistema termine hay que meter interrupciones (en la proxima clase de teoría)
La cuestión es que cuando las hebras pasajero hayan terminado el main puede saber que todas hayan terminado (join)
y entonces lo que va a hacer el método es hacer que el coche termine cuando hayan terminado todos los pasajeros
*/
