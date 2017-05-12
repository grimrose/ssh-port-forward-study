'use strict';

const assert = require('assert');
const tunnel = require('tunnel-ssh');
const mysql = require('promise-mysql');

describe('study', () => {
    it('ssh port forwarding', done => {

        const config = {
            host: '127.0.0.1',
            port: 10022,
            username: 'root',
            password: 'screencast',
            localPort: 13306,
            dstHost: '127.0.0.1',
            dstPort: 3306
        };

        // ssh <remote user>@<remote IP> -p <remote ssh port> -f -N -L 13306:127.0.0.1:3306
        tunnel(config, err => {
            assert.ok(typeof err === 'undefined');
        })
        .on('error', e => {
            done(e);
        });

        let connection;

        mysql.createConnection({
            host: '127.0.0.1',
            port: 13306,
            user: 'root',
            password: '',
            debug: true
        })
        .then(conn => {

            // connected
            assert.ok(conn.threadId !== null);

            connection = conn;

            // call query
            return connection.query('SELECT 1 AS one');
        })
        .then(rows => {

            // query executed
            assert.ok(rows.length > 0);
            assert.ok(rows[0].hasOwnProperty('one'));
            assert.ok(rows[0].one === 1);

            // connection close
            return connection.end();
        })
        .then(() => {

            // connection closed
            assert.ok(connection.connection.state === 'disconnected');

            done();
        })
        .catch(error => {
            done(error);
        });
    });
});
