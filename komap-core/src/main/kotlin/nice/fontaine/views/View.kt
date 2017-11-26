package nice.fontaine.views

import javafx.scene.canvas.GraphicsContext

interface View {
    fun draw(context: GraphicsContext)
    fun invalidate()
}