FROM centos:7
MAINTAINER Neville Bonavia
ENV container docker
ENV SCALA_VERSION 2.10.5
ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk
ENV JRE_HOME /usr/lib/jvm/jre
ENV MAVEN_HOME /opt/apache/maven
ENV VALIDATOR_HOME /opt/betsson/validator
ENV APPLICATION_HOME /opt/betsson/application

RUN (cd /lib/systemd/system/sysinit.target.wants/; for i in *; do [ $i == \
systemd-tmpfiles-setup.service ] || rm -f $i; done); \
rm -f /lib/systemd/system/multi-user.target.wants/*;\
rm -f /etc/systemd/system/*.wants/*;\
rm -f /lib/systemd/system/local-fs.target.wants/*; \
rm -f /lib/systemd/system/sockets.target.wants/*udev*; \
rm -f /lib/systemd/system/sockets.target.wants/*initctl*; \
rm -f /lib/systemd/system/basic.target.wants/*;\
rm -f /lib/systemd/system/anaconda.target.wants/*;\
mkdir -p ${VALIDATOR_HOME};\
mkdir -p ${APPLICATION_HOME};
VOLUME [ "/sys/fs/cgroup" ]

COPY validator/ ${VALIDATOR_HOME}/
COPY application/ ${APPLICATION_HOME}/

RUN timedatectl set-timezone Europe/Amsterdam; \
    curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.2/install.sh | bash; \
    source /root/.bashrc; \
    nvm install v7; \
    echo "nvm use v7 >/dev/null" >> /root/.bashrc; \
    nvm use v7; \

    cd $VALIDATOR_HOME; \
    npm install; \

    yum install -y java-1.8.0-openjdk.x86_64 \
                    wget ;\
    cd /root ;\
    wget http://downloads.lightbend.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.rpm ;\
    yum install -y ./scala-${SCALA_VERSION}.rpm ;\
    rm ./scala-${SCALA_VERSION}.rpm ;\

    mkdir -p /opt/apache/ ;\
    cd /opt/apache ;\
    wget wget http://www-eu.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz ;\
    tar -zxvf ./apache-maven-3.3.9-bin.tar.gz ;\
    rm ./apache-maven-3.3.9-bin.tar.gz ;\
    ln -s ./apache-maven-3.3.9 maven ;\
    echo "PATH=${PATH}:${MAVEN_HOME}/bin" > /etc/profile.d/maven.sh ;\

    echo "Finished."

EXPOSE 8080

CMD ${VALIDATOR_HOME}/run.sh