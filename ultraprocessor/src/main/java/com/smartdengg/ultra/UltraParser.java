package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.Http;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 创建时间: 2017/03/23 12:13 <br>
 * 作者: dengwei <br>
 * 描述:
 */
public class UltraParser<Request> {

  private Request request;

  public static <Request> UltraParser<Request> createWith(Request request) {

    Utils.checkNotNull(request, "request == null");

    Class<?> clazz = request.getClass();
    Http http = clazz.getAnnotation(Http.class);
    if (http == null) {
      throw Utils.classError(null, clazz, "%s lack of @Http annotation", clazz.getName());
    }

    return new UltraParser<>(request);
  }

  private UltraParser(Request request) {
    this.request = request;
  }

  public Flowable<RequestEntity<Request>> parseAsFlowable() {
    try {
      return new RequestEntityBuilder<>(request).build().asFlowable();
    } catch (Exception e) {
      RxJavaPlugins.onError(e);
      return Flowable.error(e);
    }
  }

  public Observable<RequestEntity<Request>> parseAsObservable() {
    try {
      return new RequestEntityBuilder<>(request).build().asObservable();
    } catch (Exception e) {
      RxJavaPlugins.onError(e);
      return Observable.error(e);
    }
  }
}
