
app.factory('LatestAnnouncementsPopulator', function(LatestAnnouncements, $filter) {
	return new ModelPopulator(LatestAnnouncements, $filter('i18n')("desktop.announcements.messages.errors.loadingannouncements"));
});

app.factory('LatestAnnouncementsUIModel', function(LatestAnnouncementsPopulator) {	
	return new UIFilterModel(LatestAnnouncementsPopulator).setParams({});
});

app.factory('LatestEventsPopulator', function(Events, $filter) {
	var eventsResultValidator = new PostResultValidator;
	eventsResultValidator.result = function(result) {
		return result.events;
	}
	return new ModelPopulator(Events, $filter('i18n')("desktop.events.messages.errors.loadingevents"), eventsResultValidator);
});

app.factory('LatestEventsUIModel', function(LatestEventsPopulator) {	
	return new UIFilterModel(LatestEventsPopulator).setParams({});
});

app.factory('LatestMaterialsPopulator', function(LatestMaterials, $filter) {
	return new ModelPopulator(LatestMaterials, $filter('i18n')("desktop.materials.messages.errors.loadingmaterials"));
});

app.factory('LatestMaterialsUIModel', function(LatestMaterialsPopulator) {	
	return new UIFilterModel(LatestMaterialsPopulator).setParams({});
});

app.factory('TextSearchAnnoucementsPopulator', function(TextSearchAnnouncements, $filter) {
	return new ModelPopulator(TextSearchAnnouncements, $filter('i18n')("desktop.announcements.messages.errors.loadingannouncements"));
});

app.factory('ArchiveAnnouncementsUIModel', function(TextSearchAnnoucementsPopulator) {
	return new UIFilterModel(TextSearchAnnoucementsPopulator);
});

app.factory('TextSearchMaterialsPopulator', function(TextSearchMaterials, $filter) {
	return new ModelPopulator(TextSearchMaterials, $filter('i18n')("desktop.materials.messages.errors.loadingmaterials"));
});

app.factory('ArchiveMaterialsUIModel', function(TextSearchMaterialsPopulator) {
	return new UIFilterModel(TextSearchMaterialsPopulator);
});

app.factory("SearchTxtUIModel", function(AnnouncementsSortOrderUIModel, MaterialsSortOrderUIModel) {
	var model = new function() {
		this.searchtext = "";
		this.search = function() {
			model.searchedtext = model.searchtext;
			AnnouncementsSortOrderUIModel.search(model.searchtext);
			MaterialsSortOrderUIModel.search(model.searchtext);
		}
	}
	return model;
});

var SearchUI = function(UIModel) {
	var model = new function() {
		this.sort_order = "title ASC";
		this.order_by = "title";
		this.order ="ASC";
		this.searchtext = "";
		this.search = function(txt) {
			model.searchtext =  txt;
			model.setParams();
		};
		this.sort = function() {
			var _sort=this.sort_order.split(" ");
			model.order_by=_sort[0];
			model.order=_sort[1];
			if (UIModel.ready) {
				model.setParams();
			}		
		}
		this.setParams = function() {
			UIModel.setParams(
					{ "search"  : model.searchtext,
					  "order"   : model.order,
					  "orderby" : model.order_by
			});
		}
	}
	return model;
};

app.factory("AnnouncementsSortOrderUIModel", function(ArchiveAnnouncementsUIModel) {
	return new SearchUI(ArchiveAnnouncementsUIModel);
});

app.factory("MaterialsSortOrderUIModel", function(ArchiveMaterialsUIModel) {
	return new SearchUI(ArchiveMaterialsUIModel);
});

app.factory('CategoriesPopulator', function(Categories, $filter) {
	var categoriesResultValidator = new PostResultValidator;
	categoriesResultValidator.result = function(result) {
		return result.categories;
	}	
	return new ModelPopulator(Categories, $filter('i18n')("desktop.materials.messages.errors.loadingcategories"), categoriesResultValidator);
});

