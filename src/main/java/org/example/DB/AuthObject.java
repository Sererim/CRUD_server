package org.example.DB;

import java.util.List;

final public class AuthObject {
  final private String url;
  final private String username;
  final private String password;

  public AuthObject(String url, String username, String password) {
    this.url      = url;
    this.username = username;
    this.password = password;
  }

  List<String> getAuth() {
    return List.of(url, username, password);
  }

}
