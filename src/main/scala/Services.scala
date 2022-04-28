import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{ZEnvironment, ZIO}

trait NumService:
  val get: ZIO[Any, Throwable, Int]

case class NumServiceLive(channelFactory: ChannelFactory, eventLoopGroup: EventLoopGroup) extends NumService:
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"

  val z: ZIO[ChannelFactory & EventLoopGroup, Throwable, Int] =
    for
      resp <- Client.request(url)
      body <- resp.bodyAsString
    yield body.toInt

  val get: ZIO[Any, Throwable, Int] =
    z.provideEnvironment(ZEnvironment(channelFactory, eventLoopGroup))

object NumService:
  val get: ZIO[NumService, Throwable, Int] = ZIO.serviceWithZIO[NumService](_.get)


trait WordService:
  val get: ZIO[Any, Throwable, String]

case class WordServiceLive(channelFactory: ChannelFactory, eventLoopGroup: EventLoopGroup) extends WordService:
  val url = "https://random-word-x5ht4amjia-uc.a.run.app/"

  val z: ZIO[ChannelFactory & EventLoopGroup, Throwable, String] =
    for
      resp <- Client.request(url)
      body <- resp.bodyAsString
    yield body

  val get: ZIO[Any, Throwable, String] =
    z.provideEnvironment(ZEnvironment(channelFactory, eventLoopGroup))

object WordService:
  val get: ZIO[WordService, Throwable, String] = ZIO.serviceWithZIO[WordService](_.get)

