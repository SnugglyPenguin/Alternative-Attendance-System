<?php
// Starts database connection (check if admin message is set)
$db_conn = new mysqli('localhost', 'rwp', 'LZ.sNnClF.KBbHmH46', 'rwp');
$db_sql = "SELECT * FROM admin WHERE id=1";
$db_result = $db_conn->query($db_sql);
$row = $db_result->fetch_assoc();
// End of database connection

$admin = $row['admin'];
$msg = $row['msg'];

$output_data = array('admin' => $admin, 'mesg' => $msg);    // Outputs admin message in JSON format
header('Content-type: text/javascript');
echo json_encode($output_data, JSON_PRETTY_PRINT);
?>