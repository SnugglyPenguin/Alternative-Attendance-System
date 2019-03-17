<?php
date_default_timezone_set("Europe/London");                         // Defines default timezone

$hash = $_GET["qr"];                                                // Sets up variable with qr hash from GET request
$scannerID = $_GET["sid"];                                          // Sets up variable with scanner id from GET request
$current_time = date("Y-m-d H:i:s");                                // Sets up variable with current time
$error = 0;

if(!preg_match('/^[a-f0-9]{32}$/', $hash)) {                        // Sets up error if hash doeas not meet regex
    $error = 1;
}
if(is_int($scannerID)) {                                            // Sets up error if scanner id is not an integer
    $error = 2;
}

// Start of database connection
// Here code will connect to main database, confirm if hash is correct, if it was scanner within time limit and if everything is correct pass scan information to diffrent table
$db_conn = new mysqli('localhost', 'rwp', 'LZ.sNnClF.KBbHmH46', 'rwp');
if ($db_conn->connect_error) {                                      // Sets up error if there was any problem with connection to database
    $error = 3;
}
$db_stmt = $db_conn->prepare("SELECT * FROM GeneratedHashes WHERE AuthHash = ?");
$db_stmt->bind_param("s", $hash);
$db_stmt->execute();
$db_stmt->store_result();

if ($db_stmt->num_rows > 0) {                                       // Check if hash exist in database
    $db_stmt->bind_result($studentID, $auth_hash, $date);
    $db_stmt->fetch();
    if ((time()-3600) > strtotime($date)) {                           // Check if scan was within 60 seconds from generation of qr
        $error = 5;
        $db_stmt = $db_conn->prepare("DELETE FROM GeneratedHashes WHERE AuthHash = ?");
        $db_stmt->bind_param("s", $hash);
        $db_stmt->execute();
    } else {                                                        // If everything went correctly deletes information with hash and passes scan information to diffrent table in database
        $db_stmt = $db_conn->prepare("INSERT INTO ConfirmedScans (StudentID, ScannerID, Date) VALUES (?, ?, ?)");
        $db_stmt->bind_param("iss", $studentID, $scannerID, $current_time);
        $db_stmt->execute();
        $db_stmt = $db_conn->prepare("DELETE FROM GeneratedHashes WHERE AuthHash = ?");
        $db_stmt->bind_param("s", $hash);
        $db_stmt->execute();
    }
} else {                                                            // Sets up error QR is not in database
    if($error != 1)
      $error = 4;
}
$db_stmt->close();
// End of database connection

// Start of generation of json output
// Here code will generate easy to read for JavaScript output
$output_data = array('error' => $error, 'sid' => $scannerID);
header('Content-type: text/javascript');
echo json_encode($output_data, JSON_PRETTY_PRINT);
// End of generation of json output

// Error detection codes:
// 0 - No Error
// 1 - Given QR codes does not meet regex
// 2 - Given scanner id is not an integer
// 3 - There was an error while connecting to database
// 4 - QR does not exist in database
// 5 - QR expired
?>