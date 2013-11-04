package tengu

import javafx.stage.DirectoryChooser
import javafx.scene.web.WebView
import javafx.fxml.{Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.{ToolBar, Button}
import javafx.event.ActionEvent
import java.nio.file.{Paths, Files}
import java.io.File
import javafx.scene.input.MouseEvent
import javafx.animation.{Timeline, KeyValue, KeyFrame}
import javafx.util.Duration
import javafx.beans.value.WritableValue

/**
 * Created by razon on 13/10/26.
 */
class TenguController extends Initializable {

  @FXML
  val toolBar: ToolBar = null
  @FXML
  val webView: WebView = null
  @FXML
  val chooseDir: Button = null

  private lazy val webEngine = webView.getEngine
  private lazy val dirChooser = new DirectoryChooser

  private val prevDirSettingsDir = Paths.get(System.getProperty("user.home") + "/.tengu/")
  private val prevDirSettingsFile = Paths.get(prevDirSettingsDir + ".prevDir")
  private val encoding = "UTF-8"

  private var svr: PicturShowServer = PicturShowServer(null)

  override def initialize(url: URL, rb: ResourceBundle) {
    Files.createDirectories(prevDirSettingsDir)
    if (!Files.exists(prevDirSettingsFile)) Files.createFile(prevDirSettingsFile)
  }

  def stop = svr.stop

  @FXML
  def menuVisible(e: MouseEvent):Unit = {
    val isDisplay = (e.getSceneX >= webView.getScene.getWidth - 100)
    (toolBar.getTranslateX match {
      case 0.0 => if (isDisplay) None else Some(0.0, toolBar.getWidth)
      case _ => if (isDisplay) Some(toolBar.getWidth, 0.0) else None
      }).foreach({width =>
      new Timeline(
        new KeyFrame(Duration.ZERO, new KeyValue(toolBar.translateXProperty().asInstanceOf[WritableValue[Any]], width._1)),
        new KeyFrame(Duration.millis(200), new KeyValue(toolBar.translateXProperty().asInstanceOf[WritableValue[Any]], width._2))
      ).play
    })
  }

  @FXML
  def open(e: ActionEvent):Unit = {
    new String(Files.readAllBytes(prevDirSettingsFile), encoding) match {
      case s: String if 0 < s.length => dirChooser.setInitialDirectory(new File(s))
      case _ => {}
    }
    dirChooser.showDialog(null) match {
      case d: File => svr.start(d.getPath).fold(
      { _ =>  },
      { svr =>
          Files.write(prevDirSettingsFile, d.toString.getBytes(encoding))
          this.svr = svr
          webEngine.load("http://localhost:3000")
          webView.requestFocus
        })
      case _ => {}
    }
  }
}
