import zhttp.http.{Http, Response}
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio.{ZEnvironment, ZIO, ZIOAppDefault}


object WebApp extends ZIOAppDefault:

  val gibberish: ZIO[NumService & WordService, Throwable, String] =
    for
      num   <- NumService.get
      reqs  =  Seq.fill(num)(WordService.get)
      words <- ZIO.collectAllPar(reqs)
    yield words.mkString(" ")

  val app =
    Http.fromZIO(gibberish.map(Response.text))

  def run =
    val layers =
      for
        channelFactory <- ChannelFactory.auto
        eventLoopGroup <- EventLoopGroup.auto()
      yield
        val numServiceLive = NumServiceLive(channelFactory.get, eventLoopGroup.get)
        val wordServiceLive = WordServiceLive(channelFactory.get, eventLoopGroup.get)
        ZEnvironment(numServiceLive, wordServiceLive)

    Server.start(8080, app).provideLayer(layers).exitCode
