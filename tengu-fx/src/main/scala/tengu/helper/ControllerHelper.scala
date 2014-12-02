package tengu.helper

import javafx.scene.Scene

/**
 * Created by razon on 13/12/21.
 */
object ControllerHelper {
  def lookup[T](id: String)(implicit scene: Scene): T = scene.lookup(s"#${id}").asInstanceOf[T]
}
