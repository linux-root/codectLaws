package codec

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.Configuration
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

class ByteCodecSpec extends AnyFunSuite with FunSuiteDiscipline with Configuration{
  checkAll("ByteCodec[Int]", ByteCodecTest[Int].ruleSet)

  test("example test for 10"){
    exampleTest(10)
    exampleTest(11)
//    exampleTest(-600061376)
  }

  def exampleTest(n: Int): Unit ={
    val barray = ByteCodec[Int].encode(n)
    assert(ByteCodec[Int].decode(barray).contains(n))
  }
}
