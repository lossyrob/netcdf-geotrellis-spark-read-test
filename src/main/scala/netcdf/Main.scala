package netcdf

import geotrellis.raster._
import geotrellis.spark.io.hadoop._
import geotrellis.spark.io.hadoop.formats._
import geotrellis.spark.utils._
import org.apache.spark._
import org.apache.spark.rdd._
import org.apache.hadoop.fs.Path

object Main {
  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.createLocalSparkContext("local[8]", "netcdf-test")
    try {
      val filePath = new java.io.File("tasmax_amon_BCSD_rcp60_r1i1p1_CONUS_bcc-csm1-1_207601-208012.nc").getAbsolutePath
      val rdd: RDD[(NetCdfBand, Tile)] = sc.netCdfRDD(new Path(s"file://$filePath"))
      val count = rdd.count

      println(s"Count is: $count")
    } finally {
      sc.stop()
    }
  }
}
