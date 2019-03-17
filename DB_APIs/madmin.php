<?php
$admin = $_GET["admin"];    // Gets custom message from GET request
if(isset($_GET["msg"])) {   // If GET request is not set, sets it to empty string
    $msg = $_GET["msg"];
} else {
    $msg = '';
}

// Start of database connection (updates admin message)
$db_conn = new mysqli('localhost', 'rwp', 'LZ.sNnClF.KBbHmH46', 'rwp');
$db_sql = "UPDATE admin SET admin=".$admin.", msg='".$msg."' WHERE id=1";
$db_conn->query($db_sql);
// End of Database connection

header("Location: https://ziemni.me/rwp/moodle/my/index.php");  // Redirects to main moodle page
die();
?>