import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Tag, ZIO, ZLayer}

trait NumService[Env]:
  def get[E <: Env]: ZIO[E, Throwable, Int]

class NumServiceLive extends NumService[ChannelFactory & EventLoopGroup]:
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"
  def get[E <: ChannelFactory & EventLoopGroup]: ZIO[E, Throwable, Int] =
    for
      resp <- Client.request(url)
      body <- resp.bodyAsString
    yield body.toInt

object NumService:
  def get[E: Tag]: ZIO[E & NumService[E], Throwable, Int] = ZIO.serviceWithZIO[NumService[E]](_.get)


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

