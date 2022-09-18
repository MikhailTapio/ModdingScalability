package committee.nova.scalability.implicits

import java.lang.{Iterable => JIterable}
import java.util
import java.util.function.{Consumer, Function, Predicate, Supplier}
import java.util.{Iterator => JIterator}

/**
 * The package provides implicits to 4 common java function interfaces
 */
package object functions {
  implicit class FunctionImplicit[T, R](val f: T => R) extends Function[T, R] {
    override def apply(t: T): R = f.apply(t)
  }

  implicit class ConsumerImplicit[T](val c: T => Unit) extends Consumer[T] {
    override def accept(t: T): Unit = c.apply(t)
  }

  implicit class SupplierImplicit[T](val s: () => T) extends Supplier[T] {
    override def get(): T = s.apply()
  }

  implicit class PredicateImplicit[T](val s: T => Boolean) extends Predicate[T] {
    override def test(t: T): Boolean = s.apply(t)
  }

  implicit class IterableImplicit[T](val s: () => JIterator[T]) extends JIterable[T] {
    override def iterator(): util.Iterator[T] = s.apply()
  }
}
