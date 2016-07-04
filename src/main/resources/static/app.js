angular.module('registrationApp', ['ngAnimate', 'toastr'])
    .config(function (toastrConfig) {
        angular.extend(toastrConfig, {
            positionClass: 'toast-top-center'
        });
    })
    .controller('RegisterCtrl', function ($scope, $http, toastr) {
        $scope.errors = {};
        $scope.user = user();


        function register() {
            var user = $scope.user;
            $http.post('/api/v1/register', {
                    login: user.login,
                    password: user.password,
                    passwordAgain: user.passwordAgain,
                    coupon: user.coupon,
                    promotionPolicyAccepted: user.promotionPolicyAccepted
                })
                .success(function () {
                    $scope.errors = {};
                    toastr.success('Registered');
                    $scope.reset();
                })
                .error(function (data) {
                    $scope.errors = {};

                    data.errors.forEach(function (error) {
                        $scope.errors[error.path] = error.message;
                    });
                })
                .finally(function () {
                    $scope.submitted = false;
                });
        }

        $scope.register = function () {
            register(true);
        };

        $scope.reset = function () {
            $scope.errors = {};
            $scope.user = user();
        };

        $scope.hasError = function (fieldName) {
            return $scope.errors[fieldName];
        };

        function user() {
            return {
                login: "",
                password: "",
                passwordAgain: "",
                coupon: "",
                promotionPolicyAccepted: false

            };
        }

    });