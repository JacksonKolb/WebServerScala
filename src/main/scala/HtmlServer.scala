import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import java.io._
import java.util.Scanner
import java.lang.StringBuilder

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = 
    	path("hello") {
	    	get {
	      		complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Hi</h1>"))
	    	}
	  	}

	val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)

    while(true){

		println(s"Server online at http://localhost:8000/" +
				"\nEnter a file name, or press Q to quit...")

		val input = StdIn.readLine()

		if(input == "Q" || input == "q") {
			bindingFuture
	      	.flatMap(_.unbind()) 
	      	.onComplete(_ => system.terminate()) 
		}
		else {
			val html = string_to_file(input)
			val route = path("hello") {
	    		get {
	      			complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
	    		}
	  		}
	  		val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)
		}
    }    
  }

  def string_to_file(file_name: String): String = {
  	val path = "C:\\Users\\drewe\\Desktop\\CS390\\homework2\\src\\main\\scala\\" + file_name
  	val file = new File(path)
  	val scanner = new Scanner(file)
  	val html = ""

  	while(scanner.hasNext()) {
  		html = html + scanner.Next().ToString()
  	}

  	return html
  }
}