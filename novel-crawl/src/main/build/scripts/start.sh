#!/bin/bash

ENGINE=novel-crawl.jar
cd ../

#部署目路
DEPLOY_DIR=`pwd`
#获取到当前目录的名称
SERVER_NAME=`basename $DEPLOY_DIR`

#应用进程
PIDS=`ps -ef | grep java | grep "$ENGINE" |awk '{print $2}'`
#设置日志文件的输出目录
LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
#日志
STDOUT_FILE=$LOGS_DIR/stdout.log
#JAVA 环境配置
JAVA_OPTS=" -Djava.net.preferIPv4Stack=true -Dlog.home=$LOGS_DIR"

JAVA_MEM_OPTS=" -server -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:InitiatingHeapOccupancyPercent=50 -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -XX:+PrintHeapAtGC -Xloggc:$LOGS_DIR/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof "
#退出标志
RETVAL="0"

if [ -n "$PIDS" ]; then
		echo "ERROR: The $SERVER_NAME already started!"
		echo "PID: $PIDS"
		exit $RETVAL
fi

nohup java -jar  $JAVA_OPTS $JAVA_MEM_OPTS  -Dloader.path=conf,lib $ENGINE > $STDOUT_FILE 2>&1 &
COUNT=0
	while [ $COUNT -lt 1 ]; do
		echo -e ".\c"
		sleep 1
		COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
		if [ $COUNT -gt 0 ]; then
			break
		fi
	done

	echo "OK!"
	PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
	echo "PID: $PIDS"
	echo "STDOUT: $STDOUT_FILE"
