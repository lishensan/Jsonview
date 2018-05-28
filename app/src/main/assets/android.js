/*
	Base.js, version 1.1a
	Copyright 2006-2010, Dean Edwards
	License: http://www.opensource.org/licenses/mit-license.php
*/
'use strict'
var Base = function () {
    // dummy
};

Base.extend = function (_instance, _static) { // subclass
    var extend = Base.prototype.extend;

    // build the prototype
    Base._prototyping = true;
    var proto = new this;
    extend.call(proto, _instance);
    proto.base = function () {
        // call this method from any other method to invoke that method's ancestor
    };
    delete Base._prototyping;

    // create the wrapper for the constructor function
    //var constructor = proto.constructor.valueOf(); //-dean
    var constructor = proto.constructor;
    var klass = proto.constructor = function () {
        if (!Base._prototyping) {
            if (this._constructing || this.constructor == klass) { // instantiation
                this._constructing = true;
                constructor.apply(this, arguments);
                delete this._constructing;
            } else if (arguments[0] != null) { // casting
                return (arguments[0].extend || extend).call(arguments[0], proto);
            }
        }
    };

    // build the class interface
    klass.ancestor = this;
    klass.extend = this.extend;
    klass.forEach = this.forEach;
    klass.implement = this.implement;
    klass.prototype = proto;
    klass.toString = this.toString;
    klass.valueOf = function (type) {
        //return (type == "object") ? klass : constructor; //-dean
        return (type == "object") ? klass : constructor.valueOf();
    };
    extend.call(klass, _static);
    // class initialisation
    if (typeof klass.init == "function") klass.init();
    return klass;
};

Base.prototype = {
    extend: function (source, value) {
        if (arguments.length > 1) { // extending with a name/value pair
            var ancestor = this[source];
            if (ancestor && (typeof value == "function") && // overriding a method?
                // the valueOf() comparison is to avoid circular references
                (!ancestor.valueOf || ancestor.valueOf() != value.valueOf()) &&
                /\bbase\b/.test(value)) {
                // get the underlying method
                var method = value.valueOf();
                // override
                value = function () {
                    var previous = this.base || Base.prototype.base;
                    this.base = ancestor;
                    var returnValue = method.apply(this, arguments);
                    this.base = previous;
                    return returnValue;
                };
                // point to the underlying method
                value.valueOf = function (type) {
                    return (type == "object") ? value : method;
                };
                value.toString = Base.toString;
            }
            this[source] = value;
        } else if (source) { // extending with an object literal
            var extend = Base.prototype.extend;
            // if this object has a customised extend method then use it
            if (!Base._prototyping && typeof this != "function") {
                extend = this.extend || extend;
            }
            var proto = { toSource: null };
            // do the "toString" and other methods manually
            var hidden = ["constructor", "toString", "valueOf"];
            // if we are prototyping then include the constructor
            var i = Base._prototyping ? 0 : 1;
            while (key = hidden[i++]) {
                if (source[key] != proto[key]) {
                    extend.call(this, key, source[key]);

                }
            }
            // copy each of the source object's properties to this object
            for (var key in source) {
                if (!proto[key]) extend.call(this, key, source[key]);
            }
        }
        return this;
    }
};

// initialise
Base = Base.extend({
    constructor: function () {
        this.extend(arguments[0]);
    }
}, {
        ancestor: Object,
        version: "1.1",

        forEach: function (object, block, context) {
            for (var key in object) {
                if (this.prototype[key] === undefined) {
                    block.call(context, object[key], key, object);
                }
            }
        },

        implement: function () {
            for (var i = 0; i < arguments.length; i++) {
                if (typeof arguments[i] == "function") {
                    // if it's a function, call it
                    arguments[i](this.prototype);
                } else {
                    // add the interface using the extend method
                    this.prototype.extend(arguments[i]);
                }
            }
            return this;
        },

        toString: function () {
            return String(this.valueOf());
        }
    });
