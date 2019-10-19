package nice.fontaine.models

class MapBox(
        private val token: String,
        private val base: Base = Base.STREETS
) : TileInfo("https://api.tiles.mapbox.com/v4/") {

    enum class Base constructor(var mapid: String) {
        BRIGHT("mapbox.bright"),
        COMIC("mapbox.comic"),
        DARK("mapbox.dark"),
        HIGH_CONTRAST("mapbox.high-contrast"),
        LIGHT("mapbox.light"),
        OUTDOORS("mapbox.outdoors"),
        PENCIL("mapbox.pencil"),
        RUN_BIKE_HIKE("mapbox.run-bike-hike"),
        SATELLITE("mapbox.satellite"),
        STREETS("mapbox.streets"),
        STREETS_SATELLITE("mapbox.streets-satellite"),
        MARS("mapbox.mars-satellite")
    }

    override fun tileUrl(x: Int, y: Int, zoom: Int): String =
        "${super.baseURL}${base.mapid}/${super.totalZoom - zoom}/$x/$y.png?access_token=$token"
}
