package tutorial.webapp

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom._
import org.scalajs.dom.document

object TenguJS extends JSApp {

  def main(): Unit = {}

  @JSExport
  def getCommnet(id: String): String = {
    val notes =
      if (0 < id.indexOf("/")) {
        val ids = id.split("/")
        val parent_id = ids(0).toInt
        val child_id = ids(1)
        val child = document.getElementById(child_id)
        val parent = child match {
          case null => document.querySelectorAll(".slides section")(parent_id)
          case p => p
        }
        child.querySelectorAll("aside.notes")
      } else {
        val idNum = id.toInt
        val section = (document.getElementById(id) match {
          case null => document.querySelectorAll(".slides section")(idNum)
          case s => s
        }).asInstanceOf[Element]
        section.querySelector("section") match {
          case null => section.querySelectorAll("aside.notes")
          case section => section.querySelector("section").querySelectorAll("aside.notes")
        }
      }
    notes match {
      case null => ""
      case notes => (for (i <- 0 until notes.length) yield notes.item(i)).mkString("\n")
    }
  }
}