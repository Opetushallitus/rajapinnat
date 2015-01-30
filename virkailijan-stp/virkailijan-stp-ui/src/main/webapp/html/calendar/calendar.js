angular.module("template/datepicker/year.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/datepicker/year.html",
    "<table role=\"grid\" aria-labelledby=\"{{uniqueId}}-title\" aria-activedescendant=\"{{activeDateId}}\">\n" +
    "  <thead>\n" +
    "    <tr>\n" +
    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(-1)\" tabindex=\"-1\"><b>&lt;</b></button></th>\n" +
    "      <th colspan=\"3\"><button id=\"{{uniqueId}}-title\" role=\"heading\" aria-live=\"assertive\" aria-atomic=\"true\" type=\"button\" class=\"btn calendar nextprev\" ng-click=\"toggleMode()\" tabindex=\"-1\" style=\"width:100%;\"><strong>{{title}}</strong></button></th>\n" +
    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(1)\" tabindex=\"-1\"><b>&gt;</b></button></th>\n" +
    "    </tr>\n" +
    "  </thead>\n" +
    "  <tbody>\n" +
    "    <tr ng-repeat=\"row in rows track by $index\">\n" +
    "      <td ng-repeat=\"dt in row track by dt.date\" class=\"text-center\" role=\"gridcell\" id=\"{{dt.uid}}\" aria-disabled=\"{{!!dt.disabled}}\">\n" +
    "        <button type=\"button\" style=\"width:100%;\" class=\"btn calendar\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt), 'text-info': dt.current}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-info': dt.current}\">{{dt.label}}</span></button>\n" +
    "      </td>\n" +
    "    </tr>\n" +
    "  </tbody>\n" +
    "</table>\n" +
    "");
}]);

var monthNamesS =
	"<div ng-show=\"(dt.date.getMonth() == 0)\">{{'calendar.january' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 1)\">{{'calendar.february' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 2)\">{{'calendar.march' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 3)\">{{'calendar.april' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 4)\">{{'calendar.may' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 5)\">{{'calendar.june' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 6)\">{{'calendar.july' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 7)\">{{'calendar.august' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 8)\">{{'calendar.september' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 9)\">{{'calendar.october' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 10)\">{{'calendar.november' | i18n}}</div>"+
	"<div ng-show=\"(dt.date.getMonth() == 11)\">{{'calendar.december' | i18n}}</div>";

angular.module("template/datepicker/month.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/datepicker/month.html",
    "<table role=\"grid\" aria-labelledby=\"{{uniqueId}}-title\" aria-activedescendant=\"{{activeDateId}}\">\n" +
    "  <thead>\n" +
    "    <tr>\n" +
    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(-1)\" tabindex=\"-1\"><b>&lt;</b></button></th>\n" +
    "      <th><button id=\"{{uniqueId}}-title\" role=\"heading\" aria-live=\"assertive\" aria-atomic=\"true\" type=\"button\" class=\"btn calendar nextprev\" ng-click=\"toggleMode()\" tabindex=\"-1\" style=\"width:100%;\"><strong>{{title}}</strong></button></th>\n" +
    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(1)\" tabindex=\"-1\"><b>&gt;</b></button></th>\n" +
    "    </tr>\n" +
    "  </thead>\n" +
    "  <tbody>\n" +
    "    <tr ng-repeat=\"row in rows track by $index\">\n" +
    "      <td ng-repeat=\"dt in row track by dt.date\" class=\"text-center\" role=\"gridcell\" id=\"{{dt.uid}}\" aria-disabled=\"{{!!dt.disabled}}\">\n" +
    "        <button type=\"button\" style=\"width:100%;\" class=\"btn calendar\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt), 'text-info': dt.current}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-info': dt.current}\">"+monthNamesS+"</span></button>\n" +
    "      </td>\n" +
    "    </tr>\n" +
    "  </tbody>\n" +
    "</table>\n" +
    "");
}]); 

var outsideMonthB =
	"(( $parent.$index<2 && dt.date.getDate()>15 ) ||"+
	" ( $parent.$index>3 && dt.date.getDate()<19 ))";

var weekendDayB = " ($index > 4) ";

var monthNameS =
	"<div ng-show=\"(rows[3][3].date.getMonth() == 0)\">{{'calendar.january' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 1)\">{{'calendar.february' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 2)\">{{'calendar.march' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 3)\">{{'calendar.april' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 4)\">{{'calendar.may' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 5)\">{{'calendar.june' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 6)\">{{'calendar.july' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 7)\">{{'calendar.august' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 8)\">{{'calendar.september' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 9)\">{{'calendar.october' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 10)\">{{'calendar.november' | i18n}}</div>"+
	"<div ng-show=\"(rows[3][3].date.getMonth() == 11)\">{{'calendar.december' | i18n}}</div>";

