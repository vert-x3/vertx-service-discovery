var JsonObject = Packages.io.vertx.core.json.JsonObject;
var JsonArray = Packages.io.vertx.core.json.JsonArray;
var asList = java.util.Arrays.asList;
var Character = Java.type("java.lang.Character");
var Long = Java.type("java.lang.Long");
var LongArrayType = Java.type("java.lang.Long[]");
var ShortArrayType = Java.type("java.lang.Short[]");
var ByteArrayType = Java.type("java.lang.Byte[]");
var ObjectArrayType = Java.type("java.lang.Object[]");
var VertxGenConverterList = Java.type("io.vertx.lang.js.VertxGenConverterList");
var VertxGenConverterSet = Java.type("io.vertx.lang.js.VertxGenConverterSet");
var JavaArraySetWrapper = Java.type("io.vertx.lang.js.JavaArraySetWrapper");
var ListConverterSet = Java.type("io.vertx.lang.js.ListConverterSet");
var VertxGenConverterMap = Java.type("io.vertx.lang.js.VertxGenConverterMap");
var LongConverterMap = Java.type("io.vertx.lang.js.LongConverterMap");
var ShortConverterMap = Java.type("io.vertx.lang.js.ShortConverterMap");
var ByteConverterMap = Java.type("io.vertx.lang.js.ByteConverterMap");
var ThrowableConverter = Java.type("io.vertx.lang.js.ThrowableConverter");
var SucceededResult = Packages.io.vertx.lang.js.SucceededResult;
var FailedResult = Packages.io.vertx.lang.js.FailedResult;

var utils = {};

// Param conversion

// Convert JS JSON object param to Java JsonObject
utils.convParamJsonObject = function(param) {
  return param != null ? new JsonObject(JSON.stringify(param)) : null;
};

// Convert JS Array param to Java JsonArray
utils.convParamJsonArray = function(param) {
  return param != null ? new JsonArray(JSON.stringify(param)) : null;
};

// Convert Object type param (e.g. eventbus send)
utils.convParamTypeUnknown = function(param) {
  if (param == null) {
    return param;
  }
  if (typeof param === 'object') {
    if (param instanceof Array) {
      return utils.convParamJsonArray(param);
    }
    if (typeof param._jdel === 'object') {
      return param;
    }
    return utils.convParamJsonObject(param);
  }
  return param;
};

utils.convParamThrowable = function(err) {
  return ThrowableConverter.catchAndReturnThrowable(function() {
    throw err;
  });
};

utils.convParamByte = function(n) {
  return n == null ? null : n.byteValue();
};

utils.convParamShort = function(n) {
  return n == null ? null : n.shortValue();
};

utils.convParamInteger = function(n) {
  return n == null ? null : n.intValue();
};

utils.convParamLong = function(n) {
  return n == null ? null : n.longValue();
};

utils.convParamFloat = function(n) {
  return n == null ? null : n.floatValue();
};

utils.convParamDouble = function(n) {
  return n == null ? null : n.doubleValue();
};

utils.convParamCharacter = function(c) {
  return c == null ? null : new Character(c.charCodeAt(0));
};

utils.convParamListLong = function(arr) {
  return arr == null ? null : asList(Java.to(arr, LongArrayType));
};

utils.convParamListShort = function(arr) {
  return arr == null ? null : asList(Java.to(arr, ShortArrayType));
}

utils.convParamListByte = function(arr) {
  return arr == null ? null: asList(Java.to(arr, ByteArrayType));
}

utils.convParamListBasicOther = function(arr) {
  return arr == null ? null: asList(Java.to(arr, ObjectArrayType));
}

utils.convParamSetBasicOther = function(arr) {
  return arr == null ? null : new ListConverterSet(arr);
}

utils.convParamSetLong = function(arr) {
  return arr == null ? null : new JavaArraySetWrapper(Java.to(arr, LongArrayType));
}

utils.convParamSetShort = function(arr) {
  return arr == null ? null : new JavaArraySetWrapper(Java.to(arr, ShortArrayType));
}

utils.convParamSetByte = function(arr) {
  return arr == null ? null : new JavaArraySetWrapper(Java.to(arr, ByteArrayType));
}

utils.convParamListVertxGen = function(arr) {
  return arr == null ? null : new VertxGenConverterList(arr);
}

utils.convParamSetVertxGen = function(arr) {
  return arr == null ? null : new VertxGenConverterSet(arr);
}

utils.convParamMapLong = function(arr) {
  return arr == null ? null : new LongConverterMap(arr);
}

