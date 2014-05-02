
1. configure mysql/ compile and run-app
-----------------------------

```
$./run-app.sh
>compile
>container:start
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

3. start RabbitMQ
---------------

```
prayagupd@prayagupd:~/backup/JVM/rabbitmq_server-3.2.4$ sbin/rabbitmq-server
```

4. scala breeze NLP hacks
-------------------------------

```
$./sbt
> run-main zazzercode.BreezeGibbs
```

References
---------------
[Hacking on liftweb 2.5 and sbt 0.12](http://prayag-waves.blogspot.com/2012/11/hacking-on-liftweb-25-and-sbt-012.html)
