package tengu.controller

import javafx.stage.{FileChooser, WindowEvent, Stage, DirectoryChooser}
import javafx.scene.web.{WebEngine, WebView}
import javafx.fxml.{FXMLLoader, Initializable, FXML}
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.control.{ToolBar, Button}
import javafx.event.{EventHandler, ActionEvent}
import java.nio.file.{Paths, Files}
import java.io.File
import javafx.scene.input.{KeyEvent, MouseEvent}
import javafx.animation.{Timeline, KeyValue, KeyFrame}
import javafx.util.Duration
import javafx.beans.value.WritableValue
import javafx.scene.{SnapshotParameters, Parent, Scene}
import tengu.helper.ControllerHelper
import ControllerHelper._

/**
 * Created by razon on 13/10/26.
 */
class TenguController extends Initializable {

  implicit var scene: Scene = null

  lazy val toolBar = lookup[ToolBar]("toolBar")
  lazy val webView = lookup[WebView]("webView")
  lazy val chooseDir = lookup[Button]("chooseDir")

  private lazy val webEngine = webView.getEngine
  private lazy val fileChooser = new FileChooser

  private val prevDirSettingsDir = Paths.get(System.getProperty("user.home") + "/.tengu/")
  private val prevDirSettingsFile = Paths.get(prevDirSettingsDir + ".prevDir")
  private val encoding = "UTF-8"

  var noteLoader: FXMLLoader = null
  var noteStage: Stage = null

  override def initialize(url: URL, rb: ResourceBundle) {
    Files.createDirectories(prevDirSettingsDir)
    if (!Files.exists(prevDirSettingsFile)) Files.createFile(prevDirSettingsFile)
  }

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
      case s: String if 0 < s.length => fileChooser.setInitialDirectory(new File(s))
      case _ => {}
    }

    fileChooser.showOpenDialog(null) match {
      case f: File => {
        Files.write(prevDirSettingsFile, f.getParent.getBytes(encoding))
        webEngine.load(f.toURI.toURL.toString)
        webView.requestFocus
      }
      case _ => {}
    }

  }

  @FXML
  def webClick(e: MouseEvent) {updateNote}
  @FXML
  def webKeyPress(e: KeyEvent) {updateNote}

  @FXML
  def note(e: ActionEvent) = {
    if (noteStage == null && !webEngine.getLocation.isEmpty) {
      noteLoader = new FXMLLoader(getClass.getResource("/fxml/Note.fxml"))
      val scene = new Scene(noteLoader.load().asInstanceOf[Parent])
      noteLoader.getController[NoteController].scene = scene
      noteStage = new Stage
      noteStage.setTitle("Note")
      noteStage.setResizable(false)
      noteStage.setFullScreen(false)
      noteStage.setScene(scene)
      noteStage.show
      noteStage.setOnCloseRequest(new EventHandler[WindowEvent] {
        def handle(p1: WindowEvent) {
          noteStage.close
          noteStage = null
          noteLoader = null
        }
      })
      updateNote
    }

  }

  private def updateNote {
    if (noteStage != null && !webEngine.getLocation.isEmpty) {
      new Timeline(
        new KeyFrame(Duration.millis(1000), new EventHandler[ActionEvent]() {
          override def handle(e: ActionEvent) {
            val id = webEngine.executeScript("url=location.href;url.substring(url.indexOf('#') + 2);").toString match {
              case s: String if s.length != 0 => s
              case _ => "0"
            }
            val comment = webEngine.executeScript(
              s"""
              var notes = document.querySelectorAll(".slides section.present aside.notes");
              if (notes.length == 0) {
                "";
              } else {
                notes[0].innerHTML;
              }
             """)
            val noteController = noteLoader.getController[NoteController]
            noteController.note.setText(id + "\n" + comment)
            noteController.image.setImage(webView.snapshot(new SnapshotParameters, null))
            noteController.image.setFitWidth(noteController.image.getScene.getWidth)
            noteController.image.setFitHeight(noteController.image.getScene.getHeight)
          }
        })
      ).play
    }
  }
}
