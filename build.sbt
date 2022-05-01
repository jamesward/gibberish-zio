name := "gibberish-zio"

scalaVersion := "3.1.2"

val zioVersion = "2.0.0-RC5"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-test"     % zioVersion,

  "io.d11"  %% "zhttp"        % "2.0.0-RC7",

  "dev.zio" %% "zio-test-sbt" % zioVersion  % Test,

  "io.d11"  %% "zhttp-test"   % "2.0.0-RC7" % Test,
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
