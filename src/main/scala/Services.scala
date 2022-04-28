import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Tag, ZIO, ZLayer}

trait NumService:
  val get: ZIO[Any, Throwable, Int]

case class NumServiceLive(layers: ZLayer[Any, Nothing, ChannelFactory & EventLoopGroup]) extends NumService:
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"
  val get: ZIO[Any, Throwable, Int] =
    val z =
      for
        resp <- Client.request(url)
        body <- resp.bodyAsString
      yield body.toInt

    z.provideLayer(layers)

object NumService:
  val get: ZIO[NumService, Throwable, Int] = ZIO.serviceWithZIO[NumService](_.get)


trait WordService:
  val get: ZIO[Any, Throwable, String]

case class WordServiceLive(layers: ZLayer[Any, Nothing, ChannelFactory & EventLoopGroup]) extends WordService:
  val url = "https://random-word-x5ht4amjia-uc.a.run.app/"
  val get: ZIO[Any, Throwable, String] =
    val z =
      for
        resp <- Client.request(url)
        body <- resp.bodyAsString
      yield body

    z.provideLayer(layers)

object WordService:
  val get: ZIO[WordService, Throwable, String] = ZIO.serviceWithZIO[WordService](_.get)