//////////////////////////////////////////////////////
var Context = Base.extend({
    constructor: function (contextId) {
        this.contextId = contextId;
    },
    contextId: 0
});
var Activity = Context.extend({
    constructor: function (contextId) {
        this.base(contextId);
        this.activiyId = contextId;
    },
    activiyId: 0,
    findViewById: function (viewId) {
        var argsJson = createJArgsJson(createJArg("I", viewId));
        return exec(this.activiyId, "findViewById", argsJson);
    }
});

var View = Base.extend({
    constructor: function (context) {
        this.context = context;
        this.contextId = context.contextId;
        console.log(this.jClassId);
        this.jViewId = arguments[1] || newJobj(this.jClassId, createJArgsJson(createJArg(this.contextClassId, this.contextId)));
    },
    contextClassId: "Landroid/content/Context",
    jViewId: 0,
    jClassId: "Landroid/view/View",
    setLayoutParams: function (jLayoutParamsId) {
        var argsJson = createJArgsJson(createJArg("Landroid/view/ViewGroup/LayoutParams", jLayoutParamsId));
        exec(jViewId, "setLayoutParams", argsJson, 1);
    },
    findViewById: function (viewId) {
        var argsJson = createJArgsJson(createJArg("I", viewId));
        return exec(this.jViewId, "findViewById", argsJson);
    },
    setId: function (viewId) {
        var argsJson = createJArgsJson(createJArg("I", viewId));
        return exec(this.jViewId, "setId", argsJson);
    }
});

var RecycleView = View.extend({
    jClassId: "Lcom/mountain/jsview/recycleview/JsRecycleView",
    setAdapter: function (adapter) {
        var argsJson = createJArgsJson(createJArg(JsRecycleViewAdapterClassId, adapter));
        exec(this.jViewId, "setAdapter", argsJson, 1);
    },
    setLayoutManager: function (layoutManager) {
        var argsJson = createJArgsJson(createJArg("Landroid/support/v7/widget/RecyclerView$LayoutManager", layoutManager));
        exec(this.jViewId, "setLayoutManager", argsJson, 1);
    },
    createLinearLayoutManager: function () {
        var argsJson = createJArgsJson(createJArg(this.contextClassId, this.contextId));
        return newJobj("Lcom/mountain/jsview/recycleview/JsLinearLayoutManager", argsJson)
    }
});
var ListView = View.extend({
    jClassId: "Lcom/mountain/jsview/listview/JsListView",
    setAdapter: function (adapter) {
        var argsJson = createJArgsJson(createJArg(JsListViewAdapterClassId, adapter));
        exec(this.jViewId, "setAdapter", argsJson, 1);
    }
});


var TextView = View.extend({
    jClassId: "Landroid/widget/TextView",
    setTextColor: function (color) {
        var argsJson = createJArgsJson(createJArg("I", color));
        exec(this.jViewId, "setTextColor", argsJson, 1);
    },
    setText: function (string) {
        var argsJson = createJArgsJson(createJArg(CharSequenceClassId, string));
        exec(this.jViewId, "setText", argsJson, 1)
    },
    //sp
    setTextSize: function (size) {
        var argsJson = createJArgsJson(createJArg("F", size));
        exec(this.jViewId, "setTextSize", argsJson, 1)
    }
});


var ViewGroup = View.extend({
    addView: function (view) {

        var argsJson = createJArgsJson(createJArg("Landroid/view/View", view.jViewId));
        console.log(view.jViewId);
        console.log(argsJson);
        exec(this.jViewId, "addView", argsJson, 1);
    }
});



function showMessage(json) {
    var name = json.name;
    var city = json.city;

    //alert(JSON.stringify(json));
    console.log("name=" + name + ",city=" + city);
}

