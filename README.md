Embedded Jetty
==============

[![Build Status](https://travis-ci.org/trautonen/embedded-jetty.png?branch=master)](https://travis-ci.org/trautonen/embedded-jetty)

Concise interface and command line tool to bootstrap embedded
[Jetty](http://www.eclipse.org/jetty/) server.


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


### License

The project Embedded Jetty is licensed under the MIT license.
