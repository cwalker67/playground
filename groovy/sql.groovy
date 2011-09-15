import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

import groovy.sql.Sql

cli = new CliBuilder(usage: "${this.class.name} <options>", header: "options:")
cli.with {
   _(longOpt: "user", argName: "username", args: 1, "user name", required: true)
   _(longOpt: "password", argName: "password", args: 1, "password", required: true)
}

if (! (opt = cli.parse(args))) return

Sql sql = Sql.newInstance( 'jdbc:mysql://localhost:3306/ofbiz', "${opt.user}",
                       "${opt.password}", 'com.mysql.jdbc.jdbc2.optional.MysqlXADataSource' )

sql.eachRow('select * from ofbiz.PARTY') {
   println "${it}"
}


