package org.grimrose.sandbox.groovy

import groovy.sql.Sql
import org.hidetake.groovy.ssh.Ssh
import spock.lang.Specification

class SshPortForwardSpec extends Specification {

    def "ssh port forwarding"() {
        given: "ssh connect to remote"
        def ssh = Ssh.newService()
        ssh.remotes {
            app {
                host = '127.0.0.1'
                port = 10022
                user = 'root'
                password = 'screencast'
                knownHosts = allowAnyHosts
            }
        }

        ssh.run {
            session(ssh.remotes.app) {
                when: "start port forward"
                // ssh <remote user>@<remote IP> -p <remote ssh port> -f -N -L 13306:127.0.0.1:3306
                forwardLocalPort port: 13306, host: '127.0.0.1', hostPort: 3306

                then: "jdbc connecting"
                def url = "jdbc:mysql://127.0.0.1:13306/"
                def db = Sql.newInstance(url, "root", "")

                and: "jdbc connected"
                !db.connection.closed

                then: "call query"
                def result = db.firstRow("SELECT 1 AS one")

                and: "query executed"
                result == [one: 1]

                then: "jdbc connection close"
                db.close()

                and: "jdbc connection closed"
                db.connection.closed
            }
        }

    }

}
