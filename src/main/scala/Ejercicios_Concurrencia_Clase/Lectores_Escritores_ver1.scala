package Ejercicios_Concurrencia_Clase

/*
CS-Escritor = Utilizan la BD en exclusión mútua (con lectores y escritores)
CS-Lector = Varios lectores pueden usar simultáneamente la BD

Si tengo varios lectores esos lectores están simultánemente en la base de datos -> están en el run del proceso
Se divide en protocolos para que el acceso a BD sea concurrente para varios procesos lectores
  - Problema de productor consumidor = el acceso al buffer es con exclusión mutua.

DESCRIPCIÓN DEL PROBLEMA:
Lectores y escritores acceden a una BD. Cuando hay un lector en la sala pueden entrar más lectores para leer a la vez.
Sin embargo, si el que hay dentro es un escritor entonces no puede entrar nadie más a la sala

VARIABLES:
  - Para los lectores
    - nLectores = número de lectores que en hay en cada momento en la base de datos (entero)
    - mutex = new Semaphore(1) para gestionar la variable nLectores con exclusión mútua
    Cogen el mutex y van a intentar incrementarse
      - mutex.acquire()

  - Para los escritorios
    - escribiendo = new Semaphore(1) = semáforo con exclusión mutúa para acceder sobre la BD
      - deja entrar al escritor si no hay ningún lector
      - no deja entrar al escritor si nLectores >= 1
    Cuando un escritor quiere entrar en la BD -> hace un acquire sobre escribiendo
      - Si está cerrado el semáforo me bloqueo
    Cuando un escritor quiere salir -> hace un release sobre escribiendo para que pueda entrar más gente cuando haya terminado

FUNCIONES:
  - OpenE = open escritores
  - CloseE = close E
  - OpenL = open lectores
    - nLectores +=1 //incrementa el número de lectores

    Grupos de lectores:
    - Si hay lectores en la base de datos -> pueden entrar directamente porque puede haber varios simultáneamente
    - Si no hay ningún lector en la base de datos (primero en entrar): (lector crítico = primero)
      - No hay nadie = entro y bloqueo a los escritores
      - Hay gente = me tengo que esperar si hay un escritor

  - CloseL = close lectores
    - nLectores -=1

NOTAS:
  - Cuando un lector está escribiendo puede tener varios escritores bloqueados pero un único lector puesto que este se bloqueará y con el mutex no entrará ninguno más
PROBLEMA DE LA VERSIÓN 1:
  - No es una solución justa, puede que nunca lleguen a entrar los escritores y nunca se actualice la base de datos
 */
import java.util.concurrent._

object Lectores_Escritores_ver1 { //Sincronización
  //Declaración de variables
  private var nLectores = 0
  private val mutex = new Semaphore(1)
  private val escribiendo = new Semaphore(1)

  //Métodos para los escritores
  def openE(): Unit = {
    escribiendo.acquire()
  }

  def closeE(): Unit = {
    escribiendo.release()
  }

  //Métodos para los lectores
  def openL(): Unit = {
    mutex.acquire()
    nLectores +=1
    if (nLectores == 1) //Si solo hay un lector (es el primero) -> bloquea a los escritores
      escribiendo.acquire()
    mutex.release()
  }

  def closeL(): Unit = {
    mutex.acquire()
    nLectores -=1
    if (nLectores == 0)
      escribiendo.release()
    mutex.release()
  }
}
