
/*
DESCRIPCIÓN:
  Condición de sincronización: no es posible que un filósofo coma hasta que no tenga los dos utensilios
  Tendremos:
    - Filósofos (hebras)
    - Recursos compartidos:
      - utensilios
  Como los recursos compartidos son distintos entre ellos lo suyo es que cada recurso sea un objeto distinto
    - clase Utensilio -> con un id
      - crearemos tantas instancias de la clase como objetos que compartimos
  Cada filósofo puede coger solo los objetos que le correspondan (el que tenga su id y el que tenga el id+1 -> en módulo 5)
  Suponemos que:
    - Cantidad infinita comida
    - Cada filósofo tiene un puesto específico en la mesa (su plato)
    - Cada filósofo tiene asociado dos utensilios para comer (necesitan los dos para poder comer)

FUNCIONAMIENTO:
  Filósofo(id: Int){
    run()
    while(true){
      //pensar
      utensilio(i).coger(f)
      utensilio((i+1)%5).coger(f)
      //comer
      utensilio(i).soltar(f)
      utensilio((i+1)%5).soltar(f)

CLASE UTENSILIO:
  VARIABLES:
    - private val libre = new Semaphore(1) //Semáforo para comprobar si un utensilio está libre o no (inicialmente libre)

  MÉTODOS:
    - coger(f:Int) //f = filósofo que lo coge
        libre.acquire()
    - soltar(f:Int) //f = filósofo que lo coge
        libre.release()

CLASE FILÓSOFO:
VARIABLES:
  - val utensilios = new Array[Utensilios](5) //Array que almacena los recursos compartidos
  - var nSillas = 4 //Inicialmente hay 4 sillas libres
  - val mutex = new Semaphore(1) //Semáforo para controlar el acceso a la variable nSillas
MÉTODOS:
  - pensar
  - comer

PROBLEMA:
  - Si todos los filósofos entran a la vez todos cogen el utensilio de la izquierda y se quedan bloqueados (todos piden en círculo)
SOLUCIÓN:
  - Impedir que sean todos los que piden (mejor solución -> desarrollamos)
    - Número máximo de filósofos que pueden estar intentando comer sin que se bloquee = 4
      Si tenemos cuatro filósofos entonces al menos 1 podrá coger los dos utensilios y no se bloqueará
      Antes de entrar al método pensar hacemos vamos contando -> imaginamos que solo hay cuatro sillas y no pueden entrar más de cuatro -> un contador y un semáforo
  - Impedir que lo pidan en círculo


*/
package Ejercicios_Concurrencia_Clase
import java.util.concurrent._

class Utensilio {
  private val libre = new Semaphore(1)

  //MÉTODOS
  def coger(f:Int): Unit = {
    libre.acquire()
  }
  def soltar(f:Int): Unit = {
    libre.release()
  }
}

object Filosofos {
}
