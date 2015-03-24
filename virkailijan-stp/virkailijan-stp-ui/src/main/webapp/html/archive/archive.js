var YearTreeUI = function($filter, CalendarUtil, ClickUIModel){
	var year = function(year) {
		this.open = false;
		this.name = year;
		this.f = function(row) {
			return (CalendarUtil.toDate(row.date)).getFullYear() == year;
		}
		this.months = [];
		var month = function(month, order) {
			this.name = month;
			this.f = function(row) {
				var d = CalendarUtil.toDate(row.date);
				return d.getFullYear() == year && d.getMonth() == (order-1);
			}
		};
		for (var i=1; i<=12; i++) {
			this.months.push(new month($filter('i18n')("calendar.month."+i),i));
		}
	}
	var model = function() {
		this.clicked = function(node, group) {
			if (typeof node.months != "undefined") { //is it year-node?
				if (!node.checked) { //clear selected months if there are any
					_(node.months).forEach(function(month) {
						month.checked = false;
						ClickUIModel.clicked(month, group);
					});
				}
			}
			ClickUIModel.clicked(node, group);
		}
		this.years = [];
		var d = parseInt((new Date()).getFullYear());
		for (var i=0; i<10; i++) {
			this.years.push(new year(""+(d-i)));
		}
	};
	return new model;
}

app.factory('AnnouncementsYearTreeUIModel', function($filter, CalendarUtil, ArchiveAnnouncementsUIModel) {
	return new YearTreeUI($filter, CalendarUtil, ArchiveAnnouncementsUIModel );
});

app.factory('MaterialsYearTreeUIModel', function($filter, CalendarUtil, ArchiveMaterialsUIModel) {
	return new YearTreeUI($filter, CalendarUtil, ArchiveMaterialsUIModel);
});

app.factory("TabsStateUIModel", function() {
	var model = new function() {
		this.tab0 = true;
		this.tab1 = false;
		this.set_activetab = function(tab) {
			tab = true;
		}
	}
	return model;
});

function ArchiveController($scope,
			breadcrumbs,
			TabsStateUIModel,
			SearchTxtUIModel,
			AnnouncementsSortOrderUIModel,
			MaterialsSortOrderUIModel,
			CategoriesUIModel,
			ArchiveAnnouncementsUIModel,
			ArchiveMaterialsUIModel,
			AnnouncementsYearTreeUIModel,
			MaterialsYearTreeUIModel,
			SelectedCategoriesModel) {
	$scope.identity = angular.identity;
	$scope.breadcrumbs = breadcrumbs;
	$scope.selectedcategoriesmodel = SelectedCategoriesModel;
	$scope.categoriesmodel = CategoriesUIModel;
	$scope.$watch('categoriesmodel.ready', function(newValue, oldValue) {
		if (newValue) {
			$scope.selectedcategoriesmodel.init();
		}
    });
	
	$scope.announcementsmodel = ArchiveAnnouncementsUIModel;
	$scope.announcementsortmodel = AnnouncementsSortOrderUIModel;
	$scope.announcementsyear_tree = AnnouncementsYearTreeUIModel;
	
	$scope.materialsmodel = ArchiveMaterialsUIModel;
	$scope.materialssortmodel = MaterialsSortOrderUIModel;
	$scope.materialsyear_tree = MaterialsYearTreeUIModel;

	$scope.tabState = TabsStateUIModel;
	$scope.searchmodel = SearchTxtUIModel;
}

app.controller('announcementsTabCtrl', ['$scope', 'AnnouncementsSortOrderUIModel', 'ArchiveAnnouncementsUIModel', function($scope, AnnouncementsSortOrderUIModel, ArchiveAnnouncementsUIModel) {
	$scope.sortmodel = AnnouncementsSortOrderUIModel;
	$scope.paginationmodel = ArchiveAnnouncementsUIModel;
}]).controller('materialsTabCtrl', ['$scope', 'MaterialsSortOrderUIModel', 'ArchiveMaterialsUIModel', function($scope, MaterialsSortOrderUIModel, ArchiveMaterialsUIModel) {
	$scope.sortmodel = MaterialsSortOrderUIModel;
	$scope.paginationmodel = ArchiveMaterialsUIModel;
}]).directive('stpPagination', function() {
	return {
		template:
			"<div style=\"margin:0 ; padding:0 ; vertical-align: top; line-height: 8px;\" class=\"pagination\">"+
			"<table>"+
			"    <tr>"+
			"        <td style=\"padding-left: 5px; padding-right: 5px;\">"+
			"            <h6>{{'archive.pagination.text.size' | i18n}}</h6>"+
			"        </td>"+
			"        <td>"+
			"            <select class=\"span4 ng-valid ng-dirty\" style=\"width: 4em; vertical-align: top; margin:0;\" ng-model=\"paginationmodel.pagesize\">"+
			"                <option value=\"10\">10</option>"+
			"                <option value=\"20\">20</option>"+
			"                <option value=\"50\">50</option>"+
			"            </select>"+
			"        </td>"+
			"        <td>"+
			"            <select class=\"span4 ng-valid ng-dirty\" style=\"padding-left: 10px; width: 15em; vertical-align: top;  margin:0;\" ng-model=\"sortmodel.sort_order\" ng-change=\"sortmodel.sort()\">"+
			"                <option value=\"title ASC\">{{'archive.sort.order.text.title_asc' | i18n}}</option>"+
			"                <option value=\"title DESC\">{{'archive.sort.order.text.title_desc' | i18n}}</option>"+
			"                <option value=\"date ASC\">{{'archive.sort.order.text.date_asc' | i18n}}</option>"+
			"                <option value=\"date DESC\">{{'archive.sort.order.text.date_desc' | i18n}}</option>"+
			"            </select>"+
			"        </td>"+
			"        <td ng-if=\"paginationmodel.rows.length>0\">"+
			"            <pagination style=\"margin:0\" items-per-page=\"paginationmodel.pagesize\" total-items=\"paginationmodel.rows.length\" ng-model=\"paginationmodel.pagenr\" ng-change=\"paginationmodel.pageChanged()\" previous-text=\"&larr;\" next-text=\"&rarr;\"></pagination>"+
			"        </td>"+
			"    </tr>"+
			"</table>"+
			"</div>"
	};
});