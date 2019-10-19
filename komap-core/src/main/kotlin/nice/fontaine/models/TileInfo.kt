package nice.fontaine.models

import java.awt.geom.Point2D


abstract class TileInfo(
        internal val baseURL: String,
        val tileSize: Int = 256,
        val minZoom: Int = 1,
        val maxZoom: Int = 19,
        val totalZoom: Int = 19
) {
    private val longitudeDegreeWidthInPixels: DoubleArray = DoubleArray(totalZoom + 1)
    private val longitudeRadianWidthInPixels: DoubleArray = DoubleArray(totalZoom + 1)
    private val mapCenterInPixelsAtZoom: Array<Point2D> = Array(totalZoom + 1) { Point2D.Double(0.0, 0.0) }
    private val mapWidthInTilesAtZoom: IntArray = IntArray(totalZoom + 1)

    init {
        var current = tileSize
        for (value in totalZoom downTo 0) {
            longitudeDegreeWidthInPixels[value] = current / 360.0
            longitudeRadianWidthInPixels[value] = current / (2.0 * Math.PI)
            val t2 = current / 2.0
            mapCenterInPixelsAtZoom[value] = Point2D.Double(t2, t2)
            mapWidthInTilesAtZoom[value] = current / tileSize
            current *= 2
        }
    }

    abstract fun tileUrl(x: Int, y: Int, zoom: Int): String

    fun isValidZoom(zoom: Int): Boolean = zoom in minZoom..maxZoom

    fun mapWidthInTilesAt(zoom: Int): Int = mapWidthInTilesAtZoom[zoom]

    fun centerPxAt(zoom: Int): Point2D = mapCenterInPixelsAtZoom[zoom]

    fun widthInDeg(zoom: Int): Double = longitudeDegreeWidthInPixels[zoom]

    fun widthInRad(zoom: Int): Double = longitudeRadianWidthInPixels[zoom]
}
