package org.example.DB;

final public class AuthObject {
  final private String url;
  final private String username;
  final private String password;

  public AuthObject(String url, String username, String password) {
    this.url      = url;
    this.username = username;
    this.password = password;
  }

  // Package visable getters.

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
