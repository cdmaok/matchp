(function ($, window, document) {
    var MP = window.MP || {};

    $.support.cors = true;

    var baseURL = 'http://121.192.180.198:8080/matchp-web/api/';
    var globalAPI = {
        getImgsByText : {
            name : 'getImgsByText',
            server : 'query',
            method : 'POST'
        }, 
        getImproveDetail : {
            name : 'queryImprove',
            server : 'improve',
            method : 'GET'
        },
        setImproveDetail : {
            name : 'setImproveDetail',
            server : 'improve',
            method : 'POST'
        }
    };

    var loadData = function (serverName, params, data) {
        var df = $.Deferred(),
            apiCfg = globalAPI[serverName];
        if (!apiCfg) throw (serverName + ' not found');
        if (data != null) {
            df.resolve(data);
            return df.promise();
        }
        $.ajax({
            url : baseURL + (apiCfg.server || ''),
            data : JSON.stringify(params),
            dataType : 'json',
            contentType: 'application/json',
            type : apiCfg.method || 'GET'
        }).done(function (rsp) {
            df.resolve(rsp);
        }).fail(function (rsp) {
            df.reject(rsp);
        });
        return df.promise();
    };

    MP.loadData = loadData;
    window.MP = MP;

})(jQuery, window, document, undefined);