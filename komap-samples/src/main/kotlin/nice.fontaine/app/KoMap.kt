package nice.fontaine.app

import nice.fontaine.map.MapCanvas
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.MapBox
import nice.fontaine.processors.TileFactory
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    val info = MapBox(MAPBOX, MapBox.Base.STREETS_SATELLITE)
    val frankfurt = GeoPosition(50.11, 8.68)

    SwingUtilities.invokeLater {
        val canvas = MapCanvas(TileFactory(info), 600, 600)
        canvas.isVisible = true
        canvas.setFocus(frankfurt)
    }
}
