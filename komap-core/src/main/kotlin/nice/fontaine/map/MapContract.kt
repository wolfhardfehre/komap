package nice.fontaine.map

import nice.fontaine.models.GeoPosition
import nice.fontaine.processors.TileFactory
import java.awt.Rectangle
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

interface MapContract {

    interface View {
        fun draw()
        fun getCanvasBounds(): Rectangle
    }

    interface Presenter {
        fun drawTiles(bounds: Rectangle2D)

        fun getAddress(): GeoPosition
        fun focusAddress(addressLocation: GeoPosition)

        fun getZoom(): Int
        fun setZoom(zoom: Int)

        fun getCenterPosition(): GeoPosition
        fun zoomToBestFit(positions: Set<GeoPosition>, maxFraction: Double, width: Int, height: Int)

        fun getCenter(): Point2D
        fun setCenter(center: Point2D)

        fun calculateViewportBounds(width: Int, height: Int): Rectangle2D

        fun getTileFactory(): TileFactory
    }
}
