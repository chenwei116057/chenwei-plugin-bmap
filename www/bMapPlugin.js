var cordova = require('cordova');

function BMapPlugin() {
    BMapPlugin.prototype.locate = function (param,successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "BMapPlugin", 'locate', [param.mode,param.coordinateType,param.openGps,param.timeOutMillis,param.enableSimulateGps]);
    }
}

module.exports = new BMapPlugin();
