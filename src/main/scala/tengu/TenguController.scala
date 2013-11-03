package tengu

import javafx.stage.DirectoryChooser
import javafx.scene.web.WebView
import javafx.fxml.{Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.Button
import javafx.event.ActionEvent
import pictureshow.Server
import unfiltered.jetty.Server
import pictureshow.Server

/**
 * Created by razon on 13/10/26.
 */
class TenguController extends Initializable {

  @FXML
  val webView: WebView = null
  @FXML
  val chooseDir: Button = null

  private lazy val webEngine = webView.getEngine
  private lazy val dirChooser = new DirectoryChooser

  override def initialize(url: URL, rb: ResourceBundle) {
  }

  private var svr: PicturShowServer = PicturShowServer(null)

  @FXML
  def open(e: ActionEvent):Unit = {
    val d = dirChooser.showDialog(null)
    if (d != null) {
      svr.start(d.getPath).fold({ _ =>
      }, { svr =>
        this.svr = svr
        webEngine.load("http://localhost:3000")
        webView.requestFocus()
      })
    }
  }
}