var weekdayNameS =
 	"<div ng-show=\"($index == 0)\">{{'calendar.monday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 1)\">{{'calendar.tuesday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 2)\">{{'calendar.wednesday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 3)\">{{'calendar.thursday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 4)\">{{'calendar.friday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 5)\">{{'calendar.saturday' | i18n}}</div>"+
 	"<div ng-show=\"($index == 6)\">{{'calendar.sunday' | i18n}}</div>";

angular.module("template/datepicker/day.html", []).run(["$templateCache", function($templateCache) {
	  $templateCache.put("template/datepicker/day.html",
	    "<table role=\"grid\" aria-labelledby=\"{{uniqueId}}-title\" aria-activedescendant=\"{{activeDateId}}\">\n" +
	    "  <thead>\n" +
	    "    <tr>\n" +
	    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(-1)\" tabindex=\"-1\" ><b>&lt;</b></button></th>\n" +
	    "      <th colspan=\"{{5 + showWeeks}}\"><button id=\"{{uniqueId}}-title\" role=\"heading\" aria-live=\"assertive\" aria-atomic=\"true\" type=\"button\" class=\"btn calendar month\" ng-click=\"toggleMode()\" style=\"width:100%;\" tabindex=\"-1\" ><strong>"+monthNameS+"</strong></button></th>\n" +
	    "      <th><button type=\"button\" class=\"btn calendar nextprev\" ng-click=\"move(1)\" tabindex=\"-1\"><b>&gt;</b></button></th>\n" +
	    "    </tr>\n" +
	    "    <tr>\n" +
	    "      <th ng-show=\"showWeeks\" class=\"eventCalendar\"></th>\n" +
	    "      <th ng-repeat=\"label in labels track by $index\"><button type=\"button\" tabindex=\"-1\" class=\"btn calendar weekdaynames\">"+weekdayNameS+"</button></th>\n" +
	    "    </tr>\n" +
	    "  </thead>\n" +
	    "  <tbody>\n" +
	    "    <tr ng-repeat=\"row in rows track by $index\">\n" +
	    "      <td ng-show=\"showWeeks\" class=\"eventCalendar\"><em>{{ weekNumbers[$index] }}</em></td>\n" +
	    "      <td ng-repeat=\"dt in row track by dt.date\" role=\"gridcell\" id=\"{{dt.uid}}\" aria-disabled=\"{{!!dt.disabled}}\">\n" +
	    "        <button ng-show=\""+outsideMonthB +" && " +weekendDayB+"\" type=\"button\" class=\"btn calendar weekend outsidemonth\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt), 'text-info': dt.current}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-muted': dt.secondary}\">{{dt.label}}</span></button>\n" +
	    "        <button ng-show=\"!"+outsideMonthB+" && " +weekendDayB+"\" type=\"button\" class=\"btn calendar weekend\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt)}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-muted': dt.secondary, 'text-info': dt.current}\">{{dt.label}}</span></button>\n" +    
	    "        <button ng-show=\""+outsideMonthB +" && !"+weekendDayB+"\" type=\"button\" class=\"btn calendar outsidemonth\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt), 'text-info': dt.current}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-muted': dt.secondary}\">{{dt.label}}</span></button>\n" +
	    "        <button ng-show=\"!"+outsideMonthB+" && !"+weekendDayB+"\" type=\"button\" class=\"btn calendar\" ng-class=\"{'btn-info': dt.selected, active: isActive(dt), 'text-info': dt.current}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\" tabindex=\"-1\"><span ng-class=\"{'text-muted': dt.secondary, 'text-info': dt.current}\">{{dt.label}}</span></button>\n" +    
	    "      </td>\n" +
	    "    </tr>\n" +
	    "  </tbody>\n" +
	    "</table>\n" +
	    "");
	}]);

var DAYS_TO_REQUEST_AT_ONCE=42;

app.service("CalendarUtil", function() {
	this.stringifyDate = function(date, separator) {
		return (date.getFullYear())+separator+(date.getMonth()<9 ?  "0" : "")+(date.getMonth()+1) +separator+(date.getDate()<10 ?  "0" : "") + date.getDate();
	};
	this.toDate = function(wpDate) {
		return new Date( wpDate.replace(/(\d+)-(\d+)-(\d+)/,"$1/$2/$3") );
	}
	this.toWPDate = function(date) {
		return this.stringifyDate(date, '-');
	}
	this.toArray = function(startDate, endDate) {
		var resultList = [];
		if (endDate < startDate)  {
			return resultList;
		}
		var start= this.toDate(this.toWPDate(startDate));
		var end = this.toDate(this.toWPDate(endDate));
		do {
			resultList[resultList.length]= new Date(start);
			start.setDate(start.getDate()+1);
		} while (start<=end);
		return resultList;
	}
});

