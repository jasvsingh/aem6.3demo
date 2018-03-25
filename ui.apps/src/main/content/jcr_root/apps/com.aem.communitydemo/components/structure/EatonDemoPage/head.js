
"use strict";

var global = this;

use(["../utils/ResourceUtils.js"], function (ResourceUtils) {
    
    var CONST = {
        PROP_DESIGN_PATH: "cq:designPath"
    };


    var currentUrl = request.getRequestURL();


    return {
        url: currentUrl,
        wcmmode: global.wcmmode
    };
});

