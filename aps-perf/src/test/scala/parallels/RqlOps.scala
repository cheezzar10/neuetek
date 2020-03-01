package parallels

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RqlOpsSimulation extends Simulation {
	val httpProto = http.baseUrl("http://10.27.75.210:8080")

	val loginRequest = exec(http("L1 Reseller page users")
				.get("/")
				.check(regex("""location.href="(\s+)";""").find(0).saveAs("bw_id"))
	)

	val testScenario = scenario("Test1").during(5 seconds) {
		loginRequest
	}

	setUp(
		testScenario.inject(atOnceUsers(16))
	).protocols(httpProto)
}
