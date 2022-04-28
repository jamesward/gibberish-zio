import zio.test.*
import zio.test.Assertion.*
import zio.{ZIO, ZLayer}

class TestNumService extends NumService:
  val get: ZIO[Any, Throwable, Int] = ZIO.succeed(5)

class TestWordService extends WordService:
  val get: ZIO[Any, Throwable, String] = ZIO.succeed("word")

val serviceLayers = ZLayer.succeed(TestNumService()) ++ ZLayer.succeed(TestWordService())

object ServicesSpec extends ZIOSpecDefault:

  val test1 = test("numService"):
    for
      num <- NumService.get
    yield
      assert(num)(equalTo(5))

  val test2 = test("wordService"):
    for
      num <- WordService.get
    yield
      assert(num)(equalTo("word"))

  def spec = suite("gibberish")(test1, test2).provideLayer(serviceLayers)
