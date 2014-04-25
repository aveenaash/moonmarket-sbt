id_="4zBbw25bRymZOYmwH7HBeg"
id="3PcaDri9Rz2LDOLtO_J3JA"
curl -XPOST "localhost:9200/gccount/Api/$id/_update" -d '{
    "script" : "ctx._source.responses+=respParam",
        "params" : {
              "respParam":{"responseId": "123","response": "tweets","status":"CORRECT"}
         }
}'
