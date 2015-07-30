name := "NetCDF GeoTrellis Test"

version := "0.1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.4.1",
  "org.apache.hadoop" % "hadoop-client" % "2.6.0",
  "com.azavea.geotrellis" %% "geotrellis-spark"  % "0.10.0-M1"
)

mainClass := Some("netcdf.Main")

javaOptions += "-Djava.library.path=/usr/local/lib"

scalacOptions ++= Seq("-unchecked", "-deprecation")
