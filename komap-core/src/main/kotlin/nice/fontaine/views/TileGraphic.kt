package nice.fontaine.views

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import nice.fontaine.models.CCoord
import java.util.concurrent.atomic.AtomicBoolean

class TileGraphic(private val url: String,
                  private var coord: CCoord,
                  overlay: GraphicOverlay) : Graphic(overlay) {

    private var loading = AtomicBoolean(false)
    private var image: Image = Image("file: images/loading.png")//Image(javaClass.getResourceAsStream("images/loading.png"))

    override fun invalidate() {
        overlay.invalidate()
    }

    override fun contains(other: CCoord): Boolean {
        return coord.x < other.x && coord.x + image.width > other.x &&
                coord.y < other.y && coord.y + image.height > other.y
    }

    override fun draw(context: GraphicsContext) {
        context.drawImage(image, coord.x, coord.y)
    }

    override fun moveTo(coord: CCoord) {
        this.coord = coord
    }

    fun change(image: Image) {
        this.image = image
        loading.set(false)
        invalidate()
    }

    fun url(): String = url

    fun loading(): Boolean = loading.get()

    fun setLoading() {
        loading.set(true)
    }
}