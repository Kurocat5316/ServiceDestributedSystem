<?php
$passcode = $_POST['passcode'];



if(isset($_POST['upload']))
{


    $address="localhost";$port=40;

    if(! $socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'))){
        $this->showError("socket create");
    }
    else{
            //establish connection
            if( !socket_connect($socket, $address, $port)){
                 echo "Connection Problem";
            }else{

                socket_write($socket, "CancelWork\r\n" , strlen ("CancelWork\r\n"));

                if(!$reponse = socket_read($socket, 2048, PHP_NORMAL_READ)){
                    echo "Out of Connection<br>";
                }

                socket_write($socket, "$passcode\r\n" , strlen ("$passcode\r\n"));

                if($reponse = socket_read($socket, 2048, PHP_NORMAL_READ)){
                    echo "Your Work Have Been Cancel";
                }
            }
    }


}
?>