function createTextView() {
    var activity = new Activity(getActivity());
    var textview = new TextView(activity);
    var viewGroupId = getContentView();
    console.log(viewGroupId);
    var viewGroup = new ViewGroup(getActivity(), viewGroupId);
    textview.setTextColor(0xff0000ff);
    textview.setText("来自javascript创建");
    viewGroup.addView(textview);

}


var JsVirtualViewClassId = "Lcom/mountain/jsview/widget/JsVirtualView";
var JsVirtualTextViewClassId = "Lcom/mountain/jsview/widget/JsVirtualTextView";
var JsVirtualImageViewClassId = "Lcom/mountain/jsview/widget/JsVirtualImageView";
var JsVirtualViewGroupClassId = "Lcom/mountain/jsview/widget/JsVirtualViewGroup";
var JsVirtualLinearLayoutClassId = "Lcom/mountain/jsview/widget/JsVirtualLinearLayout";
var CharSequenceClassId = "Ljava/lang/CharSequence";
var StringClassId = "Ljava/lang/String";
var ArrayListClassId = "Ljava/util/ArrayList";
var ListClassId = "Ljava/util/List";
var ViewModelClassId = "Lcom/mountain/jsview/recycleview/impl/ViewModel";
var ObjectClassId = "Ljava/lang/Object";
var JsRecycleViewAdapterClassId = "Lcom/mountain/jsview/recycleview/JsRecycleViewAdapter";
var JsListViewAdapterClassId = "Lcom/mountain/jsview/listview/JsListViewAdapter";



var VirtualView = Base.extend({
    constructor: function () {
        console.log(this.jClassId);
        this.jVirtualViewId = arguments[1] || newJobj(this.jClassId);
    },
    jVirtualViewId: 0,
    jClassId: JsVirtualViewClassId,
    setId: function (viewId) {
        var argsJson = createJArgsJson(createJArg("I", viewId));
        return exec(this.jVirtualViewId, "setId", argsJson);
    },
    setLayoutParams: function (layoutParams) {
        var argsJson = createJArgsJson(createJArg(StringClassId, layoutParams));
        return exec(this.jVirtualViewId, "setLayoutParams", argsJson);
    },
    setViewType:function(viewType){
        var argsJson = createJArgsJson(createJArg("I", viewType));
        return exec(this.jVirtualViewId, "setViewType", argsJson);
    }
});

var VirtualTextView = VirtualView.extend({
    jClassId: JsVirtualTextViewClassId,
    setTextColor: function (color) {
        var argsJson = createJArgsJson(createJArg("I", color));
        exec(this.jVirtualViewId, "setTextColor", argsJson);
    },
    setText: function (string) {
        var argsJson = createJArgsJson(createJArg(CharSequenceClassId, string));
        exec(this.jVirtualViewId, "setText", argsJson)
    },
    //sp
    setTextSize: function (size) {
        var argsJson = createJArgsJson(createJArg("F", size));
        exec(this.jVirtualViewId, "setTextSize", argsJson)
    }
});
var VirtualImageView = VirtualView.extend({
    jClassId: JsVirtualImageViewClassId,
    setImageUrl: function (string) {
        var argsJson = createJArgsJson(createJArg(StringClassId, string));
        exec(this.jVirtualViewId, "setImageUrl", argsJson)
    }
});
var VirtualViewGroup = VirtualView.extend({
    jClassId: JsVirtualViewGroupClassId,
    addView: function (virtualView) {
        var argsJson = createJArgsJson(createJArg(JsVirtualViewClassId, virtualView.jVirtualViewId));
        exec(this.jVirtualViewId, "addView", argsJson)
    },
    removeView: function (virtualView) {
        var argsJson = createJArgsJson(createJArg(JsVirtualViewClassId, virtualView.jVirtualViewId));
        exec(this.jVirtualViewId, "removeView", argsJson)
    }
});
var VirtualLinearLayout = VirtualViewGroup.extend({
    jClassId: JsVirtualLinearLayoutClassId,
    setOrientation:function(orientation){
        var argsJson = createJArgsJson(createJArg("I", orientation));
        exec(this.jVirtualViewId, "setOrientation", argsJson)
    }
});


