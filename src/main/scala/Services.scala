import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Tag, ZIO, ZLayer}

trait NumService:
  type Env
  val get: ZIO[Env, Throwable, Int]

class NumServiceLive extends NumService:
  type Env = ChannelFactory & EventLoopGroup
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"
  val get: ZIO[Env, Throwable, Int] =
    for
      resp <- Client.request(url)
      body <- resp.bodyAsString
    yield body.toInt

object NumService:
  // error:
  // Found:    zio.ZIO[Nothing, Throwable, Int]
  // Required: zio.ZIO[NumService#Env, Throwable, Int]
  def get: ZIO[NumService#Env, Throwable, Int] = ZIO.serviceWithZIO[NumService](_.get)


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

