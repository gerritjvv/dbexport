# dbexport

db export library to a delimited compressed output.

Default includes mysql jdbc drivers as in an uberjar.  


For compiled releases see the ```release``` folder

## Usage


Type ```java -Xmx2048 -jar <jarfile> -h``` to get:  

```clojure
Usage: dbexport [options]

Options:
  -J, --jdbc jdbc-url            localhost  JDBC url ensure that the correct jdbc drivers are on the classpath
                            mysql: jdbc:mysql://localhost:3306/mysql?connectTimeout=0&socketTimeout=0&autoReconnect=true
                            postgres: jdbc:postgresql://host:port/database
  -u, --user user                root       DB user
  -p, --password password                   DB password
  -q, --query select-query                  db select query to use in the export
  -f, --file filename                       The file to output the data to
  -c, --compression compression             Can be clojure.lang.LazySeq@14bddd8a
  -D, --delimiter delimiter      ,          What field separator default is ,
  -E, --quote quote                         Character to use to enclose fields default is nil
  -h, --help

```

To include other database drivers used the java ```-cp``` or ```--classpath``` option.

## License

Copyright © 2015 gerritjvv

Distributed under the Eclipse Public License either version 1.0