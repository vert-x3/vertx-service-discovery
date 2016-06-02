/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
// Since we intend to use the Function constructor.
/* jshint evil: true */

/*
 NOTE:
 This is an adapted version of jvm-npm.js from the project
 https://github.com/nodyn/jvm-npm
 */

module = (typeof module == 'undefined') ? {} : module;

// Java.synchronized is used in jvm-npm.js to synchronize concurrent access to require but this is only available in
// JDK 8u45 or later. So, if not there, just throw an error.
if (typeof Java.synchronized == 'undefined') {
  throw "Please update your java virtual machine, Java 1.8.0_45+ is required by the vert.x JavaScript support";
}

(function () {
  var System = java.lang.System;
  var Scanner = java.util.Scanner;
  var File = java.io.File;

  var resolver = Packages.io.vertx.lang.js.ClasspathFileResolver;
  var verticleFactoryClass = Packages.io.vertx.lang.js.JSVerticleFactory;

  NativeRequire = (typeof NativeRequire === 'undefined') ? {} : NativeRequire;
  if (typeof require === 'function' && !NativeRequire.require) {
    NativeRequire.require = require;
  }

  function Module(id, modParent, core) {
    this.id = id;
    this.core = core;
    this.modParent = modParent;
    this.children = [];
    this.filename = id;
    this.loaded = false;

    Object.defineProperty(this, 'exports', {
      get: function () {
        return this._exports;
      }.bind(this),
      set: function (val) {
        Require.cache[this.filename] = val;
        this._exports = val;
      }.bind(this)
    });
    this.exports = {};

    if (modParent && modParent.children) modParent.children.push(this);

    this.require = function (id) {
      return Require(id, this);
    }.bind(this);
  }

  Module._load = function _load(file, modParent, core, main, strict) {
    var module = new Module(file, modParent, core);
    var __FILENAME__ = module.filename;
    var body = readFile(module.filename, module.core);
    var dir = new File(module.filename).getParent();

    var sourceURL = resolver.resolveFilename(file);
    if (!sourceURL) {
      sourceURL = file;
    }

    var moduleFunc = strict ?
      "function(exports, module, require, __filename, __dirname){\"use strict\";" + body + "\n}\n//# sourceURL=" + sourceURL :
      "function(exports, module, require, __filename, __dirname){" + body + "\n}\n//# sourceURL=" + sourceURL;

    try {
      var func = eval(moduleFunc);
    } catch (ex) {
      if (ex instanceof SyntaxError) {

        // WARNING! Large pile of Yak hair ahead!

        // This is a pain!

        // If there is a syntax error in the module the exception is set to up to reflect where the eval
        // was invoked NOT the actual syntax error - we can get the information on the real file and line number
        // by poking around in the referenced nashorn exception and cause

        // And there is no easy way in Nashorn to throw our own SyntaxError with the correct line number
        // and fileName (see Nashorn dev list discussions of 19/02/15) :((
        // So we're just going to have to settle for logging the correct line numbers here

        var ne = ex.nashornException;
        var cause = ne.cause;
        var msg = cause.message.replace("<eval>", file);
        var lineNumber = cause.lineNumber;

        System.err.println("ERROR: " + msg + " in file " + file + " at line " + lineNumber);
      }
      throw ex;
    }
    func(module.exports, module, module.require, module.filename, dir);
    module.loaded = true;
    module.main = main;
    return module.exports;
  };

  function Require(id, modParent, strict) {
    return synchronizedDoRequire(id, modParent, true, strict);
  }

  function RequireNoCache(id, modParent, strict) {
    return synchronizedDoRequire(id, modParent, false, strict);
  }

  var synchronizedDoRequire = Java.synchronized(doRequire, verticleFactoryClass);

  function doRequire(id, modParent, useCache, strict) {
    var core, native, file = Require.resolve(id, modParent);

    if (!file) {
      if (typeof NativeRequire.require === 'function') {
        if (Require.debug) {
          System.out.println(['Cannot resolve', id, 'defaulting to native'].join(' '));
        }
        native = NativeRequire.require(id);
        if (native) return native;
        System.err.println("Cannot find module " + id);
      }
      throw new Error("Cannot find module " + id);
    }
    if (file.core) {
      file = file.path;
      core = true;
    }
    if (useCache) {
      var cached = Require.cache[file];
      if (cached) {
        return cached;
      }
    }
    if (file.endsWith('.js')) {
      return Module._load(file, modParent, core, null, strict);
    } else if (file.endsWith('.json')) {
      return loadJSON(file);
    }
  }

  Require.resolve = function (id, modParent) {
    var roots = findRoots(modParent);
    for (var i = 0; i < roots.length; ++i) {
      var root = roots[i];
      var result = resolveClasspathModule(id, root) ||
        resolveAsFile(id, root, '.js') ||
        resolveAsFile(id, root, '.json') ||
        resolveAsDirectory(id, root) ||
        resolveAsNodeModule(id, root);
      if (result) {
        return result;
      }
    }
    return false;
  };

  Require.root = System.getProperty('user.dir');
  Require.NODE_PATH = undefined;

  function findRoots(modParent) {
    var r = [];
    r.push(findRoot(modParent));
    return r.concat(Require.paths());
  }

  function parsePaths(paths) {
    if (!paths) {
      return [];
    }
    if (paths === '') {
      return [];
    }
    var osName = java.lang.System.getProperty("os.name").toLowerCase();
    var separator;

    if (osName.indexOf('win') >= 0) {
      separator = ';';
    } else {
      separator = ':';
    }

    return paths.split(separator);
  }

  Require.paths = function () {
    var r = [];
    r.push(java.lang.System.getProperty("user.home") + "/.node_modules");
    r.push(java.lang.System.getProperty("user.home") + "/.node_libraries");

    if (Require.NODE_PATH) {
      r = r.concat(parsePaths(Require.NODE_PATH));
    } else {
      var NODE_PATH = java.lang.System.getenv('NODE_PATH');
      if (NODE_PATH) {
        r = r.concat(parsePaths(NODE_PATH));
      }
    }
    // r.push( $PREFIX + "/node/library" );
    return r;
  };

  function findRoot(modParent) {
    if (!modParent || !modParent.id) {
      return Require.root;
    }
    // The id may be the path, so, the split must be system dependent.
    // The id may be the require id (already normalized), so the split uses "/"
    var pathParts;
    if (modParent.id.contains(File.separator)) {
      pathParts = modParent.id.split(File.separator);
    } else {
      // This branch will only be executed on windows for nested modules.
      pathParts = modParent.id.split("/");
    }
    pathParts.pop();
    return pathParts.join('/');
  }

  Require.debug = true;
  Require.cache = {};
  Require.extensions = {};
  require = Require;
  require.noCache = RequireNoCache;
  module.exports = Module;

  function loadJSON(file) {
    var json = JSON.parse(readFile(file));
    Require.cache[file] = json;
    return json;
  }

  function resolveAsNodeModule(id, root) {
    var base = [root, 'node_modules'].join('/');
    return resolveAsFile(id, base) ||
      resolveAsDirectory(id, base) ||
      (root ? resolveAsNodeModule(id, new File(root).getParent()) : false) ||
      resolveAsClasspathNodeModule(id);
  }

  function resolveAsDirectory(id, root) {
    var base = [root, id].join('/'),
      file = new File([base, 'package.json'].join('/'));
    if (file.exists()) {
      var body = readFile(file.getCanonicalPath()),
        package = JSON.parse(body);
      if (package.main) {
        return (resolveAsFile(package.main, base) ||
        resolveAsDirectory(package.main, base));
      }
      // if no package.main exists, look for index.js
      return resolveAsFile('index.js', base);
    }
    return resolveAsFile('index.js', base);
  }

  function resolveAsFile(id, root, ext) {
    var file;
    if (id.indexOf('/') === 0) {
      file = new File(normalizeName(id, ext || '.js'));
      if (!file.exists()) {
        return resolveAsDirectory(id);
      }
    } else {
      file = new File([root, normalizeName(id, ext || '.js')].join('/'));
    }
    if (file.exists()) {
      return file.getCanonicalPath();
    }
  }

  function resolveClasspathModule(id, root) {
    var name = normalizeName(id);
    name = resolveRelative(name, root);
    var classloader = java.lang.Thread.currentThread().getContextClassLoader();
    var is = classloader.getResourceAsStream(name);
    if (is) {
      return {path: name, core: true};
    }
  }
  
  function resolveAsClasspathNodeModule(name) {
    name = 'node_modules/' + name;
    var main = name + '/index.js';
    var classloader = java.lang.Thread.currentThread().getContextClassLoader();
    if (classloader.getResource(name + '/package.json')) {
      var package = JSON.parse(readFile(name + '/package.json', true));
      if (package.main) main = name + '/' + package.main;
    }
    main = main.replace(/\.\//g,'');
    if (classloader.getResource(main)) return {path: main, core: true};
  }

  function normalizeName(fileName, ext) {
    var extension = ext || '.js';
    if (fileName.endsWith(extension)) {
      return fileName;
    }
    return fileName + extension;
  }

  function isRelative(path) {
    return path[0] === '.' && (path[1] === '/' || (path[1] === '.' && path[2] === '/'));
  }

  /**
   * Code loosely based on the getNormalizedParts() function from the
   * TypeScript compiler released under the Apache License 2.0
   * https://github.com/Microsoft/TypeScript
   */
  function resolveRelative(path, root) {
    if (!isRelative(path)) {
      return path;
    }
    var parts = (root + '/' + path).split('/');
    var absolute = [];
    for (var i = 0; i < parts.length; ++i) {
      var p = parts[i];
      if (p !== '' && p !== '.') {
        if (p == '..' && absolute.length > 0 && absolute[absolute.length - 1] !== '..') {
          absolute.pop();
        } else {
          absolute.push(p);
        }
      }
    }
    return absolute.join("/");
  }

  function readFile(filename, core) {
    var input;
    if (core) {
      var classloader = java.lang.Thread.currentThread().getContextClassLoader();
      input = classloader.getResourceAsStream(filename);
    } else {
      input = new File(filename);
    }
    var scanner = new Scanner(input);
    var res = scanner.useDelimiter("\\A").next();
    scanner.close();
    return res;
  }

  // Helper function until ECMAScript 6 is complete
  if (typeof String.prototype.endsWith !== 'function') {
    String.prototype.endsWith = function (suffix) {
      if (!suffix) return false;
      return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
  }

}());
