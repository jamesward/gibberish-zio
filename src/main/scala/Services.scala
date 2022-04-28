import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Tag, ZIO, ZLayer}

trait NumService:
  val get: ZIO[ChannelFactory & EventLoopGroup, Throwable, Int]

case class NumServiceLive() extends NumService:
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"
  val get: ZIO[ChannelFactory & EventLoopGroup, Throwable, Int] =
      for
        resp <- Client.request(url)
        body <- resp.bodyAsString
      yield body.toInt

object NumService:
  val get: ZIO[NumService & ChannelFactory & EventLoopGroup, Throwable, Int] = ZIO.serviceWithZIO[NumService](_.get)

trait WordService:
  val get: ZIO[ChannelFactory & EventLoopGroup, Throwable, String]

case class WordServiceLive() extends WordService:
  val url = "https://random-word-x5ht4amjia-uc.a.run.app/"
  val get: ZIO[ChannelFactory & EventLoopGroup, Throwable, String] =
    for
      resp <- Client.request(url)
      body <- resp.bodyAsString
    yield body

object WordService:
  val get: ZIO[WordService & ChannelFactory & EventLoopGroup, Throwable, String] = ZIO.serviceWithZIO[WordService](_.get)

