package drupal

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import assertions._

class UserSimulation extends Simulation {

    val userOption: Int = Integer.getInteger("users", 1).toInt
    val timeOption: Int = Integer.getInteger("time", 1).toInt
    val webheads = System.getProperty("webheads", "http://127.0.0.1").split(",")

    val scns = webheads.map(scenarioFactory.createUserScenario(_).inject(ramp(userOption) over timeOption))

    //setUp(scn.inject(ramp(userOption) over (timeOption)))
    setUp(scns(0), scns.drop(1):_*)
        .protocols(Protocols.httpProtocol)
        .assertions(
            global.successfulRequests.percent.is(100),
            global.responseTime.mean.lessThan(1000))
}