app.factory('SelectedCategoriesModel', function(LatestAnnouncementsUIModel, LatestEventsUIModel, LatestMaterialsUIModel, ArchiveAnnouncementsUIModel, ArchiveMaterialsUIModel, CategoriesUIModel, UserOrganisations, Profiles) {
	var model = new function() {
		this.ready = false;
		this.rows = [];
		this.clicked = function(category) {
			if (category.checked && !this.rows.contains(category)) {
				this.rows.push(category);
			}
			if (!category.checked) {
				this.rows.remove(category);
			}
			CategoriesUIModel.clicked(category);
			LatestAnnouncementsUIModel.clicked(category, 1);
			LatestEventsUIModel.clicked(category, 1);
			LatestMaterialsUIModel.clicked(category, 1);

			ArchiveAnnouncementsUIModel.clicked(category, 1);
			ArchiveMaterialsUIModel.clicked(category, 1);
		}
		this.extraSettings = {externalIdProp: '',
			displayProp: 'title', 
			idProp: 'slug',
			buttonClasses: 'btn btn-default dropdown-multiselect', 
			showCheckAll : false, 
			showUncheckAll: false,
			smartButtonMaxItems: 15,
			smartButtonTextConverter: function(itemText, originalItem) {
				return itemText;
		}};
		this.categoriesevents = {
			onItemSelect :  function (category) {
				category.checked = true;
				model.clicked(category);
			},
			onItemDeselect :  function (item) {
				//there's bug in library (angularjs-dropdown-multiselect.min.js): item is not category-object (externalIdProp: '') 
				// - like on 'onItemSelect' - but Object on its own 
				var category = {};
				category = _.filter(CategoriesUIModel.rows, function(_category) {
				    return _category.slug == item.slug;
				})[0];
				category.checked = false;
				model.clicked(category);
			}
		};
		this.buttonTexts = {buttonDefaultText: 'Kaikki'};
		this.init = function() {
			if (model.ready) {
				return;
			}
			model.ready=true;
			var getProfiles = function(orgOid) {
				Profiles.post([orgOid],function(slugs){
					_(_.filter(CategoriesUIModel.rows, function(category) {
						return slugs.contains(category.slug);
					})).forEach(function(category){
						category.checked = true;
						model.clicked(category)
					});
				});
			}
			UserOrganisations.get([],
				function(orgs) {
					_(orgs).forEach(function(org) {
						getProfiles(org.organisaatioOid);
				});
			});
		}
	}
	return model;
});

app.factory('CategoriesUIModel', function(CategoriesPopulator) {
	var model = new UIListModel(CategoriesPopulator).setParams({});
	model.clicked = function(category) {
		if (category.checked) {
			category.f = function(row) {
				if (_.isUndefined(row.categories)) {
					return false;
				}
				return $.map( row.categories, function( _category ) {
					return _category.slug;
				}).contains(category.slug);
			}
		} 
	}
	return model;
});

function PostsController($scope, breadcrumbs, LatestAnnouncementsUIModel, LatestEventsUIModel, LatestMaterialsUIModel, SearchTxtUIModel, Profiles, CategoriesUIModel, SelectedCategoriesModel, $location) {
	$scope.selectedcategoriesmodel = SelectedCategoriesModel;
	$scope.categoriesmodel = CategoriesUIModel;
	$scope.$watch('categoriesmodel.ready', function(newValue, oldValue) {
		if (newValue) {
			$scope.selectedcategoriesmodel.init();
		}
    });
	$scope.breadcrumbs = breadcrumbs;
	$scope.identity = angular.identity;
	$scope.announcementsmodel = LatestAnnouncementsUIModel
	$scope.eventsmodel = LatestEventsUIModel
	$scope.materialsmodel = LatestMaterialsUIModel
	$scope.searchmodel = SearchTxtUIModel;
	$scope.location = $location;
	$scope.toArchive = function() {
		SearchTxtUIModel.search();
		$scope.location.path("/etusivu/arkisto");
	}
}
