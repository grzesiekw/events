var eventsApp = angular.module('eventsApp', []);

eventsApp.controller('EventsCtrl', function ($scope, $http) {

    $scope.eventKeys = [
        {key: "", value: ""}
    ];

    $scope.events = [];

    $scope.addEventKey = function () {
        $scope.eventKeys.push({key: "", value: ""});
    };

    $scope.removeEventKey = function (index) {
        $scope.eventKeys.splice(index, 1);
    };

    $scope.findEvents = function () {
        loadEvents();
    };

    function loadEvents() {
        $http.post('/find', $scope.eventKeys).success(function (data) {
            $scope.events = data;
        });
    }
});
