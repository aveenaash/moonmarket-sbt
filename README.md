
![hulaki](https://github.com/iPrayag/moonmarket-sbt/raw/master/hulaki.jpg)

1. configure mysql/ compile and run-app
-----------------------------

```
$./run-app.sh
>compile
>container:start
>~ ;copy-resources;aux-compile
```

2.1 [es indexing](https://github.com/iPrayag/moonmarket-sbt/tree/master/scripts)
--------------------------------------------------------------------------------

```
$./esBoostrap.sh
$ cd scripts
$./bootstrap.sh
```

2.2 check api
---------------
```
$ curl http://localhost:8080/api/tweets
```

