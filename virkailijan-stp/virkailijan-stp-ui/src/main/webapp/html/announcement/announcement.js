app.factory('AnnouncementModel', function(Announcement) {
    var model;
    model = new function() {
    	this.init = function(scope, tiedoteid, sce) {
            model.getAnnouncement(scope, tiedoteid, sce);
        };
        this.getAnnouncement = function(scope, tiedoteid, sce) {
        	Announcement.get({ id : tiedoteid }, function(result) {
        		model.announcement = result.post;
        		scope.htmlContent = sce.trustAsHtml(result.post.content);
        		scope.loadingReady = true;
        	});
        };
    };
    return model;
});

function ViewAnnouncementController($scope, $routeParams, $sce, AnnouncementModel) {
    $scope.model = AnnouncementModel;
    $scope.identity = angular.identity;
    AnnouncementModel.init($scope, $routeParams.tiedoteid, $sce);
}