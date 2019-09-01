package nice.fontaine.models

class GeoPosition(private val latitude: Double,
                  private val longitude: Double) {

    constructor(coordinates: DoubleArray) : this(coordinates[0], coordinates[1])

    constructor(latDegrees: Int, latMinutes: Int, latSeconds: Int,
                lonDegrees: Int, lonMinutes: Int, lonSeconds: Int) : this(latDegrees + (latMinutes + latSeconds / 60.0) / 60.0,
            lonDegrees + (lonMinutes + lonSeconds / 60.0) / 60.0)

    init { validate() }

    fun getLatitude(): Double = latitude

    fun getLongitude(): Double = longitude

    override fun hashCode(): Int {
        var result = 1
        var temp = longBits(latitude)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = longBits(longitude)
        return 31 * result + (temp xor temp.ushr(32)).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is GeoPosition) return false
        if (latitude != other.latitude) return false
        return longBits(longitude) == longBits(other.longitude)
    }

    override fun toString(): String = "[$latitude, $longitude]"

    private fun validate() {
        if (latitude > 90) throw IllegalArgumentException("latitude ($latitude°) is bigger than 90°")
        if (latitude < -90) throw IllegalArgumentException("latitude ($latitude°) is smaller than -90°")
        if (longitude > 180) throw IllegalArgumentException("longitude ($longitude°) is bigger than 180°")
        if (longitude < -180) throw IllegalArgumentException("longitude ($longitude°) is bigger than -180°")
    }

    private fun longBits(dbl: Double): Long = java.lang.Double.doubleToLongBits(dbl)
}
