import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.*

trait NumService:
  val get: Task[Int]

object NumService:
  val get: RIO[NumService, Int] = ZIO.serviceWithZIO[NumService](_.get)

  val live: URLayer[ChannelFactory & EventLoopGroup, NumService] =
    ZLayer.fromZIO(
      for {
        c <- ZIO.service[ChannelFactory]
        e <- ZIO.service[EventLoopGroup]
      } yield NumServiceLive(c, e)
    )

final case class NumServiceLive(c: ChannelFactory, e: EventLoopGroup) extends NumService:
  val url = "https://random-num-x5ht4amjia-uc.a.run.app/"
  val get: Task[Int] =
    val request =
      for
        resp <- Client.request(url)
        body <- resp.bodyAsString
      yield body.toInt

    request.provideEnvironment(ZEnvironment(c, e))

trait WordService:
  val get: ZIO[Any, Throwable, String]

object WordService:
  val get: ZIO[WordService, Throwable, String] = ZIO.serviceWithZIO[WordService](_.get)

  val live: ZLayer[ChannelFactory & EventLoopGroup, Nothing, WordService] =
    ZLayer.fromZIO(
      for {
        c <- ZIO.service[ChannelFactory]
        e <- ZIO.service[EventLoopGroup]
      } yield WordServiceLive(c, e)
    )

final case class WordServiceLive(c: ChannelFactory, e: EventLoopGroup) extends WordService:
  private val url = "https://random-word-x5ht4amjia-uc.a.run.app/"

  val get: ZIO[Any, Throwable, String] =
    val z =
      for
        resp <- Client.request(url)
        body <- resp.bodyAsString
      yield body

    z.provideEnvironment(ZEnvironment(c, e))
