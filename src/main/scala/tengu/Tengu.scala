package tengu

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Tengu extends Application {
  @Override
  def start(primaryStage: Stage) {
    primaryStage.setTitle("Tengu")
    primaryStage.setScene(new Scene(FXMLLoader.load(getClass.getResource("/fxml/Tengu.fxml"))))
    primaryStage.show
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

