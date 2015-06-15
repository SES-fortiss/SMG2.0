<?php
        $ip = $_GET["ip"];
        $status = $_GET["status"];      
        $output = shell_exec('sh /home/hexanode/devicecontroller.sh' . " $ip" . " $status" ); 
?>
