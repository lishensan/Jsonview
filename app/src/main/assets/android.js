
function showMessage(json) {
    var name = json.name;
    var city = json.city;

    //alert(JSON.stringify(json));
    console.log("name=" + name + ",city=" + city);
}

function clickFunc() {

    var objId = getObjId("Landroid/content/Context");
    var toastMsg = "Ok";
    var objClassType = "Landroid/widget/Toast";
    var toastArgArrayObj = createJArgsJson(createJArg("Landroid/content/Context", objId), createJArg("Ljava/lang/CharSequence", toastMsg), createJArg("I", 0));
    console.log(toastArgArrayObj)
    var toastId = staticExec(objClassType, "makeText", toastArgArrayObj, 0);
    exec(toastId, "show", null, 1);
}

function TextView(contextId) {
    this.contextId = contextId;
    this.classId = "Landroid/widget/TextView"
    this.argsJson = createJArgsJson(createJArg("Landroid/content/Context", contextId))
    this.jTextViewId = newJobj(classId, argsJson)
}

function createJArg(classId, argValue) {
    var arg = {};
    arg[classId] = argValue
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
//初始化一个java对象
function newJobj(classId, argsJson) {
    return window.JsInterface.newJobj(classId, argsJson);
}
