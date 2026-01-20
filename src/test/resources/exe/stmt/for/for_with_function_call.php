<?php
function getLimit() {
    return 3;
}

for ($i = 0; $i < getLimit(); $i++) {
    echo $i . "\n";
}
?>