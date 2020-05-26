<?php
if(isset($_POST['upload']))
{
    $address="localhost";$port=40;

    foreach ($_FILES['userfile']['error'] as $key => $error)
    {
        $filename = $_FILES["userfile"]["name"][$key];
        $ext = end((explode(".", $filename))); # extra () to prevent notice
        
        if($filename == null){
            
            echo '<script language="javascript">';
            echo 'alert("You need upload two files.");';
            echo 'window.location.href="http://144.6.227.101/";';
            echo '</script>';
        }
        
        if($key == 0)
            if($ext != "jar" && $ext != "py"){
                echo '<script language="javascript">';
                echo 'alert("Only .jar and .py allowed on Access Files.");';
                echo 'window.location.href="http://144.6.227.101/";';
                echo '</script>';
            }
    }
    
    
    if(! $socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'))){
        $this->showError("socket create");
    }
    else{
            //establish connection
            if( !socket_connect($socket, $address, $port)){
                 echo "Connection Problem";
            }else{

                echo "File Uploading to Server<br>";

                socket_write($socket, "Upload\r\n" , strlen ("Upload\r\n"));

                while(!$passcode = socket_read($socket, 2048)){

                }
                $passcode = filter_var($passcode, FILTER_SANITIZE_NUMBER_INT);

                $uploaddir = 'files/'.$passcode.'/';

                echo "Your passcode is $passcode<br>";

                $oldmask = umask(0);

                if (!mkdir("/var/www/html/".$uploaddir, 0777, true)) {
                        echo 'Failed to create folders...';
                }

                umask($oldmask);

                foreach ($_FILES['userfile']['error'] as $key => $error)
                {
                    if ($error == UPLOAD_ERR_OK)
                    {


                        $filename = $_FILES["userfile"]["name"][$key];
                        $ext = end((explode(".", $filename))); # extra () to prevent notice

                        $tmp_name = $_FILES['userfile']['tmp_name'][$key];

                        $name = "$filename";
                        $uploadfile = $uploaddir . basename($name);



                        if (move_uploaded_file($tmp_name, $uploadfile)){
                            socket_write($socket, $name."\r\n", strlen ("$name\r\n"));

                            if(!$reponse = socket_read($socket, 2048, PHP_NORMAL_READ)){
                                echo "File Upload Error";
                            }

                            echo "File " . $name . " upload successful.<br>";
                        }
                        else
                        {
                            echo "Error: File ".$name." cannot be uploaded.<br>";
                        }
                    }




                }

                $date = $_POST["requireDate"];

                socket_write($socket, "DateTest\r\n", strlen ("DateTest\r\n"));


            }
    }
}
?>
