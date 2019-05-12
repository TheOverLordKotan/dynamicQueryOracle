var app = angular.module('crudApp',['ui.router','ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8770/Qc',
    objectName_SERVICE_API : 'http://localhost:8770/Qc/api/'
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'partials/list',
                controller:'objectNameController',
                controllerAs:'ctrl',
                resolve: {
                    objectNames: function ($q, objectNameService) {
                        console.log('Load all objectNames');
                        var deferred = $q.defer();
                        objectNameService.loadAllobjectNames().then(deferred.resolve, deferred.resolve);
                        return deferred.promise;
                    }
                }
            });
        $urlRouterProvider.otherwise('/');
    }]);

