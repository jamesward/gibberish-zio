import zio.test.*
import zio.test.Assertion.*
import zio.ZIO

object WebAppSpec extends ZIOSpecDefault:

  def spec = suite("gibberish")(
    test("gibberish") {
      for
        out <- WebApp.gibberish
      yield
        assert(out)(equalTo("word word word word word"))
    }
  ).provideLayer(serviceLayers)