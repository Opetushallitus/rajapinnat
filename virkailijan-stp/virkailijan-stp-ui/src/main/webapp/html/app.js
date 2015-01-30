"use strict";

var app = angular.module('virkailijan-stp', [ 'ngHtmlCompile','ngResource', 'ngSanitize', 'truncate', 'loading', 'ngRoute', 'ngAnimate', 'localization', 'ui.bootstrap','ui.bootstrap.tpls','ui.bootstrap.transition', 'ui.utils', 'ngIdle', 'pasvaz.bindonce', 'ngUpload']);
//
// i18n toteutus kopioitu osittain http://jsfiddle.net/4tRBY/41/
//

angular.module('localization', []).filter('i18n', [ '$rootScope', '$locale', function($rootScope, $locale) {
    var localeMapping = {
        "en-us" : "en_US",
        "fi-fi" : "fi_FI",
        "sv-se" : "sv-SE"
    };

    jQuery.i18n.properties({
        name : 'messages',
        path : '../i18n/',
        mode : 'map',
        language : localeMapping[$locale.id],
        callback : function() {
        }
    });

    return function(text) {
        return jQuery.i18n.prop(text);
    };
} ]);

var SERVICE_URL_BASE = SERVICE_URL_BASE || "<virkailijan-stp-ui.rajapinnat-service-url.rest>";
var WP_API_BASE = WP_API_BASE || "<virkailijan-stp-ui.wp-api-url>";
var TEMPLATE_URL_BASE = TEMPLATE_URL_BASE || "";
var CAS_URL = CAS_URL || "<valintalaskenta-ui.cas.url>";
var SESSION_KEEPALIVE_INTERVAL_IN_SECONDS = SESSION_KEEPALIVE_INTERVAL_IN_SECONDS || 30;
var MAX_SESSION_IDLE_TIME_IN_SECONDS = MAX_SESSION_IDLE_TIME_IN_SECONDS || 1800;

app.factory('NoCacheInterceptor', function() {
    return {
        request : function(config) {
            if (config.method && config.method == 'GET' && config.url.indexOf('html') === -1) {
                var separator = config.url.indexOf('?') === -1 ? '?' : '&';
                config.url = config.url + separator + 'noCache=' + new Date().getTime();
            }
            return config;
        }
    };
});
// Route configuration

app.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $httpProvider.interceptors.push('NoCacheInterceptor');
    $routeProvider.when('/etusivu', {
    	controller : PostsController,
    	templateUrl : TEMPLATE_URL_BASE + 'posts/posts.html'
    })
    .when('/tiedote/:tiedoteid', {
        controller : ViewAnnouncementController,
        templateUrl : TEMPLATE_URL_BASE + 'announcement/announcement.html'
    })
    .when('/tapahtuma/:tapahtumaid', {
        controller : ViewEventController,
        templateUrl : TEMPLATE_URL_BASE + 'event/event.html'
    })
    .when('/kalenteri/', {
        controller : ViewCalendarController,
        templateUrl : TEMPLATE_URL_BASE + 'calendar/calendar.html'
    })
    .when('/materiaali/:materiaaliid', {
        controller : ViewMaterialController,
        templateUrl : TEMPLATE_URL_BASE + 'material/material.html'
    })
    .otherwise({
    redirectTo : '/etusivu'
    });
} ]);

app.factory('Announcements', function($resource) {
    return $resource(WP_API_BASE +"get_posts", {}, {
        get : {
            method : "GET",
            isArray : false,
        }
    });
});

app.factory('Announcement', function($resource) {
    return $resource(WP_API_BASE+"get_post", {}, {
        get : {
            method : "GET",
            isArray : false
        }
    });
});

app.factory('Events', function($resource) {
    return $resource(WP_API_BASE+"events/get_recent_events", {}, {
        get : {
            method : "GET",
            isArray : false
        }
    });
})

app.factory('Event', function($resource) {
    return $resource(WP_API_BASE+"event/get_event", {}, {
        get : {
            method : "GET",
            isArray : false
        }
    });
})

app.factory('Materials', function($resource) {
    return $resource(WP_API_BASE +"get_posts", {}, {
        get : {
            method : "GET",
            isArray : false,
            params : { post_type : "page" }
        }
    });
})

app.factory('Material', function($resource) {
    return $resource(WP_API_BASE+"get_post", {}, {
        get : {
            method : "GET",
            isArray : false,
            params : { post_type : "page" }
        }
    });
})

app.factory('SessionPoll', function($resource) {
    return $resource(SERVICE_URL_BASE + "session/maxinactiveinterval", {}, {
        get: {method:   "GET"}
    });
});

app.filter('naturalSort', function() {
    return function(arrInput, field, reverse) {
        var arr = arrInput.sort(function(a, b) {
            var valueA = field ? "a." + field : "a";
            var valueB = field ? "b." + field : "b";
            valueA = eval(valueA);
            valueB = eval(valueB);
            var aIsString = typeof valueA === 'string';
            var bIsString = typeof valueB === 'string';
            return naturalSort(aIsString ? valueA.trim().toLowerCase() : valueA, bIsString ? valueB.trim().toLowerCase() : valueB);
        });
        return reverse ? arr.reverse() : arr;
    };
});

function getLanguageSpecificValue(fieldArray, fieldName, language) {
    if (fieldArray) {
        for (var i = 0; i < fieldArray.length; i++) {
            if (fieldArray[i].kieli === language) {
                var result = eval("fieldArray[i]." + fieldName);
                return result == null ? "" : result;
            }
        }
    }
    return "";
}

function getLanguageSpecificValueOrValidValue(fieldArray, fieldName, language) {
    var specificValue = getLanguageSpecificValue(fieldArray, fieldName, language);

    if (specificValue == "" && language != "FI"){
        specificValue = getLanguageSpecificValue(fieldArray, fieldName, "FI");
    }
    if (specificValue == "" && language != "SV"){
        specificValue = getLanguageSpecificValue(fieldArray, fieldName, "SV");
    }
    if (specificValue == "" && language != "EN"){
        specificValue = getLanguageSpecificValue(fieldArray, fieldName, "EN");
    }
    return specificValue;
}

// Pagination

// Filter used to slice array to start pagination from correct location
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; // parse to int
        return input.slice(start);
    };
});

// Forloop for angularjs
app.filter('forLoop', function() {
    return function(input, start, end) {
        input = new Array(end - start);
        for (var i = 0; start < end; start++, i++) {
            input[i] = start;
        }
        return input;
    };
});

app.run(["SessionPoll", function(SessionPoll) {
    SessionPoll.get({});
}]);

function toDate(enDateString) {
	var frags = enDateString.match(/^(\d{4})-(\d{2})-(\d{2})\s*.*$/);
	console.log(enDateString+" :: "+new Date(frags[1]+"/"+frags[2]+"/"+frags[3]));
	return new Date(frags[1]+"/"+frags[2]+"/"+frags[3]);
}

app.filter('toFinnishDate', function() {
	return function(enDateString) {
		var myDate = toDate(enDateString);
		return myDate.getDate()+"."+(myDate.getMonth()+1)+"."+myDate.getFullYear();
	};
});
