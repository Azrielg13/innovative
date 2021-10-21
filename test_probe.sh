curl --request POST --header "content-type:application/json" --data '{"message":"hello world"}' https://ip360-179401.appspot.com/_api/echo/v1/echo?n=5
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/echo/v1/hello?n=3
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/nurses/v1/_?pageSize=5\&pageToken=5
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/users/v1/_?pageSize=5
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/users/v1/_?filter=username%3Deddiemay
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/users/v1/_?filter=email%3Deddiemay@gmail.com
curl --request POST --header "content-type:application/json" --data '{"username":"eddiemay","password":"6B7DE1B846CC2A047CE71E1214C3B6F7"}' https://ip360-179401.appspot.com/_api/users/v1/login
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/notifications/v1/_?endDate=1630738800000\&startDate=1627801200000
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/generalDatas/v1/_?pageSize=5
curl --request GET --header "content-type:application/json" https://ip360-179401.appspot.com/_api/users/v1/activeSession?idToken=916124632