import zhttp.http.{Http, Response}
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio.{ZIO, ZIOAppDefault, ZLayer}


object WebApp extends ZIOAppDefault:

  def gibberish[E]: ZIO[NumService & WordService & ChannelFactory & EventLoopGroup, Throwable, String] =
    for
      num   <- NumService.get
      reqs  =  Seq.fill(num)(WordService.get)
      words <- ZIO.collectAllPar(reqs)
    yield words.mkString(" ")

  val app =
    Http.fromZIO(gibberish.map(Response.text))

  def run =
    Server.start(8080, app).provide(
      ChannelFactory.auto, 
      EventLoopGroup.auto(),
      ZLayer.succeed(NumServiceLive()),
      ZLayer.succeed(WordServiceLive())
      
    ).exitCode
