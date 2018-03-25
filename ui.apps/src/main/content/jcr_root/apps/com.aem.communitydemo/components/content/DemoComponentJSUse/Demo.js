"use strict";
use(function () {
    var Constants = {
        DESCRIPTION_PROP: "jcr:description"
    };
  
    var title = "hello " + granite.resource.properties["title"];
    var description = "description :: " + granite.resource.properties["description"];

    return {
        title: title,
        description: description
    };
});