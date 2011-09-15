import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;


cli = new CliBuilder(usage: "${this.class.name} <options>", header: "options:")
cli.with {
   _(longOpt: "iterations", argName: "num", args: 1, "number of iterations to run (default: 1)")
   _(longOpt: "threads", argName: "num", args: 1, "number of threads (default: 1")
}

if (! (opt = cli.parse(args))) return

iterations = (opt.iterations ?: "1") as int
threadCount = (opt.threads ?: "1") as int

ExecutorService execSvc = Executors.newFixedThreadPool(threadCount);
Collection<Callable<String>> tasks = new ArrayList<Callable<Boolean>>()

def executeThreads = { Collection<Callable<String>> executeTasks ->
   List<Future<String>> results = execSvc.invokeAll(executeTasks, 300, TimeUnit.SECONDS);

   results.each { Future<String> i ->
      println i.get();
   }

}

try {

   threadCount.times {
      Callable<String> callable = new CallableProcessor()
      tasks.add(callable)
   }

   startTime = new Date()
   println "Started at ${startTime}"
   iterations.times { i ->
       println "Iteration: ${i}"
       executeThreads(tasks) 
   }
   endTime = new Date()
   numSecs = endTime.getTime() - startTime.getTime()
   println "Ended at ${endTime}, running ${numSecs/1000} seconds"

} catch (Exception ex) {
   ex.printStackTrace();

} finally {
   execSvc.shutdownNow()
}




private class CallableProcessor implements Callable<String> {
   private Random random = new Random();
 
   public String call() throws Exception {
      long sleep = random.nextInt(10);
      Thread.sleep(sleep * 1000);
      return "${sleep}";
   }

}


