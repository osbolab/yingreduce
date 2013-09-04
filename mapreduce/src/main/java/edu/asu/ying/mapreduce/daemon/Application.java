package edu.asu.ying.mapreduce.daemon;

/**
 * The main entry point for the node daemon. {@code Application} starts the table, scheduling, and
 * interface services before attaching the local node to an existing Kademlia network.
 */
public class Application {

  /**
   * Daemon entry point.
   */
  public static void main(final String[] args) {
    // TODO: Logging
    System.out.println(
        "YingReduce 0.2.1\nCopyright \u00A9 2013 Ying Lab, Arizona State University");
    System.out.println("For help contact Matthew Barnard: msbarnar@gmail.com");
    System.out.println();

    final Application app = new Application(args);
    app.start();
  }

  /**
   * Initializes the appropriate services, but does not start them.
   */
  private Application(final String[] args) {
    // TODO: Logging
    System.out.println("Getting things ready...");
  }

  /**
   * Starts the initialized services, transitioning the daemon to the {@code Running} state.
   */
  private void start() {
    // TODO: Logging
    System.out.println("Starting the application...");

    final Daemon instance1 = new Daemon(5000);

    System.out.println("... and we're rolling!");
    System.out.println();
    System.out.println("Visit http://localhost:8887/ to administer the local node.\n\n");
  }
}
