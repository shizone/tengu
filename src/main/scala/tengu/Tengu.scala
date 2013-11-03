package tengu

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{WindowEvent, Stage}
import javafx.event.EventHandler

class Tengu extends Application {

  override def start(primaryStage: Stage) {
    val loader = new FXMLLoader(getClass.getResource("/fxml/Tengu.fxml"))
    primaryStage.setTitle("Tengu")
    primaryStage.setScene(new Scene(loader.load().asInstanceOf[Parent]))
    primaryStage.show
    primaryStage.setOnCloseRequest(new EventHandler[WindowEvent] {
      def handle(p1: WindowEvent) = loader.getController.asInstanceOf[TenguController].stop
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

