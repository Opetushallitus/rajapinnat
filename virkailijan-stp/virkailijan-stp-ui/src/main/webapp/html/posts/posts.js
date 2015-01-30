var INITIALLINES=2;
var SHOWMORELINES=1;


function PostList(loader) {
	var model;
	   	model = new function() {
	        this.hasalerts = function() {
	        	return this.alerts.length>0;
	        }
	        this.maxlines = function() {
	        	return loader.maxlines;
	        }
	        this.showmore = function() {
	        	loader.showmore();
	        }
	        this.refresh = function() {
	        	model.load(true);
	        }
	        this.loadSuccess = function(rows) {
	        	model.rows = rows;
	        	model.ready = true;
	        }
	        this.loadError = function(error) {
	        	model.alerts.push(error);
	        	model.ready = true;
	        }
	        this.load = function(force) {
		   		this.ready = false;
		   		this.alerts = [];
		   		this.rows = [];
	        	loader.load(model.loadSuccess, model.loadError, force);
	        };
	   	};
	   	model.load(false);
	   	return model;
}

app.factory('AnnouncementsLoader', function(Announcements, $filter) {
    var loader;
    loader = new function() {
    	this.showmore = function() {
    		loader.maxlines += SHOWMORELINES;
    	}
        this.load = function(loadSuccess, loadError, force) {
        	if (!loader.maxlines) {
        		loader.maxlines = INITIALLINES;
        	}
        	if (!force && loader.cache) {
        		loadSuccess(loader.cache);	//load row from cache
        	} else {
        		loader.cache = null;
        		Announcements.get({}, function(result) {
			        if (result.status == "ok") {
			        	loader.cache = result.posts;
			        	loadSuccess(loader.cache);
			        } else {
			        	alert = { msg:result.error }
			        	loadError(alert);
			        }
        		}, function(error) {
        			alert = { msg: $filter('i18n')("desktop.announcements.messages.errors.loadingannouncements") }
			        loadError(alert);
			    });
        	}
        };
    };
    return loader;
});

app.factory('EventsLoader', function(Events, $filter) {
    var loader;
    loader = new function() {
        this.load = function(loadSuccess, loadError, force) {
        	this.showmore = function() {
        		loader.maxlines += SHOWMORELINES;
        	}        	
        	if (!loader.maxlines) {
        		loader.maxlines = INITIALLINES;
        	}
        	if(!force && loader.cache) {
        		loadSuccess(loader.cache);
        	} else {
        		loader.cache = null;
        		Events.get({}, function(result) {
        			if (result.status == "ok") {
        				loader.cache = result.events;
        				loadSuccess(loader.cache);
        			} else {
        				alert = { msg:result.error }
        				loadError(alert);
        			}
        		}, function(error) {
        			alert = { msg:$filter('i18n')("desktop.announcements.messages.errors.loadingevents") }
			        loadError(alert);
			    });
        	}
        };
    };
    return loader;
});

app.factory('MaterialsLoader', function(Materials, $filter) {
    var loader;
    loader = new function() {
        this.load = function(loadSuccess, loadError, force) {
        	this.showmore = function() {
        		loader.maxlines += SHOWMORELINES;
        	}
        	if (!loader.maxlines) {
        		loader.maxlines = INITIALLINES;
        	}
        	if (!force && loader.cache) {
        		loadSuccess(loader.cache);
        	} else {
        		loader.cache = null;
        		Materials.get({}, function(result) {
			        if (result.status == "ok") {
			        	loader.cache = result.posts;
			        	loadSuccess(loader.cache);
			        } else {
			        	alert = { msg:result.error }
			        	loadError(alert);
			        }
        		}, function(error) {
        			alert = { msg:$filter('i18n')("desktop.announcements.messages.errors.loadingmaterials") }
			        loadError(alert);
			    });
        	}
        };
    };
    return loader
});

function PostsController($scope, AnnouncementsLoader, EventsLoader, MaterialsLoader) {
   $scope.identity = angular.identity;
   $scope.announcementsmodel = PostList(AnnouncementsLoader);
   $scope.eventsmodel = PostList(EventsLoader);
   $scope.materialsmodel = PostList(MaterialsLoader);
}