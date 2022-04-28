name := "gibberish-zio"

// Find new versions at: https://mvnrepository.com/artifact/org.scala-lang/scala3-library_sjs1_3
scalaVersion := "3.2.0-RC1-bin-20220422-fd6ac43-NIGHTLY"

val zioVersion = "2.0.0-RC5"

scalacOptions += "-language:experimental.fewerBraces"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-test"     % zioVersion,

  "io.d11"  %% "zhttp"        % "2.0.0-RC7",

  "dev.zio" %% "zio-test-sbt" % zioVersion  % Test,

  "io.d11"  %% "zhttp-test"   % "2.0.0-RC7" % Test,
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
