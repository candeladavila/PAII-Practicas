package practica2

import scala.collection.mutable.ArrayBuffer

trait MutableQueue[T] {
  def enqueue(elem: T): Unit
  def dequeue(): Option[T]
  def isEmpty: Boolean
}

class ArrayQueue[T](elems: T*) extends MutableQueue[T] {
  private val buffer = ArrayBuffer[T](elems: _*)
  //ENQUEUE
  def enqueue(elem: T): Unit =
    buffer.insert(buffer.size, elem)

  //DEQUEUE
  def dequeue(): Option[T] =
    if (buffer.isEmpty) None
    else Some(buffer.remove(0))

  //ISEMPTY
  def isEmpty: Boolean =
    buffer.isEmpty

  //TOSTRING
  override def toString: String =
    buffer.mkString("Queue(",", ",")")

  //EQUALS
  override def equals(obj: Any): Boolean =
    obj match
      case that:ArrayQueue[T] => this.buffer.equals(that.buffer)
      // debería funcionar si haces this.buffer == that.buffer
      case _=> false

  //HASHCODE
  override def hashCode(): Int =
    buffer.hashCode()
}

@main def testMutableQueue(): Unit = {
  val queue = new ArrayQueue(1, 2, 3)
  queue.enqueue(4)
  assert(queue.dequeue().contains(1), "The first element of the queue should be 1")
  assert(!queue.isEmpty, "The queue should not be empty")
  assert(queue == new ArrayQueue(2, 3, 4), "The two queues should be equal")
  assert(queue.hashCode() == new ArrayQueue(2, 3, 4).hashCode(), "The hash codes of the two queues should be equal")
  assert(queue.toString == "Queue(2, 3, 4)", s"The string representation of ${queue} should be 'Queue(2, 3, 4)'")
  assert(new ArrayQueue[String]().dequeue() == None, "Dequeuing from an empty queue should return None")
}
