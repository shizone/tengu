package tengu.controller

import javafx.fxml.{Initializable, FXML}
import javafx.scene.control.{ToolBar, TextArea}
import javafx.scene.web.WebView
import javafx.scene.image.ImageView
import tengu.helper.ControllerHelper._
import javafx.scene.Scene

/**
 * Created by razon on 13/11/04.
 */
class NoteController {

  implicit var scene: Scene = null

  lazy val image = lookup[ImageView]("image")
  lazy val note = lookup[TextArea]("note")

}
