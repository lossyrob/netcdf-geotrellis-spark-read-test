package netcdf

import geotrellis.raster._
import geotrellis.raster.op.local._
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
      val rdd: RDD[Tile] = 
        sc.netCdfRDD(new Path(s"file://$filePath"))
          .repartition(32)
          .flatMap { case (_, tile) =>
            val layoutCols = tile.cols / 256
            val layoutRows = tile.rows / 256
            CompositeTile.split(tile, TileLayout(layoutCols, layoutRows, 256, 256), false)
          }

      val start = System.currentTimeMillis()

      val (min, max) =
        rdd
          .map(_.findMinMaxDouble)
          .reduce { (t1, t2) =>
            val (min1, max1) = t1
            val (min2, max2) = t2
            val min =
              if(isNoData(min1)) min2
              else {
                if(isNoData(min2)) min1
                else math.min(min1, min2)
              }
            val max =
              if(isNoData(max1)) max2
              else {
                if(isNoData(max2)) max1
                else math.max(max1, max2)
              }
            (min, max)
          }

      val end = System.currentTimeMillis()

      println(s"\n\n Min and Max: ${(min, max)} \n")

      println(f" TOOK: ${(end - start) / 1000.0}%1.4f s\n\n")
    } finally {
      sc.stop()
    }
  }
}
