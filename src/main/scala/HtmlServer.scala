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

    implicit val system = ActorSystem("datboi")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    while (true) {

    	println("Hey betch, enter a file name or press Q to quit: ")		
		
		val input = StdIn.readLine()

		if (input.equals("Q") || input.equals("q")) {
			System.exit(0)
		}

		val html = string_to_file(input)
		println(html)			
			
		val route = path("hello") {
    		get {
      			complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
    		}
  		}
  		val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)
	
  		println(s"Server online at http://localhost:8000/")

  		StdIn.readLine()
    }    
  }


  def string_to_file(file_name: String): String = {

  	val path = "C:\\Users\\drewe\\Desktop\\ScalaServer\\src\\main\\scala\\" + file_name
  	var file = new File("")

  	try {
  		file = new File(path)
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