app.factory("EventDates", function(Events, CalendarUtil, $q) {
    var model;
    model = new function() {
    	this.getEventsPromise = function(date) {
	    	var deferred = $q.defer();
	    	Events.get(
	    	    { 'scope' :CalendarUtil.toWPDate(date)+","+CalendarUtil.toWPDate(date) },
		       	function(result) {
			       		deferred.resolve(result);
	    	    });
	    	return deferred.promise;	
    	}
		this.countEventsPromise = function (startDate, endDate) {
			var hashDate = function(date) {
				return "K"+CalendarUtil.toWPDate(date);
			}
			var toWPDate = function(hashDate) {
				return hashDate.substring(1);
			}
	    	var deferred = $q.defer();
	       	Events.get(
	       	    { 'scope' : CalendarUtil.toWPDate(startDate)+","+CalendarUtil.toWPDate(endDate) }, 
	       	    function(result) {
	       	    	var e = {}; //hash for storing retrieved dates for requested span
	       			var span = CalendarUtil.toArray(startDate,endDate);
	       			$(span).each(function (idx, el) { e[hashDate(el)] = 0; }); //init to 0
		       		if (typeof result.events != 'undefined') {
		       			$(result.events).each(function (i, event) {
	       					var eventDays = CalendarUtil.toArray(CalendarUtil.toDate(event.event_date_time.event_start_date), 
	       														 CalendarUtil.toDate(event.event_date_time.event_end_date));
	       					$(eventDays).each(function (i, eventDay) {
	       						if (typeof e[(key = hashDate(eventDay))] == 'undefined') {
	       							e[key] = 0;
	       						}
	       						e[key]++;
	       					});
		       			});
		       		}
		       		var returnVal = [];
		       		for (var i in e) {
		       			returnVal[returnVal.length] = {
		       			    "date" : toDate(toWPDate(i)),
		       				"eventsCount" : e[i]
		       			}
		       		}
		       		deferred.resolve(returnVal);
	       	    });
	       		return deferred.promise;			
			}
    	}
    	return model;
	}
);

app.factory('CalendarModel', function(EventDates, CalendarUtil, $q) {
    var model;
    model = new function() {
    	this.init = function(sce, refreshCallback) {
    		model.eventsReady = false;
    		model.sce = sce;
    		model.refreshCallback = refreshCallback;
    		model.d = {}; //hash for storing retrieved dates
    		model.ReqsPendingCount = 0;
    	}
    	this.getEvents = function(date) {
    		model.eventsReady = false;
        	(EventDates.getEventsPromise(date)).then(
             	function(result){
             		model.events = result.events;
             		$(model.events).each(function (idx, el) { el.hmlContent = model.sce.trustAsHtml(el.content) }); 
             		model.eventsReady = true;
             	}
            );
    	}
    	this.isDateDisabled = function(mode, date) {
        	if (mode != 'day') {
        		return false;
    		}
        	var hashDate = function (date) {
        		return "D"+CalendarUtil.stringifyDate(date,"_");
        	} 
        	var key = hashDate(date);
        	if (typeof (val = model.d[key]) != 'undefined') {
        		return val;
        	}
        	if (model.ReqsPendingCount > 0) {
        		return true;
        	}
        	model.ReqsPendingCount++;
        	var start = new Date(date.getFullYear(),date.getMonth(),date.getDate());
        	var end = new Date(date.getFullYear(),date.getMonth(),date.getDate()+DAYS_TO_REQUEST_AT_ONCE);
        	(EventDates.countEventsPromise(start, end)).then(
         		function(results){
         			$(results).each(function (i, result)  {
         				model.d[hashDate(result.date)] = (result.eventsCount == 0);
         			});
         			model.ReqsPendingCount--;
         			if (model.ReqsPendingCount == 0) {
         				model.refreshCallback();
         			}
         		}
         	);
        	return true;
        };
    };
    return model;
});

function ViewCalendarController($scope, $routeParams, $sce , CalendarModel, CalendarUtil, $log, $q, $http, $sce, Events) {
    $scope.identity = angular.identity;
    $scope.model = CalendarModel;
    $scope.model.init($sce,
    	function () {
    		$scope.dummyDate = new Date(1900,1,1); //workaround to force refresh
    	}
    );
    $scope.isDateDisabled = $scope.model.isDateDisabled;
    $scope.$watch('eventdate',function(newValue, oldValue) {
    	if (newValue) {
    		$scope.eventdatename = CalendarUtil.toWPDate(newValue);
    		$scope.model.getEvents(newValue);
    	}
    });	
}

