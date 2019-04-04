<?php
echo 'User IP - '.$_SERVER['REMOTE_ADDR'];

$ip = $_SERVER['REMOTE_ADDR'];
if(preg_match("/194\.66\.(3[2-9]|[4-5][0-9]|6[0-3])\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))/", $ip)){
     echo "\r\n Connected to Coventry University";
}else{
     echo "\r\n External Internet Connection";
}
