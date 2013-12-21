package tengu

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{WindowEvent, Stage}
import javafx.event.EventHandler
import tengu.controller.TenguController

class Tengu extends Application {

  override def start(primaryStage: Stage) {
    val loader = new FXMLLoader(getClass.getResource("/fxml/Tengu.fxml"))
    val scene = new Scene(loader.load().asInstanceOf[Parent])
    loader.getController[TenguController].scene = scene
    primaryStage.setTitle("Tengu")
    primaryStage.setScene(scene)
    primaryStage.show
    primaryStage.setOnCloseRequest(new EventHandler[WindowEvent] {
      def handle(p1: WindowEvent) = {
        val primaryController = loader.getController.asInstanceOf[TenguController]
        val noteStage = primaryController.noteStage
        if (noteStage != null) noteStage.close
      }
    })
  }

  def launch(args: Array[String]) {
    Application.launch(args: _*)
  }
}

object Tengu {
  def main(args: Array[String]) {
    new Tengu launch args
  }
}

