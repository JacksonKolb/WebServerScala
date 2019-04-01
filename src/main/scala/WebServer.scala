import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import java.io._
import java.util.Scanner

/*Used documentation and existing code from Akka Docs*/

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

	val html = configure_file

    val route =
      get {
        pathSingleSlash {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,html))
        } ~
          path("ping") {
            complete("PONG!")
          } ~
          path("crash") {
            sys.error("BOOM!")
          }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }


	def configure_file(): String = {

  	val path = "/Users/jackkolb/schoolspace/Spring 2019/CS-390V Functional Programming/WebServerScala/src/main/scala/"
	var file = new File("")

  	try {
  		file = new File( path + "basic_html.html")
  	}
  	catch  {
  		case e: Exception => println("File not found: " + path)
  	}

  	val scanner = new Scanner(file)
  	val builder = new StringBuilder()

  	while(scanner.hasNext()) {
  		builder.append(scanner.next())
  	}

  	return builder.toString()
	}
}
