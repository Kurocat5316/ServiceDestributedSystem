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

                socket_write($socket, "Password\r\n" , strlen ("Password\r\n"));

                if(!$reponse = socket_read($socket, 2048, PHP_NORMAL_READ)){
                    echo "Out of Connection<br>";
                }

                socket_write($socket, "$passcode\r\n" , strlen ("$passcode\r\n"));

                if($reponse1 = socket_read($socket, 2048, PHP_NORMAL_READ)){
                    echo "Your Application is $reponse1<br>";
                }

                 socket_write($socket, "Got\r\n" , strlen ("Got\r\n"));

                if($reponse = socket_read($socket, 2048, PHP_NORMAL_READ)){

                }

            }
    }


    if(strpos($reponse1, 'finished') !== false){
        echo "The task consume $reponse dollars<br>";
        echo "<br><button onclick=\"location.href='/files/$passcode/output.txt'\" >DownLoad Finish Work</button>";
        }
    else
        echo "<br><button disabled>DownLoad Finish Work</button>";


}
?>
