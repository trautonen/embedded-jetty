Embedded Jetty
==============

[![Build Status](https://travis-ci.org/trautonen/embedded-jetty.png?branch=master)](https://travis-ci.org/trautonen/embedded-jetty)

Concise interface and command line tool to bootstrap embedded
[Jetty](http://www.eclipse.org/jetty/) server. Due to implementation details the required Jetty
version must be at least **9.0.1**.

The main reason behind yet another embedded Jetty wrapper is that there is no really simple
library available that allows bootstrapping a Jetty server from classpath resources using Servlet 3
annotations inside your favourite IDE. This wrapper is designed to be really simple and aimed for
mostly developing purposes, but nothing prevents it to be used as a production server too.

While the wrapper leverages all the normal bootstrapping mechanisms, it also provides a concise
interface to load static resources from classpath and load your web application from classpath or
JAR only, without using the normal WAR packaging.


### Server

The main server access point is the `EmbeddedJetty` class which is configured using the
`ServerConfig`.

Bootstrapping the server using Servlet 3 annotations is as simple as:

```java
ServerConfig serverConfig = new ServerConfig().setClassPath(true);
new EmbeddedJetty(serverConfig).run();
```

The server configuration can be altered with the `ServerConfig` and more customization of the
server initialization can be done by customizing the `ServerFactory`.

```java
ServerConfig serverConfig = new ServerConfig()
        .setPort(9999)
        .setPlus(true)
        .setBaseResource("classpath:/resources")
        .setDefaultServlet(true)
        .setClassPath(true);
new EmbeddedJetty(serverConfig) {
    @Override
    protected ServerFactory createServerFactory() {
        return new MyCustomServerFactory(serverConfig);
    }
}.run();
```


### Command line

The command line interface is started from `EmbeddedJettyCli` and bundled as a shaded JAR with all
the required dependencies inside. All the `ServerConfig` settings are mapped as command line
options.

Running the command line server can be done as follows:

```shell 
java -jar embedded-jetty-cli-<version>-shaded.jar -port 9999 -webApp /path/to/webapp.war
```


### License

The project Embedded Jetty is licensed under the MIT license.
