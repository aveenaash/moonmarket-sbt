curl -XDELETE 'http://localhost:9200/gccount/Api/_mapping'
echo "Api mapping deleted"

curl -XDELETE 'http://localhost:9200/gccount/'
echo "gccount index deleted"
echo

