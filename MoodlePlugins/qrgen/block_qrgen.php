<?php
class block_qrgen extends block_base {
    public function init() {
        $this->title = get_string('qrgen', 'block_qrgen');  // Sets title of plugin
    }
    
    public function get_content() {
        global $USER;                   // Gets moodle user data

        if ($this->content !== null) {
          return $this->content;        // Sets plugin content
        }
     
        $json = file_get_contents('http://51.68.136.156/rwp/admin.php');    // Gets data from admin api
        $data = json_decode($json);

        if($data->admin != 0) {     // Checks if admin message needs to be set
            $mesg = "<center>".$data->mesg."</center>";
        } else {
            $json = file_get_contents('http://51.68.136.156/rwp/hasher.php?sid='.$USER->username);
            $data = json_decode($json);
            $mesg = '<img style="width: 100%; height: 100%; margin: auto; display: block; text-align: center;" src="https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl='.$data->hash.'">';
        }

        // Start of plugin's HTML code
        $text = '
        <div id="qrcode" style="width: 100%; height: 100%">'.$mesg.'<br>
        <center><span id="qrgen_time">EXPIRED!</span></center>
        </div>
        <button onclick="location.reload();" style="margin: auto; display: block;">Generate new QR code</button>
        <script>
        function startTimer(duration, display) {
            var timer = duration, minutes, seconds;
            setInterval(function () {
                minutes = parseInt(timer / 60, 10)
                seconds = parseInt(timer % 60, 10);
        
                minutes = minutes < 10 ? "0" + minutes : minutes;
                seconds = seconds < 10 ? "0" + seconds : seconds;
        
                display.textContent = minutes + ":" + seconds;
        
                if (--timer < 0) {
                    timer = duration;
                }
            }, 1000);
        }
        
        window.onload = function () {
            var minute = 60,
                display = document.querySelector("#qrgen_time");
            startTimer(minute, display);
        };
        </script>
        ';
        // End of plugin's HTML code

        $this->content         = new stdClass;  // Sets content class
        $this->content->text   = $text;         // Sets plugin's HTML code as content
        return $this->content;
    }
}