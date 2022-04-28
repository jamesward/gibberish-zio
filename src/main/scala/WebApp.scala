import zhttp.http.{Http, Response}
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio.{ZIO, ZIOAppDefault, ZLayer}


object WebApp extends ZIOAppDefault:

  def gibberish: ZIO[NumService & WordService, Throwable, String] =
    for
      num   <- NumService.get
      reqs  =  Seq.fill(num)(WordService.get)
      words <- ZIO.collectAllPar(reqs)
    yield words.mkString(" ")

  val app =
    Http.fromZIO(gibberish.map(Response.text))

  def run =
    val clientLayers = ChannelFactory.auto ++ EventLoopGroup.auto()
    val layers = ZLayer.succeed(NumServiceLive(clientLayers)) ++ ZLayer.succeed(WordServiceLive(clientLayers))

    Server.start(8080, app).provideLayer(layers).exitCode
