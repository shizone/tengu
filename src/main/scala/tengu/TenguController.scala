package tengu

import javafx.stage.{DirectoryChooser, FileChooser}
import javafx.scene.web.WebView
import javafx.fxml.{Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.Button
import javafx.event.ActionEvent

/**
 * Created by razon on 13/10/26.
 */
class TenguController extends Initializable {

  @FXML
  val webView: WebView = null
  @FXML
  val chooseDir: Button = null

  lazy val webEngine = webView.getEngine
  lazy val dirChooser = new DirectoryChooser

  @Override
  def initialize(url: URL, rb: ResourceBundle) {
  }

  @FXML
  def open(e: ActionEvent):Unit = {
    val d = dirChooser.showDialog(null)
    if (d != null) webEngine.load("http://localhost:3000")
  }
}
