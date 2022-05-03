import zhttp.http.{Http, HttpApp, Response}
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio.{ZIO, ZIOAppDefault, ZLayer}

object WebApp extends ZIOAppDefault:

  def gibberish: ZIO[NumService & WordService, Throwable, String] =
    for
      num   <- NumService.get
      reqs   = Seq.fill(num)(WordService.get)
      words <- ZIO.collectAllPar(reqs)
    yield words.mkString(" ")

  val app: HttpApp[NumService & WordService, Throwable] =
    Http.fromZIO(gibberish.map(Response.text))

  def run =
    // alternatively rename this to ULayer[A] since it's aliased to ZLayer[Any, Nothing, A]
    val channelFactory: ZLayer[Any, Nothing, ChannelFactory] = ChannelFactory.auto
    val eventLoopGroup: ZLayer[Any, Nothing, EventLoopGroup] = EventLoopGroup.auto()

    Server
      .start(8080, app)
      .provide(
        channelFactory,
        eventLoopGroup,
        NumService.live,
        WordService.live
        //ZLayer.Debug.mermaid  // Uncomment this in order to generate Mermaid.JS graphs
      )
      .exitCode