utils.convParamMapShort = function(arr) {
  return arr == null ? null : new ShortConverterMap(arr);
}

utils.convParamMapByte = function(arr) {
  return arr == null ? null : new ByteConverterMap(arr);
}

utils.convParamMapVertxGen = function(arr) {
  return arr == null ? null : new VertxGenConverterMap(arr);
}

utils.convParamMapJsonObject = function(arr) {
  if (arr) {
    var newmap = {};
    for (var key in arr) {
      if (arr.hasOwnProperty(key)) {
        var val = arr[key];
        if (val) {
          newmap[key] = new JsonObject(JSON.stringify(val));
        } else {
          newmap[key] = null;
        }
      }
    }
    return newmap;
  } else {
    return null;
  }
}

utils.convParamMapJsonArray = function(arr) {
  if (arr) {
    var newmap = {};
    for (var key in arr) {
      if (arr.hasOwnProperty(key)) {
        var val = arr[key];
        if (val) {
          newmap[key] = new JsonArray(JSON.stringify(val));
        } else {
          newmap[key] = null;
        }
      }
    }
    return newmap;
  } else {
    return null;
  }
}

utils.convParamListJsonObject = function(arr) {
  if (arr) {
    var len = arr.length;
    var newarr = [];
    for (var i = 0; i < len; i++) {
      var elem = arr[i];
      newarr[i] = elem != null ? new JsonObject(JSON.stringify(elem)) : elem;
    }
    return newarr;
  } else {
    return null;
  }
}

utils.convParamListJsonArray = function(arr) {
  if (arr) {
    var len = arr.length;
    var newarr = [];
    for (var i = 0; i < len; i++) {
      var elem = arr[i];
      newarr[i] = elem != null ? new JsonArray(JSON.stringify(elem)) : null;
    }
    return newarr;
  } else {
    return null;
  }
}

utils.convParamListDataObject = function(arr, constructor) {
  if (arr) {
    var len = arr.length;
    var newarr = [];
    for (var i = 0; i < len; i++) {
      var elem = arr[i];
      newarr[i] = elem != null ? constructor(new JsonObject(JSON.stringify(elem))) : null;
    }
    return newarr;
  } else {
    return null;
  }
}

utils.convParamListEnum = function(arr, constructor) {
  if (arr) {
    var len = arr.length;
    var newarr = [];
    for (var i = 0; i < len; i++) {
      var elem = arr[i];
      newarr[i] = elem != null ? constructor(elem) : null;
    }
    return newarr;
  } else {
    return null;
  }
}

utils.convParamSetJsonObject = function(arr) {
  return arr == null ? null : new ListConverterSet(utils.convParamListJsonObject(arr));
}

utils.convParamSetJsonArray = function(arr) {
  return arr == null ? null : new ListConverterSet(utils.convParamListJsonArray(arr));
}

utils.convParamSetDataObject = function(arr, constructor) {
  return arr == null ? null : new ListConverterSet(utils.convParamListDataObject(arr, constructor));
}

utils.convParamSetEnum = function(arr, constructor) {
  return arr == null ? null : new ListConverterSet(utils.convParamListEnum(arr, constructor));
}

// Return conversion

// This is used to convert the return value from any Generic method where we don't know the actual type
// or Throwable return
utils.convReturnTypeUnknown = function(ret) {
  if (ret instanceof JsonObject || ret instanceof JsonArray) {
    return JSON.parse(ret.encode());
  } else if (ret instanceof Long) {
    return ret.doubleValue();
  } else {
    return ret;
  }
};

utils.convReturnThrowable = function(ret) {
  try {
    throw ret;
  } catch (e) {
    return e;
  }
};

// Convert a Java JsonObject/JsonArray return to JS JSON
utils.convReturnJson = function(param) {
  return param != null ? JSON.parse(param.encode()) : null;
};

// Convert a java.lang.Long return to JS number
utils.convReturnLong = function(param) {
  return param != null ? param.doubleValue() : null;
};

/*
 Convert a set return
 Nashorn doesn't automatically convert Sets to JS Arrays so we have to do this manually
 Note that this involves copying - so best to avoid Set in the API!
 */
utils.convReturnSet = function(jSet) {
  return jSet != null ? new java.util.ArrayList(jSet) : null;
};

// Convert a list/set containing json return
utils.convReturnListSetJson = function(jList) {
  if (jList == null) {
    return null;
  }
  var arr = [];
  arr.length = jList.size();
  var iter = jList.iterator();
  var pos = 0;
  while (iter.hasNext()) {
    var elem = iter.next();
    if (elem instanceof JsonObject || elem instanceof JsonArray) {
      elem = JSON.parse(elem.encode());
    }
    arr[pos++] = elem;
  }
  return arr;
};

