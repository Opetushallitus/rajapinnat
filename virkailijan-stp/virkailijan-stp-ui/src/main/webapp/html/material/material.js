app.factory('MaterialModel', function(Material) {
    var model;
    model = new function() {
    	this.init = function(scope, materiaaliid, sce) {
            model.getEvent(scope, materiaaliid, sce);
        };
        this.getEvent = function(scope, materiaaliid, sce) {
        	Material.get({ id : materiaaliid }, function(result) {
        		model.material = result.post;
        		scope.htmlContent = sce.trustAsHtml(result.post.content);
        		scope.loadingReady = true;
        	});
        };
    };
    return model;
});

function ViewMaterialController($scope, $routeParams, $sce, MaterialModel) {
    $scope.model = MaterialModel;
    $scope.identity = angular.identity;
    MaterialModel.init($scope, $routeParams.materiaaliid, $sce);
}