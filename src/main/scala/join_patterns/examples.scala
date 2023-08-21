package join_patterns
import java.util.concurrent.LinkedTransferQueue
import scala.util.Random

sealed abstract class Msg
case class A()                                      extends Msg
case class B()                                      extends Msg
case class C()                                      extends Msg
case class D(a: Int)                                extends Msg
case class E(a: Int)                                extends Msg
case class F(a: Int)                                extends Msg
case class G(b: Int, a: String, c: Int, d: Boolean) extends Msg

def demo(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val queue = LinkedTransferQueue[Msg]()

  val rcv = receive { (msg: Msg) =>
    msg match
      case (A(), B(), C())     => println(s"I've received 3 messages: A, B and C :)")
      case D(n: Int) if n > 0  => println(s"I've received one message with the payload ${n} :)")
      case E(n: Int) if n != n => println(s"I cannot happen :(")
      case (F(a: Int), E(b: Int)) if (a + b == 42) =>
        println(s"I've received 2 messages with the same payload :)")
  }

  val matcher = rcv(algorithm)

  queue.add(F(21))
  queue.add(E(21))

  queue.add(A())
  queue.add(B())
  queue.add(C())

  // queue.add(D(42))

  // queue.add(E(2))

  println(s"Matcher returned: ${matcher(queue)}")

def test01(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")

  val q = LinkedTransferQueue[Msg]

  var rcv = receive { (y: Msg) =>
    y match
      case (D(x: Int), E(y: Int), F(z: Int)) => println(s"Case 00: x = ${x}, y = ${y}, z = ${z}")
      case (D(x: Int), F(z: Int), E(y: Int)) => println(s"Case 01: x = ${x}, y = ${y}, z = ${z}")
      case (E(y: Int), D(x: Int), F(z: Int)) => println(s"Case 02: x = ${x}, y = ${y}, z = ${z}")
      case (E(y: Int), F(z: Int), D(x: Int)) => println(s"Case 03: x = ${x}, y = ${y}, z = ${z}")
      case (F(z: Int), D(x: Int), E(y: Int)) => println(s"Case 04: x = ${x}, y = ${y}, z = ${z}")
      case (F(z: Int), E(y: Int), D(x: Int)) => println(s"Case 05: x = ${x}, y = ${y}, z = ${z}")
    // case _                                 => println("No match")
  }

  val matcher = rcv(algorithm)
  q.add(D(3))
  q.add(F(2))
  q.add(E(1))
  q.add(A())
  q.add(B())
  q.add(C())

  val initalQ = q.toArray.toList.zipWithIndex
  println(s"Q =  ${initalQ}")
  println(f"Matcher returned: ${matcher(q)}")
  println(s"Q =  ${q.toArray.toList.zipWithIndex}")
  println("\n======================================================\n\n")

def test02(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val q = LinkedTransferQueue[Msg]

  var rcv =
    receive { (y: Msg) =>
      y match
        case (A(), A(), A(), A(), A(), A(), A(), A(), A()) => println("Match!")
    }

  val matcher = rcv(algorithm)

  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())
  q.add(A())

  val initalQ = q.toArray.toList.zipWithIndex

  println(s"Q =  ${initalQ}")
  println(f"receive = ${matcher(q)}")
  println("\n======================================================\n\n")

def test03(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val i: Int                 = 0;
  val m                      = 0
  val isZero: Int => Boolean = (n: Int) => n == 0
  val q                      = LinkedTransferQueue[Msg]

  var rcv = receive { (y: Msg) =>
    y match
      case (E(m: Int), E(n: Int)) if n == 2 && m == 42 => { { val z = "hi"; println(z) }; n + 1 }
      case (A(), B(), A(), E(n: Int)) if n == 2        => 500 * n
      case (B(), A(), B(), E(n: Int)) if n == 2        => 600 * n
  }

  val matcher = rcv(algorithm)

  // A E E B A B
  q.add(E(43))     // 3
  q.add(E(341231)) // 0
  q.add(E(231))    // 5
  q.add(A())       // 4
  q.add(E(2))      // 1
  q.add(E(42))     // 2

  val initalQ = q.toArray.toList.zipWithIndex
  println(s"Q =  ${initalQ}")
  println(s"Matcher returned: ${matcher(q)}")

  println("\n======================================================\n\n")

def test04(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")

  val i: Int                 = 0;
  val m                      = 0
  val isZero: Int => Boolean = (n: Int) => n == 0
  val q                      = LinkedTransferQueue[Msg]

  var rcv = receive { (y: Msg) =>
    y match
      case (E(m: Int), F(n: Int), E(o: Int)) => {
        { val z = "E(m: Int), F(n: Int), E(o: Int)"; println(z) }
      }
  }

  val matcher = rcv(algorithm)

  // q.add(A())
  // q.add(B())
  // q.add(A())
  q.add(E(4))
  q.add(F(2))
  q.add(E(1))

  val initalQ = q.toArray.toList.zipWithIndex
  println(s"Q =  ${initalQ}")
  println(s"Matcher returned: ${matcher(q)}")

  println("\n======================================================\n\n")

def test05(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val q = LinkedTransferQueue[Msg]

  var rcv =
    receive { (y: Msg) =>
      y match
        case (
              E(a: Int),
              E(b: Int),
              E(c: Int),
              E(d: Int),
              E(e: Int),
              E(f: Int),
              E(g: Int),
              E(h: Int),
              E(i: Int),
              E(j: Int)
            )
            if a == 10 && b == 9 && c == 8 && d == 7 && e == 6 && f == 5 && g == 4 && h == 3 && i == 2 && j == 1 =>
          println("Match!")
    }

  val matcher = rcv(algorithm)

  q.add(E(10))
  q.add(E(9))
  q.add(E(8))
  q.add(E(7))
  q.add(E(6))
  q.add(E(5))
  q.add(E(4))
  q.add(E(3))
  q.add(E(2))
  q.add(E(1))

  // q.add(E(1))
  // q.add(E(2))
  // q.add(E(3))
  // q.add(E(4))
  // q.add(E(5))
  // q.add(E(6))
  // q.add(E(7))
  // q.add(E(8))
  // q.add(E(9))
  // q.add(E(10))

  val initalQ = q.toArray.toList.zipWithIndex

  println(s"Q =  ${initalQ}")
  println(f"receive = ${matcher(q)}")
  println("\n======================================================\n\n")

def test06(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val result = Random.nextInt(64)
  val rcv = receive { (y: Msg) =>
    y match
      case (F(i0: Int), E(i1: Int)) if i0 == i1 =>
        result
      case (F(i0: Int), G(i1: Int, s1: String, i2: Int, b: Boolean)) if i0 == i1 && s1 == s1 && b =>
        result + 1
  }
  val matcher = rcv(algorithm)
  val q       = LinkedTransferQueue[Msg]

  q.add(B())
  q.add(A())
  q.add(F(4))
  q.add(G(1, "G", 1, false))
  q.add(B())
  q.add(E(1))
  q.add(E(2))
  q.add(E(3))
  q.add(E(4))
  q.add(E(5))
  q.add(E(42))
  q.add(G(42, "G", 1, true))
  q.add(F(42))

  // println(s"Q =  ${q.toArray.toList.zipWithIndex}")
  println(s"Q =  ${q.toArray.toList.zipWithIndex}")
  println(f"receive = ${matcher(q)}")
  println("\n======================================================\n\n")

def test07(algorithm: MatchingAlgorithm): Unit =
  println(s"Using ${algorithm}\n\n")
  val result = Random.nextInt(64)
  val rcv = receive { (y: Msg) =>
    y match
      case (F(i0: Int), E(i1: Int), F(i2: Int)) if i0 == i1 && i1 == i2 =>
        result
      case F(a: Int) => result * a
  }

  val matcher = rcv(algorithm)
  val q       = LinkedTransferQueue[Msg]

  q.add(F(4))
  q.add(E(4))
  q.add(F(4))

  println(result)
  println(s"Q =  ${q.toArray.toList.zipWithIndex}")
  println(f"receive = ${matcher(q)}")
  println("\n======================================================\n\n")
