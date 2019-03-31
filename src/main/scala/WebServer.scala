import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import java.io._
import java.util.Scanner

object WebServer {

	def main(args: Array[String]) {
	implicit val system = ActorSystem()
	implicit val materializer = ActorMaterializer()
	implicit val executionContext = system.dispatcher

	val html = configure_file()

	val requestHandler: HttpRequest => HttpResponse = {
		case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
		HttpResponse(entity = HttpEntity(
			ContentTypes.`text/html(UTF-8)`,
			html))

		case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
		HttpResponse(entity = "PONG!")

		case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
		sys.error("BOOM!")

		case r: HttpRequest =>
		r.discardEntityBytes() // important to drain incoming HTTP Entity stream
		HttpResponse(404, entity = "Unknown resource!")
	}

	val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8080)
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
