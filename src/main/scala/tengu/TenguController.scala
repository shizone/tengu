package tengu

import javafx.stage.{WindowEvent, Stage, DirectoryChooser}
import javafx.scene.web.WebView
import javafx.fxml.{FXMLLoader, Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.{ToolBar, Button}
import javafx.event.{EventHandler, ActionEvent}
import java.nio.file.{Paths, Files}
import java.io.{StringWriter, StringReader, File}
import javafx.scene.input.{KeyEvent, MouseEvent}
import javafx.animation.{Timeline, KeyValue, KeyFrame}
import javafx.util.Duration
import javafx.beans.value.{ChangeListener, ObservableValue, WritableValue}
import javafx.scene.{SnapshotParameters, Parent, Scene}
import javafx.concurrent.Worker.State
import javax.swing.text.html.{HTMLEditorKit, HTMLDocument}

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
  private lazy val noteLoader = new FXMLLoader(getClass.getResource("/fxml/Note.fxml"))

  private val prevDirSettingsDir = Paths.get(System.getProperty("user.home") + "/.tengu/")
  private val prevDirSettingsFile = Paths.get(prevDirSettingsDir + ".prevDir")
  private val encoding = "UTF-8"

  private var svr: PicturShowServer = PicturShowServer(null)
  var noteStage: Stage = null

  override def initialize(url: URL, rb: ResourceBundle) {
    Files.createDirectories(prevDirSettingsDir)
    if (!Files.exists(prevDirSettingsFile)) Files.createFile(prevDirSettingsFile)
  }

  def stop = svr.stop

  @FXML
  def menuVisible(e: MouseEvent) {
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
  def open(e: ActionEvent) {
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

  @FXML
  def webClick(e: MouseEvent) {updateNote}
  @FXML
  def webKeyPress(e: KeyEvent):Unit = {updateNote}

  @FXML
  def note(e: ActionEvent) = {
    if (noteStage == null && svr.svr != null) {
      noteStage = new Stage
      noteStage.setTitle("Note")
      noteStage.setResizable(false)
      noteStage.setFullScreen(false)
      noteStage.setScene(new Scene(noteLoader.load().asInstanceOf[Parent]))
      noteStage.show
      noteStage.setOnCloseRequest(new EventHandler[WindowEvent] {
        def handle(p1: WindowEvent) {
          noteStage.close
          noteStage = null
        }
      })
      updateNote
    }

  }

  private def updateNote {
    if (noteStage != null && svr.svr != null) {
      new Timeline(
        new KeyFrame(Duration.millis(500), new EventHandler[ActionEvent]() {
          override def handle(e: ActionEvent) {
            val id = webEngine.executeScript("location.hash").toString match {
              case s: String if s.head == '#' => s.tail.mkString
              case _ => "0"
            }
            val bodyHTML = webEngine.executeScript("document.getElementById('slide-"+ id +"').innerHTML").toString
            val noteController = noteLoader.getController[NoteController]
            noteController.note.setText(bodyHTML)
            noteController.image.setImage(webView.getScene.snapshot(null))
            noteController.image.setFitWidth(noteController.image.getScene.getWidth)
            noteController.image.setFitHeight(noteController.image.getScene.getHeight)
          }
        })
      ).play

    }
  }
}
