
/*
PROBLEMA DE LOS WORKERS: Hay que esperar a que se termine la iteración de todos para ir a la siguiente
Sala en la que va entrando gente para formar un grupo y hasta que no lleguemos todos no podemos irnos
  La gente va llegando, n se va incrementando y mientras esperamos (n < N)
  Cuando llegue la última persona entonces hay que desbloquearse (despertar a todos los demás)

Necesitamos:
- variable que cuente el número de terminados: private var num_terminados = 0 //inicialmente
- mutex para la variable: private val mutex = Semaphore(1) //inicialmente
- semáforo espera: private val espera = Semaphore(0) //para bloquearse

Cuando llega el primero: (sabe que es el primero al ver n)
Si ha llegado cualquier anterior al último lo que tiene que hacer es bloquearse (necesito un semáforo que valga cero)
Cuando llegue el último queremos hacer una especio de broadcast (con semáforo solo podemos ir despertando 1 a uno con la versión estándar)
La idea sería Dado un número de personas (n < N & n > 0) cuando llegue al último
  1. El último da un toque al siguiente
  2. El siguiente da un toque al siguiente
  Así consecutivamente (se van despertando entre ellos)
  Cuando llegue al último, no puede hacer nada (porque cambiaría el semáforo) así que no despierta a nadie

Métodos:
- heTerminado()
  //Cuando una hebra llama a heTerminado lo que tiene que hacer es incrementar el número de hebras terminadas
  Casos:
    - Soy el último
      El proceso que llega el último tiene que evaluar y no bloquearse, sino despertar a los demás
      Hay N-1 procesos que no son últimos y tengo que hacer N-1 release
    - No soy el último (num_Terminados < N) -> esperamos
        No podemos bloquearnos en una sección crítica, soltamos mutex y esperamos
  Código:
    mutex.acquiere()
    num_Terminados += 1
    //CASO: no es el último
    if (num_Terminados < N)
      mutex.release()
      espera.acquire()
      mutex.acquire()
    //(*1)
    //cuando estoy en esta línea es como si no hubiera sido el último
    //CASO: soy el último
    num_Terminados -= 1 //la hebra que está decrementando se está yendo, y al irse tiene que decidir si despierta a alguien o no
    if (num_Terminados > 0) //quedan procesos terminados que todavía no se han ido
      espera.release()
    mutex.release() //NOTA: Los mutex son comos los paréntesis, siempre que poner el acquire y el release
  Problemas:
  - (*1) = espera.release -> despertaría a dos, y entonces el sistema está desincronizado (se ha ejecutado un release más y num_Terminados no coincide con los bloqueados)
           En el caso de que solo hubiera un trabajador el código funcionaría bien, si hay dos estaría mal porque no se prodían hacer dos release

EL PROBLEMA NO ESTÁ COMPLETO:
- El código heTerminado() lo ejecuta una hebra cuando ha terminado su iteración
  Cuando la hebra termina vuelve a iterar en su código de su bucle
- Escenarios posibles en los que puede haber un error:
  - Cuando la hebra llega la punto en el que ha salido una hebra del método (la última) si vuelve a llegar a la barrera antes que las demás se volvería a meter
    y por tanto el número de terminados no cuadra (se mezclan los que salen con los que llegan y la variable no representa lo que yo quiero
    num_Terminados representa los terminados de una iteración

SOLUCIÓN:
Una barrera (o puerta) que permita controlar en que parte está algoritmo (entrada de los trabajadores o salida de los workers)
- Entrada: abierta
- Salida: cerrada
No nos sirve un booleano porque necesitamos una condición de espera para bloquear
  Creamos un nuevo semáforo que represente si la puerta está abierta o está cerrada (semáforo general)

IMPLEMENTACIÓN CORRECTA (con la puerta)
- variable que cuente el número de terminados: private var num_terminados = 0 //inicialmente
- mutex para la variable: private val mutex = Semaphore(1) //inicialmente
- semáforo espera: private val espera = Semaphore(0) //para bloquearse
- semáforo puerta: private val puerta = Semaphore(1) //inicialmente abierta

  Código:
    puerta.acquire() //si tengo la puerta arriba es como una fase (la fase de entrar por la puerta que es lo primero) //hemos cerrado la puerta
    mutex.acquiere()
    num_Terminados += 1
    //CASO: no es el último -> tengo que abrir la puerta
    if (num_Terminados < N)
      puerta.release() //si llego y no soy el último vuelvo a abrir la puerta para que pueda entrar el siguiente
      mutex.release()
      espera.acquire()
      mutex.acquire()
    //(*1)
    //cuando estoy en esta línea es como si no hubiera sido el último
    //CASO: soy el último -> tengo que cerrar la puerta
    //El último no tiene que tocar la puerta, al principio, solo cuando sale al final del código para poder empezar otra iteración
    num_Terminados -= 1 //la hebra que está decrementando se está yendo, y al irse tiene que decidir si despierta a alguien o no
    if (num_Terminados > 0) //quedan procesos terminados que todavía no se han ido
      espera.release()
    else puerta.release() //para que pueda iniciarse una nueva iteración (ha terminado la fase de salida)
    mutex.release() //NOTA: Los mutex son comos los paréntesis, siempre que poner el acquire y el release
*/


//Release no es bloqueante, puede estar en la sección crítica

package Ejercicios_Concurrencia_Clase
import java.util.concurrent._

object Workers {
  private var num_terminados = 0 //inicialmente
  private val mutex = new Semaphore(1) //inicialmente
  private val espera = new Semaphore(0) //para bloquearse
  private val puerta = new Semaphore(1) //inicialmente abierta
  private val N = 5 //Número de trabajadores totales que hay (depende del problema)
  puerta.acquire()
  mutex.acquire()
  num_terminados += 1
  //CASO: no es el último
  if (num_terminados < N)
    puerta.release()
    mutex.release()
    espera.acquire()
    mutex.acquire()
  //CASO: soy el último
  num_terminados -= 1
  if (num_terminados > 0)
    espera.release()
  else puerta.release()
  mutex.release()
}
