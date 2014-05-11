
##start elasticsearch
#/usr/local/elasticsearch-1.0.0/bin/elasticsearch

echo "[info] : configuring mysql "
echo ""
./mysqlBootstrap.sh

##start ampq
#rammitmq-server

#start sbt console
./sbt
