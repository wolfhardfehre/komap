package nice.fontaine.views

import java.awt.Graphics

interface View {
    fun draw(context: Graphics)
    fun invalidate()
}
