
1. compile and run-app
-------------

```
$./sbt compile
```

```
$./sbt
>container:start
```

2.1 es indexing
---------------

https://github.com/iPrayag/gccount/tree/master/scripts

2.2 check api
---------------
```
$ curl http://localhost:8080/api/moonmarket
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
