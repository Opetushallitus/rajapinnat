app.factory('EventModel', function(Event) {
    var model;
    model = new function() {
    	this.init = function(scope, tapahtumaid, sce) {
            model.getEvent(scope, tapahtumaid, sce);
        };
        this.getEvent = function(scope, tapahtumaid, sce) {
        	Event.get({ id : tapahtumaid }, function(result) {
        		model.event = result;
        		scope.htmlContent = sce.trustAsHtml(result.content);
        	});
        };
    };
    return model;
});

function ViewEventController($scope, $routeParams, $sce, EventModel) {
    $scope.model = EventModel;
    $scope.identity = angular.identity;
    EventModel.init($scope, $routeParams.tapahtumaid, $sce);
}