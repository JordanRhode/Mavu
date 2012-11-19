<?php //conn.php
	define("SQL_HOST", "pdb7.your-hosting.net");
	define("SQL_USER", "888609_6438");
	define("SQL_PASS", "saladFootBark");
	define("SQL_DB", "888609_6438");
        

	$conn = mysql_connect(SQL_HOST, SQL_USER, SQL_PASS) or 
		die("Couldn't connect to the database: " . mysql_error());
	mysql_select_db(SQL_DB, $conn) or 
		die("Couldn't select the database: " . mysql_error());
                
?>