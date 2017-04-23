FROM ubuntu:16.04

RUN apt-get update && apt-get install -y \
    openssh-server \
    supervisor

RUN mkdir -p /var/run/sshd /var/log/supervisor

## @see https://docs.docker.com/engine/examples/running_ssh_service/
RUN echo 'root:screencast' | chpasswd
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH login fix. Otherwise user is kicked off after login
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

## @see http://stackoverflow.com/questions/38356219/mysql-in-docker-frozen-at-root-password-config
RUN apt-get update \
    && apt-get install -y apt-utils \
    && { \
        echo debconf debconf/frontend select Noninteractive; \
		echo mysql-community-server mysql-community-server/data-dir select ''; \
		echo mysql-community-server mysql-community-server/root-pass password ''; \
		echo mysql-community-server mysql-community-server/re-root-pass password ''; \
		echo mysql-community-server mysql-community-server/remove-test-db select false; \
	} | debconf-set-selections \
    && apt-get update && apt-get install -y mysql-server \
    && mkdir -p /var/run/mysqld \
    && chown -R mysql:mysql /var/run/mysqld \
    && chmod 777 /var/run/mysqld

COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 22

CMD ["/usr/bin/supervisord", "-n"]
