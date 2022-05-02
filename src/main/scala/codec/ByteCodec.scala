package codec

import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

import java.nio.ByteBuffer
import scala.util.Try

trait ByteCodec[A] {
  def encode(x: A): Array[Byte]
  def decode(bs: Array[Byte]): Option[A]
}


trait ByteCodecLaws[A]{
  def codec: ByteCodec[A]

  //first law
  def isomorphism(x: A): Boolean = {
    codec.decode(codec.encode(x)).contains(x)
  }
}

trait ByteCodecTest[A] extends Laws{
  protected def laws: ByteCodecLaws[A]
  def ruleSet(implicit ab: Arbitrary[A]): RuleSet = new DefaultRuleSet(
    "byteCodec",
    parent = None,
    "isomorphism" -> forAll(this.laws.isomorphism _)
  )
}

object ByteCodecTest {
  def apply[A](implicit c: ByteCodec[A]): ByteCodecTest[A] = new ByteCodecTest[A] {
    override def laws: ByteCodecLaws[A] = new ByteCodecLaws[A] {
      override def codec: ByteCodec[A] = c
    }
  }
}

object ByteCodec{

  def apply[A](implicit byteCodec: ByteCodec[A]): ByteCodec[A] = byteCodec

  implicit object ByteCodecInt extends ByteCodec[Int]{
    val INT_SIZE = 4
    override def encode(x: Int): Array[Byte] = {
      val bb = ByteBuffer.allocate(INT_SIZE)
      bb.putInt(x)
      bb.array()
    }

    /***
     * simulate a bug which is very hard to be found by example test
     * @param n
     * @return
     */
    private def bug(n: Int): Int = {
      if (n != 0 && (n % 17) == 0) n + 1 else n
    }

    override def decode(bs: Array[Byte]): Option[Int] = {
      if (bs.length != INT_SIZE){
        None
      } else {
        Try{
          val bb = ByteBuffer.allocate(INT_SIZE)
          bb.put(bs)
          bb.flip()
          bug(bb.getInt)
        }.toOption
      }
    }
  }

}