function createReycleViewAdapter() {
    var activity = new Activity(getActivity());
    var recycleView = new RecycleView(activity);
    var viewGroupId = activity.findViewById(1);
    var jsRecycleViewAdapter = newJobj(JsRecycleViewAdapterClassId);
    var models = newJobj(ArrayListClassId);
    var len = 100;
    var i = 0;
    for (; i < len; i++) {
        var virtualLinearLayout = new VirtualLinearLayout();
        var virtualTextView = new VirtualTextView();
        var layoutParams = { width: 200, height: 200 };
        virtualTextView.setLayoutParams(JSON.stringify(layoutParams));
        virtualTextView.setText("来自javascript" + i)
        var layoutParams = { width: 800, height: 800 };
        var virtualImageView = new VirtualImageView();
        if (i % 2 == 0) {
            virtualImageView.setImageUrl("http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b60bfac143de9f81a4c500fa2dd.jpg")
        } else {
            virtualImageView.setImageUrl("http://img5q.duitang.com/uploads/item/201411/30/20141130235625_H5yuH.jpeg")
        }
        virtualImageView.setLayoutParams(JSON.stringify(layoutParams));

        virtualLinearLayout.addView(virtualTextView);
        virtualLinearLayout.addView(virtualImageView);

        var argsJson = createJArgsJson(createJArg(JsVirtualViewClassId, virtualLinearLayout.jVirtualViewId));
        var model = newJobj(ViewModelClassId, argsJson)
        var argsJson = createJArgsJson(createJArg(ObjectClassId, model));
        exec(models, "add", argsJson, model);
    }
    var argsJson = createJArgsJson(createJArg(ListClassId, models));
    exec(jsRecycleViewAdapter, "setViewModels", argsJson);
    var viewGroup = new ViewGroup(activity, getContentView());
    console.log("putData6")
    var linearLayoutManager = recycleView.createLinearLayoutManager();
    recycleView.setLayoutManager(linearLayoutManager);
    recycleView.setAdapter(jsRecycleViewAdapter);
    viewGroup.addView(recycleView);
}

function onBindViewHolder(jObjId, data) {
    console.log(jObjId);
    var meta = data.dataSouce.meta;
    console.log(meta);
    var argsJson = createJArgsJson(createJArg("I", 0));
    var textViewId = exec(jObjId, "getViewByPostition", argsJson);
    console.log(textViewId);
    var textview = new TextView(getActivity(), textViewId);
    textview.setText("来自js " + meta);
    textview.setTextColor(0xffff0000);
    textview.setTextSize(20);


}


function createJArg(classId, argValue) {
    var arg = {};
    arg[classId] = argValue;
    return arg;
}

function createJArgsJson() {
    var array = [];

    var len = arguments.length;
    for (var i = 0; i < len; i++) {
        array.push(arguments[i]);
    }
    return JSON.stringify(array)
}

//执行java某个对象的方法
function exec(objId, methodName, argsJson, thread) {
    return window.JsInterface.exec(objId, methodName, argsJson, thread);
}
//执行java某个类的静态方法
function staticExec(classId, methodName, argsJson, thread) {
    return window.JsInterface.staticExec(classId, methodName, argsJson, thread);
}

//获取环境默认类对象的hash
function getObjId(classId) {
    return window.JsInterface.getObjId(classId);
}
//获取环境默认类对象的hash
function getJsInterface() {
    return window.JsInterface.getObjId("this");
}
//当前添加viewGroup
function getContentView() {
    return window.JsInterface.getContentView();
}

//初始化一个java对象
function newJobj(classId, argsJson) {
    return window.JsInterface.newJobj(classId, argsJson);
}
function getActivity() {
    return window.JsInterface.getActivity();
}
