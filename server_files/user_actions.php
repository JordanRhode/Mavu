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
						$output[] = $row;
						print(json_encode($output));
					}
				}
				break;
			case "Create Account":
			    if(isset($_REQUEST["fname"])
			    	and isset($_REQUEST["lname"])
					and isset($_REQUEST["email"])
					and isset($_REQUEST["password"])
			    	and isset($_REQUEST["dob"]))
			    {
			    	$sql = "SELECT account_id " .
			    			"FROM mavu_account " .
			    			"WHERE email='" . $_REQUEST["email"] . "'";
			    	$result = mysql_query($sql, $conn);
			    	if(mysql_fetch_array($result)){
			    		$bus = array(
                                    'insert' => 'duplicate'
                                    );
			    	} else {
			    		$userID = uniqid();
				        $password = sha1($_REQUEST["password"] . $salt);

				     	$sql = "INSERT INTO mavu_account(account_id, first_name, last_name, email, password, dob) " .
				     			"VALUES('" . $userID . "', '" . $_REQUEST["fname"] . "', '" . $_REQUEST["lname"] . "', '" . 
				     			 $_REQUEST["email"] . "', '" . $password . "', '" . $_REQUEST["dob"] . "')";
						$result = mysql_query($sql, $conn);
						if($result){
							$bus = array(
                                    'insert' => 'success',
                                    'accountID' => $userID
                                    );
						} else {
							$bus = array(
                                    'insert' => 'fail'
                                    );
						}
			    	}
			    	$output = array();
			    	array_push($output, $bus);
			    	print(json_encode($output));
			    }
				break;
			case "Update Account":
				if(isset($_REQUEST["fname"])
			    	and isset($_REQUEST["lname"])
					and isset($_REQUEST["email"])
					and isset($_REQUEST["password"])
			    	and isset($_REQUEST["dob"])
			    	and isset($_REQUEST["account_id"]))
			    {
			        $password = sha1($_REQUEST["password"] . $salt);

			     	$sql = "UPDATE mavu_account " .
			     			"SET first_name='" . $_REQUEST["fname"] .
			     			"', last_name='" . $_REQUEST["lname"] .
			     			"', email='" . $_REQUEST["email"] .
			     			"', password='" . $password .
			     			"', dob='" . $_REQUEST["dob"] . "' " .
			     			"WHERE account_id='" . $_REQUEST["account_id"] . "'";
			        $result = mysql_query($sql, $conn);
			        if($result){
							$bus = array(
                                    'update' => 'success',
                                    );
						} else {
							$bus = array(
                                    'update' => 'fail'
                                    );
						}
						$output = array();
			    	array_push($output, $bus);
			    	print(json_encode($output));
			    }
				break;
			case "Get Account":
				if(isset($_REQUEST["account_id"])){
					$sql = "SELECT first_name, last_name, email, dob, likes, dislikes " .
							"FROM mavu_account " .
							"WHERE account_id='" . $_REQUEST["account_id"] . "'";
					$result = mysql_query($sql, $conn) or die(mysql_error());
					$output = array();
					while($row = mysql_fetch_array($result))
					{
						$bus = array(
                                    'first_name' => $row['first_name'],
                                    'last_name' => $row['last_name'],
                                    'email' => $row['email'],
                                    'dob' => $row['dob'],
                                    'likes' => $row['likes'],
                                    'dislikes' => $row['dislikes']
                                );
                        array_push($output, $bus);
                    }
                    print(json_encode($output));
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
									"', '" . $_REQUEST["category"] . "', '" . strtoupper(($_REQUEST["city"])) . "', '" . $_REQUEST["time"] .
									"', '" . $_REQUEST["date"] . "', '" . $_REQUEST["address"] . "', '" . $_REQUEST["zipcode"] . "')";
						mysql_query($sql, $conn)
								or die("Couldn't create post: " + mysql_error());
					}
					break;
			case "Get Posts":
                        if(isset($_REQUEST["lowDate"])
					and isset($_REQUEST["highDate"])
					and isset($_REQUEST["city"])
					and isset($_REQUEST["music"])
					and isset($_REQUEST["business"])
					and isset($_REQUEST["food"]))
				{
                                     
					$sql = "SELECT post_id, title, description, category, city, time, date, address " .
					"FROM mavu_post " .
					"WHERE date BETWEEN '" . $_REQUEST["lowDate"] . "' and '" . $_REQUEST["highDate"] . "' " .
					"AND city='" . strtoupper($_REQUEST["city"]) . "' ";
                                        //echo $sql;
					if($_REQUEST["music"] == "true" && $_REQUEST["business"] == "true" && $_REQUEST["food"] == "true")
					{
						$category = " AND category IN ('music', 'business', 'food') ";
					}
					else if($_REQUEST["music"] == "true" && $_REQUEST["business"] == "false" && $_REQUEST["food"] == "true")
					{
						$category = " AND category IN ('music', 'food') ";
					}
					else if($_REQUEST["music"] == "true" && $_REQUEST["business"] == "false" && $_REQUEST["food"] == "false")
					{
						$category = " AND category IN ('music') ";
					}
					else if($_REQUEST["music"] == "true" && $_REQUEST["business"] == "true" && $_REQUEST["food"] == "false")
					{
						$category = " AND category IN ('music', 'business') ";
					}
					else if($_REQUEST["music"] == "false" && $_REQUEST["business"] == "true" && $_REQUEST["food"] == "true")
					{
						$category = " AND category IN ('business', 'food') ";
					}
					else if($_REQUEST["music"] == "false" && $_REQUEST["business"] == "true" && $_REQUEST["food"] == "false")
					{
						$category = " AND category IN ('business') ";
					}
					else if($_REQUEST["music"] == "false" && $_REQUEST["business"] == "false" && $_REQUEST["food"] == "true")
					{
						$category = " AND category IN ('food')";
					}
					$sql = $sql . $category;
                                        
					if(isset($_REQUEST["title"])){
						$sql = $sql . " AND title='" . $_REQUEST["title"] . "'";
					}
                                        
					$result = mysql_query($sql, $conn) or die(mysql_error());
					$output = array();
					while($row = mysql_fetch_array($result))
					{
						$bus = array(
                                                        'post_id' => $row['post_id'],
                                                        'title' => $row['title'],
                                                        'description' => $row['description'],
                                                        'category' => $row['category'],
                                                        'city' => $row['city'],
                                                        'time' => $row['time'],
                                                        'date' => $row['date'],
                                                        'address' => $row['address']
                                                        ']
                                                    );
                                                array_push($output, $bus);
                                        }
                                        print(json_encode($output));
                                }
			mysql_close();
			break;
		}
		mysql_close();
	}

	?>