// Convert a VertxGen return value
utils.convReturnVertxGen = function(jdel, constructorFunction) {
  if (jdel != null) {
    // A bit of jiggery pokery to create the object given a reference to the constructor function
    var obj = Object.create(constructorFunction.prototype, {});
    constructorFunction.apply(obj, [jdel]);
    return obj;
  }
  return null;
}

utils.convReturnEnum = function(jVal) {
  return jVal != null ? jVal.toString() : null;
}

// Convert a DataObject return value
utils.convReturnDataObject = function(dataObject) {
  if (dataObject != null) {
    return utils.convReturnJson(dataObject.toJson());
  }
  return null;
}

// Convert a list/set containing VertxGen return
utils.convReturnListSetVertxGen = function(jList, constructorFunction) {
  if (jList) {
    var arr = [];
    arr.length = jList.size();
    var iter = jList.iterator();
    var pos = 0;
    while (iter.hasNext()) {
      var jVertxGen = iter.next();
      // A bit of jiggery pokery to create the object given a reference to the constructor function
      if (jVertxGen) {
        var obj = Object.create(constructorFunction.prototype, {});
        constructorFunction.apply(obj, [jVertxGen]);
        arr[pos++] = obj;
      } else {
        arr[pos++] = null;
      }
    }
    return arr;
  } else {
    return null;
  }
};

// Convert a list/set containing data object return
utils.convReturnListSetDataObject = function(jList) {
  if (jList) {
    var arr = [];
    arr.length = jList.size();
    var iter = jList.iterator();
    var pos = 0;
    while (iter.hasNext()) {
      var elem = iter.next();
      arr[pos++] = elem != null ? JSON.parse(elem.toJson().encode()) : null;
    }
    return arr;
  } else {
    return null;
  }
};

// Convert a list/set containing enum return
utils.convReturnListSetEnum = function(jList) {
  if (jList) {
    var arr = [];
    arr.length = jList.size();
    var iter = jList.iterator();
    var pos = 0;
    while (iter.hasNext()) {
      var elem = iter.next();
      arr[pos++] = elem != null ? elem.toString() : null;
    }
    return arr;
  } else {
    return null;
  }
};

// Convert a list/set containing Long return
utils.convReturnListSetLong = function(jList) {
  if (jList) {
    var arr = [];
    arr.length = jList.size();
    var iter = jList.iterator();
    var pos = 0;
    while (iter.hasNext()) {
      var elem = iter.next();
      arr[pos++] = elem != null ? elem.doubleValue() : null;
    }
    return arr;
  } else {
    return null;
  }
};

// Convert a map return
utils.convReturnMap = function(jMap) {
  if (jMap) {
    // Object.keys is not supported. hasOwnKeys is called on ScriptObject which does not get proxied down
    // to JSAdapter.
    return new JSAdapter({
      __get__: function (name) {
        return utils.convReturnTypeUnknown(jMap.get(name));
      },

      __put__: function (name, value) {
        jMap.put(name, utils.convParamTypeUnknown(value));
      },

      __call__: function (name, arg1, arg2) {
        switch (name) {
          case "size": {
            return jMap.size();
          }
          case "forEach": {
            if (typeof arg1 == 'function') {
              jMap.entrySet().forEach(function(entry) {
                arg1(utils.convReturnTypeUnknown(entry.getValue()), entry.getKey());
              });
            } else {
              throw new TypeError(arg1 + " is not a function");
            }
            break;
          }
          case "clear": {
            jMap.clear();
            break;
          }
          default :
            console.log("WARN: Unsupported method call " + name + " for wrapped map object.");
        }
      },

      __new__: function (arg1, arg2) {
      },

      __getIds__: function () {
        return utils.convReturnSet(jMap.keySet());
      },

      __getValues__: function () {
        return utils.convReturnListSetJson(jMap.values());
      },

      __has__: function (name) {
        return jMap.containsKey(name);
      },

      __delete__: function (name) {
        return jMap.remove(name);
      }

    });
  } else {
    return null;
  }
};

// Convert a Handler<AsyncResult> return
utils.convReturnHandlerAsyncResult = function(handler, converter) {
  return function(result, err) {
    if (err == null) {
      handler.handle(new SucceededResult(converter(result)));
    } else {
      handler.handle(new FailedResult(err));
    }
  }
};

// Convert a Handler return
utils.convReturnHandler = function(handler, converter) {
  return function(result) {
    handler.handle(converter(result));
  }
};

module.exports = utils;
