<?php
class block_madmin_qrgen extends block_base {
    public function init() {
        $this->title = get_string('madmin_qrgen', 'block_madmin_qrgen');  // Sets title of plugin
    }
    
    public function get_content() {
        global $USER;                   // Gets moodle user data

        if ($this->content !== null) {
          return $this->content;        // Sets plugin content
        }

        // Start of plugin's HTML code
        // This code sets administartor's form to communicate with admin API
        $text='
        <center>
        <form action="http://51.68.136.156/rwp/madmin.php" method="get">
        <input type="hidden" id="admin" name="admin" value="1">
            <input list="msg" name="msg">
                <datalist id="msg">
                    <option value="QR System is temporarily disabled by administrator">
                    <option value="QR System is undergoing maintenance">
                    <option value="Database is undergoing maintenance">
                </datalist><br><br>
            <input type="submit" value="Submit">
        </form>
        or<br><br>
        <form action="http://51.68.136.156/rwp/madmin.php" method="get">
            <input type="hidden" id="admin" name="admin" value="0">
            <input type="submit" value="Enable QR System">
        </form>
        </center>
        ';
        // End of plugin's HTML code

        $this->content         = new stdClass;  // Sets content class
        $this->content->text   = $text;         // Sets plugin's HTML code as content
     
        return $this->content;
    }
}