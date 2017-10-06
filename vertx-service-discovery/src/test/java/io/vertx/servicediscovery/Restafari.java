package io.vertx.servicediscovery;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import okhttp3.*;
import org.hamcrest.Matcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A test utility to verify HTTP calls. Can be seen as a minimal version of REST Assured.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Restafari {

  public static final String DEFAULT_URI = "http://localhost";
  public static final int DEFAULT_PORT = 8080;
  public static final String DEFAULT_PATH = "";
  public static final String DEFAULT_SESSION_ID_VALUE = null;
  public static String baseURI = "http://localhost";
  public static int port = -1;
  public static String basePath = "";
  public static String rootPath;
  public static String sessionId;

  public Restafari() {
  }


  private static final OkHttpClient client;

  static {
    rootPath = DEFAULT_PATH;
    sessionId = DEFAULT_SESSION_ID_VALUE;
    client = new OkHttpClient();
    baseURI = DEFAULT_URI;
    port = DEFAULT_PORT;
  }


  public static Response get(String s) {
    return new Request().get(s);
  }

  public static Response delete(String s) {
    return new Request().delete(s);
  }

  public static Request given() {
    return new Request();
  }

  public static class Request {

    okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
    HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
    MultipartBody.Builder formBuilder;
    private RequestBody body;

    public Request() {
      try {
        URL url = new URL(baseURI);
        urlBuilder.scheme(url.getProtocol()).host(url.getHost());
        if (url.getPort() > 0) {
          urlBuilder.port(url.getPort());
        } else if (port > 0) {
          urlBuilder.port(port);
        }

        if (url.getPath() != null && !url.getPath().isEmpty()) {
          urlBuilder.addPathSegment(url.getPath());
        }

        if (basePath != null && !basePath.isEmpty()) {
          urlBuilder.addPathSegment(basePath);
        }

      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    public Request header(String key, String value) {
      builder.header(key, value);
      return this;
    }


    public Response get(String s) {
      applyPath(s);

      try {
        HttpUrl url = urlBuilder.build();
        okhttp3.Response resp = client.newCall(builder.url(url).build()).execute();
        return new Response(resp);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void applyPath(String s) {
      if (s != null) {
        String[] segments = s.split("/");
        for (String segment : segments) {
          urlBuilder.addPathSegment(segment);
        }
      }
    }

    public Request param(String key, String value) {
      urlBuilder.addQueryParameter(key, value);
      return this;
    }

    public Request formParam(String key, String value) {
      if (formBuilder == null) {
        formBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
      }
      formBuilder
        .addFormDataPart(key, value);
      return this;
    }

    public Response post(String s) {
      applyPath(s);

      try {
        HttpUrl url = urlBuilder.build();
        RequestBody payload = null;
        if (body != null) {
          payload = body;
        } else if (formBuilder != null) {
          payload = formBuilder.build();
        }
        okhttp3.Request request = builder.method("POST", payload).url(url).build();
        return new Response(client.newCall(request).execute());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Response delete(String s) {
      applyPath(s);

      try {
        HttpUrl url = urlBuilder.build();
        okhttp3.Request request = builder.method("DELETE", null).url(url).build();
        return new Response(client.newCall(request).execute());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Response put(String s) {
      applyPath(s);

      try {
        HttpUrl url = urlBuilder.build();
        RequestBody payload = null;
        if (body != null) {
          payload = body;
        } else if (formBuilder != null) {
          payload = formBuilder.build();
        }
        okhttp3.Request request = builder.method("PUT", payload).url(url).build();
        return new Response(client.newCall(request).execute());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Request body(String content) {
      body = RequestBody.create(null, content);
      return this;
    }

    public Request request() {
      return this;
    }
  }

  public static class Response {

    private final okhttp3.Response response;

    public Response(okhttp3.Response resp) {
      this.response = resp;
    }

    public Response then() {
      return this;
    }

    public Response statusCode(int i) {
      assertThat(response.code()).isEqualTo(i);
      return this;
    }

    public Response header(String key, String value) {
      assertThat(response.header(key)).isEqualToIgnoringCase(value);
      return this;
    }

    public Response extract() {
      return this;
    }


    public String asString() {
      try {
        ResponseBody body = response.body();
        if (body == null) {
          throw new RuntimeException("The body is null");
        }
        return body.string();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public int getStatusCode() {
      return response.code();
    }

    public Response body(String key, Matcher<Object> matcher) {
      try {
        JsonObject json = new JsonObject(response.body().string());
        matcher.matches(json.getMap().get(key));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return this;
    }

    public JsonArray asJsonArray() {
      try {
        return new JsonArray(response.body().string());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
