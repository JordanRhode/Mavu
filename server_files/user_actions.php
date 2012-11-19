<?php //user_actions.php
	require_once "conn.php";
	$salt = "1654asdf1e3avd5";

	if(isset($_REQUEST["action"]))
	{
		switch ($_REQUEST["action"]) {
			case "Login":
				if(isset($_REQUEST["email"])
					and isset($_REQUEST["password"]))
				{
					$password = sha1($_REQUEST["password"] . $salt);

					$sql = "SELECT account_id, name " .
							"FROM mavu_account " .
							"WHERE email='" . $_REQUEST["email"] . "' " .
							"AND password='" . $password . "'";
					$result = mysql_query($sql, $conn)
						or die("Couldn't look up user information: " . mysql_error());
					if($row = mysql_fetch_array($result))
					{
						session_start();
						$_SESSION["account_id"] = $row["account_id"];
						$_SESSION["name"] = $row["name"];
					}
				}
				break;
			case "Logout":
				session_start();
				session_unset();
				session_destroy();
				break;
			case "Create Account":
			    if(isset($_REQUEST["fname"])
			    	and isset($_REQUEST["lname"])
					and isset($_REQUEST["email"])
					and isset($_REQUEST["password"])
			    	and isset($_REQUEST["dob"]))
			    {
			        $password = sha1($_REQUEST["password"] . $salt);

			     	$sql = "INSERT INTO mavu_account(first_name, last_name, email, password, dob) " .
			     			"VALUES('" . $_REQUEST["fname"] . "', '" . $_REQUEST["lname"] . "', '" . 
			     			 $_REQUEST["email"] . "', '" . $password . "', '" . $_REQUEST["dob"] . "')";
			        mysql_query($sql, $conn) 
			        	or die("Couldn't create user account: " . mysql_error());
			        session_start();
			        $_SESSION["account_id"] = mysql_insert_id($conn);
			        $_SESSION["name"] = $_REQUEST["fname"];
			    }
				break;
			case "Modify Account":
				if(isset($_REQUEST["fname"])
			    	and isset($_REQUEST["lname"])
					and isset($_REQUEST["email"])
					and isset($_REQUEST["password"])
			    	and isset($_REQUEST["dob"]))
			    {
			        $password = sha1($_REQUEST["password"] . $salt);

			     	$sql = "UPDATE mavu_account " .
			     			"SET first_name='" . $_REQUEST["fname"] .
			     			"', last_name='" . $_REQUEST["lname"] .
			     			"', email='" . $_REQUEST["email"] .
			     			"', password='" . $password .
			     			"', dob='" . $_REQUEST["dob"] . " " .
			     			"WHERE account_id=" . $_SESSION["account_id"];
			        mysql_query($sql, $conn) 
			        	or die("Couldn't update user account: " . mysql_error());
			    }
				break;
			case "Create Post":
					if(isset($_REQUEST["title"])
						and isset($_REQUEST["description"])
						and isset($_REQUEST["category"])
						and isset($_REQUEST["city"])
						and isset($_REQUEST["time"])
						and isset($_REQUEST["date"])
						and isset($_REQUEST["address"])
						and isset($_REQUEST["zipcode"]))
					{
						$sql = "INSERT INTO mavu_post(account_id, title, description, category, city, time, date, address, zipcode) " .
								"VALUES('" . $_SESSION["account_id"] . "', '" . $_REQUEST["title"] . "', '" . $_REQUEST["description"] . 
									"', '" . $_REQUEST["category"] . "', '" . $_REQUEST["city"] . "', '" . $_REQUEST["time"] .
									"', '" . $_REQUEST["date"] . "', '" . $_REQUEST["address"] . "', '" . $_REQUEST["zipcode"] . "')";
						mysql_query($sql, $conn)
								or die("Couldn't create post: " + mysql_error());
					}
					break;
		}
		mysql_close($conn);
	}