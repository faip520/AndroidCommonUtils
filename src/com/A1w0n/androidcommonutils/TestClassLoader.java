package com.A1w0n.androidcommonutils;

import dalvik.system.DexClassLoader;

/**
 *
 *
 * Created by A1w0n on 14/11/20.
 */
public class TestClassLoader extends DexClassLoader {

    public TestClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }


}
