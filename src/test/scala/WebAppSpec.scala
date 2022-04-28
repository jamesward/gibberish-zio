import zio.test.*
import zio.test.Assertion.*
import zio.ZIO

object WebAppSpec extends ZIOSpecDefault:

  val test1 = test("gibberish"):
    for
      out <- WebApp.gibberish
    yield
      assert(out)(equalTo("word word word word word"))

  def spec = suite("gibberish")(test1).provideLayer(serviceLayers)