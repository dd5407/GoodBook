pids=$(ps -ef |grep xiaogu_word|grep -v grep|awk '{print $2}')
for pid in $pids
do
	echo $pid
	kill -9 $pid
done
