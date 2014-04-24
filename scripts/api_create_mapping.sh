curl -XPUT localhost:9200/gccount/Api/_mapping -d '{
    "Api" : {
                  "settings" : {
                  }, 
                  "properties"   : {
                    "requestId"     : { "type":"String" , "index" : "not_analyzed"}, 
                    "documentIndex" : { "type":"String"}, 
                    "documentCount" : { "type":"Integer"}, 
                    "module"   : { "type":"String" , "index" : "not_analyzed"}, 
                    "request"  : { "type":"String" , "index" : "not_analyzed"}, 
                    "user"     : { "type":"String" , "index" : "not_analyzed"},
                    "created": {
                         "type"   : "date",
                         "format" : "yyyy-MM-dd HH:mm:ss"
                     },
                     "responses": {
                       "type": "nested",
                       "properties": {
                          "responseId": {
                             "type"  : "string", "index" : "not_analyzed"
                           },                           
                          "nestedCount" : { "type":"Integer"}, 
                          "createdDate": {
                             "type"   : "date",
                             "format" : "dateOptionalTime"
                          },
                          "response"  : {"type" : "string"},
                          "status" : {
                              "type" : "string"
                          }
                      } 
                   }
              }
      }
}'
