package command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import entity.Article;

public class ArticleInvoker {
    private final ExecutorService executorService;

    public ArticleInvoker(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Execute commands.
     * @param commands commands
     * @return Article articles
     * @throws InterruptedException exception
     */
    public List<Article> executeCommands(List<ArticleCommand> commands) throws InterruptedException {
        final List<Callable<Article>> callables = commands.stream()
                .map(cmd -> (Callable<Article>) cmd::execute)
                .toList();

        final List<Article> articles = new ArrayList<>();
        try {
            final List<Future<Article>> futures = executorService.invokeAll(callables);
            for (Future<Article> future : futures) {
                try {
                    final Article article = future.get();
                    if (article != null) {
                        articles.add(article);
                    }
                }
                catch (InterruptedException executionException) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread was interrupted: " + executionException.getMessage());
                    // Optionally, you can decide to rethrow or handle it as needed
                }
                catch (ExecutionException executionException) {
                    System.err.println("Error executing command: " + executionException.getCause().getMessage());
                    // Handle specific causes if necessary
                }
            }
        }
        finally {
            shutdown();
        }

        return articles;
    }

    private void shutdown() {
        executorService.shutdown();
    }
}
