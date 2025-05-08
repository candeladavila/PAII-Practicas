/*
PROBLEMAS DE LA VERSIÓN 1:
  - Cuando hacemos un escribiendo.release() del escritor se puede despertar a un lector o a un escritor
  - En caso de que se despierte a un escritor no hay problema, pero si despertamos a un lector entonces se van despertando entre ellos
    - es un despertado a un número desconocido de elementos
    - si nLectores es siempre >= 1 entonces siempre van a entrar lectores

DESCRIPCIÓN DE LA VERSIÓN JUSTA:
Tenemos que añadir una condición que si hay varios lectores esperando a entrar, un escritor y después otro lector
primero entren los lectores, más tarde el escritor y finalmente el último lector.
  - tenemos que añadir una nueva variable que cuente el número de lectores que quieren entrar
  - tenemos que añadir un nuevo semáforo para que no entren los escritores al código openL
    - 0 si hay escritores que están esperando (bloqueado)
    - 1 si hay escritores esperando (libre)

VARIABLES:
  - originales
    - var nLectores = 0
    - val mutex_1 = new Semaphore(1)
    - val escribiendo = newSemaphore(1)
  - nuevas
    - var nEscritores_esp = 0 //Número de lectores que están esperando para entrar
    - val mutex_2 = new Semaphore(1) //Mutex asociado a la variable nEscritores_esp para modficarla
    - val leyendo = new Semaphore(1) //Semáforo para cerrar el acceso a los lectores
    - val mutex3 = new Sempahore(1) //Semáforo para los lectores (evitar que entren en leyendo)
FUNCIONES:
  - openE
    - Misma función que antes, pero tenemos que modificar las nuevas variables
    - Si el escritor es el primero lo que queremos hacer es bloquear a los lectores que lleguen después
  - closeE
     - Si nEscritores_esp después de decrementar >=1 entonces siguen entrando escritores
     - Si nEscritores_esp es el último escritor (nEscritores_esp == 0) -> leyendo.release para que vuelvan a entrar los lectores
  - openL
  - closeL

PROBLEMA:
  - Si hay un escritor que quiere entrar y un lector intentando entrar (se puede dar porque hay dos mutex distintos)
    - El lector que está entrando tiene leyendo a 0 y el escritor que ha llegado es el primero entonces se bloquea -> no puede cerrarle el paso porque el lector le ha cerrado el paso a él
      - El lector se bloquea en leyendo
      - Cuando salga el lector con leyendo.release() entonces podría entrar otro lector
      - Solución: que nunca se bloquee el lector en leyendo (en el método openE) -> tenemos que modificar el código
         - Modificaciones:
            - Cuando hacemos un leyendo.release() en openL queremos que despierte a un escritor y no a un lector -> hacemos que solo haya una hebra bloqueada
            - nuevo semáforo mutex3 para bloquear a los lectores antes de entrar al leyendo para que haya solo un proceso en leyendo (el escritor)

//Si queremos que cuando hagamos un release se despierte a un proceso en concreto es necesario que SOLO haya un proceso bloqueado para que entre inmediatamente
 */
package practica4
import java.util.concurrent._
import scala.util.Random

object Lectores_Escritores_ver2 {
  //Declaración de variables
  private var nLectores = 0
  private val mutex1 = new Semaphore(1)
  private val escribiendo = new Semaphore(1)
  private var nEscritores_esp = 0
  private val mutex2 = new Semaphore(1) //Semáforo para controlar variable nEscritores_esp
  private val leyendo = new Semaphore(1) //Semáforo para controlar lectores
  private val mutex3 = new Semaphore(1) //Semáforo para que los lectores no entren al semáfoto leyendo

  //Métodos para los escritores
  def openE(i: Int): Unit = {
    //código nuevo
    mutex2.acquire()
    nEscritores_esp +=1
    if (nEscritores_esp == 1) //si el escritor es el primero
      leyendo.acquire()
    log(s"Escritor$i entra en la BD. Hay $nLectores")
    mutex2.release
    //código de ver1
    escribiendo.acquire()
  }

  def closeE(i:Int): Unit = {
    //código nuevo
    mutex2.acquire()
    nEscritores_esp -=1
    if (nEscritores_esp==0)
      leyendo.release()
    mutex2.release()
    //código de ver1
    log(s"Escritor$i sale en la BD. Hay $nLectores")
    escribiendo.release()
  }

  //Métodos para los lectores
  def openL(i: Int): Unit = {
    mutex3.acquire()
    leyendo.acquire()
    mutex1.acquire()
    nLectores += 1
    if (nLectores == 1) //Si solo hay un lector (es el primero) -> bloquea a los escritores
      escribiendo.acquire()
    log(s"Lector$i entra en la BD. Hay $nLectores")
    mutex1.release()
    leyendo.release()
    mutex3.release
  }

  def closeL(i:Int): Unit = {
    mutex1.acquire()
    nLectores -= 1
    if (nLectores == 0)
      escribiendo.release()
    log(s"Lector$i sale en la BD. Hay $nLectores")
    mutex1.release()
  }

  @main def LectEsc = {
    val NL = 10;
    val NE = 2
    val lector = new Array[Thread](NL)
    val escritor = new Array[Thread](NE)
    for (i <- 0 until (lector.length)) {
        lector(i) = thread {
        while (true) {
          openL(i)
          // lector i en la BD
          Thread.sleep(Random.nextInt(200))
          closeL(i)
        }
      }
    }
    for (i <- 0 until (escritor.length)) {
      lector(i) = thread {
        while (true) {
          openE(i)
          // escritor i en la BD
          Thread.sleep(Random.nextInt(200))
          closeE(i)
        }
      }
    }
  }
}

