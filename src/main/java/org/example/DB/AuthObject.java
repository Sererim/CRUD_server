package org.example.db;

/**
 * Auth class for getting database credentials from json file.
 */
final public class AuthObject {
  final private String url;
  final private String username;
  final private String password;

  public AuthObject(String url, String username, String password) {
    this.url      = url;
    this.username = username;
    this.password = password;
  }

  // Package visible getters.

  String getUrl() {
    return this.url;
  }

  String getUsername() {
    return this.username;
  }

  String getPassword() {
    return this.password;
  }

}
