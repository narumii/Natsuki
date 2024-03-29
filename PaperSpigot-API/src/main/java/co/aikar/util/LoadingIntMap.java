/*
 * Copyright (c) 2015. Starlis LLC / dba Empire Minecraft
 *
 * This source code is proprietary software and must not be redistributed without Starlis LLC's approval
 *
 */
package co.aikar.util;


import com.google.common.base.Function;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Allows you to pass a Loader function that when a key is accessed that doesn't exist,
 * automatically loads the entry into the map by calling the loader Function.
 * <p>
 * .get() Will only return null if the Loader can return null.
 * <p>
 * You may pass any backing Map to use.
 * <p>
 * This class is not thread safe and should be wrapped with Collections.synchronizedMap on the
 * OUTSIDE of the LoadingMap if needed.
 * <p>
 * Do not wrap the backing map with Collections.synchronizedMap.
 *
 * @param <V> Value
 */
public class LoadingIntMap<V> extends TIntObjectHashMap<V> {

  private final Function<Integer, V> loader;

  /**
   * Initializes an auto loading map using specified loader and backing map
   *
   * @param loader The loader
   */
  public LoadingIntMap(Function<Integer, V> loader) {
    this.loader = loader;
  }


  @Override
  public V get(int key) {
    V res = super.get(key);
    if (res == null) {
      res = loader.apply(key);
      if (res != null) {
        put(key, res);
      }
    }
    return res;
  }

  /**
   * Due to java stuff, you will need to cast it to (Function) for some cases
   */
  public abstract static class Feeder<T> implements Function<T, T> {

    @Override
    public T apply(Object input) {
      return apply();
    }

    public abstract T apply();
  }
}
