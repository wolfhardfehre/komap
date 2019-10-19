package nice.fontaine.models

data class GeoPosition(
        val latitude: Double,
        val longitude: Double
) {
    init { validate() }

    override fun toString(): String = "[$latitude, $longitude]"

    private fun validate() {
        require(latitude <= 90) { "latitude ($latitude°) is bigger than 90°" }
        require(latitude >= -90) { "latitude ($latitude°) is smaller than -90°" }
        require(longitude <= 180) { "longitude ($longitude°) is bigger than 180°" }
        require(longitude >= -180) { "longitude ($longitude°) is bigger than -180°" }
    }
}
