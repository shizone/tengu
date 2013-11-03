package tengu

import javafx.stage.DirectoryChooser
import javafx.scene.web.WebView
import javafx.fxml.{Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.Button
import javafx.event.ActionEvent
import java.nio.file.{Paths, FileSystems, Files}
import java.io.File
import java.nio.file.attribute.FileAttribute

/**
 * Created by razon on 13/10/26.
 */
class TenguController extends Initializable {

  val prevDirSettingsDir = Paths.get(System.getProperty("user.home") + "/.tengu/")
  val prevDirSettingsFile = Paths.get(prevDirSettingsDir + ".prevDir")
  val encoding = "UTF-8"

  @FXML
  val webView: WebView = null
  @FXML
  val chooseDir: Button = null

  private lazy val webEngine = webView.getEngine
  private lazy val dirChooser = new DirectoryChooser

  override def initialize(url: URL, rb: ResourceBundle) {
    Files.createDirectories(prevDirSettingsDir)
    if (!Files.exists(prevDirSettingsFile)) Files.createFile(prevDirSettingsFile)
  }

  private var svr: PicturShowServer = PicturShowServer(null)

  @FXML
  def open(e: ActionEvent):Unit = {
    new String(Files.readAllBytes(prevDirSettingsFile), encoding) match {
      case s: String if 0 < s.length => dirChooser.setInitialDirectory(new File(s))
      case _ => {}
    }
    dirChooser.showDialog(null) match {
      case d: File => svr.start(d.getPath).fold({ _ =>
        }, { svr =>
          Files.write(prevDirSettingsFile, d.toString.getBytes(encoding))
          this.svr = svr
          webEngine.load("http://localhost:3000")
          webView.requestFocus()
        })
      case _ => {}
    }
  }
}
