<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">objectName </span></div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">Query</label>
	                        <div class="col-md-7">
	                            <input type="text" ng-model="ctrl.queryValue" id="uname" class="username form-control input-sm" placeholder="Enter your query" required ng-minlength="3"/>
	                        </div>
	                    </div>
	                </div>
	                <div class="row">
	                    <div class="form-actions floatRight">
	                        <input type="submit"  value="{{!ctrl.queryValue ? 'query' : 'query'}}" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid || myForm.$pristine">
	                        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
	                    </div>
	                </div>
	            </form>
    	    </div>
		</div>	
    </div>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of Users </span></div>
		<div class="panel-body">
			<div class="table-responsive">
            <table class="table table-striped table-condensed table-hover">

                <thead>
                     <tr>
                        <th class="" ng-repeat="(header, value) in ctrl.notSorted(ctrl.itemresul)">
                            {{value}}
                    </tr>
                </thead>
                <tfoot>
                    <td colspan="6">
                        <div class="pagination pull-right">
                            <ul>
                                <li ng-class="{disabled: ctrl.currentPage == 0}">
                                    <a href ng-click="ctrl.prevPage()">« Prev</a>
                                </li>
                            
                                <li ng-repeat="n in range(ctrl.pagedItems.length, ctrl.currentPage, ctrl.currentPage + ctrl.gap) "
                                    ng-class="{active: n == ctrl.currentPage}"
                                ng-click="ctrl.setPage()">
                                    <a href ng-bind="n + 1">1</a>
                                </li>
                             
                                <li ng-class="{disabled: (ctrl.currentPage) == ctrl.pagedItems.length - 1}">
                                    <a href ng-click="ctrl.nextPage()">Next »</a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tfoot>
                <pre>ctrl.pagedItems.length: {{ctrl.pagedItems.length|json}}</pre>
                <pre>ctrl.currentPage: {{ctrl.currentPage|json}}</pre>
                <pre>ctrl.currentPage: {{ctrl.sort|json}}</pre>
                <tbody>
                    <tr ng-repeat="row in ctrl.pagedItems[ctrl.currentPage] | orderBy:ctrl.sort.sortingOrder:ctrl.sort.reverse">
                    <td ng-repeat="cell in ctrl.notSorted(row)" ng-init="valuea = row[cell]">
                        {{valuea}}
                    </td>
                </tr>
                </tbody>
            </table>		
			</div>
		</div>
    </div>
</div>