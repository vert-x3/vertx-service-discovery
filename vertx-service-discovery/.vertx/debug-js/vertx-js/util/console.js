var stdout = java.lang.System.out;
var stderr = java.lang.System.err;

/**
 * A simple console object that can be used to print log messages
 * errors, and warnings.
 * @example
 *
 * console.log('Hello standard out');
 * console.warn('Warning standard error');
 * console.error('Alert! Alert!');
 *
 */
var console = {

  /**
   * Log the msg to STDOUT.
   *
   * @param {string} msg The message to log to standard out.
   */
  log: function(msg) {
    stdout.println(msg);
  },

  /**
   * Log the msg to STDERR
   *
   * @param {string} msg The message to log with a warning to standard error.
   */
  warn: function(msg) {
    stderr.println(msg);
  },

  /**
   * Log the msg to STDERR
   *
   * @param {string} msg The message to log with a warning alert to standard error.
   */
  error: function(msg) {
    stderr.println(msg);
  }
};

module.exports = console;
