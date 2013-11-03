package tengu

import pictureshow.Server
import unfiltered.jetty.Server

/**
 * Created by razon on 13/11/03.
 */
case class PicturShowServer(svr: Server) {
  def start(path: String) = Server.instance(Array("-s=" + path)).fold({ err =>
    println("instance failed!")
    println(err)
    Left()
  }, { svr =>
    if (this.svr != null) this.svr.stop
    svr.start
    Right(PicturShowServer(svr))
  })

  def stop = if (this.svr != null) this.svr.stop
}
