package org.grimrose.sandbox.scala

import fr.janalyse.ssh.{SSH, SSHOptions}
import org.scalatest._
import scalikejdbc._

class SshPortForwardSpec
    extends FunSpec
    with DiagrammedAssertions
    with Matchers
    with OptionValues
    with GivenWhenThen {

  it("ssh port forward") {

    Given("ssh connect to remote")
    val options = SSHOptions(
      host = "127.0.0.1",
      port = 10022,
      username = "root",
      password = "screencast"
    )

    SSH.once(options) { ssh =>
      When("start port forward")
      info("ssh <remote user>@<remote IP> -p <remote ssh port> -f -N -L 13306:127.0.0.1:3306")
      val lp = ssh.remote2Local(13306, "127.0.0.1", 3306)

      Then("port allocated:%s".format(lp))
      assert(lp === 13306)

      Then("jdbc connecting")
      ConnectionPool.singleton(
        url = "jdbc:mysql://127.0.0.1:13306/",
        user = "root",
        password = ""
      )

      And("jdbc connected")
      assert(ConnectionPool.isInitialized())

      Then("call query")
      val actual = DB readOnly { implicit s =>
        sql"SELECT 1 AS one".toMap().single().apply()
      }

      And("query executed")
      assert(actual.value === Map("one" -> 1))

      Then("connection close")
      ConnectionPool.close()

      And("connection closed")
      assert(ConnectionPool.isInitialized() === false)
    }

  }

}
