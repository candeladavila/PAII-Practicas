/*
Entran -> Sala de espera -> Sala de pelar -> Salen pelados

Condiciones de sincronización:
- El barbabero no puede pelar si no hay ningún cliente
- Cuando no hay ningún cliente el barbero está durmiendo

Solución usando semáforos binarios
Parte pasiva:
- Hebra barbero
- Array de clientes donde cada cliente(i) es una hebra
Parte activa:
- Objeto barbería = representa el habitáculo con número de clientes que hay esperando en la sala de espera
  - buffer: private var num_clients = 0 (corresponde con el semáforo general)
  - semáforo en el que el barbero esté bloqueado (binario): private val espera = new Semaphore(0) //DEBE VALER SIEMPRE CERO (cuando acquire quiero bloquearme)
    El valor del semáforo tiene que ir en consonancia con el buffer (si no hay nadie semáforo cerrado)
    Si n es distinto de 0 entonces el semáforo tiene que ser 1
  - mutex semáforo que garantiza la exclusión mutua sobre num_clients: private val mutex = new Semaphore(1)
    Los clientes incrementan el valor del buffer cuando llegan
    El semáforo decrementa el valor del buffer cuando llegan

Métodos:
- Añadir elementos (nuevo cliente en la sala de espera): def nuevoCliente()=
  Cuando llega un nuevo cliente lo que hace es llegar a la sala de espera y añadirse (no hay condición de sincronización, siempre caben)
    Si el buffer está acotado entonces no puedo entrar si está lleno, no es el caso
    Cuando llega un cliente pueden pasar dos cosas:
      - Cuando llega un cliente ya hay gente esperando (se añade a la cola)
      - Cuando llega un cliente la sala de espera está vacía
    Código:
      def nuevoCliente() =
        mutex.acquire()
        num_clients +=1
        if (n == 1) //si al incrementar sale 1 es porque soy el primero
          espera.release() //despierto al barbero
        //si no soy el primero no despierto porque el barbero ya está despierto
        mutex.release()
        
- Eliminar elementos (pelar): def pelar()=
  Pelar va a estar dentro de un bucle 
  Barbero = thread
    while (true)
      barbero.pelar()
  Cuando mire en la sala de espera el barbero va a decrementar el número de clientes que hay
    Código: 
      def pelar() = 
      //Cada vez que va a pelar coge al cliente y mira cuanta gente queda después de haberlo cogido en la sala de espera
      //Si el cliente al que va a pelar es el último (solo hay uno en la sala de espera y lo coge)
        Sabe que la sala de espera se ha quedado vacía y se bloquea
        if (primera) (si entra primero el barbero que se bloquee)
          primera = false
          espera.acquire
        //en este punto espera = 0 y num_clients >= 1
        mutex.acquire() (si entra primero el cliente)
        num_clients = num_clients -1 //Pela
        val m = num_clients //variable local creada para almacenar el valor de ese intante de la global
        mutex.release() 
        if (m == 0) espera.acquire()
    Problemas: 
      - si la variable n la leo fuera de la sección crítica puede no coincidir ya que pueden venir más clientes tras hacer el release
        y entonces ya no coinciden valores, necesitamos que la n del if (n == 0) coincida justo con la n del mutex.acquiere
      - No podemos sacar una variable global fuera de la sección crítica (casi siempre va a estar mal)
    Solución: 
      - crear una variable global en la sección crítica que almacene el valor global
*/

package practica4
import java.util.concurrent._

object barberos {
  private var num_clients = 0
  private val espera = new Semaphore(0)
  private val mutex = new Semaphore(1)
  private var primera = true

  def nuevoCliente() =
    mutex.acquire()
    num_clients += 1
    if (num_clients == 1) 
      espera.release()
    mutex.release()
    
  def pelar() =
    if (primera)
      primera = false
      espera.acquire
    mutex.acquire()
    num_clients = num_clients - 1 //Pela
    val m = num_clients
    mutex.release()
    if (m == 0) espera.acquire()
}

