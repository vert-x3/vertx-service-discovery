/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-js/file_system */
var utils = require('vertx-js/util/utils');
var AsyncFile = require('vertx-js/async_file');
var Buffer = require('vertx-js/buffer');
var FileSystemProps = require('vertx-js/file_system_props');
var FileProps = require('vertx-js/file_props');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JFileSystem = io.vertx.core.file.FileSystem;
var OpenOptions = io.vertx.core.file.OpenOptions;

/**
 Contains a broad set of operations for manipulating files on the file system.
 <p>
 A (potential) blocking and non blocking version of each operation is provided.
 <p>
 The non blocking versions take a handler which is called when the operation completes or an error occurs.
 <p>
 @class
*/
var FileSystem = function(j_val) {

  var j_fileSystem = j_val;
  var that = this;

  /**
   Copy a file from the path <code>from</code> to path <code>to</code>, asynchronously.
   <p>
   The copy will fail if the destination already exists.

   @public
   @param from {string} the path to copy from 
   @param to {string} the path to copy to 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.copy = function(from, to, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["copy(java.lang.String,java.lang.String,io.vertx.core.Handler)"](from, to, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#copy}

   @public
   @param from {string} 
   @param to {string} 
   @return {FileSystem}
   */
  this.copyBlocking = function(from, to) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["copyBlocking(java.lang.String,java.lang.String)"](from, to);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Copy a file from the path <code>from</code> to path <code>to</code>, asynchronously.
   <p>
   If <code>recursive</code> is <code>true</code> and <code>from</code> represents a directory, then the directory and its contents
   will be copied recursively to the destination <code>to</code>.
   <p>
   The copy will fail if the destination if the destination already exists.

   @public
   @param from {string} the path to copy from 
   @param to {string} the path to copy to 
   @param recursive {boolean} 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.copyRecursive = function(from, to, recursive, handler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] ==='boolean' && typeof __args[3] === 'function') {
      j_fileSystem["copyRecursive(java.lang.String,java.lang.String,boolean,io.vertx.core.Handler)"](from, to, recursive, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#copyRecursive}

   @public
   @param from {string} 
   @param to {string} 
   @param recursive {boolean} 
   @return {FileSystem}
   */
  this.copyRecursiveBlocking = function(from, to, recursive) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] ==='boolean') {
      j_fileSystem["copyRecursiveBlocking(java.lang.String,java.lang.String,boolean)"](from, to, recursive);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Move a file from the path <code>from</code> to path <code>to</code>, asynchronously.
   <p>
   The move will fail if the destination already exists.

   @public
   @param from {string} the path to copy from 
   @param to {string} the path to copy to 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.move = function(from, to, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["move(java.lang.String,java.lang.String,io.vertx.core.Handler)"](from, to, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#move}

   @public
   @param from {string} 
   @param to {string} 
   @return {FileSystem}
   */
  this.moveBlocking = function(from, to) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["moveBlocking(java.lang.String,java.lang.String)"](from, to);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Truncate the file represented by <code>path</code> to length <code>len</code> in bytes, asynchronously.
   <p>
   The operation will fail if the file does not exist or <code>len</code> is less than <code>zero</code>.

   @public
   @param path {string} the path to the file 
   @param len {number} the length to truncate it to 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.truncate = function(path, len, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_fileSystem["truncate(java.lang.String,long,io.vertx.core.Handler)"](path, len, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#truncate}

   @public
   @param path {string} 
   @param len {number} 
   @return {FileSystem}
   */
  this.truncateBlocking = function(path, len) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='number') {
      j_fileSystem["truncateBlocking(java.lang.String,long)"](path, len);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Change the permissions on the file represented by <code>path</code> to <code>perms</code>, asynchronously.
   <p>
   The permission String takes the form rwxr-x--- as
   specified <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.

   @public
   @param path {string} the path to the file 
   @param perms {string} the permissions string 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.chmod = function(path, perms, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["chmod(java.lang.String,java.lang.String,io.vertx.core.Handler)"](path, perms, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of [#chmod(String, String, Handler)] {@link FileSystem}

   @public
   @param path {string} 
   @param perms {string} 
   @return {FileSystem}
   */
  this.chmodBlocking = function(path, perms) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["chmodBlocking(java.lang.String,java.lang.String)"](path, perms);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Change the permissions on the file represented by <code>path</code> to <code>perms</code>, asynchronously.<p>
   The permission String takes the form rwxr-x--- as
   specified in {<a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>}.
   <p>
   If the file is directory then all contents will also have their permissions changed recursively. Any directory permissions will
   be set to <code>dirPerms</code>, whilst any normal file permissions will be set to <code>perms</code>.

   @public
   @param path {string} the path to the file 
   @param perms {string} the permissions string 
   @param dirPerms {string} the directory permissions 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.chmodRecursive = function(path, perms, dirPerms, handler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string' && typeof __args[3] === 'function') {
      j_fileSystem["chmodRecursive(java.lang.String,java.lang.String,java.lang.String,io.vertx.core.Handler)"](path, perms, dirPerms, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#chmodRecursive}

   @public
   @param path {string} 
   @param perms {string} 
   @param dirPerms {string} 
   @return {FileSystem}
   */
  this.chmodRecursiveBlocking = function(path, perms, dirPerms) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string') {
      j_fileSystem["chmodRecursiveBlocking(java.lang.String,java.lang.String,java.lang.String)"](path, perms, dirPerms);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Change the ownership on the file represented by <code>path</code> to <code>user</code> and {code group}, asynchronously.

   @public
   @param path {string} the path to the file 
   @param user {string} the user name, <code>null</code> will not change the user name 
   @param group {string} the user group, <code>null</code> will not change the user group name 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.chown = function(path, user, group, handler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && (typeof __args[1] === 'string' || __args[1] == null) && (typeof __args[2] === 'string' || __args[2] == null) && typeof __args[3] === 'function') {
      j_fileSystem["chown(java.lang.String,java.lang.String,java.lang.String,io.vertx.core.Handler)"](path, user, group, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of 
  

   @public
   @param path {string} 
   @param user {string} 
   @param group {string} 
   @return {FileSystem}
   */
  this.chownBlocking = function(path, user, group) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'string' || __args[1] == null) && (typeof __args[2] === 'string' || __args[2] == null)) {
      j_fileSystem["chownBlocking(java.lang.String,java.lang.String,java.lang.String)"](path, user, group);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Obtain properties for the file represented by <code>path</code>, asynchronously.
   <p>
   If the file is a link, the link will be followed.

   @public
   @param path {string} the path to the file 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.props = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["props(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), FileProps), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#props}

   @public
   @param path {string} 
   @return {FileProps}
   */
  this.propsBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_fileSystem["propsBlocking(java.lang.String)"](path), FileProps);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Obtain properties for the link represented by <code>path</code>, asynchronously.
   <p>
   The link will not be followed.

   @public
   @param path {string} the path to the file 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.lprops = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["lprops(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), FileProps), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#lprops}

   @public
   @param path {string} 
   @return {FileProps}
   */
  this.lpropsBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_fileSystem["lpropsBlocking(java.lang.String)"](path), FileProps);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a hard link on the file system from <code>link</code> to <code>existing</code>, asynchronously.

   @public
   @param link {string} the link 
   @param existing {string} the link destination 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.link = function(link, existing, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["link(java.lang.String,java.lang.String,io.vertx.core.Handler)"](link, existing, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#link}

   @public
   @param link {string} 
   @param existing {string} 
   @return {FileSystem}
   */
  this.linkBlocking = function(link, existing) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["linkBlocking(java.lang.String,java.lang.String)"](link, existing);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a symbolic link on the file system from <code>link</code> to <code>existing</code>, asynchronously.

   @public
   @param link {string} the link 
   @param existing {string} the link destination 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.symlink = function(link, existing, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["symlink(java.lang.String,java.lang.String,io.vertx.core.Handler)"](link, existing, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#link}

   @public
   @param link {string} 
   @param existing {string} 
   @return {FileSystem}
   */
  this.symlinkBlocking = function(link, existing) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["symlinkBlocking(java.lang.String,java.lang.String)"](link, existing);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Unlinks the link on the file system represented by the path <code>link</code>, asynchronously.

   @public
   @param link {string} the link 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.unlink = function(link, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["unlink(java.lang.String,io.vertx.core.Handler)"](link, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#unlink}

   @public
   @param link {string} 
   @return {FileSystem}
   */
  this.unlinkBlocking = function(link) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_fileSystem["unlinkBlocking(java.lang.String)"](link);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the path representing the file that the symbolic link specified by <code>link</code> points to, asynchronously.

   @public
   @param link {string} the link 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.readSymlink = function(link, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["readSymlink(java.lang.String,io.vertx.core.Handler)"](link, function(ar) {
      if (ar.succeeded()) {
        handler(ar.result(), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#readSymlink}

   @public
   @param link {string} 
   @return {string}
   */
  this.readSymlinkBlocking = function(link) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_fileSystem["readSymlinkBlocking(java.lang.String)"](link);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Deletes the file represented by the specified <code>path</code>, asynchronously.

   @public
   @param path {string} path to the file 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.delete = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["delete(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#delete}

   @public
   @param path {string} 
   @return {FileSystem}
   */
  this.deleteBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_fileSystem["deleteBlocking(java.lang.String)"](path);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Deletes the file represented by the specified <code>path</code>, asynchronously.
   <p>
   If the path represents a directory and <code>recursive = true</code> then the directory and its contents will be
   deleted recursively.

   @public
   @param path {string} path to the file 
   @param recursive {boolean} delete recursively? 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.deleteRecursive = function(path, recursive, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_fileSystem["deleteRecursive(java.lang.String,boolean,io.vertx.core.Handler)"](path, recursive, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#deleteRecursive}

   @public
   @param path {string} 
   @param recursive {boolean} 
   @return {FileSystem}
   */
  this.deleteRecursiveBlocking = function(path, recursive) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean') {
      j_fileSystem["deleteRecursiveBlocking(java.lang.String,boolean)"](path, recursive);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create the directory represented by <code>path</code>, asynchronously.
   <p>
   The new directory will be created with permissions as specified by <code>perms</code>.
   <p>
   The permission String takes the form rwxr-x--- as specified
   in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.
   <p>
   The operation will fail if the directory already exists.

   @public
   @param path {string} path to the file 
   @param perms {string} the permissions string 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.mkdir = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["mkdir(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["mkdir(java.lang.String,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#mkdir}

   @public
   @param path {string} 
   @param perms {string} 
   @return {FileSystem}
   */
  this.mkdirBlocking = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_fileSystem["mkdirBlocking(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["mkdirBlocking(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create the directory represented by <code>path</code> and any non existent parents, asynchronously.
   <p>
   The new directory will be created with permissions as specified by <code>perms</code>.
   <p>
   The permission String takes the form rwxr-x--- as specified
   in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.
   <p>
   The operation will fail if the directory already exists.<p>

   @public
   @param path {string} path to the file 
   @param perms {string} the permissions string 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.mkdirs = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["mkdirs(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["mkdirs(java.lang.String,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#mkdirs}

   @public
   @param path {string} 
   @param perms {string} 
   @return {FileSystem}
   */
  this.mkdirsBlocking = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_fileSystem["mkdirsBlocking(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["mkdirsBlocking(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Read the contents of the directory specified by <code>path</code>, asynchronously.
   <p>
   The parameter <code>filter</code> is a regular expression. If <code>filter</code> is specified then only the paths that
   match  @{filter}will be returned.
   <p>
   The result is an array of String representing the paths of the files inside the directory.

   @public
   @param path {string} path to the directory 
   @param filter {string} the filter expression 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.readDir = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["readDir(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](ar.result(), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["readDir(java.lang.String,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](ar.result(), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#readDir}

   @public
   @param path {string} 
   @param filter {string} 
   @return {Array.<string>}
   */
  this.readDirBlocking = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_fileSystem["readDirBlocking(java.lang.String)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      return j_fileSystem["readDirBlocking(java.lang.String,java.lang.String)"](__args[0], __args[1]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Reads the entire file as represented by the path <code>path</code> as a , asynchronously.
   <p>
   Do not user this method to read very large files or you risk running out of available RAM.

   @public
   @param path {string} path to the file 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.readFile = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["readFile(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), Buffer), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#readFile}

   @public
   @param path {string} 
   @return {Buffer}
   */
  this.readFileBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_fileSystem["readFileBlocking(java.lang.String)"](path), Buffer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Creates the file, and writes the specified <code>Buffer data</code> to the file represented by the path <code>path</code>,
   asynchronously.

   @public
   @param path {string} path to the file 
   @param data {Buffer} 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.writeFile = function(path, data, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'object' && __args[1]._jdel && typeof __args[2] === 'function') {
      j_fileSystem["writeFile(java.lang.String,io.vertx.core.buffer.Buffer,io.vertx.core.Handler)"](path, data._jdel, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#writeFile}

   @public
   @param path {string} 
   @param data {Buffer} 
   @return {FileSystem}
   */
  this.writeFileBlocking = function(path, data) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'object' && __args[1]._jdel) {
      j_fileSystem["writeFileBlocking(java.lang.String,io.vertx.core.buffer.Buffer)"](path, data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Open the file represented by <code>path</code>, asynchronously.
   <p>
   The file is opened for both reading and writing. If the file does not already exist it will be created.

   @public
   @param path {string} path to the file 
   @param options {Object} options describing how the file should be opened 
   @param handler {function} 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.open = function(path, options, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_fileSystem["open(java.lang.String,io.vertx.core.file.OpenOptions,io.vertx.core.Handler)"](path, options != null ? new OpenOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), AsyncFile), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#open}

   @public
   @param path {string} 
   @param options {Object} 
   @return {AsyncFile}
   */
  this.openBlocking = function(path, options) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null)) {
      return utils.convReturnVertxGen(j_fileSystem["openBlocking(java.lang.String,io.vertx.core.file.OpenOptions)"](path, options != null ? new OpenOptions(new JsonObject(JSON.stringify(options))) : null), AsyncFile);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Creates an empty file with the specified <code>path</code> and permissions <code>perms</code>, asynchronously.

   @public
   @param path {string} path to the file 
   @param perms {string} the permissions string 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.createFile = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["createFile(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_fileSystem["createFile(java.lang.String,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#createFile}

   @public
   @param path {string} 
   @param perms {string} 
   @return {FileSystem}
   */
  this.createFileBlocking = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_fileSystem["createFileBlocking(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_fileSystem["createFileBlocking(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Determines whether the file as specified by the path <code>path</code> exists, asynchronously.

   @public
   @param path {string} path to the file 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.exists = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["exists(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(ar.result(), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#exists}

   @public
   @param path {string} 
   @return {boolean}
   */
  this.existsBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_fileSystem["existsBlocking(java.lang.String)"](path);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns properties of the file-system being used by the specified <code>path</code>, asynchronously.

   @public
   @param path {string} path to anywhere on the filesystem 
   @param handler {function} the handler that will be called on completion 
   @return {FileSystem} a reference to this, so the API can be used fluently
   */
  this.fsProps = function(path, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_fileSystem["fsProps(java.lang.String,io.vertx.core.Handler)"](path, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), FileSystemProps), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Blocking version of {@link FileSystem#fsProps}

   @public
   @param path {string} 
   @return {FileSystemProps}
   */
  this.fsPropsBlocking = function(path) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_fileSystem["fsPropsBlocking(java.lang.String)"](path), FileSystemProps);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_fileSystem;
};

// We export the Constructor function
module.exports = FileSystem;