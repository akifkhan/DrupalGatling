package drupal
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import bootstrap._

object scenarioFactory {
    val headers_1 = Map(
        "Keep-Alive" -> "115")

    def createUserScenario(webhead: String): io.gatling.core.structure.ScenarioBuilder = {
        val scn = scenario("User Scenario:" + webhead)
            .exec(
                http("landing")
                .get(webhead + "/")
                .headers(headers_1)
                .check(
                    status.is(200),
                    css("input[name=form_build_id]", "value").saveAs("form_build_id"),
                    css("input[name=form_id]", "value").saveAs("form_id"),
                    css("input[name=op]", "value").saveAs("op"),
                    css("link[rel=stylesheet]", "href").findAll.saveAs("csss")))
            .foreach("${csss}", "css") {
                exec(
                    http("css")
                        .get("${css}")
                        .check(status.is(200)))
            }
            .pause(0 milliseconds, 300 milliseconds)
            .feed(csv("user_credentials.csv").circular)
            .exec(
                http("login")
                    .post(webhead + "/?q=node&destination=node")
                    .headers(headers_1)
                    .param("name", "${username}")
                    .param("pass", "${password}")
                    .param("form_build_id", "${form_build_id}")
                    .param("form_id", "${form_id}")
                    .param("op", "${op}")
                    .check(status.is(302)))
            .pause(0 milliseconds, 300 milliseconds)
            .exec(
                http("account")
                    .get(webhead + "/?q=user")
                    .check(status.is(200)))
            .pause(0 milliseconds, 300 milliseconds)            
            .exec(
                http("logout")
                    .get(webhead + "/?q=user/logout")
                    .headers(headers_1)
                    .check(status.is(302)))

        return scn
    }
}
