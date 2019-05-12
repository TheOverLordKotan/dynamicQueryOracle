'use strict';

angular.module('crudApp').factory('objectNameService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllobjectNames: loadAllobjectNames,
                getAllobjectNames: getAllobjectNames,
                getobjectName: getobjectName,
                createobjectName: createobjectName,
                updateobjectName: updateobjectName,
                removeobjectName: removeobjectName
            };

            return factory;

            function loadAllobjectNames() {
                console.log('Fetching all objectNames');
                var deferred = $q.defer();
                $http.get(urls.objectName_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all objectNames');
                            $localStorage.objectNames = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading objectNames');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllobjectNames(){
                return $localStorage.objectNames;
            }

            function getobjectName(id) {
                console.log('Fetching objectName with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.objectName_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully objectName with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading objectName with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createobjectName(objectName) {
                console.log('Creating objectName');
                var deferred = $q.defer();
                $http.post(urls.objectName_SERVICE_API+'getObjectsByName', objectName)
                    .then(
                        function (response) {
                            
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating objectName : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateobjectName(objectName, id) {
                console.log('Updating objectName with id '+id);
                var deferred = $q.defer();
                $http.put(urls.objectName_SERVICE_API + id, objectName)
                    .then(
                        function (response) {
                            
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating objectName with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeobjectName(id) {
                console.log('Removing objectName with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.objectName_SERVICE_API + id)
                    .then(
                        function (response) {
                            
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing objectName with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);