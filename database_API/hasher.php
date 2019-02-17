<?php
date_default_timezone_set("Europe/London");                         // Defines default timezone

$studentID = $_GET["sid"];                                          // Sets up variable with student id from GET request
$current_time = date("Y-m-d H:i:s");                                // Sets up variable with current time
$crypto_secure = TRUE;
$error = 0;
$random_string = openssl_random_pseudo_bytes(64, $crypto_secure);   // Creates cryptographically secure random string 
$pre_hash_string = $studentID.$current_time.$random_string;
$hash = hash('md5', $pre_hash_string);                              // Hashes combination of student id, current date and secure random string. This will be used as a data for qr code

if(is_int($studentID)) {                                            // Sets up error if student id is not in right format
    $error = 1;
}

if(!preg_match('/^[a-f0-9]{32}$/', $hash)) {                        // Sets up error if hash was not created successfully
    $error = 2;
}

// Start of database connection
// Here code will connect to main database and pass information about generation of hash
$db_conn = new mysqli('localhost', 'rwp', 'password', 'rwp');
if ($db_conn->connect_error) {                                      // Sets up error if there was any problem with connection to database
    $error = 3;
}
$db_stmt = $db_conn->prepare("INSERT INTO GeneratedHashes (StudentID, AuthHash, Date) VALUES (?, ?, ?)");
$db_stmt->bind_param("iss", $studentID, $hash, $current_time);
$db_stmt->execute();
$db_stmt->close();
// End of database connection

// Start of generation of json output
// Here code will generate easy to read for JavaScript output
$output_data = array('error' => $error, 'hash' => $hash, 'sid' => $studentID);
header('Content-type: text/javascript');
echo json_encode($output_data, JSON_PRETTY_PRINT);
// End of generation of json output

// Error detection codes:
// 0 - No Error
// 1 - Given student id is not in the right format
// 2 - There was an error while creating hash
// 3 - There was an error while connecting to database
?>