import zhttp.http.{Http, Response}
import zhttp.service.{ChannelFactory, EventLoopGroup, Server}
import zio.{Tag, ZIO, ZIOAppDefault, ZLayer}


object WebApp extends ZIOAppDefault:

  def gibberish[E: Tag]: ZIO[E & NumService[E] & WordService, Throwable, String] =
    for
      num   <- NumService.get[E]
      reqs  =  Seq.fill(num)(WordService.get)
      words <- ZIO.collectAllPar(reqs)
    yield words.mkString(" ")

  def app[E: Tag] =
    Http.fromZIO(gibberish.map(Response.text))

  def run =
    val clientLayers = ChannelFactory.auto ++ EventLoopGroup.auto()
    val layers = clientLayers ++ ZLayer.succeed(NumServiceLive()) ++ ZLayer.succeed(WordServiceLive(clientLayers))

    // error:
    // could not find implicit value for izumi.reflect.Tag[zhttp.service.ChannelFactory & zhttp.service.EventLoopGroup]
    Server.start(8080, app[ChannelFactory & EventLoopGroup]).provideLayer(layers).exitCode
    //gibberish[ChannelFactory & EventLoopGroup].debug.provideLayer(layers)
