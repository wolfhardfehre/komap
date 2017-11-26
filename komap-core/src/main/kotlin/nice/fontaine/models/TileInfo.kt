package nice.fontaine.models

import javafx.geometry.Point2D

abstract class TileInfo(
        internal val baseURL: String,
        private val tileSize: Int = 256,
        private val minZoom: Int = 1,
        private val maxZoom: Int = 17,
        private val totalZoom: Int = 19) {

    private val longitudeDegreeWidthInPixels: DoubleArray = DoubleArray(totalZoom + 1)
    private val longitudeRadianWidthInPixels: DoubleArray = DoubleArray(totalZoom + 1)
    private val mapCenterInPixelsAtZoom: Array<Point2D> = Array(totalZoom + 1, { _ -> Point2D.ZERO })
    private val mapWidthInTilesAtZoom: IntArray = IntArray(totalZoom + 1)

    init {
        var current = tileSize
        for (value in totalZoom downTo 0) {
            longitudeDegreeWidthInPixels[value] = current / 360.0
            longitudeRadianWidthInPixels[value] = current / (2.0 * Math.PI)
            val t2 = current / 2.0
            mapCenterInPixelsAtZoom[value] = Point2D(t2, t2)
            mapWidthInTilesAtZoom[value] = current / tileSize
            current *= 2
        }
    }

    abstract fun getTileUrl(x: Int, y: Int, zoom: Int): String

    fun getTileSize(): Int = tileSize

    fun getMinZoom(): Int = minZoom

    fun getMaxZoom(): Int = maxZoom

    fun getTotalZoom(): Int = totalZoom

    fun mapWidthInTilesAt(zoom: Int): Double = mapWidthInTilesAtZoom[zoom].toDouble()

    fun centerPxAt(zoom: Int): Point2D = mapCenterInPixelsAtZoom[zoom]

    fun widthInDeg(zoom: Int): Double = longitudeDegreeWidthInPixels[zoom]

    fun widthInRad(zoom: Int): Double = longitudeRadianWidthInPixels[zoom]
}