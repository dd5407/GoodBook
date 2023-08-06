pids=$(ps -ef |grep goodbook|grep -v grep|awk '{print $2}')
for pid in $pids
do
	echo $pid
	kill -9 $pid
done
