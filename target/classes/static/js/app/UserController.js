'use strict';

angular.module('crudApp').controller('objectNameController',
    ['objectNameService', '$scope','$filter',  function( objectNameService, $scope,$filter) {

        var self = this;
        self.objectName = {};
        self.queryValue = null;
        self.queryValue1 = null;
        self.objectNames=[];

        self.submit = submit;
        self.getAllobjectNames = getAllobjectNames;
        self.createobjectName = createobjectName;
        self.updateobjectName = updateobjectName;
        self.removeobjectName = removeobjectName;
        self.editobjectName = editobjectName;
        self.reset = reset;
        self.objectNameQuery = null;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.prods= {
        		"6": "NAME",
        		"2": "ID",
        		"3": "DESCRIPTION",
        		"4": "FIELD3",
        		"5": "FIELD4",
        		"1": "FIELD5"
        	};
        self.pagedItemsBack = null;
        self.pagedItemsBackF = [];

        
        self.sort = {       
                sortingOrder : 'id',
                reverse : false
            };
    
    self.gap = 5;
    
    self.filteredItems = [];
    self.groupedItems = [];
    self.itemsPerPage = 5;
    self.pagedItems = [];
    self.currentPage = 0;
    self.itemsSort = null;
    self.itemsHeader = null;
    self.itemresul = {};
    self.items = [
        {"ID":1,"NAME":"name 1","DESCRIPTION":"description 1","FIELD3":"field3 1","FIELD4":"field4 1","FIELD5":"field5 1"}, 
        {"ID":2,"NAME":"name 2","DESCRIPTION":"description 1","FIELD3":"field3 2","FIELD4":"field4 2","FIELD5":"field5 2"}
    ];

    var searchMatch = function (haystack, needle) {
        if (!needle) {
            return true;
        }
        return haystack.toLowerCase().indexOf(needle.toLowerCase()) !== -1;
    };
    
    self.valuesFo = function listProducts(prods) {
    	 self.itemresul = {};
		var obj = {};
		var valuew = 1;
		for (const prop in prods) {
			valuew = valuew + 1;
		}
		for (var i = 1; i < valuew; i++) {
			for (const prop in prods) {

				if (prop == i) {
					var a = prods[prop];

					obj[a] = "value";
				}
			}
		}
		 self.itemresul = obj;

	}
    self.notSorted = function (obj) {
		if (!obj) {
			return [];
		}
		return Object.keys(obj);
	}

    // init the filtered items
    self.search = function () {
    	self.valuesFo(self.prods);
        self.filteredItems = $filter('filter')(self.items, function (item) {
            for(var attr in item) {
                if (searchMatch(item[attr], self.query))
                    return true;
            }
            return false;
        });
        // take care of the sorting order
        if (self.sort.sortingOrder !== '') {
            self.filteredItems = $filter('orderBy')(self.filteredItems, self.sort.sortingOrder, self.sort.reverse);
        }
        self.currentPage = 0;
        // now group by pages
        self.groupToPages();
    };
    
  
    // calculate page in place
    self.groupToPages = function () {
        self.pagedItems = [];
        
        self.valuesFo(self.prods);

        self.pagedItemsBack =self.filteredItems;
		for (var ii = 0; ii < self.pagedItemsBack.length; ii++) {
			var valuew = 1;
			var obj = {};
			for (const prop in self.pagedItemsBack[ii]) {
				valuew = valuew + 1;
			}
			for (var i = 1; i < valuew; i++) {
              for (const propnx in self.prods) {  
				for (const prop in self.pagedItemsBack[ii]) {
					var nombre = prop;
					if (self.prods[propnx] == nombre.toUpperCase()) {
						var a = self.prods[propnx];

						obj[a] = self.pagedItemsBack[ii][prop];
      }
					}
				}
			}

			self.pagedItemsBackF[ii] = obj;
		}
		console.log('generateValue');
		console.log(self.pagedItemsBackF);
		console.log(self.filteredItems);
		self.filteredItems =self.pagedItemsBackF;
        
        for (var i = 0; i < self.filteredItems.length; i++) {
            if (i % self.itemsPerPage === 0) {
                self.pagedItems[Math.floor(i / self.itemsPerPage)] = [ self.filteredItems[i] ];
            } else {
                self.pagedItems[Math.floor(i / self.itemsPerPage)].push(self.filteredItems[i]);
            }
        }
    };
    
    self.getHeaders = function(arr) {
        return Object.keys(arr[0]);
      };
    
    self.range = function (size,start, end) {
        var ret = [];        
        console.log(size,start, end);
                      
        if (size < end) {
            end = size;
            start = size-self.gap;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }        
         console.log(ret);        
        return ret;
    };
    
    self.prevPage = function () {
        if (self.currentPage > 0) {
            self.currentPage--;
        }
    };
    
    self.nextPage = function () {
        if (self.currentPage < self.pagedItems.length - 1) {
            self.currentPage++;
        }
    };
    
    self.setPage = function () {
        self.currentPage = this.n;
    };

    // functions have been describe process the data for display
    self.search();

    self.changeSorting = function (column) {
                var sort = self.sort;
                if (sort.sortingOrder == column) {
                    sort.reverse = !self.sort.reverse;
                } else {
                    sort.sortingOrder = column;
                    sort.reverse = false;
                }
            };






            self.selectedCls2 = function (column) {
                if (column == self.sort.sortingOrder) {
                    return ('fa fa-chevron-' + ((self.sort.reverse) ? 'down' : 'up'));
                }
                else {
                    return 'fa fa-sort'
                }
            };
        
        

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.objectName.id === undefined || self.objectName.id === null) {
                console.log('Saving New objectName', self.objectName);
                createobjectName(self.queryValue);
            } else {
                updateobjectName(self.objectName, self.objectName.id);
                console.log('objectName updated with id ', self.objectName.id);
            }
        }

        function createobjectName(objectName) {
            console.log('About to create objectName');
            self.queryValue1 = objectName;
           
            var hola =objectName;
            
            var holas=[];
            var hola = { objectName: 'select count(*) from dual' };
            holas[0]= hola;
            hola.objectName=objectName;
            objectNameService.createobjectName(holas)
                .then(
                    function (response) {
                        console.log('objectName created successfully');
                        self.successMessage = 'Query OK';
                        self.errorMessage='';
                        self.done = true;
                        self.objectName={};
                        self.items =response.Values;
                        self.prods =response.Order;
                        self.queryValue= self.queryValue1;
                        $scope.myForm.$setPristine();
                        self.search();
                    },
                    function (errResponse) {
                    	 self.queryValue= self.queryValue1;
                        console.error('Error while creating objectName');
                        //self.errorMessage = 'Error while query objectName: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateobjectName(objectName, id){
            console.log('About to update objectName');
            objectNameService.updateobjectName(objectName, id)
                .then(
                    function (response){
                        console.log('objectName updated successfully');
                        self.successMessage='objectName updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating objectName');
                        self.errorMessage='Error while updating objectName '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeobjectName(id){
            console.log('About to remove objectName with id '+id);
            objectNameService.removeobjectName(id)
                .then(
                    function(){
                        console.log('objectName '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing objectName '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllobjectNames(){
            return objectNameService.getAllobjectNames();
        }

        function editobjectName(id) {
            self.successMessage='';
            self.errorMessage='';
            objectNameService.getobjectName(id).then(
                function (objectName) {
                    self.objectName = objectName;
                },
                function (errResponse) {
                    console.error('Error while removing objectName ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.objectName={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);