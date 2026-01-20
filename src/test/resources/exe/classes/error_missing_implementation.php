<?php
interface Required {
    public function requiredMethod();
}

class MissingImpl implements Required {
    // Не реализован requiredMethod
}
?>